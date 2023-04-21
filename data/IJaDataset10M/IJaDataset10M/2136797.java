package q_impress.pmi.plugin.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import q_impress.pmi.lib.atop.AtopTranslationTask.MODEL_TYPE;

public class Constants {

    /** The extension used for PMI files */
    public static final String PMI_FILE_EXTENSION = "pmi";

    /** The extension used for JMT files */
    public static final String JMT_FILE_EXTENSION = "jsimg";

    /** The prefix to append to the model name to obtain the properties file name */
    public static final String PRISM_PROPERTIES_FILE_PREFIX = "props_";

    /** The extension of prism models*/
    public static final Map<MODEL_TYPE, String> PRISM_MODEL_FILE_EXT = new HashMap<MODEL_TYPE, String>();

    static {
        PRISM_MODEL_FILE_EXT.put(MODEL_TYPE.CTMC, "sm");
        PRISM_MODEL_FILE_EXT.put(MODEL_TYPE.DTMC, "pm");
        PRISM_MODEL_FILE_EXT.put(MODEL_TYPE.MDP, "nm");
    }

    public static final Map<MODEL_TYPE, String> PRISM_PROPERTIES_FILE_EXT = new HashMap<MODEL_TYPE, String>();

    static {
        PRISM_PROPERTIES_FILE_EXT.put(MODEL_TYPE.CTMC, "csl");
        PRISM_PROPERTIES_FILE_EXT.put(MODEL_TYPE.DTMC, "pctl");
        PRISM_PROPERTIES_FILE_EXT.put(MODEL_TYPE.MDP, "pctl");
    }

    public static final Set<String> PRISM_MODEL_TYPES = new HashSet<String>();

    static {
        for (MODEL_TYPE type : PRISM_PROPERTIES_FILE_EXT.keySet()) {
            PRISM_MODEL_TYPES.add(type.toString());
        }
    }

    /** Default modeling project name */
    public static final String DEFAULT_MODEL_NAME = "my_model";

    /** The directory under the project root used to store UML models */
    public static final String UML_MODELS_DIR = "models";

    /** The directory under the project root used to store prism models */
    public static final String ATOP_DIR = "atop";

    /** The directory under the project root used to store jmt models */
    public static final String JMT_DIR = "jmt";
}
