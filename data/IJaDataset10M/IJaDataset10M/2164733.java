package com.entelience.test.test15export;

import java.util.Date;

public class TestAction implements java.io.Serializable {

    public TestAction() {
    }

    private int actionId;

    private int recId;

    private Date creationDate;

    private String reference;

    public int getRecId() {
        return recId;
    }

    public void setRecId(int id) {
        recId = id;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int id) {
        actionId = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date d) {
        creationDate = d;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String ref) {
        reference = ref;
    }
}
