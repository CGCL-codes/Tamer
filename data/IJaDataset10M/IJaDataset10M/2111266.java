package com.volantis.mcs.protocols.gallery.renderers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.gallery.attributes.ItemsAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;

/**
 * Renderer for ItemsElement.
 * 
 * Works both for XDIME, and response.
 * 
 * It also performs actual rendering of following elements:
 *  - ItemElement
 *  - SummaryElement
 *  - DefailElement
 */
public class ItemsDefaultRenderer extends BaseGalleryDefaultRenderer {

    private List itemIds;

    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        ItemsAttributes itemsAttributes = (ItemsAttributes) attributes;
        boolean insideWidgetResponse = (itemsAttributes.getCount() != null);
        if (!insideWidgetResponse) {
            renderOpenForWidget(protocol, itemsAttributes);
        } else {
            renderOpenForResponse(protocol, itemsAttributes);
        }
        itemIds = new ArrayList();
    }

    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        ItemsAttributes itemsAttributes = (ItemsAttributes) attributes;
        if (itemsAttributes.getLoadAttributes() != null) {
            require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
        }
        boolean insideWidgetResponse = (itemsAttributes.getCount() != null);
        if (!insideWidgetResponse) {
            renderCloseForWidget(protocol, itemsAttributes);
        } else {
            renderCloseForResponse(protocol, itemsAttributes);
        }
        itemIds = null;
    }

    private void renderOpenForWidget(VolantisProtocol protocol, ItemsAttributes attributes) throws ProtocolException {
        require(WIDGET_GALLERY, protocol, attributes, true);
    }

    private void renderCloseForWidget(VolantisProtocol protocol, ItemsAttributes attributes) throws ProtocolException {
        LoadAttributes loadAttributes = attributes.getLoadAttributes();
        String itemsId = attributes.getId();
        if (itemsId == null) {
            itemsId = protocol.getMarinerPageContext().generateFCID("GALLERY_ITEMS");
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("Widget.register(").append(createJavaScriptString(itemsId)).append(", ").append("Gallery.createItems({");
        addCreatedWidgetId(itemsId);
        if (loadAttributes != null) {
            buffer.append("loadURL:" + createJavaScriptString(loadAttributes.getSrc()));
            if (loadAttributes.getWhen() != null) {
                if (loadAttributes.getWhen().equals("defer")) {
                    buffer.append(",loadOnDemand:true");
                }
                if (loadAttributes.getWhen().equals("onload")) {
                    buffer.append(",loadOnDemand:false");
                }
            }
        } else {
            buffer.append("items:[");
            Iterator iterator = itemIds.iterator();
            boolean firstItemId = true;
            while (iterator.hasNext()) {
                String itemId = (String) iterator.next();
                if (firstItemId) {
                    firstItemId = false;
                } else {
                    buffer.append(", ");
                }
                buffer.append(createJavaScriptWidgetReference(itemId));
                addUsedWidgetId(itemId);
            }
            buffer.append("]");
        }
        buffer.append("}))");
        try {
            getJavaScriptWriter().write(buffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
        itemIds = null;
    }

    private void renderOpenForResponse(VolantisProtocol protocol, ItemsAttributes attributes) throws ProtocolException {
    }

    private void renderCloseForResponse(VolantisProtocol protocol, ItemsAttributes attributes) throws ProtocolException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Gallery.itemsRequest.setResponse(").append("new Gallery.Response([");
        Iterator iterator = itemIds.iterator();
        boolean firstItemId = true;
        while (iterator.hasNext()) {
            String itemId = (String) iterator.next();
            if (firstItemId) {
                firstItemId = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(createJavaScriptWidgetReference(itemId));
            addUsedWidgetId(itemId);
        }
        buffer.append("], ").append(attributes.getCount()).append("))");
        try {
            getJavaScriptWriter().write(buffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
        itemIds = null;
    }

    public void addItemId(String id) {
        itemIds.add(id);
    }
}
