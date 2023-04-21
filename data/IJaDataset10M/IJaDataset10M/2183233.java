package org.tolven.teditor.controller;

import java.util.ArrayList;
import javax.swing.JTextArea;
import javax.swing.JTree;
import org.tolven.teditor.model.TreeNodes.DuplicateNodeException;
import org.tolven.teditor.model.TreeNodes.TolvenTreeNode;

public class CommandCenter {

    private SetCommand setcommand;

    private ResetCommand resetcommand;

    private GetCommand getcommand;

    private java.util.List<String> trimmenus = new ArrayList<String>();

    public CommandCenter(JTree tree, TolvenTreeNode node, JTextArea log) {
        this.setcommand = new SetCommand(tree, node, log);
        this.resetcommand = new ResetCommand(tree, node, log);
        this.getcommand = new GetCommand(node);
    }

    public void setData(Object nodeData) throws DuplicateNodeException {
        setcommand.execute(nodeData);
    }

    public void resetData(Object nodeData) {
        resetcommand.execute(nodeData);
    }

    public Object getData() {
        return getcommand.getNodeValue();
    }

    public void refreshTree() {
        setcommand.refreshTree();
    }

    public java.util.List<String> getTrimmenus() {
        return trimmenus;
    }

    public void setTrimmenus(java.util.List<String> trimmenus) {
        this.trimmenus = trimmenus;
    }

    public String getPathtoRoot() {
        return getcommand.getPath();
    }
}
