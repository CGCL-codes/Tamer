package com.seitenbau.testing.shared.templateengine;

public class SimpleModelParam implements ITemplateParams {

    private Object myModel;

    private Object[] myModelList;

    public SimpleModelParam(Object... aModelList) {
        if (aModelList != null && aModelList.length > 0) {
            myModel = aModelList[0];
            myModelList = aModelList;
        }
    }

    public Object getModel() {
        return myModel;
    }

    public void setModel(Object model) {
        myModel = model;
    }

    public Object getModel(int index) {
        return myModelList[index];
    }
}
