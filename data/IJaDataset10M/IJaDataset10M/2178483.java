package com.genia.toolbox.web.gwt.form.client.widget.item.impl;

import com.genia.toolbox.web.gwt.form.client.form.RadioButtonItem;

/**
 * <code>SimpleItemWidget</code> to represents a group of radio.
 */
public class RadioButtonItemWidget extends AbstractRadioButtonItemWidget<RadioButtonItem> {

    /**
   * constructor.
   * 
   * @param item
   *          the item that this widget represents.
   */
    public RadioButtonItemWidget(final RadioButtonItem item) {
        super(item);
        setValuedItemEntrys(item, item.getEntries().getValuedItemEntries());
    }
}
