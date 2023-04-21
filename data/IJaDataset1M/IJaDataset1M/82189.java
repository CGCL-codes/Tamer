package org.columba.api.gui.frame;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

/**
 * A dockable view which can be registered in a <code>IDock</code>.
 * 
 * @author fdietz
 */
public interface IDockable {

    /**
	 * Return dockable id;
	 * 
	 * @return dockable id
	 */
    public String getId();

    /**
	 * Return dockable human-readable name. This name is used in the Show/Hide
	 * menu and is also the initially displayed title.
	 * 
	 * @return dockable name
	 */
    public String resolveName();

    /**
	 * Return view component.
	 * 
	 * @return view component
	 */
    public JComponent getView();

    /**
	 * Return popup menu instance.
	 * 
	 * @return popup menu
	 */
    public JPopupMenu getPopupMenu();

    /**
	 * Set new dockable title.
	 * 
	 * @param title
	 *            new dockable title
	 */
    public void setTitle(String title);
}
