package org.robocup.msl.refbox.applications;

import java.io.FileOutputStream;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.robocup.msl.communication.client.CommunicationClient;
import org.robocup.msl.refbox.CommunicationClient;
import org.robocup.msl.refbox.RefboxUtils;
import org.robocup.msl.refbox.XmlUtils;
import org.robocup.msl.refbox.constants.CardColor;
import org.robocup.msl.refbox.constants.GameStage;
import org.robocup.msl.refbox.constants.Team;
import org.robocup.msl.refbox.data.CardData;
import org.robocup.msl.refbox.data.GameSetupData;
import org.robocup.msl.refbox.data.GoalData;
import org.robocup.msl.refbox.data.HistoryElement;
import org.robocup.msl.refbox.data.TeamData;
import org.robocup.msl.refbox.data.TeamSetupData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class GameSheet {

    private GameSheet() {
    }

    public static void generate(final CommunicationClient communicationClient, final String xmlFileName) {
        saveXMLfile(communicationClient, xmlFileName);
        generateGameSheetHTMLfile(xmlFileName);
    }

    private static void generateGameSheetHTMLfile(final String xmlFileName) throws TransformerFactoryConfigurationError {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            StreamSource xsltStream = new StreamSource("refbox.xslt");
            Transformer transformer = tFactory.newTransformer(xsltStream);
            String htmlFileName = xmlFileName.substring(0, xmlFileName.length() - 4) + ".html";
            StreamSource xmlStream = new StreamSource(xmlFileName);
            StreamResult htmlResultStream = new StreamResult(new FileOutputStream(htmlFileName));
            transformer.transform(xmlStream, htmlResultStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveXMLfile(final CommunicationClient communicationClient, final String fileName) {
        final Document doc = XmlUtils.createDocument();
        final Element gameElement = doc.createElement("Game");
        doc.appendChild(gameElement);
        final GameSetupData gameSetupData = communicationClient.getGameSetupData();
        addElement(doc, gameElement, "Location", gameSetupData.getLocation());
        addElement(doc, gameElement, "Date", gameSetupData.getDate());
        addElement(doc, gameElement, "Time", gameSetupData.getTime());
        final Element refereeElement = doc.createElement("Referee");
        addElement(doc, refereeElement, "Main-referee", gameSetupData.getReferee());
        addElement(doc, refereeElement, "Assistant-Referee-1", gameSetupData.getAssistantReferee1());
        addElement(doc, refereeElement, "Assistant-Referee-2", gameSetupData.getAssistantReferee2());
        addElement(doc, refereeElement, "Time-Keeper", gameSetupData.getTimeKeeper());
        addElement(doc, refereeElement, "Additional-Assistant-Referee-1", gameSetupData.getAdditionalAssistantReferee1());
        addElement(doc, refereeElement, "Additional-Assistant-Referee-2", gameSetupData.getAdditionalAssistantReferee1());
        gameElement.appendChild(refereeElement);
        final Element teamsElement = doc.createElement("Teams");
        fillTeamSetupData(communicationClient, doc, teamsElement, "SetupA", Team.TEAM_A);
        fillTeamSetupData(communicationClient, doc, teamsElement, "SetupB", Team.TEAM_B);
        gameElement.appendChild(teamsElement);
        final Element statsElement = doc.createElement("Stats");
        final Element generalstatsElement = doc.createElement("General");
        final Element timesElement = doc.createElement("Times");
        addElement(doc, timesElement, "PreGame", communicationClient.getTimeUsedAsString(GameStage.PREGAME));
        addElement(doc, timesElement, "FirstHalf", communicationClient.getTimeUsedAsString(GameStage.FIRST_HALF));
        addElement(doc, timesElement, "FirstHalfPlayed", communicationClient.getTimePlayedAsString(GameStage.FIRST_HALF));
        addElement(doc, timesElement, "HalfTime", communicationClient.getTimeUsedAsString(GameStage.HALF_TIME));
        addElement(doc, timesElement, "SecondHalf", communicationClient.getTimeUsedAsString(GameStage.SECOND_HALF));
        addElement(doc, timesElement, "SecondHalfPlayed", communicationClient.getTimePlayedAsString(GameStage.SECOND_HALF));
        addElement(doc, timesElement, "TimeStopped", communicationClient.getTimeStoppedAsString());
        generalstatsElement.appendChild(timesElement);
        addElement(doc, generalstatsElement, "DroppedBalls", "" + communicationClient.getNumberOfDroppedBalls());
        addElement(doc, generalstatsElement, "NeutralStarts", "" + communicationClient.getNumberOfNeutralStarts());
        statsElement.appendChild(generalstatsElement);
        writeTeamStats(communicationClient, doc, statsElement, "TeamA", Team.TEAM_A);
        writeTeamStats(communicationClient, doc, statsElement, "TeamB", Team.TEAM_B);
        gameElement.appendChild(statsElement);
        final Element logElement = doc.createElement("Log-data");
        for (HistoryElement element : communicationClient.getHistoryList()) {
            final Element logStatement = doc.createElement("Log-statement");
            addElement(doc, logStatement, "Stage", RefboxUtils.getGameStageAsString(element.getStage()));
            addElement(doc, logStatement, "Time", element.getTime());
            addElement(doc, logStatement, "Message", element.getMessage());
            logElement.appendChild(logStatement);
        }
        gameElement.appendChild(logElement);
        XmlUtils.saveXMLDocument(fileName, doc);
    }

    private static void fillTeamSetupData(final CommunicationClient gameControl, final Document doc, final Element teamsElement, final String teamSetup, final Team team) {
        final TeamSetupData setupData = gameControl.getTeamSetup(team);
        final Element setupElement = doc.createElement(teamSetup);
        addElement(doc, setupElement, "Teamname", setupData.getTeamName());
        addElement(doc, setupElement, "Teamleader", setupData.getTeamLeader());
        addElement(doc, setupElement, "Teamcolor", gameControl.getTeamColorName(team));
        final Element playersElement = doc.createElement("Players");
        final Map<String, Boolean> playersMap = setupData.getPlayers();
        for (final Map.Entry<String, Boolean> entry : playersMap.entrySet()) {
            addElement(doc, playersElement, "Player", entry.getKey());
        }
        setupElement.appendChild(playersElement);
        teamsElement.appendChild(setupElement);
    }

    private static void writeTeamStats(final CommunicationClient gameControl, final Document doc, final Element statsElement, final String teamStatsId, final Team team) {
        final Element teamStatsElement = doc.createElement(teamStatsId);
        final TeamData teamData = gameControl.getTeamData(team);
        addElement(doc, teamStatsElement, "Kickoffs", "" + teamData.getKickoffs());
        addElement(doc, teamStatsElement, "Freekicks", "" + teamData.getFreekicks());
        addElement(doc, teamStatsElement, "Goalkicks", "" + teamData.getGoalkicks());
        addElement(doc, teamStatsElement, "Corners", "" + teamData.getCorners());
        addElement(doc, teamStatsElement, "Throwins", "" + teamData.getThrowins());
        addElement(doc, teamStatsElement, "Penalties", "" + teamData.getPenalties());
        final Element goalsElement = doc.createElement("Goals");
        for (final GoalData goalData : teamData.getGoals()) {
            final Element goalElement = doc.createElement("Goal");
            addElement(doc, goalElement, "Player", goalData.getPlayer());
            String ownGoal;
            if (goalData.isOwnGoal()) {
                ownGoal = "true";
            } else {
                ownGoal = "false";
            }
            addElement(doc, goalElement, "Own", ownGoal);
            addElement(doc, goalElement, "Stage", RefboxUtils.getGameStageAsString(goalData.getGameStage()));
            addElement(doc, goalElement, "Time", goalData.getTimeOccurred());
            goalsElement.appendChild(goalElement);
        }
        teamStatsElement.appendChild(goalsElement);
        final Element cardsElement = doc.createElement("Cards");
        for (final CardData cardData : teamData.getCards()) {
            final Element cardElement = doc.createElement("Card");
            addElement(doc, cardElement, "Player", cardData.getPlayer());
            String cardColor;
            if (cardData.getColor() == CardColor.RED) {
                cardColor = "Red";
            } else {
                cardColor = "Yellow";
            }
            addElement(doc, cardElement, "Color", cardColor);
            addElement(doc, cardElement, "Stage", RefboxUtils.getGameStageAsString(cardData.getGameStage()));
            addElement(doc, cardElement, "Time", cardData.getTimeOccurred());
            cardsElement.appendChild(cardElement);
        }
        teamStatsElement.appendChild(cardsElement);
        statsElement.appendChild(teamStatsElement);
    }

    private static void addElement(final Document doc, final Element parentElement, final String elementName, final String contents) {
        final Element newElement = doc.createElement(elementName);
        newElement.setTextContent(contents);
        parentElement.appendChild(newElement);
    }
}
