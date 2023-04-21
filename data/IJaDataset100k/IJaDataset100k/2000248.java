package com.jguigen.standard;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.accessibility.AccessibleContext;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataEvent;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class JxComboBox extends JComboBox {

    public JxComboBox() {
        this.setUI(new NewBasicComboBoxUI());
    }

    public JxComboBox(ComboBoxModel aModel) {
        super(aModel);
    }

    public JxComboBox(java.util.Vector items) {
        super(items);
    }

    public JxComboBox(Object[] items) {
        super(items);
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);
    }

    public void addActionListener(java.awt.event.ActionListener l) {
        super.addActionListener(l);
    }

    public void addItem(java.lang.Object anObject) {
        super.addItem(anObject);
    }

    public void addItemListener(java.awt.event.ItemListener aListener) {
        super.addItemListener(aListener);
    }

    public void configureEditor(ComboBoxEditor anEditor, java.lang.Object anItem) {
        super.configureEditor(anEditor, anItem);
    }

    public void contentsChanged(ListDataEvent e) {
        super.contentsChanged(e);
    }

    protected JComboBox.KeySelectionManager createDefaultKeySelectionManager() {
        return super.createDefaultKeySelectionManager();
    }

    protected void fireActionEvent() {
        super.fireActionEvent();
    }

    protected void fireItemStateChanged(java.awt.event.ItemEvent e) {
        super.fireItemStateChanged(e);
    }

    public AccessibleContext getAccessibleContext() {
        return super.getAccessibleContext();
    }

    public java.lang.String getActionCommand() {
        return super.getActionCommand();
    }

    public ComboBoxEditor getEditor() {
        return super.getEditor();
    }

    public java.lang.Object getItemAt(int index) {
        return super.getItemAt(index);
    }

    public int getItemCount() {
        return super.getItemCount();
    }

    public JComboBox.KeySelectionManager getKeySelectionManager() {
        return super.getKeySelectionManager();
    }

    public int getMaximumRowCount() {
        return super.getMaximumRowCount();
    }

    public ComboBoxModel getModel() {
        return super.getModel();
    }

    public ListCellRenderer getRenderer() {
        return super.getRenderer();
    }

    public int getSelectedIndex() {
        return super.getSelectedIndex();
    }

    public java.lang.Object getSelectedItem() {
        return super.getSelectedItem();
    }

    public java.lang.Object[] getSelectedObjects() {
        return super.getSelectedObjects();
    }

    public ComboBoxUI getUI() {
        return super.getUI();
    }

    public java.lang.String getUIClassID() {
        return super.getUIClassID();
    }

    public void hidePopup() {
        super.hidePopup();
    }

    public void insertItemAt(java.lang.Object anObject, int index) {
        super.insertItemAt(anObject, index);
    }

    protected void installAncestorListener() {
        super.installAncestorListener();
    }

    public void intervalAdded(ListDataEvent e) {
        super.intervalAdded(e);
    }

    public void intervalRemoved(ListDataEvent e) {
        super.intervalRemoved(e);
    }

    public boolean isEditable() {
        return super.isEditable();
    }

    public boolean isLightWeightPopupEnabled() {
        return super.isLightWeightPopupEnabled();
    }

    public boolean isPopupVisible() {
        return super.isPopupVisible();
    }

    protected java.lang.String paramString() {
        return super.paramString();
    }

    public void processKeyEvent(java.awt.event.KeyEvent e) {
        super.processKeyEvent(e);
    }

    public void removeActionListener(java.awt.event.ActionListener l) {
        super.removeActionListener(l);
    }

    public void removeAllItems() {
        super.removeAllItems();
    }

    public void removeItem(java.lang.Object anObject) {
        super.removeItem(anObject);
    }

    public void removeItemAt(int anIndex) {
        super.removeItemAt(anIndex);
    }

    public void removeItemListener(java.awt.event.ItemListener aListener) {
        super.removeItemListener(aListener);
    }

    protected void selectedItemChanged() {
        super.selectedItemChanged();
    }

    public boolean selectWithKeyChar(char keyChar) {
        return super.selectWithKeyChar(keyChar);
    }

    public void setActionCommand(java.lang.String aCommand) {
        super.setActionCommand(aCommand);
    }

    public void setEditable(boolean aFlag) {
        super.setEditable(aFlag);
    }

    public void setEditor(ComboBoxEditor anEditor) {
        super.setEditor(anEditor);
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
    }

    public void setKeySelectionManager(JComboBox.KeySelectionManager aManager) {
        super.setKeySelectionManager(aManager);
    }

    public void setLightWeightPopupEnabled(boolean aFlag) {
        super.setLightWeightPopupEnabled(aFlag);
    }

    public void setMaximumRowCount(int count) {
        super.setMaximumRowCount(count);
    }

    public void setModel(ComboBoxModel aModel) {
        super.setModel(aModel);
    }

    public void setPopupVisible(boolean v) {
        super.setPopupVisible(v);
    }

    public void setRenderer(ListCellRenderer aRenderer) {
        super.setRenderer(aRenderer);
    }

    public void setSelectedIndex(int anIndex) {
        super.setSelectedIndex(anIndex);
    }

    public void setSelectedItem(java.lang.Object anObject) {
        super.setSelectedItem(anObject);
    }

    public void setUI(javax.swing.plaf.ComboBoxUI ui) {
        super.setUI(ui);
    }

    public void showPopup() {
        super.showPopup();
    }

    public void updateUI() {
        super.updateUI();
    }

    class NewBasicComboBoxUI extends BasicComboBoxUI {

        FocusListener prevFocusListener;

        public NewBasicComboBoxUI() {
            super();
            prevFocusListener = focusListener;
        }

        protected FocusListener createFocusListener() {
            System.out.println("PLEASE change the focus Listener");
            prevFocusListener = super.createFocusListener();
            return new MyFocusHandler();
        }

        public class MyFocusHandler implements FocusListener {

            public void focusLost(FocusEvent parm1) {
                System.out.println("LOST" + parm1);
                prevFocusListener.focusLost(parm1);
            }

            public void focusGained(FocusEvent parm1) {
                System.out.println("Yupppeeeeeeeeeee" + parm1);
                prevFocusListener.focusGained(parm1);
            }
        }
    }
}
