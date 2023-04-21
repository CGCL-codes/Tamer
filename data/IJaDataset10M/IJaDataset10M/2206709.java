package org.openremote.web.console.client.gxtextends;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Implements a String value BaseModelData.
 * Can be used in ComboBox and Grid components.
 */
public class StringModelData extends BaseModelData {

    private static final long serialVersionUID = -4014669528270383777L;

    private String property;

    public StringModelData(String property, String value) {
        super();
        this.property = property;
        set(property, value);
    }

    public String getValue() {
        return get(property);
    }
}
