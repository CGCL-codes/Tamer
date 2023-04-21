package net.sourceforge.cobertura.runtime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Base class for all implementations of <code>CoverageDataPersistence</code>
 */
public abstract class AbstractCoverageDataPersistence implements HasBeenInstrumented, CoverageDataPersistence {

    /**
     * This stores the CoverageData for each class
     */
    private Hashtable data;

    /**
     * Indicates whether the runtime data is in sync with
     * the persistence one. This allows us to not save
     * (which is time consuming on device) if not necessary. 
     */
    private boolean dirty;

    /**
	 * Default constructor
	 */
    protected AbstractCoverageDataPersistence() {
        data = new Hashtable(20);
        setDirty(true);
    }

    public Hashtable load(InputStream is) {
        if (is == null) {
            throw new NullPointerException();
        }
        Factory.trace(Factory.TRACE_EXTENDED, "Loading coverage data");
        Hashtable table = null;
        try {
            DataInputStream dis = new DataInputStream(is);
            int size = dis.readInt();
            table = new Hashtable(size);
            for (int i = 0; i < size; i++) {
                String className = dis.readUTF();
                CoverageDataImpl coverageData = new CoverageDataImpl();
                Factory.trace(Factory.TRACE_COVERAGE_DATA, "Resurrecting '" + className + "' ...");
                coverageData.resurrect(dis);
                table.put(className, coverageData);
            }
        } catch (Exception e) {
            Factory.trace(Factory.TRACE_COVERAGE_DATA, e);
        }
        return table;
    }

    public synchronized void merge(Hashtable table) {
        if (table == null) return;
        Enumeration keys = table.keys();
        if (keys.hasMoreElements()) {
            setDirty(true);
        }
        while (keys.hasMoreElements()) {
            String className = (String) keys.nextElement();
            if (data.containsKey(className)) {
                getCoverageData(className).merge((CoverageData) table.get(className));
            } else {
                data.put(className, table.get(className));
            }
        }
    }

    public synchronized boolean isDirty() {
        return dirty;
    }

    /**
     * Sets the dirty flag
     * 
     * @param dirty true if dirty, false otherwise
     */
    protected synchronized void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public synchronized void save(OutputStream os) {
        try {
            if (!isDirty()) {
                return;
            }
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(data.size());
            Enumeration keys = data.keys();
            while (keys.hasMoreElements()) {
                String className = (String) keys.nextElement();
                dos.writeUTF(className);
                Factory.trace(Factory.TRACE_COVERAGE_DATA, "Persisting '" + className + "'...");
                ((CoverageDataImpl) data.get(className)).persist(dos);
            }
            dos.flush();
            setDirty(false);
        } catch (Exception e) {
            Factory.trace(Factory.TRACE_COVERAGE_DATA, e);
        }
    }

    public synchronized CoverageData getCoverageData(String classname) {
        setDirty(true);
        return (CoverageData) data.get(classname);
    }

    public synchronized CoverageData newCoverageData(String className) {
        if (!data.containsKey(className)) data.put(className, new CoverageDataImpl());
        setDirty(true);
        return (CoverageData) data.get(className);
    }

    public synchronized Hashtable getAllCoverageData() {
        return data;
    }
}
