package net.sourceforge.openstego.plugin.dctlsb;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import net.sourceforge.openstego.OpenStegoConfig;
import net.sourceforge.openstego.OpenStegoException;
import net.sourceforge.openstego.plugin.template.dct.DCTDataHeader;
import net.sourceforge.openstego.util.ImageUtil;
import net.sourceforge.openstego.util.StringUtil;
import net.sourceforge.openstego.util.dct.DCT;

/**
 * InputStream to read embedded data from image file using DCT LSB algorithm
 */
public class DctLSBInputStream extends InputStream {

    /**
     * Data header
     */
    private DCTDataHeader dataHeader = null;

    /**
     * Current message bit number
     */
    private int n = 0;

    /**
     * Width of the image
     */
    private int imgWidth = 0;

    /**
     * Height of the image
     */
    private int imgHeight = 0;

    /**
     * Array to store Y component from YUV colorspace of the image
     */
    private int[][] y = null;

    /**
     * Object to handle DCT transforms
     */
    private DCT dct = null;

    /**
     * Array to store the DCT coefficients for the image
     */
    private double[][] dcts = null;

    /**
     * Coordinate hit check class
     */
    private Coordinates coord = null;

    /**
     * Random number generator
     */
    private Random rand = null;

    /**
     * Configuration data
     */
    private OpenStegoConfig config = null;

    /**
     * Default constructor
     * 
     * @param image Image data to be read
     * @param config Configuration data to use while reading
     * @throws OpenStegoException
     */
    public DctLSBInputStream(BufferedImage image, OpenStegoConfig config) throws OpenStegoException {
        if (image == null) {
            throw new IllegalArgumentException("No image file provided");
        }
        this.config = config;
        this.imgWidth = image.getWidth();
        this.imgHeight = image.getHeight();
        this.imgWidth = this.imgWidth - (this.imgWidth % DCT.NJPEG);
        this.imgHeight = this.imgHeight - (this.imgHeight % DCT.NJPEG);
        this.y = (int[][]) ImageUtil.getYuvFromImage(image).get(0);
        this.dct = new DCT();
        this.dct.initDct8x8();
        this.dct.initQuantumJpegLumin();
        this.dcts = new double[DCT.NJPEG][DCT.NJPEG];
        this.coord = new Coordinates((this.imgWidth * this.imgHeight * 8) / (DCT.NJPEG * DCT.NJPEG));
        this.rand = new Random(StringUtil.passwordHash(this.config.getPassword()));
        readHeader();
    }

    /**
     * Method to read header data from the input stream
     * 
     * @throws OpenStegoException
     */
    private void readHeader() throws OpenStegoException {
        this.dataHeader = new DCTDataHeader(this, this.config);
    }

    /**
     * Implementation of <code>InputStream.read()</code> method
     * 
     * @return Byte read from the stream
     * @throws IOException
     */
    public int read() throws IOException {
        int out = 0;
        int xb = 0;
        int yb = 0;
        int coeffNum = 0;
        for (int count = 0; count < 8; count++) {
            if (this.n >= (this.imgWidth * this.imgHeight * 8)) {
                return -1;
            }
            do {
                xb = Math.abs(this.rand.nextInt()) % (this.imgWidth / DCT.NJPEG);
                yb = Math.abs(this.rand.nextInt()) % (this.imgHeight / DCT.NJPEG);
            } while (!this.coord.add(xb, yb));
            this.dct.fwdDctBlock8x8(this.y, xb * DCT.NJPEG, yb * DCT.NJPEG, this.dcts);
            do {
                coeffNum = (Math.abs(this.rand.nextInt()) % (DCT.NJPEG * DCT.NJPEG - 2)) + 1;
            } while (this.dct.isMidFreqCoeff8x8(coeffNum) == 0);
            this.dct.quantize8x8(this.dcts);
            out = (out << 1) + (((int) this.dcts[coeffNum / DCT.NJPEG][coeffNum % DCT.NJPEG]) & 1);
            this.n++;
        }
        return out;
    }

    /**
     * Get method for dataHeader
     * 
     * @return Data header
     */
    public DCTDataHeader getDataHeader() {
        return this.dataHeader;
    }
}
