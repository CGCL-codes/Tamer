package org.mariella.rcp.table;

public class UserDefinedTableColumnDescriptor extends TableColumnDescriptor {

    public UserDefinedTableColumnDescriptor(String header) {
        super(header);
    }

    public UserDefinedTableColumnDescriptor(String header, int weight) {
        super(header, weight);
    }
}
