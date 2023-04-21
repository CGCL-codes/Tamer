package ru.pit.homemoney.test.dao;

import org.junit.Test;
import ru.pit.homemoney.dao.AccountDao;
import ru.pit.homemoney.dao.IndexDao;
import ru.pit.homemoney.dao.ReflistItemDao;
import ru.pit.homemoney.domain.types.AccountType;
import ru.pit.homemoney.domain.value.AccountVO;
import ru.pit.homemoney.domain.value.IndexVO;
import ru.pit.homemoney.domain.value.ReflistItemVO;
import ru.pit.homemoney.test.SpringBasedTest;

/**
 * Test Cases for {@link AccountDao}
 *
 * @author P.Salnikov (p.salnikov@gmail.com)
 * @version $Revision: 114 $
 */
public class AccountDaoTest extends SpringBasedTest {

    private AccountDao accountDao = null;

    private IndexDao indexDao = null;

    private ReflistItemDao reflistItemDao;

    public void setAccountDao(AccountDao dao) {
        this.accountDao = dao;
    }

    public void setIndexDao(IndexDao indexDao) {
        this.indexDao = indexDao;
    }

    public void setReflistItemDao(ReflistItemDao reflistItemDao) {
        this.reflistItemDao = reflistItemDao;
    }

    /**
     * get existent AccountVO
     */
    @Test
    public void testAccountGetOk() {
        Long id = new Long(1);
        AccountVO account = accountDao.findById(id);
        assertNotNull("can't get existent account", account);
        assertEquals("account.id", id, account.getId());
        assertEquals("account.nane", "Депозит на 1/2 года", account.getName());
        assertEquals("account.type", AccountType.DEPOSIT, account.getType());
        assertNotNull("account.bank", account.getBank());
        assertEquals("account.bank.id", new Long(1), account.getBank().getId());
        assertNotNull("account.index", account.getIndex());
        assertEquals("account.index.id", new Long(1), account.getIndex().getId());
    }

    @Test
    public void testAccountGetFailed() {
        Long id = new Long(100);
        AccountVO account = accountDao.findById(id);
        int i = 0;
    }

    /**
     * save AccountVO
     *
     */
    @Test
    public void testAccountSave() {
        AccountVO account = new AccountVO();
        account.setName("Тестовый Банк");
        account.setType(AccountType.CREDIT);
        IndexVO index = indexDao.findById(new Long(1));
        account.setIndex(index);
        ReflistItemVO bank = reflistItemDao.findById(new Long(1));
        account.setBank(bank);
        accountDao.save(account);
    }

    /**
     * delete existent AccountVO
     */
    @Test
    public void testAccountDelete() {
        Long id = new Long(3);
        AccountVO account = accountDao.findById(id);
        accountDao.delete(account);
        AccountVO accountTest = accountDao.findById(id);
        assertNull("got deleted account", accountTest);
    }
}
