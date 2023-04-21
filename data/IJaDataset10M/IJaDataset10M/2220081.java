package org.plantstreamer.opc2out.database;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.communications.CommunicationManager.STATUS;
import org.opcda2out.output.database.DatabaseSelectionImporter;
import org.opcda2out.output.database.nodes.PersistentTreeInfo;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.plantstreamer.Main;
import org.plantstreamer.MainWindow;
import org.plantstreamer.TagSelection;
import swingextras.gui.SwingWorkerX;

/**
 * A worker that loads the item selection information from the database
 * 
 * @author Joao Leal
 */
public class ImportSelectionWorker extends SwingWorkerX<PersistentTreeInfo, Object> {

    private static final Logger logger = Logger.getLogger(ImportSelectionWorker.class.getName(), "org/plantstreamer/i18n/logging");

    private final TagSelection tagSelection;

    private final DatabaseSelectionImporter dbSelectionImporter;

    /**
     * Creates a new ImportSelectionWorker
     * @param tableBaseName
     * @param connectionInfo
     */
    public ImportSelectionWorker(List<ConnectionInformation> connectionInfo) {
        this.dbSelectionImporter = new DatabaseSelectionImporter(Main.db, connectionInfo, Main.SCRIPTING);
        this.tagSelection = Main.mainWindow.tagSelection;
        dbSelectionImporter.setTableStructUserInteraction(new PlantstreamerTableStructureUserInteraction());
    }

    /**
     * Provides the list of OPC server connections
     *
     * @return The OPC server connection pool information
     */
    public List<ConnectionInformation> getOpcConnections() {
        return dbSelectionImporter.getOpcConnections();
    }

    @Override
    protected PersistentTreeInfo doInBackground() throws Exception {
        return dbSelectionImporter.load(true);
    }

    @Override
    protected void done2() {
        if (isCancelled()) {
            return;
        }
        if (Main.opc.getConnectionStatus() != STATUS.CONNECTED) {
            return;
        }
        PersistentTreeInfo persistInfo;
        try {
            persistInfo = get();
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return;
        } catch (ExecutionException ex) {
            Throwable th = ex.getCause() != null ? ex.getCause() : ex;
            String message = th.getLocalizedMessage();
            if (message == null || message.isEmpty()) {
                message = th.getClass().getSimpleName();
            }
            logger.log(Level.WARNING, message, ex);
            return;
        }
        if (persistInfo == null) {
            return;
        }
        if (!persistInfo.isEmpty()) {
            boolean allRootNodesExist = Main.options.autoFetchOPCTree.isSelected();
            TreeSelectionWorker worker = new TreeSelectionWorker(tagSelection, persistInfo, allRootNodesExist);
            MainWindow.getStatusBar().addWorker(worker);
            worker.executeX();
        } else {
            logger.warning("No selection available in the database for the current OPC server!");
        }
    }
}
