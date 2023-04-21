package cx.fbn.nevernote.gui;

import java.util.ArrayList;
import com.evernote.edam.type.Note;
import cx.fbn.nevernote.filters.ContainsAttributeFilter;
import cx.fbn.nevernote.filters.ContainsAttributeFilterFactory;
import cx.fbn.nevernote.sql.NoteTable;

public class ContainsAttributeFilterTable {

    ArrayList<ContainsAttributeFilter> table;

    public ContainsAttributeFilterTable() {
        table = new ArrayList<ContainsAttributeFilter>();
        for (ContainsAttributeFilterFactory.Contains type : ContainsAttributeFilterFactory.Contains.values()) table.add(ContainsAttributeFilterFactory.create(type));
    }

    public void reset() {
        for (int i = 0; i < table.size(); i++) table.get(i).set(false);
    }

    public void select(int i) {
        table.get(i).set(true);
    }

    public int size() {
        return table.size();
    }

    public boolean hasSelection() {
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).isSet()) return true;
        }
        return false;
    }

    public boolean check(NoteTable sqlTable, Note n) {
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).isSet()) {
                n = sqlTable.getNote(n.getGuid(), true, true, false, false, false);
                if (!table.get(i).attributeCheck(n)) return false;
            }
        }
        return true;
    }

    public String getLabel(int i) {
        return table.get(i).getLabel();
    }
}
