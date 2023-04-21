package com.impact.xbm.client.orchestration.data;

import com.impact.xbm.client.data.BaseDataItem;
import org.w3c.dom.Node;

public class Workflow extends BaseDataItem {

    public String xmlString;

    public Workflow(Node node) {
        super(node);
    }

    public Workflow(String xmlString) {
        super();
        this.xmlString = xmlString;
    }

    protected String getDefaultDescriptor() {
        return "Workflow";
    }

    public void populateFromNode(Node node) {
    }

    public String toXmlString(int indentLevel) {
        return this.xmlString;
    }

    protected void setupPanelContents() {
    }
}
