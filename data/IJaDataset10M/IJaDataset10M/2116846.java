package ru.pit.homemoney.test.dao;

import org.junit.Test;
import ru.pit.homemoney.dao.AccountDao;
import ru.pit.homemoney.dao.PersonDao;
import ru.pit.homemoney.dao.ReflistItemDao;
import ru.pit.homemoney.dao.TransactionDao;
import ru.pit.homemoney.domain.types.TransactionType;
import ru.pit.homemoney.domain.value.AccountVO;
import ru.pit.homemoney.domain.value.PersonVO;
import ru.pit.homemoney.domain.value.ReflistItemVO;
import ru.pit.homemoney.domain.value.TransactionID;
import ru.pit.homemoney.domain.value.TransactionVO;
import ru.pit.homemoney.test.SpringBasedTest;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

/**
 * Test Cases for {@link TransactionDao}
 *
 * @author P.Salnikov (p.salnikov@gmail.com)
 * @version $Revision: 108 $
 */
public class TransactionDaoTest extends SpringBasedTest {

    private TransactionDao transactionDao = null;

    private PersonDao personDao = null;

    private AccountDao accountDao = null;

    private ReflistItemDao reflistItemDao = null;

    public void setTransactionDao(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public void setReflistItemDao(ReflistItemDao reflistItemDao) {
        this.reflistItemDao = reflistItemDao;
    }

    /**
   * get existent TransactionVO
   */
    @Test
    public void testTransactionGetOk() {
        Long trId = new Long(1);
        Long seqNo = new Long(1);
        TransactionID id = new TransactionID(trId, seqNo);
        TransactionVO tr = transactionDao.findById(id);
        GregorianCalendar dateTest = new GregorianCalendar(2006, 03, 30, 22, 9, 35);
        PersonVO personToTest = personDao.findById(new Long(1));
        PersonVO personFromTest = personDao.findById(new Long(2));
        AccountVO accountToTest = accountDao.findById(new Long(1));
        AccountVO accountFromTest = accountDao.findById(new Long(2));
        ReflistItemVO categoryTest = reflistItemDao.findById(new Long(1));
        ReflistItemVO subCategoryTest = reflistItemDao.findById(new Long(2));
        assertNotNull("can't get existent transaction", tr);
        assertEquals("transaction.id.id", trId, tr.getId().getId());
        assertEquals("transaction.id.seqno", seqNo, tr.getId().getSeqNo());
        assertEquals("transaction.person_dest", personToTest, tr.getToPerson());
        assertEquals("transaction.person_src", personFromTest, tr.getFromPerson());
        assertEquals("transaction.account_dest", accountToTest, tr.getToAccount());
        assertEquals("transaction.account_src", accountFromTest, tr.getFromAccount());
        assertEquals("transaction.category", categoryTest, tr.getCategory());
        assertEquals("transaction.subcategory", subCategoryTest, tr.getSubCategory());
        assertEquals("transaction.date", dateTest.getTime(), tr.getDate());
        assertEquals("transaction.descr", "описание", tr.getDescr());
        assertEquals("transaction.amount", new BigDecimal("100.1234"), tr.getAmount());
        assertEquals("transaction.type", TransactionType.TRANSFER, tr.getType());
    }

    /**
   * save TransactionVO
   *
   */
    @Test
    public void testTransactionSave() {
        TransactionVO tr = new TransactionVO();
        GregorianCalendar dateTest = new GregorianCalendar(2006, 04, 30, 22, 9, 35);
        PersonVO personToTest = personDao.findById(new Long(1));
        PersonVO personFromTest = personDao.findById(new Long(2));
        AccountVO accountToTest = accountDao.findById(new Long(1));
        AccountVO accountFromTest = accountDao.findById(new Long(2));
        ReflistItemVO categoryTest = reflistItemDao.findById(new Long(1));
        ReflistItemVO subCategoryTest = reflistItemDao.findById(new Long(2));
        tr.setToPerson(personToTest);
        tr.setFromPerson(personFromTest);
        tr.setToAccount(accountToTest);
        tr.setFromAccount(accountFromTest);
        tr.setType(TransactionType.OWNER);
        tr.setCategory(categoryTest);
        tr.setSubCategory(subCategoryTest);
        tr.setDescr("описание тестовое");
        tr.setAmount(new BigDecimal("123.4567"));
        transactionDao.save(tr);
        TransactionVO trTest = transactionDao.findById(tr.getId());
        assertNotNull("can't get saved index", trTest);
    }

    /**
   * delete existent TransactionVO
   */
    @Test
    public void testTransactionDelete() {
        TransactionID id = new TransactionID(new Long(1), new Long(1));
        TransactionVO tr = transactionDao.findById(id);
        transactionDao.delete(tr);
        TransactionVO trTest = transactionDao.findById(id);
        assertNull("got deleted index", trTest);
    }
}
