package net.sf.mailsomething.gui.mail.options;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * A skeleton for a reusable optionpanel. This can be extends by an
 * option panel which is to be used with several other optionpanels,
 * for example in a tabbedpane showing several optionpanels with 
 * different settings in each.
 * 
 * This is kept as simple as possible.
 * 
 * @author Stig Tanggaard
 * @created 17-03-2003
 * 
 */
public abstract class AbstractOptionPanel extends JPanel {

    private Vector listeners = new Vector();

    private boolean isBuild = false;

    /**
	 * Override this to build the panel and the components. This will be
	 * called just before the panel is being displayed, ie, by implementing
	 * this the building of the panel can be removed from the constructor
	 * or similar places, where it takes time even though it might not
	 * will be actually displayed. Use isInited() to know if the 
	 * panel is inited, ie, if this method has been called.
	 */
    public void lazyInit() {
    }

    public void setVisible(boolean visible) {
        if (visible && !isBuild) {
            lazyInit();
            isBuild = true;
        }
        super.setVisible(visible);
    }

    /**
	 * Returns true if the option panel (the components) have been
	 * 'build', otherwise false.
	 * 
	 * @return
	 */
    public boolean isInited() {
        return isBuild;
    }

    public void setIsInited(boolean inited) {
        isBuild = inited;
    }

    /**
		 * For adding a changelistener. This is being used by the class
		 * controlling the optionpanel(s).
		 * 
		 * 
		 * 
		 */
    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    /**
		 * This method will be called when its time to apply any
		 * changes, ie, set some property(s) of an object. This
		 * doesnt say anything on how the panel will get write
		 * access to that object (but could be through an argument
		 * in constructor or similar).
		 * 
		 */
    public abstract void apply();

    /**
		 * 
		 * This is suplyed with the intention that it should be called
		 * when there is a change in the properties, ie, when a property
		 * has been edited (but not saved - set).
		 * 
		 * 
		 */
    public void fireChangeEvent(ChangeEvent e) {
        for (int i = 0; i < listeners.size(); i++) {
            ((ChangeListener) listeners.elementAt(i)).stateChanged(e);
        }
    }

    /**
	 * Method for adding an actionlistener to the buttons. 
	 * 
	 * @param listener
	 */
    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    /**
	 * Static method to create a optionpanel 'header'. This is
	 * actually a look&feel attribute and should probably be placed
	 * there somehow. 
	 * 
	 * @param labelstring
	 * @return
	 */
    public static JPanel createHeaderLabel(String labelstring) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JLabel label = new JLabel(labelstring);
        panel.add(label);
        JSeparator sep1 = new JSeparator();
        sep1.setPreferredSize(new Dimension(220, 2));
        panel.add(sep1);
        return panel;
    }
}
