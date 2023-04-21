package org.jpasample.dao.impl;

import java.util.List;
import org.jpasample.dao.BaseDAO;
import org.jpasample.dao.ReleaseDAO;
import org.jpasample.model.Release;

public class JpaReleaseDAO extends BaseDAO implements ReleaseDAO {

    public void delete(Release release) {
        getJpaTemplate().remove(release);
    }

    public List<Release> findAll() {
        return getJpaTemplate().find("select s from Release s");
    }

    public Release findById(long id) {
        return getJpaTemplate().find(Release.class, id);
    }

    public List<Release> findByName(String name) {
        return getJpaTemplate().find("select s from Release s where s.name = ?1", name);
    }

    public Release save(Release newRelease) {
        getJpaTemplate().persist(newRelease);
        return newRelease;
    }

    public Release update(Release release) {
        getJpaTemplate().merge(release);
        return release;
    }
}
