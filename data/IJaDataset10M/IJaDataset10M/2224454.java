package net.sf.doolin.sdo.io.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import net.sf.doolin.array.Array;
import net.sf.doolin.sdo.DataFactory;
import net.sf.doolin.sdo.DataObject;
import net.sf.doolin.sdo.TstUtils;
import net.sf.doolin.sdo.factory.json.JSONDataFactoryReader;
import net.sf.doolin.sdo.io.DataObjectIO;
import net.sf.doolin.sdo.support.DataFactories;
import org.junit.Before;
import org.junit.Test;

public class JSONDataObjectReaderTest {

    private DataFactory factory;

    @Before
    public void init() throws IOException {
        this.factory = DataFactories.typedFactory();
        new JSONDataFactoryReader().read(getClass().getResource("/base.json"), this.factory);
    }

    @Test
    public void test() throws IOException {
        String json = TstUtils.readResource("/json.txt");
        DataObject o = DataObjectIO.readString(json, new JSONDataObjectReader(), this.factory);
        System.out.println(o);
        assertNotNull(o);
        assertEquals("Base::Person", o.getType().toString());
        assertEquals("Damien", o.getString("firstName"));
        assertEquals("Coraboeuf", o.getString("lastName"));
        assertEquals(true, o.getBoolean("male"));
        assertEquals(Integer.valueOf(37), o.getInteger("age"));
        {
            DataObject address = o.getDataObject("mainAddress");
            assertNotNull(address);
            assertEquals("Base::Address", address.getType().toString());
            assertEquals("Brussels", address.getString("city"));
        }
        Array<String> phones = o.getArray("phones");
        assertNotNull(phones);
        assertEquals(2, phones.length());
        assertEquals("002", phones.get(0));
        assertEquals("107", phones.get(1));
        Array<DataObject> addresses = o.getArray("addresses");
        assertNotNull(addresses);
        assertEquals(1, addresses.getLength());
        {
            DataObject address = addresses.get(0);
            assertNotNull(address);
            assertEquals("Base::Address", address.getType().toString());
            assertEquals("Paris", address.getString("city"));
        }
    }
}
