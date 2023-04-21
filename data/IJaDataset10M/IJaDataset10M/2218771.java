package org.tn5250j.sql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;
import org.tn5250j.event.FTPStatusEvent;
import org.tn5250j.event.FTPStatusListener;
import org.tn5250j.framework.tn5250.tnvt;
import org.tn5250j.tools.filters.FileFieldDef;
import org.tn5250j.tools.filters.OutputFilterInterface;

public class AS400Xtfr {

    private boolean loggedIn;

    private String hostName;

    private int timeout = 50000;

    private boolean connected;

    private ArrayList ffd;

    private tnvt vt;

    private Vector<FTPStatusListener> listeners;

    private FTPStatusEvent status;

    private boolean aborted;

    private char decChar;

    private OutputFilterInterface ofi;

    private Thread getThread;

    private String user;

    private String pass;

    private Connection connection;

    public AS400Xtfr(tnvt v) {
        vt = v;
        status = new FTPStatusEvent(this);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        decChar = formatter.getDecimalFormatSymbols().getDecimalSeparator();
    }

    public void setOutputFilter(OutputFilterInterface o) {
        ofi = o;
    }

    public void setDecimalChar(char dec) {
        decChar = dec;
    }

    /**
    * Set up ftp sockets and connect to an as400
    */
    public boolean connect(String host) {
        connection = null;
        hostName = host.toUpperCase();
        try {
            printFTPInfo("Connecting to " + hostName);
            Driver driver2 = (Driver) Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
            DriverManager.registerDriver(driver2);
            connection = DriverManager.getConnection("jdbc:as400://" + hostName + ";decimal separator=" + decChar + ";extended metadata=true;translate binary=true", user, pass);
            printFTPInfo("jdbc:as400://" + hostName + ";decimal separator=" + decChar + ";extended metadata=true;translate binary=true");
            fireInfoEvent();
            printFTPInfo("Connected to " + hostName);
            return true;
        } catch (NoClassDefFoundError ncdf) {
            printFTPInfo("Error: JDBC Driver not found.  Please check classpath.");
        } catch (Exception e) {
            printFTPInfo("Error: " + e.getMessage() + "\n\n" + "There was an error connecting to host " + host.toUpperCase() + "\n\nPlease make sure that you run " + "the command STRHOSTSVR");
            System.out.println("Exception while retrieving data : " + e.getMessage());
        }
        return false;
    }

    /**
    * Send quit command to ftp server and close connections
    */
    public void disconnect() {
    }

    /**
    * returns whether or not the system is connected to an AS400 or not
    */
    public boolean isConnected() {
        return connected;
    }

    /**
    * Add a FTPStatusListener to the listener list.
    *
    * @param listener  The FTPStatusListener to be added
    */
    public synchronized void addFTPStatusListener(FTPStatusListener listener) {
        if (listeners == null) {
            listeners = new java.util.Vector<FTPStatusListener>(3);
        }
        listeners.addElement(listener);
    }

    /**
    * Notify all registered listeners of the FTPStatusEvent.
    *
    */
    private void fireStatusEvent() {
        if (listeners != null) {
            int size = listeners.size();
            for (int i = 0; i < size; i++) {
                FTPStatusListener target = listeners.elementAt(i);
                target.statusReceived(status);
            }
        }
    }

    /**
    * Notify all registered listeners of the command status.
    *
    */
    private void fireCommandEvent() {
        if (listeners != null) {
            int size = listeners.size();
            for (int i = 0; i < size; i++) {
                FTPStatusListener target = listeners.elementAt(i);
                target.commandStatusReceived(status);
            }
        }
    }

    /**
    * Notify all registered listeners of the file information status.
    *
    */
    private void fireInfoEvent() {
        if (listeners != null) {
            int size = listeners.size();
            for (int i = 0; i < size; i++) {
                FTPStatusListener target = listeners.elementAt(i);
                target.fileInfoReceived(status);
            }
        }
    }

    /**
    * Remove a FTPStatusListener from the listener list.
    *
    * @param listener  The FTPStatusListener to be removed
    */
    public synchronized void removeFTPStatusListener(FTPStatusListener listener) {
        if (listeners == null) {
            return;
        }
        listeners.removeElement(listener);
    }

    /**
    * Send the user id and password to the connected host
    *
    * @param user  The user name
    * @param password  The password of the user
    */
    public boolean login(String user, String passWord) {
        aborted = false;
        loggedIn = true;
        this.user = user;
        this.pass = passWord;
        return true;
    }

    /**
    * Returns whether a field is selected for output or not
    *
    */
    public boolean isFieldSelected(int which) {
        FileFieldDef ffD = (FileFieldDef) ffd.get(which);
        return ffD.isWriteField();
    }

    /**
    * Select all the fields for output
    */
    protected void selectAll() {
        FileFieldDef f;
        for (int x = 0; x < ffd.size(); x++) {
            f = (FileFieldDef) ffd.get(x);
            f.setWriteField(true);
        }
    }

    /**
    * Unselect all fields for output.  This is a convenience method to unselect
    * all fields for a file that will only need to output a couple of fields
    */
    protected void selectNone() {
        FileFieldDef f;
        for (int x = 0; x < ffd.size(); x++) {
            f = (FileFieldDef) ffd.get(x);
            f.setWriteField(false);
        }
    }

    /**
    * Returns whether there are any fields selected or not
    */
    public boolean isFieldsSelected() {
        FileFieldDef f;
        for (int x = 0; x < ffd.size(); x++) {
            f = (FileFieldDef) ffd.get(x);
            if (f.isWriteField()) return true;
        }
        return false;
    }

    /**
    * Convenience method to select or unselect a field for output
    */
    public void setFieldSelected(int which, boolean value) {
        FileFieldDef ffD = (FileFieldDef) ffd.get(which);
        ffD.setWriteField(value);
    }

    /**
    * Convenience method to return the name of a field
    */
    public String getFieldName(int which) {
        FileFieldDef ffD = (FileFieldDef) ffd.get(which);
        return ffD.getFieldName();
    }

    /**
    * Returns the number of fields in the File Field Definition array of fields
    * returned from the DSPFFD command
    */
    public int getNumberOfFields() {
        return ffd.size();
    }

    /**
    * Transfer the file information to an output file
    */
    public boolean getFile(String remoteFile, String localFile, String statement, boolean useInternal) {
        boolean flag = true;
        if (connection == null) {
            printFTPInfo("Not connected to any server!");
            return false;
        }
        final String localFileF = localFile;
        final String query = statement;
        final boolean internal = useInternal;
        Runnable getRun = new Runnable() {

            public void run() {
                try {
                    DatabaseMetaData dmd = connection.getMetaData();
                    Statement select = connection.createStatement();
                    ResultSet rs = select.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int numCols = rsmd.getColumnCount();
                    ResultSet rsd = dmd.getColumns(null, "VISIONR", "CXREF", null);
                    while (rsd.next()) {
                        System.out.println(rsd.getString(12));
                    }
                    if (ffd != null) {
                        ffd.clear();
                        ffd = null;
                    }
                    ffd = new ArrayList();
                    printFTPInfo("Number of columns: " + rsmd.getColumnCount());
                    for (int x = 1; x <= numCols; x++) {
                        printFTPInfo("Column " + x + ": " + rsmd.getColumnLabel(x) + " " + rsmd.getColumnName(x) + " " + rsmd.getColumnType(x) + " " + rsmd.getColumnTypeName(x) + " " + rsmd.getPrecision(x) + " " + rsmd.getScale(x) + " cn " + rsmd.getCatalogName(x) + " tn " + rsmd.getTableName(x) + " sn " + rsmd.getSchemaName(x));
                        FileFieldDef ffDesc = new FileFieldDef(vt, decChar);
                        if (internal) ffDesc.setFieldName(rsmd.getColumnName(x)); else ffDesc.setFieldName(rsmd.getColumnLabel(x));
                        ffDesc.setNeedsTranslation(false);
                        ffDesc.setStartOffset("0");
                        ffDesc.setFieldLength(Integer.toString(rsmd.getColumnDisplaySize(x)));
                        ffDesc.setNumDigits(Integer.toString(rsmd.getPrecision(x)));
                        ffDesc.setDecPositions(Integer.toString(rsmd.getScale(x)));
                        switch(rsmd.getColumnType(x)) {
                            case 2:
                                ffDesc.setFieldType("S");
                                break;
                            case 3:
                                ffDesc.setFieldType("P");
                                break;
                            default:
                                ffDesc.setFieldType(" ");
                        }
                        ffDesc.setFieldText("");
                        ffDesc.setWriteField(true);
                        ffd.add(ffDesc);
                    }
                    writeHeader(localFileF);
                    int processed = 0;
                    StringBuffer rb = new StringBuffer();
                    while (rs.next() && !aborted) {
                        for (int x = 1; x <= numCols; x++) {
                            ((FileFieldDef) ffd.get(x - 1)).setFieldData(rs.getString(x));
                        }
                        status.setCurrentRecord(processed++);
                        status.setFileLength(processed + 1);
                        rb.setLength(0);
                        ofi.parseFields(null, ffd, rb);
                        fireStatusEvent();
                    }
                    printFTPInfo("Transfer Successful ");
                    status.setCurrentRecord(processed);
                    status.setFileLength(processed);
                    fireStatusEvent();
                    writeFooter();
                } catch (SQLException sqle) {
                    printFTPInfo("SQL Exception ! " + sqle.getMessage());
                } catch (FileNotFoundException fnfe) {
                    printFTPInfo("File Not found Exception ! " + fnfe.getMessage());
                } finally {
                    try {
                        if (connection != null) connection.close();
                    } catch (SQLException e) {
                    }
                    if (ffd != null) {
                        ffd.clear();
                        ffd = null;
                    }
                    System.gc();
                }
            }
        };
        getThread = new Thread(getRun);
        getThread.start();
        return flag;
    }

    /**
    * Print ftp command events and responses
    */
    private void printFTPInfo(String msgText) {
        status.setMessage(msgText);
        fireCommandEvent();
    }

    /**
    * Write the html header of the output file
    */
    private void writeHeader(String fileName) throws FileNotFoundException {
        ofi.createFileInstance(fileName);
        ofi.writeHeader(fileName, hostName, ffd, decChar);
    }

    /**
    * write the footer of the html output
    */
    private void writeFooter() {
        ofi.writeFooter(ffd);
    }
}
