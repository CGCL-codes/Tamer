package net.sourceforge.seqware.queryengine.backend.util;

import java.util.Iterator;
import net.sourceforge.seqware.queryengine.backend.io.berkeleydb.tuplebinders.ConsequenceTB;
import net.sourceforge.seqware.queryengine.backend.io.berkeleydb.tuplebinders.MismatchTB;
import net.sourceforge.seqware.queryengine.backend.model.Consequence;
import net.sourceforge.seqware.queryengine.backend.model.Mismatch;
import net.sourceforge.seqware.queryengine.backend.model.ContigPosition;
import net.sourceforge.seqware.queryengine.backend.util.SeqWareIterator;
import com.sleepycat.db.Cursor;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.LockMode;
import com.sleepycat.db.OperationStatus;
import com.sleepycat.db.SecondaryCursor;

/**
 * @author boconnor
 *
 */
public class ConsequenceCursorIterator implements SeqWareIterator {

    private Cursor cursor = null;

    private boolean hasNext = false;

    DatabaseEntry key = new DatabaseEntry();

    DatabaseEntry value = new DatabaseEntry();

    Consequence consequence = new Consequence();

    ConsequenceTB ctb = new ConsequenceTB();

    public ConsequenceCursorIterator(Cursor cursor, DatabaseEntry searchKey) throws Exception {
        super();
        this.cursor = cursor;
        this.key = searchKey;
        OperationStatus status = this.cursor.getFirst(key, value, LockMode.DEFAULT);
        if (status == OperationStatus.SUCCESS) {
            hasNext = true;
        }
    }

    public boolean hasNext() {
        return (hasNext);
    }

    public int getCount() throws Exception {
        return (cursor.count());
    }

    public Object next() {
        if (hasNext) {
            consequence = (Consequence) ctb.entryToObject(value);
        }
        try {
            OperationStatus status = cursor.getNextDup(key, value, null);
            if (status != OperationStatus.SUCCESS) {
                status = cursor.getNext(key, value, null);
            }
            if (status != OperationStatus.SUCCESS) {
                cursor.close();
                hasNext = false;
            }
        } catch (Exception e) {
            return (null);
        }
        return (consequence);
    }

    public void close() throws Exception {
        cursor.close();
    }

    public void remove() {
    }
}
