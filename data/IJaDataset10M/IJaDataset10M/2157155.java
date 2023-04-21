package org.jcrpg.world.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Logger;
import org.jcrpg.game.element.EncounterPhaseLineup;
import org.jcrpg.game.element.PlacementMatrix;
import org.jcrpg.game.element.TurnActUnitTopology;
import org.jcrpg.threed.J3DCore;
import org.jcrpg.world.ai.EntityFragments.EntityFragment;
import org.jcrpg.world.ai.player.PartyInstance;
import org.jcrpg.world.intelligence.history.HistoricalEvent;

/**
 * Class containing a possible encounter's data with most of the subdata related to units
 * lineups and such. Should be updated turn by turn! 
 * @author illes
 */
public class EncounterInfo {

    static final Logger logger = Logger.getLogger(EncounterInfo.class.getName());

    public boolean active = false;

    public boolean initiatorIsPlayerFriendly = false;

    /**
	 * The initiator group that faces the encounter.
	 */
    public EntityInstance subject;

    public EntityFragment subjectFragment;

    /**
	 * If player is inside this should be always set to the player fragment,
	 * because UI will rely on this field!
	 */
    public EntityFragment playerIfPresent = null;

    public EncounterUnitData playerPartyUnitData = null;

    /**
	 * Encountered instances and their common radius ratios and middle point data, including subject and target as well.
	 * Never forget to append the subject's data to these, encountered entities need
	 * this data for easy selection of initiator for acts.
	 */
    public TreeMap<EncounterUnit, int[][]> encountered = new TreeMap<EncounterUnit, int[][]>();

    /**
	 * encountered instances' subgroupIds facing the possible encounter, including subject and target as well.
	 * Never forget to append the subject's data to these, encountered entities need
	 * this data for easy selection of initiator for acts.
	 */
    public TreeMap<EncounterUnit, int[]> encounteringGroupIds = new TreeMap<EncounterUnit, int[]>();

    public TreeMap<EncounterUnit, ArrayList<EncounterUnit>> encounteringSubUnits = new TreeMap<EncounterUnit, ArrayList<EncounterUnit>>();

    /**
	 * (Subject/initiator's) own group ids for a given fragment of the target/enctountered.
	 * You shouldn't put subject data into THIS one's keys, only to the values! all other hashmaps need subject data for key.
	 */
    public TreeMap<EncounterUnit, int[]> targetUnitsMappedForSubjectGroupIds = new TreeMap<EncounterUnit, int[]>();

    public TreeMap<EncounterUnit, ArrayList<EncounterUnit>> targetUnitsMappedToSubjectSubUnits = new TreeMap<EncounterUnit, ArrayList<EncounterUnit>>();

    /**
	 * The subgroups of the initiator group which face the encountered.  
	 */
    public ArrayList<Integer> subjectGroupIds = new ArrayList<Integer>();

    public ArrayList<EncounterUnit> subjectSubUnits = new ArrayList<EncounterUnit>();

    public EncounterInfo(EntityFragment subjectFragment, EntityFragment player) {
        super();
        this.subject = subjectFragment.instance;
        this.subjectFragment = subjectFragment;
        playerIfPresent = player;
    }

    public EncounterInfo copy() {
        EncounterInfo r = new EncounterInfo(subjectFragment, playerIfPresent);
        r.active = active;
        r.encountered.putAll(encountered);
        r.encounteringGroupIds.putAll(encounteringGroupIds);
        r.encounteringSubUnits.putAll(encounteringSubUnits);
        r.targetUnitsMappedForSubjectGroupIds.putAll(targetUnitsMappedForSubjectGroupIds);
        r.targetUnitsMappedToSubjectSubUnits.putAll(targetUnitsMappedToSubjectSubUnits);
        r.subjectGroupIds.addAll(subjectGroupIds);
        r.subjectSubUnits.addAll(subjectSubUnits);
        return r;
    }

    /**
	 * Makes a copy of the info filtering for the fragment and groupid.
	 * @param f Fragment.
	 * @param groupId GroupId.
	 * @return new EncounterInfo.
	 */
    public EncounterInfo copyForFragment(EntityFragment f) {
        logger.info("COPYING FOR FRAGMENT: " + f.getName());
        EncounterInfo r = new EncounterInfo(subjectFragment, playerIfPresent);
        r.active = active;
        r.encountered.put(f, encountered.get(f));
        r.encounteringGroupIds.put(f, encounteringGroupIds.get(f));
        r.encountered.put(subjectFragment, encountered.get(subjectFragment));
        if (encounteringSubUnits.get(subjectFragment) != null) r.encounteringSubUnits.put(subjectFragment, encounteringSubUnits.get(subjectFragment));
        if (encounteringGroupIds.get(subjectFragment) != null) r.encounteringGroupIds.put(subjectFragment, encounteringGroupIds.get(subjectFragment));
        int[] gIds = targetUnitsMappedForSubjectGroupIds.get(f);
        if (gIds != null) {
            for (int i = 0; i < gIds.length; i++) {
                if (subjectGroupIds.contains(gIds[i])) continue;
                r.subjectGroupIds.add(gIds[i]);
            }
        }
        return r;
    }

    /**
	 * This should be used to filter out neutrals in a Turn Act phase.
	 * @param notThePlayer Tells if player should be left even if neutral
	 * @param player the player entity. 
	 */
    public ArrayList<EncounterUnit> filterNeutralsForSubjectBeforeTurnAct(boolean notThePlayer, PartyInstance player) {
        ArrayList<EncounterUnit> keysToRemove = new ArrayList<EncounterUnit>();
        for (EncounterUnit unit : encountered.keySet()) {
            if (unit == subjectFragment) continue;
            if (notThePlayer && (unit == player.theFragment || player.orderedParty.contains(unit))) continue;
            int level = unit.getRelationLevel(subjectFragment);
            if (level == EntityScaledRelationType.NEUTRAL) {
                if (J3DCore.LOGGING()) logger.finer("REMOVING NEUTRAL: " + unit);
                keysToRemove.add(unit);
            }
        }
        encountered.keySet().removeAll(keysToRemove);
        encounteringSubUnits.keySet().removeAll(keysToRemove);
        encounteringGroupIds.keySet().removeAll(keysToRemove);
        updateEncounterDataLists();
        return keysToRemove;
    }

    /**
	 * Stores transient member instances for Player involved encounters.
	 */
    public transient TreeMap<Integer, ArrayList<EntityMemberInstance>> generatedGroups;

    /**
	 * Stores a set of generated members for a given groupId (transiently).
	 * @param groupId
	 * @param members
	 */
    public void setGroupMemberInstances(int groupId, ArrayList<EntityMemberInstance> members) {
        if (generatedGroups == null) generatedGroups = new TreeMap<Integer, ArrayList<EntityMemberInstance>>();
        generatedGroups.put(groupId, members);
    }

    /**
	 * Appends to initiator's (subject) group ids for a given met target unit.
	 * @param target
	 * @param groupIds
	 */
    public void mapSubjectGroupIdsToTargetUnit(EncounterUnit target, int[] groupIds) {
        for (int i = 0; i < groupIds.length; i++) {
            if (subjectGroupIds.contains(groupIds[i])) continue;
            subjectGroupIds.add(groupIds[i]);
        }
        targetUnitsMappedForSubjectGroupIds.put(target, groupIds);
    }

    /**
	 * Appends to initiator's (subject) subunits those that met a given target unit.
	 * @param target
	 * @param subUnits
	 */
    public void mapSubjectSubUnitsToTargetUnit(EncounterUnit target, ArrayList<EncounterUnit> subUnits) {
        for (EncounterUnit eu : subUnits) {
            if (subjectSubUnits.contains(eu)) continue;
            subjectSubUnits.add(eu);
        }
        targetUnitsMappedToSubjectSubUnits.put(target, subUnits);
    }

    /**
	 * Returns all groups and subunits in a given encounter except groups and subunits belonging to filtered unit.
	 * @param filtered
	 * @return
	 */
    public int getGroupsAndSubUnitsCount(EncounterUnit filtered) {
        int allSize = 0;
        for (EncounterUnit unit : encountered.keySet()) {
            if (filtered != null && unit == filtered) continue;
            int[] groupIds = encounteringGroupIds.get(unit);
            if (groupIds != null) for (int in : groupIds) {
                int size = unit.getGroupSize(in);
                if (size > 0) allSize++;
            }
            ArrayList<EncounterUnit> subUnits = encounteringSubUnits.get(unit);
            if (subUnits != null) {
                allSize += subUnits.size();
            }
            if (unit instanceof EntityFragment) {
                if (((EntityFragment) unit).alwaysIncludeFollowingMembers) {
                    ArrayList<PersistentMemberInstance> members = ((EntityFragment) unit).getFollowingMembers();
                    for (PersistentMemberInstance p : members) {
                        if (!subUnits.contains(p)) {
                            allSize++;
                        }
                    }
                }
            }
        }
        return allSize;
    }

    ArrayList<EncounterUnitData> encounterUnitDataList = null;

    ArrayList<EncounterUnitData> removedEncounterUnitDataList = null;

    private EncounterPhaseLineup encounterPhaseLineup = null;

    private TurnActUnitTopology topology = null;

    /**
	 * Updates encounter data list, filtering out destroyed or leaving / orphan units.
	 */
    public void updateEncounterDataLists() {
        if (encounterUnitDataList == null) {
            initEncounterDataLists();
        } else {
            ArrayList<EncounterUnitData> toRemove = new ArrayList<EncounterUnitData>();
            for (EncounterUnitData unitData : encounterUnitDataList) {
                EncounterUnit key = unitData.parent;
                if (unitData.isGroupId) {
                    if (unitData.getSize() < 1) toRemove.add(unitData);
                    if (encountered.containsKey(key)) continue;
                    toRemove.add(unitData);
                    if (J3DCore.LOGGING()) logger.finer("REMOVING ORPHAN: " + unitData.parent.getGroupType(unitData.groupId).getName() + " - " + unitData.parent.getName());
                } else {
                    if (unitData.isDead() || unitData.isNeutralized()) {
                        toRemove.add(unitData);
                        continue;
                    }
                    if (key instanceof EntityFragment) {
                        if (((EntityFragment) key).alwaysIncludeFollowingMembers) {
                            if (((EntityFragment) key).getFollowingMembers().contains(unitData.subUnit)) {
                                if (unitData.isDead() || unitData.isNeutralized()) {
                                    toRemove.add(unitData);
                                }
                                continue;
                            }
                        }
                    }
                    if (encounteringSubUnits.get(key) != null && encounteringSubUnits.get(key).contains(unitData.subUnit)) {
                        continue;
                    }
                    for (EncounterUnit unitKey : encounteringSubUnits.keySet()) {
                        if (J3DCore.LOGGING()) logger.finer("--- " + unitKey.getName());
                        if (encounteringSubUnits.get(unitKey) != null) for (EncounterUnit u : encounteringSubUnits.get(unitKey)) {
                            if (J3DCore.LOGGING()) logger.finer("    " + u.getName());
                        }
                    }
                    if (J3DCore.LOGGING()) logger.finer("REMOVING ORPHAN: " + unitData.subUnit.getName() + " - " + unitData.parent.getName());
                    toRemove.add(unitData);
                }
            }
            encounterUnitDataList.removeAll(toRemove);
            removedEncounterUnitDataList.addAll(toRemove);
            if (topology != null) {
                topology.removeUnits(toRemove);
            }
        }
    }

    /**
	 * initializes encounter unit data list and other lists.
	 */
    public void initEncounterDataLists() {
        encounterUnitDataList = new ArrayList<EncounterUnitData>();
        removedEncounterUnitDataList = new ArrayList<EncounterUnitData>();
        for (EncounterUnit u : encounteringSubUnits.keySet()) {
            ArrayList<EncounterUnit> u2 = encounteringSubUnits.get(u);
            if (u2 != null) for (EncounterUnit u3 : u2) {
                if (J3DCore.LOGGING()) logger.finer(subjectFragment.getName() + " : " + u.getName() + " : " + u3.getName());
            }
        }
        ArrayList<EncounterUnitData> list = encounterUnitDataList;
        for (EncounterUnit unit : encountered.keySet()) {
            if (J3DCore.LOGGING()) logger.finer("--" + unit.getName());
            int[] groupIds = encounteringGroupIds.get(unit);
            if (groupIds != null) for (int in : groupIds) {
                int size = unit.getGroupSize(in);
                if (size > 0) {
                    EncounterUnitData data = new EncounterUnitData(unit, in);
                    if (unit == J3DCore.getInstance().gameState.player.theFragment) {
                        playerPartyUnitData = data;
                    }
                    list.add(data);
                }
            }
            ArrayList<EncounterUnit> subUnits = encounteringSubUnits.get(unit);
            if (J3DCore.LOGGING()) logger.finer(subUnits + " " + (subUnits == null ? "" : subUnits.size()));
            if (subUnits != null) {
                for (EncounterUnit u : subUnits) {
                    EncounterUnit parent = unit;
                    if (unit instanceof PersistentMemberInstance) {
                        if (((PersistentMemberInstance) unit).getParentFragment() != null) parent = ((PersistentMemberInstance) unit).getParentFragment();
                    }
                    EncounterUnitData data = new EncounterUnitData(parent, u);
                    if (!data.isDead() && !data.isNeutralized()) {
                        list.add(data);
                        if (unit instanceof PersistentMemberInstance) {
                            ((PersistentMemberInstance) unit).encounterData = data;
                        }
                    }
                }
            }
            if (unit instanceof EntityFragment) {
                if (((EntityFragment) unit).alwaysIncludeFollowingMembers) {
                    ArrayList<PersistentMemberInstance> members = ((EntityFragment) unit).getFollowingMembers();
                    for (PersistentMemberInstance p : members) {
                        if (subUnits == null || !subUnits.contains(p)) {
                            EncounterUnit parent = unit;
                            if (((PersistentMemberInstance) p).getParentFragment() != null) parent = ((PersistentMemberInstance) p).getParentFragment();
                            EncounterUnitData data = new EncounterUnitData(parent, p);
                            if (!data.isDead() && !data.isNeutralized()) {
                                list.add(data);
                                if (p instanceof PersistentMemberInstance) {
                                    ((PersistentMemberInstance) p).encounterData = data;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * Updates and returns actualized encountered unit data list.
	 * @param filtered
	 * @return
	 */
    public ArrayList<EncounterUnitData> getEncounterUnitDataList(EncounterUnit filtered) {
        updateEncounterDataLists();
        ArrayList<EncounterUnitData> filteredList = encounterUnitDataList;
        if (filtered != null) {
            filteredList = new ArrayList<EncounterUnitData>();
            for (EncounterUnitData unitData : encounterUnitDataList) {
                if (unitData.parent == filtered || !unitData.isGroupId && unitData.subUnit == filtered) continue;
                filteredList.add(unitData);
            }
        }
        if (J3DCore.LOGGING()) logger.finer(" + FilterList size " + filteredList.size() + " Filter = " + (filtered == null ? "null" : filtered.getName()));
        for (EncounterUnitData unitData : encounterUnitDataList) {
            logger.finest("Filtered Units: " + unitData.getName());
        }
        return filteredList;
    }

    public EncounterPhaseLineup getEncounterPhaseLineup() {
        return encounterPhaseLineup;
    }

    public void setEncounterPhaseLineup(EncounterPhaseLineup encounterPhaseLineup) {
        this.encounterPhaseLineup = encounterPhaseLineup;
    }

    public TurnActUnitTopology getTopology() {
        return topology;
    }

    public void setTopology(TurnActUnitTopology topology) {
        this.topology = topology;
    }

    private int phase = -1;

    public void setEncounterPhaseStatus() {
        phase = Ecology.PHASE_ENCOUNTER;
    }

    public void setTurnActPhaseCombatStatus() {
        phase = Ecology.PHASE_TURNACT_COMBAT;
    }

    public void setTurnActPhaseSocialRivalryStatus() {
        phase = Ecology.PHASE_TURNACT_SOCIAL_RIVALRY;
    }

    public int getPhase() {
        return phase;
    }

    PlacementMatrix encounterMatrix = null;

    PlacementMatrix turnActMatrix = null;

    /**
	 * create the startup placement matrix for pseudo visualization.
	 * Based on phase fills up the placement matrix.
	 * @return
	 */
    public void initPlacementMatrixForPhase() {
        PlacementMatrix m = new PlacementMatrix();
        if (phase == Ecology.PHASE_ENCOUNTER) {
            if (J3DCore.LOGGING()) logger.finer("EncounterInfo.initPlacementMatrix PHASE_ENCOUNTER " + getEncounterPhaseLineup().orderedList.size());
            for (ArrayList<EncounterUnitData> dList : getEncounterPhaseLineup().orderedList.values()) {
                for (EncounterUnitData d : dList) {
                    m.addAhead(d, 0);
                }
            }
            encounterMatrix = m;
        } else if (phase == Ecology.PHASE_TURNACT_COMBAT || phase == Ecology.PHASE_TURNACT_SOCIAL_RIVALRY) {
            int lineCount = 0;
            for (ArrayList<EncounterUnitData> dList : getTopology().getEnemyLineup().lines) {
                int line = lineCount > 3 ? 3 : lineCount;
                for (EncounterUnitData d : dList) {
                    m.addAhead(d, line);
                }
                lineCount++;
            }
            lineCount = 0;
            for (ArrayList<EncounterUnitData> dList : getTopology().getFriendlyLineup().lines) {
                int line = lineCount > 3 ? 4 : lineCount;
                for (EncounterUnitData d : dList) {
                    m.addBehind(d, line);
                }
                lineCount++;
            }
            turnActMatrix = m;
        }
    }

    public PlacementMatrix getEncounterMatrix() {
        return encounterMatrix;
    }

    public void setEncounterMatrix(PlacementMatrix encounterMatrix) {
        this.encounterMatrix = encounterMatrix;
    }

    public PlacementMatrix getTurnActMatrix() {
        return turnActMatrix;
    }

    public void setTurnActMatrix(PlacementMatrix turnActMatrix) {
        this.turnActMatrix = turnActMatrix;
    }

    public PlacementMatrix getCurrentPhaseMatrix() {
        if (phase == Ecology.PHASE_ENCOUNTER) {
            return encounterMatrix;
        } else {
            return turnActMatrix;
        }
    }

    public ArrayList<EncounterUnitData> getRemovedEncounterUnitDataList() {
        return removedEncounterUnitDataList;
    }

    public void fillRanksForEncounter(boolean topologyNeeded) {
        EncounterInfo info = this;
        TurnActUnitTopology topology = new TurnActUnitTopology(info);
        if (topologyNeeded) info.setTopology(topology);
        if (J3DCore.LOGGING()) logger.finer("{ fillRanksForBattleCalculator }");
        for (EncounterUnitData unit : info.getEncounterUnitDataList(null)) {
            int line = 0;
            int level = unit.getRelationLevel(info.subjectFragment);
            if (level < EntityScaledRelationType.NEUTRAL) {
                unit.friendly = false;
            } else if (level == EntityScaledRelationType.NEUTRAL) {
                if (unit.parent == info.playerIfPresent) {
                    unit.partyMember = true;
                    PartyInstance party = (PartyInstance) ((EntityFragment) unit.parent).instance;
                    int index = party.orderedParty.indexOf(unit.getFirstLivingMember());
                    line = index / 2;
                }
                if (unit.parent.getFragment().instance == info.subject) {
                    unit.friendly = true;
                } else {
                    unit.friendly = false;
                }
            }
            if (level > EntityScaledRelationType.NEUTRAL) {
                unit.friendly = true;
            }
            logger.finest("" + (level > EntityScaledRelationType.NEUTRAL) + " Friendliness check U / Subject U: " + unit.parent.getFragment().instance.description.getName() + " -- " + info.subject.description.getName() + " : REL LVL = " + level + ", FR:" + unit.friendly);
            if (topologyNeeded) info.getTopology().addUnitPushing(unit, line);
        }
        if (J3DCore.getInstance().gameState.player == info.subject) {
            initiatorIsPlayerFriendly = true;
        } else {
            if (info.subjectFragment.getRelationLevel(J3DCore.getInstance().gameState.player.theFragment) > EntityScaledRelationType.NEUTRAL) {
                initiatorIsPlayerFriendly = true;
            } else {
                initiatorIsPlayerFriendly = false;
            }
        }
    }

    /**
	 * Stores the event of the encounter for all the units related.
	 * @param event
	 */
    public void storeHistoricalEvent(HistoricalEvent event) {
        for (EncounterUnitData d : getEncounterUnitDataList(null)) {
            d.getUnit().storeHistoricalEvent(1.0f, null, event);
        }
    }

    public void clear() {
        subject = null;
        encountered.clear();
        encounteringGroupIds.clear();
        encounteringSubUnits.clear();
        targetUnitsMappedForSubjectGroupIds.clear();
        targetUnitsMappedToSubjectSubUnits.clear();
        encounterMatrix = null;
        turnActMatrix = null;
        playerIfPresent = null;
        encounterPhaseLineup = null;
        topology = null;
        if (generatedGroups != null) generatedGroups.clear();
        if (subjectGroupIds != null) subjectGroupIds.clear();
        if (subjectGroupIds != null) subjectGroupIds.clear();
        playerPartyUnitData = null;
        if (removedEncounterUnitDataList != null) removedEncounterUnitDataList.clear();
        subjectFragment = null;
    }
}
