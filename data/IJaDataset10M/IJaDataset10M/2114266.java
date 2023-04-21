package ru.ssau.university.persistence.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.ssau.hibernate.util.AbstractDAO;
import ru.ssau.university.persistence.entity.Progress;

public class ProgressDAO extends AbstractDAO<Progress> {

    public ProgressDAO() {
        this.objectClass = Progress.class;
    }

    public Progress getProgress(Long studentId, Long planItemId, int numb) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("student.id", studentId));
        criteria.add(Restrictions.eq("planItem.id", planItemId));
        criteria.add(Restrictions.eq("numb", numb));
        return (Progress) criteria.uniqueResult();
    }
}
