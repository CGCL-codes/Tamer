package mediathek.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.event.EventListenerList;
import mediathek.Konstanten;
import mediathek.beobachter.Listener;
import mediathek.daten.Daten;

public class GetUrl {

    static EventListenerList listeners = new EventListenerList();

    public static boolean stop = false;

    Daten daten;

    String user_agent = "";

    int timeout = 30000;

    private long wartenBasis = Konstanten.WARTEN_BASIS_URL;

    public GetUrl(Daten ddaten) {
        daten = ddaten;
    }

    public GetUrl(Daten ddaten, int ttimeout, long wwartenBasis) {
        daten = ddaten;
        timeout = ttimeout;
        wartenBasis = wwartenBasis;
    }

    public GetUrl(Daten ddaten, long wwartenBasis) {
        daten = ddaten;
        wartenBasis = wwartenBasis;
    }

    public StringBuffer getUri_Utf(String sender, String addr, StringBuffer seite, String meldung) {
        return getUri(sender, addr, seite, Konstanten.KODIERUNG_UTF, timeout, meldung);
    }

    public StringBuffer getUri_Iso(String sender, String addr, StringBuffer seite, String meldung) {
        return getUri(sender, addr, seite, Konstanten.KODIERUNG_ISO15, timeout, meldung);
    }

    public static void addAdListener(Listener listener) {
        listeners.add(Listener.class, listener);
    }

    private synchronized void gibBescheid() {
        for (Listener l : listeners.getListeners(Listener.class)) {
            l.progress();
        }
    }

    private synchronized StringBuffer getUri(String sender, String addr, StringBuffer seite, String kodierung, int timeout, String meldung) {
        char[] zeichen = new char[1];
        try {
            long w = wartenBasis * Integer.parseInt(daten.system[Konstanten.SYSTEM_WARTEN_NR]);
            this.wait(w);
        } catch (Exception ex) {
            System.err.println("GetUrl.getUri: " + ex.getMessage());
        }
        daten.filmeLaden.incSeitenZaehler(sender);
        gibBescheid();
        user_agent = daten.system[Konstanten.SYSTEM_USER_AGENT_NR];
        seite.setLength(0);
        URLConnection conn = null;
        InputStream in = null;
        InputStreamReader inReader = null;
        try {
            URL url = new URL(addr);
            conn = url.openConnection();
            conn.setRequestProperty("User-Agent", user_agent);
            if (timeout > 0) {
                conn.setReadTimeout(timeout);
                conn.setConnectTimeout(timeout);
            }
            in = conn.getInputStream();
            inReader = new InputStreamReader(in, kodierung);
            while (!stop && inReader.read(zeichen) != -1) {
                seite.append(zeichen);
            }
        } catch (Exception ex) {
            if (!meldung.equals("")) {
                daten.fehler.fehlerMeldung("GetUrl.getUri für: ", meldung);
            }
            daten.fehler.fehlerMeldung(ex, "GetUrl.getUri für: " + addr);
        } finally {
            try {
                if (in != null) {
                    inReader.close();
                }
            } catch (IOException ex) {
            }
        }
        return seite;
    }
}
