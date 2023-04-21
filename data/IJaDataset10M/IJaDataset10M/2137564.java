package org.fcrepo.server.security.xacml.pep;

/**
 * @author nishen@melcoe.mq.edu.au
 */
public interface PDPClient {

    /**
     * Sends an XACML request for evaluation to the PDP.
     * 
     * @param request
     *        an XACML request as a String
     * @return an XACML reponse as a String
     * @throws PEPException
     */
    public String evaluate(String request) throws PEPException;

    /**
     * Sends a String array of XACML requests for evaluation to the PDP. A
     * single resposne with the results of all requests is returned.
     * 
     * @param request
     *        a String array of XACML requests
     * @return an XACML reponse as a String containing results for all requests
     * @throws PEPException
     */
    public String evaluateBatch(String[] request) throws PEPException;
}
