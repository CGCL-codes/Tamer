package org.gems.designer.model.event;

import java.io.Serializable;
import org.gems.designer.model.ModelObject;
import org.gems.designer.model.Wire;
import org.gems.designer.model.actions.ModelActionContext;

public class ModelChangeEvent implements ModelActionContext, Serializable {

    public static final int ELEMENT_PARENT_CHANGED = 0;

    public static final int ELEMENT_ADDED = 1;

    public static final int ELEMENT_REMOVED = 2;

    public static final int ELEMENT_ATTRIBUTE_CHANGED = 3;

    public static final int CONNECTION_ADDED = 11;

    public static final int CONNECTION_REMOVED = 10;

    public static final int CONNECTION_MODIFIED = 9;

    public static final int MODEL_OPENED = 6;

    public static final int MODEL_CLOSED = 7;

    public static final int MODEL_SAVED = 4;

    public static final int OTHER = 8;

    public static final int MUTATION_EVENTS = ModelChangeEvent.CONNECTION_ADDED | ModelChangeEvent.CONNECTION_REMOVED | ModelChangeEvent.ELEMENT_ADDED | ModelChangeEvent.ELEMENT_REMOVED | ModelChangeEvent.ELEMENT_ATTRIBUTE_CHANGED;

    private boolean proposedTransition_ = false;

    private ModelObject changedElement_;

    private Object change_;

    private int eventType_;

    private Wire wire_;

    private boolean vetoed_ = false;

    private String eventID_;

    private static int currentSequenceNumber_ = 0;

    private static synchronized int nextSeqNumber() {
        return currentSequenceNumber_++;
    }

    private int sequenceNumber_;

    /**
     *  
     */
    public ModelChangeEvent(ModelObject el, int eventtype, Object change) {
        super();
        changedElement_ = el;
        eventType_ = eventtype;
        change_ = change;
        sequenceNumber_ = nextSeqNumber();
    }

    public ModelChangeEvent(ModelObject el, int eventtype, Object change, String eid) {
        super();
        changedElement_ = el;
        eventType_ = eventtype;
        change_ = change;
        eventID_ = eid;
        sequenceNumber_ = nextSeqNumber();
    }

    public ModelChangeEvent(Wire el, int eventtype, boolean proposed, Object change) {
        super();
        wire_ = el;
        eventType_ = eventtype;
        proposedTransition_ = proposed;
        change_ = change;
        sequenceNumber_ = nextSeqNumber();
    }

    public ModelChangeEvent(ModelObject el, int eventtype, boolean proposed, Object change) {
        super();
        changedElement_ = el;
        eventType_ = eventtype;
        proposedTransition_ = proposed;
        change_ = change;
        sequenceNumber_ = nextSeqNumber();
    }

    public ModelObject getChangedElement() {
        return changedElement_;
    }

    public void setChangedElement(ModelObject changedElement) {
        changedElement_ = changedElement;
    }

    public Object getChange() {
        return change_;
    }

    public void setChange(Object change) {
        change_ = change;
    }

    public int getEventType() {
        return eventType_;
    }

    public void setEventType(int eventType) {
        eventType_ = eventType;
    }

    public ModelObject getEventSource() {
        return getChangedElement();
    }

    public boolean isProposedTransition() {
        return proposedTransition_;
    }

    public void setProposedTransition(boolean proposedTransition) {
        proposedTransition_ = proposedTransition;
    }

    public void veto() {
        if (!isProposedTransition()) vetoed_ = true;
        if (canVeto()) vetoed_ = true;
    }

    public boolean vetoed() {
        return vetoed_;
    }

    public boolean canVeto() {
        return true;
    }

    public ModelChangeEvent getEvent() {
        return this;
    }

    public String getEventID() {
        return eventID_;
    }

    public void setEventID(String eventID) {
        eventID_ = eventID;
    }

    public Wire getChangedWire() {
        return wire_;
    }

    public void setChangedWire(Wire w) {
        wire_ = w;
    }

    public int getSequenceNumber() {
        return sequenceNumber_;
    }
}
