package org.mattressframework.core;

import java.util.Locale;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.mattressframework.test.TestConstants;

public class CaseInsensitiveStringKeyHashMapTest {

    private static final String TEST_STRING_LOWERCASE = TestConstants.TEST_STRING.toLowerCase(Locale.getDefault());

    private static final Object TEST_OBJECT = new Object();

    @Test
    public void testAll() throws Exception {
        Map<String, Object> map = new CaseInsensitiveStringKeyHashMap<Object>(1);
        Assert.assertNull(map.put(TestConstants.TEST_STRING, TEST_OBJECT));
        Assert.assertTrue(map.containsKey(TestConstants.TEST_STRING));
        Assert.assertTrue(map.containsKey(TEST_STRING_LOWERCASE));
        Assert.assertEquals(TEST_OBJECT, map.get(TestConstants.TEST_STRING));
        Assert.assertEquals(TEST_OBJECT, map.get(TEST_STRING_LOWERCASE));
        Assert.assertEquals(TEST_OBJECT, map.remove(TestConstants.TEST_STRING));
    }

    @Test
    public void testGetCaseInsensitiveStringKey() throws Exception {
        String key = CaseInsensitiveStringKeyHashMap.getCaseInsensitiveStringKey(TestConstants.TEST_STRING);
        Assert.assertEquals(TEST_STRING_LOWERCASE, key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCaseInsensitiveStringKey_IllegalArgument() throws Exception {
        CaseInsensitiveStringKeyHashMap.getCaseInsensitiveStringKey(TEST_OBJECT);
    }
}
