package net.sourceforge.univecs.client;

import java.awt.Graphics2D;
import net.sourceforge.univecs.gui.hex.HexMapModel;
import net.sourceforge.univecs.gui.hex.HexTile;

/**
 * Renderer for a hex map tile.
 * 
 * @author Jason Steele
 * @author Copyright (c) 2008, UniVeCS Dev Team. All rights reserved.
 */
public interface TileRenderer {

    /**
    * Renders the tile into the given graphics context.
    * 
    * @param g2d graphics context to draw in
    * @param model model for hex coordinate system
    * @param tile tile to draw
    */
    void render(Graphics2D g2d, HexMapModel model, HexTile tile);
}
