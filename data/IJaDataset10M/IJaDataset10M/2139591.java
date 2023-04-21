package ho.core.file.xml;

import ho.core.model.misc.Verein;
import ho.core.util.HOLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * DOCUMENT ME!
 *
 * @author thomas.werth
 */
public class XMLClubParser {

    /**
     * Creates a new instance of XMLClubParser
     */
    public XMLClubParser() {
    }

    public final Verein parseClub(String dateiname) {
        Verein club = null;
        Document doc = null;
        doc = XMLManager.instance().parseFile(dateiname);
        club = parseVerein(doc);
        return club;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param datei TODO Missing Method Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final Verein parseClub(java.io.File datei) {
        Document doc = null;
        doc = XMLManager.instance().parseFile(datei);
        return parseVerein(doc);
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param inputStream TODO Missing Method Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final java.util.Hashtable<?, ?> parseClubFromString(String inputStream) {
        Document doc = null;
        doc = XMLManager.instance().parseString(inputStream);
        return parseDetails(doc);
    }

    /**
     * erstellt das MAtchlineup Objekt
     *
     * @param doc TODO Missing Constructuor Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    protected final java.util.Hashtable<?, ?> parseDetails(Document doc) {
        Element ele = null;
        Element root = null;
        ho.core.file.xml.MyHashtable club = new ho.core.file.xml.MyHashtable();
        if (doc == null) {
            return club;
        }
        root = doc.getDocumentElement();
        try {
            ele = (Element) root.getElementsByTagName("FetchedDate").item(0);
            club.put("FetchedDate", XMLManager.instance().getFirstChildNodeValue(ele));
            root = (Element) root.getElementsByTagName("Team").item(0);
            ele = (Element) root.getElementsByTagName("TeamID").item(0);
            club.put("TeamID", XMLManager.instance().getFirstChildNodeValue(ele));
            ele = (Element) root.getElementsByTagName("TeamName").item(0);
            club.put("TeamName", XMLManager.instance().getFirstChildNodeValue(ele));
            root = (Element) doc.getDocumentElement().getElementsByTagName("Specialists").item(0);
            ele = (Element) root.getElementsByTagName("Doctors").item(0);
            club.put("Doctors", XMLManager.instance().getFirstChildNodeValue(ele));
            ele = (Element) root.getElementsByTagName("PressSpokesmen").item(0);
            club.put("PressSpokesmen", XMLManager.instance().getFirstChildNodeValue(ele));
            ele = (Element) root.getElementsByTagName("AssistantTrainers").item(0);
            club.put("AssistantTrainers", XMLManager.instance().getFirstChildNodeValue(ele));
            ele = (Element) root.getElementsByTagName("Physiotherapists").item(0);
            club.put("Physiotherapists", XMLManager.instance().getFirstChildNodeValue(ele));
            ele = (Element) root.getElementsByTagName("Psychologists").item(0);
            club.put("Psychologists", XMLManager.instance().getFirstChildNodeValue(ele));
            root = (Element) doc.getDocumentElement().getElementsByTagName("YouthSquad").item(0);
            ele = (Element) root.getElementsByTagName("Investment").item(0);
            club.put("Investment", XMLManager.instance().getFirstChildNodeValue(ele));
            ele = (Element) root.getElementsByTagName("HasPromoted").item(0);
            club.put("HasPromoted", XMLManager.instance().getFirstChildNodeValue(ele));
            ele = (Element) root.getElementsByTagName("YouthLevel").item(0);
            club.put("YouthLevel", XMLManager.instance().getFirstChildNodeValue(ele));
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), "XMLClubParser.createVerein Exception gefangen: " + e);
            HOLogger.instance().log(getClass(), e);
            club = null;
        }
        return club;
    }

    /**
     * erstellt das MAtchlineup Objekt
     *
     * @param doc TODO Missing Constructuor Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    protected final Verein parseVerein(Document doc) {
        Element ele = null;
        Element root = null;
        Verein club = new Verein();
        if (doc == null) {
            return club;
        }
        root = doc.getDocumentElement();
        try {
            ele = (Element) root.getElementsByTagName("FetchedDate").item(0);
            club.setDateFromString(ele.getFirstChild().getNodeValue());
            root = (Element) root.getElementsByTagName("Team").item(0);
            ele = (Element) root.getElementsByTagName("TeamID").item(0);
            club.setTeamID(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("TeamName").item(0);
            club.setTeamName(ele.getFirstChild().getNodeValue());
            root = (Element) doc.getDocumentElement().getElementsByTagName("Specialists").item(0);
            ele = (Element) root.getElementsByTagName("Doctors").item(0);
            club.setAerzte(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("PressSpokesmen").item(0);
            club.setPRManager(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("AssistantTrainers").item(0);
            club.setCoTrainer(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("Physiotherapists").item(0);
            club.setMasseure(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("Psychologists").item(0);
            club.setPsychologen(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            root = (Element) doc.getDocumentElement().getElementsByTagName("YouthSquad").item(0);
            ele = (Element) root.getElementsByTagName("Investment").item(0);
            club.setJugendGeld(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("HasPromoted").item(0);
            club.setYouthPull(Boolean.valueOf(ele.getFirstChild().getNodeValue()).booleanValue());
            ele = (Element) root.getElementsByTagName("YouthLevel").item(0);
            club.setJugend(Integer.parseInt(ele.getFirstChild().getNodeValue()));
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), "XMLClubParser.createVerein Exception gefangen: " + e);
            HOLogger.instance().log(getClass(), e);
            club = null;
        }
        return club;
    }
}
