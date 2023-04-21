package mediathek.update;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import mediathek.Konstanten;
import mediathek.daten.DatenFilmUpdateServer;
import mediathek.daten.ListeFilmUpdateServer;

public class FilmListenServerSuchen {

    public String[] getListe(ListeFilmUpdateServer sListe, String useragent) throws MalformedURLException, IOException, XMLStreamException {
        String[] ret = new String[] { "", "", "" };
        sListe.clear();
        int event;
        XMLInputFactory inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        XMLStreamReader parser = null;
        InputStreamReader inReader = null;
        int timeout = 10000;
        URLConnection conn = null;
        conn = new URL(Konstanten.ADRESSE_UPDATE).openConnection();
        conn.setRequestProperty("User-Agent", useragent);
        conn.setReadTimeout(timeout);
        conn.setConnectTimeout(timeout);
        inReader = new InputStreamReader(conn.getInputStream(), Konstanten.KODIERUNG_UTF);
        parser = inFactory.createXMLStreamReader(inReader);
        while (parser.hasNext()) {
            event = parser.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if (parser.getLocalName().equals("Program_Version")) {
                    ret[0] = parser.getElementText();
                } else if (parser.getLocalName().equals("Program_Release_Info")) {
                    ret[1] = parser.getElementText();
                } else if (parser.getLocalName().equals("Download_Programm")) {
                    ret[2] = parser.getElementText();
                } else if (parser.getLocalName().equals("Server")) {
                    getServer(parser, sListe);
                }
            }
        }
        return ret;
    }

    private void getServer(XMLStreamReader parser, ListeFilmUpdateServer sListe) {
        String anzahl = "";
        String zeit = "";
        String datum = "";
        String filmUrl = "";
        String prio = "";
        int event;
        try {
            while (parser.hasNext()) {
                prio = Konstanten.FILM_UPDATE_SERVER_PRIO_1;
                event = parser.next();
                if (event == XMLStreamConstants.END_ELEMENT) {
                    if (parser.getLocalName().equals("Server")) {
                        if (!filmUrl.equals("")) {
                            sListe.addWithCheck(new DatenFilmUpdateServer(filmUrl, prio, zeit, datum, anzahl));
                        }
                        break;
                    }
                }
                if (event == XMLStreamConstants.START_ELEMENT) {
                    if (parser.getLocalName().equals("Download_Filme_1")) {
                        filmUrl = parser.getElementText();
                        prio = Konstanten.FILM_UPDATE_SERVER_PRIO_1;
                    } else if (parser.getLocalName().equals("Download_Filme_2")) {
                        filmUrl = parser.getElementText();
                        prio = Konstanten.FILM_UPDATE_SERVER_PRIO_2;
                    } else if (parser.getLocalName().equals("Datum")) {
                        datum = parser.getElementText();
                    } else if (parser.getLocalName().equals("Zeit")) {
                        zeit = parser.getElementText();
                    } else if (parser.getLocalName().equals("Anzahl")) {
                        anzahl = parser.getElementText();
                    }
                }
            }
        } catch (XMLStreamException ex) {
        }
    }
}
