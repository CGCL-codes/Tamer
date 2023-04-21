package de.tudresden.inf.rn.mobilis.android.xhunt.packet;

import java.util.HashMap;
import org.jivesoftware.smack.packet.IQ;
import de.tudresden.inf.rn.mobilis.android.xhunt.TicketManagement;

public class StartRoundIQ extends IQ {

    public static final String elementName = "query";

    public static final String namespace = "mobilisxhunt:iq:startround";

    private int round;

    private HashMap<String, TicketManagement> players;

    public StartRoundIQ() {
        super();
        this.setType(IQ.Type.SET);
        players = new HashMap<String, TicketManagement>();
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void setPlayers(HashMap<String, TicketManagement> players) {
        this.players = players;
    }

    public HashMap<String, TicketManagement> getPlayers() {
        return players;
    }

    public void addPlayer(String jid, TicketManagement tm) {
        players.put(jid, tm);
    }

    @Override
    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<" + elementName + " xmlns=\"" + namespace + "\">\n");
        buf.append("<round>").append(this.round).append("</round>\n");
        for (String jid : players.keySet()) {
            buf.append("<Player jid=\"" + jid + "\" busTicket=\"" + players.get(jid).getBusTicketNumber() + "\" tramTicket=\"" + players.get(jid).getTramTicketNumber() + "\" railwayTicket=\"" + players.get(jid).getRailwayTicketNumber() + "\" blackTicket=\"" + players.get(jid).getBlackTicketNumber() + "\" doubleTicket=\"" + players.get(jid).getDoubleTicketNumber() + "\" waitingTicket=\"" + players.get(jid).getWaitingTicketNumber() + "\"></Player>\n");
        }
        buf.append("</" + elementName + ">");
        return buf.toString();
    }
}
