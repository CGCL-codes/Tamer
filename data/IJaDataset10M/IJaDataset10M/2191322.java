package com.michaelzanussi.genalyze.ui.toolbar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import com.michaelzanussi.genalyze.ui.actions.StopAction;
import com.michaelzanussi.genalyze.ui.property.GUIPropertyManager;

/**
 * This class defines the Stop Process toolbar item.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (28 September 2006) 
 */
public class StopToolBarItem extends JButton {

    private static final long serialVersionUID = 1L;

    /**
	 * No-arg constructor.
	 */
    public StopToolBarItem() {
        super();
        setToolTipText(GUIPropertyManager.getInstance().getStopToolTip());
        ClassLoader cldr = this.getClass().getClassLoader();
        setIcon(new ImageIcon(cldr.getResource(GUIPropertyManager.getInstance().getStopMenuItemIcon())));
        setText(GUIPropertyManager.getInstance().getStopMenuItemText());
        addActionListener(new StopAction());
        setFocusable(false);
    }
}
