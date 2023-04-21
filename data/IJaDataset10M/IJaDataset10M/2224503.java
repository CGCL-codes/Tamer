package gov.sns.apps.pvbrowser;

import com.cosylab.gui.components.ProgressEvent;
import gov.sns.application.Application;
import gov.sns.tools.database.ConnectionDictionary;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import gov.sns.tools.swing.SwingWorker;
import java.util.HashMap;
import javax.swing.SwingUtilities;

/**
 * Provides a model for the PV Browser application. This model is used by the 
 * application to connect to the database.
 * 
 * @author Chris Fowlkes
 */
public class PVBrowserModel extends DatabaseModel {

    /**
   * Holds the list models for the 
   */
    private HashMap listModels = new HashMap();

    /**
   * Holds the data from the configuration table.
   */
    private ArrayList configurationRecords;

    /**
   * Flag that determines if the invalid flag from the database is used.
   */
    private boolean usesInvalidData = false;

    /**
   * Holds the name of the RDB table used for searching for PVs. Defaults to 
   * <CODE>REC_NM_V</CODE>.
   */
    private String searchTableName = "REC_NM_V";

    /**
   * Creates a new <CODE>PVBrowserModel</CODE>.
   */
    public PVBrowserModel(ConnectionDictionary connectionDictionary) {
        super(connectionDictionary);
    }

    /**
   * Loads the configuration data from the view in the database.
   * 
   * @param searchInterface The <CODE>PVBrowserSearchInterface</CODE> being created.
   */
    public void loadConfiguration(final PVBrowserSearchInterface searchInterface) {
        SwingWorker worker = new SwingWorker() {

            @Override
            public Object construct() {
                synchronized (listModels) {
                    if (configurationRecords == null) loadConfigurationFromDB(searchInterface); else loadConfigurationFromCache(searchInterface);
                }
                return null;
            }
        };
        worker.start();
    }

    /**
   * Loads the configuration data from the database.
   * 
   * @param searchInterface
   */
    private void loadConfigurationFromDB(final PVBrowserSearchInterface searchInterface) {
        try {
            fireTaskStarted(new ProgressEvent(PVBrowserModel.this, "Loading Configuration", 0, -1));
            Connection connection = getConnection();
            try {
                Statement query = connection.createStatement();
                try {
                    ResultSet data = query.executeQuery("SELECT * FROM PV_BROW_CONFIG ORDER BY CONFIG_CD, CONFIG_CD_SEQ");
                    try {
                        configurationRecords = new ArrayList();
                        while (data.next()) {
                            String code = data.getString("CONFIG_CD").trim();
                            String showIndicator = data.getString("CONFIG_SHOW_IND").trim();
                            String synonym = data.getString("CONFIG_SYN");
                            String column = data.getString("CONFIG_COL");
                            String text = data.getString("CONFIG_TEXT");
                            ConfigurationRecord record = new ConfigurationRecord(code, showIndicator, synonym, column, text);
                            configurationRecords.add(record);
                            if (code.equals("LSTBOX") && showIndicator.equals("Y")) {
                                StringBuffer key = new StringBuffer(synonym);
                                key.append(".");
                                key.append(column);
                                final String tableName = key.toString();
                                DefaultListModel model = (DefaultListModel) listModels.get(tableName);
                                if (model == null) {
                                    model = new DefaultListModel();
                                    listModels.put(tableName, model);
                                    loadListModel(model, synonym, column);
                                }
                            } else if (code.equals("INVLDPV") && showIndicator.equals("Y")) {
                                usesInvalidData = true;
                                searchTableName = synonym;
                            }
                            addConfigurationRecordToSearchInterface(record, searchInterface);
                            fireProgress(new ProgressEvent(PVBrowserModel.this, "Loading Configuration", 0, -1));
                        }
                    } finally {
                        data.close();
                    }
                } finally {
                    query.close();
                }
            } finally {
                connection.close();
            }
        } catch (final java.sql.SQLException exception) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    Application.displayError("SQL Error", "Database exception while fetching configuration data.", exception);
                    exception.printStackTrace();
                }
            });
        } finally {
            fireTaskComplete(new ProgressEvent(PVBrowserModel.this, "Loading Configuration", 0, -1));
        }
    }

    /**
   * Loads the configuration data from the cache.
   * 
   * @param searchInterface The search interface to setup.
   */
    private void loadConfigurationFromCache(PVBrowserSearchInterface searchInterface) {
        int configurationRecordCount = configurationRecords.size();
        for (int i = 0; i < configurationRecordCount; i++) {
            ConfigurationRecord record = (ConfigurationRecord) configurationRecords.get(i);
            addConfigurationRecordToSearchInterface(record, searchInterface);
        }
    }

    /**
   * Loads the <CODE>DefaultListModel</CODE> with the data from the RDB.
   * 
   * @param listModel The <CODE>DefaultListModel</CODE> to load.
   */
    private void loadListModel(final DefaultListModel listModel, final String synonym, final String column) {
        SwingWorker worker = new SwingWorker() {

            @Override
            public Object construct() {
                StringBuffer messageBuffer = new StringBuffer("Loading ");
                messageBuffer.append(column);
                messageBuffer.append(" Data");
                String message = messageBuffer.toString();
                try {
                    fireTaskStarted(new ProgressEvent(PVBrowserModel.this, message, 0, -1));
                    Connection connection = getConnection();
                    try {
                        Statement query = connection.createStatement();
                        try {
                            StringBuffer sql = new StringBuffer("SELECT DISTINCT ");
                            sql.append(column);
                            sql.append(" FROM ");
                            sql.append(synonym);
                            sql.append(" ORDER BY ");
                            sql.append(column);
                            ResultSet data = query.executeQuery(sql.toString());
                            try {
                                addToListLater(listModel, "Any");
                                while (data.next()) {
                                    addToListLater(listModel, data.getString(1));
                                    fireProgress(new ProgressEvent(PVBrowserModel.this, message, 0, -1));
                                }
                            } finally {
                                data.close();
                            }
                        } finally {
                            query.close();
                        }
                    } finally {
                        connection.close();
                    }
                } catch (final java.sql.SQLException exception) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            Application.displayError("SQL Error", "Database exception while fetching configuration data.", exception);
                            exception.printStackTrace();
                        }
                    });
                } finally {
                    fireTaskComplete(new ProgressEvent(PVBrowserModel.this, message, 0, -1));
                }
                return null;
            }
        };
        worker.start();
    }

    /**
   * Uses <CODE>SwingUtilities.invokeLater</CODE> to add the given value to the
   * given <CODE>DefaultListModel</CODE>.
   * 
   * @param model The <CODE>DefaultListModel</CODE> to which to add the given value.
   * @param value The value to add to the given <CODE>DefaultListModel</CODE>.
   */
    private void addToListLater(final DefaultListModel model, final Object value) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                model.addElement(value);
            }
        });
    }

    /**
   * Adds components to the search interface for the given record. This method
   * uses <CODE>SwingUtilities.invokeLater</CODE> to be thread safe.
   * 
   * @param record The <CODE>ConfigurationRecord</CODE> to add to the interface.
   * @param searchInterface The <CODE>PVBrowserSearchInterface</CODE> to change.
   */
    private void addConfigurationRecordToSearchInterface(final ConfigurationRecord record, final PVBrowserSearchInterface searchInterface) {
        if (record.isShow()) {
            String code = record.getCode();
            boolean show = record.isShow();
            if (show) if (code.equals("LSTBOX")) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        StringBuffer key = new StringBuffer(record.getSynonym());
                        key.append(".");
                        String column = record.getColumn();
                        key.append(column);
                        String tableName = key.toString();
                        DefaultListModel model = (DefaultListModel) listModels.get(tableName);
                        String text = record.getText();
                        searchInterface.addList(model, text, column);
                    }
                });
            } else if (code.equals("INVLDPV")) SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    searchInterface.showInvalidPVs();
                }
            });
        }
    }

    /**
   * Gets the value of the uses invalid data flag for the application.
   * 
   * @return The value of the applications uses invalid data flag.
   */
    public boolean isUsesInvalidData() {
        return usesInvalidData;
    }

    /**
   * Gets the name of the search table.
   * 
   * @return The name of the RDB table to use to search for PVs.
   */
    public String getSearchTableName() {
        return searchTableName;
    }

    /**
   * Holds the data for a configuration record from the database.
   */
    private class ConfigurationRecord {

        /**
     * Holds the data from the CONFIG_CD column.
     */
        private String code;

        /**
     * Holds the data from the CONFIG_SHOW_IND column.
     */
        private boolean show;

        /**
     * Holds the data from the CONFIG_SYN column.
     */
        private String synonym;

        /**
     * Holds the data from the CONFIG_COL column.
     */
        private String column;

        /**
     * Holds the data fron the CONFIG_TEXT column.
     */
        private String text;

        /**
     * Creates a new <CODE>ConfigurationRecord</CODE>.
     * 
     * @param code The value for the CONFIG_CD column.
     * @param show The value for the CONFIG_SHOW_IND column.
     * @param synonym The value for the CONFIG_SYN column.
     * @param column The value for the CONFIG_COL column.
     * @param text The value for the CONFIG_TEXT column.
     */
        public ConfigurationRecord(String code, boolean show, String synonym, String column, String text) {
            setCode(code);
            setShow(show);
            setSynonym(synonym);
            setColumn(column);
            setText(text);
        }

        /**
     * Creates a new <CODE>ConfigurationRecord</CODE>.
     * 
     * @param code The value for the CONFIG_CD column.
     * @param show The value for the CONFIG_SHOW_IND column.
     * @param synonym The value for the CONFIG_SYN column.
     * @param column The value for the CONFIG_COL column.
     * @param text The value for the CONFIG_TEXT column.
     */
        public ConfigurationRecord(String code, String show, String synonym, String column, String text) {
            this(code, show != null && show.equals("Y"), synonym, column, text);
        }

        /**
     * Sets the data from the CONFIG_CD column.
     * 
     * @param code The value for the CONFIG_CD column.
     */
        public void setCode(String code) {
            this.code = code;
        }

        /**
     * Gets the data from the CONFIG_CD column.
     * 
     * @return The value for the CONFIG_CD column.
     */
        public String getCode() {
            return code;
        }

        /**
     * Sets the value for the CONFIG_SHOW_IND column.
     * 
     * @param show The value for the CONFIG_SHOW_IND column.
     */
        public void setShow(boolean show) {
            this.show = show;
        }

        /**
     * Gets the value for the CONFIG_SHOW_IND column.
     * 
     * @return The value for the CONFIG_SHOW_IND column.
     */
        public boolean isShow() {
            return show;
        }

        /**
     * Sets the value for the CONFIG_SYN column.
     * 
     * @param synonym The value for the CONFIG_SYN column.
     */
        public void setSynonym(String synonym) {
            this.synonym = synonym;
        }

        /**
     * Gets the value for the CONFIG_SYN column.
     * 
     * @return The value for the CONFIG_SYN column.
     */
        public String getSynonym() {
            return synonym;
        }

        /**
     * Sets the value for the CONFIG_COL column.
     * 
     * @param column The value for the CONFIG_COL column.
     */
        public void setColumn(String column) {
            this.column = column;
        }

        /**
     * Gets the value for the CONFIG_COL column.
     * 
     * @return The value for the CONFIG_COL column.
     */
        public String getColumn() {
            return column;
        }

        /**
     * Sets the value for the CONFIG_TEXT column.
     * 
     * @param text The value for the CONFIG_TEXT column.
     */
        public void setText(String text) {
            this.text = text;
        }

        /**
     * Gets the value for the CONFIG_TEXT column.
     * 
     * @return The value for the CONFIG_TEXT column.
     */
        public String getText() {
            return text;
        }
    }
}
