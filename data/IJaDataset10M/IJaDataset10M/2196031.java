package net.sf.mvc.prototype.view;

/**
 * 
 *
 * View of the MVC-Pattern. A View presents info to the user and also takes input from the user.
 *
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2010-10-31
 *
 */
public interface View {

    /**
	 * 
	 * Enable this {@code View}.
	 * 
	 * <p>
	 * A Swing view for example will call {@link javax.swing.JFrame#show()}.
	 * </p>
	 * 
	 */
    void showView();

    /**
	 * Disable this {@code View} temporally.
	 * 
	 * <p>
	 * A Swing view for example will call {@link javax.swing.JFrame#setVisible(boolean)}.
	 * </p>
	 * 
	 * 
	 */
    void hideView();

    /**
	 * 
	 * Destroy this {@code View}.
	 * 
	 * <p>
	 * A Swing view for example will call {@link javax.swing.JFrame#dispose()}.
	 * </p>
	 * 
	 * 
	 */
    void destroyView();

    /**
	 * 
	 * 
	 * Set this {@code View} enabled or disabled.
	 *
	 */
    void setEnabled(boolean enabled);
}
