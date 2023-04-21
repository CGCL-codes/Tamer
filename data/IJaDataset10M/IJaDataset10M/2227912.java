package org.streets.database.datadict.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Table;
import org.streets.database.annotations.DataDictDef;
import org.streets.database.datadict.ColumnDefinition;
import org.streets.database.datadict.TableDefinition;

/**
 * 	Module.Tablename[alias] {
 *		column1[alias](P|F|N|CodeGroupId]):type|group|<Y|N>e[];
 *		column2[alias](P|F|N|CodeGroupId]):type|group|<Y|N>e[];
 *		column3[alias](P|F|N|CodeGroupId]):type|group|<Y|N>e[];
 *	}
 *
 * @author dzb
 *
 */
public class TableDefinitionImpl implements TableDefinition, Serializable {

    private static final long serialVersionUID = -7387285799920146894L;

    private String category;

    private String name;

    private String alias;

    private final List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

    public TableDefinitionImpl(Table table, DataDictDef def) {
        this.category = def.category();
        this.name = table.name();
        this.alias = def.alias();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String display) {
        this.alias = display;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ColumnDefinition> getColumns() {
        return columns;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TableDefinitionImpl)) {
            return false;
        }
        TableDefinitionImpl other = (TableDefinitionImpl) object;
        if ((this.category == null && other.category != null) || (this.category != null && other.category == null)) {
            return false;
        }
        return this.name.equals(other.name);
    }
}
