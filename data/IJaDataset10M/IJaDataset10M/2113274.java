package com.aimluck.eip.cayenne.om.portlet;

import org.apache.cayenne.ObjectId;
import com.aimluck.eip.cayenne.om.portlet.auto._EipTTodo;

public class EipTTodo extends _EipTTodo {

    public static final String TODO_NAME_COLUMN = "TODO_NAME";

    public Integer getTodoId() {
        if (getObjectId() != null && !getObjectId().isTemporary()) {
            Object obj = getObjectId().getIdSnapshot().get(TODO_ID_PK_COLUMN);
            if (obj instanceof Long) {
                Long value = (Long) obj;
                return Integer.valueOf(value.intValue());
            } else {
                return (Integer) obj;
            }
        } else {
            return null;
        }
    }

    public void setTodoId(String id) {
        setObjectId(new ObjectId("EipTTodo", TODO_ID_PK_COLUMN, Integer.valueOf(id)));
    }
}
