package org.atlantal.impl.cms.view.display.item;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.cms.app.content.ContentContext;
import org.atlantal.api.cms.data.MapContentData;
import org.atlantal.api.cms.display.DisplayItem;
import org.atlantal.api.cms.display.DisplayItemType;
import org.atlantal.api.cms.display.DisplayMedia;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public class DisplayItemHtml extends DisplayItemDefault {

    private static final Logger LOGGER = Logger.getLogger(DisplayItemHtml.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    private static DisplayItemType singleton = new DisplayItemHtml();

    /**
     * Constructor
     */
    protected DisplayItemHtml() {
    }

    /**
     * @return itemtype
     */
    public static DisplayItemType getInstance() {
        return singleton;
    }

    /**
     * @return boolean
     */
    public boolean hasTitle() {
        return false;
    }

    /**
     * @param media media
     * @param ctx cm
     * @param dspitem item
     * @param values values
     * @return html string
     */
    public String text(DisplayMedia media, ContentContext ctx, DisplayItem dspitem, MapContentData values) {
        String html = "";
        if (dspitem.getItem() == null) {
            String defval = dspitem.getDefaultValue();
            if (defval != null) {
                html = defval;
            }
        } else {
            String val = dspitem.getItemObject().toString(values);
            if (val.length() > 0) {
                html = val;
            } else {
                String defval = dspitem.getDefaultValue();
                if (defval != null) {
                    html = defval;
                }
            }
        }
        return html;
    }
}
