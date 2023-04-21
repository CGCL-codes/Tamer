package com.yeep.objanalyser.common.model;

import java.io.Serializable;
import com.yeep.objanalyser.common.Constants;

@SuppressWarnings("serial")
public abstract class AbstractPersistable implements Serializable {

    private int id = Constants.INVALID_ID;

    private PersistType persistType = PersistType.INSERT;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersistType(PersistType persistType) {
        this.persistType = persistType;
    }

    public boolean isInsert() {
        return PersistType.INSERT == persistType;
    }

    public boolean isUpdate() {
        return PersistType.UPDATE == persistType;
    }

    public boolean isDelete() {
        return PersistType.DELETE == persistType;
    }
}
