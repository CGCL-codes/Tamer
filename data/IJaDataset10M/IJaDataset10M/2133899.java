package iwonto;

import org.apache.commons.dbutils.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

/**
 * Die Tabellenansicht im Datenbank-Betrachter (siehe {@link DbBetrachter}).
 * Zeigt die in der Relationenliste (siehe {@link DbListe}) ausgewählte Relation als Tabelle an. 
 * 
 * @author Mario Bachmann
 * @version 0.71
 */
public class DbTabelle extends DefaultTableModel {

    private static final long serialVersionUID = 6305893591779163743L;

    private final QueryRunner queryRunner = new QueryRunner();

    /**
	 * Instanziiert eine DbTabelle
	 * 
	 * @param con Das JDBC-Verbindungs-Objekt.
	 * @param tableName Die in der Relationenliste ausgewählte Relation. 
	 * @param join_anfrage Die SQL-Anfrage, um den Join-Table zu "bauen".
	 * @throws SQLException Bei stellen der SQL-Anfrage kann etwas schief gehen. 
	 */
    public DbTabelle(Connection con, Object tableName, String join_anfrage) throws SQLException {
        String sql = null;
        if (tableName == "Join-Table") {
            sql = join_anfrage;
        } else {
            sql = "SELECT * FROM [" + tableName + "]";
        }
        System.out.println("AUSGABE VON sql: " + sql);
        queryRunner.query(con, sql, new ResultSetHandler() {

            public Object handle(ResultSet rs) throws SQLException {
                int numColumns = rs.getMetaData().getColumnCount();
                Vector<String> column = new Vector<String>();
                for (int i = 1; i <= numColumns; i++) {
                    column.add(rs.getMetaData().getColumnName(i));
                }
                Vector<Vector<String>> data = new Vector<Vector<String>>();
                while (rs.next()) {
                    Vector<String> row = new Vector<String>();
                    for (int i = 1; i <= numColumns; i++) {
                        String Spalte = rs.getString(i);
                        row.add(Spalte);
                    }
                    data.add(row);
                }
                setDataVector(data, column);
                return null;
            }
        });
    }
}
