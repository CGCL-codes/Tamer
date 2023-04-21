package net.sf.daro.core.page;

import javax.swing.Icon;

/**
 * The PageFactory must be implemented and registered as service for each page.
 * 
 * @author Daniel
 */
public interface PageFactory {

    /**
	 * Returns the localized title of the page factory.
	 * 
	 * @return the title
	 */
    String getTitle();

    /**
	 * Returns the icon of the page factory.
	 * 
	 * @return the icon
	 */
    Icon getIcon();

    /**
	 * Returns the name of the page the factory creates.
	 * 
	 * @return the page name
	 */
    String getPageName();

    /**
	 * Creates and returns a new page instance.
	 * 
	 * @return the page
	 */
    Page createPage();
}
