package io.github.jodlodi.minions.network;

import io.github.jodlodi.minions.capabilities.IMasterCapability;
import io.github.jodlodi.minions.orders.MineAheadOrder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MineAheadButtonPacket extends AbstractServerboundPacket {
	protected final boolean paused;
	protected final BlockPos minPos;
	protected final BlockPos maxPos;
	protected final int direction;
	protected final int depth;

	public MineAheadButtonPacket(boolean paused, BlockPos minPos, BlockPos maxPos, Direction direction, int depth) {
		this.paused = paused;
		this.minPos = minPos;
		this.maxPos = maxPos;
		this.direction = direction.ordinal();
		this.depth = depth;
	}

	public MineAheadButtonPacket(FriendlyByteBuf buf) {
		this.paused = buf.readBoolean();
		this.minPos = buf.readBlockPos();
		this.maxPos = buf.readBlockPos();
		this.direction = buf.readInt();
		this.depth = buf.readInt();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(this.paused);
		buf.writeBlockPos(this.minPos);
		buf.writeBlockPos(this.maxPos);
		buf.writeInt(this.direction);
		buf.writeInt(this.depth);
	}

	@Override
	void execute(ServerPlayer player, IMasterCapability capability) {
		capability.setOrder(new MineAheadOrder(this.minPos, this.maxPos, Direction.values()[this.direction], this.depth));
		capability.setPaused(this.paused);
	}
}
