package org.apache.shindig.gadgets.servlet;

import com.google.common.collect.Maps;
import org.apache.shindig.gadgets.GadgetContext;
import junit.framework.TestCase;
import org.json.JSONObject;
import java.util.Locale;
import java.util.Map;

public class JsonRpcGadgetContextTest extends TestCase {

    static final String SPEC_URL = "http://example.org/gadget.xml";

    static final int SPEC_ID = 1234;

    static final String[] PREF_KEYS = { "hello", "foo" };

    static final String[] PREF_VALUES = { "world", "bar" };

    static final Map<String, String> prefs = Maps.newHashMap();

    static {
        for (int i = 0, j = PREF_KEYS.length; i < j; ++i) {
            prefs.put(PREF_KEYS[i], PREF_VALUES[i]);
        }
    }

    public void testCorrectExtraction() throws Exception {
        JSONObject gadget = new JSONObject().put("url", SPEC_URL).put("moduleId", SPEC_ID).put("prefs", prefs).put("gadget-field", "gadget-value");
        JSONObject context = new JSONObject().put("language", Locale.US.getLanguage()).put("country", Locale.US.getCountry().toUpperCase()).put("context-field", "context-value");
        GadgetContext jsonContext = new JsonRpcGadgetContext(context, gadget);
        assertEquals(SPEC_URL, jsonContext.getUrl().toString());
        assertEquals(SPEC_ID, jsonContext.getModuleId());
        assertEquals(Locale.US.getLanguage(), jsonContext.getLocale().getLanguage());
        assertEquals(Locale.US.getCountry(), jsonContext.getLocale().getCountry());
        for (String key : PREF_KEYS) {
            String value = jsonContext.getUserPrefs().getPref(key);
            assertEquals(prefs.get(key), value);
        }
        assertEquals("gadget-value", jsonContext.getParameter("gadget-field"));
        assertEquals("context-value", jsonContext.getParameter("context-field"));
    }
}
