package net.minecraft.src;

public class BlockItemRedstoneWirelessR extends BlockItemRedstoneWireless {

    public BlockItemRedstoneWirelessR(int par1) {
        super(par1);
    }

    @Override
    protected int getRedstoneWirelessItemIndex() {
        return mod_WirelessRedstone.spriteRItem;
    }
}
