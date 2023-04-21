package org.jaffa.persistence.blackboxtests;

import java.util.Iterator;
import junit.framework.TestCase;
import org.jaffa.persistence.domainobjects.*;
import org.jaffa.persistence.UOW;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jaffa.persistence.Criteria;
import org.jaffa.util.StringHelper;

/** Tests for performing inserts through the Jaffa Persistence Engine.
 *
 * @author GautamJ
 */
public class PagingTest extends TestCase {

    /** Assembles and returns a test suite containing all known tests.
     * @return A test suite.
     */
    public static Test suite() {
        return new Wrapper(new TestSuite(PagingTest.class));
    }

    /** Creates new QueryTest
     * @param name The name of the test case.
     */
    public PagingTest(String name) {
        super(name);
    }

    /** Creates 95 CategoryOfInstrument records and then reads them in batces of 25.
     */
    public void testPaging() {
        UOW uow = null;
        final String prefix = "Z-PAGING-";
        final int count = 95;
        final int batch = 25;
        final int pad = ("" + count).length();
        try {
            uow = new UOW();
            createCategoryOfInstrument(uow, prefix, count);
            int loops = count / batch;
            if (count % batch > 0) ++loops;
            for (int i = 0; i < loops; i++) {
                Criteria c = new Criteria();
                c.setTable(CategoryOfInstrumentMeta.getName());
                c.addCriteria(CategoryOfInstrumentMeta.CATEGORY_INSTRUMENT, Criteria.RELATIONAL_BEGINS_WITH, prefix);
                c.addOrderBy(CategoryOfInstrumentMeta.CATEGORY_INSTRUMENT);
                c.setFirstResult(i * batch);
                c.setMaxResults(batch);
                Iterator itr = uow.query(c).iterator();
                int batchSize = (i * batch) + batch <= count ? batch : count - (i * batch);
                for (int j = 0; j < batchSize; j++) {
                    String expected = prefix + StringHelper.pad((i * batch) + j + 1, pad);
                    assertTrue("CategoryOfInstrument '" + expected + "' not found", itr.hasNext());
                    CategoryOfInstrument obj = (CategoryOfInstrument) itr.next();
                    assertEquals(expected, obj.getCategoryInstrument());
                }
                assertTrue("No more CategoryOfInstrument records should have been retrieved", !itr.hasNext());
            }
            deleteCategoryOfInstrument(uow, prefix);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            try {
                if (uow != null) uow.rollback();
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    private void createCategoryOfInstrument(UOW uow, String prefix, int count) throws Exception {
        int pad = ("" + count).length();
        for (int i = 1; i <= count; i++) {
            CategoryOfInstrument obj = (CategoryOfInstrument) uow.newPersistentInstance(CategoryOfInstrument.class);
            String ci = prefix + StringHelper.pad(i, pad);
            obj.setCategoryInstrument(ci);
            obj.setDescription("TEST PAGING");
            uow.add(obj);
        }
    }

    private void deleteCategoryOfInstrument(UOW uow, String prefix) throws Exception {
        Criteria c = new Criteria();
        c.setTable(CategoryOfInstrumentMeta.getName());
        c.addCriteria(CategoryOfInstrumentMeta.CATEGORY_INSTRUMENT, Criteria.RELATIONAL_BEGINS_WITH, prefix);
        for (Iterator i = uow.query(c).iterator(); i.hasNext(); ) uow.delete(i.next());
    }
}
