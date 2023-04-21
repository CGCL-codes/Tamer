package de.tudresden.inf.rn.mobilis.android.xhunt.packet;

import org.jivesoftware.smack.packet.IQ;

public class GameOverIQ extends IQ {

    public static final String elementName = "query";

    public static final String namespace = "mobilisxhunt:iq:gameover";

    private String gameOverReason;

    public GameOverIQ() {
        super();
        this.setType(IQ.Type.SET);
    }

    public void setGameOverReason(String gameOverReason) {
        this.gameOverReason = gameOverReason;
    }

    public String getGameOverReason() {
        return gameOverReason;
    }

    @Override
    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<" + elementName + " xmlns=\"" + namespace + "\">\n");
        buf.append("<gameoverreason>").append(this.gameOverReason).append("</gameoverreason>\n");
        buf.append("</" + elementName + ">");
        return buf.toString();
    }
}
