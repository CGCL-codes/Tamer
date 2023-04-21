package com.ivis.xprocess.ui.perspectives.homepages.model;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import com.ivis.xprocess.ui.perspectives.homepages.IHomePageElement;

/**
 * The header part of the home page instantiated by Jelly.
 *
 * Usage in an XML home page schema:
 *
 * <xp:header title="" image="" />
 *
 */
public class HeaderElement implements IHomePageElement {

    private String title;

    private String image;

    public void initialize(Composite parent, ViewPart viewPart) {
    }

    /**
     * @return the header text to display on the home page
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set by Jelly from the home page XML schema.
     *
     * The header text.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the image to display before the header text
     */
    public String getImage() {
        return image;
    }

    /**
     * Set by Jelly from the home page XML schema.
     *
     * The last part of the image path - /images/image.gif
     *
     * The full path used starts in com.ivis.xprocess.doc/html
     *
     * The image to display before the header text.
     *
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }
}
