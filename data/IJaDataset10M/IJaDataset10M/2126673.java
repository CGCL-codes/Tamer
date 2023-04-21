package com.novasurv.turtle.backend.project.itest;

import java.io.File;
import java.util.List;
import junit.framework.TestCase;
import com.novasurv.turtle.backend.project.FileSystemProjectFileLocator;
import com.novasurv.turtle.backend.project.Project;
import com.novasurv.turtle.backend.project.ProjectFileLocator;

/**
 * @author Jason Dobies
 */
public class ProjectTest extends TestCase {

    public void testListClassFiles() throws Exception {
        String studentId = "student1";
        File projectDirectory = new File(TestConstants.PROJECTS_BASE_DIR, "student1");
        ProjectFileLocator locator = new FileSystemProjectFileLocator(TestConstants.GRADER_DIR, projectDirectory);
        Project project = new Project(studentId, locator);
        List<String> classes = project.listAllProjectClasses();
        assertNotNull(classes);
        for (String clazz : classes) {
            System.out.println("Class: " + clazz);
        }
        assertEquals(4, classes.size());
    }
}
