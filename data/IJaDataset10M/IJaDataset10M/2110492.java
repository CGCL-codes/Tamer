package jbreport.data;

import java.sql.Connection;
import java.util.Map;
import jbreport.ReportException;

/**
 * This interface defines the contract that is required for the class to be
 * pluggable into a Datasource instance.
 *
 * <p> Any classes implementing this method should have a public no-args
 * constructor which can be used by the datasource.
 *
 * @author Grant Finnemore
 * @version $Revision: 1.1.1.1 $
 */
public interface PhysicalConnection {

    /**
    * This method is called once, just after the instance is created. It 
    * should use the parameters in the map to initialize itself as appropriate.
    *
    * @throws ReportException should the initialization fail.
    */
    public void initialize(String datasourceType, Map properties) throws ReportException;

    /**
    * Returns the database connection to the caller
    */
    public Connection getConnection() throws ReportException;
}
