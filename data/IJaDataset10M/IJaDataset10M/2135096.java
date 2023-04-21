package se.kth.cid.conzilla.metadata;

import se.kth.cid.component.MetaData;
import se.kth.cid.component.MetaDataUtils;
import se.kth.cid.conzilla.util.*;
import se.kth.cid.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LanguageList extends AbstractListDisplayer implements MetaDataFieldEditor {

    Vector languageBoxes;

    class GridModel extends ListGridTableModel {

        public GridModel() {
        }

        public int getRowCount() {
            return languageBoxes.size();
        }

        public int getColumnCount() {
            return 1;
        }

        public Object getValueAt(int row, int col) {
            return languageBoxes.get(row);
        }

        public String getTitle(int col) {
            return null;
        }
    }

    public LanguageList(String[] langs, boolean editable, MetaDataEditListener editListener, String metaDataField) {
        super(editable, editListener, metaDataField);
        languageBoxes = new Vector();
        if (langs == null) langs = new String[0];
        for (int i = 0; i < langs.length; i++) addItem(langs[i], i);
        setModel(new GridModel());
    }

    public String[] getLanguages(boolean resetEdited) {
        if (resetEdited) edited = false;
        if (languageBoxes.size() == 0) return null;
        String[] strings = new String[languageBoxes.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = ((LanguageBox) languageBoxes.get(i)).getLanguage(resetEdited);
        }
        return strings;
    }

    protected boolean isItemEdited() {
        for (int i = 0; i < languageBoxes.size(); i++) {
            if (((LanguageBox) languageBoxes.get(i)).isEdited()) return true;
        }
        return false;
    }

    protected void removeItemImpl(int index) {
        removeAndDetach(languageBoxes, index);
    }

    protected void createItemImpl(int index) {
        addItem("", index);
    }

    void addItem(String string, int index) {
        LanguageBox box = new LanguageBox(string, editable, editListener, metaDataField);
        languageBoxes.add(index, box);
    }
}
