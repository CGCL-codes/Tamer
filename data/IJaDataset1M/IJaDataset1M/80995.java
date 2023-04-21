package samples;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class AddressSerializer {

    public static void main(String[] args) throws Exception {
        Address addr = new Address(175, "Main Street - Suite 200", "New City", State.OH, 59101, new Phone(758, "874", "1221"));
        Order order = new Order();
        order.addr = addr;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File(args[0]));
            jaxbMarshaller.setSchema(schema);
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(order, ba);
            System.out.println(ba.toString());
            Unmarshaller u = jaxbContext.createUnmarshaller();
            Order roundTripOrder = (Order) u.unmarshal(new StringReader(ba.toString()));
            printAddress(roundTripOrder.addr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printAddress(Address addr) {
        System.out.println("street num = " + addr.getStreetNum());
        System.out.println("street name = " + addr.getStreetName());
        System.out.println("city = " + addr.getCity());
        System.out.println("state = " + addr.getState());
        System.out.println("zip = " + addr.getZip());
        System.out.println("street num = " + addr.getStreetNum());
        System.out.println("phone area = " + addr.getPhoneNumber().getAreaCode());
        System.out.println("phone exchange = " + addr.getPhoneNumber().getExchange());
        System.out.println("phone number = " + addr.getPhoneNumber().getNumber());
    }
}
