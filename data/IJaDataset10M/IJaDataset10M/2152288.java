package com.volantis.mcs.eclipse.builder.wizards.variants;

import com.volantis.mcs.eclipse.controls.ImagePreview;
import com.volantis.mcs.eclipse.controls.Preview;
import com.volantis.mcs.eclipse.controls.UnavailablePreview;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;

/**
 * AssetPreviewer for images.
 */
public class ImageAssetPreviewer {

    /**
     * The preview control for this image asset or an UnavailablePreview
     * if the image cannot be loaded for some reason.
     */
    private Preview preview = null;

    /**
     * Generate the preview for a given image.
     * @param container the Composite to contain the preview control
     * @param style flags to set on the preview control
     * @param image the image we are obtaining the details for.
     */
    public ImageAssetPreviewer(Composite container, int style, ImageData image) {
        if (image == null) {
            preview = new UnavailablePreview(container, style);
        } else {
            final Image swtImage = new Image(null, image);
            preview = new ImagePreview(container, style, swtImage);
            preview.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    swtImage.dispose();
                }
            });
        }
    }

    /**
     * Return the preview object for this image
     * @return an ImagePreview object for this image asset or an
     * UnavailablePreview object if the image is not defined.
     */
    public Preview getPreview() {
        return preview;
    }
}
