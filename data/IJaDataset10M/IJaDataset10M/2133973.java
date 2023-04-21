package com.rwatsh.clients;

import com.rwatsh.ejb3.TravelAgentRemote;
import com.rwatsh.ejb3.domain.Cabin;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Properties;
import javax.rmi.PortableRemoteObject;
import static java.lang.System.out;

public class Client {

    public static void main(String[] args) {
        try {
            Context jndiContext = getInitialContext();
            Object ref = jndiContext.lookup("TravelAgentRemoteBean/remote");
            TravelAgentRemote dao = (TravelAgentRemote) PortableRemoteObject.narrow(ref, TravelAgentRemote.class);
            Cabin cabin1 = new Cabin();
            cabin1.setId(2);
            cabin1.setName("Master Suite");
            cabin1.setDeckLevel(1);
            cabin1.setShipId(1);
            cabin1.setBedCount(3);
            dao.createCabin(cabin1);
            Cabin cabin2 = dao.findCabin(2);
            out.println(cabin2.getName());
            out.println(cabin2.getDeckLevel());
            out.println(cabin2.getShipId());
            out.println(cabin2.getShipId());
            out.println(cabin2.getBedCount());
        } catch (NamingException ne) {
            ne.printStackTrace();
        }
    }

    private static Context getInitialContext() throws NamingException {
        Properties p = new Properties();
        return new InitialContext(p);
    }
}
