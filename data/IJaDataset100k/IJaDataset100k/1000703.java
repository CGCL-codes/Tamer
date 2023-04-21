package jj2000.j2k.codestream;

/**
 * This class is used to store the coordinates of precincts.
 * */
public class PrecCoordInfo extends CoordInfo {

    /** Horizontal upper left coordinate in the reference grid */
    public int xref;

    /** Vertical upper left coordinate on the reference grid */
    public int yref;

    /** 
     * Constructor. Creates a PrecCoordInfo object.
     *
     * @param ulx Horizontal upper left coordinate in the subband
     *
     * @param uly Vertical upper left coordinate in the subband
     *
     * @param w Precinct's width
     *
     * @param h Precinct's height
     *
     * @param xref The horizontal coordinate on the reference grid 
     *
     * @param yref The vertical coordinate on the reference grid 
     * */
    public PrecCoordInfo(int ulx, int uly, int w, int h, int xref, int yref) {
        super(ulx, uly, w, h);
        this.xref = xref;
        this.yref = yref;
    }

    /** 
     * Empty Constructor. Creates an empty PrecCoordInfo object.
     * */
    public PrecCoordInfo() {
        super();
    }

    /** 
     * Returns precinct's information in a String 
     * 
     * @return String with precinct's information
     * */
    @Override
    public String toString() {
        return super.toString() + ", xref=" + xref + ", yref=" + yref;
    }
}
