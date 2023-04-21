package com.nexirius.tools.properties.model;

import com.nexirius.framework.datamodel.ArrayModel;
import com.nexirius.framework.datamodel.DataModelEnumeration;
import com.nexirius.util.StringVector;
import com.nexirius.util.XFile;

public class EntryArrayModel extends ArrayModel {

    public static final String FIELD_NAME = "EntryArray";

    public EntryArrayModel() {
        super(new EntryModel(), FIELD_NAME);
    }

    public void readEntries(String filename) throws Exception {
        XFile file = new XFile(filename);
        StringVector lines = file.getTextLines();
        for (String line = lines.firstItem(); line != null; line = lines.nextItem()) {
            EntryModel entry = new EntryModel();
            if (entry.parse(line)) {
                sortInsert(entry);
            }
        }
    }

    public void saveTo(String filename, String locale) throws Exception {
        XFile file = new XFile(filename);
        DataModelEnumeration e = getEnumeration();
        StringVector lines = new StringVector();
        while (e.hasMore()) {
            EntryModel entry = (EntryModel) e.next();
            String line = entry.getLine(locale);
            if (line != null) {
                lines.append(line);
            }
        }
        file.writeTextLines(lines);
    }

    public void addLocaleEntry(String locale, EntryModel newEntry) {
        DataModelEnumeration e = getEnumeration();
        while (e.hasMore()) {
            EntryModel entry = (EntryModel) e.next();
            if (entry.getKey().equals(newEntry.getKey())) {
                entry.setLocaleText(locale, newEntry.getValueText());
                return;
            }
        }
        sortInsert(newEntry);
    }
}
