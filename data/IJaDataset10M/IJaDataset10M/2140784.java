package net.sf.l2j.gameserver.model.actor.instance;

import java.util.Calendar;
import java.util.List;
import javolution.text.TextBuilder;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.SevenSignsFestival;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * Festival of Darkness Guide (Seven Signs)
 *
 * @author Tempy
 */
public final class L2FestivalGuideInstance extends L2FolkInstance {

    protected int _festivalType;

    protected int _festivalOracle;

    protected int _blueStonesNeeded;

    protected int _greenStonesNeeded;

    protected int _redStonesNeeded;

    /**
	 * @param template
	 */
    public L2FestivalGuideInstance(int objectId, L2NpcTemplate template) {
        super(objectId, template);
        switch(getNpcId()) {
            case 31127:
            case 31132:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_31;
                _festivalOracle = SevenSigns.CABAL_DAWN;
                _blueStonesNeeded = 900;
                _greenStonesNeeded = 540;
                _redStonesNeeded = 270;
                break;
            case 31128:
            case 31133:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_42;
                _festivalOracle = SevenSigns.CABAL_DAWN;
                _blueStonesNeeded = 1500;
                _greenStonesNeeded = 900;
                _redStonesNeeded = 450;
                break;
            case 31129:
            case 31134:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_53;
                _festivalOracle = SevenSigns.CABAL_DAWN;
                _blueStonesNeeded = 3000;
                _greenStonesNeeded = 1800;
                _redStonesNeeded = 900;
                break;
            case 31130:
            case 31135:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_64;
                _festivalOracle = SevenSigns.CABAL_DAWN;
                _blueStonesNeeded = 4500;
                _greenStonesNeeded = 2700;
                _redStonesNeeded = 1350;
                break;
            case 31131:
            case 31136:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_NONE;
                _festivalOracle = SevenSigns.CABAL_DAWN;
                _blueStonesNeeded = 6000;
                _greenStonesNeeded = 3600;
                _redStonesNeeded = 1800;
                break;
            case 31137:
            case 31142:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_31;
                _festivalOracle = SevenSigns.CABAL_DUSK;
                _blueStonesNeeded = 900;
                _greenStonesNeeded = 540;
                _redStonesNeeded = 270;
                break;
            case 31138:
            case 31143:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_42;
                _festivalOracle = SevenSigns.CABAL_DUSK;
                _blueStonesNeeded = 1500;
                _greenStonesNeeded = 900;
                _redStonesNeeded = 450;
                break;
            case 31139:
            case 31144:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_53;
                _festivalOracle = SevenSigns.CABAL_DUSK;
                _blueStonesNeeded = 3000;
                _greenStonesNeeded = 1800;
                _redStonesNeeded = 900;
                break;
            case 31140:
            case 31145:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_64;
                _festivalOracle = SevenSigns.CABAL_DUSK;
                _blueStonesNeeded = 4500;
                _greenStonesNeeded = 2700;
                _redStonesNeeded = 1350;
                break;
            case 31141:
            case 31146:
                _festivalType = SevenSignsFestival.FESTIVAL_LEVEL_MAX_NONE;
                _festivalOracle = SevenSigns.CABAL_DUSK;
                _blueStonesNeeded = 6000;
                _greenStonesNeeded = 3600;
                _redStonesNeeded = 1800;
                break;
        }
    }

    @Override
    public void onBypassFeedback(L2PcInstance player, String command) {
        if (command.startsWith("FestivalDesc")) {
            int val = Integer.parseInt(command.substring(13));
            showChatWindow(player, val, null, true);
        } else if (command.startsWith("Festival")) {
            L2Party playerParty = player.getParty();
            int val = Integer.parseInt(command.substring(9, 10));
            switch(val) {
                case 1:
                    if (SevenSigns.getInstance().isSealValidationPeriod()) {
                        showChatWindow(player, 2, "a", false);
                        return;
                    }
                    if (SevenSignsFestival.getInstance().isFestivalInitialized()) {
                        player.sendMessage("You cannot sign up while a festival is in progress.");
                        return;
                    }
                    if (playerParty == null) {
                        showChatWindow(player, 2, "b", false);
                        return;
                    }
                    if (!playerParty.isLeader(player)) {
                        showChatWindow(player, 2, "c", false);
                        return;
                    }
                    if (playerParty.getMemberCount() < Config.ALT_FESTIVAL_MIN_PLAYER) {
                        showChatWindow(player, 2, "b", false);
                        return;
                    }
                    if (playerParty.getLevel() > SevenSignsFestival.getMaxLevelForFestival(_festivalType)) {
                        showChatWindow(player, 2, "d", false);
                        return;
                    }
                    if (player.isFestivalParticipant()) {
                        SevenSignsFestival.getInstance().setParticipants(_festivalOracle, _festivalType, playerParty);
                        showChatWindow(player, 2, "f", false);
                        return;
                    }
                    showChatWindow(player, 1, null, false);
                    break;
                case 2:
                    int stoneType = Integer.parseInt(command.substring(11));
                    int stonesNeeded = 0;
                    switch(stoneType) {
                        case SevenSigns.SEAL_STONE_BLUE_ID:
                            stonesNeeded = _blueStonesNeeded;
                            break;
                        case SevenSigns.SEAL_STONE_GREEN_ID:
                            stonesNeeded = _greenStonesNeeded;
                            break;
                        case SevenSigns.SEAL_STONE_RED_ID:
                            stonesNeeded = _redStonesNeeded;
                            break;
                    }
                    if (!player.destroyItemByItemId("SevenSigns", stoneType, stonesNeeded, this, true)) return;
                    SevenSignsFestival.getInstance().setParticipants(_festivalOracle, _festivalType, playerParty);
                    SevenSignsFestival.getInstance().addAccumulatedBonus(_festivalType, stoneType, stonesNeeded);
                    showChatWindow(player, 2, "e", false);
                    break;
                case 3:
                    if (SevenSigns.getInstance().isSealValidationPeriod()) {
                        showChatWindow(player, 3, "a", false);
                        return;
                    }
                    if (SevenSignsFestival.getInstance().isFestivalInProgress()) {
                        player.sendMessage("You cannot register a score while a festival is in progress.");
                        return;
                    }
                    if (playerParty == null) {
                        showChatWindow(player, 3, "b", false);
                        return;
                    }
                    List<L2PcInstance> prevParticipants = SevenSignsFestival.getInstance().getPreviousParticipants(_festivalOracle, _festivalType);
                    if (prevParticipants == null) return;
                    if (!prevParticipants.contains(player)) {
                        showChatWindow(player, 3, "b", false);
                        return;
                    }
                    if (player.getObjectId() != prevParticipants.get(0).getObjectId()) {
                        showChatWindow(player, 3, "b", false);
                        return;
                    }
                    L2ItemInstance bloodOfferings = player.getInventory().getItemByItemId(SevenSignsFestival.FESTIVAL_OFFERING_ID);
                    int offeringCount = 0;
                    if (bloodOfferings == null) {
                        player.sendMessage("You do not have any blood offerings to contribute.");
                        return;
                    }
                    offeringCount = bloodOfferings.getCount();
                    int offeringScore = offeringCount * SevenSignsFestival.FESTIVAL_OFFERING_VALUE;
                    boolean isHighestScore = SevenSignsFestival.getInstance().setFinalScore(player, _festivalOracle, _festivalType, offeringScore);
                    player.destroyItem("SevenSigns", bloodOfferings, this, false);
                    SystemMessage sm = new SystemMessage(SystemMessageId.CONTRIB_SCORE_INCREASED);
                    sm.addNumber(offeringScore);
                    player.sendPacket(sm);
                    if (isHighestScore) showChatWindow(player, 3, "c", false); else showChatWindow(player, 3, "d", false);
                    break;
                case 4:
                    TextBuilder strBuffer = new TextBuilder("<html><body>Festival Guide:<br>These are the top scores of the week, for the ");
                    final StatsSet dawnData = SevenSignsFestival.getInstance().getHighestScoreData(SevenSigns.CABAL_DAWN, _festivalType);
                    final StatsSet duskData = SevenSignsFestival.getInstance().getHighestScoreData(SevenSigns.CABAL_DUSK, _festivalType);
                    final StatsSet overallData = SevenSignsFestival.getInstance().getOverallHighestScoreData(_festivalType);
                    final int dawnScore = dawnData.getInteger("score");
                    final int duskScore = duskData.getInteger("score");
                    int overallScore = 0;
                    if (overallData != null) overallScore = overallData.getInteger("score");
                    strBuffer.append(SevenSignsFestival.getFestivalName(_festivalType) + " festival.<br>");
                    if (dawnScore > 0) strBuffer.append("Dawn: " + calculateDate(dawnData.getString("date")) + ". Score " + dawnScore + "<br>" + dawnData.getString("members") + "<br>"); else strBuffer.append("Dawn: No record exists. Score 0<br>");
                    if (duskScore > 0) strBuffer.append("Dusk: " + calculateDate(duskData.getString("date")) + ". Score " + duskScore + "<br>" + duskData.getString("members") + "<br>"); else strBuffer.append("Dusk: No record exists. Score 0<br>");
                    if (overallScore > 0) {
                        String cabalStr = "Children of Dusk";
                        if (overallData.getString("cabal").equals("dawn")) cabalStr = "Children of Dawn";
                        strBuffer.append("Consecutive top scores: " + calculateDate(overallData.getString("date")) + ". Score " + overallScore + "<br>Affilated side: " + cabalStr + "<br>" + overallData.getString("members") + "<br>");
                    } else strBuffer.append("Consecutive top scores: No record exists. Score 0<br>");
                    strBuffer.append("<a action=\"bypass -h npc_" + getObjectId() + "_Chat 0\">Go back.</a></body></html>");
                    NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
                    html.setHtml(strBuffer.toString());
                    player.sendPacket(html);
                    break;
                case 8:
                    if (playerParty == null) return;
                    if (!SevenSignsFestival.getInstance().isFestivalInProgress()) return;
                    if (!playerParty.isLeader(player)) {
                        showChatWindow(player, 8, "a", false);
                        break;
                    }
                    if (SevenSignsFestival.getInstance().increaseChallenge(_festivalOracle, _festivalType)) showChatWindow(player, 8, "b", false); else showChatWindow(player, 8, "c", false);
                    break;
                case 9:
                    if (playerParty == null) return;
                    boolean isLeader = playerParty.isLeader(player);
                    if (isLeader) SevenSignsFestival.getInstance().updateParticipants(player, null); else {
                        SevenSignsFestival.getInstance().updateParticipants(player, playerParty);
                        playerParty.removePartyMember(player);
                    }
                    break;
                case 0:
                    if (!SevenSigns.getInstance().isSealValidationPeriod()) {
                        player.sendMessage("Bonuses cannot be paid during the competition period.");
                        return;
                    }
                    if (SevenSignsFestival.getInstance().distribAccumulatedBonus(player) > 0) showChatWindow(player, 0, "a", false); else showChatWindow(player, 0, "b", false);
                    break;
                default:
                    showChatWindow(player, val, null, false);
            }
        } else super.onBypassFeedback(player, command);
    }

    private void showChatWindow(L2PcInstance player, int val, String suffix, boolean isDescription) {
        String filename = SevenSigns.SEVEN_SIGNS_HTML_PATH + "festival/";
        filename += isDescription ? "desc_" : "festival_";
        filename += suffix != null ? val + suffix + ".htm" : val + ".htm";
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(filename);
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%festivalType%", SevenSignsFestival.getFestivalName(_festivalType));
        html.replace("%cycleMins%", String.valueOf(SevenSignsFestival.getInstance().getMinsToNextCycle()));
        if (!isDescription && "2b".equals(val + suffix)) html.replace("%minFestivalPartyMembers%", String.valueOf(Config.ALT_FESTIVAL_MIN_PLAYER));
        if (val == 5) html.replace("%statsTable%", getStatsTable());
        if (val == 6) html.replace("%bonusTable%", getBonusTable());
        if (val == 1) {
            html.replace("%blueStoneNeeded%", String.valueOf(_blueStonesNeeded));
            html.replace("%greenStoneNeeded%", String.valueOf(_greenStonesNeeded));
            html.replace("%redStoneNeeded%", String.valueOf(_redStonesNeeded));
        }
        player.sendPacket(html);
        player.sendPacket(ActionFailed.STATIC_PACKET);
    }

    private final String getStatsTable() {
        TextBuilder tableHtml = new TextBuilder();
        for (int i = 0; i < 5; i++) {
            int dawnScore = SevenSignsFestival.getInstance().getHighestScore(SevenSigns.CABAL_DAWN, i);
            int duskScore = SevenSignsFestival.getInstance().getHighestScore(SevenSigns.CABAL_DUSK, i);
            String festivalName = SevenSignsFestival.getFestivalName(i);
            String winningCabal = "Children of Dusk";
            if (dawnScore > duskScore) winningCabal = "Children of Dawn"; else if (dawnScore == duskScore) winningCabal = "None";
            tableHtml.append("<tr><td width=\"100\" align=\"center\">" + festivalName + "</td><td align=\"center\" width=\"35\">" + duskScore + "</td><td align=\"center\" width=\"35\">" + dawnScore + "</td><td align=\"center\" width=\"130\">" + winningCabal + "</td></tr>");
        }
        return tableHtml.toString();
    }

    private final String getBonusTable() {
        TextBuilder tableHtml = new TextBuilder();
        for (int i = 0; i < 5; i++) {
            int accumScore = SevenSignsFestival.getInstance().getAccumulatedBonus(i);
            String festivalName = SevenSignsFestival.getFestivalName(i);
            tableHtml.append("<tr><td align=\"center\" width=\"150\">" + festivalName + "</td><td align=\"center\" width=\"150\">" + accumScore + "</td></tr>");
        }
        return tableHtml.toString();
    }

    private final String calculateDate(String milliFromEpoch) {
        long numMillis = Long.valueOf(milliFromEpoch);
        Calendar calCalc = Calendar.getInstance();
        calCalc.setTimeInMillis(numMillis);
        return calCalc.get(Calendar.YEAR) + "/" + calCalc.get(Calendar.MONTH) + "/" + calCalc.get(Calendar.DAY_OF_MONTH);
    }
}
