package mediathek.io.starter;

import mediathek.Funktionen;
import mediathek.TModel;
import mediathek.Konstanten;
import mediathek.HinweisKeineAuswahl;
import mediathek.daten.Daten;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import mediathek.daten.DatenAbo;
import mediathek.filme.DatenFilm;
import mediathek.daten.DatenPgruppe;
import mediathek.daten.DatenProg;
import mediathek.gui.dialoge.DialogZielDatei;

public class StarterClass {

    private Daten daten;

    private boolean allesStop = false;

    private ListeStarts listeStarts;

    private Starten starten = null;

    private EventListenerList listeners = new EventListenerList();

    /**
     * Neue Starter-Klasse inizialisieren
     * @param d 
     */
    public StarterClass(Daten d) {
        daten = d;
        listeners = new EventListenerList();
        listeStarts = new ListeStarts(daten);
        startenThreadStarten();
    }

    /**
     *
     * @param url
     * @param open
     * @param ersterFilm
     * @param listener
     * @return
     */
    public synchronized Starts urlStarten(String url, int open, DatenFilm ersterFilm) {
        Starts ret = null;
        String befehlsString = "";
        String zielDateiname = "";
        String zielPfad = "";
        String zielPfadDatei = "";
        int art = 0;
        if (!url.equals("")) {
            DatenPgruppe gruppe = daten.listePgruppeButton.get(open);
            DatenProg prog = gruppe.getProgUrl(url);
            if (prog != null) {
                befehlsString = prog.getBegehl();
                Funktionen.checkFlash(daten, url, befehlsString);
                zielDateiname = gruppe.getZielDateiname();
                zielPfad = gruppe.getZielPfad();
                art = gruppe.checkDownloadDirekt(daten, url);
                if (!zielDateiname.equals("")) {
                    if (zielDateiname.contains("%p")) {
                        String name = Funktionen.getHeute_yyyyMMdd() + "_" + ersterFilm.arr[Konstanten.FILM_THEMA_NR] + "-" + ersterFilm.arr[Konstanten.FILM_TITEL_NR] + ".mp4";
                        name = Funktionen.replaceLeerDateiname(name, true);
                        DialogZielDatei dialog = new DialogZielDatei(null, true, daten, gruppe.getZielPfad(), Funktionen.getDateiName(daten, name));
                        dialog.setVisible(true);
                        if (!dialog.ok) {
                            return null;
                        }
                        zielDateiname = zielDateiname.replace("%p", dialog.zielDateiname);
                        zielPfad = dialog.zielPfad;
                    } else if (zielDateiname.contains("%n")) {
                        String str;
                        String name = Funktionen.getHeute_yyyyMMdd() + "_" + ersterFilm.arr[Konstanten.FILM_THEMA_NR] + "-" + ersterFilm.arr[Konstanten.FILM_TITEL_NR] + ".mp4";
                        name = Funktionen.replaceLeerDateiname(name, true);
                        str = JOptionPane.showInputDialog("Dateiname eingeben", name);
                        if (str == null) {
                            return null;
                        }
                        zielDateiname = zielDateiname.replace("%n", Funktionen.replaceLeerDateiname(str, true));
                    }
                    if (zielPfad.equals("")) {
                        JOptionPane.showMessageDialog(null, "Zielpfad angeben!", "Pfad leer", JOptionPane.INFORMATION_MESSAGE);
                    }
                    zielPfadDatei = ersterFilm.dateiNamenBauen(zielPfad, zielDateiname);
                    befehlsString = Funktionen.getBefhelsString(befehlsString, zielPfadDatei, url, ersterFilm);
                } else {
                    befehlsString = befehlsString.replace("%f", url);
                }
                Starts s = new Starts(Starts.QUELLE_BUTTON, ersterFilm, art, befehlsString, Boolean.parseBoolean(prog.arr[Konstanten.PROGRAMM_RESTART_NR]));
                ret = s;
                if (s.art == Starts.ART_PROGRAMM) {
                    StartenProgramm zdfStarten = new StartenProgramm(s);
                    new Thread(zdfStarten).start();
                    addStarts(s);
                } else {
                    StartenDonwnload podderStart = new StartenDonwnload(s);
                    new Thread(podderStart).start();
                    addStarts(s);
                }
            }
        } else {
            new HinweisKeineAuswahl().zeigen();
        }
        return ret;
    }

    public synchronized LinkedList<Starts> getStarts(int quelle) {
        LinkedList<Starts> ret = new LinkedList<Starts>();
        Iterator<Starts> it = listeStarts.getIt();
        while (it.hasNext()) {
            Starts s = it.next();
            if (s.quelle == quelle || quelle == Starts.QUELLE_ALLE) {
                ret.add(s);
            }
        }
        return ret;
    }

    /**
     * Liefert ein TModell mit den aktuelen Starts
     * @return
     */
    public synchronized TModel getStarterModell(TModel model) {
        return listeStarts.getModel(model);
    }

    /**
     * Listener hinzufügen, informiert über Änderungen am Status der Downloads
     * @param listener 
     */
    public void addListener(StartListener listener) {
        listeners.add(StartListener.class, listener);
    }

    /**Eine liste mit Downloads wird an die Auftragsliste angehängt
     * @param starts 
     */
    public synchronized void addListe(LinkedList<Starts> starts) {
        allesStop = false;
        if (starts != null) {
            ListIterator<Starts> it = starts.listIterator(0);
            while (it.hasNext()) {
                Starts s = it.next();
                addStarts(s);
            }
        }
    }

    public synchronized void addStarts(Starts starts) {
        allesStop = false;
        if (starts != null) {
            if (!listeStarts.contain(starts)) {
                listeStarts.add(starts);
            }
        }
        notifyStartEvent();
    }

    /**Gibt den Status eines Downloads zurück*
     * ziel: ist die Zieldatei
     * @param url
     * @return Status des Downloads
     */
    public synchronized int getState(String url) {
        int ret = 0;
        Iterator<Starts> it = listeStarts.getIt();
        while (it.hasNext()) {
            Starts s = it.next();
            if (s.film.arr[Konstanten.FILM_URL_NR].equals(url)) {
                ret = s.status;
                break;
            }
        }
        return ret;
    }

    public synchronized Starts getStart(String url) {
        Starts ret = null;
        Iterator<Starts> it = listeStarts.getIt();
        while (it.hasNext()) {
            Starts s = it.next();
            if (s.film.arr[Konstanten.FILM_URL_NR].equals(url)) {
                ret = s;
                break;
            }
        }
        return ret;
    }

    public synchronized void delStart(String url) {
        listeStarts.delStart(url);
    }

    /**Alle erledigten Downloads werden aus der Liste gelöscht,
     * StartEvent wird ausgelöst
     * @param startArt 
     */
    public synchronized void aufraeumen(int quelle) {
        listeStarts.aufraeumen(quelle);
        notifyStartEvent();
    }

    /**Alle Downloads werden abgebrochen*/
    public synchronized void abbrechen() {
        allesStop = true;
        listeStarts.delAlle();
        notifyStartEvent();
    }

    /**Alle Downloads die nicht laufen, löschen: Es wird nach dem laufenden Download abgebrochen*/
    public synchronized void abbrechenNachFilm() {
        listeStarts.delRest();
        notifyStartEvent();
    }

    public synchronized void filmLoeschen(String url) {
        listeStarts.delStart(url);
        notifyStartEvent();
    }

    private void notifyStartEvent() {
        StartEvent event;
        int down = 0;
        int progress = 0;
        int max = listeStarts.getmax();
        Iterator<Starts> it = listeStarts.getIt();
        while (it.hasNext()) {
            Starts s = it.next();
            if (s.status == Starts.STATUS_RUN) {
                ++down;
            }
            if (s.status >= Starts.STATUS_FERTIG) {
                ++progress;
            }
        }
        event = new StartEvent(this, down, progress, max, allesStop);
        for (StartListener l : listeners.getListeners(StartListener.class)) {
            l.starter(event);
        }
    }

    private Starts getListe() {
        Iterator<Starts> it;
        Starts ret = null;
        if (allesStop) {
            it = listeStarts.getIt();
            while (it.hasNext()) {
                Starts s = it.next();
                if (s.status < Starts.STATUS_FERTIG) {
                    it.remove();
                }
            }
        } else {
            if (listeStarts.size() >= 0 && listeStarts.getDown() < Integer.parseInt(daten.system[Konstanten.SYSTEM_MAX_DOWNLOAD_NR])) {
                Starts s = naechsterStart();
                if (s != null) {
                    if (s.status == Starts.STATUS_INIT) {
                        ret = s;
                    }
                }
            }
        }
        return ret;
    }

    private void buttonStartsPutzen() {
        boolean habsGetan = false;
        Iterator<Starts> it = listeStarts.getIt();
        while (it.hasNext()) {
            Starts s = it.next();
            if (s.quelle == Starts.QUELLE_BUTTON) {
                if (s.status != Starts.STATUS_RUN) {
                    it.remove();
                    habsGetan = true;
                }
            }
        }
        if (habsGetan) {
            notifyStartEvent();
        }
    }

    private Starts naechsterStart() {
        Starts s = null;
        Iterator<Starts> it = listeStarts.getIt();
        while (it.hasNext()) {
            s = it.next();
            if (s.status == Starts.STATUS_INIT) {
                if (!maxSenderLaufen(s, 1)) {
                    return s;
                }
            }
        }
        if (Konstanten.MAX_SENDER_FILME_LADEN == 1) {
            return null;
        }
        it = listeStarts.getIt();
        while (it.hasNext()) {
            s = it.next();
            if (s.status == Starts.STATUS_INIT) {
                if (!maxSenderLaufen(s, Konstanten.MAX_SENDER_FILME_LADEN)) {
                    return s;
                }
            }
        }
        return null;
    }

    private boolean maxSenderLaufen(Starts s, int max) {
        try {
            int counter = 0;
            Starts start = null;
            String host = getHost(s);
            Iterator<Starts> it = listeStarts.getIt();
            while (it.hasNext()) {
                start = it.next();
                if (start.status == Starts.STATUS_RUN && getHost(start).equalsIgnoreCase(host)) {
                    counter++;
                    if (counter >= max) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    private String getHost(Starts s) {
        String host = "";
        try {
            try {
                String uurl = s.film.getUrlOrg();
                if (uurl.startsWith(Konstanten.RTMP_PRTOKOLL)) {
                    uurl = uurl.toLowerCase().replace(Konstanten.RTMP_PRTOKOLL, "http");
                }
                URL url = new URL(uurl);
                String tmp = url.getHost();
                if (tmp.contains(".")) {
                    host = tmp.substring(tmp.lastIndexOf("."));
                    tmp = tmp.substring(0, tmp.lastIndexOf("."));
                    if (tmp.contains(".")) {
                        host = tmp.substring(tmp.lastIndexOf(".") + 1) + host;
                    } else if (tmp.contains("/")) {
                        host = tmp.substring(tmp.lastIndexOf("/") + 1) + host;
                    } else {
                        host = "host";
                    }
                }
            } catch (Exception ex) {
                System.out.println("getHost 1: " + s.film.arr[Konstanten.FILM_URL_NR]);
                host = "host";
            } finally {
                if (host == null) {
                    System.out.println("getHost 2: " + s.film.arr[Konstanten.FILM_URL_NR]);
                    host = "host";
                }
                if (host.equals("")) {
                    System.out.println("getHost 3: " + s.film.arr[Konstanten.FILM_URL_NR]);
                    host = "host";
                }
            }
        } catch (Exception ex) {
            System.out.println("getHost 4: " + s.film.arr[Konstanten.FILM_URL_NR]);
            host = "exception";
        }
        return host;
    }

    private void startenThreadStarten() {
        starten = new Starten();
        Thread startenThread = new Thread(starten);
        startenThread.setDaemon(true);
        startenThread.start();
    }

    private class Starten implements Runnable {

        Starts starts;

        @Override
        public synchronized void run() {
            while (true) {
                try {
                    while ((starts = getListe()) != null) {
                        switch(starts.art) {
                            case Starts.ART_PROGRAMM:
                                StartenProgramm zdfStarten = new StartenProgramm(starts);
                                new Thread(zdfStarten).start();
                                this.wait(10000);
                                break;
                            case Starts.ART_DOWNLOAD:
                                StartenDonwnload podderStart = new StartenDonwnload(starts);
                                new Thread(podderStart).start();
                                this.wait(2000);
                                break;
                            default:
                                daten.fehler.fehlerMeldung("Fehler!", "StarterClass.Starten - Switch-default");
                                break;
                        }
                    }
                    buttonStartsPutzen();
                    this.wait(3000);
                } catch (Exception ex) {
                    daten.fehler.fehlerMeldung(ex, "StarterClass.Starten.run");
                }
            }
        }
    }

    private class StartenProgramm implements Runnable {

        Starts starts;

        RuntimeExec runtimeExec;

        public StartenProgramm(Starts s) {
            starts = s;
            starts.status = Starts.STATUS_RUN;
            notifyStartEvent();
            try {
                new File(starts.film.arr[Konstanten.FILM_ZIEL_PFAD_NR]).mkdirs();
            } catch (Exception ex) {
                daten.fehler.fehlerMeldung(ex, "StarterClass.ZdfStarten.run");
            }
        }

        @Override
        public synchronized void run() {
            int k = 0;
            long filesize = -1;
            boolean restart = false;
            boolean startOk = false;
            try {
                if (starten()) {
                    restart = true;
                }
                while (restart && !starts.stoppen) {
                    startOk = false;
                    restart = false;
                    while (!allesStop && !starts.stoppen) {
                        try {
                            k = starts.process.exitValue();
                            break;
                        } catch (Exception ex) {
                            try {
                                this.wait(2000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                    if (allesStop || starts.stoppen) {
                        if (starts.process != null) {
                            starts.process.destroy();
                            if (starts.quelle == Starts.QUELLE_BUTTON) {
                                starts.status = Starts.STATUS_FERTIG;
                            } else {
                                starts.status = Starts.STATUS_INIT;
                            }
                        }
                    } else {
                        if (k != 0) {
                            if (starts.restart) {
                                if (filesize == -1) {
                                    File file = new File(starts.film.arr[Konstanten.FILM_ZIEL_PFAD_DATEI_NR]);
                                    if (file.exists()) {
                                        filesize = file.length();
                                        startOk = true;
                                    } else if (starts.startcounter < Starts.STARTCOUNTER_MAX) {
                                        startOk = true;
                                    }
                                } else {
                                    File file = new File(starts.film.arr[Konstanten.FILM_ZIEL_PFAD_DATEI_NR]);
                                    if (file.exists()) {
                                        if (file.length() > filesize) {
                                            startOk = true;
                                            filesize = file.length();
                                        }
                                    }
                                }
                                if (startOk && starten()) {
                                    restart = true;
                                } else {
                                    starts.status = Starts.STATUS_ERR;
                                }
                            } else {
                                starts.status = Starts.STATUS_ERR;
                            }
                        } else if (starts.quelle == Starts.QUELLE_BUTTON) {
                            starts.status = Starts.STATUS_FERTIG;
                        } else if (pruefen(starts)) {
                            starts.status = Starts.STATUS_FERTIG;
                        } else {
                            starts.status = Starts.STATUS_ERR;
                        }
                    }
                }
            } catch (Exception ex) {
                daten.fehler.fehlerMeldung(ex, "StarterClass.StartenProgramm");
            }
            notifyStartEvent();
        }

        private boolean starten() {
            boolean ret = true;
            runtimeExec = new RuntimeExec(daten, starts);
            starts.process = runtimeExec.exec();
            if (starts.process == null) {
                starts.status = Starts.STATUS_ERR;
                ret = false;
            } else {
                starts.startcounter++;
            }
            return ret;
        }
    }

    private class StartenDonwnload implements Runnable {

        Starts starts;

        public StartenDonwnload(Starts s) {
            starts = s;
            starts.status = Starts.STATUS_RUN;
            notifyStartEvent();
        }

        @Override
        public void run() {
            InputStream input = null;
            OutputStream destStream = null;
            try {
                int len;
                new File(starts.film.arr[Konstanten.FILM_ZIEL_PFAD_NR]).mkdirs();
                URL feedUrl = new URL(starts.film.arr[Konstanten.FILM_URL_NR]);
                input = feedUrl.openStream();
                byte[] buffer = new byte[1024];
                destStream = new FileOutputStream(starts.film.arr[Konstanten.FILM_ZIEL_PFAD_DATEI_NR]);
                while ((len = input.read(buffer)) != -1) {
                    destStream.write(buffer, 0, len);
                    if (allesStop || starts.stoppen) {
                        break;
                    }
                }
                input.close();
                destStream.close();
            } catch (Exception e) {
                daten.fehler.fehlerMeldung(e, "StarterClass.StartenDonwnload-1");
            }
            try {
                if (allesStop || starts.stoppen) {
                } else {
                    if (starts.quelle == Starts.QUELLE_BUTTON) {
                        starts.status = Starts.STATUS_FERTIG;
                    } else if (pruefen(starts)) {
                        starts.status = Starts.STATUS_FERTIG;
                    } else {
                        starts.status = Starts.STATUS_ERR;
                    }
                }
            } catch (Exception e) {
                daten.fehler.fehlerMeldung(e, "StarterClass.StartenDonwnload-2");
            }
            notifyStartEvent();
        }
    }

    private boolean einmalErledigen(DatenFilm film) {
        boolean ret = false;
        String name = "";
        try {
            DatenAbo abo = daten.listeAbo.getEinmalAbo(film.arr[Konstanten.FILM_URL_NR]);
            if (abo != null) {
                ret = true;
                name = abo.arr[Konstanten.ABO_NAME_NR];
                abo.arr[Konstanten.ABO_EINMAL_ERLEDIGT_NR] = Boolean.toString(true);
                daten.fehler.systemMeldung("StarterClass.einmalErledigen Abo: " + name);
            }
        } catch (Exception ex) {
            daten.fehler.fehlerMeldung(ex, "StarterClass.einmalErledigen Abo: " + name);
        }
        return ret;
    }

    private boolean pruefen(Starts starts) {
        boolean ret = false;
        String logfile = "";
        if (starts.quelle == Starts.QUELLE_ABO) {
            logfile = Konstanten.LOG_DATEI_ZDF;
        } else if (starts.quelle == Starts.QUELLE_PODCAST) {
            logfile = Konstanten.LOG_DATEI_POD;
        }
        File file = new File(starts.film.arr[Konstanten.FILM_ZIEL_PFAD_DATEI_NR]);
        if (!file.exists()) {
            daten.fehler.fehlerMeldung("Fehler!", "Download fehlgeschlagen: " + starts.film.arr[Konstanten.FILM_ZIEL_PFAD_DATEI_NR]);
        } else if (file.length() < Konstanten.MIN_DATEI_GROESSE_KB * 1024) {
            daten.fehler.fehlerMeldung("Fehler!", "Download fehlgeschlagen: " + starts.film.arr[Konstanten.FILM_ZIEL_PFAD_DATEI_NR]);
        } else {
            if (!einmalErledigen(starts.film)) {
                daten.log.zeileSchreiben(starts.film.arr[Konstanten.FILM_TITEL_NR], starts.film.arr[Konstanten.FILM_URL_NR], logfile);
            }
            ret = true;
        }
        return ret;
    }
}
