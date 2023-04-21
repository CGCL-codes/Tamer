package base.net;

import java.net.DatagramPacket;

/**
 * Interface für Klassen, deren Instanzen über eingehende Multicast-Nachrichten informiert werden möchten.
 */
public interface ReceiveListener {

    /**
     * Wird aufgerufen, wenn eine Multicast-Nachricht eingegangen ist.
     *
     * @param packet Paket mit der Nachricht
     */
    void received(DatagramPacket packet);
}
