package geisler.projekt.editor.model;

import geisler.projekt.game.constants.EnumNpcStatus;
import java.util.List;
import java.util.Map;

/**
 *
 * Ein Objekt welches die Informationen des AttributeDialogNpc
 * aufnimmt und speichert
 * 
 * @author Geislern
 */
public class DialogModelNPC {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgTitel() {
        return msgTitel;
    }

    public void setMsgTitel(String msgTitel) {
        this.msgTitel = msgTitel;
    }

    public Integer getNpcID() {
        return npcID;
    }

    public void setNpcID(Integer npcID) {
        this.npcID = npcID;
    }

    public String getNpcName() {
        return npcName;
    }

    public void setNpcName(String npcName) {
        this.npcName = npcName;
    }

    public Integer getNpcSpriteNr() {
        return npcSpriteNr;
    }

    public void setNpcSpriteNr(Integer npcSpriteNr) {
        this.npcSpriteNr = npcSpriteNr;
    }

    public EnumNpcStatus getNpcTyp() {
        return npcTyp;
    }

    public void setNpcTyp(EnumNpcStatus npcTyp) {
        this.npcTyp = npcTyp;
    }

    public Map<String, String> getMapMessages() {
        return mapMessages;
    }

    public void setMapMessages(Map<String, String> mapMessages) {
        this.mapMessages = mapMessages;
    }

    private Integer npcID;

    private Integer npcSpriteNr;

    private String npcName;

    private EnumNpcStatus npcTyp;

    private Map<String, String> mapMessages;

    private String msgTitel;

    private String message;
}
