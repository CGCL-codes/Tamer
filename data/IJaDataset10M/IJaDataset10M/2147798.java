package org.scub.foundation.framework.gwt.module.client.util.evenement;

/**
 * Classe générique Observateur à encapsuler pour observer d'autres classes.
 * @author Anthony GUILLEMETTE (anthony.guillemette@scub.net)
 */
public abstract class ObservateurGenerique implements Observateur {

    /**
     * {@inheritDoc}
     */
    public abstract void nouvelEvenement(Evenement evenement);
}
