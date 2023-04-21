package se.biobanksregistersyd.boakeity.extra;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loads, transforms and caches properties. 
 * <p>
 * Implemented as a singleton class which holds already
 * fetched values.
 *
 * @author johant
 */
public class Config {

    private static Config config;

    private Properties properties = null;

    /**
	 * Initialises the object.
	 */
    private Config() {
        properties = new Properties();
    }

    /**
	 * Fetches a property <code>key</code> from the system and applys some
	 * transformation on it to implement macro-variables.
	 * <p>
	 * Having the following (system) properties:
	 * <pre>
     * simple=one level
     * alpha.beta.gamma.one=uno
     * alpha.beta.gamma.higher.two=dos
     * alpha.beta.three=tres
     * alpha.beta.gamma.numbers=${one} ${higher.two} ${three}
	 * </pre>
	 * running this code:
	 * <pre>
	 * println("Config: " + Config.getProperty("alpha.beta.gamma.numbers"));
	 * println("Config: " + Config.getProperty("simple"));
	 * </pre>
	 * will result in the following output:
	 * <pre>
	 * Config: uno dos tres
	 * Config: one level
	 * </pre>
	 * 
	 * @param  key  a <code>String</code> with the name of the property
	 *              to fetch.
	 * @return a <code>String</code> with the value from the system
	 *         property.
	 * @see #getProperty(String, String)
	 */
    public static String getProperty(String key) {
        return getProperty(key, "");
    }

    /**
	 * Fetches a property <code>key</code> from the system and applys some
	 * transformation on it to implement macro-variables.
	 * 
	 * @param  key  a <code>String</code> with the name of the property
	 *              to fetch.
	 * @param  ignoreMacros  a <code>String</code> of comma separated macro
	 *         variable names that should be ignored when substituting.
	 *         This is a safeguard to ensure that the code does not
	 *         get stuck in a endless recursive loop. 
	 * @return a <code>String</code> with the value from the system
	 *         property.
	 * @see #getProperty(String)
	 */
    private static String getProperty(String key, String ignoreMacros) {
        if (config == null) {
            config = new Config();
        }
        String value = config.properties.getProperty(key);
        if (value == null) {
            value = System.getProperty(key);
            if (value != null) {
                String familyName = "";
                if (key.contains(".")) familyName = key.substring(0, key.lastIndexOf('.'));
                String varName = null;
                String varValue = null;
                Matcher matcher = Pattern.compile("\\$\\{([^\\}]+)\\}").matcher(value);
                while (matcher.find()) {
                    varName = matcher.group(1);
                    if (!ignoreMacros.contains(varName)) {
                        String tmp = familyName + ".dummy";
                        while (tmp.contains(".")) {
                            tmp = tmp.substring(0, tmp.lastIndexOf('.'));
                            varValue = getProperty(tmp + "." + varName, ignoreMacros + "," + varName);
                            if (varValue != null) {
                                varValue = Matcher.quoteReplacement(varValue);
                                value = value.replaceAll("\\$\\{" + varName + "\\}", varValue);
                                break;
                            }
                        }
                    }
                }
                config.properties.setProperty(key, value);
            }
        }
        return value;
    }
}
