package org.xulbooster.eclipse.xb.ui.editors.svg.util;

import org.eclipse.swt.graphics.Image;
import org.xulbooster.eclipse.xb.ui.editors.svg.util.SharedSVGEditorPluginImageHelper;

public class SVGImageFactory {

    public static Image getImageForElement(String elementName) {
        Image image = null;
        if ("bindings".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_BINDINGS);
        } else if ("binding".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_BINDING);
        } else if ("resources".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_RESOURCES);
        } else if ("resource".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_RESOURCE);
        } else if ("implementation".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_IMPLEMENTATION);
        } else if ("constructor".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_CONSTRUCTOR);
        } else if ("content".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_CONTENT);
        } else if ("destructor".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_DESTRUCTOR);
        } else if ("field".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_FIELD);
        } else if ("handler".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_HANDLER);
        } else if ("handlers".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_HANDLERS);
        } else if ("method".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_METHODE);
        } else if ("property".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_PROPERTY);
        } else if ("parameter".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_PARAMETER);
        } else if ("body".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_BODY);
        } else if ("setter".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_SETTER);
        } else if ("getter".compareTo(elementName) == 0) {
            image = SharedSVGEditorPluginImageHelper.getImage(SharedSVGEditorPluginImageHelper.IMG_XBL_GETTER);
        }
        return image;
    }
}
