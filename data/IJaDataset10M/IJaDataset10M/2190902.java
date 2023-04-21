package org.equanda.tapestry.selectionModel;

import org.equanda.tapestry.model.GMTable;
import org.equanda.tapestry.model.TableType;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Selection model for comboboxes links
 *
 * @author Florin
 */
public class TableTypeSelectionModel implements IPropertySelectionModel {

    IComponent resolver;

    GMTable table;

    boolean required;

    List<String> types = new ArrayList<String>();

    List<String> names = new ArrayList<String>();

    public TableTypeSelectionModel(IComponent resolver, GMTable table) {
        this(resolver, table, true);
    }

    public TableTypeSelectionModel(IComponent resolver, GMTable table, boolean required) {
        this.resolver = resolver;
        this.table = table;
        if (!required) {
            types.add(null);
            names.add(null);
        }
        for (TableType type : table.getTypes()) {
            types.add(type.getType());
            names.add(type.getName());
        }
    }

    public Object getOption(int index) {
        return types.get(index);
    }

    public int getOptionCount() {
        return types.size();
    }

    public String getValue(int index) {
        return types.get(index);
    }

    public String getLabel(int index) {
        return resolver.getMessages().getMessage("type." + table.getName() + "." + names.get(index) + ".label");
    }

    public String translateValue(String value) {
        return value;
    }
}
