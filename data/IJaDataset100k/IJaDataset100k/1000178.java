package net.sf.vat4net.io;

/**
 * Class that contains the data for a link.
 * @author $Author: tom77 $
 * @version $Revision: 1.1 $
 */
public class LinkData extends DataWithUpdateElements {

    private int srcID, dstID;

    public LinkData(int src, int dst) {
        srcID = src;
        dstID = dst;
    }

    public int getDstID() {
        return dstID;
    }

    public int getSrcID() {
        return srcID;
    }
}
