package org.parosproxy.paros.extension.scanner;

import javax.swing.tree.DefaultMutableTreeNode;

public class AlertNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1551687819948225491L;

    private String nodeName = null;

    private int risk = -1;

    private int errorCount = 0;

    public AlertNode(int risk, String nodeName) {
        super();
        this.nodeName = nodeName;
        this.setRisk(risk);
    }

    public String toString() {
        if (errorCount > 0) {
            return nodeName + " (" + errorCount + ")";
        }
        return nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void resetErrorCount() {
        errorCount = 0;
    }

    public void incErrorCount() {
        this.errorCount++;
        if (this.getParent() != null) {
            ((AlertNode) this.getParent()).incErrorCount();
        }
    }

    public void decErrorCount() {
        this.errorCount--;
        if (this.getParent() != null) {
            ((AlertNode) this.getParent()).decErrorCount();
        }
    }

    public void setRisk(int risk) {
        this.risk = risk;
    }

    public int getRisk() {
        return risk;
    }
}
