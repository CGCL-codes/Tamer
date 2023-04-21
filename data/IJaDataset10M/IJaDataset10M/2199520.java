package de.berndsteindorff.junittca.model.test;

import static de.berndsteindorff.junittca.model.test.Const.CLASSPATH;
import static de.berndsteindorff.junittca.model.test.Const.DIRSEP;
import static de.berndsteindorff.junittca.model.test.Const.NAME_DUMMYTEST;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import de.berndsteindorff.junittca.TcaMain;
import de.berndsteindorff.junittca.model.ClassFile;
import de.berndsteindorff.junittca.model.ClassMethods;
import de.berndsteindorff.junittca.model.Project;
import de.berndsteindorff.junittca.model.Run;

/**
 * Test the class Project.
 * 
 * @author Bernd Steindorff
 */
public class ProjectTest {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Before
    public void setUp() {
        logger.setLevel(TcaMain.LOGLEVEL);
    }

    /**
	 * Test Extracting the Methods of the ClassFile while running Tests.
	 * 
	 * @throws ClassNotFoundException
	 *             The exception should not be thrown.
	 */
    @Test
    public void classMethodsMethodExtractingSimple() throws ClassNotFoundException {
        logger.info("constructorAndMethodExtracting");
        Project project = new Project("Bla");
        project.addTestClass(new ClassFile(NAME_DUMMYTEST, CLASSPATH + DIRSEP));
        project.runTests();
        assertEquals(1, project.getTestRuns().size());
        assertEquals(1, project.getTestRuns().get(0).getClassMethods().size());
        ClassMethods classMethods = project.getTestRuns().get(0).getClassMethods().get(0);
        assertEquals(NAME_DUMMYTEST, classMethods.getClassName());
        assertEquals(3, classMethods.getTestMethods().size());
        assertEquals(2, classMethods.getIgnoredTestMethods().size());
    }

    /**
	 * Test Extracting the Methods of the ClassFile while running Tests with
	 * sorting.
	 * 
	 * @throws ClassNotFoundException
	 *             The exception should not be thrown.
	 */
    @Test
    public void classMethodsMethodExtractingSort() throws ClassNotFoundException {
        logger.info("constructorAndMethodExtracting");
        Project project = new Project("Bla");
        project.addTestClass(new ClassFile(NAME_DUMMYTEST, CLASSPATH + DIRSEP));
        project.runTests();
        assertEquals(1, project.getTestRuns().size());
        assertEquals(1, project.getTestRuns().get(0).getClassMethods().size());
        ClassMethods classMethods = project.getTestRuns().get(0).getClassMethods().get(0);
        assertEquals(NAME_DUMMYTEST, classMethods.getClassName());
        assertEquals("dummyTest", classMethods.getTestMethods().get(0));
        assertEquals("gDummyTest", classMethods.getTestMethods().get(1));
        assertEquals("zDummyTest", classMethods.getTestMethods().get(2));
        assertEquals("ignore1", classMethods.getIgnoredTestMethods().get(0));
        assertEquals("ignore2", classMethods.getIgnoredTestMethods().get(1));
    }

    /**
	 * Test sorting project-classes when insert it into the project..
	 */
    @Test
    public void sortedTestClasses() {
        logger.info("sortedTestClasses");
        Project project = new Project("Bla");
        project.addTestClass(new ClassFile("de.berndsteindorff.junittca.model.test.ProjectTest", CLASSPATH + DIRSEP));
        project.addTestClass(new ClassFile(NAME_DUMMYTEST, CLASSPATH + DIRSEP));
        project.addTestClass(new ClassFile("de.berndsteindorff.junittca.model.test.RunTest", CLASSPATH + DIRSEP));
        List<ClassFile> exp = new ArrayList<ClassFile>(3);
        exp.add(new ClassFile(NAME_DUMMYTEST, CLASSPATH + DIRSEP));
        exp.add(new ClassFile("de.berndsteindorff.junittca.model.test.ProjectTest", CLASSPATH + DIRSEP));
        exp.add(new ClassFile("de.berndsteindorff.junittca.model.test.RunTest", CLASSPATH + DIRSEP));
        List<ClassFile> done = project.getActualTestClasses();
        assertArrayEquals(exp.toArray(new ClassFile[0]), done.toArray(new ClassFile[0]));
    }

    /**
	 * Test if the list which the project gives bakc are unmodificable.
	 * 
	 * @throws Exception
	 */
    @Test
    public void unmodicableGetter() throws Exception {
        logger.info("unmodicableGetter");
        Project project = new Project("Bla");
        project.setClasspath(CLASSPATH);
        project.addTestClass(new ClassFile(NAME_DUMMYTEST, CLASSPATH + DIRSEP));
        List<ClassFile> classes = project.getActualTestClasses();
        try {
            classes.add(new ClassFile("de.berndsteindorff.junittca.model.test.ProjectTest", CLASSPATH + DIRSEP));
            System.out.println("bla");
            fail("Es konnte die Liste der Test-Klassen doch verändert werden");
        } catch (UnsupportedOperationException e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
        project.runTests();
        List<Run> runs = project.getTestRuns();
        Result result = JUnitCore.runClasses(DummyTest.class);
        ArrayList<String> testMethods = new ArrayList<String>(3);
        testMethods.add("zDummyTest");
        testMethods.add("dummyTest");
        testMethods.add("gDummyTest");
        ArrayList<String> ignoredTestMethods = new ArrayList<String>(2);
        ignoredTestMethods.add("ignore2");
        ignoredTestMethods.add("ignore1");
        ClassMethods cm = new ClassMethods(NAME_DUMMYTEST, CLASSPATH + DIRSEP, testMethods, ignoredTestMethods);
        List<ClassMethods> cms = new ArrayList<ClassMethods>(1);
        cms.add(cm);
        try {
            runs.add(new Run(new Date(), result, cms));
            fail("Es konnte die Liste der Runs doch verändert werden");
        } catch (UnsupportedOperationException e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    /**
	 * Test if a class could be inserted more than once in a project.
	 */
    @Test
    public void addClassMultiple() {
        logger.info("addClassMultiple");
        Project project = new Project("Bla");
        project.addTestClass(new ClassFile(NAME_DUMMYTEST, CLASSPATH + DIRSEP));
        project.addTestClass(new ClassFile(NAME_DUMMYTEST, CLASSPATH + DIRSEP));
        project.addTestClass(new ClassFile(NAME_DUMMYTEST, CLASSPATH + DIRSEP));
        assertEquals(1, project.getActualTestClasses().size());
    }

    /**
	 * Test if a run is insert at the begin of the run-list.
	 * 
	 * @throws Exception
	 *             Should not happen.
	 */
    @Test
    public void addRunAtBeginOfList() throws Exception {
        logger.info("addRunAtBeginOfList");
        Project project = new Project("Bla");
        project.setClasspath(Const.CLASSPATH);
        project.addTestClass(new ClassFile(NAME_DUMMYTEST, CLASSPATH + DIRSEP));
        project.runTests();
        assertEquals(1, project.getTestRuns().size());
        Run run1 = project.getTestRuns().get(0);
        Date date1 = run1.getRunDate();
        project.runTests();
        assertEquals(2, project.getTestRuns().size());
        Run run2 = project.getTestRuns().get(0);
        Date date2 = run2.getRunDate();
        assertEquals(true, date2.after(date1));
        project.runTests();
        assertEquals(3, project.getTestRuns().size());
        Run run3 = project.getTestRuns().get(0);
        Date date3 = run3.getRunDate();
        assertEquals(true, date3.after(date1));
        assertEquals(true, date3.after(date2));
    }

    /**
	 * Test setting classpath with other file-endings like .jar and .class and
	 * another format.
	 */
    @Test
    public void setClassPathWrongFiles() {
        logger.info("setClassPathWrongFiles");
        Project project = new Project("bla");
        logger.info("setClassPathsWrongDirectories");
        try {
            project.setClasspath("/home/bernd/eclipse_workspace/junittca_local/junittca.jar");
            fail("Es konnte doch ein Klassenpfad gesetzt werden:" + project.getClasspath());
        } catch (Exception e) {
            assertEquals("Der Klassenpfad /home/bernd/eclipse_workspace/junittca_local/junittca.jar ist ungültig.\nBitte geben Sie ein existierendes Verzeichnis ein.", e.getMessage());
        }
        try {
            project.setClasspath("/home/bernd/eclipse_workspace/junittca_local/Bla.class");
            fail("Es konnte doch ein Klassenpfad gesetzt werden:" + project.getClasspath());
        } catch (Exception e) {
            assertEquals("Der Klassenpfad /home/bernd/eclipse_workspace/junittca_local/Bla.class ist ungültig.\nBitte geben Sie ein existierendes Verzeichnis ein.", e.getMessage());
        }
        try {
            project.setClasspath("/home/bernd/eclipse_workspace/junittca_local/tiefe.svg");
            fail("Es konnte doch ein Klassenpfad gesetzt werden:" + project.getClasspath());
        } catch (Exception e) {
            assertEquals("Der Klassenpfad /home/bernd/eclipse_workspace/junittca_local/tiefe.svg ist ungültig.\nBitte geben Sie ein existierendes Verzeichnis ein.", e.getMessage());
        }
    }

    /**
	 * Test setting classpath with correct directories.
	 * 
	 */
    @Test
    public void setClassPathsDirectories() throws Exception {
        Project project = new Project("bla");
        logger.info("setClassPathsWrongDirectories");
        project.setClasspath(CLASSPATH);
    }

    /**
	 * Test setting classpath with wrong directories.
	 * 
	 */
    @Test
    public void setClassPathsWrongDirectories() throws Exception {
        Project project = new Project("bla");
        logger.info("setClassPathsWrongDirectories");
        try {
            project.setClasspath("");
            fail("Es konnte doch ein Klassenpfad gesetzt werden:" + project.getClasspath());
        } catch (Exception e) {
            assertEquals("Ein leerer Klassenpfad ist ungültig.\nBitte geben Sie ein existierendes Verzeichnis ein.", e.getMessage());
        }
        try {
            project.setClasspath("/home/bla");
            fail("Es konnte doch ein Klassenpfad gesetzt werden:" + project.getClasspath());
        } catch (Exception e) {
            assertEquals("Der Klassenpfad /home/bla ist ungültig.\nBitte geben Sie ein existierendes Verzeichnis ein.", e.getMessage());
        }
    }
}
