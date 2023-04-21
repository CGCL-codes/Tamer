package it.unibo.lmc.pjdbc.parser.schema;

import java.util.ArrayList;

public class ColumnType {

    private ArrayList<String> argument;

    private String typeName;

    private String columnName;

    public ColumnType(String name) {
        this.columnName = name;
    }

    public void setType(String typeName) {
        this.typeName = typeName;
    }

    public void setArgumentsStringList(ArrayList<String> argumentsStringList) {
        this.argument = argumentsStringList;
    }

    public String toString() {
        if (null != this.argument && this.argument.size() > 0) {
            String list = "(";
            for (String arg : this.argument) {
                list += arg + ",";
            }
            list += ")";
            return this.columnName + "{" + this.typeName + list + "}";
        }
        return this.columnName + "{" + this.typeName + "}";
    }

    public String getType() {
        return this.typeName;
    }

    public String getName() {
        return this.columnName;
    }
}
