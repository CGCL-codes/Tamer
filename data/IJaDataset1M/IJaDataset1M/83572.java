package org.compiere.print.layout;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import org.compiere.print.*;

/**
 *	Graphic Element
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: GraphElement.java,v 1.2 2006/07/30 00:53:02 jjanke Exp $
 */
public class GraphElement extends PrintElement {

    /**
	 *	Constructor
	 *  @param pg graph model
	 */
    public GraphElement(MPrintGraph pg) {
    }

    /**
	 * 	Layout and Calculate Size
	 * 	Set p_width & p_height
	 * 	@return true if calculated
	 */
    protected boolean calculateSize() {
        return false;
    }

    /**
	 * 	Paint/Print.
	 * 	@param g2D Graphics
	 *  @param pageNo page number for multi page support (0 = header/footer)
	 *  @param pageStart top left Location of page
	 *  @param ctx context
	 *  @param isView true if online view (IDs are links)
	 */
    public void paint(Graphics2D g2D, int pageNo, Point2D pageStart, Properties ctx, boolean isView) {
    }
}
