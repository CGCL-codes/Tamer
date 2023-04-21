package free.util.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JDesktopPane;
import free.util.ImageUtilities;

/**
 * Implements some more features on top of JDesktopPane:
 * <UL>
 *   <LI> You can set a wallpaper image which will be either centered, tiled or
 *        stretched.
 * </UL>
 */
public class AdvancedJDesktopPane extends JDesktopPane {

    /**
   * The code for centering the wallpaper image.
   */
    public static final int CENTER = 1;

    /**
   * The code for tiling the wallpaper image.
   */
    public static final int TILE = 2;

    /**
   * The code for scaling the wallpaper image to match the size of the desktop.
   */
    public static final int SCALE = 3;

    /**
   * The wallpaper image.
   */
    private Image wallpaper;

    /**
   * The layout style of the wallpaper image.
   */
    private int wallpaperLayoutStyle = CENTER;

    /**
   * Sets the wallpaper.
   */
    public void setWallpaper(Image wallpaper) {
        this.wallpaper = wallpaper;
        if (wallpaper != null) {
            if (ImageUtilities.preload(wallpaper) != ImageUtilities.COMPLETE) wallpaper = null;
        }
        repaint();
    }

    /**
   * Returns the current wallpaper image.
   */
    public Image getWallpaper() {
        return wallpaper;
    }

    /**
   * Sets the wallpaper layout style. Possible values are {@link #CENTER}, 
   * {@link #SCALE} and {@link #TILE}.
   */
    public void setWallpaperLayoutStyle(int wallpaperLayoutStyle) {
        switch(wallpaperLayoutStyle) {
            case CENTER:
            case TILE:
            case SCALE:
                break;
            default:
                throw new IllegalArgumentException("Illegal wallpaper layout style: " + wallpaperLayoutStyle);
        }
        this.wallpaperLayoutStyle = wallpaperLayoutStyle;
        repaint();
    }

    /**
   * Returns the current wallpaper layout style.
   */
    public int getWallpaperLayoutStyle() {
        return wallpaperLayoutStyle;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension size = this.getSize();
        if (wallpaper != null) {
            int imageWidth = wallpaper.getWidth(this);
            int imageHeight = wallpaper.getHeight(this);
            if ((imageWidth == -1) || (imageHeight == -1)) return; else if (wallpaperLayoutStyle == SCALE) g.drawImage(wallpaper, 0, 0, size.width, size.height, this); else if (wallpaperLayoutStyle == CENTER) {
                int x = (size.width - imageWidth) / 2;
                int y = (size.height - imageHeight) / 2;
                g.drawImage(wallpaper, x, y, this);
            } else if (wallpaperLayoutStyle == TILE) {
                for (int x = 0; x < size.width; x += imageWidth) for (int y = 0; y < size.height; y += imageHeight) g.drawImage(wallpaper, x, y, this);
            }
        }
    }
}
