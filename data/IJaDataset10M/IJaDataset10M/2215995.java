package net.sf.cobol2j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RecordsMap extends HashMap {

    public RecordsMap(FileFormat fF) {
        List l = fF.getRecordFormat();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            RecordFormat rF = (RecordFormat) i.next();
            put(rF.getDistinguishFieldValue(), rF);
        }
    }
}
