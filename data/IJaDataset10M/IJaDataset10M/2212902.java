package globali.smartcard;

import globali.jcVariabili;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class jcSmartCardFunzioni {

    public static void checkSchedaConnessa() {
        if (!jcVariabili.CARD.isConnessa) {
            jcVariabili.CARD.caricaLettori();
            jcVariabili.CARD.connettiScheda("*");
        }
    }

    public static void accediAllaSchedaPrivata() {
        if (!jcVariabili.CARD.isConnessa) {
            jcVariabili.CARD.caricaLettori();
            jcVariabili.CARD.connettiScheda("*");
            if (jcVariabili.CARD.isConnessa) {
                globali.smartcard.jdPIN mm = new globali.smartcard.jdPIN(null, true);
                do {
                    mm.stringaLabel = "PIN";
                    mm.setVisible(true);
                    if (mm.CODICE != null) jcVariabili.CARD.checkPINoPUK(0x01, mm.CODICE);
                } while (!jcVariabili.CARD.pinOK && jcVariabili.CARD.ottieniErrorCounterPIN() < 3 && !mm.annullato);
                if (jcVariabili.CARD.isBloccata) JOptionPane.showMessageDialog(null, "Scheda bloccata usa la funzione per lo sblocco con PUK !", "Errore", JOptionPane.ERROR_MESSAGE);
            } else JOptionPane.showMessageDialog(null, "Impossibile accedere alla scheda !", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
