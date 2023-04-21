package org.qsari.effectopedia.core.embeddedobjects;

import java.util.HashMap;
import java.util.Iterator;
import org.qsari.effectopedia.base.XMLExportable;
import org.qsari.effectopedia.base.XMLImportable;

public class FixedValuesLists extends ValuesList implements XMLImportable, XMLExportable {

    public FixedValuesLists() {
        super();
        lists = new HashMap<String, FixedValuesList>();
    }

    public FixedValuesLists(String name, FixedValuesList categories) {
        super(name);
        lists = new HashMap<String, FixedValuesList>(categories.size());
        createCategories(categories);
    }

    public void createCategories(FixedValuesList categories) {
        Iterator<String> iterator = categories.getIterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            lists.put(name, new FixedValuesList(name, ""));
        }
    }

    public void setListValues(String listName, String commaSeparatedList) {
        FixedValuesList list = lists.get(listName);
        if (list != null) list.setList(commaSeparatedList);
    }

    public FixedValuesList getList(String listName) {
        return this.lists.get(listName);
    }

    public String get(String listName, int index) {
        FixedValuesList list = lists.get(listName);
        if (list != null) return list.get(index); else return null;
    }

    protected HashMap<String, FixedValuesList> lists;
}
