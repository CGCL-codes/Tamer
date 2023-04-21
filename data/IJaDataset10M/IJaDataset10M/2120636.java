package de.icehorsetools.iceoffice.workflow.test;

import java.io.File;
import java.text.MessageFormat;
import org.apache.log4j.Logger;
import org.ugat.wiser.dialog.MessageBoxHandler;
import org.ugat.wiser.interfaces.IUnTransactionCommand;
import org.ugat.wiser.io.UploadManager;
import org.ugat.wiser.language.Lang;
import org.ugat.wiser.listener.TransactionCommandExecutor;
import org.ugat.wiser.workflow.implementations.ADefaultStatelessWorkflow;
import de.icehorsetools.dataImport.ImportTests;
import de.icehorsetools.exeption.InvalidImportCsvFormatException;
import de.icehorsetools.iceoffice.workflow.testsection.TestsectionImportWf;
import de.ug2t.kernel.interfaces.IKeExecutable;
import de.ug2t.unifiedGui.UnComponent;
import de.ug2t.unifiedGui.interfaces.IUnGuiEventListener;

/**
 * @author tkr
 * @version $Id$
 */
public class TestImportWf extends ADefaultStatelessWorkflow {

    private static Logger logger = Logger.getLogger(TestImportWf.class);

    private File upload = null;

    @Override
    public boolean work() {
        UploadManager.requestUpload(new UploadReceiver());
        return false;
    }

    public class UploadReceiver implements IKeExecutable, IUnTransactionCommand {

        public Object execObj(Object arg0) {
            TransactionCommandExecutor.run(TestImportWf.this.getUnitOfWork(), this, arg0);
            return null;
        }

        public void executeInTransaction(Object... args) {
            MessageBoxHandler mbh = new MessageBoxHandler();
            File upload = (File) args[0];
            TestImportWf.this.upload = upload;
            mbh.showYesNo(Lang.get(this.getClass(), "title"), MessageFormat.format(Lang.get(this.getClass(), "messsage"), new Object[] { upload.getName() }), new YesListener(), new NoListener(), 500, 250);
        }
    }

    private class YesListener implements IUnGuiEventListener, IUnTransactionCommand {

        @Override
        public void execListener(UnComponent arg0) throws Exception {
            TransactionCommandExecutor.run(TestImportWf.this.getUnitOfWork(), this, arg0);
        }

        @Override
        public void executeInTransaction(Object... args) {
            ImportTests imp = new ImportTests();
            imp.setCsvFile(upload.getAbsolutePath());
            logger.debug(Lang.get(TestImportWf.class, "import_initialized"));
            try {
                imp.startImport();
            } catch (InvalidImportCsvFormatException e) {
                new MessageBoxHandler().showError("title", e.getMessage(), null, 500, 250);
            }
            logger.debug(Lang.get(TestsectionImportWf.class, "import_completed"));
            if (TestImportWf.this.save()) {
                TestImportWf.this.close();
            }
        }
    }

    private class NoListener implements IUnGuiEventListener, IUnTransactionCommand {

        @Override
        public void execListener(UnComponent arg0) throws Exception {
            TransactionCommandExecutor.run(TestImportWf.this.getUnitOfWork(), this, arg0);
        }

        @Override
        public void executeInTransaction(Object... args) {
            new MessageBoxHandler().showInfo(Lang.get(TestImportWf.class, "title"), Lang.get(TestImportWf.class, "import_aborted"), null, 500, 250);
            TestImportWf.this.cancel();
        }
    }
}
