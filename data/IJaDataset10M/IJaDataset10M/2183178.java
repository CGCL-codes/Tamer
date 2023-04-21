package sol.admin.systemmanagement.db;

/**
 *  Exception die bei kritischen Datenbankfehlern geworfen wird,
 *  zum Beispiel Login nicht eindeutig. Darf eigentlich nie geworfen werden,
 *  wenn nicht mal wirklich was schief geht mit der Datenbank
 *  @author Markus Hammori
 *  @version 0.1
 */
public class DatabaseInconsistenceException extends DatabaseException {

    /**
     *  String der angibt wie ernst der aufgetretene Fehler ist
     */
    private String _priority;

    public DatabaseInconsistenceException(String s) {
        super(s);
    }

    public DatabaseInconsistenceException(String s, String qString) {
        super(s, qString);
    }

    public DatabaseInconsistenceException(String s, String qString, String priority) {
        super(s, qString);
        _priority = priority;
    }

    public String getPriority() {
        return _priority;
    }
}
