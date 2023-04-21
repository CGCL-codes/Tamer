package org.ji18n.core.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.bsf.BSFManager;

/**
 * @version $Id: Scripts.java 159 2008-07-03 01:28:51Z david_ward2 $
 * @author david at ji18n.org
 */
public class Scripts {

    private static final Map<String, String> LANGUAGES = new HashMap<String, String>();

    static {
        InputStream is = null;
        try {
            is = BSFManager.class.getClassLoader().getResourceAsStream("org/apache/bsf/Languages.properties");
            is = new BufferedInputStream(is);
            Properties props = new Properties();
            props.load(is);
            for (Enumeration<?> names = props.propertyNames(); names.hasMoreElements(); ) {
                String lang = (String) names.nextElement();
                StringTokenizer st = new StringTokenizer(props.getProperty(lang), ",");
                st.nextToken();
                st = new StringTokenizer(st.nextToken(), "|");
                while (st.hasMoreTokens()) LANGUAGES.put(st.nextToken().trim(), lang);
            }
        } catch (IOException ioe) {
        } finally {
            try {
                if (is != null) is.close();
            } catch (Throwable t) {
            }
        }
    }

    private Scripts() {
    }

    public static void execute(String language, String script) throws Exception {
        execute(language, script, null);
    }

    public static void execute(String language, String script, Map<String, ?> variables) throws Exception {
        if (LANGUAGES.containsKey(language)) language = LANGUAGES.get(language);
        BSFManager bsf = new BSFManager();
        if (variables != null) {
            for (String key : variables.keySet()) {
                Object value = variables.get(key);
                bsf.declareBean(key, value, value.getClass());
            }
        }
        bsf.exec(language, null, 0, 0, script);
    }
}
