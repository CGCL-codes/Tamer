package org.plazmaforge.studio.dbmanager.actions;

import org.plazmaforge.framework.action.IProcess;
import org.plazmaforge.framework.core.FileFormat;
import org.plazmaforge.framework.core.exception.ApplicationException;
import org.plazmaforge.framework.datawarehouse.ITransferService;
import org.plazmaforge.framework.datawarehouse.convert.dataimport.IImporter;
import org.plazmaforge.framework.client.swt.dialogs.dataimport.AbstractImportDialog;

public class DBImportAction extends AbstractImportAction {

    private static final String[] EXPORT_TYPES = { FileFormat.CSV, FileFormat.XML };

    public DBImportAction() {
        super();
        setText("Import...");
        setToolTipText("Import");
    }

    protected IProcess createProcess(String type) throws ApplicationException {
        final IImporter importer = createImporter(type, getShell());
        if (importer == null) {
            return null;
        }
        IProcess process = new IProcess() {

            public void run() throws ApplicationException {
                importer.execute();
            }
        };
        return process;
    }

    public void run() {
        if (!initContext()) {
            return;
        }
        super.run();
    }

    protected String getType() {
        return openSelectionFormatDialog(EXPORT_TYPES);
    }

    protected String getNormalizeFormat(String format) {
        return "DATABASE." + format;
    }

    protected void initDialog(final AbstractImportDialog dialog) throws ApplicationException {
        super.initDialog(dialog);
        dialog.setTransferService(getTransferService());
    }

    protected void initImporter(final AbstractImportDialog dialog, final IImporter importer) throws ApplicationException {
        super.initImporter(dialog, importer);
        importer.setTransferService(getTransferService());
    }

    protected ITransferService getTransferService() {
        return new TransferServiceImpl();
    }
}
