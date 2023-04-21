package org.atlantal.impl.cms.view.display.item;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.cms.app.content.ContentContext;
import org.atlantal.api.cms.data.MapContentData;
import org.atlantal.api.cms.display.DisplayItem;
import org.atlantal.api.cms.display.DisplayItemType;
import org.atlantal.api.cms.display.DisplayMedia;
import org.atlantal.impl.cms.display.DisplayItemTypeInstance;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public class DisplayItemWeb extends DisplayItemTypeInstance {

    private static final Logger LOGGER = Logger.getLogger(DisplayItemWeb.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    private static DisplayItemType singleton = new DisplayItemWeb();

    /**
     * Constructor
     */
    protected DisplayItemWeb() {
    }

    /**
     * @return itemtype
     */
    public static DisplayItemType getInstance() {
        return singleton;
    }

    /**
     * @param media media
     * @param ctx cm
     * @param dspitem item
     * @param values values
     * @return html string
     */
    public String text(DisplayMedia media, ContentContext ctx, DisplayItem dspitem, MapContentData values) {
        if (dspitem.getItem() == null) {
            return dspitem.getDefaultValue();
        } else {
            String html;
            String val = dspitem.getItemObject().toString(values);
            if ((val != null) && (val.length() > 0)) {
                StringBuilder tmp = new StringBuilder();
                tmp.append("<a href=\"").append(val).append("\"");
                tmp.append(" target=\"_blank\">");
                tmp.append(val).append("</a>");
                html = tmp.toString();
            } else {
                html = "";
            }
            return html;
        }
    }
}
