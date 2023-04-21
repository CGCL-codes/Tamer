package org.maveryx.server.guiObject;

import java.awt.Component;
import org.jdom.Element;
import abbot.tester.JComboBoxTester;

public class AUTComboBox extends AUTObject {

    private JComboBoxTester comboBox = new JComboBoxTester();

    public Object selectItem(Component c, String[] item) {
        Object obj = null;
        comboBox.actionSelectItem(c, item[0]);
        return obj;
    }

    public Object selectIndex(Component c, String[] index) {
        Object obj = null;
        int indice = Integer.valueOf(index[0]);
        comboBox.actionSelectIndex(c, indice);
        return obj;
    }

    public String[] getItems(Element e) {
        return null;
    }

    public String[] getItemAt(Element e, String[] index) {
        return null;
    }

    public Integer getItemCount(Element e) {
        return null;
    }

    public String getSelectedItem(Element e) {
        return null;
    }

    public Boolean isEditable(Element e) {
        String state = e.getAttributeValue("editable", "accessibleComponent");
        return state.matches("true");
    }

    public Boolean contains(Element e, String[] item) {
        return true;
    }
}
