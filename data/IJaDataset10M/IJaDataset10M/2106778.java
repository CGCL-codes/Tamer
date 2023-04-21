package net.sourceforge.lascreen.networking;

import java.io.Serializable;
import net.sourceforge.lascreen.logic.Graphic;

/**
 * Diese Klasse dient zur vereinheitlichten Kommunikation zwichen Server und Client
 * <br>
 */
public class Communication implements Serializable {

    Graphic graphic;

    String requestType;

    int graphicCount = -1;

    boolean succeded;

    public Communication() {
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getGraphicCount() {
        return graphicCount;
    }

    public void setGraphicCount(int graphicCount) {
        this.graphicCount = graphicCount;
    }

    public boolean isSucceded() {
        return succeded;
    }

    public void setSucceded(boolean succeded) {
        this.succeded = succeded;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }
}
