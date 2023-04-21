package ho.core.file.extension;

import ho.core.constants.player.PlayerAbility;
import ho.core.constants.player.PlayerSkill;
import ho.core.db.DBManager;
import ho.core.epv.EPVData;
import ho.core.file.xml.XMLManager;
import ho.core.model.HOVerwaltung;
import ho.core.model.XtraData;
import ho.core.model.misc.Basics;
import ho.core.model.player.ISkillup;
import ho.core.model.player.Spieler;
import ho.core.model.player.SpielerPosition;
import ho.core.training.FutureTrainingManager;
import ho.core.training.TrainingPerWeek;
import ho.core.training.TrainingManager;
import ho.core.util.HOLogger;
import ho.module.training.FutureTrainingWeek;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PlayerCreator extends XMLCreator {

    private static int teamId = HOVerwaltung.instance().getModel().getBasics().getTeamId();

    private static DecimalFormat df = new DecimalFormat("#,###");

    protected static void extractActual() {
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
            Element root = doc.createElement("info");
            doc.appendChild(root);
            addRoster(root, HOVerwaltung.instance().getModel().getID(), true, 0);
            File playerFile = new File(dir, "players.xml");
            BufferedWriter bw = new BufferedWriter(new FileWriter(playerFile));
            bw.write(XMLManager.instance().getXML(doc));
            bw.flush();
            bw.close();
        } catch (Exception e) {
            HOLogger.instance().log(PlayerCreator.class, e);
        }
    }

    protected static void extractHistoric() {
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
            Vector<TrainingPerWeek> l = TrainingManager.instance().getTrainingsVector();
            int oldWeek = 0;
            for (int index = l.size(); index > 0; index--) {
                TrainingPerWeek tpw = (TrainingPerWeek) l.get(index - 1);
                oldWeek = addRoster(root, tpw.getPreviousHrfId(), false, oldWeek);
            }
            File dbFile = new File(dir, "playersdb.xml");
            BufferedWriter bw = new BufferedWriter(new FileWriter(dbFile));
            bw.write(XMLManager.instance().getXML(doc));
            bw.flush();
            bw.close();
        } catch (Exception e) {
            HOLogger.instance().log(PlayerCreator.class, e);
        }
    }

    private static int addRoster(Element root, int hrfId, boolean extended, int oldWeek) throws IOException {
        List<Spieler> players = DBManager.instance().getSpieler(hrfId);
        Basics basics = DBManager.instance().getBasics(hrfId);
        XtraData xtradata = DBManager.instance().getXtraDaten(hrfId);
        int actualSeason = basics.getSeason();
        int actualWeek = basics.getSpieltag();
        try {
            if (xtradata.getTrainingDate().after(xtradata.getSeriesMatchDate())) {
                actualWeek++;
                if (actualWeek == 17) {
                    actualWeek = 1;
                    actualSeason++;
                }
            }
        } catch (Exception e1) {
        }
        int tmpWeek = actualWeek + actualSeason * 16;
        if (tmpWeek == oldWeek) {
            return tmpWeek;
        }
        Document doc = root.getOwnerDocument();
        Element roster = doc.createElement("roster");
        root.appendChild(roster);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(basics.getDatum().getTime());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        roster.appendChild(createNode(doc, "date", dayFormat.format(c.getTime())));
        roster.appendChild(createNode(doc, "time", timeFormat.format(c.getTime())));
        roster.appendChild(createNode(doc, "week", tmpWeek + ""));
        Element playersTag = doc.createElement("players");
        roster.appendChild(playersTag);
        for (Iterator<Spieler> iter = players.iterator(); iter.hasNext(); ) {
            Spieler element = iter.next();
            addPlayer(playersTag, element, extended);
        }
        return tmpWeek;
    }

    private static void addPlayer(Element playersTag, Spieler player, boolean extended) throws IOException {
        Document doc = playersTag.getOwnerDocument();
        Element playerTag = doc.createElement("player");
        playersTag.appendChild(playerTag);
        playerTag.appendChild(createNode(doc, "id", player.getSpielerID() + ""));
        playerTag.appendChild(createNode(doc, "name", player.getName() + ""));
        final int salary = (int) (player.getGehalt() / ho.core.model.UserParameter.instance().faktorGeld);
        playerTag.appendChild(createNode(doc, "salary", salary + ""));
        playerTag.appendChild(createNode(doc, "nationality", player.getNationalitaet() + ""));
        playerTag.appendChild(createNode(doc, "match", player.getBewertung() + ""));
        playerTag.appendChild(createNode(doc, "lastmatch", player.getLetzteBewertung() + ""));
        Element bestposition = doc.createElement("bestposition");
        playerTag.appendChild(bestposition);
        bestposition.appendChild(createNode(doc, "role", SpielerPosition.getNameForPosition(player.getIdealPosition()) + ""));
        bestposition.appendChild(createNode(doc, "value", player.calcPosValue(player.getIdealPosition(), true) + ""));
        bestposition.appendChild(createNode(doc, "code", player.getIdealPosition() + ""));
        EPVData data = HOVerwaltung.instance().getModel().getEPV().getEPVData(player);
        double price = HOVerwaltung.instance().getModel().getEPV().getPrice(data);
        playerTag.appendChild(createNode(doc, "epv", df.format(price)));
        Element skill = doc.createElement("skill");
        playerTag.appendChild(skill);
        skill.appendChild(createNode(doc, "playmaking", (player.getSpielaufbau() + player.getSubskill4SkillWithOffset(PlayerSkill.PLAYMAKING)) + ""));
        skill.appendChild(createNode(doc, "passing", (player.getPasspiel() + player.getSubskill4SkillWithOffset(PlayerSkill.PASSING)) + ""));
        skill.appendChild(createNode(doc, "cross", (player.getFluegelspiel() + player.getSubskill4SkillWithOffset(PlayerSkill.WINGER)) + ""));
        skill.appendChild(createNode(doc, "defense", (player.getVerteidigung() + player.getSubskill4SkillWithOffset(PlayerSkill.DEFENDING)) + ""));
        skill.appendChild(createNode(doc, "attack", (player.getTorschuss() + player.getSubskill4SkillWithOffset(PlayerSkill.SCORING)) + ""));
        skill.appendChild(createNode(doc, "setpieces", (player.getStandards() + player.getSubskill4SkillWithOffset(PlayerSkill.SET_PIECES)) + ""));
        skill.appendChild(createNode(doc, "keeper", (player.getTorwart() + player.getSubskill4SkillWithOffset(PlayerSkill.KEEPER)) + ""));
        skill.appendChild(createNode(doc, "stamina", (player.getKondition() + player.getSubskill4SkillWithOffset(PlayerSkill.STAMINA)) + ""));
        skill.appendChild(createNode(doc, "form", player.getForm() + ""));
        skill.appendChild(createNode(doc, "experience", player.getErfahrung() + ""));
        skill.appendChild(createNode(doc, "tsi", player.getTSI() + ""));
        if (extended) {
            Element skillups = doc.createElement("skillups");
            playerTag.appendChild(skillups);
            int coTrainer = HOVerwaltung.instance().getModel().getVerein().getCoTrainer();
            int trainer = HOVerwaltung.instance().getModel().getTrainer().getTrainer();
            List<FutureTrainingWeek> futures = DBManager.instance().getFutureTrainingsVector();
            FutureTrainingManager ftm = new FutureTrainingManager(player, futures, coTrainer, trainer);
            List<ISkillup> futureSkillups = ftm.getFutureSkillups();
            for (Iterator<ISkillup> iterator = futureSkillups.iterator(); iterator.hasNext(); ) {
                ISkillup skillup = iterator.next();
                Element skillupTag = doc.createElement("skillup");
                skillups.appendChild(skillupTag);
                skillupTag.appendChild(createNode(doc, "week", skillup.getHtSeason() + "/" + skillup.getHtWeek()));
                skillupTag.appendChild(createNode(doc, "skill", skillup.getType() + ""));
                skillupTag.appendChild(createNode(doc, "skillDesc", PlayerSkill.toString(skillup.getType())));
                skillupTag.appendChild(createNode(doc, "value", skillup.getValue() + ""));
                skillupTag.appendChild(createNode(doc, "valueDesc", PlayerAbility.getNameForSkill(skillup.getValue(), false)));
            }
        }
    }
}
