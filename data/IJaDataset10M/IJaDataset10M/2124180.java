package net.sourceforge.cobertura.runtime.midp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import net.sourceforge.cobertura.runtime.AbstractCoverageDataPersistence;
import net.sourceforge.cobertura.runtime.CoverageDataPersistence;
import net.sourceforge.cobertura.runtime.Factory;
import net.sourceforge.cobertura.runtime.HasBeenInstrumented;

/**
 * <p>
 * This class implements HasBeenInstrumented so that when cobertura
 * instruments itself, it will omit this class.  It does this to
 * avoid an infinite recursion problem because instrumented classes
 * make use of this class.
 * </p>
 * 
 * 
 * FIXME if the jsr75 FileConnection is instrumented we have a problem as initialisation
 * makes use of the api, which in turn tries to log data
 */
public class MIDPCoverageDataPersistence extends AbstractCoverageDataPersistence implements HasBeenInstrumented {

    /**
	 * The file connection url to the instrumentation data file
	 */
    private static final String URL = "file:///" + getPath();

    public synchronized Hashtable load() {
        Hashtable table = null;
        try {
            FileConnection conn = (FileConnection) Connector.open(URL, Connector.READ_WRITE);
            Factory.trace(Factory.TRACE_BASIC, "Loading data from " + conn.getName());
            try {
                if (conn.exists()) {
                    InputStream is = conn.openInputStream();
                    table = load(is);
                }
            } finally {
                conn.close();
            }
            Factory.trace(Factory.TRACE_BASIC, "Data loaded!!");
        } catch (Exception e) {
            Factory.trace(Factory.TRACE_BASIC, e);
        }
        return table;
    }

    public synchronized void save() {
        try {
            if (!isDirty()) {
                Factory.trace(Factory.TRACE_BASIC, "Save -> Data up-to-date");
                return;
            }
            FileConnection conn = (FileConnection) Connector.open(URL, Connector.READ_WRITE);
            Factory.trace(Factory.TRACE_BASIC, "Saving data to " + conn.getName());
            try {
                if (!conn.exists()) {
                    conn.create();
                }
                OutputStream os = conn.openOutputStream();
                save(os);
            } finally {
                conn.close();
            }
            Factory.trace(Factory.TRACE_BASIC, "Data saved!!");
        } catch (Exception e) {
            Factory.trace(Factory.TRACE_BASIC, e);
        }
    }

    /**
	 * Gets the instrumentation data file path. 
	 * 
	 * @return String
	 */
    private static String getPath() {
        try {
            InputStream in = MIDPCoverageDataPersistence.class.getResourceAsStream("path.txt");
            if (in != null) {
                StringBuffer path = new StringBuffer(50);
                byte[] buffer = new byte[100];
                int read = 0;
                while ((read = in.read(buffer)) != -1) {
                    path.append(new String(buffer, 0, read));
                }
                if (path.charAt(path.length() - 1) != '/') {
                    path.append('/');
                }
                path.append(CoverageDataPersistence.FILE_NAME);
                in.close();
                Factory.trace(Factory.TRACE_BASIC, "Path to instrumentation data file is " + path.toString());
                return path.toString();
            } else {
                Factory.trace(Factory.TRACE_BASIC, "path.txt no found ");
            }
        } catch (IOException e) {
            Factory.trace(Factory.TRACE_BASIC, "Error getting the instrumentation data file path");
        }
        return "";
    }
}
