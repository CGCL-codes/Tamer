package org.imsoss.scscf;

import java.text.ParseException;
import java.util.ListIterator;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.InvalidArgumentException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.HeaderFactory;
import javax.sip.header.RouteHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerFacility;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import org.apache.log4j.Logger;
import org.mobicents.slee.resource.sip.SipResourceAdaptorSbbInterface;
import org.mobicents.slee.services.sip.common.SipSendErrorResponseException;
import org.mobicents.slee.services.sip.location.LocationSbbLocalObject;
import org.mobicents.slee.services.sip.location.LocationServiceException;
import org.mobicents.slee.services.sip.location.RegistrationBinding;
import org.imsoss.sbb.CommonSbb;
import org.imsoss.util.ImsConfiguration;

public abstract class ServingSbb extends CommonSbb {

    public void onServiceStarted(javax.slee.serviceactivity.ServiceStartedEvent serviceEvent, ActivityContextInterface aci) {
        logger.debug("Starting S-CSCF node of the imsoss ;) sexy ");
        System.out.println("Starting S-CSCF node of the imsoss ;) sexy ");
    }

    public void onRegisterEventDummy(javax.sip.RequestEvent event, ActivityContextInterface aci) {
        syslog("Check this in S-CSCF");
    }

    /**
	 * Event handler for the SIP MESSAGE from the UA
	 * @param event
	 * @param aci
	 */
    public void onRegisterEvent(javax.sip.RequestEvent event, ActivityContextInterface aci) {
        Request request = event.getRequest();
        boolean icscfFound = false;
        boolean scscfFound = false;
        ListIterator<ViaHeader> iter = request.getHeaders(ViaHeader.NAME);
        while (iter.hasNext()) {
            ViaHeader viaHeader = iter.next();
            if (viaHeader.getPort() == ImsConfiguration.icscfPort) {
                icscfFound = true;
            }
            if (viaHeader.getPort() == ImsConfiguration.scscfPort) {
                scscfFound = true;
            }
        }
        if (icscfFound && !scscfFound) {
            System.out.println("[5]S-CSCF request as \n" + request);
            System.out.println("[6]S-CSCF processing is required, this needs to talk to the SIP-AS !!!!! ");
            ServerTransaction serverTransaction = event.getServerTransaction();
            try {
                serverTransaction.sendResponse(messageFactory.createResponse(Response.OK, request));
            } catch (ParseException exp) {
                System.out.println("Error cropping in ServingSbb " + exp);
            } catch (SipException exp) {
                System.out.print("Error cropping in ServingSbb " + exp);
            } catch (InvalidArgumentException exp) {
                System.out.print("Error cropping in ServingSbb " + exp);
            }
        } else {
            System.out.println("S-CSCF processing is not needed here");
            return;
        }
    }

    /**
	 *  Initialize the component
	 */
    public void setSbbContext(SbbContext context) {
        this.sbbContext = context;
        try {
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
            timerFacility = (TimerFacility) myEnv.lookup("slee/facilities/timer");
            nullACIFactory = (NullActivityContextInterfaceFactory) myEnv.lookup("slee/nullactivity/activitycontextinterfacefactory");
            nullActivityFactory = (NullActivityFactory) myEnv.lookup("slee/nullactivity/factory");
            fp = (SipResourceAdaptorSbbInterface) myEnv.lookup("slee/resources/jainsip/1.2/provider");
            provider = fp.getSipProvider();
            addressFactory = fp.getAddressFactory();
            headerFactory = fp.getHeaderFactory();
            messageFactory = fp.getMessageFactory();
        } catch (Exception ne) {
            logger.error("Failed to set sbb context", ne);
        }
    }

    public abstract org.imsoss.scscf.ServingSbbActivityContextInterface asSbbActivityContextInterface(ActivityContextInterface aci);

    private SbbContext sbbContext;

    private MessageFactory messageFactory;

    private SipProvider provider;

    private SipResourceAdaptorSbbInterface fp;

    private TimerFacility timerFacility;

    private NullActivityContextInterfaceFactory nullACIFactory;

    private NullActivityFactory nullActivityFactory;

    private AddressFactory addressFactory;

    private HeaderFactory headerFactory;

    private static final Logger logger = Logger.getLogger(ServingSbb.class);
}
