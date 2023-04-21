package net.sf.doolin.sdo;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import net.sf.doolin.array.Array;
import net.sf.doolin.sdo.factory.json.JSONDataFactoryReader;
import net.sf.doolin.sdo.support.DataFactories;
import org.junit.Before;
import org.junit.Test;

public class AbstractTypesTest {

    private DataFactory factory;

    @Test
    public void abstractArray() {
        DataObject freeAddress = this.factory.getType("Base::FreeAddress").newInstance();
        freeAddress.setString("country", "BE");
        freeAddress.setArray("content", new Array<String>("My address\nCity"));
        DataObject structuredAddress = this.factory.getType("Base::StructuredAddress").newInstance();
        structuredAddress.setString("country", "BE").setString("street", "My street").setString("zip", "1111").setString("city", "My City");
        DataObject person = this.factory.getType("Base::Person").newInstance();
        person.setString("name", "Damien");
        Array<DataObject> addresses = new Array<DataObject>(freeAddress, structuredAddress);
        person.setArray("addresses", addresses);
        String actual = person.toString();
        String expected = "{\"sdo:type\":\"Base::Person\",\"sdo:id\":1,\"name\":\"Damien\",\"addresses\":[{\"sdo:type\":\"Base::FreeAddress\",\"sdo:id\":1,\"country\":\"BE\",\"content\":[\"My address\\nCity\"]},{\"sdo:type\":\"Base::StructuredAddress\",\"sdo:id\":1,\"country\":\"BE\",\"street\":\"My street\",\"zip\":\"1111\",\"city\":\"My City\"}]}";
        assertEquals(expected, actual);
    }

    @Test
    public void abstractType() {
        DataObject freeAddress = this.factory.getType("Base::FreeAddress").newInstance();
        freeAddress.setString("country", "BE");
        freeAddress.setArray("content", new Array<String>("My address\nCity"));
        DataObject person = this.factory.getType("Base::Person").newInstance();
        person.setString("name", "Damien");
        person.setDataObject("mainAddress", freeAddress);
        String actual = person.toString();
        String expected = "{\"sdo:type\":\"Base::Person\",\"sdo:id\":1,\"name\":\"Damien\",\"mainAddress\":{\"sdo:type\":\"Base::FreeAddress\",\"sdo:id\":1,\"country\":\"BE\",\"content\":[\"My address\\nCity\"]}}";
        assertEquals(expected, actual);
    }

    @Before
    public void init() throws IOException {
        this.factory = DataFactories.typedFactory();
        new JSONDataFactoryReader().read(getClass().getResource("/abstract-array.json"), this.factory);
    }
}
