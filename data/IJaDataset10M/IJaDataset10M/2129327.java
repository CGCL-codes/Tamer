package zeusmsn;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 *
 * @author Francesco Lopez
 */
public class Generic {

    public static String codebase;

    public static final int statoINDEFINITO = 0;

    public static final int statoINSERIMENTO = 1;

    public Generic() {
    }

    public static int notifica(String sTesto) {
        return notifica(sTesto, 2, "OK", null, null, null, 0);
    }

    public static int notifica(String sTesto, int iTipo) {
        return notifica(sTesto, iTipo, "OK", null, null, null, 0);
    }

    public static int notifica(String sTesto, int iTipo, String sOpzione1, String sOpzione2, String sOpzione3, String sOpzione4, int iOpzioneDefault) {
        String sTitolo = null;
        int iMessageType = 0;
        if (iTipo == 1) {
            sTitolo = " Messaggio";
            iMessageType = 1;
        } else if (iTipo == 2) {
            sTitolo = " Segnalazione";
            iMessageType = 2;
        } else {
            sTitolo = " Errore";
            iMessageType = 0;
        }
        int iNumOpzioni = 0;
        if (sOpzione1 != null) iNumOpzioni++;
        if (sOpzione2 != null) iNumOpzioni++;
        if (sOpzione3 != null) iNumOpzioni++;
        if (sOpzione4 != null) iNumOpzioni++;
        Object oOptions[] = new Object[iNumOpzioni];
        iNumOpzioni = -1;
        if (sOpzione1 != null) {
            iNumOpzioni++;
            oOptions[iNumOpzioni] = sOpzione1.trim();
        }
        if (sOpzione2 != null) {
            iNumOpzioni++;
            oOptions[iNumOpzioni] = sOpzione2.trim();
        }
        if (sOpzione3 != null) {
            iNumOpzioni++;
            oOptions[iNumOpzioni] = sOpzione3.trim();
        }
        if (sOpzione4 != null) {
            iNumOpzioni++;
            oOptions[iNumOpzioni] = sOpzione4.trim();
        }
        if (iOpzioneDefault > iNumOpzioni || iOpzioneDefault < 1) iOpzioneDefault = 1;
        sTesto = sTesto + "      ";
        int replay = JOptionPane.showOptionDialog(null, sTesto, sTitolo, 0, iMessageType, null, oOptions, oOptions[iOpzioneDefault - 1]);
        return replay + 1;
    }

    public static void centraGui(Window w) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = w.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        w.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    /** Mi restituisce un boleano se la stringa è vuota o meno
    */
    public static boolean strVuota(String stringa) {
        if (stringa == null) return true;
        return stringa.equals("") || stringa.length() == 0;
    }

    public static URL getURL(String nomefile) {
        URL myURL = null;
        if (!strVuota(nomefile)) try {
            myURL = new URL(codebase + nomefile);
        } catch (MalformedURLException e) {
        }
        return myURL;
    }
}
