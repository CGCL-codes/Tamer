package com.impact.xbm.client.coa.data;

import java.util.ArrayList;
import org.w3c.dom.Node;
import com.impact.xbm.client.data.BaseDataItem;
import com.impact.xbm.utils.Xml;

public class ActionHistoryCondition extends BaseDataItem {

    public int numberOfMoves;

    public ArrayList<Action> actions;

    public ActionHistoryCondition(Node node) {
        super(node);
    }

    public ActionHistoryCondition(int numberOfMoves, ArrayList<Action> actions) {
        super();
        this.numberOfMoves = numberOfMoves;
        this.actions = actions;
        this.addDataListeners();
    }

    protected String getDefaultDescriptor() {
        return "Action History Condition";
    }

    public void populateFromNode(Node node) {
        this.numberOfMoves = Xml.getIntegerAttributeValue(node, "NumberOfMoves");
        this.actions = new ArrayList<Action>();
        Node actionsNode = Xml.getSingleChildNode(node, "Actions");
        ArrayList<Node> actionNodes = Xml.getChildNodes(actionsNode, "Action");
        int numActions = (actionNodes == null ? 0 : actionNodes.size());
        for (int i = 0; i < numActions; i++) {
            this.actions.add(new Action(actionNodes.get(i)));
        }
        this.addDataListeners();
    }

    private void addDataListeners() {
        for (Action action : this.actions) {
            action.addDataModifiedListener(this);
        }
    }

    public String toXmlString(int indentLevel) {
        String indent = this.getIndentString(indentLevel);
        String xml = indent + "<ActionHistoryCondition numberOfMoves=\"" + this.numberOfMoves + "\">" + LINE_SEPARATOR;
        if (this.actions != null) {
            String indent1 = this.getIndentString(indentLevel + 1);
            xml += indent1 + "<Actions>" + LINE_SEPARATOR;
            for (Action action : this.actions) {
                xml += action.toXmlString(indentLevel + 2);
            }
            xml += indent1 + "</Actions>" + LINE_SEPARATOR;
        }
        xml += indent + "</ActionHistoryCondition>" + LINE_SEPARATOR;
        return xml;
    }

    @Override
    public String getLabelText() {
        return getDescriptor();
    }

    protected void setupPanelContents() {
        addToPanel(getDisplayFieldJPanel("Number of Moves", String.valueOf(this.numberOfMoves)));
        addToPanel(this.actions);
    }
}
