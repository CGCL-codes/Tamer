package de.lamasep.navigation;

import de.lamasep.map.addresses.Address;

/**
 * Eine Klasse die das Interface implementiert kann sich als Listener 
 * registrieren, der benachrichtigt wird, sobald ein Zwischen- oder Endziel einer Route
 * erreicht wurde.
 */
public interface DestinationReachedListener {

    /**
     * Wird aufgerufen, sobald das Zwischen- oder Endziel einer Route erreicht wurde.
     * @param adr                   Adresse des erreichten Ziels,
     *                              <code>adr != null</code>
     * @param isLastDestination     <code>true</code>: <code>adr</code> ist das Endziel, 
     *                              <code>false</code>: sonst
     * @throws IllegalArgumentException falls <code>adr == null</code>
     */
    void reached(Address adr, boolean isLastDestination);
}
