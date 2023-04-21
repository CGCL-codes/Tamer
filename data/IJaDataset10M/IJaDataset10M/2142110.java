package org.taak.module.dom;

import org.dom4j.*;
import org.taak.Context;
import org.taak.error.*;
import org.taak.function.*;
import org.taak.util.*;
import java.util.*;

public class MakeHtmlTable extends Function {

    public static final String NAME = "makeHtmlTable";

    public static final MakeHtmlTable INSTANCE = new MakeHtmlTable();

    public Object apply(Context context, Object[] args) {
        if (args.length < 2 || args.length > 3) {
            throw new BadArgument("Wrong number of arguments to makeHtmlTable");
        }
        if (args[0] == null) {
            throw new NullArgument("Null first argument to makeHtmlTable()");
        }
        if (args[1] == null) {
            throw new NullArgument("Null second argument to makeHtmlTable()");
        }
        if (!(args[0] instanceof List)) {
            throw new TypeError("First argument to makeHtmlTable() is not a List");
        }
        if (!(args[1] instanceof List)) {
            throw new TypeError("Second argument to makeHtmlTable() is not a List");
        }
        Element table = null;
        if (args.length == 3 && args[2] != null) {
            table = (Element) args[2];
            table.setContent(null);
        } else {
            table = DocumentHelper.createElement("table");
        }
        List rows = (List) args[0];
        List cols = (List) args[1];
        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
            Element row = DocumentHelper.createElement("tr");
            table.add(row);
            Object obj = iter.next();
            for (int i = 0; i < cols.size(); i++) {
                String name = (String) cols.get(i);
                Object val = ObjectUtil.getMember(obj, name);
                Element cell = DocumentHelper.createElement("td");
                cell.setText(StringUtil.toString(val));
                row.add(cell);
            }
        }
        return table;
    }

    public String getName() {
        return NAME;
    }
}
