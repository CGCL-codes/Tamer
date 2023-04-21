package preprocessing.methods.Normalization;

import preprocessing.Parameters.ParameterString;
import preprocessing.methods.BasePreprocessorConfig;

/**
 */
public class MeanNormalizerConfig extends BasePreprocessorConfig {

    private static final long serialVersionUID = 1L;

    public static String avg = "Custom average";

    protected void setInitialValues() {
        addConfigKey(new ParameterString(avg, ""));
    }
}
