package cn.myapps.core.dynaform.form.constants;

import java.util.HashMap;
import java.util.Map;

public class ConfirmConstant {

    /**
	 * @uml.property name="msgCodeNames"
	 * @uml.associationEnd qualifier="key:java.lang.Object java.lang.String"
	 */
    public static Map msgCodeNames;

    public static final int FORM_EXIST = 0;

    public static final int FORM_DATA_EXIST = 1;

    public static final int FIELD_EXIST = 2;

    public static final int FIELD_DATA_EXIST = 3;

    public static final int FIELD_TYPE_INCOMPATIBLE = 4;

    public static final int FIELD_DUPLICATE = 5;

    static {
        msgCodeNames = new HashMap();
        msgCodeNames.put(new Integer(FORM_EXIST), "table.exist");
        msgCodeNames.put(new Integer(FORM_DATA_EXIST), "table.data.exist");
        msgCodeNames.put(new Integer(FIELD_EXIST), "field.exist");
        msgCodeNames.put(new Integer(FIELD_DATA_EXIST), "field.data.exist");
        msgCodeNames.put(new Integer(FIELD_TYPE_INCOMPATIBLE), "field.type.incompatible");
        msgCodeNames.put(new Integer(FIELD_DUPLICATE), "field.duplicate");
    }

    public static String getMsgKeyName(int keyCode) {
        return (String) msgCodeNames.get(new Integer(keyCode));
    }
}
