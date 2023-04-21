package com.narirelays.ems.olap.context;

import oracle.express.olapi.data.full.ExpressDataProvider;
import oracle.express.olapi.transaction.ExpressTransactionProvider;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import oracle.olapi.data.source.FundamentalMetadataProvider;
import oracle.olapi.data.cursor.Cursor;
import oracle.olapi.data.cursor.CompoundCursor;
import oracle.olapi.data.cursor.ValueCursor;
import oracle.olapi.data.source.CursorManagerSpecification;
import oracle.olapi.data.source.Source;
import oracle.olapi.data.source.SpecifiedCursorManager;
import oracle.olapi.metadata.mdm.MdmAttribute;
import oracle.olapi.metadata.mdm.MdmHierarchy;
import oracle.olapi.metadata.mdm.MdmLevel;
import oracle.olapi.metadata.mdm.MdmLevelHierarchy;
import oracle.olapi.metadata.mdm.MdmMeasure;
import oracle.olapi.metadata.mdm.MdmMeasureDimension;
import oracle.olapi.metadata.mdm.MdmMetadataProvider;
import oracle.olapi.metadata.mdm.MdmPrimaryDimension;
import oracle.olapi.metadata.mdm.MdmSchema;
import oracle.olapi.transaction.NotCommittableException;
import oracle.olapi.transaction.TransactionProvider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.narirelays.ems.olap.context.CursorPrintWriter;
import com.narirelays.ems.persistence.orm.EnergyBaseDAO;
import com.narirelays.ems.resources.StorageService;

public class Context10g extends Object {

    /**
     * The CursorPrintWriter for this session.
     */
    private CursorPrintWriter cpw = null;

    /**
	 * The Logger for this class.
	 */
    private static final Logger log = LoggerFactory.getLogger(Context10g.class);

    /**
	 * The ExpressTransactionProvider for this session.
	 */
    private ExpressTransactionProvider tp = null;

    /**
	 * The ExpressDataProvider for this session.
	 */
    private ExpressDataProvider dp = null;

    /**
	 * The Oracle JDBC OracleConnection for this session.
	 */
    private oracle.jdbc.OracleConnection conn = null;

    /**
	 * The MdmMetadataProvider for this session.
	 */
    private MdmMetadataProvider mp = null;

    /**
	 * The Properties object for storing the command-line arguments and other
	 * parameters to use when creating the connection to the database.
	 */
    private Properties props = new Properties();

    /**
	 * The HashMap to store the metadata objects and their names.
	 */
    private Map m_hashMap = null;

    private List m_dimensionList = null;

    private List m_measureList = null;

    private String _server;

    private String _user;

    /**
	 * Creates a new Context10g object and specifies that the messages relating
	 * the progress of making the connection to the database and of creating the
	 * DataProvider, TransactionProvider, and the MetadataProvider are not
	 * displayed.
	 */
    public Context10g(Properties properties) {
        props = properties;
        cpw = new CursorPrintWriter();
        _connect();
    }

    /**
	 * Closes the DataProvider and the connection to the Oracle Database
	 * instance.
	 */
    public void close() {
        _disconnect();
    }

    /**
	 * Makes the connection to the Oracle OLAP server.
	 */
    private void _connect() {
        showInfo("Setting up connection properties...");
        try {
            OracleDataSource bds = (OracleDataSource) StorageService.ctx.getBean("olapdatasource");
            Connection connection = bds.getConnection();
            conn = (OracleConnection) connection;
            showInfo("Connection made.");
            _user = bds.getUser();
            _server = bds.getServerName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        _createProviders();
    }

    /**
	 * Closes the DataProvider.
	 */
    public void _disconnect() {
        if (null != dp) {
            showInfo("Closing DataProvider");
            dp.close();
            dp = null;
        }
        _closeConnection();
    }

    /**
	 * Closes the connection to the Oracle Database instance.
	 */
    private void _closeConnection() {
        try {
            if (null != conn) {
                showInfo("Closing JDBC connection");
                conn.close();
            }
        } catch (Exception e) {
        } finally {
            conn = null;
        }
    }

    /**
	 * Creates and stores the TransactionProvider, ExpressDataProvider, and the
	 * MdmMetadataProvider.
	 */
    private void _createProviders() {
        showInfo("Creating a TransactionProvider...");
        tp = new ExpressTransactionProvider();
        props.setProperty("HierarchyValueType", "unique");
        showInfo("Creating a DataProvider...");
        dp = new ExpressDataProvider(conn, tp, props);
        try {
            dp.initialize();
        } catch (SQLException e) {
            showError("Cannot not initialize the DataProvider. " + e);
        }
        showInfo("Getting the MDM MetadataProvider...");
        try {
            mp = (MdmMetadataProvider) dp.getDefaultMetadataProvider();
        } catch (Exception e) {
            showError("Cannot create the MDM metadata provider." + e);
        }
    }

    /**
	 * Gets the stored DataProvider.
	 * 
	 * @return The stored DataProvider, which is an instance of
	 *         ExpressDataProvider.
	 */
    public ExpressDataProvider getDataProvider() {
        return dp;
    }

    /**
	 * Gets the connection for the current thread.
	 */
    public final oracle.jdbc.OracleConnection getConnection() {
        try {
            ExpressDataProvider edp = (ExpressDataProvider) getDataProvider();
            oracle.jdbc.OracleConnection con = edp.getConnection();
            if (con != null) return con;
        } catch (Exception e) {
            showError("Cannot get a connection. " + "Using the stored connection." + e);
        }
        return conn;
    }

    /**
	 * Gets the FundamentalMetadataProvider.
	 * 
	 * @return The FundamentalMetadataProvider associated with the DataProvider.
	 */
    public FundamentalMetadataProvider getFundamentalMetadataProvider() {
        return dp.getFundamentalMetadataProvider();
    }

    /**
	 * Gets the stored MdmMetadataProvider.
	 * 
	 * @return The MdmMetadataProvider associated with the DataProvider.
	 */
    public MdmMetadataProvider getMetadataProvider() {
        return mp;
    }

    /**
	 * Gets the stored TransactionProvider.
	 * 
	 * @return The stored TransactionProvider.
	 */
    public TransactionProvider getTransactionProvider() {
        return tp;
    }

    /**
	 * From the top-level MdmSchema, gets the subschemas. If the user is
	 * GLOBAL_AW, then gets the GLOBALAW_SCHEMA subschema and gets a list of the
	 * MdmDimension objects and the list of MdmMeasure objects for that
	 * subschema. If the user is not GLOBAL_AW, then gets a list of MdmDimension
	 * objects and a list of MdmMeasure objects from the root MdmSchema. From
	 * the lists, the method creates a hash map to store the names of the
	 * dimensions and measures.
	 */
    private Map loadHashMap() {
        MdmSchema schema = mp.getRootSchema();
        Map map = new HashMap();
        boolean isGlobal = false;
        MdmSchema schemaToUse = null;
        if (getUser().toUpperCase().equals("GLOBAL_AW")) {
            isGlobal = true;
            List subSchemas = schema.getSubSchemas();
            Iterator subSchemasItr = subSchemas.iterator();
            while (subSchemasItr.hasNext()) {
                schemaToUse = (MdmSchema) subSchemasItr.next();
                if (schemaToUse.getName().equals("GLOBALAW_SCHEMA")) {
                    schema = schemaToUse;
                    break;
                }
            }
        }
        m_dimensionList = schema.getDimensions();
        Iterator objIter = m_dimensionList.iterator();
        MdmPrimaryDimension mdmPDim = null;
        while (objIter.hasNext()) {
            mdmPDim = (MdmPrimaryDimension) objIter.next();
            map.put(mdmPDim.getName().toUpperCase(), mdmPDim);
        }
        if (isGlobal) {
            m_measureList = schema.getMeasures();
        } else {
            MdmMeasureDimension mDim = (MdmMeasureDimension) schema.getMeasureDimension();
            m_measureList = mDim.getMeasures();
        }
        objIter = m_measureList.iterator();
        MdmMeasure mdmMeasure = null;
        while (objIter.hasNext()) {
            mdmMeasure = (MdmMeasure) objIter.next();
            map.put(mdmMeasure.getName().toUpperCase(), mdmMeasure);
        }
        return map;
    }

    /**
	 * Gets the MdmMeasure that has the specified name.
	 * 
	 * @param name
	 *            The name of the MdmMeasure that you want.
	 * 
	 * @return The MdmMeasure that has the specified name or null.
	 */
    public MdmMeasure getMdmMeasureByName(String name) {
        if (m_hashMap == null) m_hashMap = loadHashMap();
        MdmMeasure result = null;
        if (m_hashMap != null) {
            String val = name.toUpperCase();
            result = (MdmMeasure) m_hashMap.get(val);
        }
        return result;
    }

    /**
	 * Gets an array of MdmMeasure objects that has one MdmMeasure for each
	 * specified name. If the name is not in the hash map of MdmObject objects,
	 * then the array element for that name is null.
	 * 
	 * @param names
	 *            The names of the MdmMeasure objects that you want.
	 * 
	 * @return An array of the MdmMeasure objects that have the specified names.
	 */
    public MdmMeasure[] getMdmMeasuresByName(String[] names) {
        if (m_hashMap == null) m_hashMap = loadHashMap();
        MdmMeasure[] mdmMeasures = new MdmMeasure[names.length];
        if (m_hashMap != null) {
            for (int i = 0; i < names.length; i++) {
                String val = names[i].toUpperCase();
                mdmMeasures[i] = (MdmMeasure) m_hashMap.get(val);
            }
        }
        return (mdmMeasures);
    }

    /**
	 * Gets the MdmPrimaryDimension that has the specified name.
	 * 
	 * @param name
	 *            The name of the MdmPrimaryDimension that you want.
	 * 
	 * @return The MdmPrimaryDimension that has the specified name or null.
	 */
    public MdmPrimaryDimension getMdmPrimaryDimensionByName(String name) {
        if (m_hashMap == null) m_hashMap = loadHashMap();
        MdmPrimaryDimension result = null;
        if (m_hashMap != null) {
            String val = name.toUpperCase();
            result = (MdmPrimaryDimension) m_hashMap.get(val);
        }
        return result;
    }

    /**
	 * Gets the specified MdmAttribute.
	 * 
	 * @param mdmPDim
	 *            The MdmPrimaryDimension associated with the attribute.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * 
	 * @return The MdmAttribute with the specified name.
	 */
    public MdmAttribute getAttributeByName(MdmPrimaryDimension mdmPDim, String name) {
        List mdmAttrs = mdmPDim.getAttributes();
        Iterator mdmAttrsItr = mdmAttrs.iterator();
        while (mdmAttrsItr.hasNext()) {
            MdmAttribute mdmAttr = (MdmAttribute) mdmAttrsItr.next();
            if (mdmAttr.getName().equals(name)) return mdmAttr;
        }
        return null;
    }

    /**
	 * Gets the specified MdmHierarchy.
	 * 
	 * @param mdmPDim
	 *            The MdmPrimaryDimension associated with the hierarchy.
	 * 
	 * @param name
	 *            The name of the hierarchy.
	 * 
	 * @return The hierarchy with the specified name.
	 */
    public MdmHierarchy getHierarchyByName(MdmPrimaryDimension mdmPDim, String name) {
        List mdmHiers = mdmPDim.getHierarchies();
        Iterator mdmHierItr = mdmHiers.iterator();
        while (mdmHierItr.hasNext()) {
            MdmHierarchy mdmHier = (MdmHierarchy) mdmHierItr.next();
            if (mdmHier.getName().equals(name)) return mdmHier;
        }
        return null;
    }

    /**
	 * Gets the MdmLevel that has the specified name.
	 * 
	 * @param mdmLvlHier
	 *            The MdmLevelHierarchy that contains the level.
	 * 
	 * @param levelName
	 *            The name of the level.
	 * 
	 * @return The MdmLevel that has the specified name.
	 */
    public MdmLevel getLevelByName(MdmLevelHierarchy mdmLvlHier, String levelName) {
        List mdmLevels = mdmLvlHier.getLevels();
        Iterator mdmLevelsItr = mdmLevels.iterator();
        while (mdmLevelsItr.hasNext()) {
            MdmLevel mdmLevel = (MdmLevel) mdmLevelsItr.next();
            if ((mdmLevel.getName()).equals(levelName)) return mdmLevel;
        }
        return null;
    }

    /**
	 * Gets the local value portion of the unique value.
	 * 
	 * @param uniqueValue
	 *            The unique value from which to extract the local value.
	 * 
	 * @return The local value portion of the unique value.
	 */
    public String getLocalValue(String uniqueValue) {
        return cpw.getLocalValue(uniqueValue);
    }

    /**
	 * Displays a line of text.
	 * 
	 * @param line
	 *            A String that contains the text that you want to display.
	 */
    public void println(String line) {
        System.out.println(line);
    }

    /**
	 * Calls the printCursor(Cursor rootCursor, boolean displayLocVal) method of
	 * the CursorPrintWriter and passes it the rootCursor and the boolean value
	 * false. The boolean value specifies that if a ValueCursor has Dimension
	 * values, the method displays the full unique value rather than the local
	 * value.
	 * 
	 * @param rootCursor
	 *            The Cursor whose values you want to display.
	 */
    public void printCursor(Cursor rootCursor) {
        cpw.printCursor(rootCursor, false);
    }

    /**
	 * Displays the values of a CompoundCursor in a crosstab format.
	 * 
	 * @param rootCursor
	 *            The CompoundCursor to display.
	 */
    public void printAsCrosstab(CompoundCursor rootCursor) {
        cpw.printAsCrosstab(rootCursor);
    }

    /**
	 * Displays the specified message.
	 * 
	 * @param message
	 *            A String that contains the message to display.
	 */
    public void showInfo(String message) {
        log.debug(message);
    }

    /**
	 * Displays the specified error message.
	 * 
	 * @param message
	 *            A String that contains the error message to display.
	 */
    public void showError(String message) {
        log.error(message);
    }

    /**
	 * Prepares and commits the current Transaction.
	 */
    public void commit() {
        try {
            tp.prepareCurrentTransaction();
        } catch (NotCommittableException e) {
            println("Cannot prepare the current Transaction. " + e);
        }
        tp.commitCurrentTransaction();
    }

    /**
	 * Creates a Cursor for the specified Source and displays the values of the
	 * Cursor, with unique values for the dimension elements.
	 */
    public void displayResult(Source source) {
        _displayResult(source, false);
    }

    /**
	 * Creates a Cursor for the specified Source and displays the values of the
	 * Cursor, with unique values for the dimension elements if displayLocVal is
	 * false and with local values only if displayLocVal is true. This method
	 * retrieves local values only when the value separation String is the
	 * default double colons (::).
	 * 
	 * @param source
	 *            The Source for which you want to create a Cursor and display
	 *            its values.
	 * 
	 * @param displayLocVal
	 *            A boolean that specifies whether to display unique or local
	 *            dimension element values.
	 */
    private void _displayResult(Source source, boolean displayLocVal) {
        CursorManagerSpecification cursorMngrSpec = dp.createCursorManagerSpecification(source);
        SpecifiedCursorManager cursorManager = dp.createCursorManager(cursorMngrSpec);
        Cursor cursor = cursorManager.createCursor();
        cpw.printCursor(cursor, displayLocVal);
        cursorManager.close();
    }

    /**
	 * Displays the values of the specified Cursor. This method displays the
	 * unique value of dimension elements.
	 * 
	 * @param cursor
	 *            The Cursor that has the values you want to display.
	 */
    public void displayCursor(Cursor cursor) {
        _displayCursor(cursor, false);
    }

    /**
	 * Displays the values of the specified Cursor.
	 * 
	 * @param cursor
	 *            The Cursor that has the values you want to display.
	 * 
	 * @param displayLocVal
	 *            A boolean that specifies whether to display unique or local
	 *            dimension element values.
	 */
    public void displayCursor(Cursor cursor, boolean displayLocVal) {
        _displayCursor(cursor, displayLocVal);
    }

    /**
	 * Displays the values of the specified Cursor.
	 * 
	 * @param cursor
	 *            The Cursor that has the values you want to display.
	 * 
	 * @param displayLocVal
	 *            A boolean that specifies whether to display unique or local
	 *            dimension element values.
	 */
    private void _displayCursor(Cursor cursor, boolean displayLocVal) {
        cpw.printCursor(cursor, displayLocVal);
    }

    /**
	 * Creates a Cursor for the Source produced by a TopBottomTemplate and
	 * displays the values of the Cursor.
	 * 
	 * @param source
	 *            The Source returned by the getSource method of a
	 *            DynamicDefinition for the TopBottomTemplate example.
	 */
    public void displayTopBottomResult(Source source) {
        CursorManagerSpecification cursorMngrSpec = dp.createCursorManagerSpecification(source);
        SpecifiedCursorManager cursorManager = dp.createCursorManager(cursorMngrSpec);
        ValueCursor valueCursor = (ValueCursor) cursorManager.createCursor();
        cpw.printTopBottomResult(valueCursor);
        cursorManager.close();
    }

    /**
	 * Displays the values of a Cursor in a crosstab format.
	 * 
	 * @param cursor
	 *            The CompoundCursor to display.
	 */
    public void displayCursorAsCrosstab(CompoundCursor cursor) {
        cpw.printAsCrosstab(cursor);
    }

    /**
	 * Specifies the name of the Oracle OLAP server for the connection.
	 * 
	 * @param server
	 *            The name of the server on which the Oracle Database instance
	 *            is running.
	 */
    public void setServer(String server) {
        _server = server;
    }

    /**
	 * Gets the name of the Oracle OLAP server for the connection.
	 * 
	 * @return A String containing the name of the Oracle OLAP server.
	 */
    public String getServer() {
        return _server;
    }

    /**
	 * Specifies the username for the connection.
	 * 
	 * @param user
	 *            The username to use in creating the connection.
	 */
    public void setUser(String user) {
        _user = user;
    }

    /**
	 * Gets the username to use for the connection.
	 * 
	 * @return A String containing the username for creating the connection.
	 */
    public String getUser() {
        return _user;
    }

    /**
	 * Gets the Properties object that contains the command-line arguments for
	 * this example.
	 * 
	 * @return Properties object that contains the command-line arguments for
	 *         this example.
	 */
    public Properties getProperties() {
        return props;
    }

    /**
	 * Gets a runtime property (a command-line argument).
	 * 
	 * @param key
	 *            The name of the property.
	 * 
	 * @param required
	 *            If this flag is true and the property is not specified, then
	 *            this method throws an IllegalArgumentException with an
	 *            appropriate message.
	 */
    public String getProperty(String key, boolean required) {
        String ret = props.getProperty(key);
        if ((required == true) && (ret == null)) {
            throw new IllegalArgumentException("Command-line arguments must be " + "specified in the form -<property_name> <value> with " + "the property name and value separated by whitespace.");
        }
        return ret;
    }

    /**
	 * Gets the value of the property with the specified key.
	 * 
	 * @param key
	 *            The key of the property for which you want the value.
	 * 
	 * @return The value associated with the specified key.
	 */
    public String getOptionalProperty(String key) {
        return getProperty(key, false);
    }
}
