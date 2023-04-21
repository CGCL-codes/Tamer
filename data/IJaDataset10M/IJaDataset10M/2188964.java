package net.sf.jradius.handler.accounting;

import net.sf.jradius.dictionary.Attr_Class;
import net.sf.jradius.exception.RadiusException;
import net.sf.jradius.handler.RadiusSessionHandler;
import net.sf.jradius.packet.RadiusPacket;
import net.sf.jradius.server.JRadiusRequest;
import net.sf.jradius.session.JRadiusSession;

/**
 * Check for the Class Attribute set by PostAuthorizeClassHandler. If not found,
 * then add a log message stating the NAS did not support this attribute. After
 * this handler, the attribute is no longer needed and is deleted.
 *
 * @author David Bird
 * @see net.sf.jradius.handler.authorize.PostAuthorizeClassHandler
 */
public class AccountingClassHandler extends RadiusSessionHandler {

    public boolean handle(JRadiusRequest request) throws RadiusException {
        JRadiusSession session = request.getSession();
        if (session == null) return noSessionFound(request);
        RadiusPacket req = request.getRequestPacket();
        byte[] bClass = (byte[]) req.getAttributeValue(Attr_Class.TYPE);
        if (bClass != null) {
            String sClass = new String(bClass);
            if (sClass.startsWith(ClassPrefix)) {
                if (session.getRadiusClass() != null) {
                    req.overwriteAttribute(new Attr_Class(session.getRadiusClass()));
                } else {
                    req.removeAttribute(Attr_Class.TYPE);
                }
                return false;
            }
        }
        session.addLogMessage(request, "Accounting without Class Attribute");
        return false;
    }
}
