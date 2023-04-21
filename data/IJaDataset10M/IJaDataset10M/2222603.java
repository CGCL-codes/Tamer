package business_layer;

import java.util.Vector;

/**
 *
 * @author Asia
 */
public class Statement {

    private String baseStatement;

    private StatementKind kind;

    private Vector<String> columnNames;

    private Vector<String> values;

    private Vector<DataType> types;

    private String tableName;

    Vector<Comparison> where;

    Vector<LogicalConnector> whereCorellations;

    public Statement(String s) throws Exception {
        baseStatement = s;
        columnNames = new Vector<String>();
        values = new Vector<String>();
        types = new Vector<DataType>();
        where = new Vector<Comparison>();
        whereCorellations = new Vector<LogicalConnector>();
        parse(s);
    }

    private void parse(String s) throws Exception {
        s = s.trim();
        String[] splittedStatement = s.split(" ");
        if (splittedStatement[0].equals("CREATE")) {
            if (splittedStatement[1].equals("TABLE")) {
                this.kind = StatementKind.CREATE;
            }
        } else {
            if (splittedStatement[0].equals("DROP")) {
                if (splittedStatement[1].equals("TABLE")) {
                    this.kind = StatementKind.DROP;
                } else {
                    throw new Exception("WRONG SYNTAX: not TABLE after DROP");
                }
            } else {
                if (splittedStatement[0].equals("DELETE")) {
                    this.kind = StatementKind.DELETE;
                } else {
                    if (splittedStatement[0].equals("UPDATE")) {
                        this.kind = StatementKind.DELETE;
                    } else {
                        if (splittedStatement[0].equals("INSERT")) {
                            if (splittedStatement[1].equals("INTO")) {
                                this.kind = StatementKind.DELETE;
                            } else {
                                throw new Exception("WRONG SYNTAX: no INTO after INSERT");
                            }
                        }
                    }
                }
            }
        }
        switch(kind.getStatementKind()) {
            case 1:
                this.tableName = splittedStatement[2];
                break;
            case 2:
                this.tableName = splittedStatement[2];
                break;
            case 3:
                this.columnNames.add(splittedStatement[2]);
                break;
            case 4:
                this.columnNames.add(splittedStatement[2]);
                break;
            case 5:
                this.columnNames.add(splittedStatement[2]);
                break;
        }
    }
}
