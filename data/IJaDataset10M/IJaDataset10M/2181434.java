package games.strategy.triplea.Dynamix_AI.Code;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.data.Territory;
import games.strategy.engine.data.Unit;
import games.strategy.triplea.Dynamix_AI.DMatches;
import games.strategy.triplea.Dynamix_AI.DSettings;
import games.strategy.triplea.Dynamix_AI.DUtils;
import games.strategy.triplea.Dynamix_AI.Dynamix_AI;
import games.strategy.triplea.Dynamix_AI.CommandCenter.CachedCalculationCenter;
import games.strategy.triplea.Dynamix_AI.CommandCenter.CachedInstanceCenter;
import games.strategy.triplea.Dynamix_AI.CommandCenter.GlobalCenter;
import games.strategy.triplea.Dynamix_AI.CommandCenter.ReconsiderSignalCenter;
import games.strategy.triplea.Dynamix_AI.CommandCenter.ThreatInvalidationCenter;
import games.strategy.triplea.Dynamix_AI.Group.MovePackage;
import games.strategy.triplea.Dynamix_AI.Group.UnitGroup;
import games.strategy.triplea.Dynamix_AI.Others.CM_Task;
import games.strategy.triplea.Dynamix_AI.Others.CM_TaskType;
import games.strategy.triplea.attatchments.TerritoryAttachment;
import games.strategy.triplea.delegate.Matches;
import games.strategy.triplea.delegate.remote.IMoveDelegate;
import games.strategy.triplea.oddsCalculator.ta.AggregateResults;
import games.strategy.util.CompositeMatchAnd;
import games.strategy.util.CompositeMatchOr;
import games.strategy.util.Match;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Stephen
 */
@SuppressWarnings("unchecked")
public class DoCombatMove {

    public static void doCombatMove(final Dynamix_AI ai, final GameData data, final IMoveDelegate mover, final PlayerID player) {
        if (DSettings.LoadSettings().AIC_disableAllUnitMovements) {
            final String message = ai.getName() + " is skipping it's cm phase, as instructed.";
            DUtils.Log(Level.FINE, message);
            final Runnable runner = new Runnable() {

                public void run() {
                    CachedInstanceCenter.CachedDelegateBridge.getHistoryWriter().startEvent(message);
                }
            };
            try {
                SwingUtilities.invokeAndWait(runner);
            } catch (final InterruptedException ex) {
            } catch (final InvocationTargetException ex) {
            }
            Dynamix_AI.Pause();
            return;
        }
        final MovePackage pack = new MovePackage(ai, data, mover, player, null, null, null);
        final List<CM_Task> tasks = GenerateTasks(pack);
        final List<Territory> ourCaps = TerritoryAttachment.getAllCapitals(player, data);
        final List<Territory> capsAndNeighbors = new ArrayList<Territory>();
        for (final Territory cap : ourCaps) capsAndNeighbors.addAll(DUtils.GetTerritoriesWithinXDistanceOfY(data, cap, 1));
        final List<CM_Task> capsAndNeighborsAttackTasks = new ArrayList<CM_Task>();
        for (final CM_Task task : tasks) {
            if (capsAndNeighbors.contains(task.GetTarget())) capsAndNeighborsAttackTasks.add(task);
        }
        DUtils.Log(Level.FINE, "  Beginning capital-protecting attacks on cap-neighboring enemies section");
        while (considerAndPerformWorthwhileTasks(pack, capsAndNeighborsAttackTasks)) {
        }
        DoNonCombatMove.doPreCombatMove(ai, data, mover, player);
        DUtils.Log(Level.FINE, "  Beginning task consideration loop section");
        for (int i = 0; i < 5; i++) {
            DUtils.Log(Level.FINE, "  Task consideration loop {0} started", i + 1);
            ReconsiderSignalCenter.get(data, player).ObjectsToReconsider.clear();
            final List<Territory> tersAttackedBeforeLoop = new ArrayList<Territory>();
            for (final CM_Task task : tasks) {
                if (task.IsCompleted()) tersAttackedBeforeLoop.add(task.GetTarget());
            }
            while (considerAndPerformWorthwhileTasks(pack, tasks)) {
            }
            if (ReconsiderSignalCenter.get(data, player).ObjectsToReconsider.isEmpty()) break; else {
                final List<Territory> tersToReconsider = DUtils.ToList(ReconsiderSignalCenter.get(data, player).ObjectsToReconsider);
                for (final CM_Task task : tasks) {
                    if (tersToReconsider.contains(task.GetTarget())) {
                        if (task.IsCompleted()) {
                        } else task.Reset();
                    }
                }
            }
        }
        DUtils.Log(Level.FINE, "  Calculating and adding additional task recruits. (Wave 2)");
        for (final CM_Task task : tasks) {
            if (task.IsCompleted()) {
                task.RecruitUnits2();
                if (task.IsTaskWithAdditionalRecruitsWorthwhile()) {
                    task.PerformTask(mover);
                    task.InvalidateThreatsThisTaskResists();
                }
            }
        }
        DUtils.Log(Level.FINE, "  Calculating and adding additional task recruits. (Wave 3)");
        for (final CM_Task task : tasks) {
            if (task.IsCompleted()) {
                task.RecruitUnits3();
                if (task.IsTaskWithAdditionalRecruitsWorthwhile()) {
                    task.PerformTask(mover);
                    task.InvalidateThreatsThisTaskResists();
                }
            }
        }
        ThreatInvalidationCenter.get(data, player).SuspendThreatInvalidation();
        DUtils.Log(Level.FINE, "  Calculating and adding additional task recruits. (Wave 4)");
        for (final CM_Task task : tasks) {
            if (task.IsCompleted()) {
                task.RecruitUnits4();
                if (task.IsTaskWithAdditionalRecruitsWorthwhile()) {
                    task.PerformTask(mover);
                    task.InvalidateThreatsThisTaskResists();
                }
            }
        }
        ThreatInvalidationCenter.get(data, player).ResumeThreatInvalidation();
        DUtils.Log(Level.FINE, "  Beginning of temporary ship movement block.");
        if (true) {
            UnitGroup.PerformBufferedMovesAndDisableMoveBufferring(mover);
            for (final Territory ter : data.getMap().getTerritories()) {
                if (!ter.isWater()) continue;
                final List<Unit> ourUnitGroup = ter.getUnits().getMatches(Matches.unitIsOwnedBy(player));
                if (ourUnitGroup.isEmpty()) continue;
                int unfilledTransports = 0;
                int filledTransports = 0;
                for (final Unit unit : ourUnitGroup) {
                    if (Matches.UnitIsTransport.match(unit)) {
                        if (Matches.unitIsTransporting().match(unit)) filledTransports++; else unfilledTransports++;
                    }
                }
                if (unfilledTransports > 0 && filledTransports < 10) {
                    Territory loadingTer = null;
                    Territory loadingPort = null;
                    int highestLoadingTerScore = Integer.MIN_VALUE;
                    for (final Territory ter2 : data.getMap().getTerritories()) {
                        if (ter2.isWater()) continue;
                        if (ter2.getUnits().getMatches(DUtils.CompMatchAnd(Matches.unitIsLandAndOwnedBy(player), Matches.UnitHasEnoughMovement(1), Matches.UnitCanBeTransported)).isEmpty()) continue;
                        final List<Territory> areaTroubleTers = DUtils.GetTerritoriesWithinXDistanceOfYMatchingZAndHavingRouteMatchingA(data, ter2, (int) (3 * GlobalCenter.MapTerCountScale), DUtils.CompMatchAnd(Matches.TerritoryIsLand, Matches.territoryHasEnemyLandUnits(player, data)), DMatches.TerritoryIsLandAndPassable);
                        if (areaTroubleTers.size() > 0) continue;
                        Territory openPort = null;
                        for (final Territory port : data.getMap().getNeighbors(ter2, Matches.TerritoryIsWater)) {
                            if (data.getMap().getRoute(ter2, port, DUtils.CompMatchAnd(Matches.TerritoryIsWater)) == null) continue;
                            openPort = port;
                            break;
                        }
                        if (openPort == null) continue;
                        int score = 0;
                        score -= CachedCalculationCenter.GetSeaRoute(data, ter, openPort).getLength();
                        if (score > highestLoadingTerScore) {
                            highestLoadingTerScore = score;
                            loadingTer = ter2;
                            loadingPort = openPort;
                        }
                    }
                    if (loadingTer == null) continue;
                    final UnitGroup ships = DUtils.CreateUnitGroupForUnits(ourUnitGroup, ter, data);
                    final String error = ships.MoveAsFarTo_CM(loadingPort, mover);
                    if (error != null) {
                        DUtils.Log(Level.FINER, "    There was an error moving ships[{0}] to loading port({1}->{2}): {3}", ships, ter, loadingPort, error);
                        continue;
                    }
                    final List<Unit> toLoadOntoShips = loadingTer.getUnits().getMatches(DUtils.CompMatchAnd(Matches.unitIsLandAndOwnedBy(player), Matches.UnitHasEnoughMovement(1), Matches.UnitCanBeTransported));
                    for (final Unit unit : toLoadOntoShips) {
                        final UnitGroup ug = DUtils.CreateUnitGroupForUnit(unit, loadingTer, data);
                        final String error2 = ug.MoveAsFarTo_NCM(loadingPort, mover);
                        if (error2 != null) {
                            DUtils.Log(Level.FINER, "    There was an error moving units[{0}] onto ship({1}->{2}): {3}", ug, loadingTer, loadingPort, error2);
                            continue;
                        }
                        ourUnitGroup.add(unit);
                    }
                } else if (filledTransports == 0) {
                    final Territory closestTerWithOtherShips = DUtils.GetClosestTerMatchingX(data, ter, DUtils.CompMatchAnd(Matches.TerritoryIsWater, Matches.territoryIs(ter).invert(), Matches.territoryHasUnitsThatMatch(Matches.unitIsOwnedBy(player))));
                    for (final Unit unit : ourUnitGroup) {
                        final UnitGroup ug = DUtils.CreateUnitGroupForUnit(unit, ter, data);
                        ug.MoveAsFarTo_CM(closestTerWithOtherShips, mover);
                    }
                }
                unfilledTransports = 0;
                filledTransports = 0;
                for (final Unit unit : ourUnitGroup) {
                    if (Matches.UnitIsTransport.match(unit)) {
                        if (Matches.unitIsTransporting().match(unit)) filledTransports++; else unfilledTransports++;
                    }
                }
                Territory unloadingTer = null;
                Territory unloadingPort = null;
                int highestUnloadingTerScore = Integer.MIN_VALUE;
                for (final Territory ter2 : data.getMap().getTerritories()) {
                    if (ter2.isWater()) continue;
                    if (!Matches.TerritoryIsPassableAndNotRestricted(player, data).match(ter2)) continue;
                    Territory openPort = null;
                    for (final Territory port : data.getMap().getNeighbors(ter2, Matches.TerritoryIsWater)) {
                        if (data.getMap().getRoute(ourUnitGroup.get(0).getTerritoryUnitIsIn(), port, DUtils.CompMatchAnd(Matches.TerritoryIsWater)) == null) continue;
                        openPort = port;
                        break;
                    }
                    if (openPort == null) continue;
                    int score = 0;
                    final List<Territory> areaTroubleTers = DUtils.GetTerritoriesWithinXDistanceOfYMatchingZAndHavingRouteMatchingA(data, ter2, (int) (3 * GlobalCenter.MapTerCountScale), DUtils.CompMatchAnd(Matches.TerritoryIsLand, Matches.territoryHasEnemyLandUnits(player, data)), DMatches.TerritoryIsLandAndPassable);
                    if (areaTroubleTers.isEmpty()) score -= 1000000000;
                    final List<Territory> continentTroubleTers = DUtils.GetTerritoriesWithinXDistanceOfYMatchingZAndHavingRouteMatchingA(data, ter2, Integer.MAX_VALUE, DUtils.CompMatchAnd(Matches.TerritoryIsLand, Matches.territoryHasEnemyLandUnits(player, data)), DMatches.TerritoryIsLandAndPassable);
                    if (continentTroubleTers.isEmpty()) score -= 10000000;
                    score -= CachedCalculationCenter.GetSeaRoute(data, ourUnitGroup.get(0).getTerritoryUnitIsIn(), openPort).getLength() * 100000;
                    score -= ter2.getUnits().getMatches(Matches.unitIsEnemyOf(data, player)).size() * 100;
                    score += TerritoryAttachment.get(ter2).getProduction();
                    if (score > highestUnloadingTerScore) {
                        highestUnloadingTerScore = score;
                        unloadingTer = ter2;
                        unloadingPort = openPort;
                    }
                }
                if (unloadingTer == null) continue;
                if (unfilledTransports > 0 && unloadingPort != ourUnitGroup.get(0).getTerritoryUnitIsIn()) {
                    if (filledTransports < 10) continue;
                }
                final UnitGroup ships = DUtils.CreateUnitGroupForUnits(ourUnitGroup, ourUnitGroup.get(0).getTerritoryUnitIsIn(), data);
                final String error = ships.MoveAsFarTo_CM(unloadingPort, mover);
                if (error != null) {
                    DUtils.Log(Level.FINER, "    There was an error moving ships[{0}] to unloading port({1}->{2}): {3}", ships, ourUnitGroup.get(0).getTerritoryUnitIsIn(), unloadingPort, error);
                    continue;
                }
                for (final Unit unit : (List<Unit>) Match.getMatches(ourUnitGroup, DUtils.CompMatchAnd(Matches.UnitIsLand))) {
                    final UnitGroup ug = DUtils.CreateUnitGroupForUnit(unit, unloadingPort, data);
                    final String error2 = ug.MoveAsFarTo_CM(unloadingTer, mover);
                    if (error2 != null) {
                        DUtils.Log(Level.FINER, "    There was an error moving units[{0}] to unloading ter({1}->{2}): {3}", ug, unloadingPort, unloadingTer, error2);
                        continue;
                    }
                }
            }
        }
    }

    private static List<CM_Task> GenerateTasks(final MovePackage pack) {
        final List<CM_Task> result = new ArrayList<CM_Task>();
        final GameData data = pack.Data;
        final PlayerID player = pack.Player;
        final List<Territory> ourCaps = TerritoryAttachment.getAllCapitals(player, data);
        final Match<Territory> isLandGrab = new Match<Territory>() {

            @Override
            public boolean match(final Territory ter) {
                if (!DSettings.LoadSettings().TR_enableAttackLandGrab) return false;
                if (ter.isWater()) return false;
                if (TerritoryAttachment.get(ter) == null || TerritoryAttachment.get(ter).getIsImpassible()) return false;
                if (data.getRelationshipTracker().isAllied(ter.getOwner(), player)) return false;
                if (TerritoryAttachment.get(ter) == null) return false;
                if (TerritoryAttachment.get(ter).getProduction() < 1) return false;
                if (ter.getUnits().getMatches(new CompositeMatchAnd<Unit>(Matches.unitHasDefenseThatIsMoreThanOrEqualTo(1), Matches.unitIsEnemyOf(data, player), Matches.UnitIsNotAA)).size() > 0) return false;
                return true;
            }
        };
        final List<Territory> capsAndNeighbors = new ArrayList<Territory>();
        for (final Territory cap : ourCaps) capsAndNeighbors.addAll(DUtils.GetTerritoriesWithinXDistanceOfY(data, cap, 1));
        final Match<Territory> isAttack_Stabilize = new Match<Territory>() {

            @Override
            public boolean match(final Territory ter) {
                if (!DSettings.LoadSettings().TR_enableAttackStabalize) return false;
                if (ter.isWater()) return false;
                if (TerritoryAttachment.get(ter) == null || TerritoryAttachment.get(ter).getIsImpassible()) return false;
                if (ter.getOwner() != null && data.getRelationshipTracker().isAllied(ter.getOwner(), player)) return false;
                if (GlobalCenter.IsFFAGame) {
                    if (!capsAndNeighbors.contains(ter)) return false;
                } else {
                    if (!ourCaps.contains(ter)) return false;
                }
                return true;
            }
        };
        final Match<Territory> isAttack_Offensive = new Match<Territory>() {

            @Override
            public boolean match(final Territory ter) {
                if (!DSettings.LoadSettings().TR_enableAttackOffensive) return false;
                if (ter.isWater()) return false;
                if (TerritoryAttachment.get(ter) == null || TerritoryAttachment.get(ter).getIsImpassible()) return false;
                if (ter.getOwner() != null && data.getRelationshipTracker().isAllied(ter.getOwner(), player)) return false;
                return true;
            }
        };
        final Match<Territory> isAttack_Trade = new Match<Territory>() {

            @Override
            public boolean match(final Territory ter) {
                if (!DSettings.LoadSettings().TR_enableAttackTrade) return false;
                if (ter.isWater()) return false;
                if (TerritoryAttachment.get(ter) == null || TerritoryAttachment.get(ter).getIsImpassible()) return false;
                if (ter.getOwner() != null && data.getRelationshipTracker().isAllied(ter.getOwner(), player)) return false;
                if (ter.getUnits().getMatches(new CompositeMatchAnd<Unit>(Matches.unitHasDefenseThatIsMoreThanOrEqualTo(1), Matches.unitIsEnemyOf(data, player), Matches.UnitIsNotAA)).isEmpty()) return false;
                return true;
            }
        };
        final List<Territory> tersWeCanAttack = DUtils.GetEnemyTersThatCanBeAttackedByUnitsOwnedBy(data, player);
        DUtils.Log(Level.FINE, "  Beginning task creation loop. tersWeCanAttack: {0}", tersWeCanAttack);
        for (final Territory ter : tersWeCanAttack) {
            if (isAttack_Trade.match(ter)) {
                List<Unit> possibleAttackers = DUtils.GetUnitsOwnedByPlayerThatCanReach(data, ter, player, Matches.TerritoryIsLandOrWater);
                possibleAttackers = Match.getMatches(possibleAttackers, new CompositeMatchOr<Unit>(Matches.UnitIsLand, Matches.UnitIsAir));
                final AggregateResults results = DUtils.GetBattleResults(possibleAttackers, DUtils.ToList(ter.getUnits().getUnits()), ter, data, DSettings.LoadSettings().CA_CM_determinesIfTaskCreationsWorthwhileBasedOnTakeoverChance, true);
                if (results.getAttackerWinPercent() > .5F) {
                    final float priority = DUtils.GetCMTaskPriority_Trade(data, player, ter);
                    final CM_Task task = new CM_Task(data, ter, CM_TaskType.Land_Attack_Trade, priority);
                    result.add(task);
                    DUtils.Log(Level.FINER, "    Attack_Trade task added. Ter: {0} Priority: {1}", ter.getName(), priority);
                }
            }
            if (isLandGrab.match(ter)) {
                final float priority = DUtils.GetCMTaskPriority_LandGrab(data, player, ter);
                final CM_Task task = new CM_Task(data, ter, CM_TaskType.Land_LandGrab, priority);
                result.add(task);
                DUtils.Log(Level.FINER, "    Land grab task added. Ter: {0} Priority: {1}", ter.getName(), priority);
            } else if (isAttack_Stabilize.match(ter)) {
                List<Unit> possibleAttackers = DUtils.GetUnitsOwnedByPlayerThatCanReach(data, ter, player, Matches.TerritoryIsLandOrWater);
                possibleAttackers = Match.getMatches(possibleAttackers, new CompositeMatchOr<Unit>(Matches.UnitIsLand, Matches.UnitIsAir));
                final AggregateResults results = DUtils.GetBattleResults(possibleAttackers, DUtils.ToList(ter.getUnits().getUnits()), ter, data, DSettings.LoadSettings().CA_CM_determinesIfTaskCreationsWorthwhileBasedOnTakeoverChance, true);
                if (results.getAttackerWinPercent() < .25F) continue;
                final float priority = DUtils.GetCMTaskPriority_Stabalization(data, player, ter);
                final CM_Task task = new CM_Task(data, ter, CM_TaskType.Land_Attack_Stabilize, priority);
                result.add(task);
                DUtils.Log(Level.FINER, "    Attack_Stabilize task added. Ter: {0} Priority: {1}", ter.getName(), priority);
            } else if (isAttack_Offensive.match(ter)) {
                List<Unit> possibleAttackers = DUtils.GetUnitsOwnedByPlayerThatCanReach(data, ter, player, Matches.TerritoryIsLandOrWater);
                possibleAttackers = Match.getMatches(possibleAttackers, new CompositeMatchOr<Unit>(Matches.UnitIsLand, Matches.UnitIsAir));
                final AggregateResults results = DUtils.GetBattleResults(possibleAttackers, DUtils.ToList(ter.getUnits().getUnits()), ter, data, DSettings.LoadSettings().CA_CM_determinesIfTaskCreationsWorthwhileBasedOnTakeoverChance, true);
                if (results.getAttackerWinPercent() < .20F) continue;
                final float priority = DUtils.GetCMTaskPriority_Offensive(data, player, ter);
                final CM_Task task = new CM_Task(data, ter, CM_TaskType.Land_Attack_Offensive, priority);
                result.add(task);
                DUtils.Log(Level.FINER, "    Attack_Offensive task added. Ter: {0} Priority: {1}", ter.getName(), priority);
            }
        }
        return result;
    }

    private static boolean considerAndPerformWorthwhileTasks(final MovePackage pack, final List<CM_Task> tasks) {
        @SuppressWarnings("unused") final GameData data = pack.Data;
        @SuppressWarnings("unused") final PlayerID player = pack.Player;
        final IMoveDelegate mover = pack.Mover;
        CM_Task highestPriorityTask = null;
        float highestTaskPriority = Integer.MIN_VALUE;
        for (final CM_Task task : tasks) {
            if (task.IsDisqualified()) continue;
            if (task.IsCompleted()) continue;
            final float priority = task.GetPriority();
            if (priority > highestTaskPriority) {
                highestPriorityTask = task;
                highestTaskPriority = priority;
            }
        }
        if (highestPriorityTask != null) {
            highestPriorityTask.CalculateTaskRequirements();
            highestPriorityTask.RecruitUnits();
            if (highestPriorityTask.IsPlannedAttackWorthwhile(tasks)) {
                DUtils.Log(Level.FINER, "      Task worthwhile, performing planned task.");
                highestPriorityTask.PerformTask(mover);
                highestPriorityTask.InvalidateThreatsThisTaskResists();
            } else {
                highestPriorityTask.Disqualify();
            }
        }
        if (highestPriorityTask != null) return true; else return false;
    }
}
