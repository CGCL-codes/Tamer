package de.tudresden.inf.rn.mobilis.jclient.xhunt.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import de.tudresden.inf.rn.mobilis.jclient.xhunt.packet.PlayerExitGameIQ;

public class PlayerExitGameIQProvider implements IQProvider {

    @Override
    public IQ parseIQ(XmlPullParser xpp) throws Exception {
        PlayerExitGameIQ iq = new PlayerExitGameIQ();
        int eventType = xpp.getEventType();
        boolean done = false;
        do {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("Player")) {
                    iq.setJid(xpp.nextText());
                }
                eventType = xpp.next();
            } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals(PlayerExitGameIQ.elementName)) {
                done = true;
            } else {
                eventType = xpp.next();
            }
        } while (!done);
        return iq;
    }
}
