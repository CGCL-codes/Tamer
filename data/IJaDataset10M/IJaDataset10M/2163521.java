package com.impact.xbm.client.data;

import com.impact.qwicgui.event.QDataModifiedEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.w3c.dom.Node;
import com.impact.qwicgui.event.QDataModifiedListener;
import java.awt.AWTEvent;

public abstract class BaseDataItem implements ActionListener, QDataModifiedListener {

    protected static String LINE_SEPARATOR = System.getProperty("line.separator");

    private static String INDENT = "  ";

    private static int NAME_COMPONENT_WIDTH = 125;

    private static int VALUE_COMPONENT_WIDTH = 550;

    private static int SPACER_WIDTH = 10;

    private static int PANEL_WIDTH = NAME_COMPONENT_WIDTH + VALUE_COMPONENT_WIDTH + SPACER_WIDTH;

    protected JPanel _jpanel;

    protected Font _plainFont;

    protected Font _boldFont;

    protected Font _bigBoldFont;

    private boolean _panelInitialized;

    private boolean _panelLabelVisible;

    private int _displayIndentLevel;

    private String _descriptor;

    private ArrayList<QDataModifiedListener> _dataModifiedListeners = new ArrayList<QDataModifiedListener>();

    protected BaseDataItem() {
        _panelInitialized = false;
        _panelLabelVisible = true;
        _displayIndentLevel = 0;
        _descriptor = getDefaultDescriptor();
    }

    public BaseDataItem(Node node) {
        this();
        this.populateFromNode(node);
    }

    public abstract void populateFromNode(Node node);

    public abstract String toXmlString(int indentLevel);

    protected abstract String getDefaultDescriptor();

    public String getDescriptor() {
        return _descriptor;
    }

    public void setDescriptor(String descriptor) {
        _descriptor = descriptor;
    }

    public JPanel getJPanel() {
        return getJPanel(0);
    }

    public JPanel getJPanel(int displayIndentLevel) {
        if (!_panelInitialized) {
            initializeJPanel(displayIndentLevel);
            if (_displayIndentLevel > 0) {
                Component strut = Box.createHorizontalStrut(SPACER_WIDTH * _displayIndentLevel);
                _jpanel.add(strut);
            }
            if (hasLabel() && _panelLabelVisible) {
                addPanelLabel(getLabelText());
            }
            setupPanelContents();
            _panelInitialized = true;
        }
        return _jpanel;
    }

    public void setPanelLabelVisible(boolean isVisible) {
        _panelLabelVisible = isVisible;
    }

    public boolean hasLabel() {
        String label = getLabelText();
        return ((label != null) && (label.length() > 0));
    }

    public String getLabelText() {
        return null;
    }

    protected abstract void setupPanelContents();

    public void addPanelLabel(String header) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(header);
        Dimension size = label.getPreferredSize();
        size.width = PANEL_WIDTH;
        label.setFont(_boldFont);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        _jpanel.add(panel);
    }

    protected void addToPanel(JComponent component) {
        _jpanel.add(component);
    }

    protected void addToPanel(BaseDataItem dataItem) {
        if (dataItem != null) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
            panel.add(dataItem.getJPanel(_displayIndentLevel + 1));
            addToPanel(panel);
        }
    }

    protected void addToPanel(ArrayList dataItemList) {
        if (dataItemList != null) {
            for (Object dataItem : dataItemList) {
                addToPanel((BaseDataItem) dataItem);
            }
        }
    }

    private void initializeJPanel(int displayIndentLevel) {
        _jpanel = new JPanel();
        _jpanel.setLayout(new BoxLayout(_jpanel, BoxLayout.Y_AXIS));
        _jpanel.setBorder(new javax.swing.border.EtchedBorder());
        Font currentFont = _jpanel.getFont();
        String fontFamily = currentFont.getFamily();
        int fontSize = currentFont.getSize();
        _plainFont = new Font(fontFamily, Font.PLAIN, fontSize);
        _boldFont = new Font(fontFamily, Font.BOLD, fontSize);
        _bigBoldFont = new Font(fontFamily, Font.BOLD, fontSize + 4);
        _displayIndentLevel = displayIndentLevel;
    }

    public String toXmlString() {
        return toXmlString(0);
    }

    public void actionPerformed(ActionEvent event) {
    }

    public void keyPressed(KeyEvent event) {
    }

    public void keyReleased(KeyEvent event) {
    }

    public void keyTyped(KeyEvent event) {
    }

    protected String getKeyEventFieldValue(KeyEvent event, String fieldName) {
        String value = null;
        JTextField textField = (JTextField) event.getSource();
        String name = textField.getName();
        if (name.equals(fieldName)) {
            value = textField.getText();
        }
        return value;
    }

    public double getKeyEventFieldValue(KeyEvent event, String fieldName, double currentFieldValue) {
        double value = currentFieldValue;
        try {
            value = Double.valueOf(getKeyEventFieldValue(event, fieldName));
        } catch (Exception e) {
        }
        return value;
    }

    protected String getIndentString(int indentLevel) {
        String str = "";
        for (int i = 0; i < indentLevel; i++) {
            str += INDENT;
        }
        return str;
    }

    private static Color DEFAULT_HEADER_FG_COLOR = Color.black;

    private static Color DEFAULT_HEADER_BG_COLOR = new Color(200, 200, 200);

    protected JPanel getHeaderJPanel(String header) {
        return getHeaderJPanel(header, DEFAULT_HEADER_FG_COLOR, DEFAULT_HEADER_BG_COLOR, _boldFont);
    }

    protected JPanel getHeaderJPanel(String header, Color foregroundColor, Color backgroundColor, Font font) {
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel(header);
        headerLabel.setForeground(foregroundColor);
        headerLabel.setFont(font);
        headerPanel.setBackground(backgroundColor);
        headerPanel.add(headerLabel);
        return headerPanel;
    }

    protected JPanel getLabelJPanel(String label) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(label));
        return panel;
    }

    protected JPanel getDisplayFieldJPanel(String name, String value) {
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(_plainFont);
        valueLabel.setHorizontalAlignment(JLabel.LEFT);
        return getFieldJPanelHelper(getFieldJLabel(name), valueLabel);
    }

    protected JPanel getEditableFieldJPanel(String name, String value, DataItemKeyListener listener) {
        JTextField textField = new JTextField(value);
        return getEditableFieldJPanelHelper(name, textField, listener);
    }

    protected JPanel getEditableFieldJPanel(String name, String value, int numColumns, DataItemKeyListener listener) {
        JTextField textField = new JTextField(value, numColumns);
        return getEditableFieldJPanelHelper(name, textField, listener);
    }

    private JPanel getEditableFieldJPanelHelper(String name, JTextField textField, DataItemKeyListener listener) {
        textField.setName(name);
        if (listener != null) {
            textField.addKeyListener(listener);
        }
        return getFieldJPanelHelper(getFieldJLabel(name), textField);
    }

    protected JPanel getSelectionFieldJPanel(String name, String selectedValue, String... options) {
        JPanel panel = new JPanel();
        return panel;
    }

    protected JPanel getRadioButtonFieldJPanel(String name, String selectedValue, String... options) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup buttonGroup = new ButtonGroup();
        for (String option : options) {
            JRadioButton button = new JRadioButton(option);
            button.setSelected(selectedValue.equals(option));
            button.setActionCommand(name);
            button.addActionListener(this);
            buttonGroup.add(button);
            panel.add(button);
        }
        return getFieldJPanelHelper(getFieldJLabel(name), panel);
    }

    protected JPanel getCheckboxFieldJPanel(String name, boolean selected) {
        JCheckBox checkbox = new JCheckBox();
        checkbox.setSelected(selected);
        return getFieldJPanelHelper(getFieldJLabel(name), checkbox);
    }

    private JLabel getFieldJLabel(String name) {
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(_boldFont);
        nameLabel.setHorizontalAlignment(JLabel.RIGHT);
        return nameLabel;
    }

    private JPanel getFieldJPanelHelper(JComponent nameComponent, JComponent valueComponent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        Dimension size = nameComponent.getPreferredSize();
        size.width = NAME_COMPONENT_WIDTH;
        nameComponent.setMinimumSize(size);
        nameComponent.setMaximumSize(size);
        nameComponent.setPreferredSize(size);
        panel.add(nameComponent);
        panel.add(Box.createHorizontalStrut(SPACER_WIDTH));
        size = valueComponent.getPreferredSize();
        size.width = VALUE_COMPONENT_WIDTH - (SPACER_WIDTH * _displayIndentLevel);
        valueComponent.setMinimumSize(size);
        valueComponent.setMaximumSize(size);
        valueComponent.setPreferredSize(size);
        panel.add(valueComponent);
        return panel;
    }

    public void addDataModifiedListener(QDataModifiedListener listener) {
        _dataModifiedListeners.add(listener);
    }

    public void removeDataModifiedListener(QDataModifiedListener listener) {
        _dataModifiedListeners.remove(listener);
    }

    public void dataModified(QDataModifiedEvent event) {
        invokeDataModifiedListeners();
    }

    protected void dataModified() {
        invokeDataModifiedListeners();
    }

    private void invokeDataModifiedListeners() {
        QDataModifiedEvent event = new QDataModifiedEvent(this);
        for (int i = 0; i < _dataModifiedListeners.size(); i++) {
            _dataModifiedListeners.get(i).dataModified(event);
        }
    }

    protected int modifiedIntegerTextFieldValue(AWTEvent event) {
        JTextField textField = (JTextField) event.getSource();
        int value = 0;
        try {
            value = Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
        }
        this.dataModified();
        return value;
    }

    protected double modifiedDoubleTextFieldValue(AWTEvent event) {
        JTextField textField = (JTextField) event.getSource();
        double value = 0;
        try {
            value = Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
        }
        this.dataModified();
        return value;
    }

    public void keyPressed(String dataItemIdentifier, KeyEvent event) {
    }

    public void keyReleased(String dataItemIdentifier, KeyEvent event) {
    }

    public void keyTyped(String dataItemIdentifier, KeyEvent event) {
    }

    public class DataItemKeyListener implements KeyListener {

        BaseDataItem _dataItem;

        private String _id;

        private DataItemKeyListener() {
        }

        public DataItemKeyListener(BaseDataItem dataItem, String id) {
            _dataItem = dataItem;
            _id = id;
        }

        public void keyPressed(KeyEvent event) {
            _dataItem.keyPressed(_id, event);
        }

        public void keyReleased(KeyEvent event) {
            _dataItem.keyReleased(_id, event);
        }

        public void keyTyped(KeyEvent event) {
            _dataItem.keyTyped(_id, event);
        }
    }
}
