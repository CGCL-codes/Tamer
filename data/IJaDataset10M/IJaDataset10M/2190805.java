package com.resteassistesprevenu.services.listeners;

/**
 * Listener pour voter pour un incident
 * @author Arnaud
 *
 */
public interface IIncidentsTransportsBackgroundServiceVoteIncidentListener {

    public void dataChanged(boolean voteSent);
}
