package abatortest.execute.flat.java2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import abatortest.BaseTest;
import abatortest.generated.flat.java2.dao.FieldsblobsDAO;
import abatortest.generated.flat.java2.dao.FieldsonlyDAO;
import abatortest.generated.flat.java2.dao.PkblobsDAO;
import abatortest.generated.flat.java2.dao.PkfieldsDAO;
import abatortest.generated.flat.java2.dao.PkfieldsblobsDAO;
import abatortest.generated.flat.java2.dao.PkonlyDAO;
import abatortest.generated.flat.java2.model.Fieldsblobs;
import abatortest.generated.flat.java2.model.FieldsblobsExample;
import abatortest.generated.flat.java2.model.Fieldsonly;
import abatortest.generated.flat.java2.model.FieldsonlyExample;
import abatortest.generated.flat.java2.model.Pkblobs;
import abatortest.generated.flat.java2.model.PkblobsExample;
import abatortest.generated.flat.java2.model.Pkfields;
import abatortest.generated.flat.java2.model.PkfieldsExample;
import abatortest.generated.flat.java2.model.Pkfieldsblobs;
import abatortest.generated.flat.java2.model.PkfieldsblobsExample;
import abatortest.generated.flat.java2.model.Pkonly;
import abatortest.generated.flat.java2.model.PkonlyExample;

/**
 * @author Jeff Butler
 *
 */
public class FlatJava2Tests extends BaseTest {

    protected void setUp() throws Exception {
        super.setUp();
        initDaoManager("abatortest/execute/flat/java2/dao.xml", null);
    }

    public void testFieldsOnlyInsert() {
        FieldsonlyDAO dao = (FieldsonlyDAO) daoManager.getDao(FieldsonlyDAO.class);
        Fieldsonly record = new Fieldsonly();
        record.setDoublefield(new Double(11.22));
        record.setFloatfield(new Float(33.44));
        record.setIntegerfield(new Integer(5));
        dao.insert(record);
        FieldsonlyExample example = new FieldsonlyExample();
        example.createCriteria().andIntegerfieldEqualTo(5);
        List answer = dao.selectByExample(example);
        assertEquals(1, answer.size());
        Fieldsonly returnedRecord = (Fieldsonly) answer.get(0);
        assertEquals(record.getIntegerfield(), returnedRecord.getIntegerfield());
        assertEquals(record.getDoublefield(), returnedRecord.getDoublefield());
        assertEquals(record.getFloatfield(), returnedRecord.getFloatfield());
    }

    public void testFieldsOnlySelectByExample() {
        FieldsonlyDAO dao = (FieldsonlyDAO) daoManager.getDao(FieldsonlyDAO.class);
        Fieldsonly record = new Fieldsonly();
        record.setDoublefield(new Double(11.22));
        record.setFloatfield(new Float(33.44));
        record.setIntegerfield(new Integer(5));
        dao.insert(record);
        record = new Fieldsonly();
        record.setDoublefield(new Double(44.55));
        record.setFloatfield(new Float(66.77));
        record.setIntegerfield(new Integer(8));
        dao.insert(record);
        record = new Fieldsonly();
        record.setDoublefield(new Double(88.99));
        record.setFloatfield(new Float(100.111));
        record.setIntegerfield(new Integer(9));
        dao.insert(record);
        FieldsonlyExample example = new FieldsonlyExample();
        example.createCriteria().andIntegerfieldGreaterThan(5);
        List answer = dao.selectByExample(example);
        assertEquals(2, answer.size());
        example = new FieldsonlyExample();
        answer = dao.selectByExample(example);
        assertEquals(3, answer.size());
    }

    public void testFieldsOnlySelectByExampleNoCriteria() {
        FieldsonlyDAO dao = (FieldsonlyDAO) daoManager.getDao(FieldsonlyDAO.class);
        Fieldsonly record = new Fieldsonly();
        record.setDoublefield(new Double(11.22));
        record.setFloatfield(new Float(33.44));
        record.setIntegerfield(new Integer(5));
        dao.insert(record);
        record = new Fieldsonly();
        record.setDoublefield(new Double(44.55));
        record.setFloatfield(new Float(66.77));
        record.setIntegerfield(new Integer(8));
        dao.insert(record);
        record = new Fieldsonly();
        record.setDoublefield(new Double(88.99));
        record.setFloatfield(new Float(100.111));
        record.setIntegerfield(new Integer(9));
        dao.insert(record);
        FieldsonlyExample example = new FieldsonlyExample();
        example.createCriteria();
        List answer = dao.selectByExample(example);
        assertEquals(3, answer.size());
    }

    public void testFieldsOnlyDeleteByExample() {
        FieldsonlyDAO dao = (FieldsonlyDAO) daoManager.getDao(FieldsonlyDAO.class);
        Fieldsonly record = new Fieldsonly();
        record.setDoublefield(new Double(11.22));
        record.setFloatfield(new Float(33.44));
        record.setIntegerfield(new Integer(5));
        dao.insert(record);
        record = new Fieldsonly();
        record.setDoublefield(new Double(44.55));
        record.setFloatfield(new Float(66.77));
        record.setIntegerfield(new Integer(8));
        dao.insert(record);
        record = new Fieldsonly();
        record.setDoublefield(new Double(88.99));
        record.setFloatfield(new Float(100.111));
        record.setIntegerfield(new Integer(9));
        dao.insert(record);
        FieldsonlyExample example = new FieldsonlyExample();
        example.createCriteria().andIntegerfieldGreaterThan(5);
        int rows = dao.deleteByExample(example);
        assertEquals(2, rows);
        example = new FieldsonlyExample();
        List answer = dao.selectByExample(example);
        assertEquals(1, answer.size());
    }

    public void testPKOnlyInsert() {
        PkonlyDAO dao = (PkonlyDAO) daoManager.getDao(PkonlyDAO.class);
        Pkonly key = new Pkonly();
        key.setId(new Integer(1));
        key.setSeqNum(new Integer(3));
        dao.insert(key);
        PkonlyExample example = new PkonlyExample();
        List answer = dao.selectByExample(example);
        assertEquals(1, answer.size());
        Pkonly returnedRecord = (Pkonly) answer.get(0);
        assertEquals(key.getId(), returnedRecord.getId());
        assertEquals(key.getSeqNum(), returnedRecord.getSeqNum());
    }

    public void testPKOnlyDeleteByPrimaryKey() {
        PkonlyDAO dao = (PkonlyDAO) daoManager.getDao(PkonlyDAO.class);
        Pkonly key = new Pkonly();
        key.setId(new Integer(1));
        key.setSeqNum(new Integer(3));
        dao.insert(key);
        key = new Pkonly();
        key.setId(new Integer(5));
        key.setSeqNum(new Integer(6));
        dao.insert(key);
        PkonlyExample example = new PkonlyExample();
        List answer = dao.selectByExample(example);
        assertEquals(2, answer.size());
        int rows = dao.deleteByPrimaryKey(new Integer(5), new Integer(6));
        assertEquals(1, rows);
        answer = dao.selectByExample(example);
        assertEquals(1, answer.size());
    }

    public void testPKOnlyDeleteByExample() {
        PkonlyDAO dao = (PkonlyDAO) daoManager.getDao(PkonlyDAO.class);
        Pkonly key = new Pkonly();
        key.setId(new Integer(1));
        key.setSeqNum(new Integer(3));
        dao.insert(key);
        key = new Pkonly();
        key.setId(new Integer(5));
        key.setSeqNum(new Integer(6));
        dao.insert(key);
        key = new Pkonly();
        key.setId(new Integer(7));
        key.setSeqNum(new Integer(8));
        dao.insert(key);
        PkonlyExample example = new PkonlyExample();
        example.createCriteria().andIdGreaterThan(4);
        int rows = dao.deleteByExample(example);
        assertEquals(2, rows);
        example = new PkonlyExample();
        List answer = dao.selectByExample(example);
        assertEquals(1, answer.size());
    }

    public void testPKOnlySelectByExample() {
        PkonlyDAO dao = (PkonlyDAO) daoManager.getDao(PkonlyDAO.class);
        Pkonly key = new Pkonly();
        key.setId(new Integer(1));
        key.setSeqNum(new Integer(3));
        dao.insert(key);
        key = new Pkonly();
        key.setId(new Integer(5));
        key.setSeqNum(new Integer(6));
        dao.insert(key);
        key = new Pkonly();
        key.setId(new Integer(7));
        key.setSeqNum(new Integer(8));
        dao.insert(key);
        PkonlyExample example = new PkonlyExample();
        example.createCriteria().andIdGreaterThan(4);
        List answer = dao.selectByExample(example);
        assertEquals(2, answer.size());
    }

    public void testPKOnlySelectByExampleNoCriteria() {
        PkonlyDAO dao = (PkonlyDAO) daoManager.getDao(PkonlyDAO.class);
        Pkonly key = new Pkonly();
        key.setId(new Integer(1));
        key.setSeqNum(new Integer(3));
        dao.insert(key);
        key = new Pkonly();
        key.setId(new Integer(5));
        key.setSeqNum(new Integer(6));
        dao.insert(key);
        key = new Pkonly();
        key.setId(new Integer(7));
        key.setSeqNum(new Integer(8));
        dao.insert(key);
        PkonlyExample example = new PkonlyExample();
        example.createCriteria();
        List answer = dao.selectByExample(example);
        assertEquals(3, answer.size());
    }

    public void testPKFieldsInsert() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setDatefield(new Date());
        record.setDecimal100field(new Long(10L));
        record.setDecimal155field(new BigDecimal("15.12345"));
        record.setDecimal30field(new Short((short) 3));
        record.setDecimal60field(new Integer(6));
        record.setFirstname("Jeff");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        record.setLastname("Butler");
        record.setTimefield(new Date());
        record.setTimestampfield(new Date());
        dao.insert(record);
        Pkfields returnedRecord = dao.selectByPrimaryKey(new Integer(1), new Integer(2));
        assertNotNull(returnedRecord);
        assertTrue(datesAreEqual(record.getDatefield(), returnedRecord.getDatefield()));
        assertEquals(record.getDecimal100field(), returnedRecord.getDecimal100field());
        assertEquals(record.getDecimal155field(), returnedRecord.getDecimal155field());
        assertEquals(record.getDecimal30field(), returnedRecord.getDecimal30field());
        assertEquals(record.getDecimal60field(), returnedRecord.getDecimal60field());
        assertEquals(record.getFirstname(), returnedRecord.getFirstname());
        assertEquals(record.getId1(), returnedRecord.getId1());
        assertEquals(record.getId2(), returnedRecord.getId2());
        assertEquals(record.getLastname(), returnedRecord.getLastname());
        assertTrue(timesAreEqual(record.getTimefield(), returnedRecord.getTimefield()));
        assertEquals(record.getTimestampfield(), returnedRecord.getTimestampfield());
    }

    public void testPKFieldsUpdateByPrimaryKey() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        record.setFirstname("Scott");
        record.setLastname("Jones");
        int rows = dao.updateByPrimaryKey(record);
        assertEquals(1, rows);
        Pkfields record2 = dao.selectByPrimaryKey(new Integer(1), new Integer(2));
        assertEquals(record.getFirstname(), record2.getFirstname());
        assertEquals(record.getLastname(), record2.getLastname());
        assertEquals(record.getId1(), record2.getId1());
        assertEquals(record.getId2(), record2.getId2());
    }

    public void testPKFieldsUpdateByPrimaryKeySelective() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setDecimal60field(new Integer(5));
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        Pkfields newRecord = new Pkfields();
        newRecord.setId1(new Integer(1));
        newRecord.setId2(new Integer(2));
        newRecord.setFirstname("Scott");
        newRecord.setDecimal60field(new Integer(4));
        int rows = dao.updateByPrimaryKeySelective(newRecord);
        assertEquals(1, rows);
        Pkfields returnedRecord = dao.selectByPrimaryKey(new Integer(1), new Integer(2));
        assertTrue(datesAreEqual(record.getDatefield(), returnedRecord.getDatefield()));
        assertEquals(record.getDecimal100field(), returnedRecord.getDecimal100field());
        assertEquals(record.getDecimal155field(), returnedRecord.getDecimal155field());
        assertEquals(record.getDecimal30field(), returnedRecord.getDecimal30field());
        assertEquals(newRecord.getDecimal60field(), returnedRecord.getDecimal60field());
        assertEquals(newRecord.getFirstname(), returnedRecord.getFirstname());
        assertEquals(record.getId1(), returnedRecord.getId1());
        assertEquals(record.getId2(), returnedRecord.getId2());
        assertEquals(record.getLastname(), returnedRecord.getLastname());
        assertTrue(timesAreEqual(record.getTimefield(), returnedRecord.getTimefield()));
        assertEquals(record.getTimestampfield(), returnedRecord.getTimestampfield());
    }

    public void testPKfieldsDeleteByPrimaryKey() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        int rows = dao.deleteByPrimaryKey(new Integer(1), new Integer(2));
        assertEquals(1, rows);
        PkfieldsExample example = new PkfieldsExample();
        List answer = dao.selectByExample(example);
        assertEquals(0, answer.size());
    }

    public void testPKFieldsDeleteByExample() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Bob");
        record.setLastname("Jones");
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        dao.insert(record);
        PkfieldsExample example = new PkfieldsExample();
        List answer = dao.selectByExample(example);
        assertEquals(2, answer.size());
        example = new PkfieldsExample();
        example.createCriteria().andLastnameLike("J%");
        int rows = dao.deleteByExample(example);
        assertEquals(1, rows);
        example = new PkfieldsExample();
        answer = dao.selectByExample(example);
        assertEquals(1, answer.size());
    }

    public void testPKFieldsSelectByPrimaryKey() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Bob");
        record.setLastname("Jones");
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        dao.insert(record);
        Pkfields newRecord = dao.selectByPrimaryKey(new Integer(3), new Integer(4));
        assertNotNull(newRecord);
        assertEquals(record.getFirstname(), newRecord.getFirstname());
        assertEquals(record.getLastname(), newRecord.getLastname());
        assertEquals(record.getId1(), newRecord.getId1());
        assertEquals(record.getId2(), newRecord.getId2());
    }

    public void testPKFieldsSelectByExampleLike() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Fred");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Wilma");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Pebbles");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(3));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Barney");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Betty");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Bamm Bamm");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(3));
        dao.insert(record);
        PkfieldsExample example = new PkfieldsExample();
        example.createCriteria().andFirstnameLike("B%");
        example.setOrderByClause("ID1, ID2");
        List answer = dao.selectByExample(example);
        assertEquals(3, answer.size());
        Pkfields returnedRecord = (Pkfields) answer.get(0);
        assertEquals(2, returnedRecord.getId1().intValue());
        assertEquals(1, returnedRecord.getId2().intValue());
        returnedRecord = (Pkfields) answer.get(1);
        assertEquals(2, returnedRecord.getId1().intValue());
        assertEquals(2, returnedRecord.getId2().intValue());
        returnedRecord = (Pkfields) answer.get(2);
        assertEquals(2, returnedRecord.getId1().intValue());
        assertEquals(3, returnedRecord.getId2().intValue());
    }

    public void testPKFieldsSelectByExampleNotLike() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Fred");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Wilma");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Pebbles");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(3));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Barney");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Betty");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Bamm Bamm");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(3));
        dao.insert(record);
        PkfieldsExample example = new PkfieldsExample();
        example.createCriteria().andFirstnameNotLike("B%");
        example.setOrderByClause("ID1, ID2");
        List answer = dao.selectByExample(example);
        assertEquals(3, answer.size());
        Pkfields returnedRecord = (Pkfields) answer.get(0);
        assertEquals(1, returnedRecord.getId1().intValue());
        assertEquals(1, returnedRecord.getId2().intValue());
        returnedRecord = (Pkfields) answer.get(1);
        assertEquals(1, returnedRecord.getId1().intValue());
        assertEquals(2, returnedRecord.getId2().intValue());
        returnedRecord = (Pkfields) answer.get(2);
        assertEquals(1, returnedRecord.getId1().intValue());
        assertEquals(3, returnedRecord.getId2().intValue());
    }

    public void testPKFieldsSelectByExampleComplexLike() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Fred");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Wilma");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Pebbles");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(3));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Barney");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Betty");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Bamm Bamm");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(3));
        dao.insert(record);
        PkfieldsExample example = new PkfieldsExample();
        example.createCriteria().andFirstnameLike("B%").andId2EqualTo(new Integer(3));
        example.or(example.createCriteria().andFirstnameLike("Wi%"));
        example.setOrderByClause("ID1, ID2");
        List answer = dao.selectByExample(example);
        assertEquals(2, answer.size());
        Pkfields returnedRecord = (Pkfields) answer.get(0);
        assertEquals(1, returnedRecord.getId1().intValue());
        assertEquals(2, returnedRecord.getId2().intValue());
        returnedRecord = (Pkfields) answer.get(1);
        assertEquals(2, returnedRecord.getId1().intValue());
        assertEquals(3, returnedRecord.getId2().intValue());
    }

    @SuppressWarnings("unchecked")
    public void testPKFieldsSelectByExampleIn() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Fred");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Wilma");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Pebbles");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(3));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Barney");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Betty");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Bamm Bamm");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(3));
        dao.insert(record);
        List ids = new ArrayList();
        ids.add(new Integer(1));
        ids.add(new Integer(3));
        PkfieldsExample example = new PkfieldsExample();
        example.createCriteria().andId2In(ids);
        example.setOrderByClause("ID1, ID2");
        List answer = dao.selectByExample(example);
        assertEquals(4, answer.size());
        Pkfields returnedRecord = (Pkfields) answer.get(0);
        assertEquals(1, returnedRecord.getId1().intValue());
        assertEquals(1, returnedRecord.getId2().intValue());
        returnedRecord = (Pkfields) answer.get(1);
        assertEquals(1, returnedRecord.getId1().intValue());
        assertEquals(3, returnedRecord.getId2().intValue());
        returnedRecord = (Pkfields) answer.get(2);
        assertEquals(2, returnedRecord.getId1().intValue());
        assertEquals(1, returnedRecord.getId2().intValue());
        returnedRecord = (Pkfields) answer.get(3);
        assertEquals(2, returnedRecord.getId1().intValue());
        assertEquals(3, returnedRecord.getId2().intValue());
    }

    public void testPKFieldsSelectByExampleBetween() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Fred");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Wilma");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Pebbles");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(3));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Barney");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Betty");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Bamm Bamm");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(3));
        dao.insert(record);
        PkfieldsExample example = new PkfieldsExample();
        example.createCriteria().andId2Between(new Integer(1), new Integer(3));
        example.setOrderByClause("ID1, ID2");
        List answer = dao.selectByExample(example);
        assertEquals(6, answer.size());
    }

    public void testPKFieldsSelectByExampleNoCriteria() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Fred");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Wilma");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Pebbles");
        record.setLastname("Flintstone");
        record.setId1(new Integer(1));
        record.setId2(new Integer(3));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Barney");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(1));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Betty");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(2));
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Bamm Bamm");
        record.setLastname("Rubble");
        record.setId1(new Integer(2));
        record.setId2(new Integer(3));
        dao.insert(record);
        PkfieldsExample example = new PkfieldsExample();
        example.createCriteria();
        example.setOrderByClause("ID1, ID2");
        List answer = dao.selectByExample(example);
        assertEquals(6, answer.size());
    }

    public void testPKFieldsSelectByExampleEscapedFields() {
        PkfieldsDAO dao = (PkfieldsDAO) daoManager.getDao(PkfieldsDAO.class);
        Pkfields record = new Pkfields();
        record.setFirstname("Fred");
        record.setLastname("Flintstone");
        record.setId1(1);
        record.setId2(1);
        record.setWierdField(11);
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Wilma");
        record.setLastname("Flintstone");
        record.setId1(1);
        record.setId2(2);
        record.setWierdField(22);
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Pebbles");
        record.setLastname("Flintstone");
        record.setId1(1);
        record.setId2(3);
        record.setWierdField(33);
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Barney");
        record.setLastname("Rubble");
        record.setId1(2);
        record.setId2(1);
        record.setWierdField(44);
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Betty");
        record.setLastname("Rubble");
        record.setId1(2);
        record.setId2(2);
        record.setWierdField(55);
        dao.insert(record);
        record = new Pkfields();
        record.setFirstname("Bamm Bamm");
        record.setLastname("Rubble");
        record.setId1(2);
        record.setId2(3);
        record.setWierdField(66);
        dao.insert(record);
        List<Integer> values = new ArrayList<Integer>();
        values.add(11);
        values.add(22);
        PkfieldsExample example = new PkfieldsExample();
        example.createCriteria().andWierdFieldLessThan(40).andWierdFieldIn(values);
        example.setOrderByClause("ID1, ID2");
        List answer = dao.selectByExample(example);
        assertEquals(2, answer.size());
    }

    public void testPKBlobsInsert() {
        PkblobsDAO dao = (PkblobsDAO) daoManager.getDao(PkblobsDAO.class);
        Pkblobs record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        PkblobsExample example = new PkblobsExample();
        List answer = dao.selectByExampleWithBLOBs(example);
        assertEquals(1, answer.size());
        Pkblobs returnedRecord = (Pkblobs) answer.get(0);
        assertEquals(record.getId(), returnedRecord.getId());
        assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
        assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord.getBlob2()));
    }

    public void testPKBlobsUpdateByPrimaryKeyWithBLOBs() {
        PkblobsDAO dao = (PkblobsDAO) daoManager.getDao(PkblobsDAO.class);
        Pkblobs record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        int rows = dao.updateByPrimaryKey(record);
        assertEquals(1, rows);
        Pkblobs newRecord = dao.selectByPrimaryKey(new Integer(3));
        assertNotNull(newRecord);
        assertEquals(record.getId(), newRecord.getId());
        assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
    }

    public void testPKBlobsUpdateByPrimaryKeySelective() {
        PkblobsDAO dao = (PkblobsDAO) daoManager.getDao(PkblobsDAO.class);
        Pkblobs record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        Pkblobs newRecord = new Pkblobs();
        newRecord.setId(new Integer(3));
        newRecord.setBlob2(generateRandomBlob());
        dao.updateByPrimaryKeySelective(newRecord);
        Pkblobs returnedRecord = dao.selectByPrimaryKey(new Integer(3));
        assertNotNull(returnedRecord);
        assertEquals(record.getId(), returnedRecord.getId());
        assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
        assertTrue(blobsAreEqual(newRecord.getBlob2(), returnedRecord.getBlob2()));
    }

    public void testPKBlobsDeleteByPrimaryKey() {
        PkblobsDAO dao = (PkblobsDAO) daoManager.getDao(PkblobsDAO.class);
        Pkblobs record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        PkblobsExample example = new PkblobsExample();
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(1, answer.size());
        int rows = dao.deleteByPrimaryKey(new Integer(3));
        assertEquals(1, rows);
        example = new PkblobsExample();
        answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(0, answer.size());
    }

    public void testPKBlobsDeleteByExample() {
        PkblobsDAO dao = (PkblobsDAO) daoManager.getDao(PkblobsDAO.class);
        Pkblobs record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Pkblobs();
        record.setId(new Integer(6));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        PkblobsExample example = new PkblobsExample();
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(2, answer.size());
        example = new PkblobsExample();
        example.createCriteria().andIdLessThan(4);
        int rows = dao.deleteByExample(example);
        assertEquals(1, rows);
        example = new PkblobsExample();
        answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(1, answer.size());
    }

    public void testPKBlobsSelectByPrimaryKey() {
        PkblobsDAO dao = (PkblobsDAO) daoManager.getDao(PkblobsDAO.class);
        Pkblobs record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Pkblobs();
        record.setId(new Integer(6));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        Pkblobs newRecord = dao.selectByPrimaryKey(new Integer(6));
        assertNotNull(newRecord);
        assertEquals(record.getId(), newRecord.getId());
        assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
    }

    public void testPKBlobsSelectByExampleWithoutBlobs() {
        PkblobsDAO dao = (PkblobsDAO) daoManager.getDao(PkblobsDAO.class);
        Pkblobs record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Pkblobs();
        record.setId(new Integer(6));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        PkblobsExample example = new PkblobsExample();
        example.createCriteria().andIdGreaterThan(4);
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(1, answer.size());
        Pkblobs key = (Pkblobs) answer.get(0);
        assertEquals(6, key.getId().intValue());
        assertNull(key.getBlob1());
        assertNull(key.getBlob2());
    }

    public void testPKBlobsSelectByExampleWithoutBlobsNoCriteria() {
        PkblobsDAO dao = (PkblobsDAO) daoManager.getDao(PkblobsDAO.class);
        Pkblobs record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Pkblobs();
        record.setId(new Integer(6));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        PkblobsExample example = new PkblobsExample();
        example.createCriteria();
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(2, answer.size());
    }

    public void testPKBlobsSelectByExampleWithBlobs() {
        PkblobsDAO dao = (PkblobsDAO) daoManager.getDao(PkblobsDAO.class);
        Pkblobs record = new Pkblobs();
        record.setId(new Integer(3));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Pkblobs();
        record.setId(new Integer(6));
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        PkblobsExample example = new PkblobsExample();
        example.createCriteria().andIdGreaterThan(4);
        List answer = dao.selectByExampleWithBLOBs(example);
        assertEquals(1, answer.size());
        Pkblobs newRecord = (Pkblobs) answer.get(0);
        assertEquals(record.getId(), newRecord.getId());
        assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
    }

    public void testPKFieldsBlobsInsert() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        PkfieldsblobsExample example = new PkfieldsblobsExample();
        List answer = dao.selectByExampleWithBLOBs(example);
        assertEquals(1, answer.size());
        Pkfieldsblobs returnedRecord = (Pkfieldsblobs) answer.get(0);
        assertEquals(record.getId1(), returnedRecord.getId1());
        assertEquals(record.getId2(), returnedRecord.getId2());
        assertEquals(record.getFirstname(), returnedRecord.getFirstname());
        assertEquals(record.getLastname(), returnedRecord.getLastname());
        assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
    }

    public void testPKFieldsBlobsUpdateByPrimaryKeyWithBLOBs() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        Pkfieldsblobs updateRecord = new Pkfieldsblobs();
        updateRecord.setId1(new Integer(3));
        updateRecord.setId2(new Integer(4));
        updateRecord.setFirstname("Scott");
        updateRecord.setLastname("Jones");
        updateRecord.setBlob1(generateRandomBlob());
        int rows = dao.updateByPrimaryKeyWithBLOBs(updateRecord);
        assertEquals(1, rows);
        Pkfieldsblobs newRecord = dao.selectByPrimaryKey(new Integer(3), new Integer(4));
        assertEquals(updateRecord.getFirstname(), newRecord.getFirstname());
        assertEquals(updateRecord.getLastname(), newRecord.getLastname());
        assertEquals(record.getId1(), newRecord.getId1());
        assertEquals(record.getId2(), newRecord.getId2());
        assertTrue(blobsAreEqual(updateRecord.getBlob1(), newRecord.getBlob1()));
    }

    public void testPKFieldsBlobsUpdateByPrimaryKeyWithoutBLOBs() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        Pkfieldsblobs updateRecord = new Pkfieldsblobs();
        updateRecord.setId1(new Integer(3));
        updateRecord.setId2(new Integer(4));
        updateRecord.setFirstname("Scott");
        updateRecord.setLastname("Jones");
        int rows = dao.updateByPrimaryKeyWithoutBLOBs(updateRecord);
        assertEquals(1, rows);
        Pkfieldsblobs newRecord = dao.selectByPrimaryKey(new Integer(3), new Integer(4));
        assertEquals(updateRecord.getFirstname(), newRecord.getFirstname());
        assertEquals(updateRecord.getLastname(), newRecord.getLastname());
        assertEquals(record.getId1(), newRecord.getId1());
        assertEquals(record.getId2(), newRecord.getId2());
        assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
    }

    public void testPKFieldsBlobsUpdateByPrimaryKeySelective() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        Pkfieldsblobs updateRecord = new Pkfieldsblobs();
        updateRecord.setId1(new Integer(3));
        updateRecord.setId2(new Integer(4));
        updateRecord.setLastname("Jones");
        int rows = dao.updateByPrimaryKeySelective(updateRecord);
        assertEquals(1, rows);
        Pkfieldsblobs returnedRecord = dao.selectByPrimaryKey(new Integer(3), new Integer(4));
        assertEquals(record.getFirstname(), returnedRecord.getFirstname());
        assertEquals(updateRecord.getLastname(), returnedRecord.getLastname());
        assertEquals(record.getId1(), returnedRecord.getId1());
        assertEquals(record.getId2(), returnedRecord.getId2());
        assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
    }

    public void testPKFieldsBlobsDeleteByPrimaryKey() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        record = new Pkfieldsblobs();
        record.setId1(new Integer(5));
        record.setId2(new Integer(6));
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        PkfieldsblobsExample example = new PkfieldsblobsExample();
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(2, answer.size());
        int rows = dao.deleteByPrimaryKey(new Integer(5), new Integer(6));
        assertEquals(1, rows);
        example = new PkfieldsblobsExample();
        answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(1, answer.size());
    }

    public void testPKFieldsBlobsDeleteByExample() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        record = new Pkfieldsblobs();
        record.setId1(new Integer(5));
        record.setId2(new Integer(6));
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        PkfieldsblobsExample example = new PkfieldsblobsExample();
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(2, answer.size());
        example = new PkfieldsblobsExample();
        example.createCriteria().andId1NotEqualTo(3);
        int rows = dao.deleteByExample(example);
        assertEquals(1, rows);
        example = new PkfieldsblobsExample();
        answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(1, answer.size());
    }

    public void testPKFieldsBlobsSelectByPrimaryKey() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        record = new Pkfieldsblobs();
        record.setId1(new Integer(5));
        record.setId2(new Integer(6));
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        PkfieldsblobsExample example = new PkfieldsblobsExample();
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(2, answer.size());
        Pkfieldsblobs newRecord = dao.selectByPrimaryKey(new Integer(5), new Integer(6));
        assertEquals(record.getId1(), newRecord.getId1());
        assertEquals(record.getId2(), newRecord.getId2());
        assertEquals(record.getFirstname(), newRecord.getFirstname());
        assertEquals(record.getLastname(), newRecord.getLastname());
        assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
    }

    public void testPKFieldsBlobsSelectByExampleWithoutBlobs() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        record = new Pkfieldsblobs();
        record.setId1(new Integer(5));
        record.setId2(new Integer(6));
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        PkfieldsblobsExample example = new PkfieldsblobsExample();
        example.createCriteria().andId2EqualTo(6);
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(1, answer.size());
        Pkfieldsblobs newRecord = (Pkfieldsblobs) answer.get(0);
        assertEquals(record.getId1(), newRecord.getId1());
        assertEquals(record.getId2(), newRecord.getId2());
        assertEquals(record.getFirstname(), newRecord.getFirstname());
        assertEquals(record.getLastname(), newRecord.getLastname());
        assertNull(newRecord.getBlob1());
    }

    public void testPKFieldsBlobsSelectByExampleWithBlobs() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        record = new Pkfieldsblobs();
        record.setId1(new Integer(5));
        record.setId2(new Integer(6));
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        PkfieldsblobsExample example = new PkfieldsblobsExample();
        example.createCriteria().andId2EqualTo(6);
        List answer = dao.selectByExampleWithBLOBs(example);
        assertEquals(1, answer.size());
        Pkfieldsblobs newRecord = (Pkfieldsblobs) answer.get(0);
        assertEquals(record.getId1(), newRecord.getId1());
        assertEquals(record.getId2(), newRecord.getId2());
        assertEquals(record.getFirstname(), newRecord.getFirstname());
        assertEquals(record.getLastname(), newRecord.getLastname());
        assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
    }

    public void testPKFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        PkfieldsblobsDAO dao = (PkfieldsblobsDAO) daoManager.getDao(PkfieldsblobsDAO.class);
        Pkfieldsblobs record = new Pkfieldsblobs();
        record.setId1(new Integer(3));
        record.setId2(new Integer(4));
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        record = new Pkfieldsblobs();
        record.setId1(new Integer(5));
        record.setId2(new Integer(6));
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        dao.insert(record);
        PkfieldsblobsExample example = new PkfieldsblobsExample();
        example.createCriteria();
        List answer = dao.selectByExampleWithBLOBs(example);
        assertEquals(2, answer.size());
    }

    public void testFieldsBlobsInsert() {
        FieldsblobsDAO dao = (FieldsblobsDAO) daoManager.getDao(FieldsblobsDAO.class);
        Fieldsblobs record = new Fieldsblobs();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        FieldsblobsExample example = new FieldsblobsExample();
        List answer = dao.selectByExampleWithBLOBs(example);
        assertEquals(1, answer.size());
        Fieldsblobs returnedRecord = (Fieldsblobs) answer.get(0);
        assertEquals(record.getFirstname(), returnedRecord.getFirstname());
        assertEquals(record.getLastname(), returnedRecord.getLastname());
        assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
        assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord.getBlob2()));
    }

    public void testFieldsBlobsDeleteByExample() {
        FieldsblobsDAO dao = (FieldsblobsDAO) daoManager.getDao(FieldsblobsDAO.class);
        Fieldsblobs record = new Fieldsblobs();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Fieldsblobs();
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        FieldsblobsExample example = new FieldsblobsExample();
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(2, answer.size());
        example = new FieldsblobsExample();
        example.createCriteria().andFirstnameLike("S%");
        int rows = dao.deleteByExample(example);
        assertEquals(1, rows);
        example = new FieldsblobsExample();
        answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(1, answer.size());
    }

    public void testFieldsBlobsSelectByExampleWithoutBlobs() {
        FieldsblobsDAO dao = (FieldsblobsDAO) daoManager.getDao(FieldsblobsDAO.class);
        Fieldsblobs record = new Fieldsblobs();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Fieldsblobs();
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        FieldsblobsExample example = new FieldsblobsExample();
        example.createCriteria().andFirstnameLike("S%");
        List answer = dao.selectByExampleWithoutBLOBs(example);
        assertEquals(1, answer.size());
        Fieldsblobs newRecord = (Fieldsblobs) answer.get(0);
        assertEquals(record.getFirstname(), newRecord.getFirstname());
        assertEquals(record.getLastname(), newRecord.getLastname());
        assertNull(newRecord.getBlob1());
        assertNull(newRecord.getBlob2());
    }

    public void testFieldsBlobsSelectByExampleWithBlobs() {
        FieldsblobsDAO dao = (FieldsblobsDAO) daoManager.getDao(FieldsblobsDAO.class);
        Fieldsblobs record = new Fieldsblobs();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Fieldsblobs();
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        FieldsblobsExample example = new FieldsblobsExample();
        example.createCriteria().andFirstnameLike("S%");
        List answer = dao.selectByExampleWithBLOBs(example);
        assertEquals(1, answer.size());
        Fieldsblobs newRecord = (Fieldsblobs) answer.get(0);
        assertEquals(record.getFirstname(), newRecord.getFirstname());
        assertEquals(record.getLastname(), newRecord.getLastname());
        assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
    }

    public void testFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        FieldsblobsDAO dao = (FieldsblobsDAO) daoManager.getDao(FieldsblobsDAO.class);
        Fieldsblobs record = new Fieldsblobs();
        record.setFirstname("Jeff");
        record.setLastname("Smith");
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        record = new Fieldsblobs();
        record.setFirstname("Scott");
        record.setLastname("Jones");
        record.setBlob1(generateRandomBlob());
        record.setBlob2(generateRandomBlob());
        dao.insert(record);
        FieldsblobsExample example = new FieldsblobsExample();
        example.createCriteria();
        List answer = dao.selectByExampleWithBLOBs(example);
        assertEquals(2, answer.size());
    }
}
