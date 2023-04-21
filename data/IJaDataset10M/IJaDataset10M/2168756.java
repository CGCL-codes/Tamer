package vmap.modes;

import vmap.modes.MindMapLink;
import vmap.modes.MindMapNode;
import java.awt.Point;

public interface MindMapArrowLink extends MindMapLink {

    public Point getStartInclination();

    public Point getEndInclination();

    /** the type of the start arrow: currently "None" and "Default". */
    public String getStartArrow();

    /** the type of the end arrow: currently "None" and "Default". */
    public String getEndArrow();
}
