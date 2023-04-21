package com.loribel.commons.gui.bo.table;

import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.business.impl.bo.GB_BOStringMBO;
import com.loribel.commons.util.STools;

/**
 * Implementation of GB_RowDecoratorCol to represent a StringM property of BusinessObject.
 *
 * @author Gregory Borelli
 */
public class GB_BORowDecoratorColStringM extends GB_BORowDecoratorColAbstract {

    private String lang;

    private String columnName2;

    public GB_BORowDecoratorColStringM(String a_boName, String a_propertyName, String a_lang, boolean a_flagEdit) {
        super(a_boName, a_propertyName, a_flagEdit);
        lang = a_lang;
    }

    public Class getColumnClass() {
        return String.class;
    }

    public String getColumnName() {
        if (columnName2 == null) {
            columnName2 = super.getColumnName();
            if (columnName2 == null) {
                return null;
            }
            if (columnName2.indexOf("\n") > -1) {
                columnName2 = STools.replace(columnName2, "\n", " [" + lang + "]\n");
            } else {
                columnName2 += " [" + lang + "]";
            }
        }
        return columnName2;
    }

    public Object getColValue(Object a_item) {
        if (a_item == null) {
            return null;
        }
        GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) a_item;
        Object l_value = l_bo.getPropertyValue(propertyName);
        if (l_value == null) {
            return null;
        }
        GB_BOStringMBO l_str = (GB_BOStringMBO) l_value;
        return l_str.getPropertyValue(lang);
    }

    public void setColValue(Object a_item, Object a_value) {
        if (a_item == null) {
            return;
        }
        GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) a_item;
        Object l_value = l_bo.getPropertyValue(propertyName);
        if (l_value == null) {
            return;
        }
        GB_BOStringMBO l_str = (GB_BOStringMBO) l_value;
        l_str.setPropertyValue(lang, a_value);
    }
}
