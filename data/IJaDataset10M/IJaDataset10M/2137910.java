package jahuwaldt.maptools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Paint;
import jahuwaldt.maptools.sdts.SDTS_DLGAttribute;

/**
*  A DLG Area that represents a school.
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author  Joseph A. Huwaldt   Date:  November 24, 2000
*  @version July 11, 2004
**/
public class DLGASchool extends DLGArea {

    /**
	*  The USGS Major Code for this element:  Manmade Structures (200)
	**/
    public static final int kMajorCode = 200;

    /**
	*  The USGS Minor Code for this element:  School (403)
	**/
    public static final int kMinorCode = 403;

    /**
	*  Construct a DLG area element that has an ID, a
	*  list of attributes and a list of coordinate locations
	*  defining the boundary of the area on a map layer.
	*
	*  @param  id          The ID number assigned to this element.
	*  @param  attrib      A list of label/value pair attributes assigned to
	*                      this element.
	*  @param  center     A UTM map coordinate located inside the area
	*                     (usually near the center).
	*  @param  lines      A list of Line objects that define the boundary
	*                     of this area.
	*  @param  areaLR     An array of flags indicating if this area is to 
	*                     the left (false) or right (true) of each bounding
	*                     line segment.
	**/
    public DLGASchool(int id, SDTS_DLGAttribute[] attrib, UTMCoord center, DLGLine[] lines, boolean[] areaLR) {
        super(id, kMajorCode, kMinorCode, attrib, center, lines, areaLR);
    }

    /**
	*  Method that draws or renders this area.  A school is drawn
	*  as a solid black area with a flag on one edge.
	*
	*  @param  gc  The graphics context into which the map data is
	*              to be rendered.
	*  @param  globalRef  Geographic reference (map information) that
	*              is to be used for the map rendering.  If a map
	*              is made up of multiple layers, this geographic
	*              reference may be different than that contained in
	*              any particular layer.
	**/
    public void draw(Graphics gc, UTMMapData globalRef) {
        if (display()) {
            Graphics2D g2 = (Graphics2D) gc;
            Paint savePaint = g2.getPaint();
            g2.setPaint(DLGLayer.kBlack);
            super.draw(gc, globalRef);
            g2.setPaint(savePaint);
        }
    }

    /**
	*  Returns a description of the sub-type of this DLG element
	*  as a String.
	*
	*  @returns  "School"
	**/
    public String description() {
        return "School";
    }
}
