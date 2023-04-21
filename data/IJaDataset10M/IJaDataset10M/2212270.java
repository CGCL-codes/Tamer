package com.vlee.ejb.inventory;

import java.util.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class InvDoesNotExistException extends InventoryException {

    String entityName = null;

    String id = null;

    public InvDoesNotExistException(String entityName, String id) {
        super("");
        this.entityName = entityName;
        this.id = id;
    }

    public InvDoesNotExistException(String entityName, String id, String msg) {
        super(msg);
        this.entityName = entityName;
        this.id = id;
    }

    public String getMessage() {
        return super.getMessage() + "entityName=" + entityName + ",id=" + id;
    }

    public String toString() {
        return super.toString() + "entityName=" + entityName + ",id=" + id;
    }
}
