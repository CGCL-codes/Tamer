package megatennis.persistent.dao;

import java.util.List;
import megatennis.business.Settings;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ivan Klim
 */
public class HibernateSettingsDAO extends HibernateBaseDAO<Settings> {

    /**
     * Gets settings by its id.
     * @param id
     * @return settings with specified id, or null if not found.
     */
    public Settings getById(Long id) {
        return getById(Settings.class, id);
    }

    /**
     * Gets settings by name.
     * @param name
     * @return object of class Settings with specified name, or null if there are no settings with such name
     */
    public Settings getBySettingsName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Settings.class);
        criteria.add(Restrictions.eq("settingsNamePersistent", name));
        List<Settings> settingsList = getAllByCriteria(criteria);
        if (settingsList.size() > 1) {
            throw new RuntimeException("Several settings have name '" + name + "'");
        } else if (settingsList.size() == 0) {
            return null;
        }
        return settingsList.get(0);
    }

    /**
     * Gets all existing in data source settings.
     * @param name
     * @return list of all the settings in the data source
     */
    public List<Settings> getAll() {
        return getAll(Settings.class);
    }
}
