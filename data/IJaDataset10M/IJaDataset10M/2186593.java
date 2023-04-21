package source.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import source.model.type.*;
import source.events.action.MoveDirection;

/**
 * Loads all the images as BufferedImages and provides lookup abilities
 * based on GameObjects.
 * @author Steve
 *
 */
public abstract class ImageLibrary {

    private static HashMap<TerrainType, BufferedImage> terrainMap;

    private static HashMap<ItemType, BufferedImage> itemMap;

    private static HashMap<String, BufferedImage> unitMap;

    private static BufferedImage defaultBG;

    /**
	 * Initializes the images that will be used in the library.  Should
	 * only be called once upon startup.
	 *
	 */
    public static void init() {
        try {
            unitMap = new HashMap<String, BufferedImage>();
            terrainMap = new HashMap<TerrainType, BufferedImage>();
            itemMap = new HashMap<ItemType, BufferedImage>();
            defaultBG = ImageIO.read(new File("data" + File.separator + "images" + File.separator + "spaceBG.jpg"));
            for (MoveDirection dir : MoveDirection.values()) {
                String s = "data" + File.separator + "images" + File.separator + "units" + File.separator + "ship_" + dir.toString() + ".gif";
                BufferedImage img = ImageIO.read(new File(s));
                unitMap.put("MELEE_" + dir.toString(), img);
            }
            for (MoveDirection dir : MoveDirection.values()) {
                String s = "data" + File.separator + "images" + File.separator + "units" + File.separator + "rng_" + dir.toString() + ".gif";
                BufferedImage img = ImageIO.read(new File(s));
                unitMap.put("RANGED_" + dir.toString(), img);
            }
            for (MoveDirection dir : MoveDirection.values()) {
                String s = "data" + File.separator + "images" + File.separator + "units" + File.separator + "col_" + dir.toString() + ".gif";
                BufferedImage img = ImageIO.read(new File(s));
                unitMap.put("COLONIST_" + dir.toString(), img);
            }
            for (MoveDirection dir : MoveDirection.values()) {
                String s = "data" + File.separator + "images" + File.separator + "units" + File.separator + "exp_" + dir.toString() + ".gif";
                BufferedImage img = ImageIO.read(new File(s));
                unitMap.put("EXPLORER_" + dir.toString(), img);
            }
            for (TerrainType type : TerrainType.values()) {
                try {
                    BufferedImage img = ImageIO.read(new File("data" + File.separator + "images" + File.separator + "terrain" + File.separator + type.toString() + ".gif"));
                    terrainMap.put(type, img);
                } catch (IOException e) {
                    System.out.println("Image Library: Terrains not all loaded");
                }
            }
            try {
                BufferedImage img = ImageIO.read(new File("data" + File.separator + "images" + File.separator + "items" + File.separator + "oreRefinery.gif"));
                itemMap.put(ItemType.DERELICTOREREFINERY, img);
                img = ImageIO.read(new File("data" + File.separator + "images" + File.separator + "items" + File.separator + "comet.gif"));
                itemMap.put(ItemType.COMET, img);
            } catch (IOException e) {
            }
        } catch (IOException e) {
            System.err.println("Error reading images");
        }
    }

    public static BufferedImage getDefaultBackground() {
        return defaultBG;
    }

    public static BufferedImage getUnitImage(UnitType uType, MoveDirection direction) {
        return unitMap.get(uType.toString() + "_" + direction.toString());
    }

    public static BufferedImage getTerrainImage(TerrainType tType) {
        return terrainMap.get(tType);
    }

    public static BufferedImage getItemImage(ItemType iType) {
        return itemMap.get(iType);
    }
}
