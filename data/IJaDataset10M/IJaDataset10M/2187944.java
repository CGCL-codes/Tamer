package org.ndx.jebliki.pages;

import java.util.List;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.ndx.jebliki.HomePage;
import org.ndx.jebliki.model.Page;
import org.ndx.jebliki.persist.PersistException;

/**
 * Base class for all methods concerning only one page
 * @author Nicolas Delsaux
 *
 */
public abstract class SimpleContentPage extends WebPage {

    /**
	 * A simple content page should always receive a PageParameter containing at least the used page
	 * @param parameters
	 */
    public SimpleContentPage(PageParameters parameters) {
        this(parameters, false);
    }

    /**
	 * Generate a simple content page.
	 * This page will be built if a destination page can be built from a
	 * {@link PageLocation}, or if forceBuild is true.
	 * Botice however that calling this constructor on a page that expects 
	 * an existing page with forceBuild set to true may lead to uncontrolled
	 * behaviour.
	 * @param parameters page parameters
	 * @param forceBuild force flag. Set it to true to ensure page creation even if no page
	 * can be built from parameters
	 */
    protected SimpleContentPage(PageParameters parameters, boolean forceBuild) {
        super(parameters);
        add(new BookmarkablePageLink("home", HomePage.class));
        PageLocation location = null;
        try {
            location = PageLocation.locate(parameters);
        } catch (PersistException e) {
            e.printStackTrace();
        }
        if (location != null) {
            Page destination = location.getDestination();
            if (destination == null && !forceBuild) {
            } else {
                build(destination);
            }
        }
    }

    public SimpleContentPage(Page page) {
        super();
        add(new BookmarkablePageLink("home", HomePage.class));
        build(page);
    }

    /**
	 * Method bulding page once the page model object has been retrieved
	 * @param viewed
	 */
    protected abstract void build(Page viewed);
}
