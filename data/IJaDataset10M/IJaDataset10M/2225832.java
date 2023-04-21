package org.servingMathematics.mqat.ui.NodeEditPanels;

import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.w3c.dom.Node;
import org.servingMathematics.mqat.util.DOMHelper;

/**
 * The parent panel of elemement panel
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 * @version 0.1
 */
public class NodePanel extends JPanel implements PropertyChangeListener {

    Node node;

    ElementPanel elementPanel;

    public NodePanel(Node node) {
        this.node = node;
        initGUI();
    }

    private void initGUI() {
        Node traversalElement;
        NodesPanel nodesPanel;
        setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Node"));
        setLayout(new GridLayout(2, 1));
        elementPanel = null;
        try {
            elementPanel = new ElementPanel(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
        elementPanel.addPropertyChangeListener("node", this);
        add(elementPanel);
        traversalElement = DOMHelper.getFirstChildElement(node);
        if (traversalElement == null) {
            return;
        }
        if (NodesPanel.getNestingLevel() > 2) {
            return;
        }
        nodesPanel = new NodesPanel(traversalElement);
        nodesPanel.addPropertyChangeListener("node", this);
        add(nodesPanel);
    }

    void jbInit() throws Exception {
        initGUI();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("node".equals(evt.getPropertyName())) {
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }
}
