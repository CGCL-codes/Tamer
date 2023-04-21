package localTests;

import common.serialization.Serializer;
import data.OtherObject;
import data.SampleObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CollectionsTest {

    @BeforeClass
    public static void setUp() {
        Serializer.registerType(SampleObject.class.getName());
        Serializer.registerType(OtherObject.class.getName());
    }

    @Test
    public void testArrayList() throws Exception {
        ArrayList<SampleObject> arrayList = new ArrayList<SampleObject>();
        arrayList.add(new SampleObject());
        arrayList.add(new SampleObject());
        byte[] bytes = Serializer.encode(arrayList);
        Object[] result = (Object[]) Serializer.decode(bytes);
        Assert.assertArrayEquals(arrayList.get(0).arrayValue, ((SampleObject) result[0]).arrayValue);
    }

    @Test
    public void testArrays() throws Exception {
        Object[] array1 = new Object[] { 1, 2, 3 };
        Object[] array2 = new Object[] { 1, 1.5, false, null, "bla-bla" };
        testArray(new Object[] {});
        testArray(array1);
        testArray(array2);
        testArray(new Object[] { array1, array2 });
    }

    @Test
    public void testList() throws Exception {
        OtherObject otherObject = new OtherObject();
        otherObject.vector = new ArrayList<SampleObject>();
        otherObject.vector.add(new SampleObject());
        otherObject.vector.add(new SampleObject());
        byte[] bytes = Serializer.encode(otherObject);
        OtherObject result = (OtherObject) Serializer.decode(bytes);
        Assert.assertArrayEquals(otherObject.vector.get(0).arrayValue, result.vector.get(0).arrayValue);
    }

    @Test
    public void testMap() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        testMap(map);
        map.put("integer", 5);
        map.put("number", 0.5);
        map.put("bool", false);
        map.put("date", new Date());
        map.put("nullValue", null);
        map.put("arrayValue", new Object[] { 1, 2, 3 });
        testMap(map);
    }

    protected void testMap(Map value) throws Exception {
        byte[] bytes = Serializer.encode(value);
        Map result = (Map) Serializer.decode(bytes);
        Assert.assertArrayEquals(value.keySet().toArray(), result.keySet().toArray());
        Assert.assertArrayEquals(value.values().toArray(), result.values().toArray());
    }

    protected void testArray(Object value) throws Exception {
        byte[] bytes = Serializer.encode(value);
        Object result = Serializer.decode(bytes);
        Assert.assertArrayEquals((Object[]) value, (Object[]) result);
    }
}
