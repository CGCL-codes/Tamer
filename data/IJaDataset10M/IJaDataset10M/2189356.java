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
public class OverallOrderStatus extends BaseDataObjectDefinition {

    public OverallOrderStatus() {
        super(SQLDefinition.TABLENAMEORDERSTATUS);
        this.name = "Monthly Outbound Order";
        this.addField("LanguageID", EnumDataTypes.FK_INTEGER);
        this.addForeignKey("LanguageID", SQLDefinition.TABLENAMELANGUAGE, "ID", EnumDataTypes.INTEGER, new Language());
        this.getForeignKey("LanguageID").setTargetShowColumnName("Name");
        this.getForeignKey("LanguageID").setTranslation(true);
        this.addField("ItemTypeID", EnumDataTypes.FK_INTEGER);
        this.addForeignKey("ItemTypeID", SQLDefinition.TABLENAMEITEMTYPE, "ID", EnumDataTypes.INTEGER, new ItemType());
        this.getForeignKey("ItemTypeID").setTargetShowColumnName("ShortName");
        this.addField("Number", EnumDataTypes.INTEGER);
        this.setSortBy("LANGUAGEID, ITEMTYPEID");
        SortKey sKey = new SortKey(0, SortOrder.ASCENDING);
        this.addTableSortKey(sKey);
        sKey = new SortKey(1, SortOrder.ASCENDING);
        this.addTableSortKey(sKey);
    }
}
