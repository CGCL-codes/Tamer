package org.rjam.alert;

public interface IRuleView {

    public boolean isShowConjuction();

    public void setShowConjuction(boolean showConjuction);

    public void updateView();

    public Rule getRule();

    public String[] getFieldNames();

    public void setFieldNames(String[] fieldNames);
}
