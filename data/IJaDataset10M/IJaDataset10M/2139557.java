package net.sourceforge.traffiscope.entity.jpa;

import net.sourceforge.traffiscope.model.ConfigTO;
import net.sourceforge.traffiscope.model.DatabaseRemote;
import net.sourceforge.traffiscope.model.dao.ConfigDAO;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateless
@Remote(DatabaseRemote.class)
public class ConfigAccess extends AbstractIdAccess<Config, ConfigTO> implements ConfigDAO {

    public ConfigAccess() {
        super(Config.class, ConfigTO.class);
    }

    @Override
    public ConfigTO getConfig() {
        Config instance = getConfigEntity();
        return (instance == null) ? null : instance.buildTO();
    }

    protected Config getConfigEntity() {
        Query q = entityManager.createNamedQuery(Config.QUERY_GET_CONFIG);
        try {
            return (entityClass.cast(q.getSingleResult()));
        } catch (NoResultException x) {
            return null;
        }
    }

    @Override
    protected void createHook(Config entity, ConfigTO to) {
        super.createHook(entity, to);
    }
}
