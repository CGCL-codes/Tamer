package net.cryff.map;

import java.awt.*;

/**
 * The <code>Tile</code> class 
 * stores the Information about one Tile
 * of the complete TileMap. <br>
 * It has the Image Data that is needed
 * to paint the map and also the String
 * data used to parse the map to a file. 
 * 
 * @author Nino Wagensonner
 * @version 1.0 05/26/2008
 * @since CFF V.0.1r-2
 */
public class Tile {

    private Image imageData;

    private String metaData;

    /**
	 * Default Constructor
	 * @param imageData the Image of the Tile
	 * @param metaData extra data that is needed to save the map into a file. Format ist "0,0" for Tile 0 0
	 */
    public Tile(Image imageData, String metaData) {
        this.imageData = imageData;
        this.metaData = metaData;
    }

    /**
	 * this functions returns the meta data of the tile in the format "x,y"
	 * @return the String data of the Tile
	 */
    public String getData() {
        return this.metaData;
    }

    /**
	 * returns the Image of the Tile
	 * @return an Image object with the Tile
	 */
    public Image getImage() {
        return this.imageData;
    }
}
