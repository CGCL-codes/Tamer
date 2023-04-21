package loci.formats.in;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import loci.formats.*;

/**
 * ImageIOReader is the superclass for file format readers
 * that use the javax.imageio package.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="https://skyking.microscopy.wisc.edu/trac/java/browser/trunk/loci/formats/in/ImageIOReader.java">Trac</a>,
 * <a href="https://skyking.microscopy.wisc.edu/svn/java/trunk/loci/formats/in/ImageIOReader.java">SVN</a></dd></dl>
 *
 * @author Curtis Rueden ctrueden at wisc.edu
 */
public abstract class ImageIOReader extends FormatReader {

    /** Constructs a new ImageIOReader. */
    public ImageIOReader(String name, String suffix) {
        super(name, suffix);
    }

    /** Constructs a new ImageIOReader. */
    public ImageIOReader(String name, String[] suffixes) {
        super(name, suffixes);
    }

    public boolean isThisType(byte[] block) {
        return false;
    }

    public byte[] openBytes(int no, byte[] buf) throws FormatException, IOException {
        buf = ImageTools.getBytes(openImage(no), false, no);
        int bytesPerChannel = core.sizeX[0] * core.sizeY[0];
        if (buf.length > bytesPerChannel) {
            byte[] tmp = buf;
            buf = new byte[bytesPerChannel * 3];
            for (int i = 0; i < 3; i++) {
                System.arraycopy(tmp, i * bytesPerChannel, buf, i * bytesPerChannel, bytesPerChannel);
            }
        }
        return buf;
    }

    public BufferedImage openImage(int no) throws FormatException, IOException {
        FormatTools.assertId(currentId, true, 1);
        FormatTools.checkPlaneNumber(this, no);
        RandomAccessStream ras = new RandomAccessStream(currentId);
        DataInputStream dis = new DataInputStream(new BufferedInputStream(ras, 4096));
        BufferedImage b = ImageIO.read(dis);
        ras.close();
        dis.close();
        return b;
    }

    public void close(boolean fileOnly) throws IOException {
    }

    public void close() throws IOException {
        currentId = null;
    }

    protected void initFile(String id) throws FormatException, IOException {
        if (debug) debug("ImageIOReader.initFile(" + id + ")");
        super.initFile(id);
        status("Populating metadata");
        core.imageCount[0] = 1;
        BufferedImage img = openImage(0);
        core.sizeX[0] = img.getWidth();
        core.sizeY[0] = img.getHeight();
        core.rgb[0] = img.getRaster().getNumBands() > 1;
        core.sizeZ[0] = 1;
        core.sizeC[0] = core.rgb[0] ? 3 : 1;
        core.sizeT[0] = 1;
        core.currentOrder[0] = "XYCZT";
        core.pixelType[0] = ImageTools.getPixelType(img);
        core.interleaved[0] = false;
        core.littleEndian[0] = false;
        core.metadataComplete[0] = true;
        core.indexed[0] = false;
        core.falseColor[0] = false;
        MetadataStore store = getMetadataStore();
        store.setImage(currentId, null, null, null);
        FormatTools.populatePixels(store, this);
        for (int i = 0; i < core.sizeC[0]; i++) {
            store.setLogicalChannel(i, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }
    }
}
