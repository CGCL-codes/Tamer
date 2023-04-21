package ar.com.hjg.pngj.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLine;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.PngFilterType;
import ar.com.hjg.pngj.PngWriter;

public class CreateHuge {

    public static boolean NULLSTREAM = false;

    public static void main(String[] args) throws Exception {
        createHuge("C:/temp/x.png", 600, 800);
    }

    public static void createHuge(String filename, int cols, int rows) throws Exception {
        final OutputStream os = NULLSTREAM ? new NullOutputStream() : new FileOutputStream(new File(filename));
        final PngWriter png = new PngWriter(os, new ImageInfo(cols, rows, 8, false));
        png.setFilterType(PngFilterType.FILTER_NONE);
        final ImageLine iline1 = new ImageLine(png.imgInfo);
        final ImageLine iline2 = new ImageLine(png.imgInfo);
        ImageLine iline = iline1;
        for (int j = 0; j < cols; j++) {
            ImageLineHelper.setPixelRGB8(iline1, j, ((j & 0xFF) << 16) | (((j * 3) & 0xFF) << 8) | (j * 2) & 0xFF);
            ImageLineHelper.setPixelRGB8(iline2, j, (j * 13) & 0xFFFFFF);
        }
        long t0 = System.currentTimeMillis();
        for (int row = 0; row < rows; row++) {
            iline = row % 4 == 0 ? iline2 : iline1;
            iline.setRown(row);
            png.writeRow(iline);
        }
        png.end();
        long t1 = System.currentTimeMillis();
        System.out.println(png.imgInfo.toString() + "\n msecs: " + (t1 - t0));
    }
}
