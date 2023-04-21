package ar.com.ironsoft.javaopenauth.utils;

import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;

public class MapUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void test_queryStringToMap_null() {
        MapUtils.queryStringToMap(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_queryStringToMap_malformed() {
        MapUtils.queryStringToMap("asd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_queryStringToMap_malformed_2() {
        MapUtils.queryStringToMap("asd=asd&asd");
    }

    @Test()
    public void test_queryStringToMap() {
        Map<String, String> response = MapUtils.queryStringToMap("key1=value1&key2=value2");
        Assert.assertEquals("value1", response.get("key1"));
        Assert.assertEquals("value2", response.get("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_mapToQueryStringEncoded_null() {
        MapUtils.mapToQueryStringEncoded(null);
    }

    @Test()
    public void test_mapToQueryStringEncoded_emptyMap() {
        String response = MapUtils.mapToQueryStringEncoded(new HashMap<String, String>());
        Assert.assertEquals("", response);
    }

    @Test()
    public void test_mapToQueryStringEncoded() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String response = MapUtils.mapToQueryStringEncoded(map);
        Assert.assertEquals("key2=value2&key1=value1", response);
    }
}
