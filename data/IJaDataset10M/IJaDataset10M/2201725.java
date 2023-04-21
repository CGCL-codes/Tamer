package com.genia.toolbox.web.gwt.form.client.widget.item.impl;

import com.genia.toolbox.web.gwt.basics.client.widget.TimeChooser;
import com.genia.toolbox.web.gwt.form.client.CssNames;
import com.genia.toolbox.web.gwt.form.client.form.TimeItem;

/**
 * <code>SimpleItemWidget</code> representing a time.
 */
public class TimeItemWidget extends AbstractPeriodItemWidget<TimeItem> {

    /**
   * constructor.
   * 
   * @param item
   *          the item this object represents
   */
    public TimeItemWidget(final TimeItem item) {
        super(item, new TimeChooser(), CssNames.TIME_WIDGET_CSS_STYLE);
    }
}
