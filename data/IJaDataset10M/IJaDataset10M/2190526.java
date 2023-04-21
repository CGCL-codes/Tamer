package com.gwtext.sample.showcase2.client.chooser;

/**
 * Interface that provides the callback name so that it can be called once the image is selected.
 *
 * @author mlim
 */
public interface ImageChooserCallback {

    /**
     * This method will be called by the dialog upon an image been selected
     *
     * @param data is the data information of the selected image
     */
    public void onImageSelection(ImageData data);
}
