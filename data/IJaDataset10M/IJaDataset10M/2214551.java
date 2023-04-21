package org.scub.foundation.framework.gwt.module.client.util.evenement;

/**
 * Interface permettant de devenir observateur d'une classe observable.
 * @author Goumard Stéphane.
 */
public interface Observateur {

    /**
     * Appelé par les classes observés pour notifier d'un evenement.
     * @param evenement l'objet evenement
     */
    void nouvelEvenement(Evenement evenement);
}
