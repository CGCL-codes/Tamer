package com.umc.plugins;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import com.umc.beans.persons.Actor;
import com.umc.beans.persons.Author;
import com.umc.beans.persons.Cutter;
import com.umc.beans.persons.Director;
import com.umc.beans.persons.IPerson;
import com.umc.beans.persons.Producer;
import com.umc.beans.persons.ProducerOST;
import com.umc.dao.DataAccessFactory;
import com.umc.dao.UMCDataAccessInterface;
import com.umc.helper.UMCParams;
import com.umc.online.AbstractMovieDBPlugin;

/**
 * Implementiert das Interface MovieDBInterface für den Zugriff auf die Online-Filmdatenbank OFDB.
 * 
 * @author DonGyros
 *
 * @version 0.1 11.09.2008
 */
public class OFDB extends AbstractMovieDBPlugin {

    private static Logger log = Logger.getLogger("com.umc.plugin.moviedb");

    /**Die OFDB Schnittstelle verwendet dieses Pattern in der Ergebnismenge bei "Titel" für Serien*/
    private static final String serienPattern = "[TV-Serie]";

    private UMCDataAccessInterface dao = null;

    private static final int RCODE_0 = 0;

    private static final int RCODE_1 = 1;

    private static final int RCODE_2 = 2;

    private static final int RCODE_3 = 3;

    private static final int RCODE_4 = 4;

    private static final int RCODE_5 = 5;

    /**Return-Code 0 des OFDB Webservices, sprechende Meldung */
    private static final String RCODE_0_MSG = "Keine Fehler";

    /**Return-Code 1 des OFDB Webservices, sprechende Meldung */
    private static final String RCODE_1_MSG = "Unbekannter Fehler";

    /**Return-Code 2 des OFDB Webservices, sprechende Meldung */
    private static final String RCODE_2_MSG = "Fehler oder Timeout bei Anfrage an OFDB";

    /**Return-Code 3 des OFDB Webservices, sprechende Meldung */
    private static final String RCODE_3_MSG = "Keine oder falsche ID angebene";

    /**Return-Code 4 des OFDB Webservices, sprechende Meldung */
    private static final String RCODE_4_MSG = "Keine Daten zu angegebener ID oder Query gefunden";

    /**Return-Code 5 des OFDB Webservices, sprechende Meldung */
    private static final String RCODE_5_MSG = "Fehler bei der Datenverarbeitung.";

    private Hashtable<String, Object> result = null;

    public OFDB() {
        setPluginDBVersion(1);
        setPluginDescriptionEnglish("This plugin enables UMC to search on OFDB");
        setPluginDescriptionFrench(null);
        setPluginDescriptionGerman("Mit diesem Plugin ist es möglich Informationen zu einem Filmtitel online bei OFDB abzufragen");
        setPluginIDModul(-1);
        setPluginNameEnglish("OFDB-Plugin");
        setPluginNameFrench("OFDB-Plugin");
        setPluginNameGerman("OFDB-Plugin");
        setPluginVersion(0.1);
    }

    private String getID(String aTitle, String aYear) {
        String result = null;
        String[] bestMatch = { "1000", "" };
        Scanner scanner = null;
        int wordCount = 0;
        try {
            log.info("suche in Filmdatenbank nach: " + aTitle);
            u = new URL("http://xml.n4rf.net/ofdbgw/search/" + aTitle);
            URLConnection con = u.openConnection();
            con.setConnectTimeout(30000);
            docOnline = new SAXBuilder().build(u);
            root = docOnline.getRootElement();
            statusrcode = root.getChild("status").getChild("rcode");
            if (Integer.parseInt(statusrcode.getValue()) == 4) {
                log.warn(RCODE_4_MSG + ", alternative OFDB-Suche wird durchgeführt");
                u = new URL("http://xml.n4rf.net/ofdbgw/searchalt/" + aTitle);
                con = u.openConnection();
                con.setConnectTimeout(30000);
                docOnline = new SAXBuilder().build(u);
                root = docOnline.getRootElement();
                statusrcode = root.getChild("status").getChild("rcode");
            }
            if (Integer.parseInt(statusrcode.getValue()) == 0) {
                List l = root.getChild("resultat").getChildren("eintrag");
                for (int b = 0; b < l.size(); b++) {
                    String movieDBId = ((Element) l.get(b)).getChild("id").getValue();
                    String movieDBTitle = ((Element) l.get(b)).getChild("titel").getValue();
                    String movieDBYear = ((Element) l.get(b)).getChild("jahr").getValue();
                    String movieDBTitleOrg = null;
                    if (((Element) l.get(b)).getChild("titel_orig") != null) movieDBTitleOrg = ((Element) l.get(b)).getChild("titel_orig").getValue();
                    if (movieDBTitle.indexOf(serienPattern) == -1) {
                        if (compareTitles(aTitle, aYear, movieDBTitle, movieDBYear, movieDBTitleOrg)) {
                            log.debug("alle Wörter von " + aTitle + " passen in " + movieDBTitle + " mit ID " + movieDBId);
                            wordCount = 0;
                            scanner = new Scanner(movieDBTitle);
                            while (scanner.hasNext()) {
                                scanner.next();
                                wordCount++;
                            }
                            if (Integer.parseInt(bestMatch[0]) > wordCount) {
                                bestMatch[0] = wordCount + "";
                                bestMatch[1] = movieDBId;
                            }
                        }
                    }
                }
                if (bestMatch[1] == null || bestMatch[1].equals("")) log.warn("kein Ergebnis bei OFDB für Suche mit Filmtitel " + aTitle + " gefunden"); else log.debug("am nahe liegenster Onlinetitel mit ID " + bestMatch[1] + " für " + aTitle + " gefunden");
                result = bestMatch[1];
            } else {
                switch(Integer.parseInt(statusrcode.getValue())) {
                    case 1:
                        log.error(RCODE_1_MSG + "(" + aTitle + ")");
                        break;
                    case 2:
                        log.error(RCODE_2_MSG + "(" + aTitle + ")");
                        break;
                    case 3:
                        log.error(RCODE_3_MSG + "(" + aTitle + ")");
                        break;
                    case 4:
                        log.error(RCODE_4_MSG + "(" + aTitle + ")");
                        break;
                    case 5:
                        log.error(RCODE_5_MSG + "(" + aTitle + ")");
                        break;
                }
            }
            return result;
        } catch (SocketTimeoutException exc) {
            log.error("URL gab innerhalb einer bestimmten Zeit keine Antwort", exc);
            return result;
        } catch (MalformedURLException exc) {
            log.error("Unbekanntes Protokol", exc);
            return result;
        } catch (JDOMException exc) {
            log.error("JDOM Fehler", exc);
            return result;
        } catch (IOException exc) {
            log.error("I/O Fehler", exc);
            return result;
        } catch (Exception exc) {
            log.error("Allgemeiner Fehler", exc);
            return result;
        } finally {
        }
    }

    /**
	 * Diese Methode vergleicht den übergebenen Titel mit einem Titel aus der Online-Filmdatenbank.
	 * 
	 * @param aTitle
	 * @param onlineTitle
	 * @return true/false
	 */
    private boolean compareTitles(String aTitle, String aYear, String onlineTitle, String onlineYear, String onlineTitleOrg) {
        int tokens = 0;
        int found = 0;
        aTitle = aTitle.toLowerCase().trim();
        onlineTitle = onlineTitle.toLowerCase().trim();
        Scanner scanner = new Scanner(aTitle);
        log.debug("Vergleiche ermittelten Titel " + aTitle + " (Jahr: " + aYear + ") mit Onlinetitel " + onlineTitle + " (" + onlineYear + ")");
        String t = "";
        while (scanner.hasNext()) {
            t = scanner.next().toLowerCase();
            if (t.length() > 1 || (t.length() == 1 && !Pattern.matches("[^0-9a-zA-ZöÖüÜäÄ]", t))) {
                tokens++;
                if (Pattern.matches(".*(\\A|[^0-9a-zA-ZüÜäÄöÖ])" + t + "([^0-9a-zA-ZüÜäÄöÖ]|\\Z).*", onlineTitle)) {
                    found++;
                }
            }
        }
        if (tokens == found) {
            if (aYear != null && !aYear.equals("") && !aYear.equals("-1")) {
                if (aYear.equals(onlineYear)) return true;
            } else {
                return true;
            }
        }
        if (onlineTitleOrg != null && !onlineTitleOrg.equals("")) {
            log.debug("Vergleiche nochmal Alternativtitel " + onlineTitleOrg);
            return compareTitles(onlineTitleOrg, aYear, onlineTitle, onlineYear, null);
        }
        return false;
    }

    public Hashtable<String, Object> getInfos(String aTitle, String aYear, String aLanguage) {
        String uniqueID = "";
        result = null;
        try {
            uniqueID = getID(aTitle, aYear);
            if (uniqueID != null && !uniqueID.equals("")) {
                result = new Hashtable<String, Object>();
                result = getInfos(uniqueID);
            }
            return result;
        } catch (Exception exc) {
            log.error("Allgemeiner Fehler", exc);
            return result;
        } finally {
        }
    }

    public Hashtable<String, Object> getInfosByID(String aID) {
        return getInfos(aID);
    }

    public Hashtable<String, Object> getInfosByIMDBID(String imdbID) {
        return getInfosByIMDB(imdbID);
    }

    private Hashtable<String, Object> getInfos(String aID) {
        result = new Hashtable<String, Object>();
        try {
            if (aID != null && !aID.equals("")) {
                log.info("suche in Filmdatenbank nach: " + aID);
                u = new URL("http://xml.n4rf.net/ofdbgw/movie/" + aID);
                URLConnection con = u.openConnection();
                con.setConnectTimeout(30000);
                docOnline = new SAXBuilder().build(u);
                root = docOnline.getRootElement();
                statusrcode = root.getChild("status").getChild("rcode");
                if (Integer.parseInt(statusrcode.getValue()) == 0) {
                    result.put(KEY_ID, aID);
                    String title = "";
                    if (root.getChild("resultat").getChild("titel") != null) title = root.getChild("resultat").getChild("titel").getValue();
                    result.put(KEY_TITLE, title);
                    String alternativeTitle = "";
                    if (root.getChild("resultat").getChild("alternativ") != null) alternativeTitle = root.getChild("resultat").getChild("alternativ").getValue();
                    result.put(KEY_TITLE_ALTERNATIVE, alternativeTitle);
                    String imdbID = "";
                    if (root.getChild("resultat").getChild("imdbid") != null) imdbID = "tt" + root.getChild("resultat").getChild("imdbid").getValue();
                    result.put(KEY_IMDB_ID, imdbID);
                    String year = "";
                    if (root.getChild("resultat").getChild("jahr") != null) year = root.getChild("resultat").getChild("jahr").getValue();
                    result.put(KEY_YEAR, year);
                    String cover = "";
                    if (root.getChild("resultat").getChild("bild") != null) cover = root.getChild("resultat").getChild("bild").getValue();
                    result.put(KEY_COVER, cover);
                    String note = "";
                    if (root.getChild("resultat").getChild("bewertung").getChild("note") != null) note = root.getChild("resultat").getChild("bewertung").getChild("note").getValue();
                    result.put(KEY_RATING, note);
                    String regie = "";
                    if (root.getChild("resultat").getChild("regie").getChild("person") != null) regie = root.getChild("resultat").getChild("regie").getChild("person").getValue();
                    result.put(KEY_REGIE, regie);
                    Collection<String> genres = new ArrayList<String>();
                    Iterator i = root.getChild("resultat").getChild("genre").getChildren("titel").iterator();
                    Element child = null;
                    while (i.hasNext()) {
                        child = (Element) i.next();
                        if (child.getValue() != null) {
                            genres.add(child.getValue());
                        }
                    }
                    result.put(KEY_GENRES, genres);
                    String shortStory = "";
                    if (root.getChild("resultat").getChild("kurzbeschreibung") != null) shortStory = root.getChild("resultat").getChild("kurzbeschreibung").getValue();
                    result.put(KEY_SHORT_STORY, shortStory);
                    String story = "";
                    if (root.getChild("resultat").getChild("beschreibung") != null) story = root.getChild("resultat").getChild("beschreibung").getValue();
                    result.put(KEY_STORY, story);
                    int arraySize = 0;
                    arraySize += root.getChild("resultat").getChild("regie").getChildren("person").size();
                    arraySize += root.getChild("resultat").getChild("secondunitregie").getChildren("person").size();
                    arraySize += root.getChild("resultat").getChild("produzent").getChildren("person").size();
                    arraySize += root.getChild("resultat").getChild("drehbuch").getChildren("person").size();
                    arraySize += root.getChild("resultat").getChild("cut").getChildren("person").size();
                    arraySize += root.getChild("resultat").getChild("soundtrack").getChildren("person").size();
                    arraySize += root.getChild("resultat").getChild("besetzung").getChildren("person").size();
                    Collection<IPerson> persons = new ArrayList<IPerson>();
                    i = root.getChild("resultat").getChild("regie").getChildren("person").iterator();
                    while (i.hasNext()) {
                        child = (Element) i.next();
                        if (child != null) {
                            IPerson a = new Director();
                            if (child.getChild("id") != null && !child.getChild("id").getValue().equals("")) a.setIdOFDB(Integer.parseInt(child.getChild("id").getValue()));
                            a.setName(child.getChild("name").getValue());
                            persons.add(a);
                        }
                    }
                    i = root.getChild("resultat").getChild("secondunitregie").getChildren("person").iterator();
                    while (i.hasNext()) {
                        child = (Element) i.next();
                        if (child != null) {
                            IPerson a = new Director();
                            if (child.getChild("id") != null && !child.getChild("id").getValue().equals("")) a.setIdOFDB(Integer.parseInt(child.getChild("id").getValue()));
                            a.setName(child.getChild("name").getValue());
                            persons.add(a);
                        }
                    }
                    i = root.getChild("resultat").getChild("produzent").getChildren("person").iterator();
                    while (i.hasNext()) {
                        child = (Element) i.next();
                        if (child != null) {
                            IPerson a = new Producer();
                            if (child.getChild("id") != null && !child.getChild("id").getValue().equals("")) a.setIdOFDB(Integer.parseInt(child.getChild("id").getValue()));
                            a.setName(child.getChild("name").getValue());
                            persons.add(a);
                        }
                    }
                    i = root.getChild("resultat").getChild("drehbuch").getChildren("person").iterator();
                    while (i.hasNext()) {
                        child = (Element) i.next();
                        if (child != null) {
                            IPerson a = new Author();
                            if (child.getChild("id") != null && !child.getChild("id").getValue().equals("")) a.setIdOFDB(Integer.parseInt(child.getChild("id").getValue()));
                            a.setName(child.getChild("name").getValue());
                            persons.add(a);
                        }
                    }
                    i = root.getChild("resultat").getChild("cut").getChildren("person").iterator();
                    while (i.hasNext()) {
                        child = (Element) i.next();
                        if (child != null) {
                            IPerson a = new Cutter();
                            if (child.getChild("id") != null && !child.getChild("id").getValue().equals("")) a.setIdOFDB(Integer.parseInt(child.getChild("id").getValue()));
                            a.setName(child.getChild("name").getValue());
                            persons.add(a);
                        }
                    }
                    i = root.getChild("resultat").getChild("soundtrack").getChildren("person").iterator();
                    while (i.hasNext()) {
                        child = (Element) i.next();
                        if (child != null) {
                            IPerson a = new ProducerOST();
                            if (child.getChild("id") != null && !child.getChild("id").getValue().equals("")) a.setIdOFDB(Integer.parseInt(child.getChild("id").getValue()));
                            a.setName(child.getChild("name").getValue());
                            persons.add(a);
                        }
                    }
                    i = root.getChild("resultat").getChild("besetzung").getChildren("person").iterator();
                    while (i.hasNext()) {
                        child = (Element) i.next();
                        if (child != null) {
                            IPerson a = new Actor();
                            if (child.getChild("id") != null && !child.getChild("id").getValue().equals("")) a.setIdOFDB(Integer.parseInt(child.getChild("id").getValue()));
                            a.setName(child.getChild("name").getValue());
                            a.setRole(child.getChild("rolle").getValue());
                            persons.add(a);
                        }
                    }
                    result.put(KEY_PERSONS, persons);
                    int counter = 0;
                    String[] productionLands = new String[root.getChild("resultat").getChild("produktionsland").getChildren("name").size()];
                    i = root.getChild("resultat").getChild("produktionsland").getChildren("name").iterator();
                    child = null;
                    while (i.hasNext()) {
                        child = (Element) i.next();
                        if (child.getValue() != null) {
                            productionLands[counter] = child.getValue();
                            counter++;
                        }
                    }
                    result.put(KEY_PRODUCTION_COUNTRIES, productionLands);
                } else {
                    switch(Integer.parseInt(statusrcode.getValue())) {
                        case 1:
                            log.error(RCODE_1_MSG + "(OFDB-ID = " + aID + ")");
                            break;
                        case 2:
                            log.error(RCODE_2_MSG + "(OFDB-ID = " + aID + ")");
                            break;
                        case 3:
                            log.error(RCODE_3_MSG + "(OFDB-ID = " + aID + ")");
                            break;
                        case 4:
                            log.error(RCODE_4_MSG + "(OFDB-ID = " + aID + ")");
                            break;
                        case 5:
                            log.error(RCODE_5_MSG + "(OFDB-ID = " + aID + ")");
                            break;
                    }
                }
            }
            return result;
        } catch (SocketTimeoutException exc) {
            log.error("URL gab innerhalb einer bestimmten Zeit keine Antwort", exc);
            return result;
        } catch (MalformedURLException exc) {
            log.error("Unbekanntes Protokol", exc);
            return result;
        } catch (JDOMException exc) {
            log.error("JDOM Fehler", exc);
            return result;
        } catch (IOException exc) {
            log.error("I/O Fehler", exc);
            return result;
        } catch (Exception exc) {
            log.error("Allgemeiner Fehler", exc);
            return result;
        } finally {
        }
    }

    private Hashtable<String, Object> getInfosByIMDB(String imdbID) {
        result = null;
        try {
            if (imdbID != null && !imdbID.equals("")) {
                log.info("suche in Filmdatenbank nach: " + imdbID);
                u = new URL("http://xml.n4rf.net/ofdbgw/imdb2ofdb/" + imdbID);
                URLConnection con = u.openConnection();
                con.setConnectTimeout(30000);
                docOnline = new SAXBuilder().build(u);
                root = docOnline.getRootElement();
                statusrcode = root.getChild("status").getChild("rcode");
                if (Integer.parseInt(statusrcode.getValue()) == 0) {
                    String ofdbID = "";
                    if (root.getChild("resultat").getChild("ofdbid") != null) {
                        ofdbID = root.getChild("resultat").getChild("ofdbid").getValue();
                        return getInfosByID(ofdbID);
                    }
                } else {
                    switch(Integer.parseInt(statusrcode.getValue())) {
                        case 1:
                            log.error(RCODE_1_MSG + "(IMDB-ID = " + imdbID + ")");
                            break;
                        case 2:
                            log.error(RCODE_2_MSG + "(IMDB-ID = " + imdbID + ")");
                            break;
                        case 3:
                            log.error(RCODE_3_MSG + "(IMDB-ID = " + imdbID + ")");
                            break;
                        case 4:
                            log.error(RCODE_4_MSG + "(IMDB-ID = " + imdbID + ")");
                            break;
                        case 5:
                            log.error(RCODE_5_MSG + "(IMDB-ID = " + imdbID + ")");
                            break;
                    }
                }
            }
            return result;
        } catch (SocketTimeoutException exc) {
            log.error("URL gab innerhalb einer bestimmten Zeit keine Antwort", exc);
            return result;
        } catch (MalformedURLException exc) {
            log.error("Unbekanntes Protokol", exc);
            return result;
        } catch (JDOMException exc) {
            log.error("JDOM Fehler", exc);
            return result;
        } catch (IOException exc) {
            log.error("I/O Fehler", exc);
            return result;
        } catch (Exception exc) {
            log.error("Allgemeiner Fehler", exc);
            return result;
        } finally {
        }
    }
}
