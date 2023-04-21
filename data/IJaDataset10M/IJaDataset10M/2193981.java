package de.hattrickorganizer.logik.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import de.hattrickorganizer.model.series.LigaTabelle;
import de.hattrickorganizer.model.series.LigaTabellenEintrag;
import de.hattrickorganizer.tools.HOLogger;
import de.hattrickorganizer.tools.xml.XMLManager;

/**
 * DOCUMENT ME!
 *
 * @author thomas.werth
 */
public class XMLTabelleParser {

    /**
     * Creates a new instance of xmlTabelleParser
     */
    public XMLTabelleParser() {
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param dateiname TODO Missing Method Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final LigaTabelle parseTabelle(String dateiname) {
        LigaTabelle tbl = null;
        Document doc = null;
        doc = XMLManager.instance().parseFile(dateiname);
        tbl = createTabelle(doc);
        return tbl;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param ele TODO Missing Method Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    protected final LigaTabellenEintrag createEintrag(Element ele) {
        Element tmp = null;
        final LigaTabellenEintrag lte = new LigaTabellenEintrag();
        try {
            tmp = (Element) ele.getElementsByTagName("TeamID").item(0);
            lte.setTeamId(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("Position").item(0);
            lte.setPosition(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("Matches").item(0);
            lte.setAnzSpiele(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("GoalsFor").item(0);
            lte.setToreFuer(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("GoalsAgainst").item(0);
            lte.setToreGegen(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("Points").item(0);
            lte.setPunkte(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("TeamName").item(0);
            lte.setTeamName(tmp.getFirstChild().getNodeValue());
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), "XMLTabelleParser.createEintrag : " + e);
            HOLogger.instance().log(getClass(), e);
        }
        return lte;
    }

    protected final LigaTabelle createTabelle(Document doc) {
        Element ele = null;
        Element root = null;
        final LigaTabelle tbl = new LigaTabelle();
        NodeList list = null;
        if (doc == null) {
            return tbl;
        }
        root = doc.getDocumentElement();
        try {
            ele = (Element) root.getElementsByTagName("LeagueName").item(0);
            tbl.setLigaName(ele.getFirstChild().getNodeValue());
            ele = (Element) root.getElementsByTagName("LeagueID").item(0);
            tbl.setLigaLandId(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("LeagueName").item(0);
            tbl.setLigaLandName(ele.getFirstChild().getNodeValue());
            ele = (Element) root.getElementsByTagName("LeagueLevel").item(0);
            tbl.setSpielklasse(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("MaxLevel").item(0);
            tbl.setMaxAnzahlSpielklassen(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("LeagueLevelUnitID").item(0);
            tbl.setLigaId(Integer.parseInt(ele.getFirstChild().getNodeValue()));
            ele = (Element) root.getElementsByTagName("LeagueLevelUnitName").item(0);
            tbl.setLigaLandName(ele.getFirstChild().getNodeValue());
            list = root.getElementsByTagName("Team");
            for (int i = 0; (list != null) && (i < list.getLength()); i++) {
                tbl.addEintrag(createEintrag((Element) list.item(i)));
            }
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), "XMLTabelleParser.createTabelle : " + e);
            HOLogger.instance().log(getClass(), e);
        }
        return tbl;
    }
}
