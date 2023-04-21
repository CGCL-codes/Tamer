package jiggle.image;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import jiggle.image.raster.GLImageWritableRaster;

public class GLImageIO {

    public static BufferedImage read(File input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("input == null!");
        }
        if (!input.canRead()) {
            throw new IIOException("Can't read input file!");
        }
        ImageInputStream stream = ImageIO.createImageInputStream(input);
        if (stream == null) {
            throw new IIOException("Can't create an ImageInputStream!");
        }
        return read(stream);
    }

    public static BufferedImage read(URL input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("input == null!");
        }
        InputStream istream = null;
        try {
            istream = input.openStream();
        } catch (IOException e) {
            throw new IIOException("Can't get input stream from URL!", e);
        }
        ImageInputStream stream = ImageIO.createImageInputStream(istream);
        BufferedImage bi = read(stream);
        istream.close();
        return bi;
    }

    public static BufferedImage read(InputStream input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("input == null!");
        }
        ImageInputStream stream = ImageIO.createImageInputStream(input);
        return read(stream);
    }

    public static BufferedImage read(ImageInputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("stream == null!");
        }
        Iterator iter = ImageIO.getImageReaders(stream);
        if (!iter.hasNext()) {
            return null;
        }
        ImageReader reader = (ImageReader) iter.next();
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(stream, true, true);
        Iterator it = reader.getImageTypes(0);
        if (!it.hasNext()) {
            throw new IllegalStateException("Reader has no value image types.");
        }
        ImageTypeSpecifier itspec = (ImageTypeSpecifier) it.next();
        param.setDestination(createDestination(param, itspec));
        BufferedImage bi = reader.read(0, param);
        stream.close();
        reader.dispose();
        return bi;
    }

    public static BufferedImage createDestination(ImageReadParam param, ImageTypeSpecifier itspec) {
        ;
        SampleModel sm = itspec.getSampleModel();
        WritableRaster r = new GLImageWritableRaster(sm, new Point());
        return new BufferedImage(createColorModel(), r, false, null);
    }

    public static ColorModel createColorModel() {
        return new DirectColorModel(32, 0xff000000, 0x00ff0000, 0x0000ff00, 0x000000ff);
    }

    public static void main(String[] args) {
        String filename = "/mnt/fat/local/work/arh-model/tiger-pix/tiger-bg.png";
        BufferedImage bi = null;
        try {
            bi = read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
