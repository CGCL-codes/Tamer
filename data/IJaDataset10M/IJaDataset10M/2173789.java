package edu.tfh.s2.ehkbot.steuerung.schnittstellen;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import edu.tfh.s2.ehkbot.inventar.Einkaufsauftrag;

/**
 * @author s2zehn Die Kasse AuftragsQuelle ist ein Wrapper um die Netzwerk
 *         klasse der EHKBot bekommt von hier seine Einkaufsaufträge
 * 
 */
public class AuftragsQuelle {

    /**
	 * Der port auf den AuftragsQuelle lauscht
	 */
    private static final String port = "AuftragsQuelle";

    /**
	 * Der post auf den AuftragsQuelle die bestätigung sendet
	 */
    private static final String portACK = "AuftragsQuelleACK";

    /**
	 * Liefert einen Einkfausauftrag fals einer vorliegt.
	 * 
	 * @return Liefert null wenn kein Einkaufsauftrag vorliegt.
	 * @throws IOException
	 *             Wird geworfen wenn das Netzwerk ein Defekt hat.
	 * @throws ClassNotFoundException
	 *             Wird geworden wenn jemand etwas falsches an den port gesendet
	 *             hat.
	 */
    public Einkaufsauftrag getAuftrag() throws IOException, ClassNotFoundException {
        if (!istAuftragDa()) return null;
        ByteArrayInputStream strr = new ByteArrayInputStream(Netzwerk.getInstance().getNachricht(port));
        ObjectInputStream objstream = new ObjectInputStream(strr);
        Einkaufsauftrag empfangenerAuftrag = (Einkaufsauftrag) objstream.readObject();
        objstream.close();
        sendeBestaetigung(empfangenerAuftrag);
        return empfangenerAuftrag;
    }

    /**
	 * Prüft ob ein Einkaufsauftrag vorliegt auf den Eingangs port.
	 * 
	 * @return Ist true wenn ein Auftrag vorliegt.
	 */
    public boolean istAuftragDa() {
        return Netzwerk.getInstance().isNachrichtDa(port);
    }

    /**
	 * Bestätigt den Empfang des Einkaufauftrag Auftrag, an den portACK.
	 * 
	 * @param auftrag
	 *            Der zu Bestätigende Auftrag.
	 */
    private void sendeBestaetigung(Einkaufsauftrag auftrag) {
        String nachricht = String.format("<ack><aufragsID>%d</aufragsID></ack>", auftrag.getAuftragID());
        Netzwerk.getInstance().sendeNachricht(nachricht.getBytes(), portACK);
    }
}
