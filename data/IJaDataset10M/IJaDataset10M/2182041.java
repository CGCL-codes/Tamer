package org.dbe.composer.wfengine.bpel.server.addressing;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import org.dbe.composer.wfengine.bpel.ISdlPartnerLink;
import org.dbe.composer.wfengine.bpel.IServiceEndpointReference;
import org.dbe.composer.wfengine.bpel.SdlBusinessProcessException;
import org.dbe.composer.wfengine.bpel.serviceid.IServiceId;
import org.w3c.dom.Element;

/**
 * Partner Addressing refers to the process of finding the correct endpoint reference
 * for a given business partner. The endpoint addressing information is obtained from
 * one of the following sources:
 *    STATIC - The address exists within the business process deployment descriptor file.
 *             This is the easiest place to store the address but it's the least flexible
 *             since it requires redeploying the address in order to make a change and it
 *             also makes management difficult since the information is distributed across
 *             all of the deployed processes and makes aggregation and reporting cumbersome

 *   DYNAMIC - The address is not known until the execution of the process at which time
 *             it is dynamically assigned during the execution of an assign activity.
 *             Since there is nothing known about the endpoint until the process executes,
 *             this offers little in the way of management abilities.
 *
 * PRINCIPAL - The authenticated principal that invoked the process is used as a key
 *             into a separate system which houses endpoint addresses. This allows all
 *             of the endpoints to be stored outside of the bpel process and allows
 *             a much greater degree of flexibility since they can be changed without
 *             having to redeploy the process. The only side effect is that this forces
 *             the web service layer to authenticate the caller - which is probably
 *             already done in most production bpel processes.
 *
 *   INVOKER - The address is extracted from the MessageContext as it comes into the
 *             web service framework. It is expected that the headers within the message
 *             will contain some kind of authentication information (i.e. WS-Security).
 */
public interface ISdlPartnerAddressing {

    /**
     * Reads a static endpoint reference encoded within the element.
     *
     * This is broken out from the deployment reader in the event that we ever move
     * away from WS-Addressing, which is a possibility until WS-Addressing becomes
     * an open source standard. We may need to have pluggable support for the xml
     * formatted endpoint reference objects.
     * @param aElement
     * @return IAeEndpointReference or null if the source type wasn't static
     */
    public IServiceId readServiceIdFromDeployment(Element aElement) throws SdlBusinessProcessException;

    /**
    * Reads a static endpoint reference encoded within the element.
    *
    * This is broken out from the deployment reader in the event that we ever move
    * away from WS-Addressing, which is a possibility until WS-Addressing becomes
    * an open source standard. We may need to have pluggable support for the xml
    * formatted endpoint reference objects.
    * @param aElement
    * @return IServiceEndpointReference or null if the source type wasn't static
    */
    public IServiceEndpointReference readFromDeployment(Element aElement) throws SdlBusinessProcessException;

    /**
    * If the partner link's endpoint source is set to "invoker" then the code will
    * attempt to extract an IAeEndpointReference object from the metadata in the
    * SOAPMessageContext. Currently, this is limited to extracting WS-Addressing
    * compliant SOAP headers from a SOAPMessageContext. This gets called from the
    * web service handler since that's the layer that has access to the context.
    *
    * Called from the deployment descriptor if the source type for the partner link
    * was set to "invoker".
    * @param aContext
    */
    public IServiceEndpointReference readFromContext(SOAPMessageContext aContext) throws SdlBusinessProcessException;

    /**
    * If the partner link's endpoint source is set to "invoker" then the code will
    * attempt to extract an IAeEndpointReference object from the metadata in the
    * SOAPMessageContext. Currently, this is limited to extracting WS-Addressing
    * compliant SOAP headers from a SOAPMessageContext. This gets called from the
    * web service handler since that's the layer that has access to the context.
    *
    * Called from the deployment descriptor if the source type for the partner link
    * was set to "invoker".
    * @param aContext
    */
    public IServiceId readServiceIdFromContext(SOAPMessageContext aContext) throws SdlBusinessProcessException;

    /**
    * If the endpoint source for the partner link was set to principal then the
    * addressing layer obtains the endpoint reference data from the principal's
    * provisioning data.
    *
    * Called indirectly from the engine (through the deployment descriptor) during
    * the queueing of any receive data.
    * @param aLink
    * @param aPrincipal
    */
    public void updateFromPrincipal(ISdlPartnerLink aLink, String aPrincipal) throws SdlBusinessProcessException;

    public boolean isServiceId();

    public void setServiceId(boolean isServiceId);
}
