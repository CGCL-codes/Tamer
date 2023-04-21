package org.homeunix.thecave.moss.swing.menu;

public interface StandardMenu {

    /**
	 * The method to update menu's enabled / disabled state based on 
	 * window state.  This method is called from AbstractMenuBar, and
	 * should be filtered down to contained Menu's and MenuItems.
	 * 
	 * Abstract implementations (AbstractMenu*) should pass this call
	 * on to sub components; instantiations of these classes should
	 * call super.updateMenus() to make sure that this code still is
	 * executed.
	 * 
	 * As this tends to be called frequently, keep it as small as possible. 
	 * @return
	 */
    public abstract void updateMenus();
}
