package de.intarsys.pdf.font;

/**
 * An abstract implementation for a map from a code range to CID's.
 * 
 */
public abstract class CMapRangeMap extends CMapMap {

    protected final int start;

    protected final int end;

    /**
	 * 
	 */
    public CMapRangeMap(byte[] start, byte[] end) {
        super();
        this.start = CMap.toInt(start);
        this.end = CMap.toInt(end);
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }
}
