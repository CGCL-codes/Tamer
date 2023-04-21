package com.netx.cubigraf.entities;

import com.netx.data.Entity;
import com.netx.data.ValueList;
import com.netx.data.DatabaseException;
import com.netx.generics.basic.Checker;
import com.netx.generics.basic.Property;
import com.netx.generics.sql.Table;

public class Properties extends Entity {

    public Properties() {
        super();
    }

    public void createNew(String name, String value) throws DatabaseException {
        Checker.checkTextIdentifier(name, "name", true);
        ValueList values = new ValueList();
        values.addValue("name", name);
        values.addValue("value", value);
        insert(values);
    }

    public Property getProperty(String name) throws DatabaseException {
        Checker.checkTextIdentifier(name, "name", true);
        Table results = select("value", "name='" + name + "'");
        if (results.isEmpty()) {
            return null;
        } else {
            return new Property(name, results.getString(1, 0));
        }
    }
}
