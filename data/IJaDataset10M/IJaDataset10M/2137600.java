package preprocessing.tests;

import preprocessing.storage.SimplePreprocessingStorage;
import preprocessing.methods.SignalProcessing.Import.LoadRAWSignal;
import preprocessing.methods.SignalProcessing.Import.LoadRAWSignalConfig;
import preprocessing.methods.SignalProcessing.LoadedDataManipulation.SplitSignal;
import preprocessing.methods.SignalProcessing.LoadedDataManipulation.SplitSignalConfig;
import preprocessing.methods.SignalProcessing.LoadedDataManipulation.RemoveSignalSliceAttributes;
import preprocessing.methods.SignalProcessing.LoadedDataManipulation.RemoveSignalSliceAttributesConfig;
import preprocessing.Parameters.Parameter;
import preprocessing.Parameters.ParameterNonMutableInteger;
import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import game.utils.Exceptions.NonExistingAttributeException;

/**
* @author Petr Galik
*
* This is a test of preprocessing method RemoveSignalSliceAttributes.
* Prerequisites for this test is to setup file path and the method LoadRAWSignal must work properly.
*
*/
public class SignalPreprocessingRemoveAttributes {

    static SimplePreprocessingStorage store;

    /**
* Performs test of preprocessing method RemoveSignalSliceAttributes.
*
* Test has these phases:
* 1) Loading storage from file first_1000.txt and using LoadRAWSignalConfig
* 2) Testing loaded storage
* 3) Removing signal slices with method RemoveSignalSliceAttributes
* 4) Testing storage
*
*/
    @Test
    public void removeAttributesTest() throws NonExistingAttributeException {
        store = new SimplePreprocessingStorage();
        LoadRAWSignal loadRAWSignal = new LoadRAWSignal();
        loadRAWSignal.init(store);
        LoadRAWSignalConfig loadRAWSignalConfig = (LoadRAWSignalConfig) loadRAWSignal.getConfigurationClass();
        try {
            Parameter p1 = loadRAWSignalConfig.getParameterObjByKey("FileName");
            p1.setValue("D:\\Petr\\Skola\\___Diplomka\\Project\\core\\trunk\\target\\gui-data\\first_1000.txt");
            ParameterNonMutableInteger p2 = (ParameterNonMutableInteger) loadRAWSignalConfig.getParameterObjByKey("Sampling frequency (Hz)");
            p2.setValue(100);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail("Parameter was not found :(.");
        }
        loadRAWSignal.run();
        assertTrue(store.getNumberOfAttributes() == 4);
        assertTrue(store.getNumberOfInputAttributes() == 3);
        assertTrue(store.getNumberOfOutputAttributes() == 1);
        assertTrue(store.getNumberOfIgnoredAttributes() == 0);
        RemoveSignalSliceAttributes removeSignalSliceAttributes = new RemoveSignalSliceAttributes();
        removeSignalSliceAttributes.init(store);
        removeSignalSliceAttributes.run();
        assertTrue(store.getNumberOfAttributes() == 0);
        assertTrue(store.getNumberOfInputAttributes() == 0);
        assertTrue(store.getNumberOfOutputAttributes() == 0);
        assertTrue(store.getNumberOfIgnoredAttributes() == 0);
    }
}
