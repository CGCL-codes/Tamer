package net.minecraft.src;

public class ItemChalk extends Item {

    public ItemChalk(int i) {
        super(i);
        maxStackSize = 1;
        setMaxDamage(1000);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        if (world.getBlockId(i, j, k) != Block.snow.blockID) {
            if (l == 0) {
                j--;
            }
            if (l == 1) {
                j++;
            }
            if (l == 2) {
                k--;
            }
            if (l == 3) {
                k++;
            }
            if (l == 4) {
                i--;
            }
            if (l == 5) {
                i++;
            }
            if (!world.isAirBlock(i, j, k)) {
                return false;
            }
        }
        if (itemstack.getItem() == mod_Inscription.chalkWhite) return false; else if (itemstack.getItem() == mod_Inscription.chalkBlack) {
            if (mod_Inscription.chalkDustBlack.canPlaceBlockAt(world, i, j, k)) {
                world.setBlockWithNotify(i, j, k, mod_Inscription.chalkDustBlack.blockID);
                itemstack.damageItem(2, entityplayer);
            }
        } else if (itemstack.getItem() == mod_Inscription.chalkGreen) {
            if (mod_Inscription.chalkDustGreen.canPlaceBlockAt(world, i, j, k)) {
                world.setBlockWithNotify(i, j, k, mod_Inscription.chalkDustGreen.blockID);
                itemstack.damageItem(5, entityplayer);
            }
        } else if (itemstack.getItem() == mod_Inscription.chalkBlue) {
            if (mod_Inscription.chalkDustBlue.canPlaceBlockAt(world, i, j, k)) {
                world.setBlockWithNotify(i, j, k, mod_Inscription.chalkDustBlue.blockID);
                itemstack.damageItem(10, entityplayer);
            }
        } else if (itemstack.getItem() == mod_Inscription.chalkGold) {
            if (mod_Inscription.chalkDustGold.canPlaceBlockAt(world, i, j, k)) {
                world.setBlockWithNotify(i, j, k, mod_Inscription.chalkDustGold.blockID);
                itemstack.damageItem(25, entityplayer);
            }
        } else if (itemstack.getItem() == mod_Inscription.chalkRed) {
            if (mod_Inscription.chalkDustRed.canPlaceBlockAt(world, i, j, k)) {
                world.setBlockWithNotify(i, j, k, mod_Inscription.chalkDustRed.blockID);
                itemstack.damageItem(20, entityplayer);
            }
        }
        return true;
    }

    public String getItemNameIS(ItemStack itemstack) {
        return (new StringBuilder()).append(super.getItemName()).toString();
    }
}
