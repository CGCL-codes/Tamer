package ch.bbv.performancetests.tests;

import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import ch.bbv.application.Application;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.DataObjectId;
import ch.bbv.performancetests.list.*;

public class ListUpdateTest {

    private static final double FRAC_MODIFY = 1 / 3d;

    private static final double FRAC_REMOVE = 1 / 4d;

    private static final double FRAC_ADD = 1 / 10d;

    private static final int[] LIST_LENGTHS = { 100, 1000, 10000 };

    private Random random;

    @Before
    public void init() {
        random = new Random(System.currentTimeMillis());
    }

    @Test
    public void test() {
        Log log = LogFactory.getLog(ListUpdateTest.class);
        log.info("*** List Update Test ***");
        int nModify, nRemove, nAdd;
        DataObjectHandler dataHandler = Application.getApplication().getDataObjectMgr().getDataObjectHandler();
        String dataSource = Application.getApplication().getDataObjectMgr().getDatasource();
        for (int length : LIST_LENGTHS) {
            log.info("    Performing test with list of length " + length + ":");
            nModify = (int) Math.round(length * FRAC_MODIFY);
            nRemove = (int) Math.round(length * FRAC_REMOVE);
            nAdd = (int) Math.round(length * FRAC_ADD);
            log.info("       Creating list...");
            Master m = createList(length);
            log.info("       DONE");
            log.info("       Saving the newly created list...");
            dataHandler.store(dataSource, Master.class, m);
            log.info("       DONE");
            log.info("       modifying " + nModify + " random list entries...");
            modifyRandom(m, nModify);
            log.info("       DONE");
            log.info("       Removing " + nRemove + " random list entries...");
            removeRandom(m, nRemove);
            log.info("       DONE");
            log.info("       Adding " + nAdd + " elements to list...");
            add(m, nAdd);
            log.info("       DONE");
            log.info("       Saving the modified list...");
            dataHandler.store(dataSource, Master.class, m);
            log.info("       DONE");
        }
        log.info("*** List Update Test terminated ***");
    }

    /**
	 * Creates sample data.
	 */
    private Master createList(int length) {
        Master m = new Master();
        m.setId(DataObjectId.ILLEGAL_ID);
        Slave s;
        for (int i = 0; i < length; i++) {
            s = new Slave();
            s.setId(DataObjectId.ILLEGAL_ID);
            s.setVal(i);
            m.addHa(s);
        }
        return m;
    }

    /**
	 * Modify random entries in the list.
	 */
    public void modifyRandom(Master m, int nEntries) {
        Slave s;
        int sIdx;
        int listLength = m.getHas().size();
        for (int i = 0; i <= nEntries; i++) {
            sIdx = random.nextInt(listLength);
            s = m.getHa(sIdx);
            s.setVal(s.getVal() + 1);
        }
    }

    /**
	 * Add entries to a list.
	 */
    public void add(Master m, int nEntries) {
        Slave s;
        for (int i = 0; i <= nEntries; i++) {
            s = new Slave();
            s.setId(DataObjectId.ILLEGAL_ID);
            s.setVal(m.getHas().size());
            m.addHa(s);
        }
    }

    /**
	 * Remove random entries from a list.
	 */
    public void removeRandom(Master m, int nEntries) {
        int sIdx;
        int listLength = m.getHas().size();
        for (int i = 0; (i < nEntries) && (listLength > 0); i++) {
            sIdx = random.nextInt(listLength);
            m.removeHa(m.getHa(sIdx));
            listLength--;
        }
    }
}
