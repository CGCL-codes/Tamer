package org.one.stone.soup.grfxML.plugin.widget;

import java.awt.Image;
import java.io.IOException;
import org.one.stone.soup.grfxML.DataImage;
import org.one.stone.soup.grfxML.DataState;
import org.one.stone.soup.grfxML.DataString;
import org.one.stone.soup.grfxML.GeometryImage;
import org.one.stone.soup.grfxML.GrfxMLEngine;
import org.one.stone.soup.grfxML.TagModifier;
import org.one.stone.soup.grfxML.plugin.SimplePlugin;
import org.one.stone.soup.grfxML.plugin.grfxMLCaster;
import org.one.stone.soup.mjdb.data.field.DataLinker;
import org.one.stone.soup.mjdb.data.field.Field;

public class SimpleButton extends SimplePlugin {

    public static final int ARG_ENABLED = 0;

    public static final int ARG_SELECTED = 1;

    public static final int ARG_OVER = 2;

    public static final int ARG_IMAGE = 3;

    public static final int ARG_IMAGE_UP = 4;

    public static final int ARG_IMAGE_DOWN = 5;

    public static final int ARG_IMAGE_OVER = 6;

    public static final int ARG_IMAGE_DISABLED = 7;

    DataState enabled = new DataState();

    DataState selected = new DataState();

    DataState over = new DataState();

    DataImage image = new DataImage();

    DataString imageUp = new DataString(TagModifier.STRING_TYPE_FILE);

    java.awt.Image iUp;

    DataString imageDown = new DataString(TagModifier.STRING_TYPE_FILE);

    java.awt.Image iDown;

    DataString imageOver = new DataString(TagModifier.STRING_TYPE_FILE);

    java.awt.Image iOver;

    DataString imageDisabled = new DataString(TagModifier.STRING_TYPE_FILE);

    java.awt.Image iDisabled;

    /**
 * SimpleImageButton constructor comment.
 */
    public SimpleButton(GrfxMLEngine engine) {
        super(engine);
    }

    /**
 * initialize method comment.
 */
    public void initialize() {
        iUp = loadImage(imageUp.getValue());
        iDown = loadImage(imageDown.getValue());
        iOver = loadImage(imageOver.getValue());
        iDisabled = loadImage(imageDisabled.getValue());
    }

    /**
 *
 * @return java.awt.Image
 */
    public java.awt.Image loadImage(String fileName) {
        Image newImage = null;
        try {
            try {
                Object resource = org.one.stone.soup.browser.Browser.getResource(fileName);
                if (resource != null && resource instanceof Image) {
                    newImage = (Image) resource;
                } else {
                    newImage = org.one.stone.soup.grfx.ImageFactory.loadImage(org.one.stone.soup.browser.Browser.getInputStream(fileName));
                    org.one.stone.soup.browser.Browser.storeResource(fileName, (Object) newImage);
                }
            } catch (IOException ue) {
                System.out.println("Failed to load '" + fileName + "' in SimpleButton");
            }
        } catch (org.one.stone.soup.grfx.ImageLoadException ie) {
            ie.printStackTrace();
        }
        return newImage;
    }

    /**
 * process method comment.
 */
    public void process() {
        if (image == null || image.getGeometry(getEngine()) == null) return;
        if (enabled.getValue() == false) {
            ((GeometryImage) image.getGeometry(getEngine())).setImage(iDisabled);
            return;
        }
        if (selected.getValue() == true) {
            ((GeometryImage) image.getGeometry(getEngine())).setImage(iDown);
        } else {
            ((GeometryImage) image.getGeometry(getEngine())).setImage(iUp);
        }
        if (over.getValue() == true) {
            ((GeometryImage) image.getGeometry(getEngine())).setImage(iOver);
        }
    }

    /**
 * register method comment.
 */
    public void register(DataLinker store) {
        enabled = grfxMLCaster.cast(enabled, getArg(enabled, ARG_ENABLED, store));
        selected = grfxMLCaster.cast(selected, getArg(selected, ARG_SELECTED, store));
        over = grfxMLCaster.cast(over, getArg(over, ARG_OVER, store));
        image = grfxMLCaster.cast(image, getArg(image, ARG_IMAGE, store));
        imageUp = grfxMLCaster.cast(imageUp, getArg(imageUp, ARG_IMAGE_UP, store));
        imageDown = grfxMLCaster.cast(imageDown, getArg(imageDown, ARG_IMAGE_DOWN, store));
        imageOver = grfxMLCaster.cast(imageOver, getArg(imageOver, ARG_IMAGE_OVER, store));
        imageDisabled = grfxMLCaster.cast(imageDisabled, getArg(imageDisabled, ARG_IMAGE_DISABLED, store));
    }

    /**
 * replace method comment.
 */
    public void replace(Field oldObj, Field newObj) {
        enabled = grfxMLCaster.replace(enabled, oldObj, newObj);
        selected = grfxMLCaster.replace(selected, oldObj, newObj);
        over = grfxMLCaster.replace(over, oldObj, newObj);
        image = grfxMLCaster.replace(image, oldObj, newObj);
        imageUp = grfxMLCaster.replace(imageUp, oldObj, newObj);
        imageDown = grfxMLCaster.replace(imageDown, oldObj, newObj);
        imageOver = grfxMLCaster.replace(imageOver, oldObj, newObj);
        imageDisabled = grfxMLCaster.replace(imageDisabled, oldObj, newObj);
    }

    /**
 * stop method comment.
 */
    public void stop() {
    }
}
