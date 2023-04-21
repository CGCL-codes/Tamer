package net.persister.test.testCase.persistentContext.flush;

import static org.junit.Assert.assertEquals;
import net.persister.example.entity.Client;
import net.persister.persistentContext.PersistentContext;
import net.persister.template.TestHelper;
import net.persister.test.template.AbstractPersisterTransactionalTestForCommon;
import org.junit.Test;

/**
 * @author Park, chanwook
 *
 */
public class AutoFlushModeTest extends AbstractPersisterTransactionalTestForCommon {

    /**
	 * FlushModeType.AUTO . queue&cache 저장 -> cache에서 load -> commit 시 insert
	 */
    @Test
    public void doInsert() {
        PersistentContext persistentContext = openPersistentContext();
        Integer id = (Integer) persistentContext.persist(TestHelper.getEntity(Client.class));
        Client client = (Client) persistentContext.find(Client.class, id);
        persistentContext.getTransaction().commit();
        persistentContext.close();
    }

    /**
	 * FlushModeType.AUTO . db에서 로드 -> update 내용 queue 저장 -> cache에서 로드 ->
	 * commit 시 update
	 *
	 */
    @Test
    public void doUpdate() {
        Integer ClientId = (Integer) save(TestHelper.getEntity(Client.class));
        PersistentContext persistentContext = openPersistentContext();
        Client client = (Client) persistentContext.find(Client.class, ClientId);
        client.setName("update Name");
        persistentContext.update(client);
        Client loadClient = (Client) persistentContext.find(Client.class, ClientId);
        assertEquals(client.getName(), loadClient.getName());
        persistentContext.getTransaction().commit();
        persistentContext.close();
        Client reloadedClient = (Client) load(Client.class, client.getId());
        assertEquals(client.getName(), reloadedClient.getName());
    }

    /**
	 * FlushModeType.AUTO . db에서 로드 -> 내용 변경 -> cache에서 로드 -> commit 시 update
	 *
	 */
    @Test
    public void doUpdateNonExplicitCall() {
        Integer ClientId = (Integer) save(TestHelper.getEntity(Client.class));
        PersistentContext persistentContext = openPersistentContext();
        Client client = (Client) persistentContext.find(Client.class, ClientId);
        client.setName("update Name");
        Client loadClient = (Client) persistentContext.find(Client.class, ClientId);
        assertEquals(client.getName(), loadClient.getName());
        persistentContext.getTransaction().commit();
        persistentContext.close();
        Client reloadedClient = (Client) load(Client.class, client.getId());
        assertEquals(client.getName(), reloadedClient.getName());
    }

    /**
	 * FlushModeType.AUTO . db에서 로드 -> delete queue에 저장 -> cache에서 null 로드 ->
	 * db에서 값을 불러왔지만 없으므로 null 리턴.
	 *
	 */
    @Test
    public void doDelete() {
        Integer ClientId = (Integer) save(TestHelper.getEntity(Client.class));
        PersistentContext persistentContext = openPersistentContext();
        Client client = (Client) persistentContext.find(Client.class, ClientId);
        persistentContext.delete(client);
        Client loadClient = (Client) persistentContext.find(Client.class, ClientId);
        assertEquals(null, loadClient);
        persistentContext.getTransaction().commit();
        persistentContext.close();
        Client reloadedClient = (Client) load(Client.class, client.getId());
        assertEquals(null, reloadedClient);
    }
}
