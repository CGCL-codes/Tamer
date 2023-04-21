package net.minecraft.src;

import java.io.Serializable;
import java.util.Random;

public class BlockRedstoneWirelessR extends BlockContainer {

    private boolean initialSchedule;

    protected BlockRedstoneWirelessR(int i) {
        super(i, Material.circuits);
        setTickOnLoad(true);
        initialSchedule = true;
    }

    public void setState(World world, int i, int j, int k, boolean state) {
        int meta = world.getBlockMetadata(i, j, k);
        meta = (meta & 14);
        if (state) {
            meta += 1;
        }
        world.setBlockMetadataWithNotify(i, j, k, meta);
    }

    private boolean getState(World world, int i, int j, int k) {
        int meta = world.getBlockMetadata(i, j, k);
        return getState(meta);
    }

    private boolean getState(int meta) {
        return (meta & 1) == 1;
    }

    public Serializable getFreq(World world, int i, int j, int k) {
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        if (tileentity == null) return -1;
        if (tileentity instanceof TileEntityRedstoneWirelessR) return ((TileEntityRedstoneWirelessR) tileentity).getFreq(); else return 0;
    }

    public void onBlockAdded(World world, int i, int j, int k) {
        if (world.singleplayerWorld) return;
        TileEntityRedstoneWirelessR entity = (TileEntityRedstoneWirelessR) getBlockEntity();
        entity.assBlock(i, j, k);
        world.setBlockTileEntity(i, j, k, entity);
        RedstoneEther.getInstance().addReceiver(world, i, j, k, getFreq(world, i, j, k));
        world.notifyBlocksOfNeighborChange(i, j, k, blockID);
        updateTick(world, i, j, k, null);
    }

    public void onBlockRemoval(World world, int i, int j, int k) {
        if (world.singleplayerWorld) {
            world.removeBlockTileEntity(i, j, k);
            return;
        }
        RedstoneEther.getInstance().remReceiver(world, i, j, k, getFreq(world, i, j, k));
        world.notifyBlocksOfNeighborChange(i, j, k, blockID);
        super.onBlockRemoval(world, i, j, k);
    }

    public int tickRate() {
        return 10;
    }

    public int idDropped(int i, Random random) {
        return mod_WirelessRedstone.rxID;
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if (world.singleplayerWorld) return true;
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        if (tileentity instanceof TileEntityRedstoneWirelessR) PacketHandlerRedstoneWireless.PacketHandlerOutput.sendGuiPacketTo((EntityPlayerMP) entityplayer, (TileEntityRedstoneWirelessR) tileentity);
        return true;
    }

    public void changeFreq(World world, int i, int j, int k, Serializable oldFreq, Serializable freq) {
        RedstoneEther.getInstance().remReceiver(world, i, j, k, oldFreq);
        RedstoneEther.getInstance().addReceiver(world, i, j, k, freq);
        updateTick(world, i, j, k, null);
    }

    public boolean hasTicked() {
        return !this.initialSchedule;
    }

    public void updateTick(World world, int i, int j, int k, Random random) {
        if (initialSchedule) initialSchedule = false;
        if (world == null) return;
        Serializable freq = getFreq(world, i, j, k);
        boolean oldState = getState(world, i, j, k);
        boolean newState = RedstoneEther.getInstance().getFreqState(freq);
        if (newState != oldState) {
            setState(world, i, j, k, newState);
            world.markBlockNeedsUpdate(i, j, k);
            notifyNeighbors(world, i, j, k);
        }
    }

    public void notifyNeighbors(World world, int i, int j, int k) {
        world.notifyBlocksOfNeighborChange(i, j, k, 0);
        world.notifyBlocksOfNeighborChange(i - 1, j, k, 0);
        world.notifyBlocksOfNeighborChange(i + 1, j, k, 0);
        world.notifyBlocksOfNeighborChange(i, j - 1, k, 0);
        world.notifyBlocksOfNeighborChange(i, j + 1, k, 0);
        world.notifyBlocksOfNeighborChange(i, j, k - 1, 0);
        world.notifyBlocksOfNeighborChange(i, j, k + 1, 0);
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        if (world.singleplayerWorld) return;
        updateTick(world, i, j, k, null);
    }

    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (iblockaccess instanceof World) {
            TileEntity entity = ((World) iblockaccess).getBlockTileEntity(i, j, k);
            if (entity instanceof TileEntityRedstoneWireless) {
                return (((TileEntityRedstoneWireless) entity).isPoweringDirection(l) && getState(((World) iblockaccess), i, j, k));
            }
        }
        return false;
    }

    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
        return isPoweringTo(world, i, j, k, l);
    }

    public boolean canProvidePower() {
        return true;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (getState(iblockaccess.getBlockMetadata(i, j, k))) {
            if (l == 0 || l == 1) {
                return mod_WirelessRedstone.spriteTopOn;
            }
            return mod_WirelessRedstone.spriteROn;
        } else {
            return getBlockTextureFromSide(l);
        }
    }

    public int getBlockTextureFromSide(int i) {
        if (i == 0 || i == 1) {
            return mod_WirelessRedstone.spriteTopOff;
        }
        return mod_WirelessRedstone.spriteROff;
    }

    protected TileEntity getBlockEntity() {
        return new TileEntityRedstoneWirelessR();
    }
}
