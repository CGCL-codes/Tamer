package org.game.thyvin.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.engine.GameEngine;
import org.engine.GameEventListener;
import org.event.GameEvent;
import org.event.GenericAction;
import org.event.GenericObjectGameEvent;
import org.game.thyvin.event.MessageTypes;
import org.game.thyvin.logic.pathfinding.ModifiedWeightFunction;
import org.game.thyvin.logic.pathfinding.PathWeightFunction;
import org.game.thyvin.logic.pathfinding.PathWeightFunction.IgnoreWhich;
import org.game.thyvin.logic.room.Ownable;
import org.game.thyvin.logic.room.Room;
import org.game.thyvin.logic.room.ThyvinSampleNode;
import org.game.thyvin.logic.room.ThyvinSampleRoomMap;
import org.game.thyvin.logic.room.Unit;
import org.game.thyvin.logic.tech.Modifier;
import org.game.thyvin.logic.tech.Tech;
import org.game.thyvin.logic.tech.TechState;
import org.game.thyvin.logic.tech.TechState.ResearchTech;
import org.game.thyvin.resource.RoomUnitLoader;
import org.logic.EventChanger;

public class SimpleEventChanger implements EventChanger<ThyvinSampleGameState> {

    private RoomUnitLoader roomUnitLoader;

    private Collection<GameEventListener> gameEventListeners = new ArrayList<GameEventListener>();

    public SimpleEventChanger(RoomUnitLoader roomUnitLoader) {
        this.roomUnitLoader = roomUnitLoader;
    }

    public void performGameEvent(GameEvent gameEvent, ThyvinSampleGameState gameState) throws IllegalStateException {
        if (gameEvent instanceof GenericAction) {
            handleGenericAction((GenericAction) gameEvent, gameState);
        } else {
            switch(gameEvent.getType()) {
                case MessageTypes.MODIFIER_UPDATE:
                    {
                        GenericObjectGameEvent oEvent = (GenericObjectGameEvent) gameEvent;
                        int team = oEvent.getSource();
                        Object[] v = (Object[]) oEvent.getObject();
                        int ctype = (Integer) v[0];
                        Modifier[] modifiers = (Modifier[]) v[1];
                        if (team == GameEngine.PLAYER_ALL) {
                            final int teams = gameState.getNumberOfTeams();
                            for (int i = 0; i < teams; i++) {
                                for (int j = 0; j < modifiers.length; j++) {
                                    gameState.updateModifier(i, ctype, modifiers[j]);
                                }
                            }
                        } else {
                            for (int i = 0; i < modifiers.length; i++) {
                                gameState.updateModifier(team, ctype, modifiers[i]);
                            }
                        }
                        break;
                    }
                case MessageTypes.TECH_COMPLETE:
                    {
                        GenericObjectGameEvent oEvent = (GenericObjectGameEvent) gameEvent;
                        int team = oEvent.getSource();
                        Tech t = (Tech) oEvent.getObject();
                        TechState tstate = gameState.getTechState(team);
                        tstate.setIsResearched(t);
                        break;
                    }
                case MessageTypes.TECH_AVAILIBILITY:
                    {
                        GenericObjectGameEvent oEvent = (GenericObjectGameEvent) gameEvent;
                        int team = oEvent.getSource();
                        Object[] v = (Object[]) oEvent.getObject();
                        boolean avail = (Boolean) v[0];
                        Tech[] modifiers = (Tech[]) v[1];
                        int[] rtimes = (int[]) v[2];
                        TechState tstate;
                        if (team == GameEngine.PLAYER_ALL) {
                            final int teams = gameState.getNumberOfTeams();
                            for (int i = 0; i < teams; i++) {
                                tstate = gameState.getTechState(i);
                                for (int j = 0; j < modifiers.length; j++) {
                                    tstate.setTechAvailable(avail, modifiers[j], rtimes[j]);
                                }
                            }
                        } else {
                            tstate = gameState.getTechState(team);
                            for (int i = 0; i < modifiers.length; i++) {
                                tstate.setTechAvailable(avail, modifiers[i], rtimes[i]);
                            }
                        }
                        break;
                    }
            }
        }
        synchronized (gameEventListeners) {
            for (GameEventListener l : gameEventListeners) {
                l.gameEvent(gameEvent);
            }
        }
    }

    private void handleGenericAction(GenericAction genericAction, ThyvinSampleGameState gameState) throws IllegalStateException {
        int[] nodesIds = genericAction.getPayload();
        switch(genericAction.getType()) {
            case MessageTypes.MOVE:
                {
                    performMove(nodesIds, gameState);
                    break;
                }
            case MessageTypes.SWAP:
                {
                    performSwap(nodesIds, gameState);
                    break;
                }
            case MessageTypes.PRODUCE_UNITS:
                {
                    performProduceUnits(nodesIds, gameState);
                    break;
                }
            case MessageTypes.COMBAT:
                {
                    break;
                }
            case MessageTypes.FIELD_OWNERSHIP:
                {
                    performCapture(genericAction.getSource(), nodesIds, gameState);
                    break;
                }
            case MessageTypes.GOLD:
                {
                    performGold(nodesIds, gameState);
                    break;
                }
            case MessageTypes.POINTS:
                {
                    performPoints(nodesIds, gameState);
                    break;
                }
            case MessageTypes.NEW_TURN:
                {
                    performNewTurn(nodesIds, gameState);
                    break;
                }
            case MessageTypes.DEATH:
                {
                    performDeath(nodesIds, gameState);
                    break;
                }
            case MessageTypes.HEALTH_LOSS:
                {
                    performHealthLoss(nodesIds, gameState);
                    break;
                }
            case MessageTypes.SET_TECH:
                {
                    performSetTech(nodesIds, gameState);
                    break;
                }
            case MessageTypes.TECH_PROGRESS:
                {
                    performTechProgress(nodesIds, gameState);
                    break;
                }
            default:
                throw new IllegalStateException("Action not recognized by state changer");
        }
    }

    private void performTechProgress(int[] nodesIds, ThyvinSampleGameState gameState) {
        TechState tstate = gameState.getTechState();
        ResearchTech t = tstate.getAvailableTech(nodesIds[0]);
        if (t != null) {
            t.setResTimeSpent(nodesIds[1]);
        }
    }

    private void performSetTech(int[] nodesIds, ThyvinSampleGameState gameState) {
        int tid = nodesIds[0];
        TechState tstate = gameState.getTechState();
        if (tid == Tech.INVALID_TECH_ID) {
            tstate.setCurrentTech(null);
        } else {
            tstate.setCurrentTech(tstate.getAvailableTech(tid));
        }
    }

    private void performCapture(final int owner, int[] nodesIds, ThyvinSampleGameState gameState) {
        for (int i = 0; i < nodesIds.length; i++) {
            Ownable res = gameState.getNode(nodesIds[i]).getResourceElement();
            if (res != null) {
                res.setTeam(owner);
            }
        }
    }

    private void performGold(int[] nodesIds, ThyvinSampleGameState gameState) {
        gameState.setCurrentGold(nodesIds[2]);
    }

    private void performPoints(int[] nodesIds, ThyvinSampleGameState gameState) {
        gameState.setCurrentPoints(nodesIds[2]);
    }

    private void performMove(int[] nodesIds, ThyvinSampleGameState gameState) {
        ThyvinSampleRoomMap map = gameState.getMap();
        Unit unit = map.getNode(nodesIds[0]).getUnit();
        int distSum = 0;
        PathWeightFunction weight;
        if (unit == null) {
            throw new IllegalStateException("Movement requires a unit");
        }
        weight = new ModifiedWeightFunction(unit.getTeam(), gameState);
        int length_1 = nodesIds.length - 1;
        for (int i = 0; i < length_1; i++) {
            distSum += weight.weight(map.getNode(nodesIds[i]), map.getNode(nodesIds[i + 1]));
        }
        unit.setCurrentMoves(unit.getCurrentMoves() - distSum);
        gameState.moveUnit(unit, nodesIds[nodesIds.length - 1]);
    }

    private List<GameEvent> performSwap(int[] nodesIds, ThyvinSampleGameState gameState) {
        ThyvinSampleRoomMap map = gameState.getMap();
        ThyvinSampleNode startNode = map.getNode(nodesIds[0]);
        ThyvinSampleNode endNode = map.getNode(nodesIds[1]);
        Unit startUnit = startNode.getUnit();
        Unit endUnit = endNode.getUnit();
        PathWeightFunction weight;
        if (startUnit == null || endUnit == null) {
            throw new IllegalStateException("Swap requires two units to swap.");
        }
        if (startUnit.getTeam() != endUnit.getTeam()) {
            throw new IllegalStateException("Swap can only swap two units from the same team.");
        }
        weight = new ModifiedWeightFunction(-1, IgnoreWhich.ALL, gameState);
        startUnit.setCurrentMoves(startUnit.getCurrentMoves() - weight.weight(startNode, endNode));
        endUnit.setCurrentMoves(endUnit.getCurrentMoves() - weight.weight(endNode, startNode));
        gameState.swapUnits(startUnit, endUnit);
        List<GameEvent> actions = new ArrayList<GameEvent>();
        actions.add(new GenericAction(MessageTypes.SWAP, nodesIds));
        return actions;
    }

    private void performProduceUnits(int[] nodes, ThyvinSampleGameState gameState) {
        int team = gameState.getCurrentTeam();
        for (int nodeId : nodes) {
            Unit unit;
            try {
                unit = roomUnitLoader.loadUnit("tmp/stratac/units/unit_basic");
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            unit.setTeam(team);
            unit.setCurrentMoves(0);
            gameState.addUnit(unit, nodeId);
        }
        gameState.setUnitCount(team, nodes.length + gameState.getUnitCount(team));
    }

    private List<GameEvent> performNewTurn(int[] nodesIds, ThyvinSampleGameState gameState) {
        List<GameEvent> actions = new ArrayList<GameEvent>();
        final int currentTeam;
        gameState.setCurrentTurn(nodesIds[0], nodesIds[1]);
        currentTeam = gameState.getCurrentTeam();
        for (ThyvinSampleNode node : gameState.getMap().getUnitNodes()) {
            Unit unit = node.getUnit();
            if (unit.getTeam() == currentTeam) {
                unit.setCurrentMoves(unit.getMaxMoves());
            } else {
                unit.setCurrentMoves(0);
            }
        }
        actions.add(new GenericAction(MessageTypes.NEW_TURN, nodesIds));
        return actions;
    }

    private void performDeath(int[] nodesIds, ThyvinSampleGameState gameState) {
        final ThyvinSampleRoomMap map = gameState.getMap();
        final int losses[] = new int[gameState.getNumberOfTeams()];
        for (int nodeId : nodesIds) {
            Unit u = map.getNode(nodeId).getUnit();
            if (u != null) {
                int ut = u.getTeam();
                losses[ut]++;
            }
            gameState.removeUnit(nodeId);
        }
        for (int i = 0; i < losses.length; i++) {
            gameState.setUnitCount(i, gameState.getUnitCount(i) - losses[i]);
        }
    }

    private void performHealthLoss(int[] nodesIds, ThyvinSampleGameState gameState) {
        final Room room = gameState.getRoom();
        final ThyvinSampleRoomMap map = room.getMap();
        int i = 0;
        while (i < nodesIds.length) {
            Unit unit = map.getNode(nodesIds[i]).getUnit();
            i++;
            int amount = nodesIds[i];
            i++;
            unit.setCurrentHealth(unit.getCurrentHealth() - amount);
        }
    }

    public void addPostGameEventListener(GameEventListener gameEventListener) {
        synchronized (gameEventListeners) {
            if (!gameEventListeners.contains(gameEventListener)) {
                gameEventListeners.add(gameEventListener);
            }
        }
    }

    public void removePostGameEventListener(GameEventListener gameEventListener) {
        synchronized (gameEventListeners) {
            if (gameEventListeners.contains(gameEventListener)) {
                gameEventListeners.remove(gameEventListener);
            }
        }
    }

    public Collection<GameEventListener> getPostGameEventListeners() {
        return Collections.unmodifiableCollection(gameEventListeners);
    }
}
