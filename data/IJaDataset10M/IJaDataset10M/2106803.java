package org.maverickdbms.util;

import java.util.Hashtable;
import org.maverickdbms.basic.mvConstantString;
import org.maverickdbms.basic.mvSystemPrograms;

public class index implements mvSystemPrograms {

    Hashtable verbs = new Hashtable();

    public index() {
        verbs.put("BASIC", "org.maverickdbms.util.BASIC");
        verbs.put("COUNT", "org.maverickdbms.util.COUNT");
        verbs.put("CREATE.FILE", "org.maverickdbms.util.CREATE_056FILE");
        verbs.put("CREATE-FILE", "org.maverickdbms.util.CREATE_056FILE");
        verbs.put("DATE", "org.maverickdbms.util.DATE");
        verbs.put("DELETE.FILE", "org.maverickdbms.util.DELETE_056FILE");
        verbs.put("DELETE-FILE", "org.maverickdbms.util.DELETE_056FILE");
        verbs.put("LIST", "org.maverickdbms.util.LIST");
        verbs.put("SELECT", "org.maverickdbms.util.SELECT");
        verbs.put("TIME", "org.maverickdbms.util.TIME");
    }

    public String resolve(mvConstantString key) {
        return (String) verbs.get(key.toString());
    }
}

;
