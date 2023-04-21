package mediathek.filme.sender;

import java.text.SimpleDateFormat;
import java.util.Date;
import mediathek.Funktionen;
import mediathek.Konstanten;
import mediathek.daten.Daten;
import mediathek.filme.DatenFilm;

public class MediathekRbb extends MediathekReader implements Runnable {

    public MediathekRbb(Daten ddaten) {
        super(ddaten);
        sender = Konstanten.SENDER_RBB;
        text = "RBB  (ca. 3 MB, 50 Filme)";
    }

    private class ThemaLaden implements Runnable {

        @Override
        public synchronized void run() {
            notifyStart(1);
            addThread();
            try {
                laden();
            } catch (Exception ex) {
                System.err.println("MediathekRbb.ThemaLaden.run: " + ex.getMessage());
            }
            threadUndFertig();
        }
    }

    @Override
    void addToList() {
        new Thread(new ThemaLaden()).start();
    }

    void laden() {
        StringBuffer seite = new StringBuffer();
        int pos = 0;
        int pos1 = 0;
        int pos2 = 0;
        String url = "";
        String thema = "";
        String datum = "";
        String titel = "";
        final String ITEM_1 = "<div class=\"teaserFlash\">";
        final String MUSTER_URL = "verpasst.html\" href=\"";
        final String ADRESSE = "http://www.rbb-online.de/doku/videothek/den_film_im_tv_verpasst.html";
        this.notifyProgress(ADRESSE);
        try {
            seite = getUrlIo.getUri_Utf(sender, ADRESSE, seite, "");
            while ((pos = seite.indexOf(ITEM_1, pos)) != -1) {
                pos += ITEM_1.length();
                url = "";
                thema = "";
                datum = "";
                titel = "";
                pos1 = pos;
                if ((pos1 = seite.indexOf(MUSTER_URL, pos)) != -1) {
                    pos1 += MUSTER_URL.length();
                    if ((pos2 = seite.indexOf("\"", pos1)) != -1) {
                        url = seite.substring(pos1, pos2);
                    }
                }
                if (url.equals("")) {
                    daten.fehler.fehlerMeldung("Fehler!", "MediathekRbb.addToList: keine URL");
                } else {
                    if ((pos1 = seite.indexOf(">", pos2)) != -1) {
                        pos1 += 1;
                        if ((pos2 = seite.indexOf("</", pos1)) != -1) {
                            thema = seite.substring(pos1, pos2);
                            titel = thema;
                        }
                    }
                    if ((pos1 = url.indexOf("201")) != -1) {
                        if (url.length() > pos1 + 8) {
                            datum = convertDatum(url.substring(pos1, pos1 + 8));
                        }
                    } else if ((pos1 = url.indexOf("200")) != -1) {
                        if (url.length() > pos1 + 8) {
                            datum = convertDatum(url.substring(pos1, pos1 + 8));
                        }
                    }
                }
                String urlRtmp;
                if (url.endsWith("mp4")) {
                    urlRtmp = "--host stream5.rbb-online.de --app rbb/ --playpath mp4:" + url;
                } else {
                    urlRtmp = "--host stream5.rbb-online.de --app rbb/ --playpath " + url;
                }
                String urlOrg = Funktionen.addsUrl(daten, "rtmp://stream5.rbb-online.de/rbb", url);
                DatenFilm film = new DatenFilm(daten, sender, thema, ADRESSE, titel, urlRtmp, urlOrg, urlRtmp, datum, "", false);
                daten.filmeLaden.listeFilmeSchattenliste.addSenderRtmp(film);
            }
        } catch (Exception ex) {
            daten.fehler.fehlerMeldung(ex, "MediathekBr.addToList");
        }
    }

    public String convertDatum(String datum) {
        try {
            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMdd");
            Date filmDate = sdfIn.parse(datum);
            SimpleDateFormat sdfOut;
            sdfOut = new SimpleDateFormat("dd.MM.yyyy");
            datum = sdfOut.format(filmDate);
        } catch (Exception ex) {
            daten.fehler.fehlerMeldung(ex, "MediathekRbb.convertDatum");
        }
        return datum;
    }
}
