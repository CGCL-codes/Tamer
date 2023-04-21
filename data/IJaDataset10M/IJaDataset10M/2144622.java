package gov.lanl.COAS;

import org.omg.DsObservationAccess.ObservationLoader;
import org.omg.DsObservationAccess.AccessComponentData;

/**
 * Created by IntelliJ IDEA.
 * User: dwf
 * Date: Jul 16, 2003
 * Time: 3:48:10 PM
 * To change this template use Options | File Templates.
 */
public class ProxyObservationLoader extends org.omg.DsObservationAccess.ObservationLoaderPOA {

    gov.lanl.Utility.ConfigProperties props;

    ProxyAccessComponent parent;

    ProxyObservationComponent obsParent;

    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger(ProxyObservationLoader.class);

    /**
     * Build the Loader connected to the parent object
     *
     *
     * @param parent
     *
     * @see
     */
    public ProxyObservationLoader(ProxyObservationComponent parent) {
        this.obsParent = parent;
        this._this(parent.theOrb);
    }

    public ProxyObservationLoader(ProxyAccessComponent parent) {
        this.parent = parent;
        this._this(parent.theOrb);
    }

    /**
     * get the ProxyAccessComponent independent of how this object was created.
     * @return
     */
    private ProxyAccessComponent getAccessComponent() {
        if (parent != null) return parent;
        if (obsParent != null) return obsParent.getAccessComponent();
        cat.error("getAccessComponent has no parent, which is impossible!");
        return null;
    }

    /**
     * get the external objects
     * @return
     */
    private AccessComponentData get_external_components() {
        if (parent != null) return parent.get_external_components(); else if (obsParent != null) return obsParent.get_external_components(); else return null;
    }

    /**
     * get the external Observatoin Loader that is being proxied
     * @return
     */
    private ObservationLoader getObsLoader() {
        return get_external_components().observation_loader;
    }

    /**
     * Load a sequence of observations
     *
     *
     * @param observations
     *
     * @see
     */
    public void load_observations(org.omg.CORBA.Any[] observations) {
        cat.debug("load_observations: " + observations.length + " being processed");
        getObsLoader().load_observations(observations);
    }

    /**
     * This operation returns an AccessComponentData. AccessComponentData contains
     * references to all implemented components as a convenience for clients that have one
     * reference to a component, and wish to use a different component.
     *
     * @return the set of components
     */
    public org.omg.DsObservationAccess.AccessComponentData get_components() {
        return getAccessComponent().get_components();
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
        return get_external_components().query_access.get_supported_codes(max_sequence, the_rest);
    }

    /**
     * A complete list of qualifiers which this server can match, and also supply as returned
     * qualifiers, with respect to the given observation code. A server may be able to
     * match/supply different sets of qualifiers for different codes.
     *
     * @param code
     * @exception org.omg.DsObservationAccess.InvalidCodes
     * @exception org.omg.DsObservationAccess.NotImplemented
     */
    public java.lang.String[] get_supported_qualifiers(java.lang.String code) throws org.omg.DsObservationAccess.InvalidCodes, org.omg.DsObservationAccess.NotImplemented {
        return get_external_components().query_access.get_supported_qualifiers(code);
    }

    /**
     * A complete list of policies which this server can employ when filtering on desired
     * observations. The returned list is of codes only.
     */
    public java.lang.String[] get_supported_policies() {
        return get_external_components().query_access.get_supported_policies();
    }

    /**
     * The policies which are in effect unless overridden via get_observations_with_policy().
     * The returned list is a list of name-value pairs, both the name of the policy and its
     * default value.
     */
    public org.omg.DsObservationAccess.NameValuePair[] get_default_policies() {
        return get_external_components().query_access.get_default_policies();
    }

    /**
     * Supplies the ObservationType (CORBA::TCKind and CORBA::RepositoryId) of the
     * data type that will be returned for the given code.
     * For codes which return primitives like 'long' or 'string', the tc_kind attribute of
     * ObservationType is sufficient to completely specify the type (like tk_long or
     * tk_string), and the id attribute is empty. For objects, tc_kind will be set to tk_objref,
     * and the id attribute will hold the repository id of the object.
     * The correspondence between query code and return data type can be dynamically
     * determined by querying the AccessComponent.get_observation_type() operation with a
     * given code. However, a typical client will have such correspondences hardwired. A
     * client will expect a certain data type for a given query code, and will have static
     * programming to extract such data from the CORBA::any.
     *
     * @param observation_type
     * @exception org.omg.DsObservationAccess.InvalidCodes
     * @exception org.omg.DsObservationAccess.NotImplemented
     */
    public org.omg.CORBA.TypeCode get_type_code_for_observation_type(java.lang.String observation_type) throws org.omg.DsObservationAccess.InvalidCodes, org.omg.DsObservationAccess.NotImplemented {
        return get_external_components().query_access.get_type_code_for_observation_type(observation_type);
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
        return get_external_components().query_access.are_iterators_supported();
    }

    /**
     * Return a timestamp for the current time on the server. This can be useful for a client
     * which resides in another time zone or which has questionable date/time settings (like a
     * PC). A client can base a query on the server's time rather than the client's time.
     */
    public java.lang.String get_current_time() {
        return get_external_components().query_access.get_current_time();
    }

    /**
     * Version of COAS specification supported by this DsObservationAccess server, starting
     * with '1.0' for the first approved specification.
     */
    public java.lang.String coas_version() {
        return get_external_components().query_access.coas_version();
    }

    /**
     * a reference to this OMG standard service related to this server
     *
     *
     * @return appropriate PIDS server object
     *
     * @see
     */
    public org.omg.PersonIdService.IdentificationComponent pid_service() {
        return getAccessComponent().pid_service();
    }

    /**
     * a reference to this OMG standard service related to this server
     *
     *
     * @return object reference
     *
     * @see
     */
    public org.omg.TerminologyServices.TerminologyService terminology_service() {
        return getAccessComponent().terminology_service();
    }

    /**
     * a reference to this OMG standard service related to this server
     *
     *
     * @return object reference
     *
     * @see
     */
    public org.omg.CosTrading.TraderComponents trader_service() {
        return getAccessComponent().trader_service();
    }

    /**
     * a reference to this OMG standard service related to this server
     *
     *
     * @return object reference
     *
     * @see
     */
    public org.omg.CosNaming.NamingContext naming_service() {
        return getAccessComponent().naming_service();
    }
}
