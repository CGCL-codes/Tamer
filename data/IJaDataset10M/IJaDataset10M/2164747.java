package com.googlecode.g2re.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Input parameter used to build a report. The parameter will be
 * requested from the end-user in the form of a items box (dropdown).
 * @author Brad Rydzewski
 */
public class ReportParameterDropdown extends ReportParameter {

    /**
     * List of dropdown items.
     * @serial
     */
    private Map<String, String> items = new HashMap<String, String>();

    /**
     * Display size of the items box.
     * @serial
     */
    private int displaySize = 1;

    /**
     * Gets the items of dropdown items. The key in the map is the dropdown
     * item value and the value in the map is the dropdown item text.
     * @return items of dropdown items.
     */
    public final Map<String, String> getItems() {
        return items;
    }

    /**
     * Sets the items of dropdown items. The key in the map is the dropdown
     * item value and the value in the map is the dropdown item text.
     * @param list items of dropdown items.
     */
    public final void setItems(final Map<String, String> list) {
        this.items = list;
    }

    /**
     * Gets the display size of the items box.
     * @return number of items displayed in a items box.
     */
    public final int getDisplaySize() {
        return displaySize;
    }

    /**
     * Sets the display size of the items box. By default the ListBox display
     * size equals one (1), meaning only a single item is visible at a time
     * and all other items appear in a dropdown. A Size greater than one
     * will use scroll bars instead of a dropdown. This corresponds
     * to the "size" attribute of the html "select" tag.
     * @param size number of items displayed in a items box.
     */
    public final void setDisplaySize(final int size) {
        this.displaySize = size;
    }
}
