package de.rockon.fuzzy.controller.util;

/**
 * Zum speichern von Log-Nachrichten
 * 
 */
public class LogMessageClass {

    private String text;

    private String type;

    private int msgNumber;

    /**
	 * @param Nachrichtentyp
	 * @param Nachricht
	 */
    public LogMessageClass(String type, String message) {
        this.type = type;
        text = message;
    }

    /**
	 * @return - Nachrichtennummer
	 */
    public int getMsgNumber() {
        return msgNumber;
    }

    /**
	 * @return Nachricht
	 */
    public String getText() {
        return text;
    }

    /**
	 * @return Nachrichtentyp
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param -
	 *            Nachrichtennummer
	 */
    public void setMsgNumber(int msgNumber) {
        this.msgNumber = msgNumber;
    }

    @Override
    public String toString() {
        return type + " " + text;
    }
}
