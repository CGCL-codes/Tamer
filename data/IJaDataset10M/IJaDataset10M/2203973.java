package de.byteholder.geoclipse.map;

import java.util.ArrayList;

public interface ITileChildrenCreator {

    /**
	 * Creates sub tiles
	 * 
	 * @param parentTile
	 */
    public ArrayList<Tile> createTileChildren(Tile parentTile);

    /**
	 * @param parentTile
	 * @param childTile
	 * @return Returns the image data for the parent image
 	 */
    public ParentImageStatus getParentImage(Tile parentTile, Tile childTile);
}
