package net.minecraft.src;

import java.util.Map;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class mod_Inscription extends BaseMod {

    public static final boolean DEBUG = true;

    public static long time = 0;

    public static long startTime;

    public static final Block gypsumOre;

    public static final Block chalkDustRed;

    public static final Block chalkDustGold;

    public static final Block chalkDustBlue;

    public static final Block chalkDustGreen;

    public static final Block chalkDustBlack;

    public static final Block invisRedTorch;

    public static final Item chalkdustWhite;

    public static final Item chalkdustRed;

    public static final Item chalkdustGold;

    public static final Item chalkdustBlue;

    public static final Item chalkdustGreen;

    public static final Item chalkdustBlack;

    public static final Item chalkWhite;

    public static final Item chalkRed;

    public static final Item chalkGold;

    public static final Item chalkBlue;

    public static final Item chalkGreen;

    public static final Item chalkBlack;

    public static final Item mortarAndPestle;

    public static final Item mortarAndPestleD;

    public static final Item eyeOfInscriptionRed;

    public static final Item eyeOfInscriptionGold;

    public static final Item eyeOfInscriptionBlue;

    public static final Item eyeOfInscriptionGreen;

    public static final Item eyeOfInscriptionBlack;

    public static final Item scepterWood;

    public static final Item scepterStone;

    public static final Item scepterIron;

    public static final Item scepterDiamond;

    public static final Item scepterGold;

    static {
        gypsumOre = (new BlockGypsumOre(120, ModLoader.addOverride("/terrain.png", "/Stormsntides/gypsumBlock.png"))).setHardness(0.5F).setResistance(0.0F).setStepSound(Block.soundStoneFootstep).setBlockName("gypsumOre");
        chalkDustRed = (new BlockChalkDust(121, ModLoader.addOverride("/terrain.png", "/Stormsntides/redChalkDust.png"))).setHardness(0.0F).setResistance(0.0F).setStepSound(Block.soundPowderFootstep).setBlockName("redChalkDust");
        chalkDustGold = (new BlockChalkDust(122, ModLoader.addOverride("/terrain.png", "/Stormsntides/goldChalkDust.png"))).setHardness(0.0F).setResistance(0.0F).setStepSound(Block.soundPowderFootstep).setBlockName("goldChalkDust");
        chalkDustBlue = (new BlockChalkDust(123, ModLoader.addOverride("/terrain.png", "/Stormsntides/blueChalkDust.png"))).setHardness(0.0F).setResistance(0.0F).setStepSound(Block.soundPowderFootstep).setBlockName("blueChalkDust");
        chalkDustGreen = (new BlockChalkDust(124, ModLoader.addOverride("/terrain.png", "/Stormsntides/greenChalkDust.png"))).setHardness(0.0F).setResistance(0.0F).setStepSound(Block.soundPowderFootstep).setBlockName("greenChalkDust");
        chalkDustBlack = (new BlockChalkDust(125, ModLoader.addOverride("/terrain.png", "/Stormsntides/blackChalkDust.png"))).setHardness(0.0F).setResistance(0.0F).setStepSound(Block.soundPowderFootstep).setBlockName("blackChalkDust");
        invisRedTorch = (new BlockInvisibleRedTorch(126, ModLoader.addOverride("/terrain.png", "/Stormsntides/invisRedTorch.png"), true)).setHardness(0.0F).setResistance(0.0F).setStepSound(Block.soundWoodFootstep).setBlockName("invisRedTorch");
        chalkdustWhite = (new ItemDust(6744)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkdustWhite.png")).setItemName("chalkdustWhite");
        chalkdustRed = (new ItemDust(6745)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkdustRed.png")).setItemName("chalkdustRed");
        chalkdustGold = (new ItemDust(6746)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkdustGold.png")).setItemName("chalkdustGold");
        chalkdustBlue = (new ItemDust(6747)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkdustBlue.png")).setItemName("chalkdustBlue");
        chalkdustGreen = (new ItemDust(6748)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkdustGreen.png")).setItemName("chalkdustGreen");
        chalkdustBlack = (new ItemDust(6749)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkdustBlack.png")).setItemName("chalkdustBlack");
        chalkWhite = (new ItemChalk(6750)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkWhite.png")).setItemName("chalkWhite");
        chalkRed = (new ItemChalk(6751)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkRed.png")).setItemName("chalkRed");
        chalkGold = (new ItemChalk(6752)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkGold.png")).setItemName("chalkGold");
        chalkBlue = (new ItemChalk(6753)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkBlue.png")).setItemName("chalkBlue");
        chalkGreen = (new ItemChalk(6754)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkGreen.png")).setItemName("chalkGreen");
        chalkBlack = (new ItemChalk(6755)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/chalkBlack.png")).setItemName("chalkBlack");
        mortarAndPestle = (new Item(6756)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/mortarAndPestle.png")).setItemName("mortarAndPestle");
        mortarAndPestleD = (new Item(6757)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/mortarAndPestleD.png")).setItemName("mortarAndPestleD");
        eyeOfInscriptionRed = (new ItemInscription(6758)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/inscriptionEyeRed.png")).setItemName("eyeOfInscriptionRed");
        eyeOfInscriptionGold = (new ItemInscription(6759)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/inscriptionEyeGold.png")).setItemName("eyeOfInscriptionGold");
        eyeOfInscriptionBlue = (new ItemInscription(6760)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/inscriptionEyeBlue.png")).setItemName("eyeOfInscriptionBlue");
        eyeOfInscriptionGreen = (new ItemInscription(6761)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/inscriptionEyeGreen.png")).setItemName("eyeOfInscriptionGreen");
        eyeOfInscriptionBlack = (new ItemInscription(6762)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/inscriptionEyeBlack.png")).setItemName("eyeOfInscriptionBlack");
        scepterWood = (new ItemScepter(6763, EnumToolMaterial.WOOD, null)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/scepterWood.png")).setItemName("scepterWood");
        scepterStone = (new ItemScepter(6764, EnumToolMaterial.STONE, null)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/scepterStone.png")).setItemName("scepterStone");
        scepterIron = (new ItemScepter(6765, EnumToolMaterial.IRON, null)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/scepterIron.png")).setItemName("scepterIron");
        scepterDiamond = (new ItemScepter(6766, EnumToolMaterial.EMERALD, null)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/scepterDiamond.png")).setItemName("scepterDiamond");
        scepterGold = (new ItemScepter(6767, EnumToolMaterial.GOLD, null)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Stormsntides/scepterGold.png")).setItemName("scepterGold");
    }

    public mod_Inscription() {
        ModLoader.AddName(gypsumOre, "Gypsum Ore");
        ModLoader.AddName(chalkDustRed, "Chalkdust");
        ModLoader.AddName(chalkDustGold, "Chalkdust");
        ModLoader.AddName(chalkDustBlue, "Chalkdust");
        ModLoader.AddName(chalkDustGreen, "Chalkdust");
        ModLoader.AddName(chalkDustBlack, "Chalkdust");
        ModLoader.RegisterBlock(gypsumOre);
        ModLoader.RegisterBlock(chalkDustRed);
        ModLoader.RegisterBlock(chalkDustGold);
        ModLoader.RegisterBlock(chalkDustBlue);
        ModLoader.RegisterBlock(chalkDustGreen);
        ModLoader.RegisterBlock(chalkDustBlack);
        ModLoader.AddName(chalkdustWhite, "White Chalkdust");
        ModLoader.AddName(chalkdustRed, "Red Chalkdust");
        ModLoader.AddName(chalkdustGold, "Gold Chalkdust");
        ModLoader.AddName(chalkdustBlue, "Blue Chalkdust");
        ModLoader.AddName(chalkdustGreen, "Green Chalkdust");
        ModLoader.AddName(chalkdustBlack, "Black Chalkdust");
        ModLoader.AddName(chalkWhite, "White Chalk");
        ModLoader.AddName(chalkRed, "Red Chalk");
        ModLoader.AddName(chalkGold, "Gold Chalk");
        ModLoader.AddName(chalkBlue, "Blue Chalk");
        ModLoader.AddName(chalkGreen, "Green Chalk");
        ModLoader.AddName(chalkBlack, "Black Chalk");
        ModLoader.AddName(mortarAndPestle, "Mortar And Pestle");
        ModLoader.AddName(mortarAndPestleD, "Mortar And Diamond Pestle");
        ModLoader.AddName(eyeOfInscriptionRed, "Red Eye Of Inscription");
        ModLoader.AddName(eyeOfInscriptionGold, "Gold Eye Of Inscription");
        ModLoader.AddName(eyeOfInscriptionBlue, "Blue Eye Of Inscription");
        ModLoader.AddName(eyeOfInscriptionGreen, "Green Eye Of Inscription");
        ModLoader.AddName(eyeOfInscriptionBlack, "Black Eye Of Inscription");
        ModLoader.AddName(scepterWood, "Wooden Scepter");
        ModLoader.AddName(scepterStone, "Stone Scepter");
        ModLoader.AddName(scepterIron, "Iron Scepter");
        ModLoader.AddName(scepterDiamond, "Diamond Scepter");
        ModLoader.AddName(scepterGold, "Gold Scepter");
        readRecipes();
        ModLoader.SetInGameHook(this, true, true);
    }

    @Override
    public boolean OnTickInGame(Minecraft minecraft) {
        onTime(minecraft.thePlayer);
        TeleportManager.onTick(minecraft.theWorld, minecraft.thePlayer);
        return true;
    }

    public void onTime(EntityPlayer ep) {
        if (time == 0) {
            startTime = System.nanoTime();
            time = startTime;
        } else time = System.nanoTime();
        double computedTime = (time - startTime) * 0.000000001;
        TeleportManager.onTime(ep, computedTime, 600.0);
        if (computedTime >= 600.0) time = 0;
    }

    @Override
    public void GenerateSurface(World world, Random random, int i, int j) {
        Random rand = new Random();
        for (int repeatProcess = 0; repeatProcess < 10; repeatProcess++) {
            int randPosX = i + rand.nextInt(16);
            int randPosY = rand.nextInt(32);
            int randPosZ = j + rand.nextInt(16);
            (new WorldGenMinable(gypsumOre.blockID, 10)).generate(world, rand, randPosX, randPosY, randPosZ);
        }
    }

    public void readRecipes() {
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.scepterWood, 1), new Object[] { "MRM", " M ", " S ", Character.valueOf('M'), new ItemStack(Block.planks, 1), Character.valueOf('R'), new ItemStack(Item.redstone, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.scepterStone, 1), new Object[] { "MRM", " M ", " S ", Character.valueOf('M'), new ItemStack(Block.cobblestone, 1), Character.valueOf('R'), new ItemStack(Item.redstone, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.scepterIron, 1), new Object[] { "MRM", " M ", " S ", Character.valueOf('M'), new ItemStack(Item.ingotIron, 1), Character.valueOf('R'), new ItemStack(Item.redstone, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.scepterDiamond, 1), new Object[] { "MRM", " M ", " S ", Character.valueOf('M'), new ItemStack(Item.diamond, 1), Character.valueOf('R'), new ItemStack(Item.redstone, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.scepterGold, 1), new Object[] { "MRM", " M ", " S ", Character.valueOf('M'), new ItemStack(Item.ingotGold, 1), Character.valueOf('R'), new ItemStack(Item.redstone, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.eyeOfInscriptionRed, 1), new Object[] { "MSM", "SMS", "MSM", Character.valueOf('M'), new ItemStack(mod_Inscription.chalkdustRed, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.eyeOfInscriptionGold, 1), new Object[] { "MSM", "SMS", "MSM", Character.valueOf('M'), new ItemStack(mod_Inscription.chalkdustGold, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.eyeOfInscriptionBlue, 1), new Object[] { "MSM", "SMS", "MSM", Character.valueOf('M'), new ItemStack(mod_Inscription.chalkdustBlue, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.eyeOfInscriptionGreen, 1), new Object[] { "MSM", "SMS", "MSM", Character.valueOf('M'), new ItemStack(mod_Inscription.chalkdustGreen, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.eyeOfInscriptionBlack, 1), new Object[] { "MSM", "SMS", "MSM", Character.valueOf('M'), new ItemStack(mod_Inscription.chalkdustBlack, 1), Character.valueOf('S'), new ItemStack(Item.stick, 1) });
        ModLoader.AddShapelessRecipe(new ItemStack(mod_Inscription.chalkdustRed, 1), new Object[] { new ItemStack(mod_Inscription.chalkdustWhite, 1), Item.redstone });
        ModLoader.AddShapelessRecipe(new ItemStack(mod_Inscription.chalkdustGold, 1), new Object[] { Item.ingotGold, mod_Inscription.mortarAndPestle });
        ModLoader.AddShapelessRecipe(new ItemStack(mod_Inscription.chalkdustBlue, 1), new Object[] { new ItemStack(Item.dyePowder, 1, 4), mod_Inscription.mortarAndPestle });
        ModLoader.AddShapelessRecipe(new ItemStack(mod_Inscription.chalkdustGreen, 1), new Object[] { new ItemStack(mod_Inscription.chalkdustWhite, 1), Item.slimeBall, mod_Inscription.mortarAndPestle });
        ModLoader.AddShapelessRecipe(new ItemStack(mod_Inscription.chalkdustBlack, 1), new Object[] { Block.obsidian, mod_Inscription.mortarAndPestleD });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.chalkWhite, 1), new Object[] { "D", "D", "D", Character.valueOf('D'), new ItemStack(mod_Inscription.chalkdustWhite, 1) });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.chalkRed, 1), new Object[] { " D ", "DCD", " D ", Character.valueOf('D'), new ItemStack(mod_Inscription.chalkdustRed, 1), Character.valueOf('C'), mod_Inscription.chalkWhite });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.chalkGold, 1), new Object[] { " D ", "DCD", " D ", Character.valueOf('D'), new ItemStack(mod_Inscription.chalkdustGold, 1), Character.valueOf('C'), mod_Inscription.chalkWhite });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.chalkBlue, 1), new Object[] { " D ", "DCD", " D ", Character.valueOf('D'), new ItemStack(mod_Inscription.chalkdustBlue, 1), Character.valueOf('C'), mod_Inscription.chalkWhite });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.chalkGreen, 1), new Object[] { " D ", "DCD", " D ", Character.valueOf('D'), new ItemStack(mod_Inscription.chalkdustGreen, 1), Character.valueOf('C'), mod_Inscription.chalkWhite });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.chalkBlack, 1), new Object[] { " D ", "DCD", " D ", Character.valueOf('D'), new ItemStack(mod_Inscription.chalkdustBlack, 1), Character.valueOf('C'), mod_Inscription.chalkWhite });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.mortarAndPestle, 4), new Object[] { " # ", "PXP", " P ", Character.valueOf('#'), Item.stick, Character.valueOf('P'), Block.planks, Character.valueOf('X'), Block.cobblestone });
        ModLoader.AddRecipe(new ItemStack(mod_Inscription.mortarAndPestleD, 1), new Object[] { " # ", "PXP", " P ", Character.valueOf('#'), Item.stick, Character.valueOf('P'), Block.planks, Character.valueOf('X'), Item.diamond });
        ModLoader.AddShapelessRecipe(new ItemStack(Item.slimeBall, 1), new Object[] { Block.cactus, Item.bucketWater, mod_Inscription.mortarAndPestle });
    }

    @Override
    public String Version() {
        return "Inscriptions v1.0 by Stormsntides and trunksbomb";
    }

    public static void sendChatMessage(String s) {
        sendChatMessage(s, 'f');
    }

    public static void sendChatMessage(String s, char color) {
        int c = "0123456789abcdef".indexOf(color);
        if (c < 0 || c > 15) {
            color = 'f';
        }
        ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("\247" + color + s);
    }

    public static void debug(String s) {
        if (DEBUG) {
            sendChatMessage(s, '4');
        }
    }
}
