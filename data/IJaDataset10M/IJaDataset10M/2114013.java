package net.minecraft.src;

public class Itemlinedcrate extends ItemBlock {

    public Itemlinedcrate(int i, Block block) {
        super(i);
        maxStackSize = 16;
        setHasSubtypes(true);
        setItemName("linedcrate");
        setContainerItem(Item.itemsList[mod_hay.elinedcrate.blockID]);
    }

    public String getItemNameIS(ItemStack itemstack) {
        switch(itemstack.getItemDamage()) {
            default:
                return "item.linedcrate.soupcrate";
            case 1:
                return "item.linedcrate.milkcrate";
            case 2:
                return "item.linedcrate.watercrate";
        }
    }

    public int getMetadata(int i) {
        return i;
    }
}
