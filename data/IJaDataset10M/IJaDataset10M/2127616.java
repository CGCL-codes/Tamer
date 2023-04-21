package de.huxhorn.lilith.data.eventsource;

import static de.huxhorn.sulky.junit.JUnitTools.testSerialization;
import static de.huxhorn.sulky.junit.JUnitTools.testXmlSerialization;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class SourceInfoTest {

    private SourceInfo fresh;

    @Before
    public void initFresh() {
        fresh = new SourceInfo();
    }

    @Test
    public void constructorDefault() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SourceInfo original = new SourceInfo();
        testSerialization(original);
        testXmlSerialization(original);
    }

    @Test
    public void source() throws ClassNotFoundException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SourceInfo instance = new SourceInfo();
        SourceIdentifier value = new SourceIdentifier();
        instance.setSource(value);
        {
            SourceInfo obj = testSerialization(instance);
            assertEquals(value, obj.getSource());
            assertFalse(fresh.equals(obj));
        }
        {
            SourceInfo obj = testXmlSerialization(instance);
            assertEquals(value, obj.getSource());
            assertFalse(fresh.equals(obj));
        }
    }

    @Test
    public void numberOfEvents() throws ClassNotFoundException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SourceInfo instance = new SourceInfo();
        long value = 17;
        instance.setNumberOfEvents(value);
        {
            SourceInfo obj = testSerialization(instance);
            assertEquals(value, obj.getNumberOfEvents());
            assertFalse(fresh.equals(obj));
        }
        {
            SourceInfo obj = testXmlSerialization(instance);
            assertEquals(value, obj.getNumberOfEvents());
            assertFalse(fresh.equals(obj));
        }
    }

    @Test
    public void active() throws ClassNotFoundException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SourceInfo instance = new SourceInfo();
        boolean value = true;
        instance.setActive(value);
        {
            SourceInfo obj = testSerialization(instance);
            assertEquals(value, obj.isActive());
            assertFalse(fresh.equals(obj));
        }
        {
            SourceInfo obj = testXmlSerialization(instance);
            assertEquals(value, obj.isActive());
            assertFalse(fresh.equals(obj));
        }
    }

    @Test
    public void oldestEventTimestamp() throws ClassNotFoundException, IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SourceInfo instance = new SourceInfo();
        Date value = new Date();
        instance.setOldestEventTimestamp(value);
        {
            SourceInfo obj = testSerialization(instance);
            assertEquals(value, obj.getOldestEventTimestamp());
            assertFalse(fresh.equals(obj));
        }
        {
            SourceInfo obj = testXmlSerialization(instance);
            assertEquals(value, obj.getOldestEventTimestamp());
            assertFalse(fresh.equals(obj));
        }
    }
}
