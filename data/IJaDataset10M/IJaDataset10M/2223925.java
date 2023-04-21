package gal.base.dao.hibernate;

import gal.base.dao.ISettingsDAO;
import gal.base.dao.common.impl.AbstractDAO;
import gal.base.mapping.Settings;
import java.util.List;

/**
 * @author Benjamin
 * 
 */
public class SettingsDAOHibernate extends AbstractDAO<Settings> implements ISettingsDAO {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final Settings loadSettings() {
        final List<Object> result = getHibernateTemplate().loadAll(Settings.class);
        Settings mySettings = null;
        if (result != null && !result.isEmpty()) {
            mySettings = (Settings) result.get(0);
        }
        return mySettings;
    }
}
