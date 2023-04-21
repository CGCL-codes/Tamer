package com.datas.bean.common;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.datas.bean.enums.MetaDataType;
import com.datas.bean.model.system.SysComboItems;
import com.datas.bean.model.system.SysField;
import com.datas.bean.model.system.SysFkitems;
import com.datas.bean.model.system.SysTableField;
import com.datas.component.AdvancedDateField;
import com.datas.component.AdvancedTextField;
import com.datas.component.AdvancedTimestampField;
import com.datas.component.ForeignTailObject;
import com.datas.component.PasswordField;
import com.datas.component.ThreeStateCheckBox;
import com.datas.component.panel.ImagePanel;

/**
 * @author kimi
 * 
 */
public class MetaData {

    /** fields data */
    private FieldSetup fieldSetup;

    private MetaDataType type;

    /** */
    private ForeignTailObject foreignTailObject;

    private SortedMap<String, SysComboItems> comboItems;

    private SortedMap<String, SysFkitems> foreignKeyItems;

    /** components */
    private JLabel label;

    private AdvancedTextField advancedTextField;

    private AdvancedDateField advancedDateField;

    private AdvancedTimestampField advancedTimestampField;

    private PasswordField passwordField;

    private JComboBox comboBox;

    private JScrollPane scrollPane;

    private JTextArea textArea;

    private ThreeStateCheckBox checkBox;

    private ImagePanel imagePanel;

    public MetaData() {
        fieldSetup = new FieldSetup();
    }

    public MetaData(SysField sysField, MetaDataType type) {
        fieldSetup = new FieldSetup();
        setFieldSetup(sysField, type);
    }

    public MetaData(SysTableField sysTableField, MetaDataType type) {
        fieldSetup = new FieldSetup();
        setFieldSetup(sysTableField, type);
    }

    public void setFieldSetup(SysField sysField, MetaDataType type) {
        fieldSetup.setIdField(sysField.getIdField());
        fieldSetup.setIdModul(sysField.getIdModul());
        fieldSetup.setOrdinalPosition(sysField.getOrdinalPosition());
        fieldSetup.setClassName(sysField.getClassName());
        fieldSetup.setType(sysField.getType());
        fieldSetup.setXPosition(sysField.getxPosition());
        fieldSetup.setYPosition(sysField.getyPosition());
        fieldSetup.setWidth(sysField.getWidth());
        fieldSetup.setHeight(sysField.getHeight());
        fieldSetup.setMandatory(sysField.getMandatory());
        fieldSetup.setUniqueKey(sysField.getUniqueKey());
        fieldSetup.setPrimaryKey(sysField.getPrimaryKey());
        fieldSetup.setForeignKey(sysField.getForeignKey());
        fieldSetup.setLength(sysField.getLength());
        fieldSetup.setVisible(sysField.getVisible());
        fieldSetup.setVisibleInTable(sysField.getVisibleInTable());
        fieldSetup.setCaption(sysField.getCaption());
        fieldSetup.setCaptionXPosition(sysField.getCaptionXPosition());
        fieldSetup.setCaptionYPosition(sysField.getCaptionYPosition());
        fieldSetup.setCaptionWidth(sysField.getCaptionWidth());
        fieldSetup.setCaptionHeight(sysField.getCaptionHeight());
        fieldSetup.setBean(sysField.getBean());
        fieldSetup.setProperty(sysField.getProperty());
        fieldSetup.setIdTabpane(sysField.getIdTabpane());
        fieldSetup.setNumTabpane(sysField.getNumTabpane());
        fieldSetup.setDefaultValue(sysField.getDefaultValue());
        fieldSetup.setHelp(sysField.getHelp());
        fieldSetup.setPrecision(sysField.getPrecision());
        fieldSetup.setEditable(sysField.getEditable());
        fieldSetup.setFocusable(sysField.getFocusable());
        fieldSetup.setFkBean(sysField.getFkBean());
        fieldSetup.setFkFrameWidth(sysField.getFkFrameWidth());
        fieldSetup.setFkFrameHeight(sysField.getFkFrameHeight());
        this.type = type;
    }

    public void setFieldSetup(SysTableField sysTableField, MetaDataType type) {
        fieldSetup.setIdField(sysTableField.getIdField());
        fieldSetup.setIdModul(sysTableField.getIdModul());
        fieldSetup.setIdTable(sysTableField.getIdTable());
        fieldSetup.setOrdinalPosition(sysTableField.getOrdinalPosition());
        fieldSetup.setClassName(sysTableField.getClassName());
        fieldSetup.setType(sysTableField.getType());
        fieldSetup.setXPosition(sysTableField.getxPosition());
        fieldSetup.setYPosition(sysTableField.getyPosition());
        fieldSetup.setWidth(sysTableField.getWidth());
        fieldSetup.setHeight(sysTableField.getHeight());
        fieldSetup.setMandatory(sysTableField.getMandatory());
        fieldSetup.setUniqueKey(sysTableField.getUniqueKey());
        fieldSetup.setPrimaryKey(sysTableField.getPrimaryKey());
        fieldSetup.setForeignKey(sysTableField.getForeignKey());
        fieldSetup.setLength(sysTableField.getLength());
        fieldSetup.setVisible(sysTableField.getVisible());
        fieldSetup.setVisibleInTable(sysTableField.getVisibleInTable());
        fieldSetup.setCaption(sysTableField.getCaption());
        fieldSetup.setCaptionXPosition(sysTableField.getCaptionXPosition());
        fieldSetup.setCaptionYPosition(sysTableField.getCaptionYPosition());
        fieldSetup.setCaptionWidth(sysTableField.getCaptionWidth());
        fieldSetup.setCaptionHeight(sysTableField.getCaptionHeight());
        fieldSetup.setBean(sysTableField.getBean());
        fieldSetup.setProperty(sysTableField.getProperty());
        fieldSetup.setIdTabpane(sysTableField.getIdTabpane());
        fieldSetup.setNumTabpane(sysTableField.getNumTabpane());
        fieldSetup.setDefaultValue(sysTableField.getDefaultValue());
        fieldSetup.setHelp(sysTableField.getHelp());
        fieldSetup.setPrecision(sysTableField.getPrecision());
        fieldSetup.setEditable(sysTableField.getEditable());
        fieldSetup.setFocusable(sysTableField.getFocusable());
        fieldSetup.setFkBean(sysTableField.getFkBean());
        fieldSetup.setFkFrameWidth(sysTableField.getFkFrameWidth());
        fieldSetup.setFkFrameHeight(sysTableField.getFkFrameHeight());
        this.type = type;
    }

    public SysField getSysField() {
        SysField sysField = new SysField();
        sysField.setIdField(fieldSetup.getIdField());
        sysField.setIdModul(fieldSetup.getIdModul());
        sysField.setOrdinalPosition(fieldSetup.getOrdinalPosition());
        sysField.setClassName(fieldSetup.getClassName());
        sysField.setType(fieldSetup.getType());
        sysField.setxPosition(fieldSetup.getXPosition());
        sysField.setyPosition(fieldSetup.getYPosition());
        sysField.setWidth(fieldSetup.getWidth());
        sysField.setHeight(fieldSetup.getHeight());
        sysField.setMandatory(fieldSetup.getMandatory());
        sysField.setUniqueKey(fieldSetup.getUniqueKey());
        sysField.setPrimaryKey(fieldSetup.getPrimaryKey());
        sysField.setForeignKey(fieldSetup.getForeignKey());
        sysField.setLength(fieldSetup.getLength());
        sysField.setVisible(fieldSetup.getVisible());
        sysField.setVisibleInTable(fieldSetup.getVisibleInTable());
        sysField.setCaption(fieldSetup.getCaption());
        sysField.setCaptionXPosition(fieldSetup.getCaptionXPosition());
        sysField.setCaptionYPosition(fieldSetup.getCaptionYPosition());
        sysField.setCaptionWidth(fieldSetup.getCaptionWidth());
        sysField.setCaptionHeight(fieldSetup.getCaptionHeight());
        sysField.setBean(fieldSetup.getBean());
        sysField.setProperty(fieldSetup.getProperty());
        sysField.setIdTabpane(fieldSetup.getIdTabpane());
        sysField.setNumTabpane(fieldSetup.getNumTabpane());
        sysField.setDefaultValue(fieldSetup.getDefaultValue());
        sysField.setHelp(fieldSetup.getHelp());
        sysField.setPrecision(fieldSetup.getPrecision());
        sysField.setEditable(fieldSetup.getEditable());
        sysField.setFocusable(fieldSetup.getFocusable());
        sysField.setFkBean(fieldSetup.getFkBean());
        sysField.setFkFrameWidth(fieldSetup.getFkFrameWidth());
        sysField.setFkFrameHeight(fieldSetup.getFkFrameHeight());
        return sysField;
    }

    public void addComboItems(List<SysComboItems> items) {
        comboItems = new TreeMap<String, SysComboItems>();
        for (SysComboItems item : items) {
            comboItems.put(item.getIdItem(), item);
            comboBox.addItem(item.getName());
        }
    }

    /** getters & setters */
    public FieldSetup getFieldSetup() {
        return fieldSetup;
    }

    public MetaDataType getType() {
        return type;
    }

    public void setType(MetaDataType type) {
        this.type = type;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public AdvancedTextField getAdvancedTextField() {
        return advancedTextField;
    }

    public void setAdvancedTextField(AdvancedTextField advancedTextField) {
        this.advancedTextField = advancedTextField;
    }

    public AdvancedDateField getAdvancedDateField() {
        return advancedDateField;
    }

    public void setAdvancedDateField(AdvancedDateField advancedDateField) {
        this.advancedDateField = advancedDateField;
    }

    public AdvancedTimestampField getAdvancedTimestampField() {
        return advancedTimestampField;
    }

    public void setAdvancedTimestampField(AdvancedTimestampField advancedTimestampField) {
        this.advancedTimestampField = advancedTimestampField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(PasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public JComboBox getComboBox() {
        return comboBox;
    }

    public void setComboBox(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public ThreeStateCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(ThreeStateCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    public void setImagePanel(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    public ForeignTailObject getForeignTailObject() {
        return foreignTailObject;
    }

    public void setForeignTailObject(ForeignTailObject foreignTailObject) {
        this.foreignTailObject = foreignTailObject;
    }

    public SortedMap<String, SysComboItems> getComboItems() {
        return comboItems;
    }

    public void setComboItems(SortedMap<String, SysComboItems> comboItems) {
        this.comboItems = comboItems;
    }

    public SortedMap<String, SysFkitems> getForeignKeyItems() {
        return foreignKeyItems;
    }

    public void setForeignKeyItems(SortedMap<String, SysFkitems> foreignKeyItems) {
        this.foreignKeyItems = foreignKeyItems;
    }
}
