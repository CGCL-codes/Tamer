package tudresden.ocl20.pivot.ocl2parser.test.standardlibrary;

import org.junit.Test;
import tudresden.ocl20.pivot.ocl2parser.test.TestPerformer;

/**
 * <p>
 * Contains test cases that check that all operations defined on the type
 * Boolean are parsed appropriately.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestBoolean {

    /**
	 * <p>
	 * A test case testing the method <code>Boolean.and(Boolean)</code>.
	 * </p>
	 */
    @Test
    public void testAndPositive01() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/andPositive01.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>Boolean.and(Boolean)</code>.
	 * </p>
	 */
    @Test
    public void testAndPositive02() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/andPositive02.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 */
    @Test
    public void testImpliesPositive01() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/impliesPositive01.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>Boolean.implies(Boolean)</code>.
	 * </p>
	 */
    @Test
    public void testImpliesPositive02() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/impliesPositive02.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>not Boolean</code>.
	 * </p>
	 */
    @Test
    public void testNotPositive01() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/notPositive01.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>not Boolean</code>.
	 * </p>
	 */
    @Test
    public void testNotPositive02() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/notPositive02.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>Boolean.or(Boolean)</code>.
	 * </p>
	 */
    @Test
    public void testOrPositive01() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/orPositive01.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>Boolean.or(Boolean)</code>.
	 * </p>
	 */
    @Test
    public void testOrPositive02() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/orPositive02.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>Boolean.toString()</code>.
	 * </p>
	 */
    @Test
    public void testToStringPositive01() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/toStringPositive01.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>Boolean.xor(Boolean)</code>.
	 * </p>
	 */
    @Test
    public void testXorPositive01() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/xorPositive01.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case testing the method <code>Boolean.xor(Boolean)</code>.
	 * </p>
	 */
    @Test
    public void testXorPositive02() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "standardlibrary/boolean/xorPositive02.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllStandardLibraryTests.META_MODEL_ID, AllStandardLibraryTests.MODEL_BUNDLE, AllStandardLibraryTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }
}
