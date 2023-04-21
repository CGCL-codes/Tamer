package preprocessing.methods.FeatureSelection.WekaFeatureSelectionMethods;

import game.utils.Exceptions.NonExistingAttributeException;
import org.junit.BeforeClass;
import org.junit.Test;
import preprocessing.Parameters.Parameter;
import preprocessing.methods.Import.LoadRAWPreprocessor;
import preprocessing.storage.PreprocessingStorage;
import preprocessing.storage.SimplePreprocessingStorage;
import java.io.File;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: cepekm1
 * Date: Oct 14, 2010
 * Time: 4:41:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class InfoGainTest {

    private static PreprocessingStorage store;

    private static String data_training = "gui-data/iris.txt";

    @BeforeClass
    public static void initiateIndividual() {
        store = new SimplePreprocessingStorage();
        LoadRAWPreprocessor rawprep = new LoadRAWPreprocessor();
        Parameter p = null;
        try {
            p = rawprep.getConfigurationClass().getParameterObjByKey("FileName");
            assertTrue(p != null);
        } catch (NoSuchFieldException e) {
            fail("No parameter called \"Filename\" found...\n" + e);
        }
        File f = new File(data_training);
        assertTrue(f.exists());
        p.setValue(data_training);
        rawprep.init(store);
        rawprep.run();
    }

    @Test
    public void testInfoGain() {
        InfoGainMethod ig = new InfoGainMethod();
        ig.init(store);
        try {
            ig.run();
        } catch (NonExistingAttributeException e) {
            e.printStackTrace();
            fail();
        }
    }
}
