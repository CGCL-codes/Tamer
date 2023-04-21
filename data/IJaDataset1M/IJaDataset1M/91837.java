package tudresden.ocl20.pivot.ocl2parser.test.parsertests;

import static org.junit.Assert.*;
import org.junit.Test;
import junit.framework.TestCase;

public class StaticPropertyTest {

    @Test
    public void testStaticPropertyWithUML2() {
        String fileName = "oclTestFiles/StaticPropertyTest_UML2.ocl";
        try {
            TestPerformer test = TestPerformer.getDefault("tudresden.ocl20.pivot.metamodels.uml2");
            test.setModel("royalsandloyals.uml");
            test.parseFile(fileName);
        } catch (Throwable ex) {
            String message = " This error occured for file " + fileName + ".";
            System.err.println(message);
            ex.printStackTrace();
            fail();
            return;
        }
        assertTrue(true);
    }
}
