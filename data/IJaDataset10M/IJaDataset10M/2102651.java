package org.hip.vif.web.interfaces;

import com.vaadin.ui.Component;

/**
 * Interface for VIF skins.
 * 
 * @author Luthiger
 * Created: 02.01.2012
 */
public interface ISkin {

    /**
	 * @return String this skin bundle's ID, i.e. symbolic name: <code>bundleContext.getBundle().getSymbolicName()</code>.
	 */
    String getSkinID();

    /**
	 * 
	 * @return String the name of the skin, displayed in the skin select view
	 */
    String getSkinName();

    /**
	 * @return String Welcome title for the application's forum part.
	 */
    String getWelcomeForum();

    /**
	 * @return String Welcome title for the application's admin part.
	 */
    String getWelcomeAdmin();

    /**
	 * Create the skin's header component.<br />
	 * Note: the application's layout has 80px height reserved (and full width) for the header.
	 * 
	 * @return {@link Component}
	 */
    Component getHeader();

    /**
	 * Create the skin's footer component.<br />
	 * 
	 * @return {@link Component}
	 */
    Component getFooter();
}
