package au.edu.educationau.opensource.dsm.worker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import au.edu.educationau.opensource.dsm.service.PropertiesService;
import au.edu.educationau.opensource.dsm.util.Flog;
import au.edu.educationau.opensource.dsm.util.EducationAuUtils;

/**
 * Allows for the encoding/decoding of values against arbitrary vocabularies on
 * a per-adapter, per-field basis.
 * 
 * Is backed by a properties file where entries are of the form:
 * adapeterName.fieldName.value=vocabValue1|vocabValue2
 */
public final class VocabMapper {

    /**
	 * Map of group names to String arrays containing the adapter names for each
	 * group. *
	 */
    private static Properties vocabProps = new Properties();

    /** Config file location. */
    private static final String VOCAB_FILE_PATH = "vocab.config.file";

    /** Value delimiter in config file. */
    private static final String VALUE_DELIM_REGEX = "\\|";

    /** Value in config file meaning 'all values match'. */
    public static final String ALL_VALUES = "*";

    /** Class name display */
    public static final String classStr = "o.m.d.w.VocabMapper";

    /** Destroy objects worthy of destruction */
    public static void destroy() {
        vocabProps.clear();
        Flog.debug(classStr, "Vocab Mapper shutdown ok.");
    }

    /** Initialise. */
    public static void init(String propertiesDir) {
        Flog.console(classStr, "Building Vocab Mapper...");
        String fileLoc = PropertiesService.getStringProperty(VOCAB_FILE_PATH, "vocab.properties");
        if (!fileLoc.startsWith(File.separator)) {
            fileLoc = propertiesDir + fileLoc;
        }
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(fileLoc));
            vocabProps.load(in);
            in.close();
        } catch (Exception e) {
            Flog.error(classStr, "Error loading Vocab Mapper properties.", e);
        }
    }

    public static String[] getValuesForKey(String adapterCode, String fieldName, String key) {
        String val = vocabProps.getProperty(adapterCode + "." + fieldName + "." + key);
        return (StringUtils.isNotBlank(val)) ? val.split(VALUE_DELIM_REGEX) : null;
    }

    public static String getKeyForValue(String adapterCode, String fieldName, String value) {
        Map map = EducationAuUtils.getSubmapByKeyPrefix(vocabProps, adapterCode + "." + fieldName + ".", true);
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            String[] values = ((String) entry.getValue()).split(VALUE_DELIM_REGEX);
            if (ArrayUtils.contains(values, value) || ArrayUtils.contains(values, ALL_VALUES)) {
                return (String) entry.getKey();
            }
        }
        return null;
    }
}
