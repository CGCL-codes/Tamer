package com.express.dao.jpa;

import com.express.dao.ProjectDao;
import com.express.domain.Project;
import com.express.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository("projectDao")
public class JpaProjectDao extends JpaGenericDao<Project> implements ProjectDao {

    @Autowired
    public JpaProjectDao(EntityManagerFactory entityManagerFactory) {
        super(Project.class);
        super.setEntityManagerFactory(entityManagerFactory);
    }

    @SuppressWarnings("unchecked")
    public List<Project> findAll() {
        return this.getJpaTemplate().findByNamedQuery(Project.QUERY_FIND_ALL);
    }

    @SuppressWarnings("unchecked")
    public List<Project> findAll(User user) {
        return this.getJpaTemplate().findByNamedQuery(Project.QUERY_FIND_WORKING_ON, user);
    }

    @SuppressWarnings("unchecked")
    public List<Project> findAvailable(User user) {
        return this.getJpaTemplate().findByNamedQuery(Project.QUERY_FIND_NOT_WORKING_ON, user);
    }
}
