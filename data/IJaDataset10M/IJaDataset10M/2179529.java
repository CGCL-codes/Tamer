package org.tolven.XMLEditor.trimplugins;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.merlotxml.merlot.MerlotDOMEditor;
import org.merlotxml.merlot.MerlotDOMNode;
import org.merlotxml.merlot.MerlotDOMText;
import org.merlotxml.merlot.editors.ComplexTypeEditPanel;
import org.merlotxml.util.xml.DTDElement;
import org.merlotxml.util.xml.GrammarComplexType;
import org.tolven.logging.TolvenLogger;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;

public class RootTrimEditor implements MerlotDOMEditor {

    protected List<ComplexTypeEditPanel> panels = new ArrayList<ComplexTypeEditPanel>();

    @Override
    public JPanel getEditPanel(MerlotDOMNode node) {
        panels.clear();
        JPanel container = new JPanel(new GridBagLayout());
        TolvenLogger.info("Custom edit panel", RootTrimEditor.class);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        MerlotDOMNode[] children = node.getChildNodes();
        for (int i = 0; i < children.length; i++) {
            if ("name".equals(children[i].getNodeName()) || "description".equals(children[i].getNodeName()) || "reference".equals(children[i].getNodeName())) {
                c.gridy++;
                ComplexTypeEditPanel panel = new ComplexTypeEditPanel(children[i]);
                panels.add(panel);
                container.add(panel, c);
            }
        }
        JScrollPane sp = new JScrollPane(container, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setMinimumSize(new Dimension(4, 4));
        sp.getViewport().setMinimumSize(new Dimension(4, 4));
        JPanel outerContainer = new JPanel(new BorderLayout());
        outerContainer.setPreferredSize(new Dimension(350, 100));
        outerContainer.add(sp, BorderLayout.NORTH);
        return outerContainer;
    }

    @Override
    public JMenuItem[] getMenuItems(MerlotDOMNode node) {
        return null;
    }

    @Override
    public void grabFocus(JPanel p) {
    }

    @Override
    public void savePanel(JPanel p) throws PropertyVetoException {
        for (ComplexTypeEditPanel ctep : panels) {
            ctep.save();
        }
    }

    @Override
    public boolean suppressAddType(DTDElement el) {
        return false;
    }

    @Override
    public boolean suppressAddType(GrammarComplexType el) {
        boolean suppress = false;
        if ("name".equals(el.getName()) || "description".equals(el.getName()) || "reference".equals(el.getName())) {
            suppress = true;
        }
        return suppress;
    }

    @Override
    public boolean suppressNode(MerlotDOMNode node) {
        Node nd = node.getRealNode();
        int t = nd.getNodeType();
        switch(t) {
            case Node.TEXT_NODE:
                GrammarComplexType complexType = null;
                if (node instanceof MerlotDOMText) {
                    MerlotDOMNode parentNode = node.getParentNode();
                    complexType = parentNode.getGrammarComplexType();
                }
                if (complexType != null && complexType.isMixedType()) {
                    if (nd instanceof CharacterData) {
                        if (node instanceof MerlotDOMText && ((MerlotDOMText) node).isVisible()) {
                            return false;
                        }
                        String s = ((CharacterData) nd).getData();
                        if (s != null) {
                            s = s.trim();
                            if (s.equals("")) {
                                return true;
                            }
                            return false;
                        }
                    }
                }
                return true;
            case Node.COMMENT_NODE:
                return false;
            case Node.PROCESSING_INSTRUCTION_NODE:
                return false;
            case Node.DOCUMENT_NODE:
                return true;
            case Node.DOCUMENT_TYPE_NODE:
                return true;
            case Node.DOCUMENT_FRAGMENT_NODE:
                return true;
        }
        return false;
    }
}
