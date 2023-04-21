package org.kidsd.dspace.remotesearch;

import org.dspace.app.webui.jsptag.ItemListTag;

public class ItemListTagExt extends ItemListTag {

    private static final long serialVersionUID = 491403719126131217L;

    public static String getDateField() {
        return dateField;
    }

    public static String getListFields() {
        return listFields;
    }

    public static String getTitleField() {
        return titleField;
    }
}
