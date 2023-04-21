package com.sebscape.sebcms.pages.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.appengine.api.datastore.Key;
import com.sebscape.sebcms.pages.domain.SiteMapEntry;
import com.sebscape.sebcms.persistence.PersistenceManagerFactoryWrapper;
import com.sebscape.sebcms.persistence.dao.GenericDao;

/**
 * @author Stephen
 *
 */
public class SiteMapEntryDao extends GenericDao<SiteMapEntry, Long> {

    private static Log log = LogFactory.getLog(SiteMapEntry.class);

    /**
	 * @param clazz
	 */
    public SiteMapEntryDao() {
        super(SiteMapEntry.class);
    }

    @SuppressWarnings("unchecked")
    public SiteMapEntry findByName(String name) {
        SiteMapEntry page = null;
        PersistenceManager pm = PersistenceManagerFactoryWrapper.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            List<SiteMapEntry> result = (List<SiteMapEntry>) pm.newQuery(persistentClass, "name=='" + name + "'").execute();
            if (result != null) {
                SiteMapEntry p = result.get(0);
                if (p != null) {
                    try {
                        p.getChildSiteMapEntryKeyList().size();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                page = pm.detachCopy(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tx.commit();
            pm.close();
        }
        if (page != null) {
            initChildren(page);
        }
        return page;
    }

    @Override
    public SiteMapEntry load(Long id) {
        SiteMapEntry page = null;
        PersistenceManager pm = PersistenceManagerFactoryWrapper.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            SiteMapEntry p = pm.getObjectById(persistentClass, id);
            if (p != null) {
                try {
                    p.getChildSiteMapEntryKeyList().size();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            page = pm.detachCopy(p);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tx.commit();
            pm.close();
        }
        if (page != null) {
            initChildren(page);
        }
        return page;
    }

    /**
	 * using the groupIdList values, fetch and set the list of Groups.
	 * @param 
	 */
    private void initChildren(SiteMapEntry page) {
        log.debug("begin initGroups...");
        SiteMapEntryDao dao = new SiteMapEntryDao();
        page.setChildSiteMapEntryKeyList(new ArrayList<Key>());
        if (page.getChildSiteMapEntryKeyList() == null) {
            log.debug("groupKeySet was null...creating new/empty one.");
            page.setChildSiteMapEntryKeyList(new ArrayList<Key>());
        }
        log.debug("begin loop thru page.group keys...");
        for (Key gKey : page.getChildSiteMapEntryKeyList()) {
            log.debug("Loading key/id: " + gKey + " / " + gKey.getId());
            SiteMapEntry g = (SiteMapEntry) dao.load(gKey.getId());
            if (g != null) {
                log.debug("Added SiteMapEntry: " + g.getName());
                page.getChildSiteMapEntryKeyList().add(gKey);
            }
        }
    }

    @Override
    public SiteMapEntry save(SiteMapEntry page) {
        return super.save(page);
    }

    @Override
    public SiteMapEntry update(SiteMapEntry page) {
        return super.update(page);
    }
}
