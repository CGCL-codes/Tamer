package org.fao.fenix.web.dao4.perspective;

import java.util.Iterator;
import java.util.List;
import org.fao.fenix.domain.perspective.Project;
import org.fao.fenix.domain4.client.perspective.ChartView;
import org.fao.fenix.persistence.DaoTestDelegate;
import org.fao.fenix.persistence.perspective.ProjectDao;
import org.fao.fenix.web.dao4.BaseDao4Test;

/**
 * Test class to test Chart4Dao
 * 
 * AbstractJpaTests is part of Spring. I could not find AbstractJpaTests in the Spring jars because it is part of the
 * Spring test classs directory. This class is that use full together with some others that I exported them into
 * spring-test-erik.jar. TODO find a better solution for spring-test-erik.jar
 * 
 * 
 * @author Erik van Ingen
 */
public class Chart4DaoTest extends BaseDao4Test {

    protected Chart4Dao chart4Dao;

    protected ProjectDao projectDao;

    public void setChart4Dao(Chart4Dao chart4Dao) {
        this.chart4Dao = chart4Dao;
    }

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        Project project = dog.getProject();
        project.addChartView((org.fao.fenix.domain.perspective.ChartView) dog.getDomainObjectFilled(org.fao.fenix.domain.perspective.ChartView.class));
        projectDao.save(project);
    }

    public void testFindByIdWhereResourceExists() {
        List<ChartView> ml = chart4Dao.findAll();
        ChartView m = (ChartView) ml.get(0);
        assertNotNull(m);
        assertEquals(dog.getDescription(), m.getDescription());
    }

    public void testFindByIdWhereResourceDoesNotExist() {
        ChartView chart = chart4Dao.findById(99);
        assertNull(chart);
    }

    public void testFindByLabelWhereResourceExists() {
        List<ChartView> charts = chart4Dao.findByLabel(dog.getLabel());
        assertEquals(1, charts.size());
        ChartView project = charts.get(0);
        assertEquals(dog.getDescription(), project.getDescription());
    }

    public void testFindByLabelWhereResourceDoesNotExist() {
        List<ChartView> charts = chart4Dao.findByLabel("No Such Map");
        assertEquals(0, charts.size());
    }

    public void testFindByProject() {
        List<Project> projects = projectDao.findAll();
        Iterator it = projects.iterator();
        Project elem = null;
        while (it.hasNext()) {
            elem = (Project) it.next();
            System.out.println("elem.getChartViewList().size() ============" + elem.getChartViewList().size());
        }
        org.fao.fenix.domain.perspective.ChartView chart = (org.fao.fenix.domain.perspective.ChartView) elem.getChartViewList().toArray()[0];
        long otherMapID = chart.getResourceId();
        assertTrue(projects.size() > 0);
        List<ChartView> charts = chart4Dao.findByProject(elem.getResourceId());
        assertTrue(charts.size() > 0);
        ChartView chartFound = (ChartView) charts.get(charts.size() - 1);
        assertEquals(otherMapID, chartFound.getResourceId().longValue());
    }

    /**
     * 
     * 
     */
    public void testSave1() {
        org.fao.fenix.domain.perspective.ChartView testObject = (org.fao.fenix.domain.perspective.ChartView) dog.getDomainObjectFilled(org.fao.fenix.domain.perspective.ChartView.class);
        ChartView chartView = (ChartView) util.map(testObject, ChartView.class);
        DaoTestDelegate delegate = new DaoTestDelegate();
        delegate.testSave(chart4Dao, chartView, Chart4Dao.class, ChartView.class);
    }

    /**
     * 
     * Test to test the cardinality of Project with ChartView
     * 
     */
    public void testSave2() {
        List<Project> projectList1 = projectDao.findAll();
        List<ChartView> chartList1 = chart4Dao.findAll();
        chart4Dao.save((ChartView) util.map((ChartView) util.map(dog.getDomainObjectFilled(org.fao.fenix.domain.perspective.ChartView.class), ChartView.class), ChartView.class));
        Project project = dog.getProject();
        project.addChartView((org.fao.fenix.domain.perspective.ChartView) dog.getDomainObjectFilled(org.fao.fenix.domain.perspective.ChartView.class));
        projectDao.save(project);
        List<Project> projectList2 = projectDao.findAll();
        List<ChartView> chartList2 = chart4Dao.findAll();
        assertEquals((projectList1.size() + 1), projectList2.size());
        assertEquals((chartList1.size() + 2), chartList2.size());
        Project project2 = projectDao.findById(project.getResourceId());
        List<org.fao.fenix.domain.perspective.ChartView> charts = project2.getChartViewList();
        assertEquals(1, charts.size());
        List<ChartView> sameList = chart4Dao.findByProject(project.getResourceId());
        assertEquals(1, sameList.size());
    }
}
