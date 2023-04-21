package org.fpdev.apps.admin.gui.dialogs;

import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fpdev.apps.admin.shp.ShpImporter.FacilityTypeRule;
import org.fpdev.util.gui.GUIFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.fpdev.apps.admin.AdminClient;
import org.fpdev.apps.admin.shp.ShpImportProfile;
import org.fpdev.apps.admin.shp.ShpImportProfile.AttributeRole;
import org.fpdev.apps.admin.gui.LinkOptionsPanel;
import org.fpdev.apps.admin.shp.ShpImporter.FacilityTypeRule;
import org.fpdev.util.FPUtil;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.opengis.feature.type.AttributeType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author demory
 */
public class ImportStreetsDialog extends JDialog implements ActionListener {

    private AdminClient av_;

    private File file_;

    private List<AttributeType> attrs_;

    private JTextField fileField_;

    private JButton selectFileBtn_;

    private JList profileList_;

    private DefaultListModel profileListModel_;

    private JButton applyProfileBtn_, deleteProfileBtn_, createProfileBtn_;

    private JTabbedPane settingsPane_;

    private TopologyPanel topologyPanel_;

    private FacilityTypePanel facTypePanel_;

    private AttributesPanel attrPanel_;

    private JButton importBtn_, cancelBtn_;

    private boolean importPressed_;

    public ImportStreetsDialog(AdminClient av) {
        this(av, null);
    }

    public ImportStreetsDialog(AdminClient av, String initFile) {
        super(av.getGUI(), "Import Streets Wizard", true);
        av_ = av;
        importPressed_ = false;
        JPanel fileRow = new JPanel(new BorderLayout());
        fileField_ = GUIFactory.newTextField("(None Selected)", 100, 0);
        fileField_.setEditable(false);
        fileField_.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 0, 4), fileField_.getBorder()));
        selectFileBtn_ = GUIFactory.newButton("Select..", 80, this);
        fileRow.add(fileField_, BorderLayout.CENTER);
        fileRow.add(selectFileBtn_, BorderLayout.EAST);
        fileRow.setBorder(new CompoundBorder(new TitledBorder("Shapefile location"), new EmptyBorder(0, 2, 3, 2)));
        JPanel profiles = new JPanel(new BorderLayout());
        profiles.setBorder(new CompoundBorder(new TitledBorder("Profiles"), new EmptyBorder(0, 2, 3, 2)));
        profileListModel_ = new DefaultListModel();
        profileList_ = new JList(profileListModel_);
        JScrollPane profileListSP = new JScrollPane(profileList_);
        applyProfileBtn_ = GUIFactory.newButton("Apply", 60, this);
        deleteProfileBtn_ = GUIFactory.newButton("Delete", 60, this);
        createProfileBtn_ = GUIFactory.newButton("Create from Current", 124, this);
        JPanel profilesBtnArea = GUIFactory.newColumnPanel(), profilesBtnTopRow = GUIFactory.newRowPanel();
        profilesBtnTopRow.add(Box.createHorizontalGlue());
        profilesBtnTopRow.add(applyProfileBtn_);
        profilesBtnTopRow.add(Box.createHorizontalStrut(4));
        profilesBtnTopRow.add(deleteProfileBtn_);
        profilesBtnTopRow.add(Box.createHorizontalGlue());
        profilesBtnTopRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilesBtnArea.add(profilesBtnTopRow);
        profilesBtnArea.add(Box.createVerticalStrut(4));
        createProfileBtn_.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilesBtnArea.add(createProfileBtn_);
        profilesBtnArea.setBorder(new EmptyBorder(5, 0, 0, 0));
        profiles.add(profileListSP, BorderLayout.CENTER);
        profiles.add(profilesBtnArea, BorderLayout.SOUTH);
        profiles.setPreferredSize(new Dimension(150, 0));
        settingsPane_ = new JTabbedPane();
        settingsPane_.add("Topology", createNoFileSelectedPanel());
        settingsPane_.add("Facility Type", createNoFileSelectedPanel());
        settingsPane_.add("Attributes", createNoFileSelectedPanel());
        settingsPane_.setFont(GUIFactory.MAIN_FONT);
        settingsPane_.setBorder(new EmptyBorder(5, 5, 2, 0));
        JPanel center = new JPanel(new BorderLayout());
        center.add(profiles, BorderLayout.WEST);
        center.add(settingsPane_, BorderLayout.CENTER);
        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        importBtn_ = GUIFactory.newButton("Import", 80, this);
        importBtn_.setEnabled(false);
        cancelBtn_ = GUIFactory.newButton("Cancel", 80, this);
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(importBtn_);
        buttonRow.add(Box.createHorizontalStrut(5));
        buttonRow.add(cancelBtn_);
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.setBorder(new EmptyBorder(4, 0, 0, 0));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(fileRow, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(buttonRow, BorderLayout.SOUTH);
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        readProfilesFromFile(av.getEngine().getProperty("5pHome") + "conf" + File.separator + "shpprofiles.xml");
        System.out.println("initFile=" + initFile);
        if (initFile != null) fileSelected(new File(initFile));
        getContentPane().add(mainPanel);
        setSize(520, 340);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createNoFileSelectedPanel() {
        JPanel panel = new JPanel();
        panel.add(GUIFactory.newLabel("(No file selected)"));
        return panel;
    }

    private void readProfilesFromFile(String filename) {
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(filename);
            Node docNode = doc.getChildNodes().item(0);
            if (!docNode.getNodeName().equals("shpprofiles")) return;
            NodeList profileNodes = docNode.getChildNodes();
            for (int i = 0; i < profileNodes.getLength(); i++) {
                Node profileNode = profileNodes.item(i);
                if (profileNode.getNodeName().equals("profile")) {
                    ShpImportProfile profile = new ShpImportProfile(profileNode);
                    av_.getNetworkOps().getShapefileIO().getImporter().addProfile(profile, false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        refreshProfiles();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectFileBtn_) selectFile();
        if (e.getSource() == applyProfileBtn_) applySelectedProfile();
        if (e.getSource() == deleteProfileBtn_) deleteSelectedProfile();
        if (e.getSource() == createProfileBtn_) createProfileFromCurrent();
        if (e.getSource() == importBtn_) importPressed_ = true;
        if (e.getSource() == cancelBtn_ || e.getSource() == importBtn_) setVisible(false);
    }

    public boolean importPressed() {
        return importPressed_;
    }

    public void selectFile() {
        File shp = av_.getNetworkOps().getShapefileIO().chooseShapeFile();
        if (shp != null) fileSelected(shp);
    }

    private void fileSelected(File file) {
        file_ = file;
        fileField_.setText(file.getPath());
        importBtn_.setEnabled(true);
        initAttributesList();
        topologyPanel_ = new TopologyPanel();
        facTypePanel_ = new FacilityTypePanel();
        attrPanel_ = new AttributesPanel();
        settingsPane_.setComponentAt(0, topologyPanel_);
        settingsPane_.setComponentAt(1, facTypePanel_);
        settingsPane_.setComponentAt(2, attrPanel_);
    }

    public URL getFileURL() {
        if (file_ == null) return null;
        try {
            return file_.toURI().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImportStreetsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
   * Scans the currently selected shapefile's attribute table and stores a list
   * of attribute names/types for future reference. 
   */
    private void initAttributesList() {
        try {
            URL shapeURL = file_.toURI().toURL();
            if (shapeURL == null) return;
            ShapefileDataStore store = new ShapefileDataStore(shapeURL);
            attrs_ = store.getSchema().getTypes();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void applySelectedProfile() {
        if (profileList_.isSelectionEmpty()) return;
        ShpImportProfile profile = av_.getNetworkOps().getShapefileIO().getImporter().getProfile(profileList_.getSelectedValue().toString());
        System.out.println("applying profile " + profile.getName());
        topologyPanel_.applyProfile(profile);
        facTypePanel_.applyProfile(profile);
        attrPanel_.applyProfile(profile);
    }

    public void deleteSelectedProfile() {
        if (profileList_.isSelectionEmpty()) return;
        av_.getNetworkOps().getShapefileIO().getImporter().deleteProfile(profileList_.getSelectedValue().toString());
        refreshProfiles();
    }

    public void createProfileFromCurrent() {
        String name = JOptionPane.showInputDialog("Profile Name:").trim();
        if (!av_.getNetworkOps().getShapefileIO().getImporter().checkProfileName(name)) {
            JOptionPane.showMessageDialog(this, "Cannot create profile with duplicate name.");
            return;
        }
        ShpImportProfile profile = getCurrentProfile();
        if (profile == null) return;
        profile.setName(name);
        av_.getNetworkOps().getShapefileIO().getImporter().addProfile(profile, true);
        refreshProfiles();
    }

    /**
   * Initializes an instance of ShapefileIO.ShpImportProfile based on the dialog's
   * currently selected settings.
   * 
   * @return the newly created ShpImportProfile instance, or null if there was a
   * problem creating the instance
   */
    public ShpImportProfile getCurrentProfile() {
        ShpImportProfile profile = new ShpImportProfile();
        if (!topologyPanel_.applyToProfile(profile)) return null;
        if (!facTypePanel_.applyToProfile(profile)) return null;
        if (!attrPanel_.applyToProfile(profile)) return null;
        return profile;
    }

    public void refreshProfiles() {
        profileListModel_.removeAllElements();
        Iterator<ShpImportProfile> iter = av_.getNetworkOps().getShapefileIO().getImporter().getProfiles();
        while (iter.hasNext()) {
            ShpImportProfile profile = iter.next();
            profileListModel_.addElement(profile.getName());
        }
    }

    private void populateComboBox(JComboBox cb, String lookFor) {
        int selIndex = -1;
        int i = 1;
        for (AttributeType attr : attrs_) {
            cb.addItem(attr.getName());
            if (selIndex == -1 && attr.getName().toString().toLowerCase().indexOf(lookFor.toLowerCase()) != -1) {
                selIndex = i - 1;
            }
            i++;
        }
        cb.setSelectedIndex(selIndex == -1 ? 0 : selIndex);
    }

    private class TopologyPanel extends JPanel implements ActionListener {

        private JRadioButton nodesAttrBtn_, nodesAutoBtn_;

        private JComboBox fNodeIDSel_, tNodeIDSel_;

        private JTextField nodeTolField_, nodeFirstIDField_;

        private Set<JComponent> nodesAttrSet_, nodesAutoSet_;

        private JRadioButton linksAttrBtn_, linksAutoBtn_;

        private JComboBox linkIDSel_;

        private JTextField linkFirstIDField_;

        private Set<JComponent> linksAttrSet_, linksAutoSet_;

        public TopologyPanel() {
            super();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(3, 3, 3, 3));
            add(initNodeIDsPanel());
            add(Box.createVerticalStrut(4));
            add(initLinkIDsPanel());
        }

        private JPanel initNodeIDsPanel() {
            nodesAttrSet_ = new HashSet<JComponent>();
            nodesAutoSet_ = new HashSet<JComponent>();
            JPanel nodesPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            nodesPanel.setBorder(GUIFactory.createPaddedTitledBorder("Node IDs", 0, 2, 3, 2));
            ButtonGroup nodesBG = new ButtonGroup();
            nodesAttrBtn_ = GUIFactory.newRadioButton("From Attributes", nodesBG, this);
            nodesAutoBtn_ = GUIFactory.newRadioButton("Auto-Assign", nodesBG, this);
            JPanel nodesLPanel = new JPanel();
            nodesLPanel.setLayout(new BoxLayout(nodesLPanel, BoxLayout.Y_AXIS));
            nodesAttrBtn_.setAlignmentX(LEFT_ALIGNMENT);
            nodesLPanel.add(nodesAttrBtn_);
            fNodeIDSel_ = GUIFactory.newComboBox(100);
            populateComboBox(fNodeIDSel_, "fnode");
            JPanel selFNodeIDRow = createSelectorRow(fNodeIDSel_, "F", nodesAttrSet_);
            tNodeIDSel_ = GUIFactory.newComboBox(100);
            populateComboBox(tNodeIDSel_, "tnode");
            JPanel selTNodeIDRow = createSelectorRow(tNodeIDSel_, "T", nodesAttrSet_);
            nodesLPanel.add(selFNodeIDRow);
            nodesLPanel.add(selTNodeIDRow);
            nodesLPanel.add(Box.createVerticalGlue());
            JPanel nodesRPanel = new JPanel();
            nodesRPanel.setLayout(new BoxLayout(nodesRPanel, BoxLayout.Y_AXIS));
            nodesAutoBtn_.setAlignmentX(LEFT_ALIGNMENT);
            nodesRPanel.add(nodesAutoBtn_);
            nodeTolField_ = GUIFactory.newTextField("10", 12, 40);
            JPanel nodeTolRow = this.createFieldRow(nodeTolField_, "Tolerance", nodesAutoSet_);
            nodesRPanel.add(nodeTolRow);
            nodeFirstIDField_ = GUIFactory.newTextField("1", 12, 40);
            JPanel firstFNodeIDRow = this.createFieldRow(nodeFirstIDField_, "First ID", nodesAutoSet_);
            nodesRPanel.add(firstFNodeIDRow);
            nodesRPanel.add(Box.createVerticalGlue());
            nodesPanel.add(nodesLPanel);
            nodesPanel.add(nodesRPanel);
            nodesAttrBtn_.setSelected(true);
            GUIFactory.disableSet(nodesAutoSet_);
            return nodesPanel;
        }

        private JPanel initLinkIDsPanel() {
            linksAttrSet_ = new HashSet<JComponent>();
            linksAutoSet_ = new HashSet<JComponent>();
            JPanel linksPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            linksPanel.setBorder(GUIFactory.createPaddedTitledBorder("Link IDs", 0, 2, 3, 2));
            ButtonGroup linksBG = new ButtonGroup();
            linksAttrBtn_ = GUIFactory.newRadioButton("From Attribute", linksBG, this);
            linksAutoBtn_ = GUIFactory.newRadioButton("Auto-Assign", linksBG, this);
            JPanel linksLPanel = new JPanel();
            linksLPanel.setLayout(new BoxLayout(linksLPanel, BoxLayout.Y_AXIS));
            linksAttrBtn_.setAlignmentX(LEFT_ALIGNMENT);
            linksLPanel.add(linksAttrBtn_);
            JPanel linkSelIDRow = GUIFactory.newRowPanel();
            linkSelIDRow.add(Box.createHorizontalStrut(5));
            linkIDSel_ = GUIFactory.newComboBox(100);
            populateComboBox(linkIDSel_, "id");
            linksAttrSet_.add(linkIDSel_);
            linkSelIDRow.add(linkIDSel_);
            linkSelIDRow.setAlignmentX(LEFT_ALIGNMENT);
            linksLPanel.add(linkSelIDRow);
            linksLPanel.add(Box.createVerticalGlue());
            JPanel linksRPanel = new JPanel();
            linksRPanel.setLayout(new BoxLayout(linksRPanel, BoxLayout.Y_AXIS));
            linksAutoBtn_.setAlignmentX(LEFT_ALIGNMENT);
            linksRPanel.add(linksAutoBtn_);
            linkFirstIDField_ = GUIFactory.newTextField("1", 12, 40);
            JPanel linkFirstIDRow = this.createFieldRow(linkFirstIDField_, "First ID", linksAutoSet_);
            linksRPanel.add(linkFirstIDRow);
            linksRPanel.add(Box.createVerticalGlue());
            linksPanel.add(linksLPanel);
            linksPanel.add(linksRPanel);
            linksAttrBtn_.setSelected(true);
            GUIFactory.disableSet(linksAutoSet_);
            return linksPanel;
        }

        private JPanel createSelectorRow(JComboBox cb, String name, Set<JComponent> set) {
            JPanel row = GUIFactory.newRowPanel();
            row.add(Box.createHorizontalStrut(5));
            if (name.length() > 0) {
                JLabel label = GUIFactory.newLabel(name + ": ");
                row.add(label);
                set.add(label);
            }
            row.add(cb);
            row.setAlignmentX(LEFT_ALIGNMENT);
            set.add(cb);
            return row;
        }

        private JPanel createFieldRow(JTextField field, String name, Set<JComponent> set) {
            JPanel row = GUIFactory.newRowPanel();
            row.add(Box.createHorizontalStrut(5));
            JLabel label = GUIFactory.newLabel(name + ": ");
            row.add(label);
            row.add(field);
            row.setAlignmentX(LEFT_ALIGNMENT);
            set.add(label);
            set.add(field);
            return row;
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == nodesAttrBtn_) {
                GUIFactory.enableSet(nodesAttrSet_);
                GUIFactory.disableSet(nodesAutoSet_);
            }
            if (e.getSource() == nodesAutoBtn_) {
                GUIFactory.disableSet(nodesAttrSet_);
                GUIFactory.enableSet(nodesAutoSet_);
            }
            if (e.getSource() == linksAttrBtn_) {
                GUIFactory.enableSet(linksAttrSet_);
                GUIFactory.disableSet(linksAutoSet_);
            }
            if (e.getSource() == linksAutoBtn_) {
                GUIFactory.disableSet(linksAttrSet_);
                GUIFactory.enableSet(linksAutoSet_);
            }
        }

        public void applyProfile(ShpImportProfile profile) {
            if (profile.getAutoAssignNodeIDs()) {
                nodesAutoBtn_.setSelected(true);
                GUIFactory.disableSet(nodesAttrSet_);
                GUIFactory.enableSet(nodesAutoSet_);
                nodeFirstIDField_.setText("" + profile.getFirstNodeID());
                nodeTolField_.setText("" + profile.getNodeTolerance());
            } else {
                nodesAttrBtn_.setSelected(true);
                GUIFactory.enableSet(nodesAttrSet_);
                GUIFactory.disableSet(nodesAutoSet_);
                for (int i = 0; i < fNodeIDSel_.getItemCount(); i++) {
                    if (fNodeIDSel_.getItemAt(i).equals(profile.getFNodeIDAttrName())) {
                        fNodeIDSel_.setSelectedIndex(i);
                        break;
                    }
                }
                for (int i = 0; i < tNodeIDSel_.getItemCount(); i++) {
                    if (tNodeIDSel_.getItemAt(i).equals(profile.getTNodeIDAttrName())) {
                        tNodeIDSel_.setSelectedIndex(i);
                        break;
                    }
                }
            }
            if (profile.getAutoAssignLinkIDs()) {
                linksAutoBtn_.setSelected(true);
                GUIFactory.disableSet(linksAttrSet_);
                GUIFactory.enableSet(linksAutoSet_);
                linkFirstIDField_.setText("" + profile.getFirstLinkID());
            } else {
                linksAttrBtn_.setSelected(true);
                GUIFactory.enableSet(linksAttrSet_);
                GUIFactory.disableSet(linksAutoSet_);
                for (int i = 0; i < linkIDSel_.getItemCount(); i++) {
                    if (linkIDSel_.getItemAt(i).equals(profile.getLinkIDAttrName())) {
                        linkIDSel_.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

        public boolean applyToProfile(ShpImportProfile profile) {
            if (nodesAttrBtn_.isSelected()) profile.assignNodeIDsFromField(fNodeIDSel_.getSelectedItem().toString(), tNodeIDSel_.getSelectedItem().toString()); else {
                if (!FPUtil.isInteger(nodeFirstIDField_.getText())) {
                    JOptionPane.showMessageDialog(ImportStreetsDialog.this, "Error: First node ID is not a valid integer");
                    return false;
                }
                int firstNodeID = Integer.parseInt(nodeFirstIDField_.getText());
                if (firstNodeID <= 0) {
                    JOptionPane.showMessageDialog(ImportStreetsDialog.this, "Error: First node ID must be positive");
                    return false;
                }
                if (!FPUtil.isDouble(nodeTolField_.getText())) {
                    JOptionPane.showMessageDialog(ImportStreetsDialog.this, "Error: Node tolerance is not a valid number");
                    return false;
                }
                double tolerance = Double.parseDouble(nodeTolField_.getText());
                if (tolerance <= 0) {
                    JOptionPane.showMessageDialog(ImportStreetsDialog.this, "Error: Node tolerance must be positive");
                    return false;
                }
                profile.autoAssignNodeIDs(firstNodeID, tolerance);
            }
            if (linksAttrBtn_.isSelected()) profile.assignLinkIDsFromField(linkIDSel_.getSelectedItem().toString()); else {
                if (!FPUtil.isInteger(linkFirstIDField_.getText())) {
                    JOptionPane.showMessageDialog(ImportStreetsDialog.this, "Error: First link ID is not a valid integer");
                    return false;
                }
                int firstLinkID = Integer.parseInt(linkFirstIDField_.getText());
                if (firstLinkID <= 0) {
                    JOptionPane.showMessageDialog(ImportStreetsDialog.this, "Error: First link ID must be positive");
                    return false;
                }
                profile.autoAssignLinkIDs(firstLinkID);
            }
            return true;
        }
    }

    private class FacilityTypePanel extends JPanel implements ActionListener {

        private JComboBox defTypeSel_;

        private JPanel rulesListPanel_;

        private JButton createButton_, cloneButton_, deleteButton_;

        private RuleItem selectedRule_;

        private Color defaultBG_;

        public FacilityTypePanel() {
            super(new BorderLayout());
            defaultBG_ = this.getBackground();
            defTypeSel_ = LinkOptionsPanel.createLinkTypeComboBox(220);
            JPanel defTypeRow = GUIFactory.newRowPanel();
            defTypeRow.add(GUIFactory.newLabel("Default Type: "));
            defTypeRow.add(defTypeSel_);
            defTypeRow.setBorder(new EmptyBorder(0, 0, 5, 0));
            rulesListPanel_ = GUIFactory.newColumnPanel();
            createButton_ = GUIFactory.newButton("Create Rule", 100, this);
            cloneButton_ = GUIFactory.newButton("Clone", 60, this);
            deleteButton_ = GUIFactory.newButton("Delete", 60, this);
            cloneButton_.setEnabled(false);
            deleteButton_.setEnabled(false);
            JPanel buttonRow = GUIFactory.newRowPanel();
            buttonRow.add(Box.createHorizontalGlue());
            buttonRow.add(createButton_);
            buttonRow.add(Box.createHorizontalStrut(5));
            buttonRow.add(cloneButton_);
            buttonRow.add(Box.createHorizontalStrut(5));
            buttonRow.add(deleteButton_);
            buttonRow.add(Box.createHorizontalGlue());
            buttonRow.setBorder(new EmptyBorder(5, 0, 0, 0));
            add(defTypeRow, BorderLayout.NORTH);
            JPanel rulePanelContainer = new JPanel(new BorderLayout());
            rulePanelContainer.add(rulesListPanel_, BorderLayout.NORTH);
            add(new JScrollPane(rulePanelContainer), BorderLayout.CENTER);
            add(buttonRow, BorderLayout.SOUTH);
            this.setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createButton_) createRule();
            if (e.getSource() == deleteButton_ && selectedRule_ != null) selectedRule_.delete();
        }

        private void createRule() {
            RuleItem item = new RuleItem();
            rulesListPanel_.add(item);
            rulesListPanel_.revalidate();
        }

        public void applyProfile(ShpImportProfile profile) {
            rulesListPanel_.removeAll();
            for (FacilityTypeRule rule : profile.getFacTypeRules()) {
                rulesListPanel_.add(new RuleItem(rule));
            }
            rulesListPanel_.revalidate();
        }

        public boolean applyToProfile(ShpImportProfile profile) {
            for (Component comp : rulesListPanel_.getComponents()) {
                if (comp instanceof RuleItem) {
                    RuleItem item = (RuleItem) comp;
                    profile.createFacTypeRule(item.getAttrName(), item.getValue(), item.getFacType());
                }
            }
            return true;
        }

        private class RuleItem extends JPanel {

            private JComboBox attrSel_, typeSel_;

            private JTextField valField_;

            public RuleItem() {
                super();
                setBorder(new CompoundBorder(new EmptyBorder(1, 2, 1, 2), new BevelBorder(BevelBorder.RAISED)));
                attrSel_ = GUIFactory.newComboBox(80);
                populateComboBox(attrSel_, "");
                valField_ = GUIFactory.newTextField("", 0, 40);
                typeSel_ = LinkOptionsPanel.createLinkTypeComboBox(120);
                add(attrSel_);
                add(new JLabel("="));
                add(valField_);
                add(new JLabel("→"));
                add(typeSel_);
                this.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        clicked();
                    }
                });
            }

            private RuleItem(FacilityTypeRule rule) {
                this();
                for (int i = 0; i < attrSel_.getItemCount(); i++) {
                    if (attrSel_.getItemAt(i).toString().equals(rule.getAttrName())) {
                        attrSel_.setSelectedIndex(i);
                        break;
                    }
                }
                for (int i = 0; i < typeSel_.getItemCount(); i++) {
                    if (new Integer(typeSel_.getItemAt(i).toString().split("\\.")[0]).intValue() == rule.getFacilityType()) {
                        typeSel_.setSelectedIndex(i);
                        break;
                    }
                }
                valField_.setText(rule.getAttrValue());
            }

            public String getAttrName() {
                return attrSel_.getSelectedItem().toString();
            }

            public String getValue() {
                return valField_.getText();
            }

            public int getFacType() {
                return Integer.parseInt(typeSel_.getSelectedItem().toString().split("\\.")[0]);
            }

            private void clicked() {
                if (selectedRule_ != null) selectedRule_.setBackground(defaultBG_);
                this.setBackground(Color.cyan);
                selectedRule_ = this;
                cloneButton_.setEnabled(true);
                deleteButton_.setEnabled(true);
            }

            public void delete() {
                rulesListPanel_.remove(this);
                rulesListPanel_.revalidate();
                cloneButton_.setEnabled(false);
                deleteButton_.setEnabled(false);
            }
        }
    }

    private class AttributesPanel extends JPanel {

        private JPanel mainPanel_;

        private JComboBox nameSel_, fAddrLSel_, tAddrLSel_, fAddrRSel_, tAddrRSel_, zipLSel_, zipRSel_;

        private Map<ShpImportProfile.AttributeRole, JComboBox> cbLookup_;

        public AttributesPanel() {
            super(new GridLayout(1, 1));
            cbLookup_ = new HashMap<ShpImportProfile.AttributeRole, JComboBox>();
            mainPanel_ = new JPanel();
            mainPanel_.setLayout(new BoxLayout(mainPanel_, BoxLayout.Y_AXIS));
            nameSel_ = GUIFactory.newComboBox(200);
            fAddrLSel_ = GUIFactory.newComboBox(200);
            tAddrLSel_ = GUIFactory.newComboBox(200);
            fAddrRSel_ = GUIFactory.newComboBox(200);
            tAddrRSel_ = GUIFactory.newComboBox(200);
            zipLSel_ = GUIFactory.newComboBox(200);
            zipRSel_ = GUIFactory.newComboBox(200);
            populateComboBox(nameSel_, "name");
            populateComboBox(fAddrLSel_, "fraddl");
            populateComboBox(tAddrLSel_, "toaddl");
            populateComboBox(fAddrRSel_, "fraddr");
            populateComboBox(tAddrRSel_, "toaddr");
            populateComboBox(zipLSel_, "zipl");
            populateComboBox(zipRSel_, "zipr");
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            addRow(ShpImportProfile.AttributeRole.LINK_NAME, "Name", nameSel_);
            addRow(ShpImportProfile.AttributeRole.FADDRL, "From Addr (L)", fAddrLSel_);
            addRow(ShpImportProfile.AttributeRole.TADDRL, "To Addr (L)", tAddrLSel_);
            addRow(ShpImportProfile.AttributeRole.FADDRR, "From Addr (R)", fAddrRSel_);
            addRow(ShpImportProfile.AttributeRole.TADDRR, "To Addr (R)", tAddrRSel_);
            addRow(ShpImportProfile.AttributeRole.ZIPL, "ZIP Code (L)", zipLSel_);
            addRow(ShpImportProfile.AttributeRole.ZIPR, "ZIP Code (R)", zipRSel_);
            mainPanel_.setBorder(new EmptyBorder(5, 5, 5, 5));
            this.add(new JScrollPane(mainPanel_));
        }

        private void addRow(ShpImportProfile.AttributeRole role, String label, JComboBox cb) {
            JLabel l = new JLabel(label);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel_.add(l);
            cb.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel_.add(cb);
            mainPanel_.add(Box.createVerticalStrut(5));
            cbLookup_.put(role, cb);
        }

        public void applyProfile(ShpImportProfile profile) {
            Set<Entry<AttributeRole, String>> mappings = profile.getAttributeRoleMappings();
            for (Entry<AttributeRole, String> mapping : mappings) {
                JComboBox cb = cbLookup_.get(mapping.getKey());
                for (int i = 0; i < cb.getItemCount(); i++) {
                    if (cb.getItemAt(i).toString().equals(mapping.getValue())) {
                        cb.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

        public boolean applyToProfile(ShpImportProfile profile) {
            for (Map.Entry<ShpImportProfile.AttributeRole, JComboBox> entry : cbLookup_.entrySet()) profile.setAttributeMapping(entry.getKey(), entry.getValue().getSelectedItem().toString());
            return true;
        }
    }
}
