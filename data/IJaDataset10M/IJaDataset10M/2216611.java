package dk.simonvogensen.itodo.server.dao;

import dk.simonvogensen.itodo.server.TodoApiImpl;
import dk.simonvogensen.itodo.shared.model.Config;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: simon
 * Date: Oct 18, 2009
 * Time: 1:07:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigDAO {

    public Config get(String email, PersistenceManager pm) {
        Query q = pm.newQuery(Config.class, "email == email_address");
        q.declareParameters("java.lang.String email_address");
        List<Config> persistedConfigList = (List<Config>) q.execute(email);
        List<Config> detachedConfigList = (List<Config>) pm.detachCopyAll(persistedConfigList);
        if (detachedConfigList.isEmpty()) return null; else return detachedConfigList.get(0);
    }

    public String create(Config config, PersistenceManager pm) {
        pm.makePersistent(config);
        return config.getId();
    }

    public String update(Config config, PersistenceManager pm) {
        String id = null;
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Config managedConfig = pm.getObjectById(Config.class, config.getId());
            if (managedConfig != null) {
                managedConfig.setEmail(config.getEmail());
                managedConfig.setProjectIds(config.getProjectIds());
                managedConfig.setTransportTypes(config.getTransportTypes());
                managedConfig.setEmployeeId(config.getEmployeeId());
                id = managedConfig.getId();
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            pm.close();
        }
        return id;
    }
}
