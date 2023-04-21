package org.systemsbiology.apps.corragui.server.executor.openms;

public class OpenMSConstants {

    public static final String FILECONVERTER_EXE_NAME = "FileConverter";

    public static final String PP_EXE_NAME = "PeakPicker";

    public static final String FF_EXE_NAME = "FeatureFinder";

    public static final String IDFC_EXE_NAME = "IDFileConverter";

    public static final String IDF_EXE_NAME = "IDFilter";

    public static final String IDM_EXE_NAME = "IDMapper";

    public static final String MA_EXE_NAME = "MapAligner";

    public static final String C2A_EXE_NAME = "org.systemsbiology.apps.utils.ConsensusXML2APMLConverterApp ";

    public static final String FL_EXE_NAME = "FeatureLinker";

    public static final String FEATUREXML_APML_CONVERTER_EXE_NAME = "org.systemsbiology.apps.utils.FeatureXML2APMLConverterApp";

    public static final String NAMESPACEINJECTOR_EXE_NAME = "org.systemsbiology.apps.utils.NameSpaceInjector";

    public static String APML_JAR_FILE = "apml.jar";

    public static enum FE_Step {

        MZXML2MZML, PP, FF, FC, NAMESPACEINJECTOR, FEATURE2APML_CONVERT, FL, DONE
    }

    ;

    public static enum FL_Step {

        IDFC, IDF, IDM, MA, FL, C2A, DONE
    }

    ;

    public static enum Input {

        FF_INI_FILE("featureFinder.ini"), FL_INI_FILE("featureLinker.ini");

        private String stringVal;

        private Input(String stringVal) {
            this.stringVal = stringVal;
        }

        public String toString() {
            return this.stringVal;
        }
    }

    public static enum Args {

        SYSTEM("-Xms2g -Xmx2g"), CLASSPATH("-classpath"), INPUT_DIR("-inputdir"), INPUT_FILE("-inputfile"), OUTPUT_FILE("-outputfile"), MZ_TOLERANCE("-mzTol"), RT_TOLERANCE("-rtTol"), PEPXML("-pepXML"), IPI_FILE("-IPIfile"), LOG_FILE("-logfile");

        private String stringVal;

        private Args(String stringVal) {
            this.stringVal = stringVal;
        }

        public String toString() {
            return this.stringVal;
        }
    }

    public static enum Output {

        ANALYSISDIR("OPENMS_ANALYSIS"), FL_OUT_FILENAME("consensus.consensusXML"), LOG("OpenMS.log");

        private String stringVal;

        private Output(String stringVal) {
            this.stringVal = stringVal;
        }

        public String toString() {
            return this.stringVal;
        }
    }

    public static enum Parameters {

        SNR("Signal-To-Noise ratio"), TYPE("Type"), INTENS_BINS("intensity::bins"), MASS_TRACE_MZ_TOL("mass_trace::mz_tolerance"), MASS_TRACE_MIN_SPEC("mass_trace::min_spectra"), MASS_TRACE_MAX_MISS("mass_trace::max_missing"), MASS_TRACE_SLOPE("mass_trace::slope_bound"), ISOTOPIC_PATTERN_CHG_LOW("isotopic_pattern::charge_low"), ISOTOPIC_PATTERN_CHG_HIGH("isotopic_pattern::charge_high"), ISOTOPIC_PATTERN_MZ_TOL("isotopic_pattern::mz_tolerance"), SEED_MIN_SCORE("seed::min_score"), FEATURE_MIN_SCORE("feature::min_score"), FEATURE_REPORTED_MZ("feature::reported_mz"), ID_FILTER_PEP_SCORE("IDFilter:pep_score"), FEATURE_LINKER_INTENSITY_EXPONENT("FeatureLinker:intensity_exponent"), FEATURE_LINKER_SECOND_NEAREST_GAP("FeatureLinker:second_nearest_gap"), FEATURE_LINKER_DIF_CHARGE_PENALTY("FeatureLinker:different_charge_penalty"), FEATURE_LINKER_DIF_EXP_RT("FeatureLinker:diff_exponent:RT"), FEATURE_LINKER_DIF_EXP_MZ("FeatureLinker:diff_exponent:MZ"), FEATURE_LINKER_MAX_PAIR_DIST_RT("FeatureLinker:max_pair_distance:RT"), FEATURE_LINKER_MAX_PAIR_DIST_MZ("FeatureLinker:max_pair_distance:MZ"), MAPALIGNER_MAX_RT_SHIFT("MapAligner:max_rt_shift");

        private String name;

        private Parameters(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
