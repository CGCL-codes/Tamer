package org.appspy.server.dao.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.appspy.server.bo.scheduler.JobDeclaration;
import org.appspy.server.bo.scheduler.JobDeclarationParam;
import org.appspy.server.bo.scheduler.JobExecution;
import org.appspy.server.bo.scheduler.JobSchedule;
import org.appspy.server.bo.scheduler.JobScheduleParam;
import org.appspy.server.dao.SchedulerDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class SchedulerDaoImpl extends JpaDaoSupport implements SchedulerDao, InitializingBean, BeanFactoryAware {

    protected BeanFactory mBeanFactory = null;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public JobDeclaration createJobDeclaration(JobDeclaration jobDeclaration) {
        getJpaTemplate().persist(jobDeclaration);
        return jobDeclaration;
    }

    @SuppressWarnings("unchecked")
    public Collection<JobDeclaration> getAllJobDeclarations() {
        return getJpaTemplate().find("from JobDeclaration");
    }

    public JobDeclaration findJobDeclarationById(Long id) {
        return getJpaTemplate().find(JobDeclaration.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeJobDeclaration(JobDeclaration jobDeclaration) {
        getJpaTemplate().remove(findJobDeclarationById(jobDeclaration.getId()));
    }

    public JobExecution findJobExecutionById(Long id) {
        return getJpaTemplate().find(JobExecution.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public JobDeclaration updateJobDeclaration(JobDeclaration jobDeclaration) {
        return getJpaTemplate().merge(jobDeclaration);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public JobSchedule createJobSchedule(JobSchedule jobSchedule) {
        getJpaTemplate().persist(jobSchedule);
        return jobSchedule;
    }

    public JobSchedule findJobScheduleById(Long id) {
        JobSchedule result = getJpaTemplate().find(JobSchedule.class, id);
        updateScheduleParams(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public Collection<JobSchedule> findAllJobSchedules() {
        Collection<JobSchedule> result = getJpaTemplate().find("from JobSchedule");
        for (JobSchedule jobSchedule : result) {
            updateScheduleParams(jobSchedule);
        }
        return result;
    }

    protected void updateScheduleParams(JobSchedule jobSchedule) {
        for (JobDeclarationParam jobDeclarationParam : jobSchedule.getJobDeclaration().getParams()) {
            boolean found = false;
            for (JobScheduleParam jobScheduleParam : jobSchedule.getParams()) {
                if (jobScheduleParam.getJobDeclarationParam() == jobDeclarationParam) {
                    found = true;
                }
            }
            if (!found) {
                JobScheduleParam jobScheduleParam = new JobScheduleParam();
                jobScheduleParam.setJobDeclarationParam(jobDeclarationParam);
                jobScheduleParam.setJobSchedule(jobSchedule);
                jobSchedule.getParams().add(jobScheduleParam);
            }
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeJobSchedule(JobSchedule jobSchedule) {
        JobSchedule js = findJobScheduleById(jobSchedule.getId());
        getJpaTemplate().remove(js);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public JobSchedule updateJobSchedule(JobSchedule jobSchedule) {
        return getJpaTemplate().merge(jobSchedule);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public JobExecution createJobExecution(JobExecution jobExecution) {
        getJpaTemplate().persist(jobExecution);
        return jobExecution;
    }

    @SuppressWarnings("unchecked")
    public Collection<JobExecution> findAllJobExecutions() {
        return getJpaTemplate().find("from JobExecution");
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeJobExecution(JobExecution jobExecution) {
        JobExecution je = findJobExecutionById(jobExecution.getId());
        getJpaTemplate().remove(je);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeJobExecutionsByDate(final Date date) {
        List<JobExecution> jobExecutions = getJpaTemplate().executeFind(new JpaCallback() {

            @SuppressWarnings("unchecked")
            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query query = em.createQuery("from JobExecution where mStartTime < :date");
                query.setParameter("date", date);
                List<JobExecution> jobExecutions = query.getResultList();
                return jobExecutions;
            }
        });
        for (JobExecution jobExecution : jobExecutions) {
            removeJobExecution(jobExecution);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public JobExecution updateJobExecution(JobExecution jobExecution) {
        return getJpaTemplate().merge(jobExecution);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected void initDao() throws Exception {
        super.initDao();
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        mBeanFactory = beanFactory;
    }
}
