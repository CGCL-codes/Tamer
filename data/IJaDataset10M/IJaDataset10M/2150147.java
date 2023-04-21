package jdbm.btree;

import java.io.IOException;
import jdbm.RecordManager;
import jdbm.recman.TestCaseWithTestFile;

/**
 * Contributed test case for BTree by Christof Dallermassl (cdaller iicm.edu):
 *
 * -= quote from original message posted on jdbm-general =-
 * <pre>
 *
 * I tried to insert a couple of elements into a BTree and then remove
 * them one by one. After a number or removals, there is always (if more
 * than 20 elements in btree) a java.io.StreamCorruptedException thrown.
 *
 * The strange thing is, that on 50 elements, the exception is thrown
 * after removing 22, on 200 it is thrown after 36, on 1000 it is thrown
 * after 104, on 10000 it is thrown after 1003....
 *
 * The full stackTrace is here:
 * ---------------------- snip ------- snap -------------------------
 * java.io.StreamCorruptedException: Caught EOFException while reading the
 * stream header
 *   at java.io.ObjectInputStream.readStreamHeader(ObjectInputStream.java:845)
 *   at java.io.ObjectInputStream.<init>(ObjectInputStream.java:168)
 *   at jdbm.recman.RecordManager.byteArrayToObject(RecordManager.java:296)
 *   at jdbm.recman.RecordManager.fetchObject(RecordManager.java:239)
 *   at jdbm.helper.ObjectCache.fetchObject(ObjectCache.java:104)
 *   at jdbm.btree.BPage.loadBPage(BPage.java:670)
 *   at jdbm.btree.BPage.remove(BPage.java:492)
 *   at jdbm.btree.BPage.remove(BPage.java:437)
 *   at jdbm.btree.BTree.remove(BTree.java:313)
 *   at JDBMTest.main(JDBMTest.java:41)
 *
 * </pre>
 *
 *  @author <a href="mailto:cdaller iicm.edu">Christof Dallermassl</a>
 *  @version $Id: StreamCorrupted.java,v 1.4 2003/09/21 15:49:02 boisvert Exp $
 */
public class StreamCorrupted extends TestCaseWithTestFile {

    /**
     *  Basic tests
     */
    public void testStreamCorrupted() throws IOException {
        RecordManager recman;
        BTree btree;
        int iterations;
        iterations = 100;
        recman = newRecordManager();
        btree = BTree.createInstance(recman);
        recman.setNamedObject("testbtree", btree.getRecid());
        for (int count = 0; count < iterations; count++) {
            btree.insert("num" + count, new Integer(count), true);
        }
        for (int count = 0; count < iterations; count++) {
            btree.remove("num" + count);
        }
        recman.close();
        recman = null;
    }
}
