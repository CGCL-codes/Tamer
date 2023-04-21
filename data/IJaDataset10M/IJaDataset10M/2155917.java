package client;

import javax.swing.JTextArea;
import WebService.GameState;
import WebService.WSRisiko;

/**
 * Diese Klasse implementiert die Chatfunktion
 * 
 * @author RoCMe
 */
public class Chat {

    /**
	 * gameID
	 */
    private int gameID;

    /** Anzahl der empfangenen Nachrichten */
    private int lastMsgPos = -1;

    /** Verbindung zum Senden */
    private WSRisiko webService;

    /** hier Nachrichten hinchreiben */
    private JTextArea history;

    /** Der Spielername */
    private String playerName;

    /**
	 * Konstruktor
	 * 
	 * @param gameID
	 *            ID des Spiels
	 * @param playerName
	 *            Der SpielerName
	 * @param webService
	 *            Verbindung, um Nachrichten senden zu können
	 * @param state
	 *            Der aktuelle Status - samt Chatlog
	 * @param history
	 *            hier soll Text angezeigt werden
	 */
    public Chat(int gameID, String playerName, WSRisiko webService, GameState state, JTextArea history) {
        super();
        this.webService = webService;
        this.playerName = playerName;
        this.history = history;
        this.gameID = gameID;
    }

    /**
	 * Chat-Nachrichten senden
	 * 
	 * @param msg
	 *            Die Nachricht
	 */
    public void send(String msg) {
        try {
            String message = "[" + playerName + "] " + msg;
            webService.sendMessage(gameID, message);
        } catch (Exception e) {
            System.out.println("Fehler bei Service-Kommunikation");
        }
    }

    /**
	 * Diese Funktionkontrolliert, ob neue Nachrichten vorliegen, und zeigt sie
	 * an Der gameState enthält ein String Array der Länge 25. ist das voll,
	 * werden die ältesten Nachrichten überschrieben. lastmsgPos zeigt lokal 
	 * auf die letzte empfangene nachricht, im gameState auf die aktuellste
	 * 
	 * @param state
	 *            aktueller Status
	 */
    public void update(GameState state) {
        int tmp = state.getLastMsgPos();
        String[] messages = state.getMessages();
        while (lastMsgPos != tmp) {
            lastMsgPos = ++lastMsgPos % 25;
            history.append(messages[lastMsgPos]);
        }
    }
}
