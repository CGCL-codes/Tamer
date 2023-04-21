package org.caleigo.core.event;

import org.caleigo.core.*;

public class SelectionEvent extends java.util.EventObject {

    public static final int CONTENTS_CHANGED = 1;

    public static final int ENTITY_ADDED = 3;

    public static final int ENTITY_REMOVED = 4;

    private int mEventType;

    private IEntity mEntity;

    private int mRowIndex;

    /** Creates new EntityCollectionEvent with the type CONTENTS_CHANGED.
     */
    public SelectionEvent(ISelection source) {
        super(source);
        mEventType = CONTENTS_CHANGED;
    }

    /** Creates new EntityCollectionEvent with the specified type and entity.
     */
    public SelectionEvent(ISelection source, int eventType, IEntity entity, int row) {
        super(source);
        mEventType = eventType;
        mEntity = entity;
        mRowIndex = row;
    }

    public String toString() {
        String params = null;
        switch(mEventType) {
            case CONTENTS_CHANGED:
                params = "CONTENTS_CHANGED";
                break;
            case ENTITY_ADDED:
                params = "ENTITY_ADDED";
                break;
            case ENTITY_REMOVED:
                params = "ENTITY_REMOVED";
                break;
            default:
                params = "UNKNOWN_TYPE";
                break;
        }
        return getClass().getName() + "[" + params + "] on " + source.toString();
    }

    public ISelection getSourceSelection() {
        return (ISelection) this.getSource();
    }

    public int getEventType() {
        return mEventType;
    }

    public IEntity getEntity() {
        return mEntity;
    }

    public int getRowIndex() {
        return mRowIndex;
    }
}
