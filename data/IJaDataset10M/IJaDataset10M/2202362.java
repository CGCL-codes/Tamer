package edu.ucsd.cse135.gas.bean.chair;

import org.apache.struts.action.ActionForm;

public class AnalyticsBean extends ActionForm {

    private String type;

    private int id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
