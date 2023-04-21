package gov.lanl.adirondemo.DemoTests;

import gov.lanl.Utility.NameService;
import iaik.security.provider.IAIK;

/**
   * This server acquires CSI credentials over TLS credentials for
   * accepting connections, prints out who is connecting, accepts all
   * with credentials.  It also uses insecure TCP/IP for initiating
   * connections, and uses this to register with a name server.
   */
public class HelloServer1 {

    static int run(org.omg.CORBA.ORB orb, String[] args) throws org.omg.CORBA.UserException {
        addSrvCsiCredentials(orb);
        org.omg.PortableServer.POA rootPOA = org.omg.PortableServer.POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        org.omg.PortableServer.POAManager manager = rootPOA.the_POAManager();
        Hello_impl helloImpl = new Hello_impl(orb);
        gov.lanl.adirondemo.Demo.Hello hello = helloImpl._this(orb);
        System.out.println("Connecting to Name Service");
        addClientInsecureCredentials(orb);
        NameService ns = new NameService(orb, "");
        ns.register(hello, "jeg/HelloServer");
        System.out.println("jeg/HelloServer is registered");
        System.out.println("NameServer Contents");
        ns.find("jeg");
        manager.activate();
        com.adiron.SecurityLevel3.SecurityCurrent current = com.adiron.SecurityLevel3.SecurityCurrentHelper.narrow(orb.resolve_initial_references("SL3:SecurityCurrent"));
        System.out.println("request_is_local = " + current.request_is_local());
        orb.run();
        return 0;
    }

    public static void addSrvCsiCredentials(org.omg.CORBA.ORB orb) throws org.omg.CORBA.UserException {
        com.adiron.SecurityLevel3.SecurityManager secManager = com.adiron.SecurityLevel3.SecurityManagerHelper.narrow(orb.resolve_initial_references("SL3:SecurityManager"));
        com.adiron.SecurityLevel3.CredentialsCurator curator = secManager.credentials_curator();
        com.adiron.SL3TLS.ArgumentFactory argFac = com.adiron.SL3TLS.ArgumentFactoryHelper.narrow(orb.resolve_initial_references("SL3:ArgumentFactory"));
        com.adiron.SL3TLS.TLSKeyStoreArgBuilder transportArgB = argFac.createTLSKeyStoreArgBuilder(com.adiron.TransportSecurity.CU_AcceptOnly.value);
        transportArgB.addTLSKeyStoreWithStorePass("FILE:../keystores/targetRSA.keystore", "jks", "targetrsakeypass", "server3", "server3pass", new String[0]);
        transportArgB.addTLSX509IdentityVerifier(new CertChainVerifier("Client is", orb));
        com.adiron.SL3CSI.CSIArgBuilder csiArgB = com.adiron.SL3CSI.ArgumentFactoryHelper.narrow(argFac).createCSIArgBuilder(com.adiron.SecurityLevel3.CU_AcceptOnly.value);
        csiArgB.addTransportCredentialsAQArgs(com.adiron.SL3TLS.MID_TLS.value, com.adiron.SL3TLS.AQM_TLSArgs.value, transportArgB.reapAQArg());
        org.omg.CORBA.Any csiArgs = csiArgB.reapAQArg();
        com.adiron.SecurityLevel3.CredentialsAcquirer acquirer = curator.acquire_credentials(com.adiron.SL3CSI.AQM_CSIArgs.value, csiArgs);
        com.adiron.SecurityLevel3.OwnCredentials own = acquirer.get_credentials(true);
        System.out.println("Own Credentials are:");
        System.out.print(own.toString());
    }

    public static void addClientInsecureCredentials(org.omg.CORBA.ORB orb) throws org.omg.CORBA.UserException {
        com.adiron.TransportSecurity.SecurityManager security_manager = com.adiron.TransportSecurity.SecurityManagerHelper.narrow(orb.resolve_initial_references("TransportSecurity:SecurityManager"));
        com.adiron.TransportSecurity.CredentialsCurator curator = security_manager.credentials_curator();
        com.adiron.SL3TCPIP.ArgumentFactory argFac = com.adiron.SL3TCPIP.ArgumentFactoryHelper.narrow(orb.resolve_initial_references("SL3:ArgumentFactory"));
        com.adiron.SL3TCPIP.TCPIPArgBuilder builder = argFac.createTCPIPArgBuilder(com.adiron.SecurityLevel3.CU_InitiateOnly.value);
        org.omg.CORBA.Any targs = builder.reapAQArg();
        com.adiron.TransportSecurity.CredentialsAcquirer acquirer = curator.acquire_credentials(com.adiron.SL3TCPIP.MID_TCPIP.value, com.adiron.SL3TCPIP.AQM_TCPIPArgs.value, targs);
        com.adiron.TransportSecurity.OwnCredentials own = acquirer.get_credentials(true);
        System.out.println("TCP/IP Insecure Credentials are:\n");
        System.out.println(own.toString());
    }

    public static void addServerInsecureCredentials(org.omg.CORBA.ORB orb) throws org.omg.CORBA.UserException {
        com.adiron.TransportSecurity.SecurityManager security_manager = com.adiron.TransportSecurity.SecurityManagerHelper.narrow(orb.resolve_initial_references("TransportSecurity:SecurityManager"));
        com.adiron.TransportSecurity.CredentialsCurator curator = security_manager.credentials_curator();
        com.adiron.SL3TCPIP.ArgumentFactory argFac = com.adiron.SL3TCPIP.ArgumentFactoryHelper.narrow(orb.resolve_initial_references("SL3:ArgumentFactory"));
        com.adiron.SL3TCPIP.TCPIPArgBuilder builder = argFac.createTCPIPArgBuilder(com.adiron.SecurityLevel3.CU_AcceptOnly.value);
        org.omg.CORBA.Any targs = builder.reapAQArg();
        com.adiron.TransportSecurity.CredentialsAcquirer acquirer = curator.acquire_credentials(com.adiron.SL3TCPIP.MID_TCPIP.value, com.adiron.SL3TCPIP.AQM_TCPIPArgs.value, targs);
        com.adiron.TransportSecurity.OwnCredentials own = acquirer.get_credentials(true);
        System.out.println("TCP/IP Insecure Server Credentials are:\n");
        System.out.println(own.toString());
    }

    public static void main(String args[]) {
        java.util.Properties props = System.getProperties();
        props.put("org.omg.CORBA.ORBClass", "com.ooc.CORBA.ORB");
        props.put("org.omg.CORBA.ORBSingletonClass", "com.ooc.CORBA.ORBSingleton");
        props.put("ooc.oci.plugin.iiop", "com.adiron.orbasec.sl3.IIOP");
        props.put("org.omg.PortableInterceptor.ORBInitializerClass." + "com.adiron.orbasec.sl3.ServerInitializer", "");
        int status = 0;
        org.omg.CORBA.ORB orb = null;
        IAIK.addAsJDK14Provider(true);
        try {
            orb = org.omg.CORBA.ORB.init(args, props);
            status = run(orb, args);
        } catch (Exception ex) {
            ex.printStackTrace();
            status = 1;
        }
        if (orb != null) {
            try {
                ((com.ooc.CORBA.ORB) orb).destroy();
            } catch (Exception ex) {
                ex.printStackTrace();
                status = 1;
            }
        }
        System.exit(status);
    }
}
