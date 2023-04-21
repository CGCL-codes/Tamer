package com.google.code.jqwicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import com.google.code.jqwicket.ui.notifier.NotifierWebMarkupContainer;

public class NotifierPage extends DemoPage {

    @SuppressWarnings("serial")
    public NotifierPage() {
        final NotifierWebMarkupContainer notifier = new NotifierWebMarkupContainer("notifier1");
        add(notifier);
        add(new AjaxLink<Void>("notifier.open1") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                notifier.create(target, "Test Notification", "This is an example of the default config, and will fade out after five seconds.");
            }
        });
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see com.google.code.jqwicket.DemoPage#getExampleTitle()
	 */
    @Override
    protected String getExampleTitle() {
        return "JQuery Notifier Plugin example";
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see com.google.code.jqwicket.DemoPage#getExampleDescription()
	 */
    @Override
    protected String getExampleDescription() {
        return "This example demonstrates JQuery Notifier Plugin integration";
    }
}
