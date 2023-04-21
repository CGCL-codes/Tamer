package de.tudresden.inf.rn.mobilis.android.xhunt.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import de.tudresden.inf.rn.mobilis.android.xhunt.packet.TargetIQ;

public class TargetIQProvider implements IQProvider {

    @Override
    public IQ parseIQ(XmlPullParser xpp) throws Exception {
        TargetIQ iq = new TargetIQ();
        int eventType = xpp.getEventType();
        boolean done = false;
        do {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("target")) {
                    iq.setTarget(xpp.nextText());
                } else if (xpp.getName().equals("finalDecision")) {
                    iq.setFinalDecision(Boolean.parseBoolean(xpp.nextText()));
                } else if (xpp.getName().equals("showMe")) {
                    iq.setShowMe(Boolean.parseBoolean(xpp.nextText()));
                } else if (xpp.getName().equals("jid")) {
                    iq.setJid(xpp.nextText());
                } else if (xpp.getName().equals("tickettype")) {
                    iq.setTicketType(xpp.nextText());
                } else if (xpp.getName().equals("round")) {
                    iq.setRound(Integer.parseInt(xpp.nextText()));
                }
                eventType = xpp.next();
            } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals(TargetIQ.elementName)) {
                done = true;
            } else {
                eventType = xpp.next();
            }
        } while (!done);
        return iq;
    }
}
