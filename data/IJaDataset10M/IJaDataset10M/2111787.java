package com.tensegrity.palowebviewer.modules.widgets.client.combobox;

/**
 * Base implementation for {@link IComboboxModel}.
 *
 */
public abstract class AbstractComboboxModel implements IComboboxModel {

    protected final ComboboxListenerCollection listenerCollection = new ComboboxListenerCollection();

    /**
     * {@inheritDoc}
     */
    public void addComboboxListener(IComboboxListener listener) {
        listenerCollection.addListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeComboboxListener(IComboboxListener listener) {
        listenerCollection.removeListener(listener);
    }
}
