package net.sf.daro.core.application;

import javax.swing.JFrame;
import net.sf.daro.core.page.Page;
import net.sf.daro.core.page.PageContext;
import net.sf.daro.core.page.PageFactory;

/**
 * An application context defines the contract between the application platform
 * and application.
 * 
 * @author Daniel
 */
public interface ApplicationContext {

    /**
	 * Returns the application frame.
	 * 
	 * @return the application frame
	 */
    JFrame getApplicationFrame();

    /**
	 * Shuts down the application.
	 */
    void shutdownApplication();

    /**
	 * Opens a page using the given page factory.
	 * 
	 * @param pageFactory
	 *            the page factory
	 * @param initialValue
	 *            the initial page value
	 */
    void openPage(PageFactory pageFactory, Object initialValue);

    /**
	 * Opens or selects a page using the given page factory.
	 * 
	 * A non-open page is opened as in {@link #openPage(PageFactory, Object)
	 * openPage(pageFactory, null)}. An open page is just selected.
	 * 
	 * A page with arguments is considered already open irrespective of a
	 * potential initial value used for opening it.
	 * 
	 * @param pageFactory
	 *            the page factory
	 */
    void openOrSelectPage(PageFactory pageFactory);

    /**
	 * Selects a page.
	 * 
	 * @param page
	 *            the page.
	 */
    void selectPage(Page page);

    /**
	 * Closes the given page.
	 * 
	 * @param page
	 *            the page
	 */
    void closePage(Page page);

    /**
	 * Returns the page context for the given page.
	 * 
	 * @param page
	 *            the page
	 * @return the page context or <code>null</code> if no context exist
	 */
    PageContext getPageContext(Page page);

    /**
	 * Notifies all instances of a page of some event.
	 * 
	 * @param pageFactory
	 *            the page factory
	 * @param message
	 *            The message to send to the page.
	 */
    void notifyPage(PageFactory pageFactory, Object message);
}
