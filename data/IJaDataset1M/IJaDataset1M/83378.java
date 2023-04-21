package tudresden.ocl.test;

import java.io.IOException;
import junit.framework.TestCase;
import tudresden.ocl.OclTree;
import tudresden.ocl.check.OclTypeException;
import tudresden.ocl.check.types.testfacade.TestModelFacade;

/**
 *
 * @author  frank
 * @version $Revision: 1.1 $
 */
public class TestTypeCheck extends TestCase {

    OclTree tree;

    public TestTypeCheck(String name) {
        super(name);
    }

    public void testTestFacade() throws IOException {
        tree = OclTree.createTree("context Person inv: true", new TestModelFacade());
        tree.assureTypes();
        try {
            tree = OclTree.createTree("context Person inv: 6", new TestModelFacade());
            tree.assureTypes();
            fail();
        } catch (OclTypeException e) {
        }
        tree = OclTree.createTree("context Company inv: employees->size <= 7", new TestModelFacade());
        tree.assureTypes();
        tree = OclTree.createTree("context Company inv: employees->oclAsType( Collection(Person) )->size <= 7", new TestModelFacade());
        try {
            tree.assureTypes();
            fail("oclAsType not defined for Collections");
        } catch (OclTypeException e) {
        }
        tree = OclTree.createTree("context Company inv: employees.oclAsType( Person )->size <= 7", new TestModelFacade());
        tree.assureTypes();
    }
}
