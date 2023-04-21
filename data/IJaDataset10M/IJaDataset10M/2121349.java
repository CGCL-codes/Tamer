package net.sf.jradius.webservice.handler;

import net.sf.jradius.dictionary.Attr_AuthType;
import net.sf.jradius.dictionary.Attr_EAPMessage;
import net.sf.jradius.dictionary.Attr_MessageAuthenticator;
import net.sf.jradius.dictionary.Attr_UserName;
import net.sf.jradius.handler.RadiusSessionHandler;
import net.sf.jradius.log.RadiusLog;
import net.sf.jradius.packet.AccessAccept;
import net.sf.jradius.packet.RadiusPacket;
import net.sf.jradius.packet.attribute.AttributeList;
import net.sf.jradius.server.JRadiusRequest;
import net.sf.jradius.session.JRadiusSession;
import net.sf.jradius.webservice.OTPProxyProcessor;
import net.sf.jradius.webservice.OTPProxyRequest;

/**
 * @author David Bird
 */
public class OTPProxyPostAuthHandler extends RadiusSessionHandler {

    public boolean handle(JRadiusRequest request) throws Exception {
        JRadiusSession session = (JRadiusSession) request.getSession();
        if (session == null) return noSessionFound(request);
        RadiusPacket req = request.getRequestPacket();
        RadiusPacket rep = request.getReplyPacket();
        AttributeList ci = request.getConfigItems();
        String username = (String) req.getAttributeValue(Attr_UserName.TYPE);
        OTPProxyRequest otpRequest = OTPProxyProcessor.get(username);
        if (otpRequest == null) return false;
        otpRequest.setAccessRequest(req);
        RadiusPacket resp = otpRequest.getAccessResponse();
        if (resp == null) {
            ci.add(new Attr_AuthType(Attr_AuthType.Reject));
            return true;
        }
        RadiusLog.debug("------------------------------------------------\n" + "OTP Proxy Response:\n" + resp.toString() + "------------------------------------------------\n");
        if (resp instanceof AccessAccept) {
            AttributeList attrs = resp.getAttributes();
            attrs.remove(Attr_EAPMessage.TYPE);
            attrs.remove(Attr_MessageAuthenticator.TYPE);
            rep.addAttributes(attrs);
            return true;
        }
        ci.add(new Attr_AuthType(Attr_AuthType.Reject));
        return true;
    }
}
