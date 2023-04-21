package net.mufly.server.gwt;

import java.util.List;
import net.mufly.client.core.MuflyErrorException;
import net.mufly.client.services.TagRemote;
import net.mufly.domain.Tag;
import net.mufly.server.ApplicationContext;
import net.mufly.server.dao.TagDao;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

public class TagRemoteImpl extends PersistentRemoteService implements TagRemote {

    private static final long serialVersionUID = 6355456831563550489L;

    private TagDao tagDao;

    private HibernateUtil gileadHibernateUtil = new HibernateUtil();

    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public TagDao getTagDao() {
        return this.tagDao;
    }

    public TagRemoteImpl() {
        tagDao = (TagDao) ApplicationContext.getBean("tagDao", ApplicationContext.CONTEXT_NAME);
        gileadHibernateUtil.setSessionFactory(net.mufly.server.HibernateUtil.getSessionFactory());
        PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
        persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
        persistentBeanManager.setProxyStore(new StatelessProxyStore());
        setBeanManager(persistentBeanManager);
    }

    public List<Tag> getAllTags() {
        return tagDao.getAllTags();
    }

    public void addTag(Tag tag) throws MuflyErrorException {
        tagDao.addTag(tag);
    }

    public void updateTag(Tag tag) throws MuflyErrorException {
        tagDao.updateTag(tag);
    }

    public void deleteTag(Tag tag) throws MuflyErrorException {
        tagDao.deleteTag(tag);
    }
}
