package by.brsu.portal.project;

import org.junit.Assert;
import org.junit.Test;
import by.brsu.portal.PortalTechnicalException;
import by.brsu.portal.user.User;

/**
 * @author Roman Ulezlo
 *
 */
public class ProjectHistoryDAOTest {

    @Test
    public void testCreateTechnology() throws PortalTechnicalException {
        ProjectHistoryDAO td = new ProjectHistoryDAO();
        User user = new User();
        user.setId(1);
        Project project = new Project();
        project.setIdProject(1);
        ProjectHistory t1 = td.createProjectHistory(1, 1, "ttt", "yyy");
        System.out.println("test1 " + t1);
        Assert.assertTrue(t1 != null);
    }
}
