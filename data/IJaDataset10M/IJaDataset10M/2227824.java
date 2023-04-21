package org.fao.waicent.xmap2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;
import org.fao.waicent.attributes.DataLegendDefinition;
import org.fao.waicent.util.DATTableReader;
import org.fao.waicent.util.FileResource;
import org.fao.waicent.util.Sentinel;
import org.fao.waicent.xmap2D.coordsys.CoordSys;
import org.fao.waicent.xmap2D.coordsys.CoordSysFactory;
import org.fao.waicent.xmap2D.coordsys.CoordTransform;
import org.fao.waicent.xmap2D.util.ParamList;

public class ASCIIRasterData extends RasterData {

    int ncols = 0;

    int nrows = 0;

    float xllcorner = 0.0f;

    float yllcorner = 0.0f;

    float xllcenter = 0.0f;

    float yllcenter = 0.0f;

    float cellsize = 0.0f;

    int NODATA_value = -9999;

    Rectangle2D original_bounds = null;

    int projected_Pixel_height, projected_Pixel_width;

    int source_Pixel_height, source_Pixel_width;

    int height, width;

    int row_to_skip;

    String filename = "";

    int max_index = 0;

    Hashtable corrispondence;

    public ASCIIRasterData(FileResource Raster_filename) throws IOException {
        super(Raster_filename);
        byte r[] = { 0 };
        byte g[] = { 0 };
        byte b[] = { 0 };
        default_color_model = new IndexColorModel(DataBuffer.getDataTypeSize(DataBuffer.TYPE_USHORT), r.length, r, g, b);
        setRasterType(ASCII_RASTER_TYPE);
        DATTableReader table = new DATTableReader(Raster_filename, false);
        max_index = 0;
        corrispondence = new Hashtable();
        load(table);
        table.close();
    }

    public ParamList getParamList() {
        return null;
    }

    public ColorModel getColorModel(DataLegendDefinition legend) {
        if (legend != null) {
            byte r[] = new byte[max_index];
            byte g[] = new byte[max_index];
            byte b[] = new byte[max_index];
            Enumeration keys = corrispondence.keys();
            Object obj;
            String key;
            for (; keys.hasMoreElements(); ) {
                key = (String) keys.nextElement();
                obj = corrispondence.get(key);
                if (obj != null) {
                    Color color = legend.getColor(Float.parseFloat(key));
                    int i = ((Integer) obj).intValue();
                    r[i] = (byte) color.getRed();
                    g[i] = (byte) color.getGreen();
                    b[i] = (byte) color.getBlue();
                }
            }
            this.default_color_model = new IndexColorModel(DataBuffer.getDataTypeSize(DataBuffer.TYPE_USHORT), r.length, r, g, b);
        }
        return default_color_model;
    }

    protected void load(DATTableReader r) throws IOException {
        loadHead(r);
        filename = Raster_filename.getName().toLowerCase();
    }

    public Rectangle getDeviceBounds() {
        return new Rectangle(0, 0, projected_Pixel_width, projected_Pixel_height);
    }

    public Rectangle2D getPhysicalExtent() {
        return original_bounds;
    }

    private int collect(String value) {
        Object obj;
        if ((obj = corrispondence.get(value)) == null) {
            corrispondence.put(value, obj = new Integer(max_index++));
        }
        return ((Integer) obj).intValue();
    }

    protected void loadImage(DATTableReader r, Vector Vrange) throws IOException {
        Vector maskVector = new Vector();
        if ((filename.indexOf("pet") != -1)) {
            maskVector = mask();
        }
        int sample_depth = 1;
        float[] data = new float[sample_depth];
        long t0 = System.currentTimeMillis();
        for (int y = 0; y < nrows; y++) {
            r.readRow();
            for (int x = 0; x < ncols; x++) {
                for (int s = 0; s < sample_depth; s++) {
                    data[s] = collect(r.getValue(x));
                }
                raster.setPixel(x, y, data);
            }
        }
    }

    static Vector[] getClevIndex(int begin, int end, double sample) {
        Vector indici[] = new Vector[end - begin];
        double dindex = begin;
        int pointer = 0;
        while (dindex < end) {
            int ind = (int) (dindex - begin);
            if (ind < indici.length) {
                if (indici[ind] == null) {
                    indici[ind] = new Vector();
                }
                indici[ind].addElement(new Integer(pointer));
            }
            dindex += sample;
            pointer++;
        }
        return indici;
    }

    static int[] getValidIndex(int comincia, double sample, int index) {
        index = index - comincia;
        if (sample == 1) {
            return new int[] { index };
        }
        if (sample > 1) {
            int quot = (int) ((index + 1) / sample);
            double arg = sample * quot;
            int rr = (int) arg;
            if (rr == index) {
                return new int[] { (int) quot };
            }
            return new int[0];
        }
        if (sample < 1) {
            int quot = (int) (index / sample);
            double start = quot * sample;
            if (start < index) {
                start += sample;
                quot++;
            }
            Vector coll = new Vector();
            while (start - 0.999999999999999999999D < index) {
                coll.addElement(new Integer(quot));
                start += sample + 0.0000001;
                quot++;
            }
            int indici[] = new int[coll.size()];
            for (int i = 0; i < indici.length; i++) {
                indici[i] = ((Integer) coll.elementAt(i)).intValue();
            }
            return indici;
        }
        return null;
    }

    public Image getImage(Rectangle2D zoomcoordspace, Rectangle2D zoom, Dimension size, DataLegendDefinition legend, Sentinel sentinel) {
        ImageProducer image_producer = null;
        Image raster_img = null;
        try {
            Rectangle2D theBounds = original_bounds;
            max_index = 0;
            corrispondence = new Hashtable();
            WritableRaster rasta = getProjectedRaster(getSourceCoordSys(), getCurrentCoordSys(), org.fao.waicent.xmap2D.coordsys.ApplAffnTransform.getAffineTransform(theBounds, new Rectangle(0, 0, width, height)), getTransformToRasterSpace(), Raster_filename, this.default_color_model, zoom.getBounds(), size, source_Pixel_width, source_Pixel_height, sentinel);
            if (rasta != null) {
                raster_img = new BufferedImage(getColorModel(legend), rasta, true, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return raster_img;
    }

    public RasterData transform(Rectangle2D rbounds, org.fao.waicent.xmap2D.coordsys.CoordTransform transformer) throws Exception {
        Rectangle2D destbounds = new Rectangle2D.Double();
        int Pixel_width = projected_Pixel_width;
        int Pixel_height = projected_Pixel_height;
        transformer.transform(rbounds, destbounds, org.fao.waicent.xmap2D.coordsys.CoordTransform.FORWARD);
        AffineTransform tonewrasterspace = org.fao.waicent.xmap2D.coordsys.ApplAffnTransform.getAffineTransform(destbounds, new Rectangle(0, 0, Pixel_width, Pixel_height), true);
        int newPixel_width = (int) (tonewrasterspace.getScaleX() * destbounds.getWidth());
        int newPixel_height = (int) (tonewrasterspace.getScaleX() * destbounds.getHeight());
        projected_Pixel_width = newPixel_width;
        projected_Pixel_height = newPixel_height;
        setTransformToRaster(tonewrasterspace);
        rasterCoordSys = transformer.getDestCoordSys();
        setBounds2D(destbounds);
        return this;
    }

    protected void loadHead(DATTableReader r) {
        try {
            String label = null, value = null;
            int pos = 1;
            for (int i = 0; i < 5; i++) {
                r.readRow();
                value = r.getValue(pos).trim();
                if (i == 0) {
                    ncols = Integer.valueOf(value).intValue();
                } else if (i == 1) {
                    nrows = Integer.valueOf(value).intValue();
                } else if (i == 2) {
                    label = r.getValue(pos - 1);
                    if (label.toUpperCase().indexOf("XLLCORNER") != -1) {
                        xllcorner = Float.valueOf(value).floatValue();
                    } else if (label.toUpperCase().indexOf("XLLCENTER") != -1) {
                        xllcenter = Float.valueOf(value).floatValue();
                    }
                } else if (i == 3) {
                    label = r.getValue(pos - 1);
                    if (label.toUpperCase().indexOf("YLLCORNER") != -1) {
                        yllcorner = Float.valueOf(value).floatValue();
                    }
                    if (label.toUpperCase().indexOf("YLLCENTER") != -1) {
                        yllcenter = Float.valueOf(value).floatValue();
                    }
                } else if (i == 4) {
                    cellsize = Float.valueOf(value).floatValue();
                }
            }
            r.readRow();
            row_to_skip = r.getRowNumber();
            NODATA_value = Integer.valueOf(r.getValue(pos).trim()).intValue();
            height = nrows;
            width = ncols;
            source_Pixel_width = width;
            source_Pixel_height = height;
            projected_Pixel_height = source_Pixel_height;
            projected_Pixel_width = source_Pixel_width;
            Rectangle2D theBounds = new Rectangle2D.Double(xllcorner, -yllcorner - (cellsize * nrows), cellsize * ncols, cellsize * nrows);
            setBounds2D(theBounds);
            original_bounds = theBounds;
            try {
                String projection_string = "\"Longitude / Latitude\", 1, 0";
                rasterCoordSys = CoordSysFactory.createFromPRJ(projection_string);
                setTransformToRaster(org.fao.waicent.xmap2D.coordsys.ApplAffnTransform.getAffineTransform(getBounds2D(), new Rectangle(0, 0, ncols, nrows)));
                setSourceCoordSys(rasterCoordSys);
            } catch (Exception ex) {
                System.out.println("ASCIIRASTER:: non ce la faccio con CoordSys");
            }
        } catch (IOException e) {
        }
    }

    private WritableRaster getProjectedRaster(CoordSys sourceCS, CoordSys destCS, AffineTransform source_transform, AffineTransform dest_transform, FileResource rasterfile, ColorModel rastercolormodel, Rectangle window_p, Dimension scale_size, int width, int height, Sentinel sentinel) throws Exception {
        CoordTransform _transform = new CoordTransform(sourceCS, destCS);
        Rectangle2D window_bounds_p = dest_transform.createInverse().createTransformedShape(window_p).getBounds2D();
        Rectangle2D window_bounds_s = _transform.transform(window_bounds_p, false);
        Rectangle window = source_transform.createTransformedShape(window_bounds_s).getBounds();
        WritableRaster source_raster = getRasterSpace(rasterfile, rastercolormodel, window, scale_size, width, height, sentinel);
        if (source_raster == null) {
            return null;
        }
        AffineTransform tosourcespace = dest_transform.createInverse();
        AffineTransform at = org.fao.waicent.xmap2D.coordsys.ApplAffnTransform.getAffineTransform(window_bounds_p, new Rectangle(0, 0, scale_size.width, scale_size.height));
        AffineTransform at_inv = at.createInverse();
        AffineTransform trasf = org.fao.waicent.xmap2D.coordsys.ApplAffnTransform.getAffineTransform(window, new Rectangle(0, 0, source_raster.getWidth(), source_raster.getHeight()));
        AffineTransform transf = org.fao.waicent.xmap2D.coordsys.ApplAffnTransform.do_concatenate(source_transform, trasf);
        {
            Object data_elements = null;
            long t0 = System.currentTimeMillis();
            WritableRaster newraster = rastercolormodel.createCompatibleWritableRaster(source_raster.getWidth(), source_raster.getHeight());
            CoordTransform _do_it = new CoordTransform(new CoordSys(sourceCS, transf), new CoordSys(destCS, at));
            double pp[] = new double[newraster.getWidth() * 2];
            for (int y = 0; y < newraster.getHeight(); y++) {
                for (int j = 0; j < newraster.getWidth(); j++) {
                    pp[j * 2] = j;
                    pp[j * 2 + 1] = y;
                }
                _do_it.transform(pp, 0, pp, 0, pp.length / 2, false);
                for (int x = 0; x < newraster.getWidth(); x++) {
                    int xx = round(pp[x * 2]);
                    int yy = round(pp[x * 2 + 1]);
                    if (xx >= 0 && xx < source_raster.getWidth() && yy >= 0 && yy < source_raster.getHeight()) {
                        data_elements = source_raster.getDataElements(xx, yy, 1, 1, null);
                        newraster.setDataElements(x, y, data_elements);
                    }
                }
            }
            return newraster;
        }
    }

    public static int round(double a) {
        return (int) Math.floor(a + 0.5d);
    }

    private WritableRaster getRasterSpace(FileResource rasterfile, ColorModel rastercolormodel, Rectangle window, Dimension scale_size, int width, int height, Sentinel sentinel) {
        this.window = window;
        WritableRaster partial_raster = rastercolormodel.createCompatibleWritableRaster(scale_size.width, scale_size.height);
        if (window.x < 0) {
            window.x = 0;
        }
        if (window.y < 0) {
            window.y = 0;
        }
        if (window.x + window.width >= width) {
            window.width = width - window.x;
        }
        if (window.y + window.height >= height) {
            window.height = height - window.y;
        }
        double x_sample = window.getWidth() / scale_size.getWidth();
        double y_sample = window.getHeight() / scale_size.getHeight();
        System.out.println("SPAZIO RASTER " + window);
        if (x_sample < 0.0001 || y_sample < 0.0001) {
            return null;
        }
        try {
            LineReader in = new LineReader(rasterfile.openInputStream());
            byte buffer[];
            double x_increment = x_sample > 1 ? x_sample : 1;
            long t0 = System.currentTimeMillis();
            Vector indicis[] = getClevIndex(window.x, window.x + window.width, x_sample);
            for (int i = 0; i < window.y + row_to_skip; i++) {
                in.readLine();
            }
            Vector y_indici[] = getClevIndex(window.y, window.y + window.height, y_sample);
            for (int ii = 0; ii < y_indici.length; ii++) {
                buffer = in.readLine();
                if (sentinel != null && sentinel.stop()) {
                    return null;
                }
                int y = ii + window.y;
                if (y_indici[ii] != null) {
                    byte tokens[][] = new byte[width][];
                    ByteTokenizer.tokenize(buffer, tokens);
                    for (int yy = 0; yy < y_indici[ii].size(); yy++) {
                        int y_ind = ((Integer) y_indici[ii].elementAt(yy)).intValue();
                        if (y_ind < scale_size.height) {
                            for (int k = 0; k < indicis.length; k++) {
                                int x = k + window.x;
                                if (indicis[k] != null) {
                                    for (int j = 0; j < indicis[k].size(); j++) {
                                        int x_ind = ((Integer) indicis[k].elementAt(j)).intValue();
                                        if (x_ind < scale_size.width) {
                                            try {
                                                partial_raster.setPixel(x_ind, y_ind, new int[] { collect(new String(tokens[x])) });
                                            } catch (Exception exe) {
                                                System.out.println("\n\n\n\n FISSA ERRORE " + x_ind + " " + y_ind + "  " + x + "  " + tokens.length + window + scale_size);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
        return partial_raster;
    }

    static class ByteTokenizer implements Enumeration {

        private int currentPosition;

        private int maxPosition;

        private byte[] str;

        private byte[] delimiters;

        private boolean retTokens;

        public static int indexOf(byte b[], byte ch, int start, int limit) {
            int p = start;
            while (p < limit) {
                if (b[p] == ch) {
                    return p;
                }
                p++;
            }
            return -1;
        }

        public ByteTokenizer(byte[] str, byte[] delim, boolean returnTokens) {
            currentPosition = 0;
            this.str = str;
            maxPosition = str.length;
            delimiters = delim;
            retTokens = returnTokens;
        }

        public ByteTokenizer(byte[] str, byte[] delim) {
            this(str, delim, false);
        }

        public static void tokenize(byte src[], byte[][] dest) {
            ByteTokenizer bt = new ByteTokenizer(src);
            int counter = 0;
            while (counter < dest.length && bt.hasMoreTokens()) {
                dest[counter] = bt.nextToken();
                counter++;
            }
        }

        static byte[] common_delimitors = new byte[] { (byte) ' ', (byte) '\t', (byte) '\n', (byte) '\r', (byte) '\f' };

        public ByteTokenizer(byte[] str) {
            this(str, common_delimitors, false);
        }

        private void skipDelimiters() {
            while (!retTokens && (currentPosition < maxPosition) && indexOf(delimiters, str[currentPosition], 0, delimiters.length) >= 0) {
                currentPosition++;
            }
        }

        public boolean hasMoreTokens() {
            skipDelimiters();
            return (currentPosition < maxPosition);
        }

        public byte[] nextToken() {
            skipDelimiters();
            if (currentPosition >= maxPosition) {
                throw new NoSuchElementException();
            }
            int start = currentPosition;
            while ((currentPosition < maxPosition) && (indexOf(delimiters, str[currentPosition], 0, delimiters.length) < 0)) {
                currentPosition++;
            }
            if (retTokens && (start == currentPosition) && (indexOf(delimiters, str[currentPosition], 0, delimiters.length) < 0)) {
                currentPosition++;
            }
            byte ret[] = new byte[currentPosition - start];
            System.arraycopy(str, start, ret, 0, ret.length);
            return ret;
        }

        public byte[] nextToken(byte[] delim) {
            delimiters = delim;
            return nextToken();
        }

        public boolean hasMoreElements() {
            return hasMoreTokens();
        }

        public Object nextElement() {
            return nextToken();
        }
    }

    static class LineReader {

        boolean debug = false;

        byte residuo[] = new byte[0];

        InputStream is;

        LineReader(InputStream is) {
            this.is = is;
        }

        byte[] readLine() throws IOException {
            int ind;
            if ((ind = indexOf(residuo, (byte) '\n', 0, residuo.length)) != -1) {
                byte line[] = new byte[ind];
                System.arraycopy(residuo, 0, line, 0, line.length);
                byte newresiduo[] = new byte[residuo.length - ind - 1];
                System.arraycopy(residuo, ind + 1, newresiduo, 0, newresiduo.length);
                residuo = newresiduo;
                return line;
            } else {
                byte linea[] = null;
                while ((linea = readblock()) == null) {
                    ;
                }
                if (linea != null && linea.length == 0) {
                    return null;
                }
                return linea;
            }
        }

        int len = 32000;

        byte buffer[] = new byte[len];

        byte[] readblock() throws IOException {
            int lun = is.read(buffer);
            int ind;
            if ((ind = indexOf(buffer, (byte) '\n', 0, lun)) != -1) {
                byte[] line = new byte[residuo.length + ind + 1];
                System.arraycopy(residuo, 0, line, 0, residuo.length);
                System.arraycopy(buffer, 0, line, residuo.length, ind + 1);
                byte newresiduo[] = new byte[lun - ind - 1];
                System.arraycopy(buffer, ind + 1, newresiduo, 0, newresiduo.length);
                residuo = newresiduo;
                return line;
            } else if (lun != -1) {
                byte newresiduo[] = new byte[lun + residuo.length];
                System.arraycopy(residuo, 0, newresiduo, 0, residuo.length);
                System.arraycopy(buffer, 0, newresiduo, residuo.length, lun);
                residuo = newresiduo;
                return null;
            } else {
                byte[] line = residuo;
                residuo = new byte[0];
                return line;
            }
        }

        public static int indexOf(byte b[], byte ch, int start, int limit) {
            int p = start;
            while (p < limit) {
                if (b[p] == ch) {
                    return p;
                }
                p++;
            }
            return -1;
        }
    }

    public Vector getPixel(int x, int y, DataLegendDefinition dld) {
        System.out.println(" getPixel() not implemented in ASCIIRasterData class!");
        return null;
    }

    public Dimension getDimension() {
        System.out.println(" getDimension() not implemented in ASCIIRasterData class!");
        return null;
    }

    public Image getImage(Rectangle2D zoomcoordspace, Rectangle2D zoom, Dimension size, DataLegendDefinition legend, Sentinel sentinel, boolean silent) {
        System.out.println(" getImage() not implemented in ASCIIRasterData class!");
        return getImage(zoomcoordspace, zoom, size, legend, sentinel);
    }

    Rectangle window = null;

    public Dimension getRasterDimension() {
        if (this.window != null) {
            return new Dimension((int) window.getWidth(), (int) window.getHeight());
        } else {
            System.out.println("ASCII Raster data is null");
            return null;
        }
    }
}
