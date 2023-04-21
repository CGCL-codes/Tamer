package net.minecraft.src;

import java.util.Random;

public interface BlockRedstoneWirelessOverride {

    public void afterBlockRedstoneWirelessAdded(World world, int i, int j, int k);

    public void afterBlockRedstoneWirelessRemoved(World world, int i, int j, int k);

    public boolean beforeBlockRedstoneWirelessActivated(World world, int i, int j, int k, EntityPlayer entityplayer);

    public void afterBlockRedstoneWirelessActivated(World world, int i, int j, int k, EntityPlayer entityplayer);

    public void afterBlockRedstoneWirelessNeighborChange(World world, int i, int j, int k, int l);

    public boolean beforeUpdateRedstoneWirelessTick(World world, int i, int j, int k, Random random);
}
