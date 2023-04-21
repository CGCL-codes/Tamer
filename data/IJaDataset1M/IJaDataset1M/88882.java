package org.plazmaforge.studio.dbmanager.actions;

import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.plazmaforge.studio.dbconnector.DBConnector;
import org.plazmaforge.studio.dbconnector.dbstructure.IDBColumn;
import org.plazmaforge.studio.dbconnector.dbstructure.IDBTable;
import org.plazmaforge.studio.dbconnector.dialects.SQLScriptGenerator;
import org.plazmaforge.studio.dbconnector.model.DataStorage;
import org.plazmaforge.studio.dbconnector.model.IAlias;
import org.plazmaforge.studio.dbconnector.model.ISession;
import org.plazmaforge.studio.dbmanager.dialogs.DBStructureCompareDialog;

/** 
 * @author Oleh Hapon
 * $Id: DBStructureCompareAction.java,v 1.23 2010/05/09 10:30:53 ohapon Exp $
 */
public class DBStructureCompareAction extends AbstractScriptAction {

    private DataStorage dataStorage;

    private ISession session1;

    private ISession session2;

    public DBStructureCompareAction() {
        super();
        setText("Compare DB structure");
        setToolTipText("Compare DB structure");
    }

    public void run() {
        DBStructureCompareDialog dialog = new DBStructureCompareDialog(getShell());
        if (dialog.open() != Window.OK) {
            return;
        }
        IAlias alias1 = dialog.getAlias1();
        IAlias alias2 = dialog.getAlias2();
        if (alias1 == null || alias2 == null) {
            return;
        }
        dataStorage = DBConnector.getDataStorage();
        try {
            CompareJob job = new CompareJob("Creating...", alias1, alias2);
            job.schedule();
        } catch (Exception ex) {
            handleError("Creation SQL script error", ex);
        }
    }

    private boolean hasElement(String[] array, String element) {
        if (array == null || element == null) {
            return false;
        }
        for (String s : array) {
            if (element.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasElement(List<IDBColumn> array, IDBColumn element) {
        if (array == null || element == null) {
            return false;
        }
        String columnName = element.getName();
        if (columnName == null) {
            return false;
        }
        String name = null;
        for (IDBColumn s : array) {
            name = s.getName();
            if (name == null) {
                continue;
            }
            if (name.equals(columnName)) {
                return true;
            }
        }
        return false;
    }

    private class CompareJob extends Job {

        private IAlias alias1;

        private IAlias alias2;

        public CompareJob(String name, IAlias alias1, IAlias alias2) {
            super(name);
            this.alias1 = alias1;
            this.alias2 = alias2;
        }

        protected IStatus run(IProgressMonitor progressMonitor) {
            try {
                session1 = dataStorage.openAnonymousSession(alias1);
                session2 = dataStorage.openAnonymousSession(alias2);
                catalog = "";
                schema = "";
                isUseQuote = false;
                generator = new SQLScriptGenerator();
                generator.update(session2.getSQLDialect());
                generator.update(session2.getConnection(), isUseQuote);
            } catch (Exception ex) {
                ex.printStackTrace();
                return Status.CANCEL_STATUS;
            }
            String[] tableTypes = new String[] { "TABLE" };
            String[] tableNames1 = session1.getDBCService().getTableNames(tableTypes);
            String[] tableNames2 = session2.getDBCService().getTableNames(tableTypes);
            final StringBuffer buf = new StringBuffer();
            int tableCount = tableNames2 == null ? 0 : tableNames2.length;
            progressMonitor.beginTask("Comparing tables (" + tableCount + ")", tableCount);
            IStatus status = tableCompare(progressMonitor, buf, tableNames1, tableNames2);
            if (Status.CANCEL_STATUS == status) {
                return status;
            }
            Display display = Display.getDefault();
            display.asyncExec(new Runnable() {

                public void run() {
                    try {
                        String sql = buf.toString();
                        openSQLEditor(session2, sql);
                    } finally {
                        closeAllSessions();
                    }
                }
            });
            progressMonitor.done();
            return Status.OK_STATUS;
        }
    }

    private IStatus tableCompare(IProgressMonitor progressMonitor, StringBuffer buf, String[] tableNames1, String[] tableNames2) {
        if (progressMonitor.isCanceled()) {
            return Status.CANCEL_STATUS;
        }
        buf.append("\n/*===========================================================================*/");
        buf.append("\n/* DB COMPARE                                                                */");
        buf.append("\n/*===========================================================================*/");
        boolean noCompare = false;
        boolean isCascadeConstraints = false;
        for (String t : tableNames1) {
            if (!hasElement(tableNames2, t)) {
                noCompare = true;
                generator.generateDropTable(buf, catalog, schema, t, isCascadeConstraints);
            }
        }
        int i = 0;
        for (String t : tableNames2) {
            i++;
            if (progressMonitor.isCanceled()) {
                return Status.CANCEL_STATUS;
            }
            progressMonitor.subTask("Analyze table (" + i + ") " + t);
            if (!hasElement(tableNames1, t)) {
                noCompare = true;
                IDBTable table = session2.getDBCService().getTable("TABLE", catalog, schema, t);
                generator.generateCreateTable(buf, table);
            } else {
                IDBTable table1 = session1.getDBCService().getTable("TABLE", catalog, schema, t);
                IDBTable table2 = session2.getDBCService().getTable("TABLE", catalog, schema, t);
                List<IDBColumn> columns1 = table1.getColumns();
                List<IDBColumn> columns2 = table2.getColumns();
                if (columns1 != null) {
                    for (IDBColumn c : columns1) {
                        if (!hasElement(columns2, c)) {
                            noCompare = true;
                            generator.generateDropColumn(buf, table1, c);
                        }
                    }
                }
                if (columns2 != null) {
                    for (IDBColumn c : columns2) {
                        if (!hasElement(columns1, c)) {
                            noCompare = true;
                            generator.generateAddColumn(buf, table1, c);
                        }
                    }
                }
            }
            progressMonitor.worked(1);
        }
        if (!noCompare) {
            MessageDialog.openInformation(getShell(), "Info", "Structures of databases are identical");
        }
        if (progressMonitor.isCanceled()) {
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }

    private void closeAllSessions() {
        closeSession(session1);
        closeSession(session2);
    }

    private void closeSession(ISession session) {
        if (session != null) {
            try {
                dataStorage.closeAnonymousSession(session);
            } catch (Throwable e) {
                logError(e);
            }
        }
    }
}
