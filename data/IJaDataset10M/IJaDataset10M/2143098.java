package loci.formats;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Logic to automatically separate the channels in a file.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="https://skyking.microscopy.wisc.edu/trac/java/browser/trunk/loci/formats/ChannelSeparator.java">Trac</a>,
 * <a href="https://skyking.microscopy.wisc.edu/svn/java/trunk/loci/formats/ChannelSeparator.java">SVN</a></dd></dl>
 */
public class ChannelSeparator extends ReaderWrapper {

    /** Last image opened. */
    private byte[] lastImage;

    /** Index of last image opened. */
    private int lastImageIndex = -1;

    /** Series of last image opened. */
    private int lastImageSeries = -1;

    /** Constructs a ChannelSeparator around a new image reader. */
    public ChannelSeparator() {
        super();
    }

    /** Constructs a ChannelSeparator with the given reader. */
    public ChannelSeparator(IFormatReader r) {
        super(r);
    }

    public void setId(String id) throws FormatException, IOException {
        super.setId(id);
        lastImage = null;
        lastImageIndex = -1;
        lastImageSeries = -1;
    }

    public void setId(String id, boolean force) throws FormatException, IOException {
        super.setId(id, force);
        lastImage = null;
        lastImageIndex = -1;
        lastImageSeries = -1;
    }

    public int getImageCount() {
        FormatTools.assertId(getCurrentFile(), true, 2);
        return (reader.isRGB() && !reader.isIndexed()) ? (getSizeC() / reader.getEffectiveSizeC()) * reader.getImageCount() : reader.getImageCount();
    }

    public String getDimensionOrder() {
        FormatTools.assertId(getCurrentFile(), true, 2);
        String order = super.getDimensionOrder();
        if (reader.isRGB() && !reader.isIndexed()) {
            String newOrder = "XYC";
            if (order.indexOf("Z") > order.indexOf("T")) newOrder += "TZ"; else newOrder += "ZT";
            return newOrder;
        }
        return order;
    }

    public boolean isRGB() {
        FormatTools.assertId(getCurrentFile(), true, 2);
        return isIndexed() && !isFalseColor();
    }

    public BufferedImage openImage(int no) throws FormatException, IOException {
        FormatTools.assertId(getCurrentFile(), true, 2);
        FormatTools.checkPlaneNumber(this, no);
        if (isIndexed()) return reader.openImage(no);
        int bytes = FormatTools.getBytesPerPixel(getPixelType());
        byte[] b = openBytes(no);
        if (getPixelType() == FormatTools.FLOAT) {
            float[] f = (float[]) DataTools.makeDataArray(b, 4, true, isLittleEndian());
            if (isNormalized()) f = DataTools.normalizeFloats(f);
            return ImageTools.makeImage(f, getSizeX(), getSizeY());
        }
        return ImageTools.makeImage(b, getSizeX(), getSizeY(), 1, false, bytes, isLittleEndian());
    }

    public byte[] openBytes(int no) throws FormatException, IOException {
        FormatTools.assertId(getCurrentFile(), true, 2);
        FormatTools.checkPlaneNumber(this, no);
        if (reader.isRGB() && !reader.isIndexed()) {
            int c = getSizeC() / reader.getEffectiveSizeC();
            int source = no / c;
            int channel = no % c;
            int series = getSeries();
            if (source != lastImageIndex || series != lastImageSeries) {
                lastImage = reader.openBytes(source);
                lastImageIndex = source;
                lastImageSeries = series;
            }
            return ImageTools.splitChannels(lastImage, c, FormatTools.getBytesPerPixel(getPixelType()), false, !isInterleaved())[channel];
        } else return reader.openBytes(no);
    }

    public BufferedImage openThumbImage(int no) throws FormatException, IOException {
        FormatTools.assertId(getCurrentFile(), true, 2);
        return ImageTools.scale(openImage(no), getThumbSizeX(), getThumbSizeY(), true);
    }

    public void close() throws IOException {
        super.close();
        lastImage = null;
        lastImageIndex = -1;
        lastImageSeries = -1;
    }

    public int getIndex(int z, int c, int t) {
        return FormatTools.getIndex(this, z, c, t);
    }

    public int[] getZCTCoords(int index) {
        return FormatTools.getZCTCoords(this, index);
    }
}
