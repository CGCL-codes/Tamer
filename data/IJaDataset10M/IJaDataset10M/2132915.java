package biz.evot.osgi;

import org.osgi.service.prefs.Preferences;
import biz.evot.osgi.MockBC;
import biz.evot.osgi.MockPrefService;
import junit.framework.TestCase;

public class MockPrefServiceTest extends TestCase {

    public void testPref() {
        MockBC bc = new MockBC();
        MockPrefService prefService = bc.getPreferencesService();
        Preferences userPref = prefService.getUserPreferences("abc");
        userPref.put("a", "b");
        userPref = prefService.getUserPreferences("abc");
        assertEquals("b", userPref.get("a", null));
    }

    public void testFindChild() {
        MockBC bc = new MockBC();
        MockPrefService prefService = bc.getPreferencesService();
        Preferences userPref = prefService.getUserPreferences("abc");
        Preferences pref = userPref.node("/a/b/c/d");
        pref.put("aaa", "bbb");
        assertNotNull(userPref.node("a"));
        assertNotNull(userPref.node("a").node("b"));
        assertNotNull(userPref.node("a").node("b").node("c"));
        assertNotNull(userPref.node("a").node("b").node("c").node("d"));
        assertEquals("bbb", userPref.node("a").node("b").node("c").node("d").get("aaa", null));
    }

    public void testTypes() {
        MockBC bc = new MockBC();
        MockPrefService prefService = bc.getPreferencesService();
        Preferences pref = prefService.getUserPreferences("abc");
        pref.put("a", "b");
        pref.putBoolean("b", false);
        pref.putByteArray("c", new byte[] { 1 });
        pref.putDouble("d", 2D);
        pref.putFloat("e", 3f);
        pref.putInt("f", 4);
        pref.putLong("g", 5L);
        assertEquals("b", pref.get("a", null));
        assertFalse(pref.getBoolean("b", true));
        assertEquals(1, pref.getByteArray("c", new byte[] { 2 }).length);
        assertEquals(1, pref.getByteArray("c", new byte[] { 2 })[0]);
        assertEquals(2, (int) pref.getDouble("d", 0D));
        assertEquals(3, (int) pref.getFloat("e", 0f));
        assertEquals(4, pref.getInt("f", 0));
        assertEquals(5L, pref.getLong("g", 0));
    }
}
