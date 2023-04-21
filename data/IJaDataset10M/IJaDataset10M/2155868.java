package tudresden.ocl20.pivot.interpreter.test.standardlibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tudresden.ocl20.pivot.essentialocl.EssentialOclPlugin;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclBoolean;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclIterator;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclSet;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclVoid;
import tudresden.ocl20.pivot.essentialocl.types.OclLibrary;
import tudresden.ocl20.pivot.interpreter.IInterpretationResult;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * Contains some test cases to test Standard Library operations on
 * <code>OclInvalid</code> literals.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestOclType extends AbstractInterpreterTest {

    /** The name of the constraint directory for this test suite. */
    private static final String CONSTRAINT_DIRECTORY = "standardlibrary/ocltype";

    /**
	 * <p>
	 * Initializes the test cases.
	 * </p>
	 * 
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @BeforeClass
    public static void setUp() throws IllegalArgumentException, ModelAccessException {
        AbstractInterpreterTest.setUp();
    }

    /**
	 * <p>
	 * Tears down the test cases.
	 * </p>
	 * 
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @AfterClass
    public static void tearDown() throws IllegalArgumentException, ModelAccessException {
        AbstractInterpreterTest.tearDown();
    }

    /**
	 * <p>
	 * Tests the operation <code>OclType.allInstances()</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @SuppressWarnings("unchecked")
    @Test
    public void testAllInstances01() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/allInstances01", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        IInterpretationResult result = results.get(0);
        OclLibrary oclLib = EssentialOclPlugin.getOclLibraryProvider().getOclLibrary();
        Type booleanType = oclLib.getOclBoolean();
        Type resultType = oclLib.getSetType(booleanType);
        this.assertIsCollectionOfSize(2, result);
        this.assertIsOfType(resultType, result);
        assertTrue(result.getResult() instanceof OclSet<?>);
        OclSet<OclBoolean> oclSet = (OclSet<OclBoolean>) result.getResult();
        OclIterator<OclBoolean> it = oclSet.getIterator();
        int invalid = 0;
        int undefined = 0;
        int trues = 0;
        int falses = 0;
        while (it.hasNext().isTrue()) {
            OclBoolean elem = it.next();
            if (elem.oclIsInvalid().isTrue()) invalid++; else if (elem.oclIsUndefined().isTrue()) undefined++; else if (elem.isTrue()) trues++; else falses++;
        }
        assertEquals(0, invalid);
        assertEquals(0, undefined);
        assertEquals(1, trues);
        assertEquals(1, falses);
    }

    /**
	 * <p>
	 * Tests the operation <code>OclType.allInstances()</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @Test
    public void testAllInstances02() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/allInstances02", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        IInterpretationResult result = results.get(0);
        this.assertIsInvalid(result);
    }

    /**
	 * <p>
	 * Tests the operation <code>OclType.allInstances()</code>.
	 * </p>
	 * 
	 * @throws ParseException
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @SuppressWarnings("unchecked")
    @Test
    public void testAllInstances03() throws IllegalArgumentException, ModelAccessException, ParseException {
        List<IInterpretationResult> results;
        results = super.interpretConstraintsForInstance(MODEL1_NAME, CONSTRAINT_DIRECTORY + "/allInstances03", INSTANCE1_NAME, Arrays.asList(new String[] { "Class1" }));
        assertNotNull(results);
        assertEquals(1, results.size());
        IInterpretationResult result = results.get(0);
        OclLibrary oclLib = EssentialOclPlugin.getOclLibraryProvider().getOclLibrary();
        Type voidType = oclLib.getOclVoid();
        Type resultType = oclLib.getSetType(voidType);
        this.assertIsCollectionOfSize(1, result);
        this.assertIsOfType(resultType, result);
        assertTrue(result.getResult() instanceof OclSet<?>);
        OclSet<OclVoid> oclSet = (OclSet<OclVoid>) result.getResult();
        OclIterator<OclVoid> it = oclSet.getIterator();
        int invalid = 0;
        int undefined = 0;
        while (it.hasNext().isTrue()) {
            OclVoid elem = it.next();
            if (elem.oclIsInvalid().isTrue()) invalid++; else if (elem.oclIsUndefined().isTrue()) undefined++;
        }
        assertEquals(0, invalid);
        assertEquals(1, undefined);
    }
}
