package org.ebayopensource.twin.json;

import java.util.*;
import org.testng.annotations.*;
import static org.testng.AssertJUnit.*;

public class JSONableTest {

    public static class JSONableExample implements JSONable {

        private Object result;

        public JSONableExample(Object result) {
            this.result = result;
        }

        public Object toJSON() {
            return result;
        }
    }

    @DataProvider(name = "roundtrip")
    public static Object[][] data() {
        String string = "text";
        Integer integer = 2;
        Double dbl = 3.5;
        List<?> list = Arrays.asList(1, 2, 3, null);
        Object[] array = new Object[] { 1, 2, 3, null };
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("string", string);
        map.put("list", list);
        map.put("null", null);
        return new Object[][] { { "string", string, string }, { "integer", integer, integer }, { "double", dbl, dbl }, { "list", list, list }, { "array", list, array }, { "map", map, map }, { "null", null, null } };
    }

    @Test(dataProvider = "roundtrip")
    public static void jsonableRoundTrip(String description, Object result, Object input) throws Throwable {
        assertEquals("roundtrip " + description, result, roundTrip(input));
    }

    private static Object roundTrip(Object data) {
        JSONableExample ex = new JSONableExample(data);
        String json = JSON.toString(ex);
        return JSON.decode(json);
    }
}
