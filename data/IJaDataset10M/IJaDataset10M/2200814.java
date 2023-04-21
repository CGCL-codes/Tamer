package org.rost.web.thrithon.core;

public class FilterDefinition {

    private String alias = "";

    private ThrithonFilter filter = null;

    private boolean beforeAction = false;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ThrithonFilter getFilter() {
        return filter;
    }

    public void setFilter(ThrithonFilter filter) {
        this.filter = filter;
    }

    public boolean isBeforeAction() {
        return beforeAction;
    }

    public void setBeforeAction(boolean beforeAction) {
        this.beforeAction = beforeAction;
    }
}
