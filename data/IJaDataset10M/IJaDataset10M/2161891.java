package dk.kapetanovic.jaft.action;

import java.io.File;
import java.io.IOException;
import dk.kapetanovic.jaft.action.io.FileHandler;
import dk.kapetanovic.jaft.transaction.Transaction;

public class DeleteAction extends NullAction {

    private File original;

    private File backup;

    private boolean isFile;

    private FileHandler handler;

    private transient Transaction transaction;

    public DeleteAction(File original, Transaction transaction) {
        this.original = original;
        isFile = original.isFile();
        handler = new FileHandler();
        setTransaction(transaction);
    }

    @Override
    public void cleanup() throws IOException {
        if (backup.exists()) handler.delete(backup);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void prepare() throws IOException {
        backup = handler.generateBackup(original);
        System.out.println("Prepare, backup=" + backup);
        if (backup.exists()) throw new IOException("Backup file already exists: " + backup.getAbsolutePath());
        if (isFile) handler.rename(original, backup); else handler.delete(original);
    }

    @Override
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        handler.setBackupDir(transaction.getTransactionManager().getBackupDir());
    }

    @Override
    public void undo() throws IOException {
        if (!original.exists()) {
            if (isFile) handler.restoreBackup(original, backup); else if (!original.mkdir()) throw new IOException("Could not undo deletion of dir: " + original.getAbsolutePath());
        }
    }

    @Override
    public String toString() {
        return "DeleteAction [original=" + original + ", backup=" + backup + "]";
    }
}
