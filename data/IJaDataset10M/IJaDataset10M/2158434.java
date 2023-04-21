package com.jaeksoft.searchlib.web.xmlrpc;

import java.util.HashMap;
import java.util.Map;
import com.jaeksoft.searchlib.schema.FieldValueItem;

public abstract class AbstractXmlRpc {

    protected Map<String, FieldValueItem> newInfoMap(String info) {
        Map<String, FieldValueItem> map = new HashMap<String, FieldValueItem>();
        map.put("info", new FieldValueItem(info));
        return map;
    }
}
