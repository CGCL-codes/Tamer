package adqlParser.parser;

/**
 * <p>This implementation of {@link DBConsistency} can't ensure the consistency with a "database" because it is not linked with any database.
 * So a parser using an object of this class won't ensure the consistency with a database: only the syntax will be checked !</p>
 * <p>However columns name and table name may be checked IF an object of this class is created with <i>checkExistence = true</i> and IF
 * you manage yourself columns and tables lists of DBConsistency with the appropriate functions.</p> 
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 06/2010
 * 
 * @see DBConsistency
 */
public class DefaultDBConsistency extends DBConsistency {

    protected final boolean check;

    /**
	 * Creates a DBConsistency which won't ensure any kind of consistency with a database.
	 */
    public DefaultDBConsistency() {
        super();
        check = false;
    }

    /**
	 * Creates a DBConsistency which will check columns and tables names. To succeed you will have to add yourself columns and tables (names and alias) thanks to the appropriate functions (see {@link DBConsistency}).
	 * 
	 * @param checkExistence	<i>true</i> to check column and table names, else <i>false</i>.
	 */
    public DefaultDBConsistency(boolean checkExistence) {
        super();
        check = checkExistence;
    }

    @Override
    public boolean columnExists(String columnName, String tableAlias) throws ParseException {
        return check ? super.columnExists(columnName, tableAlias) : true;
    }

    @Override
    public int getNbTables(String columnName) {
        return check ? super.getNbTables(columnName) : 1;
    }

    @Override
    public String[] getTables(String columnName) {
        return check ? super.getTables(columnName) : (new String[] { "" });
    }

    @Override
    public void addColumns(String tableName) {
        ;
    }

    @Override
    public boolean tableExists(String tableName) {
        return true;
    }
}
