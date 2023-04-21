package com.vividsolutions.jts.io.oracle;

/**
 * Set of constants used to interact with MDSYS.GEOMETRY and JTS Geometries. 
 * 
 *
 * @author David Zwiers, Vivid Solutions.
 */
class Constants {

    /**
	 * Null SRID
	 */
    public static final int SRID_NULL = -1;

    /**
	 * 
	 * Extracted from the Oracle Documentation for SDO_ETYPE
	 * 
	 * This list may need to be expanded in the future to handle additional Geometry Types.
	 *
	 * @author David Zwiers, Vivid Solutions.
	 * @author Jody Garnett, Refractions Research, Inc.
	 */
    static final class SDO_ETYPE {

        /** <code>ETYPE</code> code representing Point */
        public static final int POINT = 1;

        /** <code>ETYPE</code> code representing Line */
        public static final int LINE = 2;

        /** <code>ETYPE</code> code representing Polygon */
        public static final int POLYGON = 3;

        /** <code>ETYPE</code> code representing exterior counterclockwise  polygon ring */
        public static final int POLYGON_EXTERIOR = 1003;

        /** <code>ETYPE</code> code representing interior clockwise  polygon ring */
        public static final int POLYGON_INTERIOR = 2003;
    }

    /**
	 * Extracted from the Oracle Documentation for SDO_GTYPE.
	 * This represents the last two digits in a GTYPE, where the first id dimension and the second if LRS
	 * 
	 * This list may need to be expanded in the future to handle additional Geometry Types.
	 *
	 * @author David Zwiers, Vivid Solutions.
	 * @author Brent Owens, The Open Planning Project.
	 */
    static final class SDO_GTEMPLATE {

        /** <code>TT</code> code representing Point */
        public static final int POINT = 01;

        /** <code>TT</code> code representing Line (or Curve) */
        public static final int LINE = 02;

        /** <code>TT</code> code representing Polygon */
        public static final int POLYGON = 03;

        /** <code>TT</code> code representing Collection */
        public static final int COLLECTION = 04;

        /** <code>TT</code> code representing Multpoint */
        public static final int MULTIPOINT = 05;

        /** <code>TT</code> code representing Multiline (or Multicurve) */
        public static final int MULTILINE = 06;

        /** <code>TT</code> code representing MULTIPOLYGON */
        public static final int MULTIPOLYGON = 07;
    }
}
