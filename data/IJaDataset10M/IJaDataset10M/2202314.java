package edu.yale.its.tp.cas.ticket;

import java.io.Serializable;

/**
 * Represents a generic CAS ticket.
 */
@SuppressWarnings("serial")
public abstract class Ticket implements Serializable {

    /** Retrieves the ticket's username. */
    public abstract String getUsername();
}
