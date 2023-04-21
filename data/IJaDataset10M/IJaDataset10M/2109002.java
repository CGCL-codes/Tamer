package se.slackers.locality.dao;

import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import se.slackers.locality.model.MetaTag;
import se.slackers.locality.model.Tag;

public class TagTest extends AbstractTransactionalDataSourceSpringContextTests {

    protected TagDao tagDao;

    protected MetaTagDao metaTagDao;

    protected SessionFactory sessionFactory = null;

    public void testManyToMany() {
        Tag tag = new Tag();
        MetaTag metatag = new MetaTag();
        tag.setName("tag");
        metatag.setName("metatag");
        metatag.addTag(tag);
        tagDao.save(tag);
        metaTagDao.save(metatag);
        System.out.println(tagDao.get(tag.getId()));
        System.out.println(metaTagDao.get(metatag.getId()));
    }

    protected String[] getConfigLocations() {
        return new String[] { "application-context/main.xml", "application-context/data.xml" };
    }

    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public void setMetaTagDao(MetaTagDao metaTagDao) {
        this.metaTagDao = metaTagDao;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
