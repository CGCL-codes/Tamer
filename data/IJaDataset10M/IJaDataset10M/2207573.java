package jahuwaldt.maptools;

import java.awt.Graphics;
import java.awt.Point;
import jahuwaldt.maptools.sdts.SDTS_DLGAttribute;

/**
*  A DLG Node that represents the location of a class 1 building.
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author  Joseph A. Huwaldt   Date:  September 2, 2000
*  @version July 10, 2004
**/
public class DLGNClass1Building extends DLGNode {

    /**
	*  The USGS Major Code for this element:  ManmadeStructures (200)
	**/
    public static final int kMajorCode = 200;

    /**
	*  The USGS Minor Code for this element:  Class 1 Building (400)
	**/
    public static final int kMinorCode = 400;

    static final long serialVersionUID = -8075797845474579202L;

    /**
	*  Construct a DLG node element that has an ID, a list of attributes and a
	*  coordinate location on a map layer.
	*
	*  @param  id          The ID number assigned to this element.
	*  @param  attrib      A list of label/value pair attributes assigned to
	*                      this element.
	*  @param  coordinate  The UTM coordinate assigned to this node.
	**/
    public DLGNClass1Building(int id, SDTS_DLGAttribute[] attrib, UTMCoord coordinate) {
        super(id, kMajorCode, kMinorCode, attrib, coordinate);
    }

    /**
	*  Constructor used only by different types of buildings that are sub-classes of this one.
	*
	*  @param  id          The ID number assigned to this element.
	*  @param  minorCode   The minor code to assign to this building node.
	*  @param  attrib      A list of label/value pair attributes assigned to
	*                      this element.
	*  @param  coordinate  The UTM coordinate assigned to this node.
	**/
    protected DLGNClass1Building(int id, int minorCode, SDTS_DLGAttribute[] attrib, UTMCoord coordinate) {
        super(id, kMajorCode, minorCode, attrib, coordinate);
    }

    /**
	*  Method that draws or renders this node.  This node is rendered
	*  as a 3 pixel wide square.
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
            if (tmpPoint == null) tmpPoint = new Point();
            globalRef.UTMtoPoint(coord.getEasting(), coord.getNorthing(), tmpPoint);
            int x1 = tmpPoint.x;
            int y1 = tmpPoint.y;
            --x1;
            --y1;
            gc.fillRect(x1, y1, 3, 3);
        }
    }

    /**
	*  Sets whether or not this node is used by a line
	*  This node type can not be used by a line.  So, this
	*  method is ignored and does nothing.
	*
	*  @param  flag     The flag indicating if this node is
	*                   used by the line or not.
	*  @param  theLine  The line that is either using (or
	*                   not using) this node.
	**/
    public void usedByLine(boolean flag, DLGLine theLine) {
    }

    /**
	*  Returns a description of the sub-type of this DLG element
	*  as a String.
	*
	*  @returns  "Class 1 building"
	**/
    public String description() {
        return "Class 1 building";
    }
}
