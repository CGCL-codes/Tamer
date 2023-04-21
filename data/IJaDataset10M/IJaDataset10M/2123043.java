package org.waveprotocol.wave.model.account;

import org.waveprotocol.wave.model.wave.SourcesEvents;

/**
 * Observable version of Roles.
 *
 */
public interface ObservableRoles extends Roles, SourcesEvents<ObservableRoles.Listener> {

    /**
   * Listens to changes (addition, removal, update) to roles.
   */
    interface Listener {

        /**
     * A role has changed.
     */
        void onChanged();
    }
}
