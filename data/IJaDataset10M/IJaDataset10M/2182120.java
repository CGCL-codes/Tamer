package org.blueoxygen.brigade.classes.actions;

import org.blueoxygen.brigade.entity.Classes;

public class Delete extends ClassForm {

    public String execute() {
        if (getClasses().getId() != null && !"".equalsIgnoreCase(getClasses().getId())) {
            setClasses((Classes) manager.getById(Classes.class, getClasses().getId()));
            manager.remove(getClasses());
        }
        return SUCCESS;
    }
}
