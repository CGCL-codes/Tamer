package opt.base;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import freemarker.template.SimpleHash;
import powerXML.*;

public class SingleBaseOption extends AbstractSingleOption {

    private String b_toolTipText;

    private String b_optionText;

    private String b_optionKey;

    private boolean b_isWritable;

    @SuppressWarnings("unused")
    private int maxEntryLength;

    protected boolean b_isChanged;

    protected JLabel b_guiLabel;

    protected JComponent b_guiObject;

    public SingleBaseOption() {
        super();
        initialize("", "", "", "", 5);
    }

    public SingleBaseOption(String optionKey) {
        super();
        initialize(optionKey, "", "", "", 5);
    }

    public SingleBaseOption(String optionKey, String displayLabel) {
        super();
        initialize(optionKey, displayLabel, "", "", 5);
    }

    public SingleBaseOption(String optionKey, String displayLabel, String toolTip, String optText, int maxLen) {
        super();
        initialize(optionKey, displayLabel, toolTip, optText, maxLen);
    }

    private void initialize(String optKey, String dispLabel, String toolTip, String optText, int maxLen) {
        b_isChanged = false;
        setOptionKey(optKey);
        setDisplayLabel(dispLabel);
        setToolTip(toolTip);
        setOptionText(optText);
        setMaxEntryLength(maxLen);
        setIsWritable(true);
        createGUIObject();
    }

    public boolean getIsWritable() {
        return (b_isWritable);
    }

    public void setIsWritable(boolean isWritable) {
        b_isWritable = isWritable;
    }

    public boolean getIsChanged() {
        return (b_isChanged);
    }

    public void setMaxEntryLength(int maxLength) {
        maxEntryLength = maxLength;
    }

    public String getOptionKey() {
        return (b_optionKey);
    }

    @Override
    public void setOptionKey(String optionKey) {
        b_optionKey = optionKey;
    }

    public void setDisplayLabel(String displayLabel) {
        if (b_guiLabel == null) b_guiLabel = new JLabel(displayLabel); else b_guiLabel.setText(displayLabel);
    }

    public String getDisplayLabel() {
        return (b_guiLabel.getText());
    }

    public void setToolTip(String toolTip) {
        b_toolTipText = toolTip;
    }

    public String getToolTip() {
        return (b_toolTipText);
    }

    public void setGUIToolTipText() {
        if (b_guiObject != null && b_toolTipText != null && b_toolTipText.length() > 0) b_guiObject.setToolTipText(b_toolTipText);
    }

    public void setOptionText(String optionText) {
        b_optionText = optionText;
    }

    public String getOptionText() {
        return (b_optionText);
    }

    @Override
    public Object getOptionDefault() {
        return null;
    }

    @Override
    public Object getOptionValue() {
        return null;
    }

    @Override
    public void setOptionDefault(Object defaultValue) {
    }

    public void setOptionDefault(String defaultValue) {
    }

    public void setOptionDefault(int defaultValue) {
    }

    public void setOptionDefault(boolean defaultValue) {
    }

    public void setOptionDefault(Color defaultValue) {
    }

    @Override
    public void setOptionValue(Object optionValue) {
    }

    public void setOptionValue(String optionValue) {
    }

    public void setOptionValue(Color optionValue) {
    }

    public void setOptionValue(int optionValue) {
    }

    public void setOptionValue(boolean optionValue) {
    }

    public String getDatatype() {
        return ("string");
    }

    @Override
    public String toString() {
        return null;
    }

    public void createXMLObject(SimpleXML parentXML) {
        try {
            if (getIsWritable() == false) return;
            SimpleXML xObj = parentXML.addElement(getOptionKey());
            xObj.Attribute.add("DisplayLabel", getDisplayLabel());
            xObj.Attribute.add("Datatype", getDatatype().toLowerCase());
            xObj.Attribute.add("Value", toString());
            xObj.Attribute.add("ToolTip", getToolTip());
            xObj.Attribute.add("OptionText", getOptionText());
        } catch (Exception e) {
        }
    }

    public void createFMPPObject(SimpleHash parentHash) {
        SimpleHash xObj = new SimpleHash();
        parentHash.put(getOptionKey(), xObj);
        xObj.put("DisplayLabel", getDisplayLabel());
        xObj.put("Datatype", getDatatype().toLowerCase());
        xObj.put("Value", toString());
        xObj.put("ToolTip", getToolTip());
        xObj.put("OptionText", getOptionText());
        xObj.put("isWritable", getIsWritable());
    }

    public JLabel getGUILabel() {
        return (b_guiLabel);
    }

    protected void createGUIObject() {
        if (b_guiObject == null) b_guiObject = new JTextField();
        setGUIToolTipText();
    }

    protected void setGUIObjectData() {
        if (b_guiObject == null) return;
        ((JTextField) b_guiObject).setText(toString());
    }

    public JComponent getGUIObject() {
        if (b_guiObject == null) createGUIObject();
        return (b_guiObject);
    }

    protected void setIsChanged(boolean isChanged) {
        b_isChanged = isChanged;
    }

    public boolean applyChanges() {
        setOptionValue(((JTextField) b_guiObject).getText());
        return (b_isChanged);
    }

    public void revertChanges() {
        this.setGUIObjectData();
    }

    public void setBounds(int x, int y, int width, int height) {
        b_guiObject.setBounds(x, y, width, height);
    }

    public void setFont(Font myFont) {
        b_guiObject.setFont(myFont);
    }

    public int drawGUI(JPanel parentPanel, int labelX, int labelY, int labelWidth, int labelHeight, int xOffset, int yOffset, int objWidth, int objHeight, Font myFont) {
        int width = labelWidth;
        int height = objHeight;
        if (getDatatype().equals("directory") || getDatatype().equals("comment")) width += 100;
        if (getDatatype().equals("comment")) height += 32;
        JLabel curLabel = getGUILabel();
        curLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        curLabel.setFont(myFont);
        parentPanel.add(curLabel);
        height += getAdditionalHeight();
        width += getAdditionalWidth();
        setBounds(labelX + labelWidth + xOffset, labelY + yOffset, width, height);
        setFont(myFont);
        parentPanel.add(getGUIObject());
        if (getDatatype().equals("directory")) {
            JButton browseButton = ((SingleDirOption) this).getBrowseButton();
            browseButton.setBounds(labelX + labelWidth + xOffset + width + 4, labelY + yOffset, 22, objHeight);
            browseButton.setFont(myFont);
            parentPanel.add(browseButton);
        }
        return (height);
    }

    protected int getAdditionalHeight() {
        return (0);
    }

    protected int getAdditionalWidth() {
        return (0);
    }
}
