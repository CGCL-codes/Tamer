package org.expasy.jpl.commons.base.io;

import java.util.Calendar;
import org.expasy.jpl.commons.base.io.Serializer;
import org.junit.Test;

public class SerializerTest {

    Calendar calendar = Calendar.getInstance();

    @Test
    public void testSerializing() throws Exception {
        Serializer<Calendar> serializer = Serializer.newInstance();
        serializer.serialize(calendar, "/tmp/caltest.ser");
        final Calendar cal = serializer.deserialize("/tmp/caltest.ser");
        System.out.println(cal);
    }
}
