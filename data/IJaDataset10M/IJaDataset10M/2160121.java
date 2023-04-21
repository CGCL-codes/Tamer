package vademecum.ui;

import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.util.Properties;
import javax.swing.JMenuBar;
import vademecum.core.experiment.ExperimentNode;

/**
 * This interface is to be implemented for 
 * non-modal Windows (Frame, Dialog) that will be used
 * with the navigation-scheme of the Core-Menu "Window"
 *
 * With it comes along the implementation of a PropertyChangeListener and a WindowListener.
 * The VademecumWindow can be registered at Core's registerWindow method. From there goes out 
 * some propertyChangeEvents, if new windows have been opened. The name of the event which core creates is 'menuRefresh'.
 * Same event takes place when windows have been closed. 
 * The best place to register and signoff a VademecumWindow are the methods windowOpened and windowClosing/Closed from the WindowListener-interface.
 *   
 */
public interface VademecumWindow extends PropertyChangeListener, WindowListener {

    /**
	 * Identifier as it will be displayed in the menu. 
	 * @param s
	 */
    public void setWindowName(String s);

    /**
	 * Counterpart to setWindowName
	 * @return
	 */
    public String getWindowName();

    /**
	 * Implementation for Showing up the window.
	 * e.g. just Focus, or placing the window in top of the other windows 
	 */
    public void showWindow();

    /**
	 * Send Results to the Protocol. This can be a thumbnail with the posibility 
	 * for restoring the window through the protocol. Or it can be just textual results.
	 */
    public void addToProtocol();

    /**
	 * Restore Window with Enode and given Properties
	 * @param en
	 * @param p
	 */
    public void restore(ExperimentNode en, Properties p);

    /**
	 * Set Parameters given by a Properties Object.
	 * @param p the Properties
	 */
    public void setProperties(Properties p);

    /**
	 * Encapsulate Parameters in a Properties Object.
	 * @return the Properties
	 */
    public Properties getProperties();
}
