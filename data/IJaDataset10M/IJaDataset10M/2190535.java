package test.capeline.transaction;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.Query;
import org.capelin.core.models.CapelinRecord;
import org.capelin.transaction.TxSpringContext;
import org.capelin.transaction.dao.PagedRecord;
import org.capelin.transaction.dao.RecordDao;
import sample.capelin.transaction.SampleTxRecord;

/**
 * 
 * <a href="http://code.google.com/p/capline-opac/">Capelin-opac</a>
 * License: GNU AGPL v3 | http://www.gnu.org/licenses/agpl.html
 * 
 * 
 * @author Jing Xiao <jing.xiao.ca at gmail dot com
 * 
 */
public class DbAllTest extends TestCase {

    private RecordDao dao;

    private static final Log log = LogFactory.getLog(DbAllTest.class);

    private List<Integer> ids = new ArrayList<Integer>();

    private List<String> titles = new ArrayList<String>();

    @Override
    protected void setUp() {
        dao = (RecordDao) TxSpringContext.getBeanFactory().getBean("sampleDao");
        setUpData();
    }

    private void setUpData() {
        SampleTxRecord record1 = new SampleTxRecord();
        record1.setTitle("A far Book");
        record1.setAuthor("Amanda A\nLarsen B");
        record1.setSubject("FAR\nBOOK");
        dao.saveRecord(record1);
        ids.add(record1.getId());
        titles.add(record1.getTitle());
        record1.setId(0);
        record1.setTitle("A not far Book");
        record1.setAuthor("Bob A\nLarsen B");
        record1.setSubject("Far\nBook");
        dao.saveRecord(record1);
        ids.add(record1.getId());
        titles.add(record1.getTitle());
        record1.setId(0);
        record1.setTitle("A close Book");
        record1.setAuthor("Arnason A\nLarsen B");
        record1.setSubject("CLOSE\nBook");
        dao.saveRecord(record1);
        ids.add(record1.getId());
        titles.add(record1.getTitle());
        record1.setId(0);
        record1.setTitle("Boys never stop");
        record1.setAuthor("Boy A");
        record1.setSubject("BOYS");
        dao.saveRecord(record1);
        ids.add(record1.getId());
        titles.add(record1.getTitle());
        record1.setId(0);
        record1.setTitle("Boys may stop");
        record1.setAuthor("Boy B");
        record1.setSubject("BOYS\nBOOK");
        dao.saveRecord(record1);
        ids.add(record1.getId());
        titles.add(record1.getTitle());
        record1.setId(0);
        record1.setTitle("Twilight X");
        record1.setAuthor("Atephenie M");
        dao.saveRecord(record1);
        ids.add(record1.getId());
        titles.add(record1.getTitle());
    }

    @Override
    protected void tearDown() {
        for (int id : ids) {
            dao.deleteRecord(id);
        }
    }

    @SuppressWarnings("unchecked")
    public void _testList() {
        List<SampleTxRecord> l = (List<SampleTxRecord>) dao.listPaged("title", "a%", 1, true).getResultList();
        assertEquals(3, l.size());
        assertEquals(titles.get(0), l.get(1).getTitle());
        assertEquals(titles.get(1), l.get(2).getTitle());
        assertEquals(titles.get(2), l.get(0).getTitle());
        l = (List<SampleTxRecord>) dao.listPaged("title", "b%", 1, true).getResultList();
        assertEquals(2, l.size());
        assertEquals(titles.get(3), l.get(1).getTitle());
        assertEquals(titles.get(4), l.get(0).getTitle());
        l = (List<SampleTxRecord>) dao.listPaged("author", "A%", 1, true).getResultList();
        assertEquals(3, l.size());
        assertEquals(titles.get(0), l.get(0).getTitle());
        assertEquals(titles.get(2), l.get(1).getTitle());
        assertEquals(titles.get(5), l.get(2).getTitle());
        dao.setPageSize(2);
        l = (List<SampleTxRecord>) dao.listPaged("author", "A%", 1, true).getResultList();
        assertEquals(titles.get(0), l.get(0).getTitle());
        assertEquals(titles.get(2), l.get(1).getTitle());
        l = (List<SampleTxRecord>) dao.listPaged("author", "A%", 2, true).getResultList();
        assertEquals(1, l.size());
        assertEquals(titles.get(5), l.get(0).getTitle());
    }

    public void _testGetIds() {
        for (Integer i : ids) {
            System.out.print(i + " ");
        }
        List<Integer> mids = ids.subList(1, 4);
        @SuppressWarnings("rawtypes") List l = dao.getRecords(mids);
        assertEquals(3, l.size());
    }

    public void _testGetWithOthers() {
        Query q = TestUtils.buildQuery("title", "book");
        PagedRecord p = dao.getRecordWithOthers(2, q, null, null, 1);
        int[] ids = new int[] { 1, 2, 3 };
        List<Integer> list = p.getResultIds();
        assertEquals(ids.length, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(ids[i], list.get(i).intValue());
        }
        assertEquals("A not far Book", p.getCapelinRecord().getTitle());
        assertEquals(1, p.getPosition());
    }

    public void testGetResultTerms() {
        String term = "title";
        Query q = TestUtils.buildQuery(term, "book");
        List<String> result = dao.getResultTerms(term, q);
    }
}
