package hu.ihash.database.dao.impl;

import hu.ihash.database.dao.ImageDao;
import hu.ihash.database.entities.Image;
import java.util.Collection;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

/**
 * A DAO implementation for Image entities.
 * 
 * @author Gergely Kiss
 */
@Transactional
public class ImageDaoImpl extends BaseDaoImpl<Image> implements ImageDao {

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Image> byHash(String hash) {
        return getSession().createCriteria(Image.class).add(Restrictions.eq("imageHash", hash)).list();
    }

    @Override
    public Image bySHA256(String sha256) {
        return (Image) getSession().createCriteria(Image.class).add(Restrictions.eq("sha256", sha256)).uniqueResult();
    }
}
