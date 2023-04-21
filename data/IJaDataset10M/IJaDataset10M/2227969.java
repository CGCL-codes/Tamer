package org.wfp.rita.util;

import org.wfp.rita.datafacade.DataFacade;
import org.wfp.rita.datafacade.DataFacade.CreateDatabaseOption;

/**
 * Class to create table structures from Hibernate mappings.
 * Loads the database parameters from database.properties.
 * @author chris
 */
public class DatabaseTool {

    /**
	 * @param args command-line arguments
	 */
    public static void main(String[] args) throws Exception {
        boolean exportData = true;
        String environmentName = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-nodata")) {
                exportData = false;
            } else if (args[i].equals("-c")) {
                environmentName = args[++i];
            } else {
                System.err.print("Unknown option: " + args[i] + "\n" + "\n" + "WFP Rita Database Tool - create tables and load initial data\n" + "\n" + "Usage: mvn exec:java [-Dexec.args=\"<options>\"]" + "\n" + "Options:\n" + "  -c <file>  Load specified configuration file instead of database.properties\n" + "  -nodata    Do not load the data, only create the tables\n");
                System.exit(2);
            }
        }
        DataFacade df = new DataFacade(environmentName, exportData ? CreateDatabaseOption.ALWAYS_WITH_INITIAL_DATA : CreateDatabaseOption.ALWAYS);
        df.close();
    }
}
