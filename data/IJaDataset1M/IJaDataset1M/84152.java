package image.png;

import Abstract.EmbeddingProperties;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * For writing a PNG image
 */
public class PngWriter {

    public static final int FILTER_NONE = 0;

    public static final int FILTER_SUB = 1;

    public static final int FILTER_UP = 2;

    public static final int FILTER_AVERAGE = 3;

    public static final int FILTER_PAETH = 4;

    public EmbeddingProperties props;

    private final String filename;

    private boolean overwrite = false;

    public final ImageInfo imgInfo;

    private final int valsPerRow;

    private final int bytesPerRow;

    private int compLevel = 6;

    private int filterType = FILTER_SUB;

    private Double dpi = null;

    private PngTxtInfo txtInfo = new PngTxtInfo();

    private boolean initialized = false;

    private final CRC32 crcEngine;

    private int rowNum = -1;

    private int[] scanline = null;

    private int[] rowb = null;

    private int[] rowbprev = null;

    private byte[] rowbfilter = null;

    private OutputStream os;

    private PngIDatChunkOutputStream datStream;

    private DeflaterOutputStream datStreamDeflated;

    public class PngTxtInfo {

        public void writeChunks(EmbeddingProperties props) {
            LinkedList<PngTextMetadataUnit> list = props.getPropsList();
            ListIterator<PngTextMetadataUnit> iter = list.listIterator();
            while (iter.hasNext()) {
                PngTextMetadataUnit unit = iter.next();
                writeChunk(unit.keyword, unit.value);
            }
        }

        private void writeChunk(String name, String val) {
            if (val == null) {
                return;
            }
            PngChunk p = PngChunk.createTextChunk(name, val, crcEngine);
            p.writeChunk(os);
        }
    }

    public PngWriter(String filename, ImageInfo imgInfo) {
        this.filename = filename;
        this.imgInfo = imgInfo;
        this.bytesPerRow = imgInfo.width * imgInfo.bytesPixel;
        this.valsPerRow = imgInfo.width * imgInfo.channels;
        crcEngine = new CRC32();
        scanline = new int[valsPerRow];
        rowb = new int[bytesPerRow + 1];
        rowbprev = new int[bytesPerRow + 1];
        rowbfilter = new byte[bytesPerRow + 1];
    }

    /**
     * To be called after setting parameters and before writing lines. If not
     * called explicityly will be called implicitly.
     *
     * @param reader  Optional. If not null, pallette and some ancillary chunks will be copied.
     *
     * http://www.w3.org/TR/PNG/#table53
     */
    public void prepare(PngReader reader) {
        if (initialized) {
            return;
        }
        File f = new File(filename);
        if (f.exists() && !overwrite) {
            throw new PngException("File exists (and overwrite=false) " + filename);
        }
        try {
            this.os = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            throw new PngOutputException("error opening " + filename + " for writing", e);
        }
        datStream = new PngIDatChunkOutputStream(this.os, 8192);
        datStreamDeflated = new DeflaterOutputStream(datStream, new Deflater(compLevel));
        writeHeader();
        boolean physChunkDone = false;
        if (dpi != null) {
            writePhysChunk();
            physChunkDone = true;
        }
        if (reader != null) {
            for (PngChunk chunk : reader.getChunks1()) {
                if (!chunk.id.equals("PLTE") && (chunk.isCritical() || !chunk.isSafeToCopy())) {
                    continue;
                }
                if (chunk.id.equals(PngHelper.IPHYS_TEXT) && physChunkDone) {
                    continue;
                }
                chunk.writeChunk(this.os);
            }
        }
        if (props != null) {
            txtInfo.writeChunks(props);
        }
        initialized = true;
    }

    public void doInit() {
        prepare(null);
    }

    /**
     * Write id header and also "IHDR" chunk
     */
    private void writeHeader() {
        PngHelper.writeBytes(os, PngHelper.pngIdBytes);
        ByteArrayOutputStream tstream = new ByteArrayOutputStream();
        PngHelper.writeInt4(tstream, imgInfo.width);
        PngHelper.writeInt4(tstream, imgInfo.height);
        PngHelper.writeByte(tstream, (byte) (imgInfo.bitDepth));
        int colormodel = 0;
        if (imgInfo.alpha) {
            colormodel += 0x04;
        }
        if (imgInfo.indexed) {
            colormodel += 0x01;
        }
        if (!imgInfo.greyscale) {
            colormodel += 0x02;
        }
        PngHelper.writeByte(tstream, (byte) colormodel);
        PngHelper.writeByte(tstream, (byte) 0);
        PngHelper.writeByte(tstream, (byte) 0);
        PngHelper.writeByte(tstream, (byte) 0);
        byte[] b = tstream.toByteArray();
        if (b.length != 13) {
            throw new PngOutputException("BAD IDHR!");
        }
        PngHelper.writeChunk(os, b, 0, b.length, PngHelper.IHDR, crcEngine);
    }

    /**
     * writes physical chunk: image resolution (from dpi attribute)
     */
    protected void writePhysChunk() {
        ByteArrayOutputStream tstream = new ByteArrayOutputStream();
        int pixelsPerMeter = (int) (dpi * 100.0 / 2.54 + 0.5);
        PngHelper.writeInt4(tstream, pixelsPerMeter);
        PngHelper.writeInt4(tstream, pixelsPerMeter);
        PngHelper.writeByte(tstream, (byte) 1);
        byte[] b = tstream.toByteArray();
        PngHelper.writeChunk(os, b, 0, b.length, PngHelper.IPHYS, crcEngine);
    }

    /**
     * writes ending chunk.
     */
    protected void endchunk() {
        byte[] b = new byte[] {};
        PngHelper.writeChunk(os, b, 0, 0, PngHelper.IEND, crcEngine);
    }

    /**
     * Writes a full image row. This must be called sequentially from n=0 to
     * n=height-1 One integer per sample , in the natural order: R G B R G B
     * ... (or R G B A R G B A... if has alpha) The values should be between 0
     * and 255 for 8 bitspc images, and between 0- 65535 form 16 bitspc images
     * (this applies also to the alpha channel if present) The array can be
     * reused.
     *
     * @param newrow
     *            Array of pixel values
     * @param n
     *            Number of row, from 0 (top) to height-1 (bottom)
     */
    public void writeRow(int[] newrow, int n) {
        if (!initialized) {
            doInit();
        }
        if (n < 0 || n > imgInfo.height) {
            throw new RuntimeException("invalid value for row " + n);
        }
        rowNum++;
        if (rowNum != n) {
            throw new RuntimeException("write order must be strict for rows " + n + " (expected=" + rowNum + ")");
        }
        scanline = newrow;
        int[] tmp = rowb;
        rowb = rowbprev;
        rowbprev = tmp;
        convertRowToBytes();
        filterRow();
        try {
            datStreamDeflated.write(rowbfilter, 0, bytesPerRow + 1);
        } catch (IOException e) {
            throw new PngOutputException(e);
        }
    }

    /**
     * this uses the row number from the imageline!
     */
    public void writeRow(ImageLine imgline) {
        writeRow(imgline.scanline, imgline.getRown());
    }

    /**
     * Finalizes the image creation and closes the file stream. This MUST be
     * called after writing the lines.
     */
    public void end() {
        if (rowNum != imgInfo.height - 1) {
            throw new PngOutputException("all rows have not been written");
        }
        try {
            datStreamDeflated.finish();
            datStream.flush();
            endchunk();
            os.close();
        } catch (IOException e) {
            throw new PngOutputException(e);
        }
    }

    private void filterRow() {
        rowbfilter[0] = (byte) filterType;
        switch(filterType) {
            case FILTER_NONE:
                filterRowNone();
                break;
            case FILTER_SUB:
                filterRowSub();
                break;
            case FILTER_UP:
                filterRowUp();
                break;
            case FILTER_AVERAGE:
                filterRowAverage();
                break;
            case FILTER_PAETH:
                filterRowPaeth();
                break;
            default:
                throw new PngOutputException("Filter type " + filterType + " not implemented");
        }
    }

    private void filterRowNone() {
        for (int i = 1; i <= bytesPerRow; i++) {
            rowbfilter[i] = (byte) rowb[i];
        }
    }

    private void filterRowSub() {
        int i, j;
        for (i = 1; i <= imgInfo.bytesPixel; i++) {
            rowbfilter[i] = (byte) rowb[i];
        }
        for (j = 1, i = imgInfo.bytesPixel + 1; i <= bytesPerRow; i++, j++) {
            rowbfilter[i] = (byte) (rowb[i] - rowb[j]);
        }
    }

    private void filterRowUp() {
        for (int i = 1; i <= bytesPerRow; i++) {
            rowbfilter[i] = (byte) (rowb[i] - rowbprev[i]);
        }
    }

    private void filterRowAverage() {
        int i, j, x;
        for (i = 1; i <= bytesPerRow; i++) {
            if (rowb[i] < 0 || rowb[i] > 255) {
                throw new PngOutputException("??" + rowb[i]);
            }
            if (rowbprev[i] < 0 || rowbprev[i] > 255) {
                throw new PngOutputException("??" + rowbprev[i]);
            }
        }
        for (j = 1 - imgInfo.bytesPixel, i = 1; i <= bytesPerRow; i++, j++) {
            x = j > 0 ? rowb[j] : 0;
            rowbfilter[i] = (byte) (rowb[i] - (rowbprev[i] + x) / 2);
        }
    }

    private void filterRowPaeth() {
        int i, j, x, y;
        for (i = 1; i <= bytesPerRow; i++) {
            if (rowb[i] < 0 || rowb[i] > 255) {
                throw new PngOutputException("??" + rowb[i] + " i=" + i + " row=" + rowNum);
            }
            if (rowbprev[i] < 0 || rowbprev[i] > 255) {
                throw new PngOutputException("??" + rowbprev[i]);
            }
        }
        for (j = 1 - imgInfo.bytesPixel, i = 1; i <= bytesPerRow; i++, j++) {
            x = j > 0 ? rowb[j] : 0;
            y = j > 0 ? rowbprev[j] : 0;
            rowbfilter[i] = (byte) (rowb[i] - PngHelper.filterPaethPredictor(x, rowbprev[i], y));
        }
    }

    private void convertRowToBytes() {
        int i, j, x;
        rowb[0] = (int) filterType;
        if (imgInfo.bitDepth == 8) {
            for (i = 0, j = 1; i < valsPerRow; i++) {
                rowb[j++] = (int) (scanline[i] & 0xFF);
            }
        } else {
            for (i = 0, j = 1; i < valsPerRow; i++) {
                x = (int) (scanline[i]) & 0xFFFF;
                rowb[j++] = ((x & 0xFF00) >> 8);
                rowb[j++] = (x & 0xFF);
            }
        }
    }

    /**
     * if this is set, the file will be overwritten, if not an exception will be
     * thrown
     */
    public void setOverrideFile(boolean overrideFile) {
        this.overwrite = overrideFile;
    }

    /**
     * Set physical resolution, in DPI (dots per inch) optional, only informative
     */
    public void setDpi(Double dpi) {
        this.dpi = dpi;
    }

    /**
     * Sets filter type: the recommend is the default FILTER_PAETH If the filter
     * is not implemented an excpetion will be thrown when writing the image.
     * Can be changed for each line.
     */
    public void setFilterType(int filterType) {
        if (filterType < 0 || filterType > 4) {
            throw new PngException("filterType  invalid (" + filterType + ") Must be 0..4");
        }
        this.filterType = filterType;
    }

    public int getFilterType() {
        return filterType;
    }

    /**
     * compression level: between 0 and 9 (default:6)
     */
    public void setCompLevel(int compLevel) {
        if (compLevel < 0 || compLevel > 9) {
            throw new PngException("Compression level invalid (" + compLevel + ") Must be 0..9");
        }
        this.compLevel = compLevel;
    }

    public int getCompLevel() {
        return compLevel;
    }

    public int getCols() {
        return imgInfo.width;
    }

    public int getRows() {
        return imgInfo.height;
    }

    public String getFilename() {
        return filename;
    }
}
