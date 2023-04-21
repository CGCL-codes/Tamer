package ossobook2010.querys;

import java.util.ArrayList;
import java.util.HashMap;
import ossobook2010.exceptions.StatementNotExecutedException;
import ossobook2010.helpers.metainfo.Concordance;
import ossobook2010.helpers.metainfo.Key;
import ossobook2010.helpers.metainfo.Project;

/**
 *
 * @author Jojo
 */
public interface IConcordanceManager extends SynchronizationInterface {

    public static final String TABLENAME_CONCORDANCE = "concordance";

    public static final String CONCORDANCE_PROJECTID = TABLENAME_CONCORDANCE + ".ProjectID";

    public static final String CONCORDANCE_PROJECT_DATABASE_NUMBER = TABLENAME_CONCORDANCE + ".ProjectDatabaseNumber";

    public static final String CONCORDANCE_ARCHEOLOGICALUNIT = TABLENAME_CONCORDANCE + ".ArcheologicalUnit";

    public static final String CONCORDANCE_CHRONOLOGY1 = TABLENAME_CONCORDANCE + ".Chronology1";

    public static final String CONCORDANCE_CHRONOLOGY2 = TABLENAME_CONCORDANCE + ".Chronology2";

    public static final String CONCORDANCE_CHRONOLOGY3 = TABLENAME_CONCORDANCE + ".Chronology3";

    public static final String CONCORDANCE_CHRONOLOGY4 = TABLENAME_CONCORDANCE + ".Chronology4";

    public static final String CONCORDANCE_ZUSTAND = TABLENAME_CONCORDANCE + "." + IQueryManager.ZUSTAND;

    public static final String CONCORDANCE_NACHRICHTENNUMER = TABLENAME_CONCORDANCE + "." + IQueryManager.NACHRICHTENNUMMER;

    public static final String CONCORDANCE_DELETED = TABLENAME_CONCORDANCE + "." + IQueryManager.DELETED;

    /**
     * Returns a mapping of all Concordances mapped to the Archeological Unit
     * @param project
     * @return
     * @throws StatementNotExecutedException 
     */
    HashMap<String, ArrayList<String>> getAllConcordances(Key project) throws StatementNotExecutedException;

    /**
     * Returns a List of all Concordances for the given Project
     * @param project
     * @return
     * @throws StatementNotExecutedException 
     */
    ArrayList<Concordance> getConcordanceInformation(Project project) throws StatementNotExecutedException;

    @Override
    ArrayList<String[]> getEntries(Project project, String[][] columntypes, int event, String lastSynchronisation) throws StatementNotExecutedException;

    @Override
    ArrayList<String[]> getEntry(String[][] data, String[] keyValues, ArrayList<String> key) throws StatementNotExecutedException;

    @Override
    String getLastChange(ArrayList<String> key, String[] keyValues) throws StatementNotExecutedException;

    @Override
    void insertData(String[] string, String[] string0) throws StatementNotExecutedException;

    @Override
    void insertData(String[] scheme, String[] data, boolean ignore) throws StatementNotExecutedException;

    /**
     * Saves the given concordance Informations in the given Project
     * @param concordances
     * @param project
     * @throws StatementNotExecutedException 
     */
    void saveConcordanceInformation(ArrayList<Concordance> concordances, Project project) throws StatementNotExecutedException;

    @Override
    int updateData(String[] scheme, String[] data, ArrayList<String> key) throws StatementNotExecutedException;
}
