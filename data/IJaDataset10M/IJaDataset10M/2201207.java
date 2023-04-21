package com.rapidminer.gui.look;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.plaf.UIResource;

/**
 * The UI for image icons.
 *
 * @author Ingo Mierswa
 */
public class ImageIconUIResource extends ImageIcon implements UIResource {

    private static final long serialVersionUID = 705603654836477091L;

    public ImageIconUIResource() {
    }

    public ImageIconUIResource(byte imageData[]) {
        super(imageData);
    }

    public ImageIconUIResource(Image image) {
        super(image);
    }

    public ImageIconUIResource(URL location) {
        super(location, location.toExternalForm());
    }

    public ImageIconUIResource(String filename) {
        super(filename, filename);
    }
}
