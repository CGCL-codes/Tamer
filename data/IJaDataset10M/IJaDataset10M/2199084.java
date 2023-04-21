package net.sf.echopm.screen;

import net.sf.echopm.EchoPMApp;
import net.sf.echopm.navigation.event.EventDispatcher;
import nextapp.echo2.app.event.ActionListener;

/**
 * @author ron
 */
public interface Screen extends ActionListener {

    public void addActionListener(ActionListener actionListener);

    public void removeActionListener(ActionListener actionListener);

    public EventDispatcher getEventDispatcher();

    public EchoPMApp getApp();

    /**
	 * This method gets called by the UIFrameworkApp setCurrentScreen() when a
	 * new screen replaces the original screen.<br/>NOTE: This method should
	 * not be called manually.
	 */
    public void dispose();

    /**
	 * This method gets called by the UIFrameworkApp setCurrentScreen() when a
	 * new screen is set as the current screen.<br/>NOTE: This method should
	 * not be called manually.
	 */
    public void setup();
}
