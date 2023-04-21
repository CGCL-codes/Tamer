package org.robocup.msl.refbox;

import java.util.LinkedHashMap;
import junit.framework.TestCase;
import org.robocup.msl.refbox.constants.CardColor;
import org.robocup.msl.refbox.constants.GameStage;
import org.robocup.msl.refbox.constants.Team;
import org.robocup.msl.refbox.data.CardData;
import org.robocup.msl.refbox.data.ConfigurationData;
import org.robocup.msl.refbox.data.TeamSetupData;
import org.robocup.msl.refbox.protocol2010.PlayerOutReason;

public class TestCaseGameControl extends TestCase {

    public void bestGameControl() {
        final GameControl gc = new GameControl();
        gc.setCancel();
        gc.setCorner(Team.TEAM_A);
        gc.setFreekick(Team.TEAM_A);
        gc.setPenalty(Team.TEAM_A);
        gc.setStart();
        gc.setStop();
        gc.setThrowin(Team.TEAM_B);
    }

    public void testCardAdministration() {
        final GameControl gc = new GameControl();
        Team team = Team.TEAM_A;
        TeamSetupData setup = new TeamSetupData();
        setup.setTeamLeader("teamLeader");
        setup.setTeamName("the A-Team");
        LinkedHashMap<String, Boolean> players = new LinkedHashMap<String, Boolean>();
        players.put("1", true);
        players.put("2", true);
        players.put("3", false);
        setup.setPlayers(players);
        gc.setTeamSetup(team, setup);
        CardData cardData = new CardData();
        cardData.setColor(CardColor.YELLOW);
        cardData.setGameStage(gc.getStage());
        cardData.setPlayer("1");
        cardData.setTimeOccurred("01:23");
        gc.addCard(team, cardData);
        assertTrue(gc.getCards(team).size() == 1);
        cardData.setPlayer("3");
        gc.addCard(team, cardData);
        assertTrue(gc.getCards(team).size() == 2);
        cardData.setPlayer("1");
        gc.removeCard(team, cardData);
        assertTrue(gc.getCards(team).size() == 1);
    }

    public void testReenterAllowed() throws InterruptedException {
        final GameControl gc = new GameControl();
        GameStage currentStage = GameStage.FIRST_HALF;
        String currentTime = "01:01";
        GameStage stageOccurred = GameStage.FIRST_HALF;
        String timeOccurred = "01:00";
        int minOutFieldTime = 60;
        boolean allowed;
        PlayerOutReason reasonOut = PlayerOutReason.OUT_FOR_REPAIR;
        allowed = gc.allowedToReenterField(currentStage, currentTime, stageOccurred, timeOccurred, minOutFieldTime, reasonOut);
        assertFalse(allowed);
        currentTime = "02:00";
        allowed = gc.allowedToReenterField(currentStage, currentTime, stageOccurred, timeOccurred, minOutFieldTime, reasonOut);
        assertFalse(allowed);
        currentTime = "02:01";
        allowed = gc.allowedToReenterField(currentStage, currentTime, stageOccurred, timeOccurred, minOutFieldTime, reasonOut);
        assertTrue(allowed);
        currentStage = GameStage.PREGAME;
        currentTime = "01:02";
        stageOccurred = GameStage.PREGAME;
        timeOccurred = "00:01";
        minOutFieldTime = 10;
        allowed = gc.allowedToReenterField(currentStage, currentTime, stageOccurred, timeOccurred, minOutFieldTime, reasonOut);
        assertTrue(allowed);
        reasonOut = PlayerOutReason.OUT_SANCTION_TEMPORARY;
        allowed = gc.allowedToReenterField(currentStage, currentTime, stageOccurred, timeOccurred, minOutFieldTime, reasonOut);
        assertFalse(allowed);
        reasonOut = PlayerOutReason.OUT_FOR_REPAIR;
        currentStage = GameStage.FIRST_HALF;
        currentTime = "00:02";
        stageOccurred = GameStage.PREGAME;
        timeOccurred = "00:01";
        minOutFieldTime = 60;
        allowed = gc.allowedToReenterField(currentStage, currentTime, stageOccurred, timeOccurred, minOutFieldTime, reasonOut);
        assertFalse(allowed);
        reasonOut = PlayerOutReason.OUT_SANCTION_REST_OF_GAME;
        allowed = gc.allowedToReenterField(currentStage, currentTime, stageOccurred, timeOccurred, minOutFieldTime, reasonOut);
        assertFalse(allowed);
        ConfigurationData cd = new ConfigurationData();
        gc.init(null, cd);
        gc.setKickoff(Team.TEAM_B);
        gc.setStart();
        gc.stepTime();
        Thread.sleep(3000);
        gc.stepTime();
        gc.setStop();
        gc.endHalf();
        gc.setStart();
        reasonOut = PlayerOutReason.OUT_SANCTION_TEMPORARY;
        currentStage = GameStage.SECOND_HALF;
        currentTime = "00:08";
        stageOccurred = GameStage.FIRST_HALF;
        timeOccurred = "00:00";
        minOutFieldTime = 10;
        allowed = gc.allowedToReenterField(currentStage, currentTime, stageOccurred, timeOccurred, minOutFieldTime, reasonOut);
        assertTrue(allowed);
        reasonOut = PlayerOutReason.OUT_SANCTION_TEMPORARY;
        currentStage = GameStage.SECOND_HALF;
        currentTime = "00:08";
        stageOccurred = GameStage.FIRST_HALF;
        timeOccurred = "00:02";
        minOutFieldTime = 10;
        allowed = gc.allowedToReenterField(currentStage, currentTime, stageOccurred, timeOccurred, minOutFieldTime, reasonOut);
        assertFalse(allowed);
    }
}
