package com.espada.bugtracker.app;

import org.exolab.castor.jdo.JDO;

/**
 * Title:        All class collection
 * Description:  Collection of all class instances, known by jdo
 * Copyright:    Copyright (c) 2001
 * Company:      JavaFreedom.org
 * @author Max Belugin
 * @version 1.0
 */
public class AllCollection extends BasicOQLCollection {

    public AllCollection(JDO jdo, String className) {
        setJDO(jdo);
        setClassName(className);
    }

    ;

    public String getFrom() {
        return (getClassName() + " inst");
    }

    ;

    public String getWhere() {
        return ("");
    }

    ;

    public JDO getJDO() {
        return (jdo);
    }

    ;

    private void setJDO(JDO jdo) {
        this.jdo = jdo;
    }

    ;

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    JDO jdo;

    private String className;
}

;
