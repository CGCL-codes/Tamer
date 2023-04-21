package jahuwaldt.maptools;

import jahuwaldt.maptools.sdts.SDTS_DLGAttribute;

/**
*  A DLG Node that represents a tower.
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author  Joseph A. Huwaldt   Date:  November 24, 2000
*  @version July 10, 2004
**/
public class DLGNTower extends DLGNode {

    /**
	*  The USGS Major Code for this element:  Manmade Structures (200)
	**/
    public static final int kMajorCode = 200;

    /**
	*  The USGS Minor Code for this element:  Tower (305)
	**/
    public static final int kMinorCode = 305;

    static final long serialVersionUID = -5293437488831388802L;

    /**
	*  Construct a DLG node element that has an ID, a list of attributes and a
	*  coordinate location on a map layer.
	*
	*  @param  id          The ID number assigned to this element.
	*  @param  attrib      A list of label/value pair attributes assigned to
	*                      this element.
	*  @param  coordinate  The UTM coordinate assigned to this node.
	**/
    public DLGNTower(int id, SDTS_DLGAttribute[] attrib, UTMCoord coordinate) {
        super(id, kMajorCode, kMinorCode, attrib, coordinate);
    }

    /**
	*  Returns a description of the sub-type of this DLG element
	*  as a String.
	*
	*  @returns  "Tower"
	**/
    public String description() {
        return "Tower";
    }
}
