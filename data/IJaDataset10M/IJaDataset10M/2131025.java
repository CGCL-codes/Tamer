package visad;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * ImageFlatField is a VisAD FlatField backed by a java.awt.image.BufferedImage
 * object, instead of the usual float[][] or double[][] samples array.
 * Expands the samples into floats or doubles on demand using
 * {@link #unpackFloats(boolean)}, which can be expensive for repeated
 * operations of certain types. Such calls can be avoided for certain types of
 * visualization using an 8-bit image with
 * {@link visad.bom.ShadowImageFunctionTypeJ3D}.
 */
public class ImageFlatField extends FlatField {

    /** Debugging flag. */
    public static final boolean DEBUG = false;

    /** The image backing this FlatField. */
    protected BufferedImage image;

    /** Dimensions of the image. */
    protected int num, width, height;

    /**
   * Converts the given BufferedImage to a format efficient
   * with ImageFlatField's grabBytes method and usable with
   * Java3D texturing by reference.
   */
    public static BufferedImage make3ByteRGB(BufferedImage image) {
        if (image == null) return null;
        int dataType = DataBuffer.TYPE_BYTE;
        ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false, ColorModel.TRANSLUCENT, dataType);
        int w = image.getWidth(), h = image.getHeight();
        byte[][] data = new byte[3][w * h];
        SampleModel model = new BandedSampleModel(dataType, w, h, data.length);
        DataBuffer buffer = new DataBufferByte(data, data[0].length);
        WritableRaster raster = Raster.createWritableRaster(model, buffer, null);
        BufferedImage result = new BufferedImage(colorModel, raster, false, null);
        Graphics2D g = result.createGraphics();
        g.drawRenderedImage(image, null);
        g.dispose();
        g = null;
        return result;
    }

    /** Constructs a FunctionType suitable for use with the given image. */
    public static FunctionType makeFunctionType(BufferedImage img) throws VisADException {
        if (img == null) throw new VisADException("image cannot be null");
        RealType x = RealType.getRealType("ImageElement");
        RealType y = RealType.getRealType("ImageLine");
        RealTupleType xy = new RealTupleType(x, y);
        int num = img.getRaster().getNumBands();
        MathType range = null;
        if (num == 4) {
            RealType r = RealType.getRealType("Red");
            RealType g = RealType.getRealType("Green");
            RealType b = RealType.getRealType("Blue");
            RealType a = RealType.getRealType("Alpha");
            range = new RealTupleType(r, g, b, a);
        } else if (num == 3) {
            RealType r = RealType.getRealType("Red");
            RealType g = RealType.getRealType("Green");
            RealType b = RealType.getRealType("Blue");
            range = new RealTupleType(r, g, b);
        } else if (num == 1) range = RealType.getRealType("Intensity"); else throw new VisADException("Unsupported # of bands (" + num + ")");
        return new FunctionType(xy, range);
    }

    /** Constructs a domain Set suitable for use with the given image. */
    public static Set makeDomainSet(BufferedImage img) throws VisADException {
        RealType x = RealType.getRealType("ImageElement");
        RealType y = RealType.getRealType("ImageLine");
        RealTupleType xy = new RealTupleType(x, y);
        int w = img.getWidth(), h = img.getHeight();
        return new Linear2DSet(xy, 0, w - 1, w, h - 1, 0, h);
    }

    /** Constructs an ImageFlatField around the given BufferedImage. */
    public ImageFlatField(BufferedImage img) throws VisADException, RemoteException {
        this(makeFunctionType(img), makeDomainSet(img));
        setImage(img);
    }

    public ImageFlatField(FunctionType type) throws VisADException {
        this(type, type.getDomain().getDefaultSet(), null, null, null, null);
    }

    public ImageFlatField(FunctionType type, Set domain_set) throws VisADException {
        this(type, domain_set, null, null, null, null);
    }

    public ImageFlatField(FunctionType type, Set domain_set, CoordinateSystem range_coord_sys, Set[] range_sets, Unit[] units) throws VisADException {
        this(type, domain_set, range_coord_sys, null, range_sets, units);
    }

    public ImageFlatField(FunctionType type, Set domain_set, CoordinateSystem[] range_coord_syses, Set[] range_sets, Unit[] units) throws VisADException {
        this(type, domain_set, null, range_coord_syses, range_sets, units);
    }

    public ImageFlatField(FunctionType type, Set domain_set, CoordinateSystem range_coord_sys, CoordinateSystem[] range_coord_syses, Set[] range_sets, Unit[] units) throws VisADException {
        super(type, domain_set, range_coord_sys, range_coord_syses, range_sets, units);
        RealTupleType domain = type.getDomain();
        if (domain.getNumberOfRealComponents() != 2) {
            throw new VisADException("FunctionType domain must be flat with 2 components");
        }
        MathType range = type.getRange();
        if (range instanceof RealType) num = 1; else if (range instanceof RealTupleType) {
            num = ((RealTupleType) range).getNumberOfRealComponents();
        }
        if (num != 1 && num != 3 && num != 4) {
            throw new VisADException("FunctionType range must be flat with 1, 3 or 4 components");
        }
        if (domain_set instanceof Gridded2DSet) {
            int[] len = ((Gridded2DSet) domain_set).getLengths();
            width = len[0];
            height = len[1];
        } else {
            throw new VisADException("Domain set must be Gridded2DSet");
        }
    }

    /** Gets the image backing this FlatField. */
    public BufferedImage getImage() {
        pr("getImage");
        return image;
    }

    /** Sets the image backing this FlatField. */
    public void setImage(BufferedImage image) throws VisADException, RemoteException {
        if (image == null) throw new VisADException("image cannot be null");
        if (image.getWidth() != width || image.getHeight() != height) {
            throw new VisADException("Image dimensions do not match domain set");
        }
        if (image.getRaster().getNumBands() != num) {
            throw new VisADException("Image component count does not match FunctionType range");
        }
        this.image = image;
        clearMissing();
        notifyReferences();
    }

    /** Gets RealType for each domain component (X and Y). */
    public RealType[] getDomainTypes() {
        RealTupleType domain = ((FunctionType) getType()).getDomain();
        return domain.getRealComponents();
    }

    /** Gets RealType for each range component. */
    public RealType[] getRangeTypes() {
        MathType range = ((FunctionType) getType()).getRange();
        RealType[] v = null;
        if (range instanceof RealType) v = new RealType[] { (RealType) range }; else if (range instanceof TupleType) {
            v = ((TupleType) range).getRealComponents();
        } else v = new RealType[0];
        return v;
    }

    public void setSamples(Data[] range, boolean copy) throws VisADException, RemoteException {
        throw new VisADException("Use setImage(Image) for ImageFlatField");
    }

    /**
   * This method has been overridden to avoid a call to
   * unpackValues or unpackFloats during range computation.
   */
    public DataShadow computeRanges(ShadowType type, DataShadow shadow) throws VisADException {
        if (isMissing()) return shadow;
        ShadowRealTupleType domainType = ((ShadowFunctionType) type).getDomain();
        int n = domainType.getDimension();
        double[][] ranges = new double[2][n];
        shadow = DomainSet.computeRanges(domainType, shadow, ranges, true);
        ShadowRealTupleType shadRef;
        int[] indices = ((ShadowFunctionType) type).getRangeDisplayIndices();
        boolean anyMapped = false;
        for (int i = 0; i < TupleDimension; i++) {
            if (indices[i] >= 0) anyMapped = true;
        }
        if (!anyMapped) return shadow;
        boolean anyRangeRef = (RangeCoordinateSystem != null);
        if (RangeCoordinateSystems != null) {
            for (int i = 0; i < RangeCoordinateSystems.length; i++) {
                anyRangeRef |= (RangeCoordinateSystems[i] != null);
            }
        }
        ranges = anyRangeRef ? new double[2][TupleDimension] : null;
        WritableRaster raster = image.getRaster();
        double[] min = new double[TupleDimension];
        Arrays.fill(min, Double.MAX_VALUE);
        double[] max = new double[TupleDimension];
        Arrays.fill(max, -Double.MAX_VALUE);
        double[] vals = new double[TupleDimension];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.getPixel(x, y, vals);
                for (int i = 0; i < TupleDimension; i++) {
                    min[i] = Math.min(min[i], vals[i]);
                    max[i] = Math.max(max[i], vals[i]);
                }
            }
        }
        for (int i = 0; i < TupleDimension; i++) {
            int k = indices[i];
            if (k >= 0 || anyRangeRef) {
                Unit dunit = ((RealType) ((FunctionType) Type).getFlatRange().getComponent(i)).getDefaultUnit();
                if (dunit != null && !dunit.equals(RangeUnits[i])) {
                    min[i] = dunit.toThis(min[i], RangeUnits[i]);
                    max[i] = dunit.toThis(max[i], RangeUnits[i]);
                }
                if (anyRangeRef) {
                    ranges[0][i] = Math.min(ranges[0][i], min[i]);
                    ranges[1][i] = Math.max(ranges[1][i], max[i]);
                }
                if (k >= 0 && k < shadow.ranges[0].length) {
                    shadow.ranges[0][k] = Math.min(shadow.ranges[0][k], min[i]);
                    shadow.ranges[1][k] = Math.max(shadow.ranges[1][k], max[i]);
                }
            }
        }
        if (RangeCoordinateSystem != null) {
            ShadowRealTupleType rangeType = (ShadowRealTupleType) ((ShadowFunctionType) type).getRange();
            shadRef = rangeType.getReference();
            shadow = computeReferenceRanges(rangeType, RangeCoordinateSystem, RangeUnits, shadow, shadRef, ranges);
        } else if (RangeCoordinateSystems != null) {
            TupleType rangeTupleType = (TupleType) ((FunctionType) Type).getRange();
            int j = 0;
            for (int i = 0; i < RangeCoordinateSystems.length; i++) {
                MathType component = rangeTupleType.getComponent(i);
                if (component instanceof RealType) j++; else {
                    int m = ((RealTupleType) component).getDimension();
                    if (RangeCoordinateSystems[i] != null) {
                        double[][] subRanges = new double[2][m];
                        Unit[] subUnits = new Unit[m];
                        for (int k = 0; k < m; k++) {
                            subRanges[0][k] = ranges[0][j];
                            subRanges[1][k] = ranges[1][j];
                            subUnits[k] = RangeUnits[j];
                            j++;
                        }
                        ShadowRealTupleType rangeType = (ShadowRealTupleType) ((ShadowTupleType) ((ShadowFunctionType) type).getRange()).getComponent(i);
                        shadRef = rangeType.getReference();
                        shadow = computeReferenceRanges(rangeType, RangeCoordinateSystems[i], subUnits, shadow, shadRef, subRanges);
                    } else {
                        j += m;
                    }
                }
            }
        }
        return shadow;
    }

    /**
   * Unpacks an array of doubles from field sample values.
   *
   * @param copy            Ignored (always returns a copy).
   */
    protected double[][] unpackValues(boolean copy) throws VisADException {
        pr("unpackValues(" + copy + ")");
        Raster r = image.getRaster();
        double[][] samps = new double[num][width * height];
        for (int c = 0; c < num; c++) r.getSamples(0, 0, width, height, c, samps[c]);
        return samps;
    }

    /**
   * Unpacks an array of floats from field sample values.
   *
   * @param copy            Ignored (always returns a copy).
   */
    protected float[][] unpackFloats(boolean copy) throws VisADException {
        pr("unpackFloats(" + copy + ")");
        Raster r = image.getRaster();
        float[][] samps = new float[num][width * height];
        for (int c = 0; c < num; c++) r.getSamples(0, 0, width, height, c, samps[c]);
        return samps;
    }

    protected double[] unpackValues(int s_index) throws VisADException {
        pr("unpackValues(" + s_index + ")");
        Raster r = image.getRaster();
        double[] samps = new double[num];
        r.getPixel(s_index % width, s_index / width, samps);
        return samps;
    }

    protected float[] unpackFloats(int s_index) throws VisADException {
        pr("unpackFloats(" + s_index + ")");
        Raster r = image.getRaster();
        float[] samps = new float[num];
        r.getPixel(s_index % width, s_index / width, samps);
        return samps;
    }

    protected double[] unpackOneRangeComp(int comp) throws VisADException {
        pr("unpackOneRangeComp(" + comp + ")");
        Raster r = image.getRaster();
        double[] samps = new double[width * height];
        r.getSamples(0, 0, width, height, comp, samps);
        return samps;
    }

    public Data getSample(int index) throws VisADException, RemoteException {
        double[] v = unpackValues(index);
        RealTupleType range = (RealTupleType) ((FunctionType) getType()).getRange();
        return new RealTuple(range, v);
    }

    protected void pr(String message) {
        if (DEBUG) {
            String s = hashCode() + " " + getClass().getName() + "  " + message;
            new Exception(s).printStackTrace();
        }
    }

    public void setSamples(double[][] range, boolean copy) throws RemoteException, VisADException {
        throw new VisADException("Use setImage(Image) for ImageFlatField");
    }

    public void setSamples(float[][] range, boolean copy) throws RemoteException, VisADException {
        throw new VisADException("Use setImage(Image) for ImageFlatField");
    }

    public void setSamples(double[][] range, ErrorEstimate[] errors, boolean copy) throws RemoteException, VisADException {
        throw new VisADException("Use setImage(Image) for ImageFlatField");
    }

    public void setSamples(int start, double[][] range) throws RemoteException, VisADException {
        throw new VisADException("Use setImage(Image) for ImageFlatField");
    }

    public void setSamples(float[][] range, ErrorEstimate[] errors, boolean copy) throws RemoteException, VisADException {
        throw new VisADException("Use setImage(Image) for ImageFlatField");
    }

    public byte[][] grabBytes() {
        pr("grabBytes");
        byte[][] data = grabBytes(image);
        if (data == null) return null;
        if (data.length > num) {
            byte[][] bytes = new byte[num][];
            System.arraycopy(data, 0, bytes, 0, num);
            data = bytes;
        }
        return data;
    }

    public static byte[][] grabBytes(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        if (raster.getTransferType() != DataBuffer.TYPE_BYTE) return null;
        DataBuffer buffer = raster.getDataBuffer();
        if (buffer instanceof DataBufferByte) {
            SampleModel model = raster.getSampleModel();
            if (model instanceof BandedSampleModel) {
                if (DEBUG) System.err.println("grabBytes: FAST");
                byte[][] data = ((DataBufferByte) buffer).getBankData();
                return data;
            } else if (model instanceof ComponentSampleModel) {
                if (DEBUG) System.err.println("grabBytes: MEDIUM");
                byte[][] data = ((DataBufferByte) buffer).getBankData();
                ComponentSampleModel csm = (ComponentSampleModel) model;
                int[] bandOffsets = csm.getBandOffsets();
                int[] bankIndices = csm.getBankIndices();
                int pixelStride = csm.getPixelStride();
                int scanlineStride = csm.getScanlineStride();
                int numBands = bandOffsets.length;
                int width = image.getWidth();
                int height = image.getHeight();
                int numPixels = width * height;
                byte[][] bytes = new byte[numBands][numPixels];
                for (int c = 0; c < numBands; c++) {
                    for (int h = 0; h < height; h++) {
                        for (int w = 0; w < width; w++) {
                            int ndx = width * h + w;
                            int q = bandOffsets[c] + h * scanlineStride + w * pixelStride;
                            bytes[c][ndx] = data[bankIndices[c]][q];
                        }
                    }
                }
                return bytes;
            }
        }
        if (DEBUG) System.err.println("grabBytes: make3ByteRGB");
        return grabBytes(make3ByteRGB(image));
    }
}
