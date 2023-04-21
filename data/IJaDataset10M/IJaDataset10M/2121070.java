package net.sf.doolin.gui.action.swing;

import javax.swing.JComponent;

/**
 * Base implementation for the {@link MenuBuilder} implementation. It provides
 * the support for dealing with the separators.
 * 
 * @author Damien Coraboeuf
 * 
 */
public abstract class AbstractMenuBuilder implements MenuBuilder {

    private boolean lastEntryIsSeparator = true;

    /**
	 * Checks if a separator has been created before before creating a new one.
	 */
    @Override
    public void addSeparator() {
        if (!this.lastEntryIsSeparator) {
            createSeparator();
            this.lastEntryIsSeparator = true;
        }
    }

    @Override
    public void add(JComponent component) {
        throw new IllegalStateException("Cannot insert a component here");
    }

    /**
	 * This method <i>must</i> be called by sub-classes whenever they create a
	 * component which is not a separator.
	 */
    protected void noSeparator() {
        this.lastEntryIsSeparator = false;
    }

    /**
	 * This method is called when a sub-class must create a separator.
	 */
    protected abstract void createSeparator();
}
