package uk.ac.essex.imageview.io;

/**
 * <br>
 * Created Date: 13-Jan-2004<br>
 * <p/>
 * You should have received a copy of Lesser GNU public license with this code.
 * If not please visit <a href="http://www.gnu.org/copyleft/lesser.html">this site </a>
 * 
 * @author Laurence Smith
 * @version $Revision: 1.3 $
 */
public class ImageFileOpHelper {

    /**
     * Returns the string needed by the "filestore" and "encode" operations
     * is also used to check users have entered a valid filetype ending ie/
     * it returns null for invalid file endings. The parameter it takes should be
     * one of the supported file types( ie/.jpg, .tif etc) it would then return
     * JPEG and TIFF respectively. (N.B. see the JAI documentation for more
     * details of the supported file types and parameters to filestore/encode)
     * @param s - The file ending to check
     * @return The corresponding parameter required by filestore/encode
     */
    public static String getWriteParam(String s) {
        if (s.length() != 4) return null; else {
            String ext = new String();
            if (s.equals(".jpg")) ext = "JPEG"; else if (s.equals(".bmp")) ext = "BMP"; else if (s.equals(".tif")) ext = "TIFF"; else if (s.equals(".ppm")) ext = "PNM"; else if (s.equals(".pgm")) ext = "PNM"; else if (s.equals(".pbm")) ext = "PNM"; else if (s.equals(".pmg")) ext = "PNG"; else ext = null;
            return ext;
        }
    }

    /**
     * Given something ".../myFile.gif" this will return "gif"
     * @param s
     * @return
     */
    public static String getReadParam(String s) {
        if (s == null) return null;
        int i = s.lastIndexOf('.') + 1;
        String ending = i == -1 ? null : s.substring(i);
        return ending;
    }
}
