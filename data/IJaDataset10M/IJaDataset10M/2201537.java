package mediathek.filme.sender;

import java.util.LinkedList;
import mediathek.Konstanten;
import mediathek.daten.Daten;
import mediathek.filme.DatenFilm;
import mediathek.io.GetUrl;

/**
 *
 * @author
 */
public class MediathekNdr extends MediathekReader implements Runnable {

    final int MAX_SEITEN_LANG = 150;

    final int MAX_SEITEN_KURZ = 20;

    /**
     *
     * @param ddaten
     */
    public MediathekNdr(Daten ddaten) {
        super(ddaten);
        sender = Konstanten.SENDER_NDR;
        text = "NDR  (ca. 6 MB, 500 Filme)";
    }

    @Override
    void addToList() {
        final String ADRESSE = "http://www.ndr.de/mediathek/videoliste100_glossaryPage-1.html";
        final String ADRESSE_TEIL = "http://www.ndr.de/mediathek/videoliste100_glossaryPage-";
        final String MUSTER_ANZAHL = "Zeige Seite";
        int maxSeiten = MAX_SEITEN_KURZ;
        listeThemen.clear();
        StringBuffer seite = new StringBuffer();
        int pos = 0;
        int pos1 = 0;
        int pos2 = 0;
        String url = "";
        String max = "";
        seite = getUrlIo.getUri_Utf(sender, ADRESSE, seite, "");
        if (daten.allesLaden) {
            if ((pos = seite.lastIndexOf(MUSTER_ANZAHL)) != -1) {
                pos += MUSTER_ANZAHL.length();
                pos1 = pos;
                if ((pos2 = seite.indexOf("\"", pos1)) != -1) {
                    try {
                        max = seite.substring(pos1, pos2);
                        max = max.trim();
                        maxSeiten = Integer.parseInt(max);
                        if (maxSeiten > MAX_SEITEN_LANG) {
                            maxSeiten = MAX_SEITEN_LANG;
                        }
                    } catch (Exception ex) {
                        maxSeiten = MAX_SEITEN_KURZ;
                    }
                }
            }
        }
        for (int i = 1; i < maxSeiten; ++i) {
            String[] add = new String[] { ADRESSE_TEIL + i + ".html", "" };
            listeThemen.add(add);
        }
        if (!stop) {
            if (listeThemen.size() > 0) {
                notifyStart(listeThemen.size());
                for (int t = 0; t < Konstanten.MAX_THREAD_LADEN_FILME_KLEIN; ++t) {
                    new Thread(new ThemaLaden()).start();
                }
            }
        }
    }

    private class ThemaLaden implements Runnable {

        GetUrl getUrl1 = new GetUrl(daten, 30000, Konstanten.WARTEN_BASIS_URL_LANG);

        GetUrl getUrl2 = new GetUrl(daten, 10000, Konstanten.WARTEN_BASIS_URL_LANG);

        private StringBuffer seite1 = new StringBuffer();

        private StringBuffer seite2 = new StringBuffer();

        @Override
        public synchronized void run() {
            try {
                addThread();
                String[] link;
                while (!stop && (link = getListeThemen()) != null) {
                    try {
                        notifyProgress(link[0]);
                        finden(link[0]);
                    } catch (Exception ex) {
                        System.err.println("MediathekNdr.ThemaLaden.run: " + ex.getMessage());
                    }
                }
                threadUndFertig();
            } catch (Exception ex) {
                System.err.println("MediathekNdr.ThemaLaden.run: " + ex.getMessage());
            }
        }

        private void finden(String urlSeite) {
            LinkedList<String> hammerSchon = new LinkedList<String>();
            final String MUSTER_DATUM = "<div class=\"subline\">";
            final String MUSTER_URL1 = "<a href=\"/fernsehen/";
            int pos = 0;
            int pos1 = 0;
            int pos2 = 0;
            String datum = "";
            String zeit = "";
            String url = "";
            String thema = "";
            seite1 = getUrl1.getUri_Utf(sender, urlSeite, seite1, "");
            while ((pos = seite1.indexOf(MUSTER_DATUM, pos)) != -1) {
                datum = "";
                zeit = "";
                url = "";
                thema = "";
                pos += MUSTER_DATUM.length();
                try {
                    if ((pos1 = seite1.indexOf("|", pos)) != -1) {
                        datum = seite1.substring(pos, pos1).trim();
                    }
                    if ((pos2 = seite1.indexOf("<", pos1)) != -1) {
                        zeit = seite1.substring(pos1 + 1, pos2).trim();
                        if (zeit.contains("Uhr")) {
                            zeit = zeit.replace("Uhr", "").trim() + ":00";
                        }
                    }
                    pos1 = 0;
                    pos2 = 0;
                    if ((pos1 = seite1.indexOf(MUSTER_URL1, pos)) != -1) {
                        pos1 += MUSTER_URL1.length();
                        if ((pos2 = seite1.indexOf("\"", pos1)) != -1) {
                            pos = pos2;
                            url = seite1.substring(pos1, pos2);
                        }
                        if (url.contains("/")) {
                            String posTh = url.substring(url.indexOf("sendungen/") + "sendungen/".length());
                            if (posTh.contains("/")) {
                                thema = posTh.substring(0, posTh.indexOf("/"));
                            }
                        }
                        if (nurAboLaden()) {
                            if (!daten.listeAbo.aboExists(sender, thema)) {
                                continue;
                            }
                        }
                        if (url.equals("")) {
                            continue;
                        }
                        if (!hammerSchon.contains(url)) {
                            hammerSchon.add(url);
                            feedEinerSeiteSuchen("http://www.ndr.de/fernsehen/" + url, thema, datum, zeit);
                        }
                    }
                } catch (Exception ex) {
                    daten.fehler.fehlerMeldung("Fehler!", "Test: kein Thema");
                }
            }
        }

        void feedEinerSeiteSuchen(String urlFilm, String thema, String datum, String zeit) {
            final String MUSTER_URL = "<span class='footer_link'><a href=\"";
            final String MUSTER_TITEL = "<title>";
            seite2 = getUrl2.getUri_Utf(sender, urlFilm, seite2, "strUrlFilm: " + urlFilm);
            int pos = 0;
            int pos1 = 0;
            int pos2 = 0;
            String url = "";
            String titel = "";
            try {
                if ((pos = seite2.indexOf(MUSTER_TITEL, pos)) != -1) {
                    pos += MUSTER_TITEL.length();
                    pos1 = pos;
                    pos2 = seite2.indexOf("<", pos);
                    if (pos1 != -1 && pos2 != -1) {
                        titel = seite2.substring(pos1, pos2);
                        if (titel.contains("|")) {
                            titel = titel.substring(0, titel.indexOf("|"));
                            titel = titel.trim();
                        }
                    }
                }
                pos = 0;
                if ((pos = seite2.indexOf(MUSTER_URL, pos)) != -1) {
                    pos += MUSTER_URL.length();
                    pos1 = pos;
                    if ((pos2 = seite2.indexOf("\"", pos)) != -1) {
                        url = seite2.substring(pos1, pos2);
                        if (!url.equals("")) {
                            if (thema.equals("")) {
                                thema = titel;
                            }
                            daten.filmeLaden.listeFilmeSchattenliste.addSenderRtmp(new DatenFilm(daten, sender, thema, urlFilm, titel, url, datum, zeit));
                        } else {
                            daten.fehler.fehlerMeldung("Fehler!", "keine Url filmSuchen" + urlFilm);
                        }
                    }
                }
            } catch (Exception ex) {
                daten.fehler.fehlerMeldung(ex, "keine Url filmSuchen" + urlFilm);
            }
        }
    }
}
