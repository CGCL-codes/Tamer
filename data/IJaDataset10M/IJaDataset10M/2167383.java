package com.hummer.service.data.definition;

import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import com.hummer.service.data.base.BaseDataObjectDefinition;
import com.hummer.service.db.SQLDefinition;
import com.hummer.service.enums.EnumDataTypes;

/**
 * @author gernot.hummer
 *
 */
public class Language extends BaseDataObjectDefinition {

    public Language() {
        super(SQLDefinition.TABLENAMELANGUAGE);
        this.name = "Language";
        this.addField("ShortName", EnumDataTypes.STRING);
        this.addField("Name", EnumDataTypes.STRING);
        this.setSortBy("NAME");
        SortKey sKey = new SortKey(1, SortOrder.ASCENDING);
        this.addTableSortKey(sKey);
    }
}
