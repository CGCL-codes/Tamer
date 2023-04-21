package net.minecraft.src;

public class MaiAlchemy {

    public static void Init() {
        blockalchemytable = new BlockAlchemyTable(BlockAlchemyTable.blockID, TileEntityAlchemyTable.class);
        ModLoader.RegisterBlock(blockalchemytable);
        ModLoader.RegisterTileEntity(TileEntityAlchemyTable.class, "Alchemy Table");
        ModLoader.AddRecipe(new ItemStack(blockalchemytable, 1), new Object[] { "111", "232", "333", Character.valueOf('1'), Item.ingotGold, Character.valueOf('2'), Item.diamond, Character.valueOf('3'), Block.obsidian });
        ElementDust = new ItemElementDust(newItemID());
        ElementDust.iconIndex = ModLoader.addOverride("/gui/items.png", "/Alchemy/Dust.png");
        ElementFire = new ItemElementFire(newItemID());
        ElementFire.iconIndex = ModLoader.addOverride("/gui/items.png", "/Alchemy/Element_Fire.png");
        ElementWater = new ItemElementWater(newItemID());
        ElementWater.iconIndex = ModLoader.addOverride("/gui/items.png", "/Alchemy/Element_Water.png");
        ElementAir = new ItemElementAir(newItemID());
        ElementAir.iconIndex = ModLoader.addOverride("/gui/items.png", "/Alchemy/Element_Air.png");
        ElementEarth = new ItemElementEarth(newItemID());
        ElementEarth.iconIndex = ModLoader.addOverride("/gui/items.png", "/Alchemy/Element_Earth.png");
        ElementLife = new ItemElementLife(newItemID());
        ElementLife.iconIndex = ModLoader.addOverride("/gui/items.png", "/Alchemy/Element_Life.png");
    }

    public static void InitBaseRecipes() {
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.grass), new ItemStack[] { new ItemStack(Block.dirt), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife) }, 120);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.dirt), new ItemStack[] { null, new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth) }, 60);
        MaiAlchemyRecipes.addPairedRecipe(new ItemStack(Block.cobblestone), new ItemStack[] { new ItemStack(Block.dirt), new ItemStack(ElementEarth), null }, 60);
        MaiAlchemyRecipes.addPairedRecipe(new ItemStack(Block.sand), new ItemStack[] { null, new ItemStack(ElementFire), new ItemStack(ElementEarth) }, 60);
        MaiAlchemyRecipes.addPairedRecipe(new ItemStack(Block.gravel), new ItemStack[] { null, new ItemStack(ElementAir), new ItemStack(ElementEarth) }, 60);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.oreGold), new ItemStack[] { new ItemStack(Item.ingotGold), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth) }, 100);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.oreIron), new ItemStack[] { new ItemStack(Item.ingotIron), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth) }, 100);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.oreCoal), new ItemStack[] { new ItemStack(Item.coal), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth) }, 100);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.oreCoal), new ItemStack[] { new ItemStack(Item.coal, 1, 1), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth) }, 100);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.oreLapis), new ItemStack[] { new ItemStack(Item.dyePowder, 1, 4), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth) }, 100);
        MaiAlchemyRecipes.addPairedRecipe(new ItemStack(Block.sandStone), new ItemStack[] { new ItemStack(Block.sand), new ItemStack(ElementFire), new ItemStack(ElementEarth) }, 60);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.web), new ItemStack[] { new ItemStack(Item.silk), new ItemStack(ElementAir), new ItemStack(ElementAir), new ItemStack(ElementAir), new ItemStack(ElementAir) }, 120);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.cobblestoneMossy), new ItemStack[] { new ItemStack(Block.cobblestone), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife) }, 120);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.obsidian), new ItemStack[] { new ItemStack(Item.bucketLava), new ItemStack(ElementWater), new ItemStack(ElementWater), new ItemStack(ElementWater), new ItemStack(ElementWater) }, 200);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.oreDiamond), new ItemStack[] { new ItemStack(Item.diamond), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth) }, 100);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.oreRedstone), new ItemStack[] { new ItemStack(Item.redstone), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth) }, 100);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.ice), new ItemStack[] { new ItemStack(Block.blockSnow), new ItemStack(ElementWater), new ItemStack(ElementWater), new ItemStack(ElementWater), new ItemStack(ElementWater) }, 120);
        MaiAlchemyRecipes.addPairedRecipe(new ItemStack(Block.blockClay), new ItemStack[] { new ItemStack(Block.sand), new ItemStack(ElementWater), new ItemStack(ElementEarth) }, 60);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.netherrack), new ItemStack[] { new ItemStack(ElementLife), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth), new ItemStack(ElementEarth) }, 120);
        MaiAlchemyRecipes.addPairedRecipe(new ItemStack(Block.slowSand), new ItemStack[] { new ItemStack(ElementLife), new ItemStack(ElementFire), new ItemStack(ElementEarth) }, 120);
        MaiAlchemyRecipes.addPairedRecipe(new ItemStack(Block.pumpkinLantern), new ItemStack[] { new ItemStack(Block.pumpkin), new ItemStack(ElementFire), null }, 60);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.stoneBrick, 1, 1), new ItemStack[] { new ItemStack(Block.stoneBrick), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife) }, 120);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.stoneBrick, 1, 2), new ItemStack[] { new ItemStack(Block.stoneBrick), new ItemStack(ElementAir), new ItemStack(ElementAir), new ItemStack(ElementAir), new ItemStack(ElementAir) }, 60);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.mycelium), new ItemStack[] { new ItemStack(Block.mushroomBrown), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife) }, 120);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Block.mycelium), new ItemStack[] { new ItemStack(Block.mushroomRed), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife) }, 120);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Item.coal), new ItemStack[] { null, new ItemStack(ElementFire), new ItemStack(ElementFire), new ItemStack(ElementFire), new ItemStack(ElementFire) }, 60);
        MaiAlchemyRecipes.addPairedRecipe(new ItemStack(Item.coal, 1, 1), new ItemStack[] { new ItemStack(Block.planks), new ItemStack(ElementFire), null }, 100);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Item.feather), new ItemStack[] { null, new ItemStack(ElementAir), new ItemStack(ElementAir), new ItemStack(ElementAir), new ItemStack(ElementAir) }, 60);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Item.bucketWater), new ItemStack[] { new ItemStack(Item.bucketEmpty), new ItemStack(ElementWater), new ItemStack(ElementWater), new ItemStack(ElementWater), new ItemStack(ElementWater) }, 60);
        MaiAlchemyRecipes.addPairedRecipe(new ItemStack(Item.snowball), new ItemStack[] { null, new ItemStack(ElementWater), new ItemStack(ElementAir) }, 60);
        MaiAlchemyRecipes.addStrictRecipe(new ItemStack(Item.dyePowder, 1, 15), new ItemStack[] { null, new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife), new ItemStack(ElementLife) }, 60);
    }

    public static boolean useBaseRecipes() {
        return usebaserecipes;
    }

    public static boolean LoadConfig() {
        return true;
    }

    public static String version() {
        return "r3";
    }

    private static int newItemID() {
        int itemid = nextItemID;
        nextItemID++;
        return itemid;
    }

    public static Block blockalchemytable;

    public static Item ElementDust;

    public static Item ElementFire;

    public static Item ElementWater;

    public static Item ElementAir;

    public static Item ElementEarth;

    public static Item ElementLife;

    private static int nextItemID = 22200;

    private static boolean usebaserecipes = true;
}
