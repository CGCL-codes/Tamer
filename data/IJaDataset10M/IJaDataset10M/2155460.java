package com.fortuityframework.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

/**
 * @author Jeroen Steenbeeke
 */
public class FortuityTestIndexPage extends WebPage {

    /**
	 * Create a new test index page
	 */
    public FortuityTestIndexPage() {
        add(new Link<Void>("next") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new StatefulComponentPage(1));
            }
        });
    }
}
