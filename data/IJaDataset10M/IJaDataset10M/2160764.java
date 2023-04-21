package de.hattrickorganizer.tools.extension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import plugins.ISpielePanel;
import de.hattrickorganizer.database.StatisticQuery;
import de.hattrickorganizer.gui.model.ArenaStatistikModel;
import de.hattrickorganizer.gui.model.ArenaStatistikTableModel;
import de.hattrickorganizer.model.HOVerwaltung;
import de.hattrickorganizer.tools.HOLogger;
import de.hattrickorganizer.tools.xml.XMLManager;

public class StadiumCreator extends XMLCreator {

    private static int teamId = HOVerwaltung.instance().getModel().getBasics().getTeamId();

    public static void extractHistoric() {
        if (teamId == 0) {
            return;
        }
        File dir = new File("Info/" + teamId);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement("historic");
            doc.appendChild(root);
            ArenaStatistikTableModel model = StatisticQuery.getArenaStatistikModel(ISpielePanel.NUR_EIGENE_SPIELE);
            for (int i = 0; i < model.getMatches().length; i++) {
                ArenaStatistikModel match = model.getMatches()[i];
                addMatch(root, match);
            }
            File dbFile = new File(dir, "stadiumdb.xml");
            BufferedWriter bw = new BufferedWriter(new FileWriter(dbFile));
            bw.write(XMLManager.instance().getXML(doc));
            bw.flush();
            bw.close();
        } catch (Exception e) {
            HOLogger.instance().log(StadiumCreator.class, e);
        }
    }

    private static void addMatch(Element root, ArenaStatistikModel match) throws IOException {
        Document doc = root.getOwnerDocument();
        Element main = doc.createElement("match");
        root.appendChild(main);
        main.appendChild(createNode(doc, "homeName", match.getHeimName() + ""));
        main.appendChild(createNode(doc, "guestName", match.getGastName() + ""));
        main.appendChild(createNode(doc, "homeGoal", match.getHeimTore() + ""));
        main.appendChild(createNode(doc, "guestGoal", match.getGastTore() + ""));
        main.appendChild(createNode(doc, "fanNumber", match.getFans() + ""));
        main.appendChild(createNode(doc, "fanMorale", match.getFanZufriedenheit() + ""));
        main.appendChild(createNode(doc, "type", match.getMatchTyp() + ""));
        main.appendChild(createNode(doc, "matchid", match.getMatchID() + ""));
        main.appendChild(createNode(doc, "arenasize", match.getArenaGroesse() + ""));
        main.appendChild(createNode(doc, "date", match.getMatchDate() + ""));
        main.appendChild(createNode(doc, "wheater", match.getWetter() + ""));
        main.appendChild(createNode(doc, "attendance", match.getZuschaueranzahl() + ""));
        main.appendChild(createNode(doc, "liga", match.getLigaPlatz() + ""));
    }
}
