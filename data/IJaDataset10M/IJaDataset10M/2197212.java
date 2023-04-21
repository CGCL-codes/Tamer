package org.appspy.server.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.appspy.server.bo.Dashboard;
import org.appspy.server.bo.DashboardColumn;
import org.appspy.server.bo.DashboardParamBinding;
import org.appspy.server.bo.DashboardReport;
import org.appspy.server.bo.Report;
import org.appspy.server.bo.ReportCategory;
import org.appspy.server.bo.ReportEntityListener;
import org.appspy.server.bo.ReportParam;
import org.appspy.server.bo.ReportParamChooser;
import org.appspy.server.bo.ReportingSystem;
import org.appspy.server.dao.ReportDao;
import org.appspy.server.dao.xml.XmlClasspathResourceLoader;
import org.appspy.server.dao.xml.XmlDataLoader;
import org.appspy.server.dao.xml.XmlResourceLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.thoughtworks.xstream.XStream;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class ReportDaoImpl extends JpaDaoSupport implements ReportDao, InitializingBean, BeanFactoryAware {

    protected BeanFactory mBeanFactory = null;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ReportCategory saveReportCategory(ReportCategory cat) {
        getJpaTemplate().persist(cat);
        return cat;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ReportCategory updateReportCategory(ReportCategory cat) {
        return getJpaTemplate().merge(cat);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeReportCategory(ReportCategory cat) {
        getJpaTemplate().remove(findReportCategoryById(cat.getId()));
    }

    public ReportCategory findReportCategoryById(Long catId) {
        return getJpaTemplate().find(ReportCategory.class, catId);
    }

    @SuppressWarnings("unchecked")
    public ReportCategory findReportCategoryByName(final String catName) {
        Collection<ReportCategory> queryResult = getJpaTemplate().executeFind(new JpaCallback() {

            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query query = em.createQuery("from ReportCategory as r where r.mName = :catName");
                query.setParameter("catName", catName);
                return query.getResultList();
            }
        });
        ReportCategory result = null;
        if (!queryResult.isEmpty()) {
            result = queryResult.iterator().next();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Collection<ReportCategory> findAllReportCategories() {
        return getJpaTemplate().find("from ReportCategory");
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Report saveReport(Report report) {
        getJpaTemplate().persist(report);
        return report;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Report updateReport(Report report) {
        report.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        HashMap<Long, ReportParam> paramMap = new HashMap<Long, ReportParam>();
        for (ReportParam param : report.getParams()) {
            if (param.getId() != null) {
                paramMap.put(param.getId(), param);
            }
        }
        ArrayList<ReportParam> toRemove = new ArrayList<ReportParam>();
        Report r = findReportById(report.getId());
        for (ReportParam param : r.getParams()) {
            if (!paramMap.containsKey(param.getId())) {
                toRemove.add(param);
            }
        }
        if (report.getReportContent() == null) {
            Report reportForContent = findReportById(report.getId());
            report.setReportContent(reportForContent.getReportContent());
            new ReportEntityListener().zipReportContent(report);
        }
        Report merged = getJpaTemplate().merge(report);
        for (ReportParam reportParam : toRemove) {
            getJpaTemplate().remove(reportParam);
        }
        return merged;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeReport(Report report) {
        Report r = findReportById(report.getId());
        ArrayList<ReportParam> toRemove = new ArrayList<ReportParam>();
        for (ReportParam param : r.getParams()) {
            toRemove.add(param);
        }
        getJpaTemplate().remove(findReportById(report.getId()));
        for (ReportParam reportParam : toRemove) {
            getJpaTemplate().remove(reportParam);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Report findReportById(Long reportId) {
        return getJpaTemplate().find(Report.class, reportId);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Report findReportByName(final String reportName) {
        Collection<Report> queryResult = getJpaTemplate().executeFind(new JpaCallback() {

            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query query = em.createQuery("from Report as r where r.mName = :reportName");
                query.setParameter("reportName", reportName);
                return query.getResultList();
            }
        });
        Report result = null;
        if (!queryResult.isEmpty()) {
            result = queryResult.iterator().next();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Collection<Report> findAllReports() {
        ArrayList<Report> result = new ArrayList<Report>();
        Collection<Report> reports = getJpaTemplate().find("SELECT r FROM Report r LEFT OUTER JOIN FETCH r.mParams");
        for (Report report : reports) {
            if (!result.contains(report)) {
                result.add(report);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Collection<ReportingSystem> findAllReportingSystems() {
        return getJpaTemplate().find("from ReportingSystem");
    }

    public ReportingSystem findReportingSystemById(Long sysId) {
        return getJpaTemplate().find(ReportingSystem.class, sysId);
    }

    @SuppressWarnings("unchecked")
    public ReportingSystem findReportingSystemByName(final String sysName) {
        Collection<ReportingSystem> queryResult = getJpaTemplate().executeFind(new JpaCallback() {

            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query query = em.createQuery("from ReportingSystem as sys where sys.mName = :sysName");
                query.setParameter("sysName", sysName);
                return query.getResultList();
            }
        });
        ReportingSystem result = null;
        if (!queryResult.isEmpty()) {
            result = queryResult.iterator().next();
        }
        return result;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ReportingSystem saveReportingSystem(ReportingSystem sys) {
        getJpaTemplate().persist(sys);
        return sys;
    }

    @SuppressWarnings("unchecked")
    public Collection<ReportParamChooser> findAllReportParamChoosers() {
        return getJpaTemplate().find("from ReportParamChooser");
    }

    public ReportParamChooser findReportParamChooserById(Long chooserId) {
        return getJpaTemplate().find(ReportParamChooser.class, chooserId);
    }

    @SuppressWarnings("unchecked")
    public ReportParamChooser findReportParamChooserByName(final String chooserName) {
        Collection<ReportParamChooser> queryResult = getJpaTemplate().executeFind(new JpaCallback() {

            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query query = em.createQuery("from ReportParamChooser as c where c.mName = :chooserName");
                query.setParameter("chooserName", chooserName);
                return query.getResultList();
            }
        });
        ReportParamChooser result = null;
        if (!queryResult.isEmpty()) {
            result = queryResult.iterator().next();
        }
        return result;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ReportParamChooser saveReportParamChooser(ReportParamChooser chooser) {
        getJpaTemplate().persist(chooser);
        return chooser;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Collection<Dashboard> findAllDashboards() {
        ArrayList<Dashboard> result = new ArrayList<Dashboard>();
        Collection<Dashboard> dashboards = getJpaTemplate().find("SELECT d FROM Dashboard d LEFT OUTER JOIN FETCH d.mParams");
        for (Dashboard dashboard : dashboards) {
            if (!result.contains(dashboard)) {
                result.add(dashboard);
            }
        }
        return result;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Dashboard findDashboardById(Long dashboardId) {
        return getJpaTemplate().find(Dashboard.class, dashboardId);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Dashboard findDashboardByName(final String name) {
        Collection<Dashboard> queryResult = getJpaTemplate().executeFind(new JpaCallback() {

            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query query = em.createQuery("from Dashboard as d where d.mName = :dashboardName");
                query.setParameter("dashboardName", name);
                return query.getResultList();
            }
        });
        Dashboard result = null;
        if (!queryResult.isEmpty()) {
            result = queryResult.iterator().next();
        }
        return result;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeDashboard(Dashboard dashboard) {
        Dashboard d = findDashboardById(dashboard.getId());
        for (ReportParam param : d.getParams()) {
            getJpaTemplate().remove(param);
        }
        for (DashboardColumn dashboardColumn : d.getColumns()) {
            deleteDashboardColumn(dashboardColumn);
        }
        getJpaTemplate().remove(findDashboardById(dashboard.getId()));
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Dashboard saveDashboard(Dashboard dashboard) {
        getJpaTemplate().persist(dashboard);
        return dashboard;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Dashboard updateDashboard(Dashboard dashboard) {
        Dashboard d = findDashboardById(dashboard.getId());
        HashMap<Long, ReportParam> paramMap = new HashMap<Long, ReportParam>();
        for (ReportParam param : dashboard.getParams()) {
            if (param.getId() != null) {
                paramMap.put(param.getId(), param);
            }
        }
        for (ReportParam param : d.getParams()) {
            if (!paramMap.containsKey(param.getId())) {
                getJpaTemplate().remove(param);
            }
        }
        HashMap<Long, DashboardColumn> columnMap = new HashMap<Long, DashboardColumn>();
        for (DashboardColumn dashboardColumn : dashboard.getColumns()) {
            if (dashboardColumn.getId() != null) {
                columnMap.put(dashboardColumn.getId(), dashboardColumn);
            }
        }
        for (DashboardColumn dashboardColumn : d.getColumns()) {
            if (!columnMap.containsKey(dashboardColumn.getId())) {
                getJpaTemplate().remove(dashboardColumn);
            }
        }
        for (DashboardColumn dashboardColumn : dashboard.getColumns()) {
            if (dashboardColumn.getId() != null) {
                updateDashboardColumn(dashboardColumn);
            }
        }
        Dashboard merged = getJpaTemplate().merge(dashboard);
        return merged;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateDashboardColumn(DashboardColumn dashboardColumn) {
        DashboardColumn old = findDashboardColumnById(dashboardColumn.getId());
        HashMap<Long, DashboardReport> dashboardReports = new HashMap<Long, DashboardReport>();
        for (DashboardReport dashboardReport : dashboardColumn.getDashboardReports()) {
            dashboardReports.put(dashboardReport.getId(), dashboardReport);
        }
        for (DashboardReport dashboardReport : old.getDashboardReports()) {
            if (!dashboardReports.containsKey(dashboardReport.getId())) {
                getJpaTemplate().remove(dashboardReport);
            }
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public DashboardColumn findDashboardColumnById(Long dashboardColumnId) {
        return getJpaTemplate().find(DashboardColumn.class, dashboardColumnId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected void deleteDashboardColumn(DashboardColumn dashboardColumn) {
        for (DashboardReport dashboardReport : dashboardColumn.getDashboardReports()) {
            deleteDashboardReport(dashboardReport);
        }
        getJpaTemplate().remove(dashboardColumn);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected void deleteDashboardReport(DashboardReport dashboardReport) {
        for (DashboardParamBinding paramBinding : dashboardReport.getDashboardParamBindings()) {
            deleteDashboardParamBinding(paramBinding);
        }
        getJpaTemplate().remove(dashboardReport);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected void deleteDashboardParamBinding(DashboardParamBinding paramBinding) {
        getJpaTemplate().remove(paramBinding);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initDao() throws Exception {
        super.initDao();
        Collection<ReportingSystem> reportingSystems = findAllReportingSystems();
        XmlResourceLoader resourceLoader = new XmlClasspathResourceLoader();
        if (reportingSystems == null || reportingSystems.size() == 0) {
            XStream xstream = new XStream();
            Collection<Object> initialDatas = (Collection<Object>) xstream.fromXML(getClass().getClassLoader().getResourceAsStream("org/appspy/server/xml/initial-report-data.xml"));
            loadObjects(initialDatas, resourceLoader);
        }
    }

    public void loadObjects(Collection<Object> objects, XmlResourceLoader resourceLoader) {
        for (Object data : objects) {
            XmlDataLoader dataLoader = (XmlDataLoader) mBeanFactory.getBean(data.getClass().getName() + ".dataLoader");
            dataLoader.loadData(data, resourceLoader);
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        mBeanFactory = beanFactory;
    }
}
