package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Composite;

/**
 * Factory to create a Filter combo for image component images
 */
public class ImageFilterComboFactory implements FilterComboFactory {

    /**
     * The name of the element that this filtered combo holds extentions for.
     * The properties for the element will be read from 
     * ControlsMessages.properties to populate the combo once it has been 
     * created.
     */
    private static String ELEMENT_NAME = "imageComponent";

    /**
     * Create the FilterCombo for image components.
     * @return a FilterCombo populated for image components
     */
    public FilterCombo getFilterCombo(Composite container) {
        return new FilterCombo(container, ELEMENT_NAME);
    }
}
