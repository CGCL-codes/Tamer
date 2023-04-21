package org.jz.xs.frames;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import javax.swing.JInternalFrame;
import org.jz.xs.entity.EntityMetadata;
import org.jz.xs.jdbc.JdbcConnector;
import org.jz.xs.jdbc.JdbcInstance;
import org.jz.xs.jdbc.JdbcMetadata;
import org.jz.xs.persistence.StorageConnector;

/**
 *
 * @author GZSVM
 */
public abstract class JdbcInternalFrame extends JInternalFrame {

    private boolean fInitialized;

    private Vector<String> fKeyFieldNames;

    private String fRelationName;

    private JdbcKernel fKernel;

    public JdbcInternalFrame() {
        super();
        fInitialized = false;
    }

    public abstract void setUpTables();

    public void initialize(JdbcKernel _Kernel) throws Exception {
        fKeyFieldNames = new Vector<String>();
        setUpTables();
        fKernel = _Kernel;
        fInitialized = true;
    }

    protected void addKeyFieldName(String _NewKeyFieldName) {
        fKeyFieldNames.add(_NewKeyFieldName.toLowerCase());
    }

    protected void setRelationName(String _NewRelationName) {
        fRelationName = _NewRelationName;
    }

    protected String getRelationName() {
        return fRelationName;
    }

    protected List<String> getKeyFieldNames() {
        return fKeyFieldNames;
    }

    protected JdbcConnector getStorageConnector() {
        return fKernel.getStorageConnector();
    }

    protected JdbcKernel getKernel() {
        return fKernel;
    }

    protected String loadSqlFromResource(String _ResourceName) throws Exception {
        String res = "";
        InputStream is = this.getClass().getResourceAsStream(_ResourceName);
        while (is.available() > 0) {
            res = res + (char) is.read();
        }
        return res;
    }
}
