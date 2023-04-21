package org.in4ama.datasourcemanager.csv;

import org.in4ama.datasourcemanager.AbstractDataSource;
import org.in4ama.datasourcemanager.DataContext;
import org.in4ama.datasourcemanager.DataSet;
import org.in4ama.datasourcemanager.DataSetTemplate;
import org.in4ama.datasourcemanager.DefaultDataSetTemplate;
import org.in4ama.datasourcemanager.cfg.DataSetConfiguration;
import org.in4ama.datasourcemanager.cfg.DataSourceConfiguration;
import org.in4ama.datasourcemanager.cfg.Property;
import org.in4ama.datasourcemanager.db.DbDataSetTemplate;
import org.in4ama.datasourcemanager.exception.DataSourceException;

/** Data source for CSV files. */
public class CsvDataSource extends AbstractDataSource {

    private static final String[] keyWords = { "select", "where" };

    public static final String TYPE_NAME = "ds.csv";

    public static final String FILEPATH = "filepath";

    public static final String SEPARATOR = "separator";

    public static final String FIRSTROWCOLS = "firstrowcols";

    public static final char DEFAULT_SEPARATOR = ',';

    public static final boolean DEFAULT_FIRSTROWCOLS = false;

    public static final String QUERY = "query";

    /** Creates a new instance of the CsvDataSourceClass */
    public CsvDataSource() throws DataSourceException {
    }

    /** Does nothing */
    public void initialise() {
    }

    /** Amends the specified query in a way that it can be executed
	 * and returns it. */
    private String getExecutableQuery(String query) {
        String[] keyValues = { null, null };
        for (int i = keyWords.length - 1; i >= 0; i--) {
            String keyWord = keyWords[i];
            int idx = query.toLowerCase().indexOf(keyWord);
            if (idx >= 0) {
                keyValues[i] = query.substring(idx + keyWord.length());
                query = query.substring(0, idx);
            }
        }
        return "select " + keyValues[0];
    }

    /** Returns the associated data set template. */
    @Override
    public DataSetTemplate getDataSetTemplate(DataSetConfiguration dataSetConfiguration) throws DataSourceException {
        DataSetTemplate dataSetTemplate = null;
        try {
            String query = (String) dataSetConfiguration.getProperty(QUERY).getValue();
            query = getExecutableQuery(query);
            DataSetConfiguration tmpDataSetConfiguration = new DataSetConfiguration(dataSetConfiguration);
            tmpDataSetConfiguration.getProperty(QUERY).setValue(query);
            DataSet dataSet = createDataSet(tmpDataSetConfiguration, null);
            dataSetTemplate = new DefaultDataSetTemplate(dataSet, DbDataSetTemplate.getBuilder());
        } catch (Exception ex) {
            String msg = "Unable to create the JDBC template data set.";
            throw new DataSourceException(msg, ex);
        }
        return dataSetTemplate;
    }

    /** Returns a template configuration of this data source. 
	 * @throws DataSourceException */
    @Override
    protected DataSourceConfiguration createConfigurationTemplate() throws DataSourceException {
        DataSourceConfiguration configuration = new DataSourceConfiguration(name);
        configuration.setType(TYPE_NAME);
        configuration.setProperty(new Property(FILEPATH));
        configuration.setProperty(new Property(SEPARATOR));
        configuration.setProperty(new Property(FILEPATH));
        return configuration;
    }

    /** Returns a template data set configuration. */
    @Override
    protected DataSetConfiguration createDataSetConfigurationTemplate() throws DataSourceException {
        DataSetConfiguration configuration = new DataSetConfiguration(name, "template");
        configuration.addProperty(new Property(QUERY));
        return configuration;
    }

    /** Creates and returns a data set according to the given configuration */
    public DataSet createDataSet(DataSetConfiguration dataSetConfiguration, DataContext dataContext) throws DataSourceException {
        String dataSetName = dataSetConfiguration.getName();
        String filePath = getFilePath();
        char separator = getSeparator();
        boolean firstRowCols = getFirstRowCols();
        String query = (String) dataSetConfiguration.getProperty(QUERY).getValue();
        return new CsvDataSet(dataSetName, this, filePath, query, separator, firstRowCols);
    }

    /** Returns the path at which the CSV file is stored. */
    public String getFilePath() {
        return (String) properties.get(FILEPATH);
    }

    /** Sets the path at which the CSV file is stored. */
    public void setFilePath(String filePath) {
        properties.put(FILEPATH, filePath);
    }

    /** Indicates whether the first row should be treated as column names. */
    public boolean getFirstRowCols() {
        Object value = properties.get(FIRSTROWCOLS);
        return (value != null) ? "true".equals(value) : DEFAULT_FIRSTROWCOLS;
    }

    /** Sets the value indicating whether the first 
	 * row should be treated as column names. */
    public void setFirstRowCols(boolean firstRowCols) {
        properties.put(FIRSTROWCOLS, String.valueOf(firstRowCols));
    }

    /** Gets the character used to separate the values in the CSV file. */
    public char getSeparator() {
        String value = (String) properties.get(SEPARATOR);
        return ((value != null) && (value.length() > 0)) ? value.charAt(0) : DEFAULT_SEPARATOR;
    }

    /** Sets the character used to separate 
	 * the values in the CSV file. */
    public void setSeparator(char c) {
        String value = String.valueOf(c);
        properties.put(SEPARATOR, value);
    }

    /** Returns the type of this data source. */
    @Override
    public String getType() {
        return TYPE_NAME;
    }
}
