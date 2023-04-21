package org.nextmock.ant;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.jmock.cglib.MockObjectTestCase;
import org.jmock.core.Constraint;
import org.nextmock.Generator;
import org.nextmock.GeneratorMock;
import org.nextmock.ant.FileSetFacade;
import org.nextmock.ant.Task;
import org.nextmock.model.MockableClass;
import org.nextmock.model.classes.ClassMockableClass;

public class TaskTest extends MockObjectTestCase {

    Project antProjet;

    File destinationDir = new File("generated");

    FileSetFacadeMock fileSetFacade;

    GeneratorMock generator;

    Task testedTask;

    FileSet dummyFileSet;

    /**
	 * @overwrite junit.framework.TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        super.setUp();
        antProjet = new Project();
        dummyFileSet = new FileSet();
        dummyFileSet.setProject(antProjet);
        dummyFileSet.setDir(new File("."));
        dummyFileSet.setIncludes("xxxx");
        fileSetFacade = new FileSetFacadeMock(this);
        generator = new GeneratorMock(this);
        testedTask = new Task();
        testedTask.setProject(antProjet);
        testedTask.setGenerator((Generator) generator.proxy());
        testedTask.setFileSetFacade((FileSetFacade) fileSetFacade.proxy());
    }

    public void testOptionSetting() {
        fileSetFacade.expects(atLeastOnce()).setFileSet_with(same(dummyFileSet)).id("1");
        fileSetFacade.expects(atLeastOnce()).setPath_with(new PathEquals(".")).id("2");
        fileSetFacade.expects(once()).getMockableClasses_with().after("1").after("2").will(returnValue(new MockableClass[] { new ClassMockableClass(TaskTest.class) }));
        generator.expects(atLeastOnce()).setDestinationDirectory_with(eq(destinationDir));
        generator.expects(once()).generateMock_with(new MockableClassConstraint("TaskTest"));
        assertTrue(testedTask instanceof org.apache.tools.ant.Task);
        testedTask.setDestdir(destinationDir);
        testedTask.addConfiguredSrcfile(dummyFileSet);
        testedTask.execute();
    }

    class PathEquals implements Constraint {

        private String path;

        public PathEquals(String path) {
            this.path = new File(path).getAbsolutePath();
        }

        public boolean eval(Object arg) {
            return ((Path) arg).list()[0].equals(path);
        }

        public StringBuffer describeTo(StringBuffer arg) {
            arg.append("path_eq('" + path + "')");
            return arg;
        }
    }
}
