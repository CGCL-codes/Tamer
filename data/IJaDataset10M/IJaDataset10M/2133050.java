package co.edu.unal.ungrid.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import co.edu.unal.ungrid.client.util.AbstractDimension;
import co.edu.unal.ungrid.client.util.AbstractPosition;
import co.edu.unal.ungrid.client.util.Dimension2D;
import co.edu.unal.ungrid.client.util.Dimension3D;
import co.edu.unal.ungrid.client.util.Position2D;
import co.edu.unal.ungrid.client.util.Position3D;
import co.edu.unal.ungrid.common.NumberUtil;
import co.edu.unal.ungrid.common.StringUtil;
import co.edu.unal.ungrid.common.TimeUtil;
import co.edu.unal.ungrid.expert.view.RgbHistogramFrame;
import co.edu.unal.ungrid.image.dicom.core.DescriptionFactory;
import co.edu.unal.ungrid.image.dicom.display.SourceImage;
import co.edu.unal.ungrid.image.processing.ImageProcessor;
import co.edu.unal.ungrid.image.processing.filter.FilterFactory;
import co.edu.unal.ungrid.image.processing.filter.WithAndCenterFilter;
import co.edu.unal.ungrid.image.processing.filter.WithAndCenterParameters;
import co.edu.unal.ungrid.image.processing.interpolation.AffineSplineInterpolator;
import co.edu.unal.ungrid.image.util.ImageTileHelper;
import co.edu.unal.ungrid.staple.Segmentation;
import co.edu.unal.ungrid.staple.SegmentationStack;
import co.edu.unal.ungrid.transformation.AffineTransform;
import co.edu.unal.ungrid.xml.XmlUtil;

public class ImageUtil {

    private ImageUtil() {
    }

    public static String getImageModality(final String sImgDesc) {
        assert sImgDesc != null;
        int lb = sImgDesc.indexOf('{');
        int rb = sImgDesc.indexOf('}');
        return (lb >= 0 && rb > 0 ? sImgDesc.substring(lb + 1, rb) : "?");
    }

    public static String getImageOrientation(final String sImgDesc) {
        assert sImgDesc != null;
        int b = sImgDesc.lastIndexOf(' ');
        return (b > 0 ? sImgDesc.substring(b + 1, sImgDesc.length() - 1) : "?");
    }

    public static ViewType getImageType(final String sImgDesc) {
        assert sImgDesc != null;
        ViewType iType = null;
        if (DBG > 0) {
            System.out.println("idesc=" + sImgDesc);
        }
        int i = sImgDesc.indexOf(DescriptionFactory.AXIAL);
        if (i >= 0) {
            iType = ViewType.AXIAL;
        } else {
            i = sImgDesc.indexOf(DescriptionFactory.CORONAL);
            if (i >= 0) {
                iType = ViewType.CORONAL;
            } else {
                i = sImgDesc.indexOf(DescriptionFactory.SAGITTAL);
                if (i >= 0) {
                    iType = ViewType.SAGITTAL;
                }
            }
        }
        return iType;
    }

    public static FloatImage floatImageFromGrayScaleImage(final GrayScaleImage gi) {
        FloatImage fi = null;
        if (gi != null) {
            fi = new FloatImage(gi.getSize());
            for (int p = 0; p < gi.numPlanes(); p++) {
                FloatPlane plane = gi.floatPlane(p);
                fi.fromArray(p, plane.floatArray());
            }
        }
        return fi;
    }

    public static Segmentation segmentationFromGrayScaleImage(final GrayScaleImage gi, int threshold) {
        Segmentation s = null;
        if (gi != null) {
            s = new Segmentation(gi.getSize());
            s.setFileName(gi.getFileName());
            for (int p = 0; p < gi.numPlanes(); p++) {
                int[] ia = gi.intArray(p);
                double[] fa = new double[ia.length];
                for (int i = 0; i < fa.length; i++) {
                    fa[i] = (ia[i] < threshold ? 0.0 : 1.0);
                }
                s.fromArray(p, fa);
            }
        }
        return s;
    }

    public static GrayScaleImage giFromDoubleImage(final AbstractImage<FloatPlane> di) {
        GrayScaleImage gi = null;
        if (di != null) {
            gi = new GrayScaleImage(di.getSize());
            gi.setFileName(di.getFileName());
            for (int p = 0; p < di.numPlanes(); p++) {
                RgbPlane plane = di.intPlane(p);
                gi.fromArray(p, plane.intArray());
            }
        }
        return gi;
    }

    public static GrayScaleImage giFromTrueSegment(final Segmentation si) {
        GrayScaleImage gi = null;
        if (si != null) {
            gi = new GrayScaleImage(si.getSize());
            for (int p = 0; p < si.numPlanes(); p++) {
                double[] in = si.floatArray(p);
                int[] out = gi.intArray(p);
                for (int v = 0; v < out.length; v++) {
                    out[v] = (in[v] < 0.5 ? BLACK : WHITE);
                }
            }
        }
        return gi;
    }

    public static Segmentation segmentFromGrayScale(final GrayScaleImage gi, int threshold) {
        assert gi != null;
        Segmentation seg = null;
        if (gi != null) {
            double[] data = new double[gi.numVoxels()];
            for (int p = 0, dstPos = 0; p < gi.numPlanes(); p++) {
                int[] in = gi.intArray(p);
                for (int i = 0; i < in.length; i++) {
                    data[dstPos++] = (in[i] < threshold ? 0.0 : 1.0);
                }
            }
            seg = new Segmentation(gi.getSize(), data);
            seg.setFileName(gi.getSimpleName());
        }
        return seg;
    }

    @SuppressWarnings("unchecked")
    public static GrayScaleImage giFromSegment(final Segmentation seg) {
        assert seg != null;
        GrayScaleImage gi = null;
        if (seg != null) {
            gi = new GrayScaleImage((Dimension3D) seg.getSize());
            gi.setFileName(seg.getFileName());
            double[] in = seg.getData();
            for (int p = 0, dstPos = 0; p < gi.numPlanes(); p++) {
                int[] out = gi.intArray(p);
                for (int i = 0; i < out.length; i++) {
                    out[dstPos++] = (in[i] > 0.0 ? WHITE : BLACK);
                }
            }
        }
        return gi;
    }

    public static Segmentation[] buildNoisySegments(final FloatImage img, int nSegments, double fNoise) {
        Segmentation[] sega = new Segmentation[nSegments];
        int w = img.getWidth();
        int h = img.getHeight();
        int d = img.numPlanes();
        Dimension3D<Integer> sDim = new Dimension3D<Integer>(w, h, d);
        int sliceSize = w * h;
        for (int s = 0; s < sega.length; s++) {
            double[] sData = new double[w * h * d];
            for (int p = 0; p < d; p++) {
                FloatPlane dp = img.floatPlane(p);
                System.arraycopy(dp.floatArray(), 0, sData, p * sliceSize, sliceSize);
            }
            int nPoints = (int) (fNoise * w * h * d);
            for (int n = 0; n < nPoints; n++) {
                int x = (int) (Math.random() * w);
                int y = (int) (Math.random() * h);
                int z = (int) (Math.random() * d);
                int pix = x + y * w + z * sliceSize;
                sData[pix] = FloatPlane.WHITE - sData[pix];
            }
            sega[s] = new Segmentation(sDim, sData);
        }
        return sega;
    }

    public static <N extends Number> boolean checkDimension(final AbstractDimension<N> dim, final AbstractDimension<N> min, final AbstractDimension<N> max) {
        assert dim != null;
        assert min != null;
        assert max != null;
        assert dim.getDims() == min.getDims();
        assert dim.getDims() == max.getDims();
        boolean bValid = true;
        for (int i = 0; i < dim.getDims(); i++) {
            double d = dim.get(i).doubleValue();
            if (d < min.get(i).doubleValue() || max.get(i).doubleValue() < d) {
                bValid = false;
                break;
            }
        }
        return bValid;
    }

    public static <N extends Number> boolean checkDimensions(final AbstractDimension<N> domnSize, final AbstractDimension<N> tileSize, final AbstractDimension<N> brdrSize) {
        assert domnSize != null;
        assert tileSize != null;
        assert brdrSize != null;
        boolean bValid = true;
        if (domnSize.getDims() != tileSize.getDims() || domnSize.getDims() != brdrSize.getDims()) {
            bValid = false;
        } else {
            AbstractDimension<N> minDomain = domnSize.newInstance();
            for (int i = 0; i < minDomain.getDims(); i++) {
                minDomain.set(i, 1);
            }
            AbstractDimension<N> maxDomain = domnSize.newInstance();
            for (int i = 0; i < maxDomain.getDims(); i++) {
                maxDomain.set(i, Integer.MAX_VALUE);
            }
            AbstractDimension<N> minTile = tileSize.newInstance();
            for (int i = 0; i < minTile.getDims(); i++) {
                minTile.set(i, 1);
            }
            AbstractDimension<N> maxTile = tileSize.newInstance();
            for (int i = 0; i < maxTile.getDims(); i++) {
                maxTile.set(i, domnSize.get(i));
            }
            AbstractDimension<N> minBrdr = brdrSize.newInstance();
            for (int i = 0; i < minBrdr.getDims(); i++) {
                minBrdr.set(i, 0);
            }
            AbstractDimension<N> maxBrdr = brdrSize.newInstance();
            for (int i = 0; i < maxBrdr.getDims(); i++) {
                maxBrdr.set(i, tileSize.get(i).doubleValue() - Double.MIN_VALUE);
            }
            bValid = checkDimension(domnSize, minDomain, maxDomain);
            if (bValid) {
                bValid = checkDimension(tileSize, minTile, maxTile);
                if (bValid) {
                    bValid = checkDimension(brdrSize, minBrdr, maxBrdr);
                }
            }
        }
        return bValid;
    }

    public static AbstractDimension<Integer> getTileSize(final AbstractImage<FloatPlane> img, final Position3D<Integer> pos, final AbstractDimension<Integer> size) {
        assert img != null;
        assert pos != null;
        assert size != null;
        return img.getTileSize(pos, size);
    }

    @SuppressWarnings("unchecked")
    public static SegmentationStack buildSegmentStack(final Segmentation[] lst, final Position3D<Integer> pos, final AbstractDimension<Integer> tileSize, final AbstractDimension<Integer> brdrSize, double beta) {
        assert lst != null;
        Segmentation[] sega = new Segmentation[lst.length];
        ImageTileHelper<FloatPlane>[] helper = new ImageTileHelper[lst.length];
        for (int i = 0; i < helper.length; i++) {
            helper[i] = new ImageTileHelper<FloatPlane>(lst[i], tileSize, brdrSize);
        }
        for (int s = 0; s < sega.length; s++) {
            sega[s] = new Segmentation(helper[s].getTile(pos));
        }
        return new SegmentationStack(sega, beta);
    }

    public static SegmentationStack[][][] buildSegmentStackArray(final Segmentation[] lst, final AbstractDimension<Integer> tileSize, final AbstractDimension<Integer> brdrSize, double beta) {
        assert lst != null;
        assert lst.length > 0;
        SegmentationStack[][][] segStacka = null;
        AbstractImage<FloatPlane> img = lst[0];
        if (checkDimensions(img.getSize(), tileSize, brdrSize)) {
            ImageTileHelper<FloatPlane> helper = new ImageTileHelper<FloatPlane>(img, tileSize, brdrSize);
            int nw = helper.numTilesX();
            int nh = helper.numTilesY();
            int nd = helper.numTilesZ();
            segStacka = new SegmentationStack[nd][nh][nw];
            Position3D<Integer> pos = new Position3D<Integer>(0, 0, 0);
            for (int z = 0; z < nd; z++) {
                pos.set(AbstractPosition.Z, z);
                for (int y = 0; y < nh; y++) {
                    pos.set(AbstractPosition.Y, y);
                    for (int x = 0; x < nw; x++) {
                        pos.set(AbstractPosition.X, x);
                        segStacka[z][y][x] = buildSegmentStack(lst, pos, tileSize, brdrSize, beta);
                    }
                }
            }
        }
        return segStacka;
    }

    public static <Plane extends AbstractPlane> SegmentationStack buildNoisySegmentStack(final AbstractImage<Plane> tile, int experts, double noise, double beta) {
        assert tile != null;
        assert experts > 0;
        assert noise >= 0.0;
        int tw = tile.getWidth();
        int th = tile.getHeight();
        int td = tile.getDepth();
        int sliceSize = tw * th;
        Segmentation[] sega = new Segmentation[experts];
        for (int s = 0; s < sega.length; s++) {
            double[] sData = new double[tile.numVoxels()];
            for (int p = 0; p < tile.numPlanes(); p++) {
                double[] tData = tile.floatArray(p);
                System.arraycopy(tData, 0, sData, p * tData.length, tData.length);
            }
            int nPoints = (int) (noise * tw * th * td);
            for (int n = 0; n < nPoints; n++) {
                int x = (int) (Math.random() * tw);
                int y = (int) (Math.random() * th);
                int z = (int) (Math.random() * td);
                int pix = x + y * tw + z * sliceSize;
                sData[pix] = 1.0 - sData[pix];
            }
            sega[s] = new Segmentation(tile.getSize(), sData);
        }
        return new SegmentationStack(sega, beta);
    }

    public static GrayScaleImage build3Dfrom2D(GrayScaleImage gi, int depth) {
        assert gi != null;
        assert depth > 1;
        GrayScaleImage out = new GrayScaleImage(new Dimension3D<Integer>(gi.getWidth(), gi.getHeight(), depth));
        for (int p = 0; p < depth; p++) {
            out.setPlane(p, gi.getPlane());
        }
        return out;
    }

    private static int[][] getRandom(int length, int max) {
        int[][] ia = new int[length][length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                ia[y][x] = -1;
            }
        }
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                int idx = -1;
                do {
                    idx = (int) (Math.random() * max);
                } while (ia[y][idx] != -1);
                ia[y][x] = idx;
            }
        }
        return ia;
    }

    public static Segmentation[] build3DSegments(final Segmentation[] in) {
        assert in != null;
        assert in[0] != null;
        Segmentation[] out = new Segmentation[in.length];
        Dimension3D<Integer> outSize = new Dimension3D<Integer>(in[0].getWidth(), in[0].getHeight(), in.length);
        int[][] ia = getRandom(in.length, in.length);
        for (int y = 0; y < ia.length; y++) {
            Segmentation seg = new Segmentation(outSize);
            for (int x = 0; x < seg.getDepth(); x++) {
                seg.setPlane(x, in[ia[y][x]].doublePlane());
            }
            out[y] = seg;
        }
        return out;
    }

    public static SegmentationStack[][][] buildSyntheticSegmentStackArray(final FloatImage img, final Dimension3D<Integer> tileSize, Dimension3D<Integer> brdrSize, int experts, double noise, double beta) {
        assert img != null;
        assert tileSize != null;
        ImageTileHelper<FloatPlane> helper = new ImageTileHelper<FloatPlane>(img, tileSize, brdrSize);
        int nx = helper.numTilesX();
        int ny = helper.numTilesY();
        int nz = helper.numTilesZ();
        SegmentationStack[][][] segStacka = new SegmentationStack[nz][ny][nx];
        for (int z = 0; z < nz; z++) {
            for (int y = 0; y < ny; y++) {
                for (int x = 0; x < nx; x++) {
                    AbstractImage<FloatPlane> tile = helper.getTileAtIndex(x, y, z);
                    segStacka[z][y][x] = buildNoisySegmentStack(tile, experts, noise, beta);
                }
            }
        }
        return segStacka;
    }

    public static void assembleTrueSegment(final Segmentation[][][] sega, final AbstractDimension<Integer> tileSize, final AbstractDimension<Integer> brdrSize, final Segmentation trueSeg) {
        ImageTileHelper<FloatPlane> helper = new ImageTileHelper<FloatPlane>(trueSeg, tileSize, brdrSize);
        assert sega[0][0].length == helper.numTilesX();
        assert sega[0].length == helper.numTilesY();
        assert sega.length == helper.numTilesZ();
        int nx = helper.numTilesX();
        int ny = helper.numTilesY();
        int nz = helper.numTilesZ();
        Position3D<Integer> pos = new Position3D<Integer>(0, 0, 0);
        for (int z = 0; z < nz; z++) {
            pos.set(AbstractPosition.Z, z);
            for (int y = 0; y < ny; y++) {
                pos.set(AbstractPosition.Y, y);
                for (int x = 0; x < nx; x++) {
                    pos.set(AbstractPosition.X, x);
                    ImageTileHelper.setTileAtIndex(sega[z][y][x], pos, brdrSize, trueSeg);
                }
            }
        }
    }

    public static <Plane extends AbstractPlane> AbstractImage<Plane> noisyCopy(final AbstractImage<Plane> img, double noise) {
        assert img != null;
        AbstractImage<Plane> copy = img.clone();
        copy.addNoise(noise);
        return copy;
    }

    public static File readImageFilename(final String sTitle, final String sDataDir) {
        assert sTitle != null;
        JFileChooser fc = new JFileChooser(sDataDir == null ? "." : sDataDir);
        fc.setDialogTitle(sTitle);
        fc.setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String sName = f.getName();
                int dot = sName.lastIndexOf('.');
                if (dot < 0) {
                    return true;
                }
                boolean isSupported = false;
                for (int i = 0; i < IMG_FILE_EXT.length; i++) {
                    if (sName.endsWith(IMG_FILE_EXT[i])) {
                        isSupported = true;
                        break;
                    }
                }
                return isSupported;
            }

            public String getDescription() {
                StringBuffer sExts = new StringBuffer();
                for (int i = 0; i < IMG_FILE_EXT.length; i++) {
                    sExts.append(IMG_FILE_EXT[i]);
                    if (i < IMG_FILE_EXT.length - 1) {
                        sExts.append(", ");
                    }
                }
                return "Image Files (" + sExts.toString() + ")";
            }
        });
        fc.showOpenDialog(null);
        return fc.getSelectedFile();
    }

    public static <Plane extends AbstractPlane> Histogram<Integer> getHistogram(final AbstractImage<Plane> img, int hsize) {
        assert img != null;
        assert hsize > 0;
        return getHistogram(img.intArray(), hsize);
    }

    public static <N extends Number> Histogram<N> getHistogram(int[] na, int hsize) {
        assert hsize > 0;
        assert na != null;
        return new Histogram<N>(na, hsize);
    }

    public static <N extends Number, Plane extends AbstractPlane> JointHistogram<N> getJointHistogram(final AbstractImage<Plane> iOne, final AbstractImage<Plane> iTwo, final Dimension2D<Integer> size) {
        assert iOne != null;
        assert iTwo != null;
        return getJointHistogram(iOne.intArray(), iTwo.intArray(), size);
    }

    public static <N extends Number> JointHistogram<N> getJointHistogram(int[] naOne, int[] naTwo, final Dimension2D<Integer> size) {
        assert size != null;
        assert size.getWidth().intValue() > 0 && size.getHeight().intValue() > 0;
        assert naOne != null;
        assert naTwo != null;
        return new JointHistogram<N>(naOne, naTwo, size);
    }

    public static int verifyThisIsInfimum(double[] vTmp, double[] vSrc, int idx_p, int small_q, int large_q) {
        assert vTmp != null;
        assert vSrc != null;
        double small = vTmp[small_q];
        double large = vSrc[large_q];
        int bin_p = idx_p;
        for (int idp = idx_p + 1; idp < vTmp.length; idp++) {
            if (vTmp[idx_p] < vTmp[idp]) {
                if (small < vTmp[idp] && vTmp[idp] <= large) {
                    bin_p = idp;
                } else {
                    break;
                }
            }
        }
        return bin_p;
    }

    public static void findMatchedBin(double[] vTmp, double[] vSrc, int idx_p, int idx_q, int[] param) {
        assert vTmp != null;
        assert vSrc != null;
        if (vTmp[idx_p] > vSrc[idx_q]) {
            int small_q = idx_q;
            for (int idq = idx_q + 1; idq < vTmp.length; idq++) {
                if (vTmp[idx_p] <= vSrc[idq]) {
                    int large_q = idq;
                    int bin_p = verifyThisIsInfimum(vTmp, vSrc, idx_p, small_q, large_q);
                    param[0] = bin_p;
                    param[1] = small_q;
                    param[2] = large_q;
                    break;
                }
            }
        }
    }

    private static void equalize(int[] viSrc, int[] viTrn, int[] viDst) {
        assert viSrc != null;
        assert viTrn != null;
        assert viDst != null;
        for (int i = 0; i < viSrc.length; i++) {
            if (viSrc[i] >= 0) {
                viDst[i] = viTrn[viSrc[i]];
            } else {
                viDst[i] = viSrc[i];
            }
        }
    }

    public static <Plane extends AbstractPlane> void equalize(final AbstractImage<Plane> iRef, final AbstractImage<Plane> iSrc, final AbstractImage<Plane> iDst) {
        assert iRef != null;
        assert iSrc != null;
        assert iDst != null;
        Histogram<Integer> hTmp = getHistogram(iRef, GRAY_HISTOGRAM_SIZE);
        Histogram<Integer> hSrc = getHistogram(iSrc, GRAY_HISTOGRAM_SIZE);
        double[] vTmp = hTmp.cummulatedSum();
        double[] vSrc = hSrc.cummulatedSum();
        int[] vParam = new int[3];
        int[] vTrans = new int[GRAY_HISTOGRAM_SIZE];
        int last_q = 0;
        for (int idx_q = 0, idx_p = 0; idx_p < GRAY_HISTOGRAM_SIZE; idx_p++) {
            findMatchedBin(vTmp, vSrc, idx_p, idx_q, vParam);
            if (vParam[0] != 0 || vParam[1] != 0 || vParam[2] != 0) {
                int min_p = vParam[0];
                int first_q = vParam[1];
                if (last_q > 0 && first_q <= last_q) {
                    first_q = last_q + 1;
                }
                last_q = vParam[2];
                if (last_q < vTrans.length) {
                    for (int i = first_q; i <= last_q; i++) {
                        vTrans[i] = min_p;
                    }
                }
            }
            idx_q = last_q;
            vParam[0] = vParam[1] = vParam[2] = 0;
        }
        equalize(iSrc.intArray(), vTrans, iDst.intArray());
        iDst.reset();
    }

    public static <Plane extends AbstractPlane> AbstractImage<Plane> equalize(final AbstractImage<Plane> iTmp, final AbstractImage<Plane> iSrc) {
        AbstractImage<Plane> iDst = iSrc.newImage();
        equalize(iTmp, iSrc, iDst);
        return iDst;
    }

    public static <Plane extends AbstractPlane> double getMin(final AbstractImage<Plane> iSrc) {
        double min = Double.MAX_VALUE;
        int[] iaSrc = iSrc.intArray();
        for (int i = 0; i < iaSrc.length; i++) {
            if (iaSrc[i] < min) {
                min = iaSrc[i];
            }
        }
        return min;
    }

    public static <Plane extends AbstractPlane> double getMax(final AbstractImage<Plane> iSrc) {
        double max = Double.MIN_VALUE;
        int[] iaSrc = iSrc.intArray();
        for (int i = 0; i < iaSrc.length; i++) {
            if (iaSrc[i] > max) {
                max = iaSrc[i];
            }
        }
        return max;
    }

    public static <Plane extends AbstractPlane> AbstractImage<Plane> segment(final AbstractImage<Plane> iSrc, double threshold) {
        AbstractImage<Plane> iDst = iSrc.newImage();
        iDst.clear(OUTLIER);
        iDst.setFileName(iSrc.getFileName());
        double max = getMax(iSrc);
        int min = (int) (threshold * max);
        int[] iaSrc = iSrc.intArray();
        int[] iaDst = iDst.intArray();
        for (int i = 0; i < iaDst.length; i++) {
            if (iaSrc[i] > min) {
                iaDst[i] = iaSrc[i];
            }
        }
        return iDst.reset();
    }

    public static int[] byteToInt(byte[] ba, int numBands) {
        assert ba != null;
        assert numBands > 0;
        assert ba.length % numBands == 0;
        if (DBG > 1) System.out.println("ImageUtils::byteToInt(): ba=" + ba.length);
        int[] ia = new int[ba.length / numBands];
        return byteToInt(ba, numBands, ia);
    }

    public static int[] byteToInt(byte[] ba, int numBands, int[] ia) {
        assert ba != null;
        assert numBands > 0;
        assert ia != null;
        assert ba.length == ia.length * numBands;
        for (int i = 0, b = 0; i < ia.length; i++, b += numBands) {
            ia[i] = ba[b] & 0xFF;
        }
        return ia;
    }

    public static byte[] intToByte(int[] ia, int numBands) {
        assert ia != null;
        byte[] ba = new byte[ia.length * numBands];
        intToByte(ia, ba, numBands);
        return ba;
    }

    public static void intToByte(int[] ia, byte[] ba, int numBands) {
        assert ia != null;
        assert ba != null;
        assert ba.length == ia.length * numBands;
        for (int i = 0, j = 0; i < ba.length; i += numBands, j++) {
            for (int b = 0; b < numBands; b++) {
                ba[i + b] = (byte) ia[j];
            }
        }
    }

    public static <Plane extends AbstractPlane> BufferedImage toBufferedImage(final AbstractImage<Plane> img) {
        BufferedImage bi = null;
        if (img != null) {
            RgbImage ai = imageToRgbImage(img);
            bi = new BufferedImage(ai.getWidth(), ai.getHeight(), BufferedImage.TYPE_INT_RGB);
            bi.setRGB(0, 0, ai.getWidth(), ai.getHeight(), ai.intArray(ai.getDepth() / 2), 0, ai.getWidth());
        }
        return bi;
    }

    public static GrayScaleImage getGrayImage(final Dimension3D<Integer> size, int[] na, final String sFileName) {
        assert size != null;
        assert na != null;
        assert sFileName != null;
        GrayScaleImage gi = new GrayScaleImage(size, na);
        gi.setFileName(sFileName);
        return gi;
    }

    public static <Plane extends AbstractPlane> GrayScaleImage rgbToGrayScale(final AbstractImage<Plane> iSrc) {
        assert iSrc != null;
        int[] naDst = null;
        if (iSrc != null) {
            int[] naSrc = iSrc.intArray();
            naDst = new int[naSrc.length];
            for (int i = 0; i < naDst.length; i++) {
                naDst[i] = naSrc[i] & 0xFF;
            }
        }
        GrayScaleImage gi = null;
        if (naDst != null) {
            gi = new GrayScaleImage(iSrc.getSize(), naDst);
            gi.setFileName(iSrc.getFileName());
            gi.setDcmSource(iSrc.getDcmSource());
        }
        return gi;
    }

    public static <Plane extends AbstractPlane> RgbImage imageToRgbImage(final AbstractImage<Plane> in) {
        assert in != null;
        assert in.intArray() != null;
        RgbImage out = null;
        if (in != null) {
            if (in.getType() == BufferedImage.TYPE_INT_RGB) {
                out = (RgbImage) in;
            } else {
                out = new RgbImage(in.getSize());
                for (int p = 0; p < in.numPlanes(); p++) {
                    int[] naSrc = in.intArray(p);
                    int[] naDst = out.intArray(p);
                    if (naSrc != null && naDst != null) {
                        for (int i = 0; i < naDst.length; i++) {
                            int v = naSrc[i];
                            naDst[i] = (v << 16) | (v << 8) | v;
                        }
                    }
                }
            }
        }
        return out;
    }

    public static boolean isRawImage(final String sFileName) {
        assert sFileName != null;
        return sFileName.endsWith(GSI_EXT);
    }

    public static ByteImage loadAsByteImage(final String sFileName) {
        ByteImage img = null;
        if (sFileName != null) {
            try {
                SourceImage dcmSrc = new SourceImage();
                BufferedImage bi = ImageFactory.load(new File(sFileName), dcmSrc);
                if (bi != null) {
                    img = new ByteImage(bi);
                    if (img != null) {
                        img.setFileName(sFileName);
                        img.setDcmSource(dcmSrc);
                    }
                }
            } catch (Exception exc) {
                System.out.println("ImageUtils::loadAsByteImage(): " + sFileName + "=" + exc);
            }
        }
        return img;
    }

    public static RgbImage loadAsRgbImage(final String sFileName) {
        RgbImage img = null;
        if (sFileName != null) {
            try {
                SourceImage dcmSrc = new SourceImage();
                BufferedImage bi = ImageFactory.load(new File(sFileName), dcmSrc);
                if (bi != null) {
                    img = new RgbImage(bi);
                    if (img != null) {
                        img.setFileName(sFileName);
                        img.setDcmSource(dcmSrc);
                    }
                }
            } catch (Exception exc) {
                System.out.println("ImageUtils::loadAsRgbImage(): " + sFileName + "=" + exc);
            }
        }
        return img;
    }

    public static FloatImage loadAsFloatImage(final String sFileName) {
        FloatImage img = null;
        if (sFileName != null) {
            try {
                SourceImage dcmSrc = new SourceImage();
                BufferedImage bi = ImageFactory.load(new File(sFileName), dcmSrc);
                if (bi != null) {
                    img = new FloatImage(bi);
                    if (img != null) {
                        img.setFileName(sFileName);
                        img.setDcmSource(dcmSrc);
                    }
                }
            } catch (Exception exc) {
                System.out.println("ImageUtils::loadAsFloatImage(): " + sFileName + "=" + exc);
            }
        }
        return img;
    }

    public static GrayScaleImage loadAsGrayScaleImage(final String sFileName) {
        GrayScaleImage gi = null;
        RgbImage img = loadAsRgbImage(sFileName);
        if (img != null) {
            gi = rgbToGrayScale(img);
        }
        return gi;
    }

    public static AbstractImage<RgbPlane> getOglConformantImage(final String sImgName) {
        return getOpenGlImage(loadAsGrayScaleImage(sImgName), true);
    }

    public static AbstractImage<RgbPlane> getOglConformantImage(final String sImgName, boolean clear) {
        assert sImgName != null;
        GrayScaleImage gi = loadAsGrayScaleImage(sImgName);
        return (gi != null ? getOpenGlImage(gi, clear) : null);
    }

    public static <Plane extends AbstractPlane> AbstractImage<Plane> getOpenGlImage(final AbstractImage<Plane> src) {
        return getOpenGlImage(src, true);
    }

    public static <Plane extends AbstractPlane> AbstractImage<Plane> getOpenGlImage(final AbstractImage<Plane> src, boolean clear) {
        AbstractImage<Plane> img = null;
        if (src != null) {
            if (isOglConformant(src)) {
                img = src;
            } else {
                img = getOpenGlConformant(src, clear);
            }
            assert isOglConformant(img);
        }
        return img;
    }

    public static void transpose(double[] in, int scanline, double[] out) {
        assert in != null;
        assert out != null;
        assert in.length == out.length;
        assert in.length % scanline == 0;
        int w = scanline;
        int h = in.length / scanline;
        for (int x = 0, o = 0; x < w; x++) {
            int i = x;
            for (int y = 0; y < h; y++, i += w, o++) {
                out[o] = in[i];
            }
        }
    }

    public static void writeGrayScaleImageObj(final GrayScaleImage gi) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(gi.getFileName()));
            oos.writeObject(gi);
            oos.close();
        } catch (Exception exc) {
            System.err.println("ImageUtil::writeGrayScaleImageObject(): exc=" + exc);
        }
    }

    public static ByteImage loadByteImageObj(final String sFileName) {
        ByteImage bi = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sFileName));
            bi = (ByteImage) ois.readObject();
            ois.close();
        } catch (Exception exc) {
            System.err.println("ImageUtil::loadByteImageObj(): exc=" + exc);
        }
        return bi;
    }

    public static void writeByteImageObj(final ByteImage bi) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bi.getFileName()));
            oos.writeObject(bi);
            oos.close();
        } catch (Exception exc) {
            System.err.println("ImageUtil::writeByteImageObject(): exc=" + exc);
        }
    }

    public static GrayScaleImage loadGrayScaleImageObj(final String sFileName) {
        GrayScaleImage gi = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sFileName));
            gi = (GrayScaleImage) ois.readObject();
            ois.close();
        } catch (Exception exc) {
            System.err.println("ImageUtil::loadGrayScaleImageObj(): exc=" + exc);
        }
        return gi;
    }

    public static void showRawImage(final String sFile, int x, int y) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sFile));
            GrayScaleImage gi = (GrayScaleImage) ois.readObject();
            ois.close();
            if (gi != null) {
                showImage(toBufferedImage(gi), x, y, gi.getFileName());
            }
        } catch (Exception exc) {
            System.err.println("ImageUtil::showRawImage(): exc=" + exc);
        }
    }

    public static <Plane extends AbstractPlane> void showImage(final AbstractImage<Plane> img, int x, int y) {
        showImage(toBufferedImage(img), x, y, img.getSimpleName());
    }

    public static void showImage(final BufferedImage bi, int x, int y, String sTitle) {
        if (bi != null) {
            final int iw = bi.getWidth();
            final int ih = bi.getHeight();
            sTitle += " [" + iw + "x" + ih + "]";
            final int w = iw + (2 * IMG_OFFSET);
            final int h = ih + (2 * IMG_OFFSET);
            JFrame f = new JFrame(sTitle);
            JPanel p = new JPanel() {

                public static final long serialVersionUID = 20060922000001L;

                public Dimension getPreferredSize() {
                    return new Dimension(w, h);
                }

                public void paint(Graphics g) {
                    super.paint(g);
                    g.drawImage(bi, IMG_OFFSET, IMG_OFFSET, null);
                    g.setColor(Color.RED);
                    g.drawRect(IMG_OFFSET - 1, IMG_OFFSET - 1, iw + 1, ih + 1);
                }
            };
            p.setBackground(Color.YELLOW);
            f.add(p);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setLocation(x, y);
            f.setVisible(true);
        }
    }

    public static <Plane extends AbstractPlane> void showPyramid(final ImagePyramid<Plane> pyramid) {
        assert pyramid != null;
        for (int i = 0, x = 0; i < pyramid.numLevels(); i++) {
            AbstractImage<Plane> img = pyramid.getResolutionLevel(i).getImage();
            showImage(toBufferedImage(img), x, 0, img.getSimpleName());
            x += img.getWidth() + (2 * IMG_OFFSET);
        }
    }

    public static boolean imgWrite(final BufferedImage bi, final String sFileName) {
        boolean bResult = false;
        try {
            bResult = ImageIO.write(bi, StringUtil.getExtension(sFileName), new File(sFileName));
        } catch (Exception exc) {
            System.out.println("ImageUtils::imgWrite(): exc=" + exc);
        }
        return bResult;
    }

    public static boolean imgWrite(int width, int height, int[] ia, final String sFileName) {
        assert width > 0;
        assert height > 0;
        assert ia != null;
        assert sFileName != null;
        assert sFileName.length() > 0;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bi.setRGB(0, 0, width, height, ia, 0, width);
        return imgWrite(bi, sFileName);
    }

    public static <Plane extends AbstractPlane> void imgWrite(final AbstractImage<Plane> img, final String sFileName) {
        assert img != null;
        imgWrite(img.getWidth(), img.getHeight(), img.intArray(), sFileName);
    }

    public static <Plane extends AbstractPlane> void imgWrite(final AbstractImage<Plane> img) {
        assert img != null;
        imgWrite(img, img.getFileName());
    }

    public static <Plane extends AbstractPlane> AbstractPosition<Double> centerOfMass(final AbstractImage<Plane> img) {
        assert img != null;
        double fCmX = 0.0;
        double fCmY = 0.0;
        double fLum = 0.0;
        int w = img.getWidth();
        int h = img.getHeight();
        int[] ia = img.intArray();
        for (int y = 0; y < h; y++) {
            int p = y * w;
            for (int x = 0; x < w; x++) {
                int i = ia[p + x];
                if (i >= 0) {
                    fLum += i;
                    fCmX += i * x;
                    fCmY += i * y;
                }
            }
        }
        if (fLum > 0.0) {
            return new Position2D<Double>(fCmX / fLum, fCmY / fLum);
        }
        return new Position2D<Double>(w / 2.0, h / 2.0);
    }

    public static <Plane extends AbstractPlane> AffineTransform centerOfMassTransform(final AbstractImage<Plane> wiRef, final AbstractImage<Plane> wiFlt) {
        AbstractPosition<Double> cmRef = centerOfMass(wiRef);
        AbstractPosition<Double> cmFlt = centerOfMass(wiFlt);
        AffineTransform transform = new AffineTransform();
        transform.set(AffineTransform.ANGLE, 0.0);
        transform.set(AffineTransform.SCALE, 1.0);
        transform.set(AffineTransform.TRN_X, cmFlt.getX().doubleValue() - cmRef.getX().doubleValue());
        transform.set(AffineTransform.TRN_Y, cmFlt.getY().doubleValue() - cmRef.getY().doubleValue());
        return transform;
    }

    public static <Plane extends AbstractPlane> boolean isOglConformant(final AbstractImage<Plane> img) {
        int w = img.getWidth();
        int h = img.getHeight();
        return (NumberUtil.isPowerOfTwo(w) && NumberUtil.isPowerOfTwo(h));
    }

    public static <Plane extends AbstractPlane> AbstractImage<Plane> getCenteredImage(final AbstractImage<Plane> src, final Dimension3D<Integer> dim, boolean clear) {
        assert src != null;
        assert dim != null;
        assert dim.getWidth().intValue() >= src.getWidth() && dim.getHeight().intValue() >= src.getHeight();
        return AbstractImage.centeredImage(src, dim, clear);
    }

    public static GrayScaleImage getCenteredImage(final GrayScaleImage src, final Dimension3D<Integer> dim, int clear) {
        assert src != null;
        assert dim != null;
        assert dim.getWidth().intValue() >= src.getWidth() && dim.getHeight().intValue() >= src.getHeight();
        return new GrayScaleImage(AbstractImage.centeredImage(src, dim, clear));
    }

    public static <Plane extends AbstractPlane> AbstractImage<Plane> getOpenGlConformant(final AbstractImage<Plane> img, boolean clear) {
        assert img != null;
        int w = NumberUtil.nextPowerOfTwo(img.getWidth());
        int h = NumberUtil.nextPowerOfTwo(img.getHeight());
        return getCenteredImage(img, new Dimension3D<Integer>(w, h, 1), clear);
    }

    public static GrayScaleImage getOglConformant(final GrayScaleImage img, int clear) {
        assert img != null;
        int w = NumberUtil.nextPowerOfTwo(img.getWidth());
        int h = NumberUtil.nextPowerOfTwo(img.getHeight());
        return getCenteredImage(img, new Dimension3D<Integer>(w, h, 1), clear);
    }

    public static double computeBasis(int cp, int x, int y) {
        double a = 0.0;
        switch(cp) {
            case 0:
                a = 1.0;
                break;
            case 1:
                a = x;
                break;
            case 2:
                a = y;
                break;
            case 3:
                a = x * x;
                break;
            case 4:
                a = x * y;
                break;
            case 5:
                a = y * y;
                break;
            case 6:
                a = x * x * x;
                break;
            case 7:
                a = x * x * y;
                break;
            case 8:
                a = x * y * y;
                break;
            case 9:
                a = y * y * y;
                break;
        }
        return a;
    }

    public static <Plane extends AbstractPlane> void computeDifference(final AbstractImage<Plane> iRef, final AbstractImage<Plane> iTrn, final AbstractImage<Plane> iDif) {
        assert iRef != null;
        assert iTrn != null;
        assert iDif != null;
        assert iRef.getWidth() == iTrn.getWidth();
        assert iRef.getHeight() == iTrn.getHeight();
        assert iRef.getWidth() == iDif.getWidth();
        assert iRef.getHeight() == iDif.getHeight();
        if (iRef != null && iTrn != null && iDif != null && iRef.getWidth() == iTrn.getWidth() && iRef.getHeight() == iTrn.getHeight() && iRef.getWidth() == iDif.getWidth() && iRef.getHeight() == iDif.getHeight()) {
            int[] iaRef = iRef.intArray();
            int[] iaTrn = iTrn.intArray();
            int[] iaDif = iDif.intArray();
            for (int i = 0; i < iaRef.length; i++) {
                int v = iaTrn[i] - iaRef[i] + 127;
                if (v < BLACK) {
                    iaDif[i] = BLACK;
                } else if (v > WHITE) {
                    iaDif[i] = WHITE;
                } else {
                    iaDif[i] = v;
                }
            }
        }
        iDif.reset();
    }

    public static <Plane extends AbstractPlane> AbstractImage<Plane> computeDifference(final AbstractImage<Plane> iRef, final AbstractImage<Plane> iTrn) {
        AbstractImage<Plane> iDif = iRef.newImage();
        if (iDif != null) {
            computeDifference(iRef, iTrn, iDif);
        }
        return iDif;
    }

    public static FloatImage halfBlackHalfWhiteImage(int w, int h) {
        FloatImage img = new FloatImage(new Dimension3D<Integer>(w, h, 1));
        double[] imgData = img.floatArray();
        for (int y = 0; y < h; y++) {
            int pos = y * w;
            for (int x = w / 2; x < w; x++) {
                imgData[pos + x] = FloatPlane.WHITE;
            }
        }
        return img;
    }

    public static FloatImage tiledImage(int iw, int ih, int tw, int th) {
        FloatImage img = new FloatImage(new Dimension2D<Integer>(iw, ih));
        double[] imgData = img.floatArray();
        int nw = iw / tw;
        int nh = ih / th;
        int ts = tw * th;
        int rs = nw * ts;
        for (int ty = 0; ty < nh; ty++) {
            int tyo = ty * rs;
            for (int tx = 0; tx < nw; tx++) {
                int to = tyo + (tx * tw);
                int mody = ty % 2;
                int modx = tx % 2;
                double color = FloatPlane.BLACK;
                if ((mody == 0 && modx > 0) || (mody > 0 && modx == 0)) {
                    color = FloatPlane.WHITE;
                }
                if (color == FloatPlane.WHITE) {
                    for (int iy = 0; iy < th; iy++) {
                        int so = to + iy * iw;
                        for (int ix = 0; ix < tw; ix++) {
                            imgData[so + ix] = color;
                        }
                    }
                }
            }
        }
        return img;
    }

    public static FloatImage tiledImage(int iw, int ih, int id, int tw, int th, int td) {
        FloatImage img = new FloatImage(new Dimension3D<Integer>(iw, ih, id));
        FloatImage img2D = tiledImage(iw, ih, tw, th);
        double[] bwPlane = img2D.floatArray();
        double[] wbPlane = new double[bwPlane.length];
        for (int i = 0; i < wbPlane.length; i++) {
            wbPlane[i] = FloatPlane.WHITE - bwPlane[i];
        }
        for (int p = 0, m = 0; p < id; p += td, m++) {
            for (int t = p; t < p + td; t++) {
                if (t < id) {
                    FloatPlane plane = img.floatPlane(t);
                    double[] fa = plane.floatArray();
                    if (m % 2 == 0) {
                        System.arraycopy(bwPlane, 0, fa, 0, bwPlane.length);
                    } else {
                        System.arraycopy(wbPlane, 0, fa, 0, wbPlane.length);
                    }
                }
            }
        }
        return img;
    }

    public static FloatImage whiteSquare(int iw, int ih, int sw, int sh) {
        FloatImage img = new FloatImage(new Dimension2D<Integer>(iw, ih));
        double[] imgData = img.floatArray();
        int nw = (iw - sw) / 2;
        int nh = (ih - sh) / 2;
        for (int ty = nh; ty < nh + sh; ty++) {
            int tyo = ty * iw;
            for (int tx = nw; tx < nw + sw; tx++) {
                imgData[tyo + tx] = FloatPlane.WHITE;
            }
        }
        return img;
    }

    public static FloatImage cube(int iw, int ih, int id, int cw, int ch, int cd) {
        FloatImage img = new FloatImage(new Dimension3D<Integer>(iw, ih, id));
        FloatImage wSquare = whiteSquare(iw, ih, cw, ch);
        FloatPlane wPlane = wSquare.doublePlane();
        int nd = (id - cd) / 2;
        for (int idx = nd; idx < nd + cd; idx++) {
            img.setPlane(idx, wPlane);
        }
        return img;
    }

    public static FloatImage blackAndWhiteSlices(int iw, int ih, int id) {
        FloatImage img = new FloatImage(new Dimension3D<Integer>(iw, ih, id));
        for (int p = 0; p < id; p++) {
            FloatPlane plane = img.floatPlane(p);
            double[] fa = plane.floatArray();
            if (p % 2 != 0) {
                for (int i = 0; i < fa.length; i++) {
                    fa[i] = FloatPlane.WHITE;
                }
            }
        }
        return img;
    }

    public static FloatImage whiteAndBlackSlices(int iw, int ih, int id) {
        FloatImage img = new FloatImage(new Dimension3D<Integer>(iw, ih, id));
        for (int p = 0; p < id; p++) {
            FloatPlane plane = img.floatPlane(p);
            double[] fa = plane.floatArray();
            if (p % 2 == 0) {
                for (int i = 0; i < fa.length; i++) {
                    fa[i] = FloatPlane.WHITE;
                }
            }
        }
        return img;
    }

    public static <Plane extends AbstractPlane> int printDifference(final FloatImage in, final FloatImage out) {
        assert in != null;
        assert out != null;
        assert in.numPlanes() == out.numPlanes();
        assert in.numVoxels() == out.numVoxels();
        int n = 0;
        for (int p = 0; p < in.numPlanes(); p++) {
            FloatPlane inp = in.floatPlane(p);
            FloatPlane oup = out.floatPlane(p);
            double[] ina = inp.floatArray();
            double[] oua = oup.floatArray();
            for (int i = 0; i < ina.length; i++) {
                if (ina[i] != oua[i]) {
                    n++;
                }
            }
        }
        double diff = (double) (n * 100) / in.numVoxels();
        System.out.println("diff: " + (n + " pix [" + String.format("%4.2f", diff) + " %]"));
        return n;
    }

    public static <Plane extends AbstractPlane> int printDifference(final AbstractImage<Plane> in, final AbstractImage<Plane> out) {
        assert in != null;
        assert out != null;
        assert in.numPlanes() == out.numPlanes();
        assert in.numVoxels() == out.numVoxels();
        int n = 0;
        for (int p = 0; p < in.numPlanes(); p++) {
            RgbPlane inp = in.intPlane(p);
            RgbPlane oup = out.intPlane(p);
            int[] ina = inp.intArray();
            int[] oua = oup.intArray();
            for (int i = 0; i < ina.length; i++) {
                if (ina[i] != oua[i]) {
                    n++;
                }
            }
        }
        double diff = (double) (n * 100) / in.numVoxels();
        System.out.println("diff: " + (n + " pix [" + String.format("%4.2f", diff) + " %]"));
        return n;
    }

    public static AbstractImage<RgbPlane> applyWindowing(final AbstractImage<RgbPlane> iSrc, final WithAndCenterParameters wp) {
        assert iSrc != null;
        assert wp != null;
        AbstractImage<RgbPlane> iDst = iSrc;
        WithAndCenterFilter<RgbPlane> filter = new WithAndCenterFilter<RgbPlane>();
        filter.setParameters(wp);
        if (filter.isValid()) {
            iDst = filter.filter(iSrc);
        }
        return iDst;
    }

    public static GrayScaleImage applyWindowing(final GrayScaleImage iSrc, final WithAndCenterParameters wp) {
        return applyWindowing(iSrc, wp);
    }

    public static void testLoadRgbImage(String[] args) {
        if (args.length > 0) {
            AbstractImage<RgbPlane> img = loadAsRgbImage(args[0]);
            if (img != null) {
                showImage(img, 350, 0);
            }
        }
    }

    public static void testLoadGrayScale(String[] args) {
        if (args.length > 0) {
            AbstractImage<RgbPlane> img = loadAsGrayScaleImage(args[0]);
            if (img != null) {
                showImage(img, 350, 0);
            }
        }
    }

    public static void testLoadFloatImage(String[] args) {
        if (args.length > 0) {
            FloatImage img = loadAsFloatImage(args[0]);
            if (img != null) {
                showImage(img, 350, 0);
            }
        }
    }

    public static void testLoadByteImage(String[] args) {
        if (args.length > 0) {
            ByteImage img = loadAsByteImage(args[0]);
            if (img != null) {
                showImage(img, 350, 0);
            }
        }
    }

    public static void testEqualize(String[] args) {
        if (args.length > 1) {
            AbstractImage<RgbPlane> iTmp = loadAsGrayScaleImage(args[0]);
            AbstractImage<RgbPlane> iSrc = loadAsGrayScaleImage(args[1]);
            if (iTmp != null && iSrc != null) {
                int w = iTmp.getWidth();
                int h = iTmp.getHeight();
                assert w > 0 && w == iSrc.getWidth();
                assert h > 0 && h == iSrc.getHeight();
                long l = System.currentTimeMillis();
                AbstractImage<RgbPlane> iTgt = equalize(iTmp, iSrc);
                System.out.println("equalize=" + (System.currentTimeMillis() - l));
                iTgt.setFileName("Equalized");
                showImage(iTmp, 0, 0);
                showImage(iSrc, 540, 0);
                showImage(iTgt, 540, 400);
            }
        }
    }

    public static void testTransform(String[] args) {
        if (args.length > 0) {
            AbstractImage<RgbPlane> src = loadAsGrayScaleImage(args[0]);
            if (src != null) {
                double[] fa = { 30.0, 0.9, 50.0, 50.0 };
                GrayScaleImage dst = new GrayScaleImage(src.getSize());
                AffineTransform transform = new AffineTransform(fa);
                AffineSplineInterpolator<RgbPlane> interpol = new AffineSplineInterpolator<RgbPlane>();
                interpol.setSplineDegree(3);
                long l = System.currentTimeMillis();
                interpol.transform(src, transform, dst);
                System.out.println("transform=" + (System.currentTimeMillis() - l));
                showImage(dst, 540, 0);
            }
        }
    }

    public static void testImageTiling(String[] args) {
        FloatImage img = tiledImage(256, 256, 32, 32);
        if (img != null) {
            showImage(toBufferedImage(img), 50, 50, "Image");
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    FloatImage tile = img.getTile(x * 68, y * 68, 0, 68, 68, 1);
                    tile.setType(img.getType());
                    BufferedImage bi = toBufferedImage(tile);
                    if (bi != null) {
                        showImage(bi, 400 + (x * 90), 50 + (y * 120), "Tile");
                    }
                }
            }
        }
    }

    public static void testImageWriters(String[] args) {
        String[] sa = ImageIO.getWriterFormatNames();
        for (int i = 0; i < sa.length; i++) {
            System.out.println(i + ": " + sa[i]);
        }
    }

    public static void testRgbHistogram(String[] args) {
        if (args.length > 0) {
            AbstractImage<RgbPlane> img = loadAsGrayScaleImage(args[0]);
            if (img != null) {
                RgbHistogram rgb = new RgbHistogram(256, img.intArray());
                RgbHistogramFrame f = new RgbHistogramFrame(img.getFileName());
                f.setBounds(100, 0, 512, 900);
                f.setHistogram(rgb);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setVisible(true);
            }
        }
    }

    public static void testCenterOfMass(String[] args) {
        if (args.length > 0) {
            AbstractImage<RgbPlane> img = loadAsGrayScaleImage(args[0]);
            if (img != null) {
                long l = System.currentTimeMillis();
                AbstractPosition<Double> cm = centerOfMass(img);
                System.out.println("cm=" + cm + " t=" + (System.currentTimeMillis() - l));
            }
        }
    }

    public static void testFilter(String[] args) {
        if (args.length > 1) {
            AbstractImage<RgbPlane> img = loadAsGrayScaleImage(args[0]);
            if (img != null) {
                showImage(img, 0, 0);
                ImageProcessor<RgbPlane> ip = FilterFactory.getInstance("co.edu.unal.ungrid.image.processing.filter.GaussianFilter");
                if (ip != null) {
                    ip.config(XmlUtil.load(args[1]));
                    if (ip.isValid()) {
                        long l = System.currentTimeMillis();
                        AbstractImage<RgbPlane> ai = ip.filter(img);
                        System.out.println("filter=" + TimeUtil.format(l, System.currentTimeMillis()));
                        showImage(ai, 540, 0);
                    }
                }
            }
        }
    }

    public static void testSegmentation(String[] args) {
        if (args.length > 0) {
            AbstractImage<RgbPlane> img = loadAsGrayScaleImage(args[0]);
            if (img != null) {
                AbstractImage<RgbPlane> seg = segment(img, 0.2);
                showImage(seg, 0, 0);
                GrayScaleImage dst = new GrayScaleImage(seg.getSize(), RgbPlane.OUTLIER);
                double[] fa = { 30.0, 0.9, 50.0, 50.0 };
                AffineTransform transform = new AffineTransform(fa);
                AffineSplineInterpolator<RgbPlane> interpol = new AffineSplineInterpolator<RgbPlane>();
                interpol.setSplineDegree(3);
                interpol.transform(seg, transform, dst);
                showImage(dst, 540, 0);
            }
        }
    }

    public static void testWriteReadGrayImage(String[] args) {
        if (args.length > 0) {
            GrayScaleImage img = loadAsGrayScaleImage(args[0]);
            if (img != null) {
                img.setFileName("data/test.gi");
                writeGrayScaleImageObj(img);
                GrayScaleImage dst = loadGrayScaleImageObj(img.getFileName());
                if (dst != null) {
                    showImage(dst, 540, 0);
                }
            }
        }
    }

    public static void testShowGrayImage(String[] args) {
        if (args.length > 0) {
            showRawImage(args[0], 100, 100);
        }
    }

    public static void testDicomImageDesc(String[] args) {
        String sImgDesc = "19 {MR} <CORONAL LPF\\FA>";
        System.out.println("typ=" + getImageType(sImgDesc));
        System.out.println("mod=" + getImageModality(sImgDesc));
        System.out.println("ori=" + getImageOrientation(sImgDesc));
    }

    public static void testCenteredImage(String[] args) {
        if (args.length > 0) {
            AbstractImage<RgbPlane> img = loadAsGrayScaleImage(args[0]);
            if (img != null) {
                showImage(img, 0, 0);
                AbstractDimension<Integer> iSize = img.getSize();
                AbstractDimension<Integer> oSize = iSize.clone();
                oSize.set(AbstractDimension.WIDTH, NumberUtil.nextPowerOfTwo(oSize.get(AbstractDimension.WIDTH).intValue()));
                oSize.set(AbstractDimension.HEIGHT, NumberUtil.nextPowerOfTwo(oSize.get(AbstractDimension.HEIGHT).intValue()));
                AbstractImage<RgbPlane> dst = AbstractImage.centeredImage(img, oSize, 255);
                dst.setFileName("Centered");
                showImage(dst, iSize.getWidth().intValue() + 30, 0);
            }
        }
    }

    public static void testTiledImage(String[] args) {
        FloatImage img = tiledImage(128, 128, 16, 32);
        if (img != null) {
            showImage(img, 0, 0);
        }
    }

    public static void testTranspose(String[] args) {
        if (args.length > 0) {
            GrayScaleImage iSrc = loadAsGrayScaleImage(args[0]);
            if (iSrc != null) {
                ImageUtil.showImage(iSrc, 0, 0);
                AbstractDimension<Integer> size = iSrc.getSize().clone();
                int w = size.getWidth().intValue();
                int h = size.getHeight().intValue();
                double[] out = new double[w * h];
                transpose(iSrc.floatArray(), w, out);
                size.set(AbstractDimension.WIDTH, h);
                size.set(AbstractDimension.HEIGHT, w);
                FloatImage iOut = new FloatImage(size, out);
                ImageUtil.showImage(iOut, iSrc.getWidth() + 24, 0);
            }
        }
    }

    public static void main(String[] args) {
        testLoadByteImage(args);
    }

    public static final int GRAY_HISTOGRAM_SIZE = RgbPlane.DEF_GRAY_LEVELS;

    public static final int IMG_OFFSET = 10;

    public static final int OUTLIER = RgbImage.OUTLIER;

    public static final int BLACK = RgbImage.BLACK;

    public static final int WHITE = RgbImage.WHITE;

    public static final int R = 0;

    public static final int G = 1;

    public static final int B = 2;

    public static final String[] IMG_FILE_EXT = { ".bi", ".gsi", ".bmp", ".dcm", ".jpg", ".png" };

    public static final String GSI_EXT = IMG_FILE_EXT[0];

    private static final int DBG = 0;
}
