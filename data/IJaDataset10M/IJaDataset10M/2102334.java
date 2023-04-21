package ecom.client;

import java.util.Properties;
import ecom.beans.*;
import ecom.util.shell.AdminEcomShell;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ExternAdmin {

    Context initialContext = null;

    public static void main(String[] args) {
        try {
            System.out.println("Beginning Admin...");
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.objectweb.carol.jndi.spi.MultiOrbInitialContextFactory");
            Properties props = new Properties();
            props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
            props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
            props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
            System.out.println("Properties set");
            Context initialContext = null;
            try {
                initialContext = new InitialContext(props);
            } catch (Exception e) {
                System.err.println("Cannot get initial context for JNDI: " + e);
                System.exit(2);
            }
            EcomAdminRemote ecomAdminBean = null;
            try {
                ecomAdminBean = (EcomAdminRemote) initialContext.lookup("EcomAdminEJB");
            } catch (NamingException e) {
                System.err.println("Cannot get EcomAdminBean : " + e);
                System.exit(2);
            }
            new AdminEcomShell(args, ecomAdminBean).run();
        } catch (Exception e) {
            System.err.println("Client get an exception " + e);
            System.exit(2);
        }
    }
}
