package org.apache.wicket.examples.customresourceloading;

import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 * Index page for custom resource loading.
 * 
 * @author Eelco Hillenius
 */
public class Index extends WicketExamplePage {

    /**
	 * Constructor.
	 */
    public Index() {
        add(new BookmarkablePageLink("customLoadingPageLink", PageWithCustomLoading.class));
    }
}
