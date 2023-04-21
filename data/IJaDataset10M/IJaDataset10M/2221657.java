package org.sgodden.echo.ext20.peers;

import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import org.sgodden.echo.ext20.AbstractButton;
import org.sgodden.echo.ext20.CheckboxField;

/**
 * Synchronization peer for {@link AbstractButton}.
 */
public abstract class AbstractButtonPeer extends ExtComponentPeer {

    /**
     * Default constructor.
     */
    public AbstractButtonPeer() {
        super();
        addEvent(new AbstractComponentSynchronizePeer.EventPeer(AbstractButton.INPUT_ACTION, AbstractButton.PROPERTY_ACTION_LISTENERS_CHANGED) {

            @Override
            public boolean hasListeners(Context context, Component component) {
                return ((AbstractButton) component).hasActionListeners();
            }
        });
    }

    /**
     * @see nextapp.echo.webcontainer.AbstractComponentSynchronizePeer#getInputPropertyClass(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class getInputPropertyClass(String propertyName) {
        if (AbstractButton.PROPERTY_PRESSED.equals(propertyName)) {
            return Boolean.class;
        }
        return null;
    }

    /**
     * @see nextapp.echo.webcontainer.ComponentSynchronizePeer#storeInputProperty(Context, Component, String, int, Object)
     */
    @Override
    public void storeInputProperty(Context context, Component component, String propertyName, int propertyIndex, Object newValue) {
        if (propertyName.equals(AbstractButton.PROPERTY_PRESSED)) {
            ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
            clientUpdateManager.setComponentProperty(component, AbstractButton.PROPERTY_PRESSED, newValue);
        }
    }
}
