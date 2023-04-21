package gui.node;

import gui.element.NodeElementUI;
import gui.element.NodeElementUIManager;
import gui.element.NodeSingleElementUIManager;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.OverlayLayout;
import models.ModelPropertyEvent;
import data.HyperTextElement;
import data.Node;

public class NodeSinglePanel extends NodePanel {

    private int indexCounter = 0;

    public NodeSinglePanel(Node n) {
        super(n);
        setLayout(new OverlayLayout(this));
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        addActionPanelControl("Element List", new JScrollPane(new NodeElementList(n).asJComponent()));
        JPanel moveUpDownPanel = new JPanel();
        moveUpDownPanel.setLayout(new GridLayout(1, 2));
        moveUpDownPanel.add(new JButton(new AbstractAction("Move Up") {

            public void actionPerformed(ActionEvent e) {
                if (hasActive()) getContent().shiftElement(getContent().getActiveElement(), -1);
            }
        }));
        moveUpDownPanel.add(new JButton(new AbstractAction("Move Down") {

            public void actionPerformed(ActionEvent e) {
                if (hasActive()) getContent().shiftElement(getContent().getActiveElement(), 1);
            }
        }));
        moveUpDownPanel.setMaximumSize(new Dimension(400, 100));
        moveUpDownPanel.setMinimumSize(moveUpDownPanel.getPreferredSize());
        addActionPanelControl("Element Ordering", moveUpDownPanel);
        getMenu("Note").add(new AbstractAction("Add Text Element") {

            public void actionPerformed(ActionEvent e) {
                HyperTextElement hte = new HyperTextElement();
                getContent().insertElement(0, hte);
                getContent().setActiveElement(hte);
            }
        });
    }

    @Override
    public void activeElementChanged(ModelPropertyEvent e) {
        super.activeElementChanged(e);
        if (getContent().getActiveElement() != null) {
            setComponentZOrder(elements.get(getContent().getElementIndex(getContent().getActiveElement())), 0);
            repaint();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (this.getComponentCount() > 0) return getComponent(0).getMinimumSize(); else return super.getPreferredSize();
    }

    @Override
    public void refreshZOrder() {
    }

    @Override
    protected NodeElementUIManager createElementUIManager(NodeElementUI neui) {
        indexCounter++;
        return new NodeSingleElementUIManager(neui, indexCounter);
    }
}
