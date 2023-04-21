package com.peusoft.ptcollect.server.service.persistance.dao;

import com.peusoft.ptcollect.core.persistance.domain.Project;
import com.peusoft.ptcollect.core.persistance.domain.User;
import com.peusoft.ptcollect.core.persistance.domain.WorkDayTimeRecord;
import com.peusoft.ptcollect.server.persistance.dao.WorkDayTimeRecordDao;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * DAO for WorkDayTimeRecord
 * 
 * @author zhenja
 *
 */
@Repository("workDayTimeRecordDao")
public class WorkDayTimeRecordDaoImpl extends AbstractDaoImpl<Long, WorkDayTimeRecord> implements WorkDayTimeRecordDao {

    private static final String Q_PROJECT = "select * from WorkDayTimeRecord wd where wd.userProjectActivity.user = ?1 " + "and wd.userProjectActivity.projectActivity.project = ?2 " + "and wd.day.date between ?3 and ?4";

    private static final String Q_NO_PROJECT = "select * from WorkDayTimeRecord wd where wd.userProjectActivity.user = ?1 " + "and wd.day.date between ?2 and ?3";

    /**
     * @see AbstractDAO#AbstractDAO()
     */
    public WorkDayTimeRecordDaoImpl() {
        super(WorkDayTimeRecord.class);
    }

    @Override
    public List<WorkDayTimeRecord> find(User user, Project project, Date begin, Date end) {
        return project == null ? find(Q_NO_PROJECT, user, begin, end) : find(Q_PROJECT, user, project, begin, end);
    }
}
