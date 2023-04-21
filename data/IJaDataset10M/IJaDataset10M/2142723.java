package up5.mi.visio.SSBC;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipURI;
import org.apache.log4j.Logger;
import up5.mi.visio.SSBC.internals.HeadersTools;
import up5.mi.visio.SSBC.ssbcdb.Location;
import up5.mi.visio.SSBC.ssbcdb.RegistrationManager;

public class Invite {

    public static void doInvite(SipServletRequest req, SipFactory sf, ConfigurationBean configurationBean) {
        Logger logger = configurationBean.getLogger();
        logger.debug("++++++++++++++++++++++++++++++++++++ Entering doInvite ++++++++++++++++++++++++++++++++++++");
        RegistrationManager registrationManager = configurationBean.getRegistrationManager();
        String presenceProxyIp = configurationBean.getPresenceProxyIp();
        SipServletRequest newInvite = sf.createRequest(req, true);
        req.getSession().setAttribute(SSBC.OTHER_LEG, newInvite.getSession());
        newInvite.getSession().setAttribute(SSBC.OTHER_LEG, req.getSession());
        SipApplicationSession sas = req.getApplicationSession();
        sas.setAttribute(SSBC.ORIGINAL_INVITE, req);
        String sourceIp = req.getRemoteAddr();
        if (presenceProxyIp.equals(sourceIp)) {
            logger.debug("++++++++++++++++++ doInvite : invite coming from the presence proxy +++++++++++++++++++");
            String taggedContact = req.getRequestURI().toString();
            StringTokenizer st = new StringTokenizer(taggedContact, ":");
            if (st.countTokens() == 1) {
            } else {
                st.nextToken();
                taggedContact = st.nextToken();
            }
            logger.debug("------------- doInvite : taggedContact = " + taggedContact);
            Location location = registrationManager.getLocation(taggedContact);
            logger.debug("-------------- doInvite : location.getIp() = " + location.getIp() + ", location.getPort() = " + location.getPort());
            SipURI destUri = sf.createSipURI("", location.getIp());
            destUri.setPort(location.getPort());
            logger.debug("----------- doInvite : destUri = " + destUri);
            newInvite.setRequestURI(req.getTo().getURI());
            logger.debug("+++++++++++++++++ newInvite will be proxied to destination");
            newInvite.pushRoute(destUri);
            String sdp = null;
            String transformedSdp = null;
            req.getFrom().getURI().toString();
            String msbcSessionId = HeadersTools.getMsbcSessionId(req);
            logger.debug("msbcSessionId = " + msbcSessionId);
            try {
                byte[] tab = req.getRawContent();
                sdp = new String(tab);
                logger.debug("++++++++ original sdp = \n" + sdp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                transformedSdp = configurationBean.getProxy().transformSdp(sdp, msbcSessionId);
                logger.debug("++++++++ transformedSdp = \n" + transformedSdp);
            } catch (Exception e) {
                logger.debug("!!!!!!!!!!!!!! exception = ");
                logger.debug("exception lors de la transformation du premier SDP\n");
                e.printStackTrace();
            }
            try {
                newInvite.setContent(transformedSdp.getBytes(), "application/sdp");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            try {
                newInvite.send();
            } catch (IOException e) {
                logger.debug("--------------- Error while sending the new invite to the client\n");
                e.printStackTrace();
            }
        } else {
            logger.debug("++++++++++++++++++ doInvite : invite coming from a client +++++++++++++++++++");
            newInvite.setRequestURI(configurationBean.getPresenceProxyUri());
            try {
                newInvite.send();
            } catch (Exception e) {
                logger.debug("Error while sending newInvite to the PP : " + e.getMessage());
                e.printStackTrace();
            }
        }
        logger.debug("++++++++++++++++++++++++++++++++++++ Exiting doInvite ++++++++++++++++++++++++++++++++++++");
    }
}
