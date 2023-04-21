package de.excrawler.server.Imagecrawler;

import de.excrawler.server.System.Configuration.CrawlerConfig;
import de.excrawler.server.Logging.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Pattern;

/**
 *
 * @author Yves Hoppe <info at yves-hoppe.de>
 * @author Karpouzas George <www.webnetsoft.gr>
 */
public class ImageTools extends Thread {

    public static int UNKNOWN = 0;

    public static int JPG = 1;

    public static int JPEG = 1;

    public static int GIF = 2;

    public static int PNG = 3;

    public static int BMP = 4;

    /**
     * Return the filetype of the image on the file ending
     * 1 = jpg, 2 = gif, 3 = png
     * @param filename
     * @return Image Type int
     */
    public static int getImageTypeByEnding(String filename) {
        int type = UNKNOWN;
        if (filename.endsWith("jpg")) type = JPG; else if (filename.endsWith("gif")) type = GIF; else if (filename.endsWith("png")) type = PNG; else if (filename.endsWith("bmp")) type = BMP; else type = UNKNOWN;
        return type;
    }

    /**
     * Returns the name of getImageType
     * @param type
     * @return imgtype name
     */
    public static String getImageTypeName(int type) {
        String imgtype = "OTHER";
        switch(type) {
            case 0:
                imgtype = "Unknown";
                break;
            case 1:
                imgtype = "jpg";
                break;
            case 2:
                imgtype = "gif";
                break;
            case 3:
                imgtype = "png";
                break;
            case 4:
                imgtype = "bmp";
                break;
            default:
                imgtype = "unknown";
                break;
        }
        return imgtype;
    }

    /**
     * Downloads the given image address and returns the filename
     * @param adr
     * @return filename
     */
    public static String downloadImage(String adr) {
        String filename = null;
        URL iURL = null;
        try {
            iURL = new URL(adr);
        } catch (Exception e) {
            Log.imagelogger.warn("Error downloading image - wrong url", e);
            return null;
        }
        filename = downImage(iURL);
        return filename;
    }

    /**
     * Downloads the given image address and returns the filename
     * @param adr
     * @param host
     * @return filename
     */
    public static String downloadImage(String adr, String host) {
        String filename = null;
        URL iURL = null;
        try {
            iURL = new URL(adr);
        } catch (Exception e) {
            if (adr.startsWith("/")) adr = "http://" + host + adr; else adr = "http://" + host + "/" + adr;
            try {
                iURL = new URL(adr);
            } catch (Exception e2) {
                Log.imagelogger.warn("Error wrong url", e2);
                return filename;
            }
        }
        filename = downImage(iURL);
        return filename;
    }

    /**
     * Downloads the given URL and returns the filename
     * @param imageurl
     * @return filename
     */
    public static String downImage(URL imageurl) {
        String filename = null;
        try {
            String tmpname = imageurl.getFile();
            String pattern = "[/]";
            Pattern splitter = Pattern.compile(pattern);
            String[] result = splitter.split(tmpname);
            filename = CrawlerConfig.TMPDIR + File.separatorChar + "images" + result[result.length - 1];
            InputStream in = imageurl.openStream();
            byte[] buffer = new byte[8192];
            FileOutputStream out = new FileOutputStream(new File(filename));
            int _tmp = 0;
            while ((_tmp = in.read(buffer)) > 0) {
                out.write(buffer, 0, _tmp);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            Log.imagelogger.warn("Error downloading image", e);
        }
        return filename;
    }
}
