package tiled.mapeditor.util.cutter;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @version $Id$
 */
public class BorderTileCutter implements TileCutter {

    public String getName() {
        return "Border";
    }

    public void setImage(BufferedImage image) {
    }

    public Image getNextTile() {
        return null;
    }

    public void reset() {
    }

    public Dimension getTileDimensions() {
        return null;
    }
}
