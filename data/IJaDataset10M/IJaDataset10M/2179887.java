package net.persister.test.testCase.persistentContext.api;

import static org.junit.Assert.assertNotNull;
import net.persister.example.entity.Client;
import net.persister.example.entity.Company;
import net.persister.exception.EntityExistException;
import net.persister.persistentContext.FlushMode;
import net.persister.persistentContext.PersistentContext;
import net.persister.template.TestHelper;
import net.persister.test.template.AbstractPersisterTransactionalTestForCommon;
import org.junit.Test;

/**
 * @author Park, chanwook
 *
 */
public class AdvancePersistTest extends AbstractPersisterTransactionalTestForCommon {

    @Test(expected = EntityExistException.class)
    public void throwExceptionWhenAlreadyPersisted() {
        Client client = (Client) TestHelper.getEntity(Client.class);
        save(client);
        PersistentContext persistentContext = openPersistentContext();
        Integer resultId = (Integer) persistentContext.persist(client);
        persistentContext.getTransaction().commit();
        persistentContext.close();
    }

    /**
	 * EntityState.Persistent. 이미 동일 pc에 인스턴스가 있으므로..
	 */
    @Test
    public void ignoreWhenAlreadyInserted() {
        PersistentContext persistentContext = openPersistentContext();
        Company company = (Company) TestHelper.getEntity(Company.class);
        Integer firstId = (Integer) persistentContext.persist(company);
        System.out.println("first persist" + firstId);
        Integer secondId = (Integer) persistentContext.persist(company);
        System.out.println("second persist" + secondId);
        persistentContext.getTransaction().commit();
        persistentContext.close();
    }

    /**
	 * 한 번 삭제된 인스턴스를 새로운 pc에서 다시 저장. 이건 transient와 동일한 상황이다. 일단 id가 할당이 되어 있기
	 * 때문에. id가 할당이 되어 있어도 save가 되도록 만들어야 한다.
	 */
    @Test
    public void reSavedThatAlreadyDeletedInstance() {
        Company company = (Company) TestHelper.getEntity(Company.class);
        save(company);
        delete(company);
        PersistentContext persistentContext = openPersistentContext();
        persistentContext.persist(company);
        persistentContext.getTransaction().commit();
        persistentContext.close();
    }

    /**
	 * EntityState.DELETING. 아직 flush되지 않은 deleteAction이 있는 경우, 이 action을 삭제하고,
	 * managed 상태로 유지.
	 */
    @Test
    public void changeStateAndDeleteThatDeleteActionWhenDeletingStateInstance() {
        Company company = (Company) TestHelper.getEntity(Company.class);
        save(company);
        PersistentContext persistentContext = openPersistentContext();
        Company loadedCompany = (Company) persistentContext.find(Company.class, company.getId());
        assertNotNull(loadedCompany);
        persistentContext.delete(loadedCompany);
        persistentContext.persist(loadedCompany);
        persistentContext.getTransaction().commit();
        persistentContext.close();
        Company reloadCompany = (Company) load(Company.class, company.getId());
        assertNotNull(reloadCompany);
    }

    /**
	 * EntityState.DELETED. 이미 삭제된 엔티티지만, 다시 persist되면 복구 됨.
	 *
	 */
    @Test
    public void reSaveThatDeleteWhenDeletedStateInstance() {
        Company company = (Company) TestHelper.getEntity(Company.class);
        save(company);
        PersistentContext persistentContext = openPersistentContext();
        Company loadedCompany = (Company) persistentContext.find(Company.class, company.getId());
        assertNotNull(loadedCompany);
        persistentContext.setFlushMode(FlushMode.ALWAYS);
        persistentContext.delete(loadedCompany);
        persistentContext.persist(loadedCompany);
        persistentContext.getTransaction().commit();
        persistentContext.close();
        Company reloadCompany = (Company) load(Company.class, company.getId());
        assertNotNull(reloadCompany);
    }

    /**
	 * Id 값을 가지고 있는 인스턴스 저장하기.
	 */
    @Test
    public void persistThatInstanceHavedIdentifier() {
        PersistentContext persistentContext = openPersistentContext();
        Company company = (Company) TestHelper.getEntity(Company.class);
        company.setId(888);
        persistentContext.persist(company);
        isNotNull(company);
        persistentContext.getTransaction().commit();
        persistentContext.close();
        delete(company);
    }

    @Test(expected = EntityExistException.class)
    public void throwExceptionWhenHavedDuplicatedPrimaryKey() {
        Company company = (Company) TestHelper.getEntity(Company.class);
        save(company);
        PersistentContext persistentContext = openPersistentContext();
        persistentContext.persist(company);
        persistentContext.getTransaction().commit();
        persistentContext.close();
        delete(company);
    }
}
