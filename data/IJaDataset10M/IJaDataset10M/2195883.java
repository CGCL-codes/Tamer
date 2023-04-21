package net.sourceforge.agiltestlist.core.dao;

import static org.junit.Assert.*;
import net.sourceforge.agiltestlist.core.Project;
import net.sourceforge.agiltestlist.core.dao.impl.ProjectDAOImpl;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/dao-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class ProjectDAOTest extends DatabaseTest {

    @Autowired
    private ProjectDAOImpl projectDAO;

    @Test
    @Transactional
    public void testSave() throws Exception {
        String projectName = "Test Projekt III";
        Project project = new Project();
        project.setName(projectName);
        projectDAO.save(project);
        projectDAO.flush();
        IDataSet dataSet = getConnection().createDataSet();
        ITable table = dataSet.getTable("project");
        assertEquals(projectName, table.getValue(1, "name"));
    }

    @Test
    @Transactional
    public void testLoadById() {
        Project project = projectDAO.loadById(1);
        assertEquals("Test Project", project.getName());
    }

    @Test
    @Transactional
    public void testDelete() throws Exception {
        Project project = projectDAO.loadById(1);
        projectDAO.delete(project);
        projectDAO.flush();
        IDataSet dataSet = getConnection().createDataSet();
        ITable table = dataSet.getTable("project");
        assertEquals(0, table.getRowCount());
    }
}
