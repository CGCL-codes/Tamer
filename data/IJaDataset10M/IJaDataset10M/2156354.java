package mediathek.gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import mediathek.daten.Daten;
import mediathek.filme.BeobFilmeLaden;

public class MediathekNoGui {

    private Daten daten;

    private Date startZeit = new Date(System.currentTimeMillis());

    private Date stopZeit = null;

    private String output = "";

    public MediathekNoGui(String[] ar) {
        boolean debug = false;
        boolean update = false;
        String pfad = "";
        if (ar != null) {
            if (ar.length > 0) {
                if (!ar[0].startsWith("-")) {
                    if (!ar[0].endsWith(File.separator)) {
                        ar[0] += File.separator;
                    }
                    pfad = ar[0];
                }
            }
            for (int i = 0; i < ar.length; ++i) {
                if (ar[i].equals("-D")) {
                    debug = true;
                }
                if (ar[i].equals("-update")) {
                    update = true;
                }
                if (ar[i].equalsIgnoreCase("-o")) {
                    if (ar.length > i) {
                        output = ar[i + 1];
                    }
                }
            }
        }
        daten = new Daten(pfad, debug, null, true);
        daten.allesLaden = !update;
        daten.fehlerFensterAnzeigen = false;
        System.out.println("");
        System.out.println("========================================");
        if (daten.allesLaden) {
            System.out.println("Laden: alles");
        } else {
            System.out.println("Laden: nur update");
        }
        System.out.println("========================================");
        System.out.println("");
        initListener();
        daten.filmeLaden.filmeBeimSenderLaden();
    }

    private void initListener() {
        daten.filmeLaden.addAdListener(new BeobachterLadenFilme());
    }

    private void undTschuess() {
        daten.speichern();
        if (!output.equals("")) {
            LinkedList<String> out = new LinkedList<String>();
            String tmp = "";
            do {
                if (output.startsWith(",")) {
                    output = output.substring(1);
                }
                if (output.contains(",")) {
                    tmp = output.substring(0, output.indexOf(","));
                    output = output.substring(output.indexOf(","));
                    out.add(tmp);
                } else {
                    out.add(output);
                }
            } while (output.contains(","));
            Iterator<String> it = out.iterator();
            while (it.hasNext()) {
                daten.ioXmlSchreiben.exportFilme(it.next());
            }
        }
        stopZeit = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int minuten = 0;
        try {
            minuten = Math.round((stopZeit.getTime() - startZeit.getTime()) / (1000 * 60));
        } catch (Exception ex) {
            minuten = -1;
        }
        System.out.println("");
        System.out.println("========================================");
        System.out.println("  " + daten.filmeLaden.getSeitenZaehlerLauf() + " Seiten geladen");
        System.out.println("  " + daten.filmeLaden.listeFilmeSchattenliste.size() + " Filme gesamt");
        System.out.println("  --> Beginn: " + sdf.format(startZeit));
        System.out.println("  --> Fertig: " + sdf.format(stopZeit));
        System.out.println("  --> Dauer[Min]: " + minuten);
        System.out.println("========================================");
        if (daten.filmeLaden.listeFilmeSchattenliste.isEmpty()) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    private synchronized void progressBar(int max, int progress) {
        int proz = 0;
        String text = "";
        if (max != 0) {
            if (progress != 0) {
                proz = progress * 100 / max;
            }
            if (max > 0 && proz == 100) {
                proz = 99;
            }
            text = "  [ ";
            int a = proz / 10;
            for (int i = 0; i < a; ++i) {
                text += "#";
            }
            for (int i = 0; i < (10 - a); ++i) {
                text += "-";
            }
            text += " ]  " + daten.filmeLaden.getSeitenZaehlerLauf() + " Seiten  /  " + proz + "% von " + max + " Themen  /  Filme: " + daten.filmeLaden.listeFilmeSchattenliste.size() + "\r";
            daten.fehler.progressMeldung(text);
        }
    }

    private class BeobachterLadenFilme extends BeobFilmeLaden {

        @Override
        public void initProgressBar(String sender, int threads, int max, int progress, String text) {
            progressBar(max, progress);
        }

        @Override
        public void beenden() {
            undTschuess();
        }
    }
}
