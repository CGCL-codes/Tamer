package zipwire.rest;

import java.util.List;
import junit.framework.TestCase;
import zipwire.text.CamelCase;
import zipwire.xml.XmlNamingRule;

public class TestResource extends TestCase {

    String xmlSimpleResponse = "<?xml version=\"1.0\"?>" + "<project>" + "<id>1234</id>" + "<project-name>Zipwire</project-name>" + "</project>";

    String xmlListResponse = "<?xml version=\"1.0\"?>" + "<projects>" + "<project>" + "<id>1234</id>" + "<project-name>Zipwire</project-name>" + "</project>" + "<project>" + "<id>5678</id>" + "<project-name>REST</project-name>" + "</project>" + "</projects>";

    XmlNamingRule xmlNamingRule = new XmlNamingRule() {

        public String transform(String method) {
            return new CamelCase(method).separateWith("-").toLowerCase();
        }
    };

    interface Project {

        String getId();

        String getProjectName();
    }

    public void testResourceStatus() {
        Resource resource = new Resource(xmlNamingRule, 200, xmlSimpleResponse);
        assertEquals(200, resource.getStatus());
    }

    public void testResourceAsProject() {
        Project project = new Resource(xmlNamingRule, 200, xmlSimpleResponse).as(Project.class);
        assertEquals("1234", project.getId());
    }

    public void testResourceAsProjectList() {
        List<Project> projects = new Resource(xmlNamingRule, 200, xmlListResponse).asList(Project.class, "project");
        assertEquals("1234", projects.get(0).getId());
        assertEquals("5678", projects.get(1).getId());
        assertEquals("Zipwire", projects.get(0).getProjectName());
        assertEquals("REST", projects.get(1).getProjectName());
    }
}
