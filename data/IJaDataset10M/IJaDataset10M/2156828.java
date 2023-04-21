package util;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import javax.swing.JFrame;

/**
 * A collection of methods used often in the different classes of PSE.
 * 
 * @author Peter Andrews
 * @version 1.1 (12/03/04)
 */
public abstract class Tools {

    /**
     * Returns a point for the upper left-hand corner of a child window so that said
     * child will be centered within the parent window.
     * 
     * @param child     The child window that is to be centered.
     * @param relative  The parent window within which to center the child.
     * 
     * @return A <code>Point</code> object representing the upper left-hand corner of a
     * child window that's centered within the parent.
     */
    public static Point getCenteredPoint(Rectangle child, JFrame relative) {
        int x, y, width, height;
        Rectangle parent = relative.getBounds();
        width = (int) child.getWidth();
        height = (int) child.getHeight();
        x = (int) parent.getX() + (int) (parent.getWidth() / 2) - (width / 2);
        y = (int) parent.getY() + (int) (parent.getHeight() / 2) - (height / 2);
        return new Point(x, y);
    }

    /**
     * Returns the extension portion of a file's name.
     * 
     * @param f     The file for which the suffix is to be made known.
     * @return A String containing the suffix of the file in lower case <b>without</b>
     * the period. So, if "song.mp3" were the name of <code>f</code>, the method would return "mp3".
     */
    public static String getSuffix(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i >= 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    /**
     * Returns the extension part of a file's name.
     * 
     * @param filename  The path or straight file name for which the extension is desired
     * @reutn A String containing the suffix of the file in lower case <b>without</b>
     * the period. So, if "song.mp3" were the name of <code>f</code>, the method would return "mp3".
     */
    public static String getSuffix(String filename) {
        if (filename != null) {
            int i = filename.lastIndexOf('.');
            if (i >= 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }
}
