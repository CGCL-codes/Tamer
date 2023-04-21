package org.arastreju.core.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import junit.framework.TestCase;
import org.arastreju.api.security.User;
import org.arastreju.core.model.security.GroupDBO;
import org.arastreju.core.model.security.UserDBO;

/**
 * Technical JAXB test case.
 * 
 * Created: 09.03.2009
 *
 * @author Oliver Tigges
 */
public class JaxbTest extends TestCase {

    public void testToXml() {
        User user = new UserDBO("knut", "geheim", null, true);
        user.addToGroup(new GroupDBO("admins"));
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            JAXBContext context = JAXBContext.newInstance(UserDBO.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(user, buffer);
            Unmarshaller u = context.createUnmarshaller();
            User read = (User) u.unmarshal(new ByteArrayInputStream(buffer.toByteArray()));
            assert (user.getName().equals("knut"));
            System.out.println(read);
        } catch (JAXBException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
