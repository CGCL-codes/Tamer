package uk.ac.cam.asnc.image.gui;

import javax.swing.JFileChooser;
import uk.ac.cam.asnc.image.gui.ImageFilter;
import uk.ac.cam.asnc.image.gui.ImagePreview;

/**
 * Simple wrapper-class for JFileChooser
 * Probably not really necessary but it helps to isolate the GUI from the image processing.
 * 
 * @author pas53
 *
 */
public class ImageFileChooser extends JFileChooser {

    private static final long serialVersionUID = 6840488435050934622L;

    public ImageFileChooser(String fname) {
        super(fname);
        setAccessory(new ImagePreview(this));
        addChoosableFileFilter(new ImageFilter());
    }

    public static int APPROVE_OPTION = JFileChooser.APPROVE_OPTION;
}
