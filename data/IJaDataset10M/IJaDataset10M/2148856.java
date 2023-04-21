package org.f2o.absurdum.puck.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.f2o.absurdum.puck.gui.graph.Arrow;
import org.f2o.absurdum.puck.gui.graph.Node;
import org.f2o.absurdum.puck.i18n.UIMessages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ArrowPanel extends GraphElementPanel {

    protected Arrow theArrow;

    protected Vector relTypes = new Vector();

    protected String relationshipType;

    protected JComboBox srcComboBox = new JComboBox();

    protected JComboBox dstComboBox = new JComboBox();

    protected JComboBox relComboBox = new JComboBox();

    protected Vector srcNodes;

    protected Vector dstNodes;

    private PropertiesPanel customRelationshipsPanel;

    protected JTabbedPane jtp = new JTabbedPane();

    public static final String NO_STRUCTURAL_RELATIONSHIP = UIMessages.getInstance().getMessage("structural.none");

    public String getNameForElement() {
        if (customRelationshipsPanel != null && relationshipType == NO_STRUCTURAL_RELATIONSHIP) {
            String report = customRelationshipsPanel.getReport();
            if (report.length() > 0) return report;
        }
        return relationshipType;
    }

    public void setRelationshipType(String relType) {
        this.relationshipType = relType;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public Vector getPossibleSourceNodes() {
        return new Vector();
    }

    public Vector getPossibleDestinationNodes() {
        return new Vector();
    }

    protected int indexOf(Vector v, String s) {
        for (int i = 0; i < v.size(); i++) {
            if (s.equals(v.get(i).toString())) return i;
        }
        return -1;
    }

    public void doInitMinimal() {
        addCustomRelationshipsTab();
        srcNodes = getPossibleSourceNodes();
        dstNodes = getPossibleDestinationNodes();
    }

    public void linkWithGraph() {
        removeAll();
        jtp.removeAll();
        this.add(jtp);
        initMinimal();
        srcComboBox = new JComboBox(new DefaultComboBoxModel(srcNodes));
        dstComboBox = new JComboBox(new DefaultComboBoxModel(dstNodes));
        relComboBox = new JComboBox(new DefaultComboBoxModel(relTypes));
        srcComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int index = srcComboBox.getSelectedIndex();
                if (index < 0) {
                    System.err.println("Warning: trying to set source of arrow panel " + ArrowPanel.this + " to index " + index + ". Stack trace:");
                    Thread.dumpStack();
                    return;
                }
                Node n = (Node) srcNodes.get(index);
                theArrow.setSource(n);
            }
        });
        dstComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (dstComboBox.getSelectedIndex() >= 0) {
                    int index = dstComboBox.getSelectedIndex();
                    if (index < 0) {
                        System.err.println("Warning: trying to set source of arrow panel " + ArrowPanel.this + " to index " + index + ". Stack trace:");
                        Thread.dumpStack();
                        return;
                    }
                    Node n = (Node) dstNodes.get(index);
                    theArrow.setDestination(n);
                }
            }
        });
        relComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                relationshipType = (String) relTypes.get(relComboBox.getSelectedIndex());
            }
        });
        srcComboBox.setSelectedIndex(indexOf(srcNodes, theArrow.getSource().getName()));
        dstComboBox.setSelectedIndex(indexOf(dstNodes, theArrow.getDestination().getName()));
        relComboBox.setSelectedIndex(indexOf(relTypes, relationshipType));
    }

    public void refresh() {
        srcComboBox.setModel(new DefaultComboBoxModel(getPossibleSourceNodes()));
        dstComboBox.setModel(new DefaultComboBoxModel(getPossibleDestinationNodes()));
        relComboBox.setModel(new DefaultComboBoxModel(relTypes));
        srcComboBox.setSelectedIndex(indexOf(srcNodes, theArrow.getSource().getName()));
        dstComboBox.setSelectedIndex(indexOf(dstNodes, theArrow.getDestination().getName()));
        relComboBox.setSelectedIndex(indexOf(relTypes, relationshipType));
    }

    public ArrowPanel(Arrow theArrow) {
        super();
        this.theArrow = theArrow;
    }

    public void addCustomRelationshipsTab() {
        if (customRelationshipsPanel == null) customRelationshipsPanel = new PropertiesPanel(UIMessages.getInstance().getMessage("label.relationships"));
        JPanel customRelationshipsTab = new JPanel();
        customRelationshipsTab.setLayout(new BoxLayout(customRelationshipsTab, BoxLayout.PAGE_AXIS));
        customRelationshipsTab.add(customRelationshipsPanel);
        jtp.add(customRelationshipsTab, UIMessages.getInstance().getMessage("tab.customrel"));
    }

    private String getDestinationName() {
        return dstComboBox.getSelectedItem().toString();
    }

    public org.w3c.dom.Node getCustomRelationshipXML(Document d) {
        forceRealCustomRelationshipsInitFromXML();
        Element result = d.createElement("Relationship");
        result.setAttribute("id", getDestinationName());
        Element elt = (Element) customRelationshipsPanel.getXML(d);
        result.appendChild(elt);
        return result;
    }

    private org.w3c.dom.Node cachedRelationshipsNode = null;

    boolean inittedRels = false;

    public final synchronized void forceRealCustomRelationshipsInitFromXML() {
        if (isCacheEnabled() && !inittedRels && cachedRelationshipsNode != null) {
            doInitCustomRelationshipsFromXML(cachedRelationshipsNode);
            inittedRels = true;
            cachedRelationshipsNode = null;
        }
    }

    public final void initCustomRelationshipsFromXML(org.w3c.dom.Node n) {
        if (isCacheEnabled()) {
            cachedRelationshipsNode = n;
        } else doInitCustomRelationshipsFromXML(n);
    }

    public void doInitCustomRelationshipsFromXML(org.w3c.dom.Node n) {
        Element e = (Element) n;
        NodeList nl1 = e.getElementsByTagName("PropertyList");
        if (nl1.getLength() > 0) {
            Element plElt = (Element) nl1.item(0);
            customRelationshipsPanel.initFromXML(plElt);
        }
    }

    public String describeArrow() {
        return "[ src=" + srcComboBox.getSelectedItem() + "(" + theArrow.getSource() + ")" + " dst=" + dstComboBox.getSelectedItem() + "(" + theArrow.getDestination() + ")" + " ]";
    }

    protected boolean hasSourceAndDestination() {
        return theArrow.getSource() != null && this.getPossibleSourceNodes().contains(theArrow.getSource()) && theArrow.getDestination() != null && this.getPossibleDestinationNodes().contains(theArrow.getDestination());
    }
}
