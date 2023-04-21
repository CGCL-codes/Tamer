package org.semanticweb.mmm.mr3.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.event.*;
import org.semanticweb.mmm.mr3.data.*;
import org.semanticweb.mmm.mr3.jgraph.*;
import org.semanticweb.mmm.mr3.util.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * @author takeshi morita
 *
 */
public class PrefDialog extends JInternalFrame implements ListSelectionListener {

    private JTabbedPane tab;

    private JTextField defaultLangField;

    private JComboBox uiLangBox;

    private JCheckBox isAntialiasBox;

    private JComboBox uriPrefixBox;

    private JLabel baseURILabel;

    private JTextField workDirectoryField;

    private JButton browseWorkDirectoryButton;

    private JCheckBox isProxy;

    private JTextField proxyHost;

    private JTextField proxyPort;

    private JLabel inputEncodingLabel;

    private ComboBoxModel inputEncodingBoxModel;

    private JComboBox inputEncodingBox;

    private JLabel outputEncodingLabel;

    private ComboBoxModel outputEncodingBoxModel;

    private JComboBox outputEncodingBox;

    private GraphManager gmanager;

    private Preferences userPrefs;

    private JTextField metaClassField;

    private JList classClassList;

    private DefaultListModel classClassListModel;

    private JButton classClassEditButton;

    private JButton classClassAddButton;

    private JButton classClassRemoveButton;

    private JList propClassList;

    private DefaultListModel propClassListModel;

    private JButton propClassEditButton;

    private JButton propClassAddButton;

    private JButton propClassRemoveButton;

    private JCheckBox isColorBox;

    private JButton rdfResourceColorButton;

    private JButton literalColorButton;

    private JButton classColorButton;

    private JButton propertyColorButton;

    private JButton selectedColorButton;

    private JButton backgroundColorButton;

    private Color rdfResourceColor;

    private Color literalColor;

    private Color classColor;

    private Color propertyColor;

    private Color selectedColor;

    private Color backgroundColor;

    private JButton applyButton;

    private JButton cancelButton;

    private static final String EDIT = Translator.getString("Edit");

    private static final String ADD = Translator.getString("Add");

    private static final String REMOVE = Translator.getString("Remove");

    public PrefDialog(GraphManager manager, Preferences prefs) {
        super(Translator.getString("PreferenceDialog.Title"), false, false, false);
        gmanager = manager;
        userPrefs = prefs;
        tab = new JTabbedPane();
        tab.add(Translator.getString("PreferenceDialog.BaseTab"), getBasePanel());
        tab.add(Translator.getString("PreferenceDialog.MetaClassListTab"), getMetaClassListPanel());
        tab.add(Translator.getString("PreferenceDialog.RenderingTab"), getRenderingPanel());
        getContentPane().add(tab);
        getContentPane().add(getButtonGroupPanel(), BorderLayout.SOUTH);
        initParameter();
        setSize(new Dimension(500, 400));
        setLocation(100, 100);
        setVisible(true);
    }

    private static final int URI_FIELD_WIDTH = 300;

    private static final int URI_FIELD_HEIGHT = 40;

    private JPanel getButtonGroupPanel() {
        applyButton = new JButton(MR3Constants.APPLY);
        applyButton.addActionListener(new DesideAction());
        cancelButton = new JButton(MR3Constants.CANCEL);
        cancelButton.addActionListener(new DesideAction());
        JPanel buttonGroup = new JPanel();
        buttonGroup.add(applyButton);
        buttonGroup.add(cancelButton);
        return buttonGroup;
    }

    private JPanel getBasePanel() {
        initLangField();
        initEncodingBox();
        initBaseURIField();
        initWorkDirectoryField();
        initProxyField();
        JPanel basePanel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        basePanel.setLayout(gridbag);
        c.weighty = 5;
        c.anchor = GridBagConstraints.WEST;
        layoutLangField(basePanel, gridbag, c);
        layoutEncodingBox(basePanel, gridbag, c);
        layoutBaseURIField(basePanel, gridbag, c);
        layoutWorkDirectory(basePanel, gridbag, c);
        layoutProxyField(basePanel, gridbag, c);
        return basePanel;
    }

    private void initColorButton(JButton button, String name, int width, int height, Action action) {
        button.setHorizontalAlignment(JButton.LEFT);
        button.setIcon(new ColorSwatch(name));
        button.setPreferredSize(new Dimension(width, height));
        button.addActionListener(action);
    }

    private static final int LONG_URI_FIELD_WIDTH = 450;

    private JPanel getMetaClassListPanel() {
        metaClassField = new JTextField();
        Utilities.initComponent(metaClassField, "URI", LONG_URI_FIELD_WIDTH, URI_FIELD_HEIGHT);
        classClassListModel = new DefaultListModel();
        classClassList = new JList(classClassListModel);
        classClassList.addListSelectionListener(this);
        JScrollPane classClassListScroll = new JScrollPane(classClassList);
        Utilities.initComponent(classClassListScroll, "Class Class List", LONG_URI_FIELD_WIDTH, 80);
        Action classClassButtonAction = new ClassClassButtonAction();
        classClassEditButton = new JButton(EDIT);
        classClassEditButton.addActionListener(classClassButtonAction);
        classClassAddButton = new JButton(ADD);
        classClassAddButton.addActionListener(classClassButtonAction);
        classClassRemoveButton = new JButton(REMOVE);
        classClassRemoveButton.addActionListener(classClassButtonAction);
        JPanel classClassButtonPanel = new JPanel();
        classClassButtonPanel.add(classClassEditButton);
        classClassButtonPanel.add(classClassAddButton);
        classClassButtonPanel.add(classClassRemoveButton);
        propClassListModel = new DefaultListModel();
        propClassList = new JList(propClassListModel);
        propClassList.addListSelectionListener(this);
        JScrollPane propClassListScroll = new JScrollPane(propClassList);
        Utilities.initComponent(propClassListScroll, "Property Class List", LONG_URI_FIELD_WIDTH, 80);
        Action propClassButtonAction = new PropClassButtonAction();
        propClassEditButton = new JButton(EDIT);
        propClassEditButton.addActionListener(propClassButtonAction);
        propClassAddButton = new JButton(ADD);
        propClassAddButton.addActionListener(propClassButtonAction);
        propClassRemoveButton = new JButton(REMOVE);
        propClassRemoveButton.addActionListener(propClassButtonAction);
        JPanel propClassButtonPanel = new JPanel();
        propClassButtonPanel.add(propClassEditButton);
        propClassButtonPanel.add(propClassAddButton);
        propClassButtonPanel.add(propClassRemoveButton);
        JPanel metaClassListPanel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        metaClassListPanel.setLayout(gridbag);
        c.weighty = 5;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(metaClassField, c);
        metaClassListPanel.add(metaClassField);
        gridbag.setConstraints(classClassListScroll, c);
        metaClassListPanel.add(classClassListScroll);
        gridbag.setConstraints(classClassButtonPanel, c);
        metaClassListPanel.add(classClassButtonPanel);
        gridbag.setConstraints(propClassListScroll, c);
        metaClassListPanel.add(propClassListScroll);
        gridbag.setConstraints(propClassButtonPanel, c);
        metaClassListPanel.add(propClassButtonPanel);
        return metaClassListPanel;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == classClassList) {
            if (!classClassList.isSelectionEmpty()) {
                metaClassField.setText(classClassList.getSelectedValue().toString());
                propClassList.clearSelection();
            }
        } else if (e.getSource() == propClassList) {
            if (!propClassList.isSelectionEmpty()) {
                metaClassField.setText(propClassList.getSelectedValue().toString());
                classClassList.clearSelection();
            }
        }
    }

    class ClassClassButtonAction extends AbstractAction {

        private boolean isEditable() {
            return !classClassList.isSelectionEmpty() && classClassList.getSelectedIndices().length == 1 && !isDefaultClass(classClassList.getSelectedValue()) && isAddable();
        }

        private boolean isAddable() {
            return !classClassListModel.contains(metaClassField.getText()) && !propClassListModel.contains(metaClassField.getText());
        }

        private void edit() {
            if (isEditable()) {
                classClassListModel.setElementAt(metaClassField.getText(), classClassList.getSelectedIndex());
            }
        }

        private void add() {
            if (isAddable()) {
                classClassListModel.addElement(metaClassField.getText());
            }
        }

        private boolean isDefaultClass(Object item) {
            return item.equals(RDFS.Class.toString()) || item.equals(OWL.Class.toString());
        }

        private void remove() {
            if (classClassList.isSelectionEmpty()) {
                return;
            }
            List removeList = Arrays.asList(classClassList.getSelectedValues());
            for (Iterator i = removeList.iterator(); i.hasNext(); ) {
                Object item = i.next();
                if (!isDefaultClass(item)) {
                    classClassListModel.removeElement(item);
                }
            }
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals(EDIT)) {
                edit();
            } else if (command.equals(ADD)) {
                add();
            } else if (command.equals(REMOVE)) {
                remove();
            }
        }
    }

    class PropClassButtonAction extends AbstractAction {

        private boolean isEditable() {
            return !propClassList.isSelectionEmpty() && propClassList.getSelectedIndices().length == 1 && !isDefaultProperty(propClassList.getSelectedValue()) && isAddable();
        }

        private boolean isAddable() {
            return !propClassListModel.contains(metaClassField.getText()) && !classClassListModel.contains(metaClassField.getText());
        }

        private void edit() {
            if (isEditable()) {
                propClassListModel.setElementAt(metaClassField.getText(), propClassList.getSelectedIndex());
            }
        }

        private void add() {
            if (isAddable()) {
                propClassListModel.addElement(metaClassField.getText());
            }
        }

        private boolean isDefaultProperty(Object item) {
            return item.equals(RDF.Property.toString()) || item.equals(OWL.ObjectProperty.toString()) || item.equals(OWL.DatatypeProperty.toString());
        }

        private void remove() {
            if (propClassList.isSelectionEmpty()) {
                return;
            }
            List removeList = Arrays.asList(propClassList.getSelectedValues());
            for (Iterator i = removeList.iterator(); i.hasNext(); ) {
                Object item = i.next();
                if (!isDefaultProperty(item)) {
                    propClassListModel.removeElement(item);
                }
            }
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals(EDIT)) {
                edit();
            } else if (command.equals(ADD)) {
                add();
            } else if (command.equals(REMOVE)) {
                remove();
            }
        }
    }

    private static final int BUTTON_WIDTH = 200;

    private static final int BUTTON_HEIGHT = 25;

    private JPanel getRenderingPanel() {
        ChangeColorAction action = new ChangeColorAction();
        rdfResourceColorButton = new JButton("RDF Resource Color");
        initColorButton(rdfResourceColorButton, "Resource", BUTTON_WIDTH, BUTTON_HEIGHT, action);
        literalColorButton = new JButton("Literal Color");
        initColorButton(literalColorButton, "Literal", BUTTON_WIDTH, BUTTON_HEIGHT, action);
        classColorButton = new JButton("Class Color");
        initColorButton(classColorButton, "Class", BUTTON_WIDTH, BUTTON_HEIGHT, action);
        propertyColorButton = new JButton("Property Color");
        initColorButton(propertyColorButton, "Property", BUTTON_WIDTH, BUTTON_HEIGHT, action);
        selectedColorButton = new JButton("Selected Color");
        initColorButton(selectedColorButton, "Selected", BUTTON_WIDTH, BUTTON_HEIGHT, action);
        backgroundColorButton = new JButton("Background Color");
        initColorButton(backgroundColorButton, "Background", BUTTON_WIDTH, BUTTON_HEIGHT, action);
        isColorBox = new JCheckBox("Color");
        isAntialiasBox = new JCheckBox("Antialias");
        JPanel renderingPanel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        renderingPanel.setLayout(gridbag);
        c.weighty = 5;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(rdfResourceColorButton, c);
        renderingPanel.add(rdfResourceColorButton);
        gridbag.setConstraints(literalColorButton, c);
        renderingPanel.add(literalColorButton);
        gridbag.setConstraints(classColorButton, c);
        renderingPanel.add(classColorButton);
        gridbag.setConstraints(propertyColorButton, c);
        renderingPanel.add(propertyColorButton);
        gridbag.setConstraints(selectedColorButton, c);
        renderingPanel.add(selectedColorButton);
        gridbag.setConstraints(backgroundColorButton, c);
        renderingPanel.add(backgroundColorButton);
        gridbag.setConstraints(isColorBox, c);
        renderingPanel.add(isColorBox);
        gridbag.setConstraints(isAntialiasBox, c);
        renderingPanel.add(isAntialiasBox);
        return renderingPanel;
    }

    class ChangeColorAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Color current = Color.black;
            if (e.getSource() == rdfResourceColorButton) {
                current = rdfResourceColor;
            } else if (e.getSource() == literalColorButton) {
                current = literalColor;
            } else if (e.getSource() == classColorButton) {
                current = classColor;
            } else if (e.getSource() == propertyColorButton) {
                current = propertyColor;
            } else if (e.getSource() == selectedColorButton) {
                current = selectedColor;
            } else if (e.getSource() == backgroundColorButton) {
                current = backgroundColor;
            }
            Color c = JColorChooser.showDialog(getContentPane(), "Choose Color", current);
            if (c == null) {
                c = current;
            }
            if (e.getSource() == rdfResourceColorButton) {
                rdfResourceColor = c;
            } else if (e.getSource() == literalColorButton) {
                literalColor = c;
            } else if (e.getSource() == classColorButton) {
                classColor = c;
            } else if (e.getSource() == propertyColorButton) {
                propertyColor = c;
            } else if (e.getSource() == selectedColorButton) {
                selectedColor = c;
            } else if (e.getSource() == backgroundColorButton) {
                backgroundColor = c;
            }
        }
    }

    private void layoutProxyField(JPanel innerPanel, GridBagLayout gridbag, GridBagConstraints c) {
        JPanel proxyPanel = new JPanel();
        proxyPanel.add(isProxy);
        proxyPanel.add(proxyHost);
        proxyPanel.add(proxyPort);
        gridbag.setConstraints(proxyPanel, c);
        innerPanel.add(proxyPanel);
    }

    private void layoutWorkDirectory(JPanel innerPanel, GridBagLayout gridbag, GridBagConstraints c) {
        JPanel workDirectoryPanel = new JPanel();
        workDirectoryPanel.add(workDirectoryField);
        workDirectoryPanel.add(browseWorkDirectoryButton);
        gridbag.setConstraints(workDirectoryPanel, c);
        innerPanel.add(workDirectoryPanel);
    }

    private void layoutBaseURIField(JPanel innerPanel, GridBagLayout gridbag, GridBagConstraints c) {
        c.gridwidth = GridBagConstraints.REMAINDER;
        JPanel baseURIPanel = new JPanel();
        baseURIPanel.add(uriPrefixBox);
        baseURIPanel.add(baseURILabel);
        gridbag.setConstraints(baseURIPanel, c);
        innerPanel.add(baseURIPanel);
    }

    private void layoutLangField(JPanel innerPanel, GridBagLayout gridbag, GridBagConstraints c) {
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(defaultLangField, c);
        innerPanel.add(defaultLangField);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(uiLangBox, c);
        innerPanel.add(uiLangBox);
    }

    private void layoutEncodingBox(JPanel innerPanel, GridBagLayout gridbag, GridBagConstraints c) {
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(inputEncodingLabel, c);
        innerPanel.add(inputEncodingLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(inputEncodingBox, c);
        innerPanel.add(inputEncodingBox);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(outputEncodingLabel, c);
        innerPanel.add(outputEncodingLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(outputEncodingBox, c);
        innerPanel.add(outputEncodingBox);
    }

    private void initLangField() {
        defaultLangField = new JTextField();
        Utilities.initComponent(defaultLangField, Translator.getString("Lang"), PREFIX_BOX_WIDTH, URI_FIELD_HEIGHT);
        uiLangBox = new JComboBox(new Object[] { "en", "ja" });
        Utilities.initComponent(uiLangBox, "UI " + Translator.getString("Lang"), PREFIX_BOX_WIDTH, PREFIX_BOX_HEIGHT);
    }

    private void initEncodingBox() {
        inputEncodingLabel = new JLabel("Input Encoding:   ");
        Object[] encodingList = new Object[] { "JISAutoDetect", "SJIS", "EUC_JP", "ISO2022JP", "UTF-8", "UTF-16" };
        inputEncodingBoxModel = new DefaultComboBoxModel(encodingList);
        inputEncodingBox = new JComboBox(inputEncodingBoxModel);
        inputEncodingBox.setPreferredSize(new Dimension(PREFIX_BOX_WIDTH, 30));
        inputEncodingBox.setMinimumSize(new Dimension(PREFIX_BOX_WIDTH, 30));
        outputEncodingLabel = new JLabel("Output Encoding:   ");
        encodingList = new Object[] { "SJIS", "EUC_JP", "ISO2022JP", "UTF-8", "UTF-16" };
        outputEncodingBoxModel = new DefaultComboBoxModel(encodingList);
        outputEncodingBox = new JComboBox(outputEncodingBoxModel);
        outputEncodingBox.setPreferredSize(new Dimension(PREFIX_BOX_WIDTH, 30));
        outputEncodingBox.setMinimumSize(new Dimension(PREFIX_BOX_WIDTH, 30));
    }

    private static final int PREFIX_BOX_WIDTH = 120;

    private static final int PREFIX_BOX_HEIGHT = 50;

    private void initBaseURIField() {
        uriPrefixBox = new JComboBox();
        uriPrefixBox.addActionListener(new ChangePrefixAction());
        Utilities.initComponent(uriPrefixBox, Translator.getString("Prefix"), PREFIX_BOX_WIDTH, PREFIX_BOX_HEIGHT);
        baseURILabel = new JLabel("");
        Utilities.initComponent(baseURILabel, "BaseURI", URI_FIELD_WIDTH, URI_FIELD_HEIGHT);
        initPrefixBox();
    }

    public void initPrefixBox() {
        PrefixNSUtil.setPrefixNSInfoSet(gmanager.getPrefixNSInfoSet());
        uriPrefixBox.setModel(new DefaultComboBoxModel(PrefixNSUtil.getPrefixes().toArray()));
        setPrefix();
    }

    private void setPrefix() {
        for (Iterator i = gmanager.getPrefixNSInfoSet().iterator(); i.hasNext(); ) {
            PrefixNSInfo prefNSInfo = (PrefixNSInfo) i.next();
            if (prefNSInfo.getNameSpace().equals(gmanager.getBaseURI())) {
                uriPrefixBox.setSelectedItem(prefNSInfo.getPrefix());
                baseURILabel.setText(prefNSInfo.getNameSpace());
                break;
            }
        }
    }

    class ChangePrefixAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            PrefixNSUtil.replacePrefix((String) uriPrefixBox.getSelectedItem(), baseURILabel);
        }
    }

    private void initWorkDirectoryField() {
        workDirectoryField = new JTextField();
        workDirectoryField.setEditable(false);
        Utilities.initComponent(workDirectoryField, "Work Directory", URI_FIELD_WIDTH, URI_FIELD_HEIGHT);
        browseWorkDirectoryButton = new JButton("Browse");
        browseWorkDirectoryButton.addActionListener(new BrowseDirectory());
    }

    private void initProxyField() {
        isProxy = new JCheckBox("Proxy");
        isProxy.addActionListener(new CheckProxy());
        proxyHost = new JTextField();
        proxyHost.setPreferredSize(new Dimension(URI_FIELD_WIDTH, URI_FIELD_HEIGHT));
        proxyHost.setMinimumSize(new Dimension(URI_FIELD_WIDTH, URI_FIELD_HEIGHT));
        proxyHost.setBorder(BorderFactory.createTitledBorder("Host"));
        proxyPort = new JTextField(5);
        proxyPort.setPreferredSize(new Dimension(50, URI_FIELD_HEIGHT));
        proxyPort.setMinimumSize(new Dimension(50, URI_FIELD_HEIGHT));
        proxyPort.setBorder(BorderFactory.createTitledBorder("Port"));
    }

    private String getDirectoryName() {
        File currentDirectory = new File(userPrefs.get(PrefConstants.DefaultWorkDirectory, ""));
        JFileChooser jfc = new JFileChooser(currentDirectory);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle("Select Directory");
        int fd = jfc.showOpenDialog(this);
        if (fd == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile().toString();
        }
        return null;
    }

    class BrowseDirectory extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            String directoryName = getDirectoryName();
            if (directoryName != null) {
                workDirectoryField.setText(directoryName);
            }
        }
    }

    class CheckProxy extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            proxyHost.setEditable(isProxy.isSelected());
            proxyPort.setEditable(isProxy.isSelected());
        }
    }

    private void initParameter() {
        uiLangBox.setSelectedItem(userPrefs.get(PrefConstants.UILang, "en"));
        defaultLangField.setText(userPrefs.get(PrefConstants.DefaultLang, "ja"));
        inputEncodingBox.setSelectedItem(userPrefs.get(PrefConstants.InputEncoding, "SJIS"));
        outputEncodingBox.setSelectedItem(userPrefs.get(PrefConstants.OutputEncoding, "SJIS"));
        baseURILabel.setText(userPrefs.get(PrefConstants.BaseURI, MR3Resource.DefaultURI.getURI()));
        workDirectoryField.setText(userPrefs.get(PrefConstants.DefaultWorkDirectory, ""));
        isProxy.setSelected(userPrefs.getBoolean(PrefConstants.Proxy, false));
        proxyHost.setText(userPrefs.get(PrefConstants.ProxyHost, "http://localhost"));
        proxyHost.setEditable(isProxy.isSelected());
        proxyPort.setText(Integer.toString(userPrefs.getInt(PrefConstants.ProxyPort, 3128)));
        proxyPort.setEditable(isProxy.isSelected());
        String classClassListStr = userPrefs.get(PrefConstants.ClassClassList, GraphManager.CLASS_CLASS_LIST);
        String[] list = classClassListStr.split(" ");
        classClassListModel.clear();
        for (int i = 0; i < list.length; i++) {
            classClassListModel.addElement(list[i]);
        }
        String propClassListStr = userPrefs.get(PrefConstants.PropClassList, GraphManager.PROPERTY_CLASS_LIST);
        list = propClassListStr.split(" ");
        propClassListModel.clear();
        for (int i = 0; i < list.length; i++) {
            propClassListModel.addElement(list[i]);
        }
        rdfResourceColor = ChangeCellAttrUtil.rdfResourceColor;
        literalColor = ChangeCellAttrUtil.literalColor;
        classColor = ChangeCellAttrUtil.classColor;
        propertyColor = ChangeCellAttrUtil.propertyColor;
        selectedColor = ChangeCellAttrUtil.selectedColor;
        backgroundColor = Color.white;
        isColorBox.setSelected(userPrefs.getBoolean(PrefConstants.Color, true));
        isAntialiasBox.setSelected(userPrefs.getBoolean(PrefConstants.Antialias, true));
    }

    private String getMetaClassStr(Object[] list) {
        String metaClassListStr = "";
        for (int i = 0; i < list.length; i++) {
            metaClassListStr += list[i] + " ";
        }
        return metaClassListStr;
    }

    class DesideAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == applyButton) {
                try {
                    userPrefs.put(PrefConstants.UILang, (String) uiLangBox.getSelectedItem());
                    userPrefs.put(PrefConstants.DefaultLang, defaultLangField.getText());
                    userPrefs.put(PrefConstants.InputEncoding, (String) inputEncodingBox.getSelectedItem());
                    userPrefs.put(PrefConstants.OutputEncoding, (String) outputEncodingBox.getSelectedItem());
                    userPrefs.put(PrefConstants.BaseURI, baseURILabel.getText());
                    gmanager.setBaseURI(baseURILabel.getText());
                    userPrefs.put(PrefConstants.DefaultWorkDirectory, workDirectoryField.getText());
                    userPrefs.putBoolean(PrefConstants.Proxy, isProxy.isSelected());
                    userPrefs.put(PrefConstants.ProxyHost, proxyHost.getText());
                    userPrefs.putInt(PrefConstants.ProxyPort, Integer.parseInt(proxyPort.getText()));
                    userPrefs.put(PrefConstants.ClassClassList, getMetaClassStr(classClassListModel.toArray()));
                    userPrefs.put(PrefConstants.PropClassList, getMetaClassStr(propClassListModel.toArray()));
                    userPrefs.putInt(PrefConstants.RDFResourceColor, rdfResourceColor.getRGB());
                    ChangeCellAttrUtil.rdfResourceColor = rdfResourceColor;
                    userPrefs.putInt(PrefConstants.LiteralColor, literalColor.getRGB());
                    ChangeCellAttrUtil.literalColor = literalColor;
                    userPrefs.putInt(PrefConstants.ClassColor, classColor.getRGB());
                    ChangeCellAttrUtil.classColor = classColor;
                    userPrefs.putInt(PrefConstants.PropertyColor, propertyColor.getRGB());
                    ChangeCellAttrUtil.propertyColor = propertyColor;
                    userPrefs.putInt(PrefConstants.SelectedColor, selectedColor.getRGB());
                    ChangeCellAttrUtil.selectedColor = selectedColor;
                    userPrefs.putInt(PrefConstants.BackgroundColor, backgroundColor.getRGB());
                    gmanager.setGraphBackground(backgroundColor);
                    userPrefs.putBoolean(PrefConstants.Color, isColorBox.isSelected());
                    ChangeCellAttrUtil.isColor = isColorBox.isSelected();
                    ChangeCellAttrUtil.changeAllCellColor(gmanager);
                    userPrefs.putBoolean(PrefConstants.Antialias, isAntialiasBox.isSelected());
                    gmanager.setAntialias();
                } catch (NumberFormatException nfe) {
                    JOptionPane.showInternalMessageDialog(gmanager.getDesktop(), "Number Format Exception", "Warning", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            setVisible(false);
        }
    }

    public void setVisible(boolean flag) {
        super.setVisible(flag);
        if (flag) {
            initParameter();
        }
    }

    class ColorSwatch implements Icon {

        private String name;

        ColorSwatch(String str) {
            name = str;
        }

        public int getIconWidth() {
            return 11;
        }

        public int getIconHeight() {
            return 11;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.black);
            g.fillRect(x, y, getIconWidth(), getIconHeight());
            if (name.equals("Resource")) {
                g.setColor(rdfResourceColor);
            } else if (name.equals("Literal")) {
                g.setColor(literalColor);
            } else if (name.equals("Class")) {
                g.setColor(classColor);
            } else if (name.equals("Property")) {
                g.setColor(propertyColor);
            } else if (name.equals("Selected")) {
                g.setColor(selectedColor);
            } else if (name.equals("Background")) {
                g.setColor(backgroundColor);
            }
            g.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
        }
    }
}
