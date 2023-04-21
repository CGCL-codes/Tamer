package com.loribel.commons.gui.bo.table;

import com.loribel.commons.business.GB_BOTools;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObjectSet;
import com.loribel.commons.business.impl.bo.GB_BOStringMBO;
import com.loribel.commons.util.STools;

/**
 * Implementation of GB_RowDecoratorCol to represent a StringM property of BusinessObject.
 *
 * @author Gregory Borelli
 */
public class GB_BORowDecoratorColMapStringM extends GB_BORowDecoratorColAbstract {

    private String lang;

    private String columnName2;

    private String mapName;

    public GB_BORowDecoratorColMapStringM(String a_boName, String a_propertyName, String a_mapName, String a_lang, boolean a_flagEdit) {
        super(a_boName, a_propertyName, a_flagEdit);
        mapName = a_mapName;
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
            columnName2 += "." + mapName;
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
        GB_SimpleBusinessObjectSet l_bo = (GB_SimpleBusinessObjectSet) a_item;
        GB_BOStringMBO l_str = (GB_BOStringMBO) GB_BOTools.getPropertyValueBoMapNotNull(l_bo, propertyName, mapName);
        return l_str.getPropertyValue(lang);
    }

    public void setColValue(Object a_item, Object a_value) {
        if (a_item == null) {
            return;
        }
        GB_SimpleBusinessObjectSet l_bo = (GB_SimpleBusinessObjectSet) a_item;
        GB_BOStringMBO l_str = (GB_BOStringMBO) GB_BOTools.getPropertyValueBoMapNotNull(l_bo, propertyName, mapName);
        l_str.setPropertyValue(lang, a_value);
    }
}
