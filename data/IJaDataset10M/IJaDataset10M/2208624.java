package net.mjrz.fm.ui.dialogs.filter;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;

public class RelationCbModel extends DefaultComboBoxModel {

    private static final long serialVersionUID = 1L;

    private Object selectedItem = null;

    private ArrayList<String> relations = null;

    public RelationCbModel() {
        relations = new ArrayList<String>();
        addDefaultRelations();
        for (int i = 0; i < relations.size(); i++) {
            this.addElement(relations.get(i));
        }
    }

    private void addDefaultRelations() {
        relations.add("Equals");
        relations.add("In");
        relations.add("Not equals");
        relations.add("Greater than");
        relations.add("Lesser than");
    }

    private void addDateRelations() {
        relations.add("In");
        relations.add("Greater than");
        relations.add("Lesser than");
    }

    public void updateModel(String field) {
        relations.clear();
        removeAllElements();
        if (field.equals("Date")) {
            addDateRelations();
        } else {
            addDefaultRelations();
        }
        for (int i = 0; i < relations.size(); i++) {
            this.addElement(relations.get(i));
        }
    }
}
