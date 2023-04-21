package org.atlantal.impl.cms.view.renderer.list;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.cms.data.MapContentData;
import org.atlantal.api.cms.definition.ContentDefinitionItem;
import org.atlantal.api.cms.definition.List;
import org.atlantal.api.cms.definition.ListItem;
import org.atlantal.api.cms.display.Display;
import org.atlantal.api.cms.util.MultiAccessItems;
import org.atlantal.impl.cms.display.ListRendererInstance;
import org.atlantal.utils.wrapper.ObjectWrapper;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public abstract class ListRendererExport extends ListRendererInstance {

    private static final Logger LOGGER = Logger.getLogger(ListRendererExport.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    /**
     * @param display display
     * @return headers array
     */
    public String[] getHeaders(Display display) {
        boolean alias = (display.getParameter("alias") != null);
        List list = (List) display.getContentDefinitionObject();
        MultiAccessItems items = list.getItemsObject();
        int length = items.size();
        String[] headers = new String[length];
        for (int i = 0; i < length; i++) {
            ContentDefinitionItem item = (ContentDefinitionItem) items.get(i);
            headers[i] = alias ? item.getAlias() : item.getLabel();
        }
        return headers;
    }

    /**
     * @param list list
     * @param row row
     * @return columns array
     */
    public Object[] getColumns(List list, MapContentData row) {
        MultiAccessItems items = list.getItemsObject();
        int length = items.size();
        String[] values = new String[length];
        for (int i = 0; i < length; i++) {
            ObjectWrapper itemW = items.getWrapper(i);
            ListItem item = (ListItem) itemW.getWrappedObject();
            values[i] = item.toString(row);
        }
        return values;
    }
}
