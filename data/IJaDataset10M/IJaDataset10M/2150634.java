package org.datanucleus.store.rdbms;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.StoreDataManager;
import org.datanucleus.store.rdbms.table.ClassTable;
import org.datanucleus.store.rdbms.table.ClassView;
import org.datanucleus.store.rdbms.table.JoinTable;
import org.datanucleus.store.rdbms.table.TableImpl;
import org.datanucleus.store.rdbms.table.ViewImpl;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.StringUtils;

/**
 * Schema transaction for deleting all known tables.
 */
public class DeleteTablesSchemaTransaction extends AbstractSchemaTransaction {

    /** Localiser for messages. */
    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.store.rdbms.Localisation", RDBMSStoreManager.class.getClassLoader());

    StoreDataManager storeDataMgr = null;

    Writer writer;

    /**
     * @param rdbmsMgr
     * @param isolationLevel
     */
    public DeleteTablesSchemaTransaction(RDBMSStoreManager rdbmsMgr, int isolationLevel, StoreDataManager dataMgr) {
        super(rdbmsMgr, isolationLevel);
        this.storeDataMgr = dataMgr;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    protected void run(ClassLoaderResolver clr) throws SQLException {
        synchronized (rdbmsMgr) {
            boolean success = true;
            try {
                NucleusLogger.DATASTORE_SCHEMA.debug(LOCALISER.msg("050045", rdbmsMgr.getCatalogName(), rdbmsMgr.getSchemaName()));
                Map baseTablesByName = new HashMap();
                Map viewsByName = new HashMap();
                for (Iterator i = storeDataMgr.getManagedStoreData().iterator(); i.hasNext(); ) {
                    RDBMSStoreData data = (RDBMSStoreData) i.next();
                    if (NucleusLogger.DATASTORE_SCHEMA.isDebugEnabled()) {
                        NucleusLogger.DATASTORE_SCHEMA.debug(LOCALISER.msg("050046", data.getName()));
                    }
                    if (data.hasTable()) {
                        if (data.mapsToView()) {
                            viewsByName.put(data.getDatastoreIdentifier(), data.getDatastoreContainerObject());
                        } else {
                            baseTablesByName.put(data.getDatastoreIdentifier(), data.getDatastoreContainerObject());
                        }
                    }
                }
                Iterator viewsIter = viewsByName.values().iterator();
                while (viewsIter.hasNext()) {
                    ViewImpl view = (ViewImpl) viewsIter.next();
                    if (writer != null) {
                        try {
                            if (view instanceof ClassView) {
                                writer.write("-- ClassView " + view.toString() + " for classes " + StringUtils.objectArrayToString(((ClassView) view).getManagedClasses()) + "\n");
                            }
                        } catch (IOException ioe) {
                            NucleusLogger.DATASTORE_SCHEMA.error("error writing DDL into file", ioe);
                        }
                    }
                    ((ViewImpl) viewsIter.next()).drop(getCurrentConnection());
                }
                Iterator tablesIter = baseTablesByName.values().iterator();
                while (tablesIter.hasNext()) {
                    TableImpl tbl = (TableImpl) tablesIter.next();
                    if (writer != null) {
                        try {
                            if (tbl instanceof ClassTable) {
                                writer.write("-- Constraints for ClassTable " + tbl.toString() + " for classes " + StringUtils.objectArrayToString(((ClassTable) tbl).getManagedClasses()) + "\n");
                            } else if (tbl instanceof JoinTable) {
                                writer.write("-- Constraints for JoinTable " + tbl.toString() + " for join relationship\n");
                            }
                        } catch (IOException ioe) {
                            NucleusLogger.DATASTORE_SCHEMA.error("error writing DDL into file", ioe);
                        }
                    }
                    tbl.dropConstraints(getCurrentConnection());
                }
                tablesIter = baseTablesByName.values().iterator();
                while (tablesIter.hasNext()) {
                    TableImpl tbl = (TableImpl) tablesIter.next();
                    if (writer != null) {
                        try {
                            if (tbl instanceof ClassTable) {
                                writer.write("-- ClassTable " + tbl.toString() + " for classes " + StringUtils.objectArrayToString(((ClassTable) tbl).getManagedClasses()) + "\n");
                            } else if (tbl instanceof JoinTable) {
                                writer.write("-- JoinTable " + tbl.toString() + " for join relationship\n");
                            }
                        } catch (IOException ioe) {
                            NucleusLogger.DATASTORE_SCHEMA.error("error writing DDL into file", ioe);
                        }
                    }
                    tbl.drop(getCurrentConnection());
                }
            } catch (Exception e) {
                success = false;
                String errorMsg = LOCALISER.msg("050047", e);
                NucleusLogger.DATASTORE_SCHEMA.error(errorMsg);
                throw new NucleusUserException(errorMsg, e);
            }
            if (!success) {
                throw new NucleusException("DeleteTables operation failed");
            }
        }
    }

    public String toString() {
        return LOCALISER.msg("050045", rdbmsMgr.getCatalogName(), rdbmsMgr.getSchemaName());
    }
}
