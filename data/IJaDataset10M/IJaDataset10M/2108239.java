package com.impact.xbm.data.xml.coa.request;

import java.util.ArrayList;
import org.w3c.dom.Node;
import com.impact.xbm.client.data.xml.XmlNodeDataPanel;
import com.impact.xbm.utils.Xml;

public class Move extends XmlNodeDataPanel {

    public String name;

    public ArrayList<Action> actions;

    public Move(Node node) {
        super(node);
    }

    protected String getDefaultDescriptor() {
        return "Move";
    }

    public void populateFromNode(Node node) {
        this.name = Xml.getAttributeValue(node, "name");
        this.actions = new ArrayList<Action>();
        Node actionsNode = Xml.getSingleChildNode(node, "Actions");
        if (actionsNode != null) {
            ArrayList<Node> actionNodes = Xml.getChildNodes(actionsNode, "Action");
            int numActions = (actionNodes != null ? actionNodes.size() : 0);
            for (int i = 0; i < numActions; i++) {
                this.actions.add(new Action(actionNodes.get(i)));
            }
        }
        this.addDataListeners();
    }

    private void addDataListeners() {
        for (Action action : this.actions) {
            action.addDataModifiedListener(this);
        }
    }

    public String toXmlString(int indentLevel) {
        String indent = getIndentString(indentLevel);
        String xml = indent + "<Move " + getAttributeXml("name", this.name) + ">" + LINE_SEPARATOR;
        if ((this.actions != null) && (this.actions.size() > 0)) {
            String indent1 = getIndentString(indentLevel + 1);
            xml += indent1 + "<Actions>" + LINE_SEPARATOR;
            for (Action action : this.actions) {
                xml += action.toXmlString(indentLevel + 2);
            }
            xml += indent1 + "</Actions>" + LINE_SEPARATOR;
        }
        xml += indent + "</Move>" + LINE_SEPARATOR;
        return xml;
    }

    protected void setupPanelContents() {
        addToPanel(getDisplayFieldJPanel(getDescriptor(), this.name));
        addToPanel(this.actions);
    }
}
