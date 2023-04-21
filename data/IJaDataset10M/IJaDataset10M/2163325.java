package games.strategy.triplea.delegate;

import games.strategy.common.delegate.BaseDelegate;
import games.strategy.engine.data.CompositeChange;
import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.data.PlayerList;
import games.strategy.engine.data.Territory;
import games.strategy.engine.delegate.IDelegateBridge;
import games.strategy.engine.message.IRemote;
import games.strategy.triplea.Constants;
import games.strategy.triplea.attatchments.ICondition;
import games.strategy.triplea.attatchments.PlayerAttachment;
import games.strategy.triplea.attatchments.TerritoryAttachment;
import games.strategy.triplea.attatchments.TriggerAttachment;
import games.strategy.util.CompositeMatchAnd;
import games.strategy.util.CompositeMatchOr;
import games.strategy.util.EventThreadJOptionPane;
import games.strategy.util.Match;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 * 
 * A delegate used to check for end of game conditions.
 * 
 * @author Sean Bridges
 */
public class EndRoundDelegate extends BaseDelegate {

    private boolean m_gameOver = false;

    private Collection<PlayerID> m_winners = new ArrayList<PlayerID>();

    /** Creates a new instance of EndRoundDelegate */
    public EndRoundDelegate() {
    }

    /**
	 * Called before the delegate will run.
	 */
    @Override
    public void start(final IDelegateBridge aBridge) {
        super.start(aBridge);
        if (m_gameOver) return;
        String victoryMessage = null;
        final GameData data = getData();
        if (isPacificTheater()) {
            final PlayerID japanese = data.getPlayerList().getPlayerID(Constants.JAPANESE);
            final PlayerAttachment pa = PlayerAttachment.get(japanese);
            if (pa != null && pa.getVps() >= 22) {
                victoryMessage = "Axis achieve VP victory";
                aBridge.getHistoryWriter().startEvent(victoryMessage);
                final Collection<PlayerID> winners = data.getAllianceTracker().getPlayersInAlliance(data.getAllianceTracker().getAlliancesPlayerIsIn(japanese).iterator().next());
                signalGameOver(victoryMessage, winners, aBridge);
            }
        }
        if (isTotalVictory()) {
            victoryMessage = " achieve TOTAL VICTORY with ";
            checkVictoryCities(aBridge, victoryMessage, " Total Victory VCs");
        }
        if (isHonorableSurrender()) {
            victoryMessage = " achieve an HONORABLE VICTORY with ";
            checkVictoryCities(aBridge, victoryMessage, " Honorable Victory VCs");
        }
        if (isProjectionOfPower()) {
            victoryMessage = " achieve victory through a PROJECTION OF POWER with ";
            checkVictoryCities(aBridge, victoryMessage, " Projection of Power VCs");
        }
        if (isEconomicVictory()) {
            final Iterator<String> allianceIter = data.getAllianceTracker().getAlliances().iterator();
            String allianceName = null;
            while (allianceIter.hasNext()) {
                allianceName = allianceIter.next();
                final int victoryAmount = getEconomicVictoryAmount(data, allianceName);
                final Set<PlayerID> teamMembers = data.getAllianceTracker().getPlayersInAlliance(allianceName);
                final Iterator<PlayerID> teamIter = teamMembers.iterator();
                int teamProd = 0;
                while (teamIter.hasNext()) {
                    final PlayerID player = teamIter.next();
                    teamProd += getProduction(player);
                    if (teamProd >= victoryAmount) {
                        victoryMessage = allianceName + " achieve economic victory";
                        aBridge.getHistoryWriter().startEvent(victoryMessage);
                        final Collection<PlayerID> winners = data.getAllianceTracker().getPlayersInAlliance(allianceName);
                        signalGameOver(victoryMessage, winners, aBridge);
                    }
                }
            }
        }
        if (isTriggeredVictory()) {
            final Match<TriggerAttachment> endRoundDelegateTriggerMatch = new CompositeMatchAnd<TriggerAttachment>(TriggerAttachment.availableUses, TriggerAttachment.whenOrDefaultMatch(null, null), new CompositeMatchOr<TriggerAttachment>(TriggerAttachment.activateTriggerMatch(), TriggerAttachment.victoryMatch()));
            final HashSet<TriggerAttachment> toFirePossible = TriggerAttachment.collectForAllTriggersMatching(new HashSet<PlayerID>(data.getPlayerList().getPlayers()), endRoundDelegateTriggerMatch, m_bridge);
            if (!toFirePossible.isEmpty()) {
                final HashMap<ICondition, Boolean> testedConditions = TriggerAttachment.collectTestsForAllTriggers(toFirePossible, m_bridge);
                final Set<TriggerAttachment> toFireTestedAndSatisfied = new HashSet<TriggerAttachment>(Match.getMatches(toFirePossible, TriggerAttachment.isSatisfiedMatch(testedConditions)));
                TriggerAttachment.triggerActivateTriggerOther(testedConditions, toFireTestedAndSatisfied, m_bridge, null, null, true, true, true, true);
                TriggerAttachment.triggerVictory(toFireTestedAndSatisfied, m_bridge, null, null, true, true, true, true);
            }
        }
        if (isWW2V2() || isWW2V3()) return;
        final PlayerList playerList = data.getPlayerList();
        final PlayerID russians = playerList.getPlayerID(Constants.RUSSIANS);
        final PlayerID germans = playerList.getPlayerID(Constants.GERMANS);
        final PlayerID british = playerList.getPlayerID(Constants.BRITISH);
        final PlayerID japanese = playerList.getPlayerID(Constants.JAPANESE);
        final PlayerID americans = playerList.getPlayerID(Constants.AMERICANS);
        if (germans == null || russians == null || british == null || japanese == null || americans == null || playerList.size() > 5) return;
        final boolean russia = TerritoryAttachment.getCapital(russians, data).getOwner().equals(russians);
        final boolean germany = TerritoryAttachment.getCapital(germans, data).getOwner().equals(germans);
        final boolean britain = TerritoryAttachment.getCapital(british, data).getOwner().equals(british);
        final boolean japan = TerritoryAttachment.getCapital(japanese, data).getOwner().equals(japanese);
        final boolean america = TerritoryAttachment.getCapital(americans, data).getOwner().equals(americans);
        int count = 0;
        if (!russia) count++;
        if (!britain) count++;
        if (!america) count++;
        victoryMessage = " achieve a military victory";
        if (germany && japan && count >= 2) {
            aBridge.getHistoryWriter().startEvent("Axis" + victoryMessage);
            final Collection<PlayerID> winners = data.getAllianceTracker().getPlayersInAlliance("Axis");
            signalGameOver(victoryMessage, winners, aBridge);
        }
        if (russia && !germany && britain && !japan && america) {
            aBridge.getHistoryWriter().startEvent("Allies" + victoryMessage);
            final Collection<PlayerID> winners = data.getAllianceTracker().getPlayersInAlliance("Allies");
            signalGameOver(victoryMessage, winners, aBridge);
        }
    }

    @Override
    public void end() {
        super.end();
        final GameData data = getData();
        if (games.strategy.triplea.Properties.getTriggers(data)) {
            final CompositeChange change = new CompositeChange();
            for (final PlayerID player : data.getPlayerList().getPlayers()) {
                change.add(TriggerAttachment.triggerSetUsedForThisRound(player, m_bridge));
            }
            if (!change.isEmpty()) {
                m_bridge.addChange(change);
                m_bridge.getHistoryWriter().startEvent("Setting uses for triggers used this round.");
            }
        }
    }

    private BattleTracker getBattleTracker() {
        return DelegateFinder.battleDelegate(getData()).getBattleTracker();
    }

    @Override
    public Serializable saveState() {
        final EndRoundExtendedDelegateState state = new EndRoundExtendedDelegateState();
        state.superState = super.saveState();
        state.m_gameOver = m_gameOver;
        state.m_winners = m_winners;
        return state;
    }

    @Override
    public void loadState(final Serializable state) {
        final EndRoundExtendedDelegateState s = (EndRoundExtendedDelegateState) state;
        super.loadState(s.superState);
        m_gameOver = s.m_gameOver;
        m_winners = s.m_winners;
    }

    private void checkVictoryCities(final IDelegateBridge aBridge, final String victoryMessage, final String victoryType) {
        final GameData data = aBridge.getData();
        final Iterator<String> allianceIter = data.getAllianceTracker().getAlliances().iterator();
        String allianceName = null;
        while (allianceIter.hasNext()) {
            allianceName = allianceIter.next();
            final int vcAmount = getVCAmount(data, allianceName, victoryType);
            final Set<PlayerID> teamMembers = data.getAllianceTracker().getPlayersInAlliance(allianceName);
            final int teamVCs = Match.countMatches(data.getMap().getTerritories(), new CompositeMatchAnd<Territory>(Matches.TerritoryIsVictoryCity, Matches.isTerritoryOwnedBy(teamMembers)));
            if (teamVCs >= vcAmount) {
                aBridge.getHistoryWriter().startEvent(allianceName + victoryMessage + vcAmount + " Victory Cities!");
                final Collection<PlayerID> winners = data.getAllianceTracker().getPlayersInAlliance(allianceName);
                signalGameOver(allianceName + victoryMessage + vcAmount + " Victory Cities!", winners, aBridge);
            }
        }
    }

    private int getEconomicVictoryAmount(final GameData data, final String alliance) {
        return data.getProperties().get(alliance + " Economic Victory", 200);
    }

    private int getVCAmount(final GameData data, final String alliance, final String type) {
        int defaultVC = 20;
        if (type.equals(" Total Victory VCs")) {
            defaultVC = 18;
        } else if (type.equals(" Honorable Victory VCs")) {
            defaultVC = 15;
        } else if (type.equals(" Projection of Power VCs")) {
            defaultVC = 13;
        }
        return data.getProperties().get((alliance + type), defaultVC);
    }

    /**
	 * Notify all players that the game is over.
	 * 
	 * @param status
	 *            the "game over" text to be displayed to each user.
	 */
    public void signalGameOver(final String status, final Collection<PlayerID> winners, final IDelegateBridge a_bridge) {
        if (!m_gameOver) {
            m_gameOver = true;
            m_winners = winners;
            final int rVal = EventThreadJOptionPane.showConfirmDialog(null, status + "\nDo you want to continue?", "Continue", JOptionPane.YES_NO_OPTION);
            if (rVal != JOptionPane.OK_OPTION) a_bridge.stopGameSequence();
        }
    }

    /**
	 * if null, the game is not over yet
	 * 
	 * @return
	 */
    public Collection<PlayerID> getWinners() {
        if (!m_gameOver) return null;
        return m_winners;
    }

    private boolean isWW2V2() {
        return games.strategy.triplea.Properties.getWW2V2(getData());
    }

    private boolean isWW2V3() {
        return games.strategy.triplea.Properties.getWW2V3(getData());
    }

    private boolean isPacificTheater() {
        return games.strategy.triplea.Properties.getPacificTheater(getData());
    }

    private boolean isTotalVictory() {
        return games.strategy.triplea.Properties.getTotalVictory(getData());
    }

    private boolean isHonorableSurrender() {
        return games.strategy.triplea.Properties.getHonorableSurrender(getData());
    }

    private boolean isProjectionOfPower() {
        return games.strategy.triplea.Properties.getProjectionOfPower(getData());
    }

    private boolean isEconomicVictory() {
        return games.strategy.triplea.Properties.getEconomicVictory(getData());
    }

    private boolean isTriggeredVictory() {
        return games.strategy.triplea.Properties.getTriggeredVictory(getData());
    }

    public int getProduction(final PlayerID id) {
        int sum = 0;
        final Iterator<Territory> territories = getData().getMap().iterator();
        while (territories.hasNext()) {
            final Territory current = territories.next();
            if (current.getOwner().equals(id)) {
                final TerritoryAttachment ta = TerritoryAttachment.get(current);
                sum += ta.getProduction();
            }
        }
        return sum;
    }

    @Override
    public Class<? extends IRemote> getRemoteType() {
        return null;
    }
}

@SuppressWarnings("serial")
class EndRoundExtendedDelegateState implements Serializable {

    Serializable superState;

    public boolean m_gameOver = false;

    public Collection<PlayerID> m_winners = new ArrayList<PlayerID>();
}
