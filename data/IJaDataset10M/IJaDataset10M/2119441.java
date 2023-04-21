package ontorama.model.graph.controller;

import org.tockit.events.Event;
import ontorama.ontotools.query.Query;

public class GeneralQueryEvent implements Event {

    private Query query;

    public GeneralQueryEvent(Query subject) {
        this.query = subject;
    }

    public Object getSubject() {
        return query;
    }
}
