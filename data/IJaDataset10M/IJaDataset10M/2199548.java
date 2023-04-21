package com.miragedev.mononara.core.dao.jpa;

import com.miragedev.mononara.core.dao.TagDao;
import com.miragedev.mononara.core.model.Tag;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Nickho
 * Date: Jan 2, 2008
 * Time: 10:42:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class JpaTagDao extends JpaDaoSupport implements TagDao {

    public Tag findByCode(String tag) {
        return getJpaTemplate().find(Tag.class, tag);
    }

    public List<Tag> findAll() {
        return getJpaTemplate().find("SELECT t FROM Tag t");
    }

    public void save(Tag tag) {
        getJpaTemplate().persist(tag);
    }

    public Tag update(Tag tag) {
        return getJpaTemplate().merge(tag);
    }

    public void delete(Tag tag) {
        getJpaTemplate().remove(tag);
    }

    public void refresh(Tag tag) {
        getJpaTemplate().refresh(tag);
    }

    public boolean exist(Tag tag) {
        return getJpaTemplate().contains(tag);
    }
}
