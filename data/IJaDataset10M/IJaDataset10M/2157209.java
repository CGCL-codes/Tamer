package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.ProjectActivity;

@Repository
public class ProjectActivityDAOImpl implements ProjectActivityDAO {

    private Log log = LogFactory.getLog(getClass().getName());

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public ArrayList<ProjectActivity> getUserProjectActivity(int uid) {
        DetachedCriteria filter = DetachedCriteria.forClass(ProjectActivity.class);
        filter.add(Restrictions.eq("uid", uid));
        List<ProjectActivity> list = hibernateTemplate.findByCriteria(filter);
        if (list.size() == 0) return null;
        return (ArrayList<ProjectActivity>) list;
    }

    @Override
    public ArrayList<Integer> getUsersOnProject(int pid) {
        DetachedCriteria filter = DetachedCriteria.forClass(ProjectActivity.class);
        filter.add(Restrictions.eq("pid", pid));
        List<ProjectActivity> list = hibernateTemplate.findByCriteria(filter);
        if (list.size() == 0) return new ArrayList<Integer>();
        ArrayList<Integer> uids = new ArrayList<Integer>();
        for (ProjectActivity pa : list) {
            uids.add(pa.getUid());
        }
        return uids;
    }

    @Override
    public int getUsersOnProjectCount(int pid) {
        return DataAccessUtils.intResult(hibernateTemplate.find("select count(*) from ProjectActivity where pid=" + pid));
    }

    @Override
    public void addActivity(int pid, int uid) {
        log.debug("[adding activity]: pid=" + pid + " uid=" + uid);
        ProjectActivity pa = new ProjectActivity();
        pa.setPid(pid);
        pa.setUid(uid);
        hibernateTemplate.merge(pa);
    }

    @Override
    public void removeActivity(int pid, ArrayList<Integer> uids) {
        DetachedCriteria filter = DetachedCriteria.forClass(ProjectActivity.class);
        filter.add(Restrictions.eq("pid", pid));
        if (uids.size() == 1) filter.add(Restrictions.eq("uid", uids.get(0)));
        if (uids.size() > 1) filter.add(Restrictions.in("uid", uids));
        List<ProjectActivity> list = hibernateTemplate.findByCriteria(filter);
        for (ProjectActivity pa : list) {
            hibernateTemplate.delete(pa);
        }
    }

    @Override
    public boolean isProjectMember(int uid, int pid) {
        DetachedCriteria filter = DetachedCriteria.forClass(ProjectActivity.class);
        filter.add(Restrictions.eq("pid", pid));
        filter.add(Restrictions.eq("uid", uid));
        List<ProjectActivity> list = hibernateTemplate.findByCriteria(filter);
        if (list.size() > 0) return true; else return false;
    }
}
