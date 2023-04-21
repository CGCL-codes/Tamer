package edu.asu.commons.foraging.data;

import java.awt.Point;
import java.io.PrintWriter;
import java.util.Map;
import java.util.SortedSet;
import edu.asu.commons.event.ChatRequest;
import edu.asu.commons.event.PersistableEvent;
import edu.asu.commons.experiment.SaveFileProcessor;
import edu.asu.commons.experiment.SavedRoundData;
import edu.asu.commons.foraging.conf.RoundConfiguration;
import edu.asu.commons.foraging.event.EnforcementRankingRequest;
import edu.asu.commons.foraging.event.HarvestFruitRequest;
import edu.asu.commons.foraging.event.HarvestResourceRequest;
import edu.asu.commons.foraging.event.MovementEvent;
import edu.asu.commons.foraging.event.QuizResponseEvent;
import edu.asu.commons.foraging.event.RealTimeSanctionRequest;
import edu.asu.commons.foraging.event.ResourcesAddedEvent;
import edu.asu.commons.foraging.event.TokenCollectedEvent;
import edu.asu.commons.foraging.model.ClientData;
import edu.asu.commons.foraging.model.GroupDataModel;
import edu.asu.commons.foraging.model.Resource;
import edu.asu.commons.foraging.model.ServerDataModel;
import edu.asu.commons.net.Identifier;
import edu.asu.commons.util.Utils;

/**
 * $Id: AllDataProcessor.java 526 2010-08-06 01:25:27Z alllee $
 * 
 * Serializes all data in the save file into a CSV string format, ordered by time.
 * 
 * 
 * @author <a href='mailto:allen.lee@asu.edu'>Allen Lee</a>
 * @version $Rev: 526 $
 */
class AllDataProcessor extends SaveFileProcessor.Base {

    @Override
    public String getOutputFileExtension() {
        return "-all-data.txt";
    }

    @Override
    public void process(SavedRoundData savedRoundData, PrintWriter writer) {
        RoundConfiguration roundConfiguration = (RoundConfiguration) savedRoundData.getRoundParameters();
        if (roundConfiguration.is2dExperiment()) {
            processData2d(savedRoundData, writer);
        } else {
            processData3d(savedRoundData, writer);
        }
    }

    private void processData2d(SavedRoundData savedRoundData, PrintWriter writer) {
        SortedSet<PersistableEvent> actions = savedRoundData.getActions();
        ServerDataModel model = (ServerDataModel) savedRoundData.getDataModel();
        Map<Identifier, ClientMovementTokenCount> clientMovementTokenCounts = ClientMovementTokenCount.createMap(model);
        Map<Identifier, ClientData> clientDataMap = model.getClientDataMap();
        for (PersistableEvent event : actions) {
            if (event instanceof MovementEvent) {
                MovementEvent movementEvent = (MovementEvent) event;
                ClientData clientData = clientDataMap.get(event.getId());
                ClientMovementTokenCount client = clientMovementTokenCounts.get(event.getId());
                client.moves++;
                GroupDataModel group = clientData.getGroupDataModel();
                String line = String.format("%s, %s, %d, %d, %s, %s", savedRoundData.toSecondString(event), clientData.getId(), group.getGroupId(), client.moves, movementEvent.getDirection(), "movement event");
                writer.println(line);
            } else if (event instanceof TokenCollectedEvent) {
                TokenCollectedEvent tokenCollectedEvent = (TokenCollectedEvent) event;
                ClientData clientData = clientDataMap.get(event.getId());
                ClientMovementTokenCount client = clientMovementTokenCounts.get(event.getId());
                Point location = tokenCollectedEvent.getLocation();
                client.tokens++;
                GroupDataModel group = clientData.getGroupDataModel();
                String line = String.format("%s, %s, %d, %d, %d, %d, %s", savedRoundData.toSecondString(event), clientData.getId(), location.x, location.y, group.getGroupId(), client.tokens, "token collected event");
                writer.println(line);
            } else if (event instanceof ResourcesAddedEvent) {
                ResourcesAddedEvent resourcesAddedEvent = (ResourcesAddedEvent) event;
                String line = String.format("%s, %s, %s, %s", savedRoundData.toSecondString(event), resourcesAddedEvent.getClass(), resourcesAddedEvent.getGroup().toString(), resourcesAddedEvent.getResourcePositions());
                writer.println(line);
            } else if (event instanceof ChatRequest) {
                ChatRequest request = (ChatRequest) event;
                Identifier sourceId = request.getSource();
                Identifier targetId = request.getTarget();
                String message = request.toString();
                String line = String.format("%s, %s, %s, %s", savedRoundData.toSecondString(event), sourceId, targetId, message);
                System.err.println(line);
                writer.println(line);
            } else if (event instanceof RealTimeSanctionRequest) {
                RealTimeSanctionRequest request = (RealTimeSanctionRequest) event;
                Identifier source = request.getSource();
                Identifier target = request.getTarget();
                String line = String.format("%s, %s, %s, %s", savedRoundData.toSecondString(event), source, target, request.toString());
                writer.println(line);
            } else if (event instanceof QuizResponseEvent) {
                QuizResponseEvent response = (QuizResponseEvent) event;
                String line = String.format("%s, %s", savedRoundData.toSecondString(event), response.toString());
                writer.println(line);
            } else if (event instanceof EnforcementRankingRequest) {
                System.err.println("enforcement ranking request: " + event);
                EnforcementRankingRequest request = (EnforcementRankingRequest) event;
                String line = String.format("%s, %s", savedRoundData.toSecondString(event), request.toString());
                writer.println(line);
            } else {
                writer.println(String.format("%s, %s", savedRoundData.toSecondString(event), event.toString()));
            }
        }
    }

    private void processData3d(SavedRoundData savedRoundData, PrintWriter writer) {
        RoundConfiguration roundConfiguration = (RoundConfiguration) savedRoundData.getRoundParameters();
        ServerDataModel dataModel = (ServerDataModel) savedRoundData.getDataModel();
        Map<Identifier, ClientMovementTokenCount> clientStatsMap = ClientMovementTokenCount.createMap(dataModel);
        SortedSet<PersistableEvent> actions = savedRoundData.getActions();
        for (PersistableEvent event : actions) {
            if (event instanceof ChatRequest) {
                ChatRequest request = (ChatRequest) event;
                Identifier sourceId = request.getSource();
                Identifier targetId = request.getTarget();
                String message = request.toString();
                String line = String.format("%s, %s, %s, %s", savedRoundData.toSecondString(event), sourceId, targetId, message);
                writer.println(line);
            } else if (event instanceof ResourcesAddedEvent) {
                ResourcesAddedEvent resourcesAddedEvent = (ResourcesAddedEvent) event;
                String line = String.format("%s, %s, %s", savedRoundData.toSecondString(event), resourcesAddedEvent.getGroup(), Utils.join(',', resourcesAddedEvent.getResourcePositions()));
                System.err.println("resources added event: " + line);
                writer.println(line);
            } else if (event instanceof HarvestFruitRequest) {
                HarvestFruitRequest request = (HarvestFruitRequest) event;
                ClientMovementTokenCount clientStats = clientStatsMap.get(event.getId());
                clientStats.tokens += roundConfiguration.getTokensPerFruits();
                Resource resource = request.getResource();
                String line = String.format("%s, %s, %d, %d, %d, %d, %d, %d, %s", savedRoundData.toSecondString(event), event.getId(), resource.getPosition().x, resource.getPosition().y, 1, resource.getAge(), roundConfiguration.getTokensPerFruits(), clientStats.tokens, "harvest fruit");
                writer.println(line);
            } else if (event instanceof HarvestResourceRequest) {
                HarvestResourceRequest request = (HarvestResourceRequest) event;
                Resource resource = request.getResource();
                ClientMovementTokenCount clientStats = clientStatsMap.get(event.getId());
                clientStats.tokens += roundConfiguration.ageToTokens(resource.getAge());
                String line = String.format("%s, %s, %d, %d, %d, %d, %d, %d, %s", savedRoundData.toSecondString(event), event.getId(), resource.getPosition().x, resource.getPosition().y, 0, resource.getAge(), roundConfiguration.ageToTokens(resource.getAge()), clientStats.tokens, "harvest resource");
                System.err.println("harvest resource request: " + line);
                writer.println(line);
            }
        }
    }
}
