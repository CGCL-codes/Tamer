package games.strategy.triplea.delegate;

import games.strategy.engine.data.Change;
import games.strategy.engine.data.ChangeFactory;
import games.strategy.engine.data.CompositeChange;
import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.data.Territory;
import games.strategy.engine.data.Unit;
import games.strategy.triplea.Constants;
import games.strategy.triplea.TripleAUnit;
import games.strategy.triplea.attatchments.UnitAttachment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Sean Bridges
 * @version 1.0
 * 
 *          Tracks which transports are carrying which units. Also tracks the capacity
 *          that has been unloaded. To reset the unloaded call clearUnloadedCapacity().
 */
public class TransportTracker {

    public static int getCost(final Collection<Unit> units) {
        return MoveValidator.getTransportCost(units);
    }

    private static void assertTransport(final Unit u) {
        if (UnitAttachment.get(u.getType()).getTransportCapacity() == -1) {
            throw new IllegalStateException("Not a transport:" + u);
        }
    }

    /**
	 * Constructor.
	 */
    public TransportTracker() {
    }

    /**
	 * Returns the collection of units that the given transport is transporting.
	 * Could be null.
	 */
    public Collection<Unit> transporting(final Unit transport) {
        return new ArrayList<Unit>(((TripleAUnit) transport).getTransporting());
    }

    public boolean isTransporting(final Unit transport) {
        return !((TripleAUnit) transport).getTransporting().isEmpty();
    }

    /**
	 * Returns the collection of units that the given transport has unloaded
	 * this turn. Could be empty.
	 */
    public Collection<Unit> unloaded(final Unit transport) {
        return ((TripleAUnit) transport).getUnloaded();
    }

    public Collection<Unit> transportingAndUnloaded(final Unit transport) {
        Collection<Unit> rVal = transporting(transport);
        if (rVal == null) rVal = new ArrayList<Unit>();
        rVal.addAll(unloaded(transport));
        return rVal;
    }

    /**
	 * Returns a map of transport -> collection of transported units.
	 */
    public Map<Unit, Collection<Unit>> transporting(final Collection<Unit> units) {
        final Map<Unit, Collection<Unit>> returnVal = new HashMap<Unit, Collection<Unit>>();
        for (final Unit transported : units) {
            final Unit transport = transportedBy(transported);
            Collection<Unit> transporting = null;
            if (transport != null) transporting = transporting(transport);
            if (transporting != null) {
                returnVal.put(transport, transporting);
            }
        }
        return returnVal;
    }

    /**
	 * Return the transport that holds the given unit. Could be null.
	 */
    public Unit transportedBy(final Unit unit) {
        return ((TripleAUnit) unit).getTransportedBy();
    }

    private boolean isNonCombat(final GameData data) {
        return data.getSequence().getStep().getName().endsWith("NonCombatMove");
    }

    public Change unloadTransportChange(final TripleAUnit unit, final Territory territory, final PlayerID id, final boolean dependentBattle) {
        final CompositeChange change = new CompositeChange();
        final TripleAUnit transport = (TripleAUnit) transportedBy(unit);
        if (transport == null) return change;
        assertTransport(transport);
        if (!transport.getTransporting().contains(unit)) {
            throw new IllegalStateException("Not being carried, unit:" + unit + " transport:" + transport);
        }
        final ArrayList<Unit> newUnloaded = new ArrayList<Unit>(transport.getUnloaded());
        newUnloaded.add(unit);
        change.add(ChangeFactory.unitPropertyChange(unit, territory, TripleAUnit.UNLOADED_TO));
        if (!isNonCombat(unit.getData())) {
            change.add(ChangeFactory.unitPropertyChange(unit, true, TripleAUnit.UNLOADED_IN_COMBAT_PHASE));
            change.add(ChangeFactory.unitPropertyChange(unit, true, TripleAUnit.UNLOADED_AMPHIBIOUS));
            change.add(ChangeFactory.unitPropertyChange(transport, true, TripleAUnit.UNLOADED_IN_COMBAT_PHASE));
            change.add(ChangeFactory.unitPropertyChange(transport, true, TripleAUnit.UNLOADED_AMPHIBIOUS));
        }
        if (!dependentBattle) {
            change.add(ChangeFactory.unitPropertyChange(unit, null, TripleAUnit.TRANSPORTED_BY));
        }
        change.add(ChangeFactory.unitPropertyChange(transport, newUnloaded, TripleAUnit.UNLOADED));
        return change;
    }

    public Change loadTransportChange(final TripleAUnit transport, final Unit unit, final PlayerID id) {
        assertTransport(transport);
        final CompositeChange change = new CompositeChange();
        change.add(ChangeFactory.unitPropertyChange(unit, transport, TripleAUnit.TRANSPORTED_BY));
        final Collection<Unit> newCarrying = new ArrayList<Unit>(transport.getTransporting());
        if (newCarrying.contains(unit)) {
            throw new IllegalStateException("Already carrying, transport:" + transport + " unt:" + unit);
        }
        newCarrying.add(unit);
        change.add(ChangeFactory.unitPropertyChange(unit, Boolean.TRUE, TripleAUnit.LOADED_THIS_TURN));
        change.add(ChangeFactory.unitPropertyChange(transport, true, TripleAUnit.LOADED_THIS_TURN));
        if (transport.getWasInCombat()) {
            change.add(ChangeFactory.unitPropertyChange(transport, true, TripleAUnit.LOADED_AFTER_COMBAT));
        }
        return change;
    }

    public Change combatTransportChange(final TripleAUnit transport, final PlayerID id) {
        assertTransport(transport);
        final CompositeChange change = new CompositeChange();
        change.add(ChangeFactory.unitPropertyChange(transport, true, TripleAUnit.WAS_IN_COMBAT));
        return change;
    }

    public int getAvailableCapacity(final Unit unit) {
        final UnitAttachment ua = UnitAttachment.get(unit.getType());
        if (ua.getTransportCapacity() == -1 || (unit.getData().getProperties().get(Constants.PACIFIC_THEATER, false) && ua.getIsDestroyer() && !unit.getOwner().getName().equals("Japanese"))) return 0;
        final int capacity = ua.getTransportCapacity();
        final int used = getCost(transporting(unit));
        final int unloaded = getCost(unloaded(unit));
        return capacity - used - unloaded;
    }

    public Change endOfRoundClearStateChange(final GameData data) {
        final CompositeChange change = new CompositeChange();
        for (final Unit unit : data.getUnits().getUnits()) {
            final TripleAUnit taUnit = (TripleAUnit) unit;
            if (!taUnit.getUnloaded().isEmpty()) {
                change.add(ChangeFactory.unitPropertyChange(unit, Collections.EMPTY_LIST, TripleAUnit.UNLOADED));
            }
            if (taUnit.getWasLoadedThisTurn()) {
                change.add(ChangeFactory.unitPropertyChange(unit, Boolean.FALSE, TripleAUnit.LOADED_THIS_TURN));
            }
            if (taUnit.getUnloadedTo() != null) {
                change.add(ChangeFactory.unitPropertyChange(unit, null, TripleAUnit.UNLOADED_TO));
            }
            if (taUnit.getWasUnloadedInCombatPhase()) {
                change.add(ChangeFactory.unitPropertyChange(unit, Boolean.FALSE, TripleAUnit.UNLOADED_IN_COMBAT_PHASE));
            }
            if (taUnit.getWasInCombat()) {
                change.add(ChangeFactory.unitPropertyChange(unit, Boolean.FALSE, TripleAUnit.WAS_IN_COMBAT));
            }
            if (taUnit.getWasAmphibious()) {
                change.add(ChangeFactory.unitPropertyChange(unit, Boolean.FALSE, TripleAUnit.UNLOADED_AMPHIBIOUS));
            }
        }
        return change;
    }

    public Collection<Unit> getUnitsLoadedOnAlliedTransportsThisTurn(final Collection<Unit> units) {
        final Collection<Unit> rVal = new ArrayList<Unit>();
        for (final Unit u : units) {
            final TripleAUnit taUnit = (TripleAUnit) u;
            if (taUnit.getWasLoadedThisTurn() && taUnit.getTransportedBy() != null && !taUnit.getTransportedBy().getOwner().equals(taUnit.getOwner())) {
                rVal.add(u);
            }
        }
        return rVal;
    }

    public boolean hasTransportUnloadedInPreviousPhase(final Unit transport) {
        final Collection<Unit> unloaded = ((TripleAUnit) transport).getUnloaded();
        for (final Unit u : unloaded) {
            final TripleAUnit taUnit = (TripleAUnit) u;
            if (isNonCombat(transport.getData()) && taUnit.getWasUnloadedInCombatPhase()) return true;
        }
        return false;
    }

    private boolean isWW2V2(final GameData data) {
        return games.strategy.triplea.Properties.getWW2V2(data);
    }

    private boolean isTransportUnloadRestricted(final GameData data) {
        return games.strategy.triplea.Properties.getTransportUnloadRestricted(data);
    }

    public boolean isTransportUnloadRestrictedToAnotherTerritory(final Unit transport, final Territory territory) {
        final Collection<Unit> unloaded = ((TripleAUnit) transport).getUnloaded();
        if (unloaded.isEmpty()) return false;
        final GameData data = transport.getData();
        for (final Unit u : unloaded) {
            final TripleAUnit taUnit = (TripleAUnit) u;
            if (isWW2V2(data) || isTransportUnloadRestricted(data)) {
                if (!taUnit.getUnloadedTo().equals(territory)) return true;
            } else {
                if (!isNonCombat(transport.getData()) && !taUnit.getUnloadedTo().equals(territory)) return true;
            }
        }
        return false;
    }

    public Territory getTerritoryTransportHasUnloadedTo(final Unit transport) {
        final Collection<Unit> unloaded = ((TripleAUnit) transport).getUnloaded();
        if (unloaded.isEmpty()) return null;
        final Iterator<Unit> iter = unloaded.iterator();
        return ((TripleAUnit) iter.next()).getUnloadedTo();
    }

    public boolean isTransportUnloadRestrictedInNonCombat(final Unit transport) {
        final TripleAUnit taUnit = (TripleAUnit) transport;
        if (isNonCombat(transport.getData()) && taUnit.getWasInCombat() && taUnit.getWasLoadedAfterCombat()) return true;
        return false;
    }
}
