package preprocessing.methods.Normalization;

import game.utils.Exceptions.NonExistingAttributeException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import preprocessing.storage.PreprocessingStorage;
import preprocessing.storage.SimplePreprocessingStorage;
import weka.core.FastVector;
import static org.junit.Assert.assertArrayEquals;

public class CustomJsNormalizerTest {

    private CustomJsNormalizer ln = null;

    private CustomJsNormalizerConfig lnc = null;

    private FastVector tVector1;

    private FastVector tVector2;

    private PreprocessingStorage storage = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        lnc = new CustomJsNormalizerConfig();
        ln = new CustomJsNormalizer(lnc);
        lnc.cleanIONames();
        tVector1 = new FastVector();
        tVector2 = new FastVector();
        storage = new SimplePreprocessingStorage();
        for (int j = 1; j < 10; j++) {
            tVector1.addElement((double) j);
            tVector2.addElement((double) j + 10);
        }
        storage.addNewAttribute(tVector1, "testVector1", PreprocessingStorage.DataType.NUMERIC, PreprocessingStorage.AttributeRole.INPUT);
        storage.addNewAttribute(tVector2, "testVector2", PreprocessingStorage.DataType.NUMERIC, PreprocessingStorage.AttributeRole.OUTPUT);
        lnc.add2IONames("testVector1");
        lnc.add2IONames("testVector2");
        ln.init(storage);
    }

    @Test
    public void basicFunctionalityTest() {
        lnc.setValueByName(CustomJsNormalizerConfig.sPath, "src/game/preprocessing/methods/Normalization/testScript.js");
        ln.run();
        Double[] expected = { 101D, 102D, 103D, 104D, 105D, 106D, 107D, 108D, 109D };
        try {
            assertArrayEquals("Output values have not expected values", expected, storage.getDeepCopy("testVector1").toArray());
        } catch (NonExistingAttributeException e) {
            System.err.printf("Some attribute was not found :(. Error : %s\n", e.getMessage());
            System.err.flush();
            System.out.flush();
            System.exit(-1);
        }
    }
}
