package com.mmbreakfast.unlod.app;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionListener;
import org.gamenet.application.mm8leveleditor.lod.LodEntry;
import org.gamenet.swing.controls.AbstractStringListCellRenderer;
import com.mmbreakfast.unlod.lod.LodFile;

public class LodFileList extends JList {

    protected LodEntry[] entries = new LodEntry[0];

    protected LodFileListModel listModel = new LodFileListModel();

    protected LodFile lodFile = null;

    public interface LodEntryComparator extends Comparator {

        public String getDisplayName();
    }

    LodEntryComparator nameLodEntryComparator = new LodEntryComparator() {

        public int compare(Object o1, Object o2) {
            LodEntry le1 = (LodEntry) o1;
            LodEntry le2 = (LodEntry) o2;
            return le1.getName().compareTo(le2.getName());
        }

        public String getDisplayName() {
            return "filename (case-sensitive)";
        }
    };

    LodEntryComparator fileTypeLodEntryComparator = new LodEntryComparator() {

        public int compare(Object o1, Object o2) {
            LodEntry le1 = (LodEntry) o1;
            LodEntry le2 = (LodEntry) o2;
            return le1.getResourceType().compareTo(le2.getResourceType());
        }

        public String getDisplayName() {
            return "file type";
        }
    };

    LodEntryComparator nameIgnoreCaseLodEntryComparator = new LodEntryComparator() {

        public int compare(Object o1, Object o2) {
            LodEntry le1 = (LodEntry) o1;
            LodEntry le2 = (LodEntry) o2;
            return le1.getName().compareToIgnoreCase(le2.getName());
        }

        public String getDisplayName() {
            return "filename";
        }
    };

    LodEntryComparator entryOffsetLodEntryComparator = new LodEntryComparator() {

        public int compare(Object o1, Object o2) {
            LodEntry le1 = (LodEntry) o1;
            LodEntry le2 = (LodEntry) o2;
            return le1.getEntryOffset() < le2.getEntryOffset() ? -1 : 1;
        }

        public String getDisplayName() {
            return "entry offset";
        }
    };

    LodEntryComparator dataOffsetLodEntryComparator = new LodEntryComparator() {

        public int compare(Object o1, Object o2) {
            LodEntry le1 = (LodEntry) o1;
            LodEntry le2 = (LodEntry) o2;
            return le1.getDataOffset() < le2.getDataOffset() ? -1 : 1;
        }

        public String getDisplayName() {
            return "data offset";
        }
    };

    public LodEntryComparator lodEntryComparatorArray[] = { nameLodEntryComparator, nameIgnoreCaseLodEntryComparator, fileTypeLodEntryComparator, entryOffsetLodEntryComparator, dataOffsetLodEntryComparator };

    public LodEntryComparator lastSelectedLodEntryComparator = dataOffsetLodEntryComparator;

    public LodFileList(ListSelectionListener listener) {
        this.addListSelectionListener(listener);
        this.setModel(listModel);
        ListCellRenderer itemLabelRenderer = new AbstractStringListCellRenderer() {

            protected String getStringForValue(Object value) {
                return ((LodEntry) value).getName();
            }
        };
        this.setCellRenderer(itemLabelRenderer);
    }

    public LodEntry[] getSelectedEntries() {
        Object[] sel = this.getSelectedValues();
        LodEntry[] lodEntries = new LodEntry[sel.length];
        System.arraycopy(sel, 0, lodEntries, 0, sel.length);
        return lodEntries;
    }

    public LodEntry getSelectedEntry() {
        return (LodEntry) this.getSelectedValue();
    }

    public void setLodFile(LodFile lodFile) {
        this.lodFile = lodFile;
        listModel.setLodFileEntries(lodFile.getLodEntries());
    }

    public Map getAllEntries() {
        return lodFile.getLodEntries();
    }

    public void changeLodEntryComparator(LodEntryComparator aComparator) {
        listModel.changeLodEntryComparator(aComparator);
    }

    protected class LodFileListModel extends AbstractListModel {

        public void setLodFileEntries(Map entries) {
            LodFileList.this.entries = (LodEntry[]) entries.values().toArray(new LodEntry[0]);
            Arrays.sort(LodFileList.this.entries, lastSelectedLodEntryComparator);
            this.fireContentsChanged(this, 0, LodFileList.this.entries.length - 1);
        }

        public void changeLodEntryComparator(LodEntryComparator newLodEntryComparator) {
            lastSelectedLodEntryComparator = newLodEntryComparator;
            Arrays.sort(entries, lastSelectedLodEntryComparator);
            fireContentsChanged(this, 0, entries.length - 1);
        }

        public Object getElementAt(int index) {
            return entries[index];
        }

        public int getSize() {
            return entries.length;
        }
    }
}
