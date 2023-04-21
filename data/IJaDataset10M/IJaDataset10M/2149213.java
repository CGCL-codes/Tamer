package com.cleangwt.client.ext.layout.kit;

import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Jess
 * @date 2011/9/23
 */
public class TopSpan extends VAlign implements HasRowSpan {

    private final int spans;

    /**
	 *
	 * @param spans
	 * @param widgets
	 */
    public TopSpan(int spans, Widget... widgets) {
        this.spans = spans;
        layout(widgets);
    }

    @Override
    public int getSpans() {
        return spans;
    }
}
