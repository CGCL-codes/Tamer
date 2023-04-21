package de.tudresden.inf.rn.mobilis.xmpp.beans.xhunt;

import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class StartRoundBean extends XMPPBean {

    private static final long serialVersionUID = 6020507205519739728L;

    public static final String NAMESPACE = "mobilisxhunt:iq:startround";

    public static final String CHILD_ELEMENT = "query";

    public int Round = -1;

    public boolean ShowMrX = false;

    public HashMap<Integer, Integer> Tickets = new HashMap<Integer, Integer>();

    public StartRoundBean() {
    }

    public StartRoundBean(int round, boolean showMrX, HashMap<Integer, Integer> tickets) {
        this.Round = round;
        this.ShowMrX = showMrX;
        this.Tickets = tickets;
    }

    @Override
    public void fromXML(XmlPullParser parser) throws Exception {
        boolean done = false;
        int ticketId = -1;
        int ticketAmount = -1;
        do {
            switch(parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    String tagName = parser.getName();
                    if (tagName.equals(CHILD_ELEMENT)) {
                        parser.next();
                    } else if (tagName.equals(XHuntElements.CHILD_ELEMENT_GAME_ROUND)) {
                        this.Round = Integer.valueOf(parser.nextText()).intValue();
                    } else if (tagName.equals(XHuntElements.CHILD_ELEMENT_GAME_SHOWMRX)) {
                        this.ShowMrX = Boolean.valueOf(parser.nextText()).booleanValue();
                    } else if (tagName.equals(XHuntElements.CHILD_ELEMENT_TICKET_ID)) {
                        ticketId = Integer.valueOf(parser.nextText()).intValue();
                    } else if (tagName.equals(XHuntElements.CHILD_ELEMENT_TICKET_AMOUNT)) {
                        ticketAmount = Integer.valueOf(parser.nextText()).intValue();
                    } else if (tagName.equals(XHuntElements.CHILD_ELEMENT_ERROR)) {
                        parser = parseErrorAttributes(parser);
                    } else parser.next();
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals(CHILD_ELEMENT)) {
                        done = true;
                    } else if (parser.getName().equals(XHuntElements.CHILD_ELEMENT_TICKET)) {
                        this.Tickets.put(ticketId, ticketAmount);
                        ticketId = -1;
                        ticketAmount = -1;
                        parser.next();
                    } else parser.next();
                    break;
                case XmlPullParser.END_DOCUMENT:
                    done = true;
                    break;
                default:
                    parser.next();
            }
        } while (!done);
    }

    @Override
    public String getChildElement() {
        return CHILD_ELEMENT;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public XMPPBean clone() {
        StartRoundBean clone = this.Round > -1 ? new StartRoundBean(this.Round, this.ShowMrX, this.Tickets) : new StartRoundBean();
        return (StartRoundBean) cloneBasicAttributes(clone);
    }

    @Override
    public String payloadToXML() {
        StringBuilder sb = new StringBuilder();
        if (this.Round > -1) {
            sb.append("<" + XHuntElements.CHILD_ELEMENT_GAME_ROUND + ">").append(this.Round).append("</" + XHuntElements.CHILD_ELEMENT_GAME_ROUND + ">");
            sb.append("<" + XHuntElements.CHILD_ELEMENT_GAME_SHOWMRX + ">").append(this.ShowMrX).append("</" + XHuntElements.CHILD_ELEMENT_GAME_SHOWMRX + ">");
            for (Map.Entry<Integer, Integer> elem : this.Tickets.entrySet()) {
                sb.append("<" + XHuntElements.CHILD_ELEMENT_TICKET + ">");
                sb.append("<" + XHuntElements.CHILD_ELEMENT_TICKET_ID + ">").append(elem.getKey()).append("</" + XHuntElements.CHILD_ELEMENT_TICKET_ID + ">");
                sb.append("<" + XHuntElements.CHILD_ELEMENT_TICKET_AMOUNT + ">").append(elem.getValue()).append("</" + XHuntElements.CHILD_ELEMENT_TICKET_AMOUNT + ">");
                sb.append("</" + XHuntElements.CHILD_ELEMENT_TICKET + ">");
            }
        }
        sb = appendErrorPayload(sb);
        return sb.toString();
    }
}
