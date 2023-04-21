package org.qsari.effectopedia.system;

import org.qsari.effectopedia.base.IndexedObject;

public class ActionType extends IndexedObject {

    public ActionType() {
        super();
        autoSetId();
    }

    public ActionType(long id, String descriptoin, String fieldName) {
        super();
        setID(id);
        this.description = descriptoin;
        this.fieldName = fieldName;
    }

    public long autoId() {
        return actionTypeIDs++;
    }

    public String getDescription() {
        return description;
    }

    public String getFieldName() {
        return fieldName;
    }

    private String description;

    private String fieldName;

    protected static long actionTypeIDs = 0;
}
