package it.unibo.lmc.pjdbc.parser.dml.imp;

import java.util.ArrayList;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.schema.ColumnType;
import it.unibo.lmc.pjdbc.parser.schema.Table;

public class CreateTable extends ParsedCommand {

    private Table table;

    private ArrayList<ColumnType> columns = new ArrayList<ColumnType>();

    public CreateTable() {
    }

    public void addElement(ColumnType element) {
        this.columns.add(element);
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return this.table;
    }

    public String toString() {
        String c = "";
        for (ColumnType type : this.columns) {
            c += type + ",";
        }
        return "create table " + table + " ( " + c + " );";
    }

    public ArrayList<ColumnType> getColumnsElement() {
        return columns;
    }
}
