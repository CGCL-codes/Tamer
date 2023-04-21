package client.domein;

import core.net.UDPConnection;
import java.net.*;
import java.util.*;
import core.domein.*;

/**
 * Klasse die zich bezig houdt met het verwerken van chatten in een bepaald spel
 * @author Vanpoucke Sven
 *
 */
public class GameChatController {

    private UDPConnection connectie;

    private InetAddress tegenstander;

    private ArrayList<TextReceivedListener> listeners;

    /**
	 * Constructor
	 * @param connectie Verbinding waarop moet worden gechat
	 * @param tegenstander Address van tegenstander naar wie we chatten
	 */
    public GameChatController(UDPConnection connectie, InetAddress tegenstander) {
        this.connectie = connectie;
        this.tegenstander = tegenstander;
        listeners = new ArrayList<TextReceivedListener>();
    }

    /**
	 * Versturen van chatbericht naar de tegenstander
	 * @param text chatbericht
	 */
    public void sendText(String text) {
        connectie.sendPacket(text, tegenstander);
    }

    /**
	 * Verwerken van ontvangen chatberichten
	 * @param text ontvangen chatbericht
	 */
    public void textReceived(String text) {
        notifyTextReceived(text);
    }

    /**
	 * listeners (bv gui) verwittigen dat er een chatbericht is binnengekomen
	 * @param text
	 */
    private void notifyTextReceived(String text) {
        for (TextReceivedListener listener : listeners) {
            listener.textReceived(text);
        }
    }

    /**
	 * Listener toevoegen
	 * @param listener TextReceivedListener toevoegen
	 */
    public void addTextReceivedListener(TextReceivedListener listener) {
        listeners.add(listener);
    }
}
