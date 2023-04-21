package jahuwaldt.maptools;

import java.awt.Graphics;
import java.awt.Color;
import jahuwaldt.maptools.sdts.SDTS_DLGAttribute;

/**
*  A DLG Line that represents a class 2, secondary route,
*  symbol undivided.
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author  Joseph A. Huwaldt   Date:  August 27, 2000
*  @version July 17, 2004
**/
public class DLGLClass2Undivided extends DLGLine {

    static final long serialVersionUID = 3693813158565494686L;

    /**
	*  The USGS Major Code for this element:  Roads and Trails (170)
	**/
    public static final int kMajorCode = 170;

    /**
	*  The USGS Minor Code for this element:  Class 2 Undivided Route (205)
	**/
    public static final int kMinorCode = 205;

    /**
	*  Construct a DLG line segment element that has an ID,
	*  a list of attributes and a list of coordinate locations on a map layer.
	*
	*  @param  id          The ID number assigned to this element.
	*  @param  attrib      A list of label/value pair attributes assigned to
	*                      this element.
	*  @param  eastings   A list of UTM easting coordinates that define this
	*                     line segment.
	*  @param  northings  A list of UTM northing coordinates that define this
	*                     line segment.
	*  @param  zone       The UTM zone for these map coordinates.
	**/
    public DLGLClass2Undivided(int id, SDTS_DLGAttribute[] attrib, double[] eastings, double[] northings, int zone) {
        super(id, kMajorCode, kMinorCode, attrib, eastings, northings, zone);
    }

    /**
	*  Method that draws or renders this line.  Class 2, secondary routes, undivided
	*  are rendered as a 2 pixel wide alternating red/white line.  I don't know
	*  how to draw a 2 pixel line (let alone alternating red/white), so a 1
	*  pixel red line will have to do.
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
        if (display() && !usedByArea()) {
            Color saveColor = gc.getColor();
            gc.setColor(DLGLayer.kRed);
            super.draw(gc, globalRef);
            gc.setColor(saveColor);
        }
    }

    /**
	*  Returns a description of the sub-type of this DLG element
	*  as a String.
	*
	*  @returns  "Seconary route, class 2, undivided"
	**/
    public String description() {
        return "Seconary route, class 2, undivided";
    }
}
