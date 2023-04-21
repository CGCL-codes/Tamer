package org.openscience.jreferences.applications;

import net.sf.bibtexml.Entry;
import net.sf.bibtexml.File;
import java.util.Enumeration;

public class DatabaseMerger {

    public static void merge(net.sf.bibtexml.File db1, net.sf.bibtexml.File db2) {
        Enumeration entryEnum = db2.enumerateEntry();
        while (entryEnum.hasMoreElements()) {
            Entry entryToAdd = (Entry) entryEnum.nextElement();
            db1.addEntry(entryToAdd);
        }
    }
}
