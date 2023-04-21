package org.escapek.core.logging.impl.interfaces;

import javax.ejb.Local;
import org.escapek.core.internal.model.logging.Event;
import org.escapek.core.internal.model.security.Ticket;

@Local
public interface ILocalLoggingService {

    public void initLogging();

    public Event logLocalEvent(Event e);

    public Event logLocalEvent(Ticket t, Event e);
}
