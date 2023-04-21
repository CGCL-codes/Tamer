package hoplugins.nthrf.parser;

import hoplugins.Nthrf;
import hoplugins.nthrf.data.NtPlayer;
import hoplugins.nthrf.util.NthrfUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import plugins.IDownloadHelper;
import plugins.IXMLParser;

public class NtPlayersParser {

    private String fetchedDate;

    private long teamId;

    private String teamName;

    private List<Long> playerIds = new ArrayList<Long>();

    private List<NtPlayer> players = new ArrayList<NtPlayer>();

    private boolean parsingSuccess;

    /**
	 * Parse player details and store the IDs and Players in local objects.
	 */
    public NtPlayersParser(IXMLParser xm, String xmlData, IDownloadHelper dh, HashMap<Integer, Integer> countryMapping) {
        Document doc = xm.parseString(xmlData);
        parseBasics(xm, doc);
        if (Nthrf.DEBUG) {
            playerIds = new ArrayList<Long>();
            playerIds.add(new Long(153597129));
            playerIds.add(new Long(89777865));
            playerIds.add(new Long(182954291));
            playerIds.add(new Long(130802618));
            playerIds.add(new Long(210382341));
            playerIds.add(new Long(207144770));
            playerIds.add(new Long(174170065));
            playerIds.add(new Long(224794056));
            playerIds.add(new Long(207144082));
            playerIds.add(new Long(67555898));
            playerIds.add(new Long(69139809));
            playerIds.add(new Long(183010066));
            playerIds.add(new Long(198329345));
            playerIds.add(new Long(78157003));
            playerIds.add(new Long(92313781));
            playerIds.add(new Long(123214852));
            playerIds.add(new Long(90534721));
        }
        parsePlayerDetails(xm, dh, countryMapping);
    }

    private void parsePlayerDetails(IXMLParser xm, IDownloadHelper dh, HashMap<Integer, Integer> countryMapping) {
        try {
            for (Iterator<Long> i = playerIds.iterator(); i.hasNext(); ) {
                Long playerId = i.next();
                String xmlData = dh.getHattrickXMLFile("/chppxml.axd?file=playerdetails&playerId=" + playerId);
                Element ele = xm.parseString(xmlData).getDocumentElement();
                ele = (Element) ele.getElementsByTagName("Player").item(0);
                players.add(createPlayer(ele, countryMapping));
            }
        } catch (Exception e) {
            parsingSuccess = false;
            e.printStackTrace();
        }
    }

    protected final NtPlayer createPlayer(Element ele, HashMap<Integer, Integer> countryMapping) throws Exception {
        Element tmp = null;
        final NtPlayer player = new NtPlayer();
        tmp = (Element) ele.getElementsByTagName("PlayerID").item(0);
        player.setPlayerId(Long.parseLong(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("PlayerName").item(0);
        player.setName(tmp.getFirstChild().getNodeValue());
        try {
            tmp = (Element) ele.getElementsByTagName("PlayerNumber").item(0);
            player.setShirtNumber(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        } catch (Exception e) {
        }
        tmp = (Element) ele.getElementsByTagName("TSI").item(0);
        player.setTsi(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("PlayerForm").item(0);
        player.setForm(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("Age").item(0);
        player.setAgeYears(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("AgeDays").item(0);
        player.setAgeDays(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("Experience").item(0);
        player.setXp(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("Leadership").item(0);
        player.setLeaderShip(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("Specialty").item(0);
        player.setSpeciality(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("TransferListed").item(0);
        player.setTranferlisted(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("NativeLeagueID").item(0);
        int nativeLeagueId = Integer.parseInt(tmp.getFirstChild().getNodeValue());
        player.setNativeLeagueId(nativeLeagueId);
        player.setCountryId(NthrfUtil.getCountryId(nativeLeagueId, countryMapping));
        player.setTrainer(false);
        player.setTrainerSkill(7);
        player.setTrainerType(2);
        try {
            if (ele.getElementsByTagName("TrainerType") != null && ele.getElementsByTagName("TrainerType").getLength() > 0) {
                tmp = (Element) ele.getElementsByTagName("TrainerType").item(0);
                player.setTrainerType(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
                tmp = (Element) ele.getElementsByTagName("TrainerSkill").item(0);
                player.setTrainerSkill(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
                player.setTrainer(true);
            }
        } catch (Exception e) {
            System.out.println("Error in Trainercheck: " + player.getPlayerId() + ", " + player.getName() + " - " + e);
        }
        tmp = (Element) ele.getElementsByTagName("Salary").item(0);
        player.setSalary(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("Agreeability").item(0);
        player.setAgreeability(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("Aggressiveness").item(0);
        player.setAggressiveness(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("Honesty").item(0);
        player.setHonesty(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("Caps").item(0);
        player.setCaps(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        tmp = (Element) ele.getElementsByTagName("CapsU20").item(0);
        player.setCapsU20(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        try {
            tmp = (Element) ele.getElementsByTagName("Cards").item(0);
            player.setYellowCards(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        } catch (Exception e) {
            System.out.println("Cant get cards: " + player.getPlayerId() + ", " + player.getName() + " - " + e);
            player.setYellowCards(0);
        }
        try {
            tmp = (Element) ele.getElementsByTagName("InjuryLevel").item(0);
            player.setInjury(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        } catch (Exception e) {
            System.out.println("Cant get injury: " + player.getPlayerId() + ", " + player.getName() + " - " + e);
            player.setInjury(0);
        }
        try {
            tmp = (Element) ele.getElementsByTagName("CareerGoals").item(0);
            player.setCareerGoals(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("CareerHattricks").item(0);
            player.setCareerHattricks(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("LeagueGoals").item(0);
            player.setLeagueGoals(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        } catch (Exception e) {
            System.out.println("Cant get goals++: " + player.getPlayerId() + ", " + player.getName() + " - " + e);
            player.setCareerGoals(0);
            player.setCareerHattricks(0);
            player.setLeagueGoals(0);
        }
        try {
            tmp = (Element) ele.getElementsByTagName("StaminaSkill").item(0);
            player.setStaminaSkill(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("KeeperSkill").item(0);
            player.setKeeperSkill(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("PlaymakerSkill").item(0);
            player.setPlaymakerSkill(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("ScorerSkill").item(0);
            player.setScorerSkill(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("PassingSkill").item(0);
            player.setPassingSkill(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("WingerSkill").item(0);
            player.setWingerSkill(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("DefenderSkill").item(0);
            player.setDefenderSkill(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
            tmp = (Element) ele.getElementsByTagName("SetPiecesSkill").item(0);
            player.setSetPiecesSkill(Integer.parseInt(tmp.getFirstChild().getNodeValue()));
        } catch (Exception e) {
            System.out.println("Cant get skills: " + player.getPlayerId() + ", " + player.getName() + " - " + e);
            e.printStackTrace();
        }
        return player;
    }

    private void parseBasics(IXMLParser xm, Document doc) {
        if (doc == null) {
            return;
        }
        try {
            Element root = doc.getDocumentElement();
            Element ele = (Element) root.getElementsByTagName("FetchedDate").item(0);
            fetchedDate = xm.getFirstChildNodeValue(ele);
            ele = (Element) root.getElementsByTagName("TeamID").item(0);
            teamId = Long.parseLong(xm.getFirstChildNodeValue(ele));
            ele = (Element) root.getElementsByTagName("TeamName").item(0);
            teamName = xm.getFirstChildNodeValue(ele);
            root = (Element) root.getElementsByTagName("Players").item(0);
            NodeList playersNode = root.getElementsByTagName("Player");
            for (int m = 0; (playersNode != null && m < playersNode.getLength()); m++) {
                ele = (Element) playersNode.item(m);
                ele = (Element) ele.getElementsByTagName("PlayerID").item(0);
                playerIds.add(new Long(Long.parseLong(xm.getFirstChildNodeValue(ele))));
            }
            parsingSuccess = true;
        } catch (Exception e) {
            parsingSuccess = false;
            e.printStackTrace();
        }
    }

    public String getFetchedDate() {
        return fetchedDate;
    }

    public long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public List<Long> getPlayerIds() {
        return playerIds;
    }

    public List<NtPlayer> getAllPlayers() {
        return players;
    }

    public boolean isParsingSuccess() {
        return parsingSuccess;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("NtPlayers (from " + fetchedDate + "), parsingSuccess: " + parsingSuccess);
        sb.append("\n\tTeam: " + teamName + " (" + teamId + ")");
        sb.append("\n\tPlayer IDs(" + playerIds.size() + "):");
        int m = 1;
        for (Iterator<Long> i = playerIds.iterator(); i.hasNext(); m++) {
            sb.append("\n\t\t" + m + ". " + i.next());
        }
        sb.append("\n---------------------------------------------");
        m = 1;
        for (Iterator<NtPlayer> i = players.iterator(); i.hasNext(); m++) {
            sb.append("\n\t" + m + ". " + i.next());
        }
        return sb.toString();
    }
}
