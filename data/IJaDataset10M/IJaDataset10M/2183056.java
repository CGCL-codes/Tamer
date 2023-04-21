package se.slackers.locality.dao.hibernate;

import java.util.List;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import se.slackers.locality.dao.MetaTagDao;
import se.slackers.locality.model.MetaTag;

public class MetaTagDaoImpl extends HibernateDaoSupport implements MetaTagDao {

    /**
	 * {@inheritDoc}
	 */
    public void delete(MetaTag tag) {
        getHibernateTemplate().delete(tag);
    }

    /**
	 * {@inheritDoc}
	 */
    @SuppressWarnings("unchecked")
    public MetaTag get(Long id) {
        List<MetaTag> result = (List<MetaTag>) getHibernateTemplate().find("from MetaTag tag fetch all properties where tag.id=?", id);
        if (result.isEmpty()) throw new DataRetrievalFailureException("No metatag with id " + id + " could be found");
        assert result.size() == 1 : "More than one metatag found with id " + id;
        return result.get(0);
    }

    /**
	 * {@inheritDoc}
	 */
    @SuppressWarnings("unchecked")
    public MetaTag get(String name) {
        List<MetaTag> result = (List<MetaTag>) getHibernateTemplate().find("from MetaTag tag fetch all properties where tag.name=?", name);
        if (result.isEmpty()) throw new DataRetrievalFailureException("No metatag with name " + name + " could be found");
        assert result.size() == 1 : "More than one metatag found with name " + name;
        return result.get(0);
    }

    /**
	 * {@inheritDoc}
	 */
    @SuppressWarnings("unchecked")
    public List<MetaTag> getLike(String name) {
        return (List<MetaTag>) getHibernateTemplate().find("from MetaTag tag fetch all properties where lower(tag.name) like ? order by tag.name", name.toLowerCase());
    }

    /**
	 * {@inheritDoc}
	 */
    public void save(MetaTag tag) {
        getHibernateTemplate().saveOrUpdate(tag);
    }
}
