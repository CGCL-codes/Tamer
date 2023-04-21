package org.jpublish.module.cayenne.demo;

import org.apache.cayenne.PersistenceState;
import org.jpublish.module.cayenne.demo.auto._Todo;

public class Todo extends _Todo {

    public void setPersistenceState(int state) {
        super.setPersistenceState(state);
        if (state == PersistenceState.NEW) {
            this.setIsDone(Boolean.FALSE);
        }
    }
}
