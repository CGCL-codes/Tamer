package tudresden.ocl20.pivot.ocl2parser.test.ecore;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;
import tudresden.ocl20.pivot.modelbus.ModelAccessException;
import tudresden.ocl20.pivot.ocl2parser.parser.exceptions.BuildingASTException;
import tudresden.ocl20.pivot.ocl2parser.parser.exceptions.LexException;
import tudresden.ocl20.pivot.ocl2parser.parser.exceptions.ParsingException;
import tudresden.ocl20.pivot.ocl2parser.parser.exceptions.SemanticException;
import tudresden.ocl20.pivot.ocl2parser.test.AllECoreTests;
import tudresden.ocl20.pivot.ocl2parser.test.MetaModelNotFoundException;
import tudresden.ocl20.pivot.ocl2parser.test.TestPerformer;

public class AtPreTest {

    @Test
    public void testAtPre() {
        String fileName = "oclTestFiles/atPreTest.ocl";
        TestPerformer test;
        try {
            test = TestPerformer.getInstance(AllECoreTests.META_MODEL_NAME, AllECoreTests.MODEL_BUNDLE, AllECoreTests.MODEL_BUNDLE_PATH);
            test.setModel("royalsandloyals.ecore");
            try {
                test.parseFile(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("Failes to parse File");
            } catch (ParsingException e) {
                e.printStackTrace();
                fail("Failes to parse File");
            } catch (LexException e) {
                e.printStackTrace();
                fail("Failes to parse File");
            } catch (IOException e) {
                e.printStackTrace();
                fail("Failes to parse File");
            } catch (BuildingASTException e) {
                e.printStackTrace();
                fail("Failes to parse File");
            } catch (SemanticException e) {
                e.printStackTrace();
                fail("Failes to parse File");
            }
        } catch (MetaModelNotFoundException e) {
            e.printStackTrace();
            fail("Unable to get TestPerformer");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Lacking file resources");
        } catch (ModelAccessException e) {
            e.printStackTrace();
            fail("Couldn't set Model");
        }
        assertTrue(true);
    }
}
