package net.sf.buildbox.buildrobot.hibernate;

import net.sf.buildbox.buildrobot.api.ResourceRegistryDao;
import net.sf.buildbox.buildrobot.model.ResourceRegistryEntry;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Yes there is a lot of soft on net that is called "distributed cache" or "distributed filesystem".
 * However, all what I found is a lot more complex than what I need.
 * So I will first implement a simple one, covering exactly my needs; then I will see if it makes sense to switch to an existing solution.
 * <p/>
 * Requirements:
 * - provide a registry for resources cached on various machines
 * - resource is identified by key (created by caller)
 * - locations are typically URLs stored as strings (no url syntax checking)
 * - for given key, a new location is added after a worker successfully downloads the resource; removed before his housekeeper starts dropping it
 * <p/>
 * Ideas (optional requirements)
 * - keep stat of upload/download times, to provide locations ordered by speed
 * - similarly, some servers are unreachable from others; perhaps this might be tracked here
 *
 * @author Petr Kozelka
 */
public class ResourceRegistryHibernateImpl extends HibernateDaoSupport implements ResourceRegistryDao {

    private static final Logger LOGGER = Logger.getLogger(ResourceRegistryHibernateImpl.class.getName());

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createEntry(ResourceRegistryEntry entry) {
        getSession().save(entry);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateEntry(ResourceRegistryEntry entry) {
        getSession().update(entry);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteEntry(ResourceRegistryEntry entry) {
        getSession().delete(entry);
    }

    public ResourceRegistryEntry getEntryByPath(long executionId, String path) {
        final Criteria crit = getSession().createCriteria(ResourceRegistryEntry.class);
        crit.add(Restrictions.eq("executionId", executionId));
        crit.add(Restrictions.eq("path", path));
        @SuppressWarnings("unchecked") final List<ResourceRegistryEntry> list = (List<ResourceRegistryEntry>) crit.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<ResourceRegistryEntry> getEntriesMatchingPath(long executionId, String path) {
        final Criteria crit = getSession().createCriteria(ResourceRegistryEntry.class);
        crit.add(Restrictions.eq("executionId", executionId));
        crit.add(Restrictions.or(Restrictions.eq("path", path), Restrictions.like("path", path + "%")));
        return (List<ResourceRegistryEntry>) crit.list();
    }

    public Collection<ResourceRegistryEntry> getEntriesContainingPath(long executionId, String path) {
        final Criteria crit = getSession().createCriteria(ResourceRegistryEntry.class);
        crit.add(Restrictions.eq("executionId", executionId));
        @SuppressWarnings("unchecked") final List<ResourceRegistryEntry> list = (List<ResourceRegistryEntry>) crit.list();
        final List<ResourceRegistryEntry> result = new ArrayList<ResourceRegistryEntry>();
        for (ResourceRegistryEntry rrEntry : list) {
            if (path.startsWith(rrEntry.getPath())) {
                result.add(rrEntry);
            }
        }
        return result;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void lock(ResourceRegistryEntry entry, String locker) {
        LOGGER.warning(String.format("NOt implemented yet: %s: lock(%s)", entry.getPath(), locker));
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void unlock(ResourceRegistryEntry entry, String locker) {
        LOGGER.warning(String.format("NOt implemented yet: %s: unlock(%s)", entry.getPath(), locker));
    }
}
