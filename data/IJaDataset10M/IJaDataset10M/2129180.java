package clavicom.tools;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import clavicom.CFilePaths;
import clavicom.core.message.CMessageEngine;
import clavicom.gui.language.UIString;

public class TSwingUtils {

    public static final String JPEG = "jpeg";

    public static final String JPG = "jpg";

    public static final String GIF = "gif";

    public static final String TIFF = "tiff";

    public static final String TIF = "tif";

    public static final String PNG = "png";

    /**
	 * Converti une Image en BufferedImage
	 * @param image
	 * @return
	 */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return ((BufferedImage) image);
        } else {
            image = new ImageIcon(image).getImage();
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            return (bufferedImage);
        }
    }

    /**
	 * Retourne une image redimensionnée
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
    public static ImageIcon scaleImage(ImageIcon image, int width, int height) {
        return new ImageIcon(image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public static ImageIcon getImage(String filepath) {
        File fileImage;
        fileImage = new File(filepath);
        if (fileImage.exists() == false) {
            filepath = CFilePaths.getDefaultPicturePath();
            fileImage = new File(filepath);
            if (fileImage.exists() == false) {
                CMessageEngine.newFatalError(UIString.getUIString("MSG_KEY_IMAGE_FILE_NOT_FOUND_1") + filepath + UIString.getUIString("MSG_KEY_IMAGE_FILE_NOT_FOUND_2"));
            }
        }
        ImageIcon iconImage = new ImageIcon(filepath);
        return iconImage;
    }

    public static boolean hasImageExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        if (ext.equals(JPEG) || ext.equals(JPG) || ext.equals(GIF) || ext.equals(TIFF) || ext.equals(TIF) || ext.equals(PNG)) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Centre un composant à l'ecran
	 * @param component
	 */
    public static void centerComponentToScreen(Component component) {
        Rectangle bounds = new Rectangle(0, 0, component.getWidth(), component.getHeight());
        Dimension screensz = Toolkit.getDefaultToolkit().getScreenSize();
        bounds.x = Math.max(0, (screensz.width - component.getWidth()) / 2);
        bounds.y = Math.max(0, (screensz.height - component.getHeight()) / 2);
        bounds.width = Math.min(bounds.width, screensz.width);
        bounds.height = Math.min(bounds.height, screensz.height);
        component.setBounds(bounds);
    }

    /**
	 * Centre un composant au parent
	 * @param component
	 */
    public static void centerComponentToParent(Component component) {
        Container myParent = component.getParent();
        centerComponentTo(component, myParent);
    }

    /**
	 * Centre un composant relativement à un autre 
	 * @param component
	 * @param parent
	 */
    public static void centerComponentTo(Component component, Component parent) {
        int x;
        int y;
        Point topLeft = parent.getLocationOnScreen();
        Dimension parentSize = parent.getSize();
        Dimension mySize = component.getSize();
        if (parentSize.width > mySize.width) x = ((parentSize.width - mySize.width) / 2) + topLeft.x; else x = topLeft.x;
        if (parentSize.height > mySize.height) y = ((parentSize.height - mySize.height) / 2) + topLeft.y; else y = topLeft.y;
        component.setLocation(x, y);
    }

    /**
	 * Récupère l'extension d'un fichier
	 * @param f
	 * @return
	 */
    public static String getExtension(File f) {
        String s = f.getName();
        return getExtension(s);
    }

    /**
	 * Récupère l'extension d'une chaîne
	 * @param f
	 * @return
	 */
    public static String getExtension(String s) {
        String ext = null;
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static class FiltreSimple extends FileFilter {

        private String description;

        private String extension;

        public FiltreSimple(String description, String extension) {
            if (description == null || extension == null) {
                throw new NullPointerException("La description (ou extension) ne peut être null.");
            }
            this.description = description;
            this.extension = extension;
        }

        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            String nomFichier = file.getName().toLowerCase();
            return nomFichier.endsWith(extension);
        }

        public String getDescription() {
            return description;
        }
    }
}
