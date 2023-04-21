package cz.zcu.fav.hofhans.packer.dao.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.plugins.jpeg.JPEGQTable;
import com.colloquial.arithcode.AdaptiveUnigramModel;
import com.colloquial.arithcode.ArithCodeInputStream;
import com.colloquial.arithcode.ArithCodeModel;
import com.colloquial.arithcode.ArithCodeOutputStream;
import cz.zcu.fav.hofhans.packer.bdo.internal.Block;
import cz.zcu.fav.hofhans.packer.exception.PackerRuntimeException;
import edu.emory.mathcs.jtransforms.dct.DoubleDCT_2D;

/**
 * Utility class which provides block compression. It is compression almost same as JPEG
 * compression, but with diferent storing method.
 * @author Tomáš Hofhans
 * @since 15.3.2010
 */
public class BlockCompression {

    private static final Logger LOG = Logger.getLogger(BlockCompression.class.getName());

    private static BlockCompression instance;

    private static final int BYTE1 = 0xFF;

    private static final int BYTE2 = 0xFF00;

    private static final int BYTE3 = 0xFF0000;

    private static final int BYTE4 = 0xFF000000;

    private int[] lastChroma;

    private int[] lastLumina;

    private float lastScale = -1;

    /**
   * The zigzag-order position of the i'th element of a DCT block read in natural order.
   */
    private final int[] ZIG_ZAG = { 0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13, 16, 26, 29, 42, 3, 8, 12, 17, 25, 30, 41, 43, 9, 11, 18, 24, 31, 40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51, 55, 60, 21, 34, 37, 47, 50, 56, 59, 61, 35, 36, 48, 49, 57, 58, 62, 63 };

    /** Testing zig-zag order for matrix size 16. */
    @SuppressWarnings("unused")
    private final int[] ZIG_ZAG_16 = { 0, 1, 16, 32, 17, 2, 3, 18, 33, 48, 64, 49, 34, 19, 4, 5, 20, 35, 50, 65, 80, 96, 81, 66, 51, 36, 21, 6, 7, 22, 37, 52, 67, 82, 97, 112, 128, 113, 98, 83, 68, 53, 38, 23, 8, 9, 24, 39, 54, 69, 84, 99, 114, 129, 144, 160, 145, 130, 115, 100, 85, 70, 55, 40, 25, 10, 11, 26, 41, 56, 71, 86, 101, 116, 131, 146, 161, 176, 192, 177, 162, 147, 132, 117, 102, 87, 72, 57, 42, 27, 12, 13, 28, 43, 58, 73, 88, 103, 118, 133, 148, 163, 178, 193, 208, 224, 209, 194, 179, 164, 149, 134, 119, 104, 89, 74, 59, 44, 29, 14, 15, 30, 45, 60, 75, 90, 105, 120, 135, 150, 165, 180, 195, 210, 225, 240, 241, 226, 211, 196, 181, 166, 151, 136, 121, 106, 91, 76, 61, 46, 31, 47, 62, 77, 92, 107, 122, 137, 152, 167, 182, 197, 212, 227, 242, 243, 228, 213, 198, 183, 168, 153, 138, 123, 108, 93, 78, 63, 79, 94, 109, 124, 139, 154, 169, 184, 199, 214, 229, 244, 245, 230, 215, 200, 185, 170, 155, 140, 125, 110, 95, 111, 126, 141, 156, 171, 186, 201, 216, 231, 246, 247, 232, 217, 202, 187, 172, 157, 142, 127, 143, 158, 173, 188, 203, 218, 233, 248, 249, 234, 219, 204, 189, 174, 159, 175, 190, 205, 220, 235, 250, 251, 236, 221, 206, 191, 207, 222, 237, 252, 253, 238, 223, 239, 254, 255 };

    /** Testing chroma qtable for matrix size 16. */
    @SuppressWarnings("unused")
    private final int[] Q_TABLE_CHROMA_16 = new int[] { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 12, 12, 12, 12, 11, 11, 11, 11, 12, 12, 12, 12, 24, 24, 24, 24, 13, 13, 13, 13, 13, 13, 13, 13, 24, 24, 24, 24, 50, 50, 50, 50, 33, 33, 33, 33, 28, 28, 28, 28, 33, 33, 33, 33, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 };

    /** Testing luba qtable for matrix size 16. */
    @SuppressWarnings("unused")
    private final int[] Q_TABLE_LUMA_16 = new int[] { 8, 8, 8, 8, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 5, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 9, 9, 9, 9, 9, 9, 9, 9, 8, 8, 8, 8, 10, 10, 10, 10, 12, 12, 12, 12, 20, 20, 20, 20, 13, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 25, 25, 25, 25, 18, 18, 18, 18, 19, 19, 19, 19, 15, 15, 15, 15, 20, 20, 20, 20, 29, 29, 29, 29, 26, 26, 26, 26, 31, 31, 31, 31, 30, 30, 30, 30, 29, 29, 29, 29, 26, 26, 26, 26, 28, 28, 28, 28, 28, 28, 28, 28, 32, 32, 32, 32, 36, 36, 36, 36, 46, 46, 46, 46, 39, 39, 39, 39, 32, 32, 32, 32, 34, 34, 34, 34, 44, 44, 44, 44, 35, 35, 35, 35, 28, 28, 28, 28, 28, 28, 28, 28, 40, 40, 40, 40, 55, 55, 55, 55, 41, 41, 41, 41, 44, 44, 44, 44, 48, 48, 48, 48, 49, 49, 49, 49, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 31, 31, 31, 31, 39, 39, 39, 39, 57, 57, 57, 57, 61, 61, 61, 61, 56, 56, 56, 56, 50, 50, 50, 50, 60, 60, 60, 60, 46, 46, 46, 46, 51, 51, 51, 51, 52, 52, 52, 52, 50, 50, 50, 50 };

    /** Hiding constructor. */
    private BlockCompression() {
        super();
    }

    /**
   * Transform colors from rgb color space to YCrCb color space. Output arrays must be initialized.
   * 
   * @param rgb input array with rgb image values
   * @param y output array with Y-component
   * @param cb output array with Cb-component
   * @param cr output array with Cr-component
   * @param alpha output array with alpha-component
   */
    public void colorTransform(int[] rgb, int[] y, int[] cb, int[] cr, int[] alpha) {
        assert rgb != null : "Input array must be initialized.";
        assert y != null : "Output array must be initialized.";
        assert cb != null : "Output array must be initialized.";
        assert cr != null : "Output array must be initialized.";
        int r, g, b, a;
        for (int i = 0; i != rgb.length; i++) {
            b = (rgb[i] & BYTE1);
            g = ((rgb[i] & BYTE2) >> 8);
            r = ((rgb[i] & BYTE3) >> 16);
            a = ((rgb[i] & BYTE4) >> 24);
            y[i] = (int) (-128 + 0.299 * r + 0.587 * g + 0.114 * b);
            cb[i] = (int) (-0.168736 * r - 0.331264 * g + 0.5 * b);
            cr[i] = (int) (0.5 * r - 0.418688 * g - 0.081312 * b);
            alpha[i] = a;
        }
    }

    /**
   * Transform colors from YCrCb color space to RGB color space. Output arrays must be initialized.
   * 
   * @param rgb output array with rgb image values
   * @param y input array with Y-component
   * @param cb input array with Cb-component
   * @param cr input array with Cr-component
   */
    public void colorTransformReverse(int[] rgb, int[] y, int[] cb, int[] cr) {
        assert rgb != null : "Input array must be initialized.";
        assert y != null : "Output array must be initialized.";
        assert cb != null : "Output array must be initialized.";
        assert cr != null : "Output array must be initialized.";
        int r, g, b;
        for (int i = 0; i != rgb.length; i++) {
            r = (int) (128 + y[i] + 1.402 * cr[i]);
            g = (int) (128 + y[i] - 0.34414 * cb[i] - 0.71414 * cr[i]);
            b = (int) (128 + y[i] + 1.772 * cb[i]);
            r = clip(r);
            g = clip(g);
            b = clip(b);
            rgb[i] = r << 16 | g << 8 | b;
        }
    }

    /**
   * Clip integer to [0, 255].
   * @param i integer to clip
   * @return cliped
   */
    private int clip(int i) {
        if (i < 0) {
            return 0;
        } else if (i > 255) {
            return 255;
        }
        return i;
    }

    /**
   * Transform colors from YCrCb color space to RGB color space. Output arrays must be initialized.
   * 
   * @param rgb output array with rgb image values
   * @param y input array with Y-component
   * @param cb input array with Cb-component
   * @param cr input array with Cr-component
   * @param alpha input array with alpha-component
   */
    public void colorTransformReverse(int[] rgb, int[] y, int[] cb, int[] cr, int[] alpha) {
        assert rgb != null : "Input array must be initialized.";
        assert y != null : "Output array must be initialized.";
        assert cb != null : "Output array must be initialized.";
        assert cr != null : "Output array must be initialized.";
        int r, g, b, a;
        for (int i = 0; i != rgb.length; i++) {
            r = (int) (128 + y[i] + 1.402 * cr[i]);
            g = (int) (128 + y[i] - 0.34414 * cb[i] - 0.71414 * cr[i]);
            b = (int) (128 + y[i] + 1.772 * cb[i]);
            a = alpha[i];
            rgb[i] = a << 24 | r << 16 | g << 8 | b;
        }
    }

    /**
   * Count number of blocks given line size.
   * @param dimension number of points in one dimention 
   * @return number of needed blocks
   */
    public int getBlocksDimension(int dimension) {
        int dim = dimension / Block.SIZE;
        if (dimension % Block.SIZE != 0) {
            dim++;
        }
        return dim;
    }

    /**
   * Block spliting of given data.
   * @param width data line width
   * @param values data
   * @return created blocks
   */
    public List<Block> getBlocks(int width, int[] values) {
        assert values.length % width == 0 : "Bad width.";
        int blocksX = getBlocksDimension(width);
        int height = values.length / width;
        int blocksY = getBlocksDimension(height);
        List<Block> blocks = new ArrayList<Block>();
        int[] block;
        int imageX;
        int imageY;
        Block b;
        for (int y = 0; y != blocksY; y++) {
            for (int x = 0; x != blocksX; x++) {
                b = new Block();
                block = new int[Block.SIZE * Block.SIZE];
                b.setImageValues(block);
                blocks.add(b);
                for (int blockX = 0; blockX != Block.SIZE; blockX++) {
                    for (int blockY = 0; blockY != Block.SIZE; blockY++) {
                        imageX = x * Block.SIZE + blockX;
                        imageY = y * Block.SIZE + blockY;
                        if (imageX >= width) {
                            imageX = width - 1;
                        }
                        if (imageY >= height) {
                            imageY = height - 1;
                        }
                        block[blockX + blockY * Block.SIZE] = values[imageX + imageY * width];
                    }
                }
            }
        }
        return blocks;
    }

    /**
   * Connect blocks together.
   * @param width image width
   * @param height image height
   * @param blocks blocks
   * @return image data
   */
    public int[] conectBlocks(int width, int height, List<Block> blocks) {
        int[] data = new int[width * height];
        int blocksX = getBlocksDimension(width);
        Block block;
        int offsetX;
        int offsetY;
        int x, y;
        for (int i = 0; i != blocks.size(); i++) {
            block = blocks.get(i);
            offsetX = (i % blocksX) * Block.SIZE;
            offsetY = (i / blocksX) * Block.SIZE;
            int[] values = block.getImageValues();
            for (int j = 0; j != values.length; j++) {
                x = j % Block.SIZE;
                y = j / Block.SIZE;
                if (offsetX + x >= width) {
                    continue;
                }
                if (offsetY + y >= height) {
                    continue;
                }
                data[offsetX + x + (offsetY + y) * width] = values[j];
            }
        }
        return data;
    }

    /**
   * Transform colors from rgb color space to YCrCb color space. Output arrays must be initialized.
   * 
   * @param rgb input array with rgb image values
   * @param y output array with Y-component
   * @param cb output array with Cb-component
   * @param cr output array with Cr-component
   */
    public void colorTransform(int[] rgb, int[] y, int[] cb, int[] cr) {
        assert rgb != null : "Input array must be initialized.";
        assert y != null : "Output array must be initialized.";
        assert cb != null : "Output array must be initialized.";
        assert cr != null : "Output array must be initialized.";
        int r, g, b;
        for (int i = 0; i != rgb.length; i++) {
            b = (rgb[i] & BYTE1);
            g = ((rgb[i] & BYTE2) >> 8);
            r = ((rgb[i] & BYTE3) >> 16);
            y[i] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
            cb[i] = (int) (-0.16874 * r - 0.33126 * g + 0.50000 * b);
            cr[i] = (int) (0.50000 * r - 0.41869 * g - 0.08131 * b);
        }
    }

    /**
   * DCT of block data.
   * @param values block values to transform
   * @return transformed data
   */
    public double[] dct(int[] values) {
        double[] vd = new double[Block.SIZE * Block.SIZE];
        for (int i = 0; i != values.length; i++) {
            vd[i] = values[i];
        }
        DoubleDCT_2D ddct = new DoubleDCT_2D(Block.SIZE, Block.SIZE);
        ddct.forward(vd, true);
        return vd;
    }

    /**
   * IDCT of block data.
   * @param values values to transform
   * @return transformed data
   */
    public int[] idct(int[] values) {
        double[] vd = new double[Block.SIZE * Block.SIZE];
        int[] output = new int[Block.SIZE * Block.SIZE];
        for (int i = 0; i != values.length; i++) {
            vd[i] = values[i];
        }
        DoubleDCT_2D ddct = new DoubleDCT_2D(Block.SIZE, Block.SIZE);
        ddct.inverse(vd, true);
        for (int i = 0; i != values.length; i++) {
            output[i] = (int) vd[i];
        }
        return output;
    }

    /**
   * Values quantization.
   * @param values values for quantize
   * @param luma true for luminance, false for chrominance
   * @return quantized values
   */
    public int[] quantize(double[] values, boolean luma, float scale) {
        int[] table = null;
        if (lastScale == -1 || lastScale != scale) {
            lastScale = scale;
            lastLumina = JPEGQTable.K1Luminance.getScaledInstance(scale, false).getTable();
            lastChroma = JPEGQTable.K2Chrominance.getScaledInstance(scale, false).getTable();
        }
        if (luma) {
            table = lastLumina;
        } else {
            table = lastChroma;
        }
        int[] ret = new int[values.length];
        for (int i = 0; i != ret.length; i++) {
            ret[i] = (int) Math.round(values[i] / table[i]);
        }
        return ret;
    }

    /**
   * Values reverse quantization.
   * @param values values for unquantize
   * @param luma true for luminance, false for chrominance
   * @return unquantized values
   */
    public int[] unquantize(int[] values, boolean luma, float scale) {
        int[] table = null;
        if (lastScale == -1 || lastScale != scale) {
            lastScale = scale;
            lastLumina = JPEGQTable.K1Luminance.getScaledInstance(scale, false).getTable();
            lastChroma = JPEGQTable.K2Chrominance.getScaledInstance(scale, false).getTable();
        }
        if (luma) {
            table = lastLumina;
        } else {
            table = lastChroma;
        }
        int[] ret = new int[values.length];
        for (int i = 0; i != ret.length; i++) {
            ret[i] = (int) (values[i] * table[i]);
        }
        return ret;
    }

    /**
   * Make zig-zag matrix order.
   * @param in input matrix
   * @return matrix in zig-zag order
   */
    public double[] zigZag(double[] in) {
        double[] data = new double[in.length];
        for (int i = 0; i != data.length; i++) {
            data[i] = in[ZIG_ZAG[i]];
        }
        return data;
    }

    /**
   * Make inverz zig-zag matrix order.
   * @param in input matrix in zig-zag
   * @return matrix in standard order
   */
    public double[] zigZagInverz(double[] in) {
        double[] data = new double[in.length];
        for (int i = 0; i != data.length; i++) {
            data[ZIG_ZAG[i]] = in[i];
        }
        return data;
    }

    /**
   * Make zig-zag matrix order.
   * @param in input matrix
   * @return matrix in zig-zag order
   */
    public int[] zigZag(int[] in) {
        int[] data = new int[in.length];
        for (int i = 0; i != data.length; i++) {
            data[i] = in[ZIG_ZAG[i]];
        }
        return data;
    }

    /**
   * Make inverz zig-zag matrix order.
   * @param in input matrix in zig-zag
   * @return matrix in standard order
   */
    public int[] zigZagInverz(int[] in) {
        int[] data = new int[in.length];
        for (int i = 0; i != data.length; i++) {
            data[ZIG_ZAG[i]] = in[i];
        }
        return data;
    }

    /**
   * Encode int values to byte using arithmetics coding and write to given output stream.
   * @param values int values
   * @param os output stream
   */
    public void encodeInts(int[] values, OutputStream os) {
        byte[] totalLinks = new byte[values.length * 4];
        for (int i = 0; i != values.length; i++) {
            for (int j = 0; j != 4; j++) {
                totalLinks[i * 4 + j] = (byte) (values[i] >> 8 * j);
            }
        }
        ArithCodeModel model;
        model = new AdaptiveUnigramModel();
        try {
            ArithCodeOutputStream acou = new ArithCodeOutputStream(os, model);
            acou.write(totalLinks);
            acou.flush();
            acou.close();
            os.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Problem with arithmetic coding.", e);
            throw new PackerRuntimeException("Problem with arithmetic coding.", e);
        }
    }

    /**
   * Encode half int values to byte using arithmetics coding and write to output stream.
   * @param values int values
   * @param os output stream
   */
    public void encodeHalfInts(int[] values, OutputStream os) {
        byte[] totalLinks = new byte[values.length * 2];
        for (int i = 0; i != values.length; i++) {
            for (int j = 0; j != 2; j++) {
                totalLinks[i * 2 + j] = (byte) ((values[i] + 32767) >> 8 * j);
            }
        }
        ArithCodeModel model;
        model = new AdaptiveUnigramModel();
        try {
            ArithCodeOutputStream acou = new ArithCodeOutputStream(os, model);
            acou.write(totalLinks);
            acou.flush();
            acou.close();
            os.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Problem with arithmetic coding.", e);
            throw new PackerRuntimeException("Problem with arithmetic coding.", e);
        }
    }

    /**
   * Decode byte values.
   * @param values size of decoded array (must be stored before encoding)
   * @param in input stream
   * @return decoded array
   */
    public int[] decodeBytes(int values, InputStream in) {
        ArithCodeModel model;
        model = new AdaptiveUnigramModel();
        int[] out = new int[values];
        try {
            ArithCodeInputStream acis = new ArithCodeInputStream(in, model);
            int read;
            int offset = 0;
            int readed = 0;
            int intVal = 0;
            while ((read = acis.read()) != -1) {
                intVal = intVal | (((read + 128) % 256) << (offset * 8));
                offset++;
                if (offset == 4) {
                    out[readed] = intVal;
                    intVal = 0;
                    offset = 0;
                    readed++;
                }
            }
            acis.close();
            in.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Problem with arithmetic coding.", e);
            throw new PackerRuntimeException("Problem with arithmetic coding.", e);
        }
        return out;
    }

    /**
   * Decode byte values.
   * @param values size of decoded array (must be stored before encoding)
   * @param in input stream
   * @return decoded array
   */
    public int[] decodeHalfBytes(int values, InputStream in) {
        ArithCodeModel model;
        model = new AdaptiveUnigramModel();
        int[] out = new int[values];
        try {
            ArithCodeInputStream acis = new ArithCodeInputStream(in, model);
            int read;
            int offset = 0;
            int readed = 0;
            int intVal = 0;
            while ((read = acis.read()) != -1) {
                intVal = intVal | (((read + 128) % 256) << (offset * 8));
                offset++;
                if (offset == 2) {
                    out[readed] = intVal - 32767;
                    intVal = 0;
                    offset = 0;
                    readed++;
                }
            }
            acis.close();
            in.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Problem with arithmetic coding.", e);
            throw new PackerRuntimeException("Problem with arithmetic coding.", e);
        }
        return out;
    }

    /**
   * Factory method for getting singleton.
   * @return dao instance
   */
    public static BlockCompression getInstance() {
        if (instance == null) {
            synchronized (BlockCompression.class) {
                if (instance == null) {
                    instance = new BlockCompression();
                }
            }
        }
        return instance;
    }
}
