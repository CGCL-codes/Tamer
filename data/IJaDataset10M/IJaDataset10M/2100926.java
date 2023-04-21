package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.AssetReferenceException;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.styling.Styles;

class TestRolloverImageMenuItemRenderer extends AbstractMenuItemImageRenderer {

    public TestRolloverImageMenuItemRenderer(boolean provideAltText) {
        super(provideAltText);
    }

    public MenuItemRenderedContent render(OutputBuffer buffer, MenuItem item) throws RendererException {
        MenuLabel label = item.getLabel();
        MenuIcon icon = label.getIcon();
        ElementDetails elementDetails = icon.getElementDetails();
        Styles styles = null;
        if (elementDetails != null) {
            styles = elementDetails.getStyles();
        }
        DOMOutputBuffer outputBuffer = (DOMOutputBuffer) buffer;
        Element element = outputBuffer.addElement("rollover-image");
        if (styles != null) {
            element.setStyles(styles);
        }
        String altText = getAltText(item);
        if (altText != null) {
            element.setAttribute("alt", altText);
        }
        try {
            String url;
            url = icon.getNormalURL().getURL();
            element.setAttribute("normal", url);
            url = icon.getOverURL().getURL();
            element.setAttribute("over", url);
        } catch (AssetReferenceException e) {
            throw new RendererException(e);
        }
        return MenuItemRenderedContent.IMAGE;
    }
}
