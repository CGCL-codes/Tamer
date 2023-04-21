package org.eclipse.jface.resource;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public abstract class ImageDescriptor {

    protected static final ImageData DEFAULT_IMAGE_DATA = new ImageData(6, 6, 1, new PaletteData(new RGB[] { new RGB(255, 0, 0) }));

    public abstract ImageData getImageData();

    public Image createImage() {
        return new Image(Display.getCurrent(), getImageData());
    }

    public static ImageDescriptor getMissingImageDescriptor() {
        return MissingImageDescriptor.getInstance();
    }

    public static ImageDescriptor createFromFile(Class<?> location, String filename) {
        return new FileImageDescriptor(location, filename);
    }
}
