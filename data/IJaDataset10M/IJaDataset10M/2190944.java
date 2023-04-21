package com.neethi.dao.hibernate;

import java.util.List;
import com.neethi.dao.PracticeAreaDao;
import com.neethi.model.PracticeArea;

public class PracticeAreaDaoHibernate extends GenericDaoHibernate<PracticeArea, Long> implements PracticeAreaDao {

    /**
     * Constructor to create a Generics-based version using PracticeArea as the entity
     */
    public PracticeAreaDaoHibernate() {
        super(PracticeArea.class);
    }

    public PracticeArea getPracticeAreaByName(String pAreaName) {
        List<PracticeArea> pAreaList = getHibernateTemplate().find("from PracticeArea where name=?", pAreaName);
        if (pAreaList.isEmpty()) {
            return null;
        } else {
            return (PracticeArea) pAreaList.get(0);
        }
    }
}
