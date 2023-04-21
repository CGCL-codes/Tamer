package gdg.dataGeneration;

import gdg.tables.tableModel.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import beans.Config;

/**
 * 
 * @author Silvio Donnini
 */
public class Filler {

    protected Connection connection;

    protected List<TableDataGenerator> tableDataGenerators;

    private StringBuffer executeOutput;

    private StringBuffer undoOutput;

    public Filler(Connection connection) {
        this.connection = connection;
    }

    public void fillTables(List<Table> tables, StringBuffer executeOutput, StringBuffer undoOutput) {
        this.executeOutput = executeOutput;
        this.undoOutput = undoOutput;
        init(tables);
        for (TableDataGenerator tdg : tableDataGenerators) {
            fillTable(tdg);
        }
    }

    public void fillTable(TableDataGenerator tdg) {
        int records = tdg.getRecords();
        if (records == 0) {
            return;
        }
        PreparedStatement template = null;
        SQLOperation operation;
        for (int i = 0; i < records; i++) {
            operation = tdg.nextTuple(executeOutput, undoOutput);
            operation.execute();
        }
    }

    public void init(List<Table> tables) {
        tableDataGenerators = new LinkedList<TableDataGenerator>();
        for (Table t : tables) {
            if (t.getRecords() != 0) {
                tableDataGenerators.add(new TableDataGenerator(t, connection));
            }
        }
    }

    public void clear(List<Table> tables) {
        Statement s = null;
        String query;
        List<Table> tabs = new LinkedList<Table>(tables);
        Collections.sort(tabs);
        Collections.reverse(tabs);
        try {
            s = connection.createStatement();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        String deleteTemplate = Config.getInstance().getDeleteTemplate();
        for (Table t : tabs) {
            query = deleteTemplate.replaceAll("\\?", t.getName());
            try {
                s.executeUpdate(query);
                Logger.getLogger("sql").info(query);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }
}
