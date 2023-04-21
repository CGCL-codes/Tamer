package tudresden.ocl20.pivot.ocl2parser.test.uml2;

import static org.junit.Assert.*;
import org.junit.Test;
import tudresden.ocl20.pivot.ocl2parser.test.AllUML2Tests;
import tudresden.ocl20.pivot.ocl2parser.test.TestPerformer;

public class IteratorOclIsTypeOfTest {

    @Test
    public void testOclIsTypeOfInIterator() {
        String fileName = "oclTestFiles/Iterator_oclIsTypeOf.ocl";
        try {
            TestPerformer test = TestPerformer.getInstance(AllUML2Tests.META_MODEL_NAME, AllUML2Tests.MODEL_BUNDLE, AllUML2Tests.MODEL_BUNDLE_PATH);
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
