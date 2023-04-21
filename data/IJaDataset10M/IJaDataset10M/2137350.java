package de.tudresden.inf.rn.mobilis.android.xhunt.packet;

import java.util.ArrayList;
import org.jivesoftware.smack.packet.IQ;
import de.tudresden.inf.rn.mobilis.android.xhunt.XHuntPlayer;

public class StatusGameIQ extends IQ {

    public static final String elementName = "query";

    public static final String namespace = "mobilisxhunt:iq:statusgame";

    private int round;

    private String gameId;

    private String chatId, chatPassword;

    private ArrayList<XHuntPlayer> players;

    public StatusGameIQ() {
        super();
        this.setType(IQ.Type.SET);
        players = new ArrayList<XHuntPlayer>();
    }

    public void setGameId(String gameid) {
        this.gameId = gameid;
    }

    public String getGameId() {
        return gameId;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatPassword(String chatPassword) {
        this.chatPassword = chatPassword;
    }

    public String getChatPassword() {
        return chatPassword;
    }

    public void setPlayers(ArrayList<XHuntPlayer> players) {
        this.players = players;
    }

    public ArrayList<XHuntPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(XHuntPlayer player) {
        players.add(player);
    }

    @Override
    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<" + elementName + " xmlns=\"" + namespace + "\">\n");
        buf.append("<gameid>").append(this.gameId).append("</gameid>\n");
        buf.append("<round>").append(this.round).append("</round>\n");
        buf.append("<chatid>").append(this.chatId).append("</chatid>\n");
        buf.append("<chatpassword>").append(this.chatPassword).append("</chatpassword>\n");
        for (XHuntPlayer p : players) {
            buf.append("<Player jid=\"" + p.getJid() + "\" name=\"" + p.getName() + "\" moderator=\"" + p.getModerator() + "\" mrx=\"" + p.getMrX() + "\" ready=\"" + p.getReady() + "\"></Player>\n");
        }
        buf.append("</" + elementName + ">");
        return buf.toString();
    }
}
