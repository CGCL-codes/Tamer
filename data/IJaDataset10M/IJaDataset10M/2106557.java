package visu.handball.moves.model.player;

import java.awt.Color;

/**
 * Ein Objekt das diese Schnittstelle implementiert, wird beim "Mouse Over"
 * gehighlighted.
 * 
 * @author Tommy
 * 
 */
public interface HighlightableItem {

    public static final Color HIGHLIGHT_COLOR = Color.YELLOW;

    public void setHighlight(boolean highlight);
}
