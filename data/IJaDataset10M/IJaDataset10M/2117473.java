package net.sf.dhwt.manager;

import net.sf.dhwt.Nominate;
import net.sf.dhwt.tracer.Debug;
import net.sf.dhwt.util.GenericDataSource;

/**
 *  This class used to keep all the data source objects, the data was load by
 *  ConfigInitializer and can be access by name via this class
 *
 * @author     Huijing Sheng
 * @version    2.0 2003-11-07
 */
public class DataSourceManager extends Manager {

    private static DataSourceManager instance = new DataSourceManager();

    /**
     *  Gets the manager attribute of the DataSourceManager class
     *
     * @return    The manager value
     */
    public static DataSourceManager getManager() {
        return instance;
    }

    private DataSourceManager() {
    }

    /**
     *  Gets a datasource by name
     *
     * @return       Nominate
     * @param  name
     */
    public Nominate get(String name) {
        Nominate result = null;
        try {
            result = (GenericDataSource) super.get(name);
        } catch (Exception e) {
            Debug.getDebug().error(getClass().getName(), e);
        }
        return result;
    }
}
