package com.levigo.jbig2.segments;

import java.io.IOException;
import com.levigo.jbig2.SegmentHeader;
import com.levigo.jbig2.SegmentData;
import com.levigo.jbig2.io.SubInputStream;
import com.levigo.jbig2.util.CombinationOperator;
import com.levigo.jbig2.util.IntegerMaxValueException;
import com.levigo.jbig2.util.InvalidHeaderValueException;

/**
 * This class represents the "Region segment information" field, 7.4.1 (page 50). <br>
 * Every region segment data starts with this part.
 * 
 * @author <a href="mailto:m.krzikalla@levigo.de">Matthäus Krzikalla</a>
 * 
 */
public class RegionSegmentInformation implements SegmentData {

    private SubInputStream subInputStream;

    /** Region segment bitmap width, 7.4.1.1 */
    private int bitmapWidth;

    /** Region segment bitmap height, 7.4.1.2 */
    private int bitmapHeight;

    /** Region segment bitmap X location, 7.4.1.3 */
    private int xLocation;

    /** Region segment bitmap Y location, 7.4.1.4 */
    private int yLocation;

    /** Region segment flags, 7.4.1.5 */
    private CombinationOperator combinationOperator;

    public RegionSegmentInformation(SubInputStream subInputStream) {
        this.subInputStream = subInputStream;
    }

    public RegionSegmentInformation() {
    }

    public void parseHeader() throws IOException {
        this.bitmapWidth = ((int) (subInputStream.readBits(32) & 0xffffffff));
        this.bitmapHeight = ((int) (subInputStream.readBits(32) & 0xffffffff));
        this.xLocation = ((int) (subInputStream.readBits(32) & 0xffffffff));
        this.yLocation = ((int) (subInputStream.readBits(32) & 0xffffffff));
        subInputStream.readBits(5);
        readCombinationOperator();
    }

    private void readCombinationOperator() throws IOException {
        this.combinationOperator = (CombinationOperator.translateOperatorCodeToEnum((short) (subInputStream.readBits(3) & 0xf)));
    }

    public void init(SegmentHeader header, SubInputStream sis) throws InvalidHeaderValueException, IntegerMaxValueException, IOException {
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public int getXLocation() {
        return xLocation;
    }

    public int getYLocation() {
        return yLocation;
    }

    public CombinationOperator getCombinationOperator() {
        return combinationOperator;
    }
}
