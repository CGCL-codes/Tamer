package it.unibo.lmc.pjdbc.parser.dml.imp;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;

public class DropDB extends ParsedCommand {

    private String dbname;

    public DropDB() {
        super();
    }

    public void setDatabase(String databaseName) {
        this.dbname = databaseName;
    }

    public String getDatabaseName() {
        return this.dbname;
    }

    @Override
    public String toString() {
        return "dropDb " + this.defaultSchema + "." + this.dbname;
    }
}
