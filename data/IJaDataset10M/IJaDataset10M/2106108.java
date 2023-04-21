package net.sourceforge.javagg.gsc;

import java.awt.Image;

/**
 * This will define the responsibilities of a game tile. A tile can be a map
 * tile or can be used for other purposes. A tile can be square, or hexegonal. A
 * tile may be transparent or solid, meaning usually white is set as a
 * transparency color therefor the tile can overlay other tiles.
 * 
 * @author Larry Gray
 * @version 1.4
 * 
 * @see <code>TileMap</code> In which an implementation will act as a factory
 *      for tiles.
 * @see <code>MapScreenComponent</code> a screen for displaying tile maps. You
 *      wil add <code>Tiles</code> to the <code>MapScreenComponent</code>.
 */
public interface Tile {

    /**
	 * Sets the tile to be used on a sqaure tile map only.
	 *  
	 */
    public abstract void setSquare();

    /**
	 * Sets the tile to be used on a hex map only.
	 *  
	 */
    public abstract void setHex();

    /**
	 * Determines if the tile is used on a square map.
	 *  
	 */
    public abstract boolean isSquare();

    /**
	 * Determines if the tile is used on a hex map.
	 *  
	 */
    public abstract boolean isHex();

    /**
	 * Sets the tile to transparent meaning that it is inttended to be drawn on
	 * top of other tiles. Also sets the transparent color for the tile, default
	 * is white.
	 */
    public abstract void setTransparent();

    /**
	 * Sets the image object associated with this tile. Be sure to call
	 * <code>super.setImage(Image image)</code>.
	 * 
	 * @param image
	 *            sets the image for this Tile.
	 */
    public abstract void setImage(Image image);

    /**
	 * Sets a descriptive name for this tile.
	 * 
	 * @param name
	 *            a name for the tile.
	 */
    public abstract void setName(String name);

    /**
	 * Sets a unique id for this Tile. Note we may remove this later.
	 * 
	 * @param id
	 *            a cryptic identifier for this tile, usually two characters in
	 *            length.
	 *  
	 */
    public abstract void setID(String id);

    /**
	 * Sets a two character mapcode associated with this Tile object which is
	 * used in delimited text files. Note we may remove this later.
	 * 
	 * @param mapCode
	 *            a cryptic code for this tile usually two characters in length.
	 */
    public abstract void setMapCode(String mapCode);

    /**
	 * If true this means it can be layered on top of other tiles.
	 * 
	 * @return <code>true</code> if images has a White set as transparent else
	 *         return <code>false</code>.
	 */
    public abstract boolean isTransparent();

    /**
	 * Gets the image which is used for drawing on the screen/panel.
	 * 
	 * @return a Graphics <cod>Image</code> object.
	 */
    public abstract Image getImage();

    /**
	 * Gets a descriptive Name for this tile.
	 * 
	 * @return <code>String</code> for Name of tile for Descriptions.
	 */
    public abstract String getName();

    /**
	 * Gets an ID associated with this Tile.
	 * 
	 * @return <code>String</code> as ID for other lookup and sort purposes.
	 */
    public abstract String getID();

    /**
	 * Gets the two character map code associated with this tile. This map code
	 * is used in delimited text files.
	 *
	 * @return <code>String</code> which represends a two character mapcode.
	 */
    public abstract String getMapCode();
}
