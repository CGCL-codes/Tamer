package com.volantis.mcs.protocols.gallery.renderers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.gallery.attributes.GalleryAttributes;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.renderers.StylesExtractor;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModulesDefinitionRegistry;

/**
 * Renderer for ChannelsCount element 
 */
public class GalleryDefaultRenderer extends BaseGalleryDefaultRenderer {

    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES = new ActionName[] { ActionName.NEXT, ActionName.PREVIOUS };

    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES = new PropertyName[] { PropertyName.START_ITEM_NUMBER, PropertyName.END_ITEM_NUMBER, PropertyName.ITEMS_COUNT, PropertyName.PAGE_NUMBER, PropertyName.PAGES_COUNT };

    /**
     * Attributes of the rendered div element of this element.
     */
    private DivAttributes galleryDivAttributes;

    private StringWriter scriptWriter;

    private String galleryId;

    private ArrayList displayIds;

    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        require(WIDGET_GALLERY, protocol, attributes, true);
        galleryId = attributes.getId();
        if (galleryId == null) {
            galleryId = protocol.getMarinerPageContext().generateUniqueFCID();
        }
        galleryDivAttributes = new DivAttributes();
        galleryDivAttributes.copy(attributes);
        if (galleryDivAttributes.getId() == null) {
            galleryDivAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        protocol.writeOpenDiv(galleryDivAttributes);
        scriptWriter = new StringWriter();
        scriptWriter.write("Widget.register(" + createJavaScriptString(galleryId) + ",");
        addCreatedWidgetId(galleryId);
        scriptWriter.write("Gallery.createGallery({");
        scriptWriter.write("displays:[");
        displayIds = new ArrayList();
    }

    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        GalleryAttributes galleryAttributes = (GalleryAttributes) attributes;
        protocol.writeCloseDiv(galleryDivAttributes);
        for (int index = 0; index < displayIds.size(); index++) {
            String displayId = (String) displayIds.get(index);
            if (index != 0) scriptWriter.write(",");
            scriptWriter.write(createJavaScriptString(displayId));
            addUsedWidgetId(displayId);
        }
        displayIds = null;
        scriptWriter.write("]");
        String itemsId = galleryAttributes.getItems();
        if (itemsId == null) {
            itemsId = protocol.getMarinerPageContext().generateFCID("GALLERY_ITEMS");
        }
        scriptWriter.write(",items:" + createJavaScriptString(itemsId));
        addUsedWidgetId(itemsId);
        if (galleryAttributes.getSlideshow() != null) {
            scriptWriter.write(",slideshow:" + createJavaScriptString(galleryAttributes.getSlideshow()));
            addUsedWidgetId(galleryAttributes.getSlideshow());
        }
        if (galleryAttributes.getSlideshowPopup() != null) {
            scriptWriter.write(",slideshowPopup:" + createJavaScriptString(galleryAttributes.getSlideshowPopup()));
            addUsedWidgetId(galleryAttributes.getSlideshowPopup());
        }
        StylesExtractor stylesExtractor = createStylesExtractor(protocol, galleryAttributes.getStyles());
        double slideshowLaunchDelay = stylesExtractor.getSlideshowLaunchDelay();
        if (!Double.isInfinite(slideshowLaunchDelay)) {
            scriptWriter.write(",slideshowLaunchDelay:" + Double.toString(slideshowLaunchDelay));
        }
        scriptWriter.write("}))");
        try {
            getJavaScriptWriter().write(scriptWriter.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
        scriptWriter = null;
    }

    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {
        return isWidgetSupported(protocol);
    }

    /**
     * @inheritDoc
     */
    protected ActionName[] getSupportedActionNames() {
        return SUPPORTED_ACTION_NAMES;
    }

    /**
     * @inheritDoc
     */
    protected PropertyName[] getSupportedPropertyNames() {
        return SUPPORTED_PROPERTY_NAMES;
    }

    public void renderItemDisplay(String id) {
        if (displayIds != null) {
            displayIds.add(id);
        }
    }
}
