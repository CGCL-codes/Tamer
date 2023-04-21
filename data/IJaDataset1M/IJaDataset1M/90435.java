package org.rubypeople.rdt.internal.ui.viewsupport;

import org.eclipse.jface.util.Assert;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;

/**
 * Helper class to manage images that should be disposed when a control is disposed
 * contol.addWidgetListener(new ImageDisposer(myImage));
 */
public class ImageDisposer implements DisposeListener {

    private Image[] fImages;

    public ImageDisposer(Image image) {
        this(new Image[] { image });
    }

    public ImageDisposer(Image[] images) {
        Assert.isNotNull(images);
        fImages = images;
    }

    public void widgetDisposed(DisposeEvent e) {
        if (fImages != null) {
            for (int i = 0; i < fImages.length; i++) {
                fImages[i].dispose();
            }
        }
    }
}
