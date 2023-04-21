package de.tudresden.inf.rn.mobilis.android.xhunt.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import de.tudresden.inf.rn.mobilis.android.xhunt.packet.RoundStatusIQ;

public class RoundStatusIQProvider implements IQProvider {

    @Override
    public IQ parseIQ(XmlPullParser xpp) throws Exception {
        RoundStatusIQ iq = new RoundStatusIQ();
        int eventType = xpp.getEventType();
        boolean done = false;
        do {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("round")) {
                    iq.setRound(Integer.parseInt(xpp.nextText()));
                } else if (xpp.getName().equals("Player")) {
                    iq.addPlayer(xpp.getAttributeValue(0), Boolean.parseBoolean(xpp.getAttributeValue(1)));
                }
                eventType = xpp.next();
            } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals(RoundStatusIQ.elementName)) {
                done = true;
            } else {
                eventType = xpp.next();
            }
        } while (!done);
        return iq;
    }
}
