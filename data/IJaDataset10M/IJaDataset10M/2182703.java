package de.lamasep.navigation;

import de.lamasep.navigation.hints.NavigationHint;
import java.util.List;

/**
 * Eine Klasse die das Interface implementiert kann sich als Listener 
 * registrieren, der benachrichtigt wird, wenn
 * <ol>
 *  <li>ein neuer Navigationshinweis aktuell wird,</li>
 *  <li>alle Navigationshinweise neuberechnet wurden</li>
 * </ol>.
 * 
 * @see NavigationHintCalculator
 */
public interface NavigationHintListener {

    /**
     * Wird aufgerufen, sobald ein neuer Navigationshinweis aktuell wird.
     * @param hint  aktueller Navigationshinweis, <code>hint != null</code>
     * @throws IllegalArgumentException falls <code> hint == null </code>
     */
    void changingEvent(NavigationHint hint);

    /**
     * Wird aufgerufen, sobald alle Navigationshinweise neuberechnet wurden.
     * @param hints     Liste aller neu-berechneten Navigationshinweise,
     *                      <code>hints != null</code>
     * @throws IllegalArgumentException falls <code> hint == null </code>
     */
    void changingEvent(List<NavigationHint> hints);
}
