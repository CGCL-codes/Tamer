package com.google.code.jqwicket;

import org.apache.wicket.markup.html.WebMarkupContainer;
import com.google.code.jqwicket.JQBehaviors;

public class SortablePage extends DemoPage {

    public SortablePage() {
        add(new WebMarkupContainer("sortable").add(JQBehaviors.sortable()));
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see com.google.code.jqwicket.DemoPage#getExampleTitle()
	 */
    @Override
    protected String getExampleTitle() {
        return "JQuery UI Sortable example";
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see com.google.code.jqwicket.DemoPage#getExampleDescription()
	 */
    @Override
    protected String getExampleDescription() {
        return "This example demonstrates JQuery UI Sortable integration";
    }
}
