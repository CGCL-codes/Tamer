package edu.asu.commons.mme.dao;

import edu.asu.commons.mme.entity.StudentStrategy;

public class HibernateStudentStrategyDao extends HibernateDao<StudentStrategy> {

    public HibernateStudentStrategyDao() {
        super(StudentStrategy.class);
    }
}
