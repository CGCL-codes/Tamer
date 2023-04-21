package com.firescrum.bugtracking.dao;

import java.util.List;
import com.firescrum.bugtracking.model.Priority;
import com.firescrum.bugtracking.model.StatusMapping;
import com.firescrum.infrastructure.dao.BaseDao;

public final class DaoStatusMapping extends BaseDao<StatusMapping> implements IDaoStatusMapping {

    public DaoStatusMapping() {
        super();
    }

    public List<StatusMapping> getStatusMapping(StatusMapping statusMapping) {
        return super.getHibernateTemplate().find("FROM StatusMapping order by id");
    }
}
