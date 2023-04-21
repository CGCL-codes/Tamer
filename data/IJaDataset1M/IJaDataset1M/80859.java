package org.exist.storage.index;

import java.nio.ByteBuffer;
import org.exist.storage.DBBroker;
import org.exist.storage.journal.LogException;
import org.exist.storage.txn.Txn;

/**
 * @author wolf
 *
 */
public class CreatePageLoggable extends AbstractBFileLoggable {

    protected long newPage;

    /**
     * 
     * 
     * @param transaction 
     * @param fileId 
     * @param newPage 
     */
    public CreatePageLoggable(Txn transaction, byte fileId, long newPage) {
        super(BFile.LOG_CREATE_PAGE, fileId, transaction);
        this.newPage = newPage;
    }

    public CreatePageLoggable(DBBroker broker, long transactionId) {
        super(broker, transactionId);
    }

    @Override
    public void write(ByteBuffer out) {
        super.write(out);
        out.putInt((int) newPage);
    }

    @Override
    public void read(ByteBuffer in) {
        super.read(in);
        newPage = in.getInt();
    }

    @Override
    public int getLogSize() {
        return super.getLogSize() + 4;
    }

    @Override
    public void redo() throws LogException {
        getIndexFile().redoCreatePage(this);
    }

    @Override
    public void undo() throws LogException {
        getIndexFile().undoCreatePage(this);
    }

    @Override
    public String dump() {
        return super.dump() + " - create new page " + newPage;
    }
}
