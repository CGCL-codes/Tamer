package panini_tests;

import framework.IPtolemyCompileTest;
import framework.IPtolemyTestAdaptor;

public class PaniniLanguageFeatureTestExampleFFTProvider {

    private static final String ROOT_TEST_FOLDER = "pyc_tests";

    private static final String LANG_FEATURE_FOLDER = "PaniniLanguageFeatures";

    private static final String EXAMPLE = "FFT";

    public static IPtolemyCompileTest createFFT2handlerTest() {
        return new IPtolemyTestAdaptor(ROOT_TEST_FOLDER + "/" + LANG_FEATURE_FOLDER + "/" + EXAMPLE + "/2handlers", "FFT.java", "Changed.java", "Handler1.java", "Handler2.java");
    }

    public static IPtolemyCompileTest createFFT3handlerTest() {
        return new IPtolemyTestAdaptor(ROOT_TEST_FOLDER + "/" + LANG_FEATURE_FOLDER + "/" + EXAMPLE + "/3handlers", "FFT.java", "Changed.java", "Handler1.java", "Handler2.java", "Handler3.java");
    }
}
