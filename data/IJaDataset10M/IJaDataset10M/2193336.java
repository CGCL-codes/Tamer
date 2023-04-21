package druid.data;

import druid.core.AttribList;

public class TableVars extends AttribList {

    public static final String BOOL = "b";

    public static final String STRING = "s";

    public static final String INT = "i";

    public static final String LONG = "l";

    public static final String CHAR = "c";

    public static final String FLOAT = "f";

    public static final String DOUBLE = "d";

    public TableVars() {
        addAttrib("name", "-UnNamed-");
        addAttrib("type", BOOL);
        addAttrib("value", "");
        addAttrib("descr", "");
    }

    protected AttribList getNewInstance() {
        return new TableVars();
    }
}
