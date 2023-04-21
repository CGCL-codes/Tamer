package net.frontlinesms.messaging.sms.internet;

import java.util.*;
import net.frontlinesms.data.domain.SmsInternetServiceSettingsTest.Test;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.messaging.sms.internet.AbstractSmsInternetService;
import net.frontlinesms.messaging.sms.properties.OptionalRadioSection;
import net.frontlinesms.messaging.sms.properties.OptionalSection;
import net.frontlinesms.messaging.sms.properties.PasswordString;
import net.frontlinesms.messaging.sms.properties.PhoneSection;

/**
 * Tests the various methods dealing with {@link AbstractSmsInternetService}'s properties classes.
 * 
 * 
 * @author Alex
 * @author Carlos Eduardo Genz
 */
public class AbstractSmsInternetServicePropertiesTest extends BaseTestCase {

    private Map<String, Object> defaultSettings = new HashMap<String, Object>();

    private Map<String, Object> expectedValues = new HashMap<String, Object>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String key = "a";
        defaultSettings.put(key, "");
        expectedValues.put(key, defaultSettings.get(key));
        defaultSettings.put(key = "b", new PasswordString(""));
        expectedValues.put(key, defaultSettings.get(key));
        defaultSettings.put(key = "c", new PhoneSection(""));
        expectedValues.put(key, defaultSettings.get(key));
        defaultSettings.put(key = "d", Boolean.FALSE);
        expectedValues.put(key, defaultSettings.get(key));
        defaultSettings.put(key = "e", 997);
        expectedValues.put(key, defaultSettings.get(key));
        OptionalRadioSection<Test> a = new OptionalRadioSection<Test>(Test.A);
        String objj = "";
        a.addDependency(Test.A, key = "f", objj);
        expectedValues.put(key, objj);
        PasswordString obj = new PasswordString("");
        a.addDependency(Test.A, key = "g", obj);
        expectedValues.put(key, obj);
        PhoneSection obj2 = new PhoneSection("");
        a.addDependency(Test.B, key = "h", obj2);
        expectedValues.put(key, obj2);
        defaultSettings.put(key = "i", a);
        expectedValues.put(key, defaultSettings.get(key));
        OptionalSection section = new OptionalSection();
        section.setValue(true);
        section.addDependency(key = "j", objj);
        expectedValues.put(key, objj);
        section.addDependency(key = "k", obj);
        expectedValues.put(key, obj);
        defaultSettings.put(key = "l", section);
        expectedValues.put(key, defaultSettings.get(key));
    }

    /**
	 * Unit tests for {@link AbstractSmsInternetService#getValue(String, java.util.LinkedHashMap)}.
	 * It does test deeper levels included in {@link OptionalRadioSection}s and {@link OptionalSection}s.
	 */
    public void testGetValue() {
        for (String key : expectedValues.keySet()) {
            Object obj = AbstractSmsInternetService.getValue(key, defaultSettings);
            assertEquals("Checking get value for key '" + key + "'", obj, expectedValues.get(key));
        }
        String invalidKey = "invalidKey";
        assertNull("Checking get value from null map", AbstractSmsInternetService.getValue(invalidKey, null));
        assertNull("Checking get value from null map", AbstractSmsInternetService.getValue(invalidKey, defaultSettings));
    }

    @Override
    protected void tearDown() throws Exception {
        defaultSettings.clear();
        expectedValues.clear();
    }
}
