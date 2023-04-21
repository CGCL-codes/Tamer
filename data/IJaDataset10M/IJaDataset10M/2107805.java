package org.pubcurator.core.events;

import java.util.EventObject;
import org.pubcurator.model.document.PubDocument;

public class EvaluationEvent extends EventObject {

    private static final long serialVersionUID = -8579812969636228555L;

    public static final String EVALUATION_FINISHED = "evaluationFinished";

    private String eventType;

    private PubDocument document;

    public EvaluationEvent(Object source, String eventType) {
        super(source);
        this.setEventType(eventType);
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setDocument(PubDocument document) {
        this.document = document;
    }

    public PubDocument getDocument() {
        return document;
    }
}
