package marquee.xmlrpc.objectcomm.example;

import marquee.xmlrpc.objectcomm.Server;

/**
 *  @author  Rainer Bischof (rainer.bischof@eds.com)
 *  @version $Revision: 1.1 $
 */
public class XmlRmiServer {

    public static void main(String[] args) {
        XmlRmiEmployeeService service = null;
        try {
            service = new XmlRmiEmployeeService();
        } catch (java.rmi.RemoteException re) {
            re.printStackTrace();
            System.exit(1);
        }
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("//localhost:1099/employeeservice", service);
            System.out.println("Service bound to registry, awaiting requests");
        } catch (Exception e) {
            System.out.println("Unable to bind object to registry");
            e.printStackTrace();
        }
        Server server = new Server();
        server.registerProxyService(service);
        try {
            server.runAsService(8080);
        } catch (java.io.IOException e) {
            System.out.println("Unable to start XmlRpc service!");
            e.printStackTrace();
        }
    }
}
