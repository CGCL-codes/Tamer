package gov.lanl.proxy.COAS;

import org.apache.log4j.Logger;
import gov.lanl.Utility.ConfigProperties;
import gov.lanl.Utility.PrintableConfigProperties;
import gov.lanl.Utility.NameService;
import gov.lanl.SSLTools.ServiceFactory;
import gov.lanl.ObservationManager.ObservationComponentHelper;
import gov.lanl.proxy.COAS.AccessComponent;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: dwf
 * Date: Jul 16, 2003
 * Time: 3:24:04 PM
 * To change this template use Options | File Templates.
 */
public class ObservationComponent extends gov.lanl.ObservationManager.ObservationComponentPOA {

    static final String version = "$Revision: 3334 $";

    static final String date = "$Date: 2006-03-27 16:11:33 -0700 (Mon, 27 Mar 2006) $";

    private static Logger cat = Logger.getLogger(ObservationComponent.class.getName());

    String serverName = "ObservationComponent";

    public AccessComponent accComp;

    public QueryAccess queryAccess;

    org.omg.CORBA.ORB theOrb;

    boolean debug;

    static ObservationMgr obsMgr;

    public ObservationLoader observationLoader;

    public gov.lanl.ObservationManager.ObservationComponent obsComp;

    private NameService ns;

    static boolean SSL = false;

    String privKeyInfoPath = null;

    String privPwd = null;

    String certPath = null;

    String proxyServerName = null;

    public ConfigProperties props = new ConfigProperties();

    static boolean useServices = false;

    /**
     * The constructor
     *
     * @param inOrb the ORB
     * @param serverName the name of the server
     * @param props a Propertie class
     */
    public ObservationComponent(org.omg.CORBA.ORB inOrb, String serverName, ConfigProperties props) {
        this.serverName = serverName;
        theOrb = inOrb;
        this.props = props;
    }

    /**
     * Initialize servant object after creation
     */
    private void init() {
        observationLoader = new ObservationLoader(this);
        queryAccess = new QueryAccess(this);
        accComp = new AccessComponent(this);
        obsMgr = new ObservationMgr(this);
        doConnect();
    }

    /**
     * get referenced components
     * @return AccessComponentData
     */
    public org.omg.DsObservationAccess.AccessComponentData get_external_components() {
        doConnect();
        return getObsComp().get_components();
    }

    /**
     *
     * @return AccessComponentImpl
     */
    public AccessComponent getAccessComponent() {
        return accComp;
    }

    /**
     *
     */
    public boolean doConnect() {
        boolean connected = false;
        proxyServerName = props.getProperty("ProxyServer", "proxyCoas");
        cat.debug("ProxyServer: " + proxyServerName);
        String iorURL = props.getProperty("ior");
        ns = new NameService(theOrb, iorURL);
        org.omg.CORBA.Object obj = ns.connect(proxyServerName);
        obsComp = ObservationComponentHelper.narrow(obj);
        if (obsComp != null) connected = true;
        debug = props.getProperty("debug", "false").equalsIgnoreCase("true");
        return connected;
    }

    /**
     * Return a ObservationMgr component, which provides edit and update functions
     */
    public gov.lanl.ObservationManager.ObservationMgr get_observation_mgr() {
        cat.debug("get_observation_mgr called");
        if (obsMgr != null) return obsMgr._this(); else return null;
    }

    /**
     * get the ObservationComponent that this object proxies.
     * @return ObservationComponent
     */
    public gov.lanl.ObservationManager.ObservationComponent getObsComp() {
        doConnect();
        return obsComp;
    }

    /**
     * M A I N
     *
     * @param args command line args
     */
    public static void main(String args[]) {
        PrintableConfigProperties props = new PrintableConfigProperties();
        props.setProperties("ObservationComponent.properties", args);
        props.print(true);
        String serverName = props.getProperty("serverName", "ObservationComponent");
        cat.info("Starting " + ObservationComponent.class.getName() + " as CORBA server '" + serverName + "'");
        cat.info(version);
        cat.info(date);
        org.omg.CORBA.ORB orb = null;
        try {
            gov.lanl.SSLTools.ServiceInterface sslService = ServiceFactory.createServiceInterface(args);
            orb = sslService.getORB();
            cat.info("SSL ORB initialized");
            if (orb == null) {
                orb = org.omg.CORBA.ORB.init(args, props);
                cat.info("ORB initialized");
            }
            org.omg.PortableServer.POA rootPOA = org.omg.PortableServer.POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            cat.info("rootPOA initialized");
            String iorURL = props.getProperty("NameService", "");
            gov.lanl.Utility.NameService ns;
            if (sslService != null) {
                ns = new gov.lanl.Utility.NameService(sslService, iorURL);
            } else {
                ns = new gov.lanl.Utility.NameService(orb, iorURL);
            }
            ObservationComponent obsCompImpl = new ObservationComponent(orb, serverName, props);
            obsCompImpl.init();
            gov.lanl.ObservationManager.ObservationComponent obsComp = obsCompImpl._this(orb);
            ns.register(obsComp, serverName);
            cat.debug("ORB reference: " + orb.object_to_string(obsComp));
            obsCompImpl.object_to_file();
            cat.info("Waiting for requests...");
            rootPOA.the_POAManager().activate();
            orb.run();
        } catch (org.omg.CORBA.SystemException e) {
            cat.fatal(e, e);
            System.exit(1);
        } catch (Exception e) {
            cat.fatal(e, e);
            System.exit(1);
        }
    }

    /**
     * Write IOR file for object
     *
     * @see
     */
    public void object_to_file() {
        String iorFile = props.getProperty("iorfile", "coas.ior");
        String ior = theOrb.object_to_string(_this());
        try {
            FileOutputStream outStream = new FileOutputStream(iorFile);
            PrintWriter outWriter = new PrintWriter(outStream);
            outWriter.println(ior);
            outWriter.close();
        } catch (IOException e) {
            System.err.println("IdentificationComponent: Failed to open iorfile " + iorFile);
        }
    }

    /**
     * A complete list of query codes for which this server can supply responses. Parameter
     * max_sequence indicates the number of codes which the client wishes to be returned
     * within the immediately returned sequence. Parameter the_rest contains an iterator for
     * remaining items if and only if the number of codes is greater than max_sequence.
     *
     * @param max_sequence
     * @param the_rest
     */
    public java.lang.String[] get_supported_codes(int max_sequence, org.omg.DsObservationAccess.QualifiedCodeIteratorHolder the_rest) {
        if (cat.isDebugEnabled()) {
            cat.debug("get_supported_codes called with max_sequence = " + max_sequence);
        }
        return getObsComp().get_supported_codes(max_sequence, the_rest);
    }

    /**
     * Supplies the ObservationType (CORBA::TCKind and CORBA::RepositoryId) of the
     * data type that will be returned for the given code.
     * For codes which return primitives like 'long' or 'string', the tc_kind attribute of
     * ObservationType is sufficient to completely specify the type (like tk_long or
     * tk_string), and the id attribute is empty. For objects, tc_kind will be set to tk_objref,
     * and the id attribute will hold the repository id of the object.
     * The correspondence between query code and return data type can be dynamically
     * determined by querying the AccessComponentImpl.get_observation_type() operation with a
     * given code. However, a typical client will have such correspondences hardwired. A
     * client will expect a certain data type for a given query code, and will have static
     * programming to extract such data from the CORBA::any.
     *
     * @param observation_type
     * @exception org.omg.DsObservationAccess.InvalidCodes
     * @exception org.omg.DsObservationAccess.NotImplemented
     */
    public org.omg.CORBA.TypeCode get_type_code_for_observation_type(java.lang.String observation_type) throws org.omg.DsObservationAccess.InvalidCodes, org.omg.DsObservationAccess.NotImplemented {
        if (cat.isDebugEnabled()) {
            cat.debug("get_type_code_for_observation_type called with observation_type = " + observation_type);
        }
        return getObsComp().get_type_code_for_observation_type(observation_type);
    }

    /**
     * This operation returns an AccessComponentData. AccessComponentData contains
     * references to all implemented components as a convenience for clients that have one
     * reference to a component, and wish to use a different component.
     */
    public org.omg.DsObservationAccess.AccessComponentData get_components() {
        cat.debug("get_components called");
        return accComp.get_components();
    }

    /**
     * A complete list of qualifiers which this server can match, and also supply as returned
     * qualifiers, with respect to the given observation code. A server may be able to
     * match/supply different sets of qualifiers for different codes.
     *
     * @param qualifiers
     * @exception org.omg.DsObservationAccess.InvalidCodes
     * @exception org.omg.DsObservationAccess.NotImplemented
     */
    public java.lang.String[] get_supported_qualifiers(java.lang.String qualifiers) throws org.omg.DsObservationAccess.InvalidCodes, org.omg.DsObservationAccess.NotImplemented {
        if (cat.isDebugEnabled()) {
            cat.debug("get_supported_qualifiers called with qualifier = " + qualifiers);
        }
        return getObsComp().get_supported_qualifiers(qualifiers);
    }

    /**
     * A complete list of policies which this server can employ when filtering on desired
     * observations. The returned list is of codes only.
     */
    public java.lang.String[] get_supported_policies() {
        cat.debug("get_supported_policies called");
        return getObsComp().get_supported_policies();
    }

    /**
     * The policies which are in effect unless overridden via get_observations_with_policy().
     * The returned list is a list of name-value pairs, both the name of the policy and its
     * default value.
     */
    public org.omg.DsObservationAccess.NameValuePair[] get_default_policies() {
        cat.debug("get_default_policies called");
        return getObsComp().get_default_policies();
    }

    /**
     * Version of COAS specification supported by this DsObservationAccess server, starting
     * with '1.0' for the first approved specification.
     */
    public java.lang.String coas_version() {
        cat.debug("coas_version called");
        return getObsComp().coas_version();
    }

    /**
     * A reference to this OMG standard service.
     */
    public org.omg.PersonIdService.IdentificationComponent pid_service() {
        cat.debug("pid_service called");
        return accComp.pid_service();
    }

    /**
     * A reference to this OMG standard service.
     */
    public org.omg.TerminologyServices.TerminologyService terminology_service() {
        cat.debug("terminology_service called");
        return accComp.terminology_service();
    }

    /**
     * A reference to this OMG standard service.
     */
    public org.omg.CosTrading.TraderComponents trader_service() {
        cat.debug("trader_service called");
        return accComp.trader_service();
    }

    /**
     * A reference to this OMG standard service.
     */
    public org.omg.CosNaming.NamingContext naming_service() {
        cat.debug("naming_service called");
        return accComp.naming_service();
    }

    /**
     * Returns a boolean describing whether this component can return iterators
     * ObservationDataIterator and iterators for some of the data values in
     * DsObservationValue.idl. Iterators are remote objects.
     * If a server does not support iterators, all ObservationData and ObservationValue items
     * are returned within sequences, and all out-parameter iterators returned as null. In this
     * case, the input parameter max_sequence (present in many operations, indicating the
     * client's preferred number of items returned in the sequence) is ignored by the server as
     * it returns all observations within the sequence.
     * If a server supports iterators, the server will pay attention to the max_sequence input
     * parameter, and an iterator will be instantiated and returned when the search for
     * observations is successful and the input parameter max_sequence is set to less than the
     * total number of observations found. Returning an iterator requires the server to be
     * stateful, since the iterator is a remote object which must be instantiated and registered
     * with the ORB for some lifetime.
     * For example, an implementer expecting a very large and busy set of clients may want
     * to make a QueryAccess component which is stateless, and thus choose to return
     * FALSE to are_iterators_supported().
     */
    public boolean are_iterators_supported() {
        cat.debug("are_iterators_supported called");
        return getObsComp().are_iterators_supported();
    }

    /**
     * Return a timestamp for the current time on the server. This can be useful for a client
     * which resides in another time zone or which has questionable date/time settings (like a
     * PC). A client can base a query on the server's time rather than the client's time.
     */
    public java.lang.String get_current_time() {
        cat.debug("get_current_time");
        return getObsComp().get_current_time();
    }
}
