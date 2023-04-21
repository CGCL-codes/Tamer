package jsh.map;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import org.apache.log4j.*;

/**

*/
public class Tile implements Drawable {

    static Logger logger = Logger.getLogger(Tile.class);

    protected BufferedImage image = null;

    protected BufferedImage openImage = null;

    protected BufferedImage blastedImage = null;

    int width, height;

    String filename = null;

    boolean isFloorTile = false;

    boolean isDoorTile = false;

    boolean isEntryTile = false;

    boolean isExitTile = false;

    boolean isWallTile = false;

    boolean isDownTile = false;

    boolean isUpTile = true;

    public void setFloor() {
        isFloorTile = true;
    }

    public boolean isFloor() {
        return isFloorTile;
    }

    public void setDoor() {
        isDoorTile = true;
    }

    public boolean isDoor() {
        return isDoorTile;
    }

    public void setDown() {
        isDownTile = true;
    }

    public boolean isDown() {
        return isDownTile;
    }

    public void setUp() {
        isUpTile = true;
    }

    public boolean isUp() {
        return isUpTile;
    }

    public void setExit() {
        isExitTile = true;
    }

    public boolean isExit() {
        return isExitTile;
    }

    public void setEntry() {
        isEntryTile = true;
    }

    public boolean isEntry() {
        return isEntryTile;
    }

    public void setWall() {
        isWallTile = true;
    }

    public boolean isWall() {
        return isWallTile;
    }

    public boolean canWalkThrough() {
        return isFloorTile || isDoorTile;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setProperties(String pin) {
        if (pin == null || pin.equals("")) {
            return;
        }
        String[] props = pin.split(" ");
        for (int i = 0; i < props.length; i++) {
            String p = props[i];
            if (p.equalsIgnoreCase("floor")) {
                isFloorTile = true;
            }
            if (p.equalsIgnoreCase("door")) {
                isDoorTile = true;
            }
            if (p.equalsIgnoreCase("exit")) {
                isExitTile = true;
            }
            if (p.equalsIgnoreCase("entry")) {
                isEntryTile = true;
            }
            if (p.equalsIgnoreCase("wall")) {
                isWallTile = true;
            }
            if (p.equalsIgnoreCase("up")) {
                isUpTile = true;
            }
            if (p.equalsIgnoreCase("down")) {
                isDownTile = true;
            }
            if (p.startsWith("OpenImage")) {
                try {
                    openImage = ImageUtilities.getImage(p.split("=")[1]);
                } catch (Exception e) {
                }
            }
            if (p.startsWith("BlastedImage")) {
                try {
                    blastedImage = ImageUtilities.getImage(p.split("=")[1]);
                } catch (Exception e2) {
                }
            }
        }
    }

    public Tile() {
        image = null;
    }

    public void setImage(BufferedImage i) {
        image = i;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void loadImage() {
        if (filename == null) {
            return;
        }
        try {
            image = ImageUtilities.getImage(filename);
        } catch (Exception e) {
            logger.error("Failed to find tile: " + filename, e);
        }
    }

    public void setFilename(String f) {
        filename = f;
    }

    public String getFilename() {
        return filename;
    }

    public Tile(String f) {
        filename = f;
        loadImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public void display(Graphics g, int posX, int posY) {
        display(g, posX, posY, true);
    }

    public void display(Graphics g, int posX, int posY, boolean FogOfWar) {
        display(g, posX, posY, Square.Closed);
    }

    public void display(Graphics g, int posX, int posY, int state) {
        BufferedImage i = image;
        if (isDoorTile) {
            if (state == Square.Open) {
                i = openImage;
            }
            if (state == Square.Blasted) {
                i = blastedImage;
            }
        }
        if (i != null) {
            g.drawImage(i, posX, posY, width, height, null);
        }
    }

    public void copyToMap(GameMap map, int posX, int posY, int posZ) {
        map.addToSquare(this, posX, posY, posZ);
    }

    public ImageIcon getIcon() {
        return new ImageIcon(image);
    }
}
