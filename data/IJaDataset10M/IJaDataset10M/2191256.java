package com.lightminds.map.source.providers;

import com.lightminds.map.source.SourceProvider;
import com.lightminds.map.tilescache.CacheProvider;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import org.apache.log4j.Logger;
import com.lightminds.map.source.exceptions.UnableToLoadSourceImageException;
import java.io.File;

/**
 *
 * @author Jon Åkerström
 */
public class TeleAtlasSourceProvider implements SourceProvider {

    private static final Logger logger = Logger.getLogger(TeleAtlasSourceProvider.class);

    private static final ResourceBundle objConf = ResourceBundle.getBundle("tileserver_conf");

    private static final String root = objConf.getString("teleatlas_source_root");

    private static final int scales[] = new int[] { 20, 40, 120, 360, 2500, 4200, 22000, 46000, 108000 };

    private static final int scale_in_1_to[] = new int[] { 9000, 18000, 50000, 150000, 1000000, 1750000, 9000000, 19000000, 45000000 };

    private static final int sizes[] = new int[] { 500, 1000, 3000, 9000, 62500, 105000, 550000, 1150000, 2700000 };

    private static final int src_sizes[] = new int[] { 3000, 6000, 18000, 54000, 375000, 630000, 3300000, 6900000, 16200000 };

    private static final int tileSize = 250;

    private static final int srcImageSize = 1500;

    int minx[] = new int[] { -1336, -1336, -7336, 16664, -950488, -1160488, -3200488, -200488, -14710625 };

    int miny[] = new int[] { -378714, -19257714, -19263714, -4299714, -175193, -520193, -2050193, -3250193, -9446908 };

    int maxx[] = new int[] { 5641664, 11404664, 11404664, 7792664, 5049512, 5139512, 6699512, 6699512, 17689375 };

    int maxy[] = new int[] { 5984286, 4718286, 4748286, 4826286, 5074807, 5149807, 7849807, 10549807, 6753092 };

    private final double getTileWidthInMeters(int zoomlevel) {
        return src_sizes[zoomlevel] * (new Double(tileSize).doubleValue() / srcImageSize);
    }

    private final double getSrcTileWidthInMeters(int zoomlevel) {
        return src_sizes[zoomlevel] * srcImageSize;
    }

    public final int x_indexToX_lower_left(int x_index, int zoomlevel) {
        return (x_index * sizes[zoomlevel]) + minx[zoomlevel];
    }

    public final int y_indexToY_lower_left(int y_index, int zoomlevel) {
        double tilemax = (maxy[zoomlevel] - miny[zoomlevel]) / getTileWidthInMeters(zoomlevel);
        int y = (int) ((tilemax - 1.0 - y_index) * getTileWidthInMeters(zoomlevel)) + miny[zoomlevel];
        return y;
    }

    private int getMappedFileScale(int zoomlevel) {
        return scale_in_1_to[zoomlevel];
    }

    private String formatPath(int x_lower_left, int y_lower_left, int zoomlevel) {
        String strY = "" + y_lower_left;
        int scale = getMappedFileScale(zoomlevel);
        String path = "";
        if (scale <= 150000) {
            path = root + "/" + scale + "/" + strY.substring(0, 4) + "/" + x_lower_left + "x" + y_lower_left + "x" + scales[zoomlevel] + "x1.gif";
        } else {
            path = root + "/" + scale + "/" + x_lower_left + "x" + y_lower_left + "x" + scales[zoomlevel] + "x1.gif";
        }
        return path;
    }

    public byte[] cutAndCache(CacheProvider cacheProvider, int x_index, int y_index, int width, int height, int zoomlevel) throws IOException, UnableToLoadSourceImageException {
        byte[] bImg = null;
        int x_lower_left = x_indexToX_lower_left(x_index, zoomlevel);
        int y_lower_left = y_indexToY_lower_left(y_index, zoomlevel);
        int x_lower_left_src = x_lower_left - (x_lower_left - minx[zoomlevel]) % src_sizes[zoomlevel];
        int y_lower_left_src = y_lower_left - (y_lower_left - miny[zoomlevel]) % src_sizes[zoomlevel];
        int localx = (x_lower_left - minx[zoomlevel]) % src_sizes[zoomlevel];
        int localy = (y_lower_left - miny[zoomlevel]) % src_sizes[zoomlevel];
        int x = (int) ((new Double(localx).doubleValue() / src_sizes[zoomlevel]) * srcImageSize);
        int y = (int) ((new Double(localy).doubleValue() / src_sizes[zoomlevel]) * srcImageSize);
        y = srcImageSize - tileSize - y;
        String path = formatPath(x_lower_left_src, y_lower_left_src, zoomlevel);
        System.out.println("Processing source image : " + path);
        logger.debug("Processing source image : " + path);
        BufferedImage img = ImageIO.read(new File(path));
        ImageWriter iw = (ImageWriter) ImageIO.getImageWritersByFormatName("png").next();
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bi.getGraphics().clipRect(0, 0, width, height);
        bi.getGraphics().drawImage(img, 0 - x, 0 - y, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        iw.setOutput(ImageIO.createImageOutputStream(baos));
        iw.write(bi);
        bImg = baos.toByteArray();
        img.flush();
        bi.flush();
        try {
            cacheProvider.saveTile(bImg, x_index, y_index, zoomlevel);
        } catch (IOException ioe) {
            logger.error("Unable to cache the cut tile to the cache with the TeleAtlasCacheProvider.", ioe);
        }
        return bImg;
    }
}
