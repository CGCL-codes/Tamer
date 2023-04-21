package org.jcrpg.game.logic.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.jcrpg.util.HashUtil;
import org.jcrpg.world.ai.DistanceBasedBoundary;
import org.jcrpg.world.ai.EncounterInfo;
import org.jcrpg.world.ai.EncounterUnitData;
import org.jcrpg.world.ai.EntityInstance;
import org.jcrpg.world.ai.EntityScaledRelationType;
import org.jcrpg.world.ai.EntityFragments.EntityFragment;
import org.jcrpg.world.intelligence.history.HistoricalEvent;

/**
 * Calculator for quick AI-AI battles while liveOneTurn in Ecology goes on and units fight.
 * @author illes
 *
 */
public class BattleCalculator {

    public BattleCalculator(EncounterInfo encounter, EntityInstance initiatorInstance, EntityFragment initiatorFragment) {
        super();
        this.encounter = encounter;
        this.initiatorInstance = initiatorInstance;
        this.initiatorFragment = initiatorFragment;
    }

    Logger logger = Logger.getLogger(BattleCalculator.class.getName());

    int powerInitiator;

    int powerTarget;

    ArrayList<EncounterUnitData> initiators;

    ArrayList<EncounterUnitData> targets;

    private HashMap<WeightedSkillGroupUsage, Float> initiatorMeans;

    private HashMap<WeightedSkillGroupUsage, Float> targetMeans;

    public BattleResult runBattle(int seed) {
        logger.info("* RUN BATTLE FOR " + initiatorInstance.getNumericId() + " " + initiatorFragment.getDescription().getClass());
        encounter.initEncounterDataLists();
        ArrayList<EncounterUnitData> list = encounter.getEncounterUnitDataList(null);
        encounter.fillRanksForEncounter(false);
        initiators = new ArrayList<EncounterUnitData>();
        targets = new ArrayList<EncounterUnitData>();
        for (EncounterUnitData d : list) {
            if (d.isFriendly()) {
                initiators.add(d);
            } else {
                targets.add(d);
            }
            logger.info(d.toString());
        }
        calculateCurrentPowerLevels();
        logger.info("Powers of combatants: " + powerInitiator + " vs. " + powerTarget);
        ArrayList<EncounterUnitData> originalInitiators = initiators;
        ArrayList<EncounterUnitData> originalTargets = targets;
        initiators = getMotivatedUnitsForBattle(powerInitiator, powerTarget, initiators, targets);
        if (initiators.size() == 0) {
            BattleResult result = new BattleResult();
            HistoricalEvent hE = new HistoricalEvent();
            hE.act = org.jcrpg.world.intelligence.history.event.Militaristic.class;
            hE.setActor(HistoricalEvent.convertToHistoricalInstanceMap(originalInitiators));
            hE.setTarget(HistoricalEvent.convertToHistoricalInstanceMap(targets));
            hE.successLevel = hE.FAILURE;
            hE.boundary = new DistanceBasedBoundary(initiatorFragment.getEncounterBoundary());
            hE.setTimeParameters(initiatorFragment.instance.world.engine.getWorldMeanTime());
            result.setHistoricalEvent(hE);
            return result;
        } else {
            targets = getMotivatedUnitsForBattle(powerInitiator, powerTarget, targets, initiators);
            if (targets.size() == 0) {
                BattleResult result = new BattleResult();
                HistoricalEvent hE = new HistoricalEvent();
                hE.act = org.jcrpg.world.intelligence.history.event.Militaristic.class;
                hE.setActor(HistoricalEvent.convertToHistoricalInstanceMap(initiators));
                hE.setTarget(HistoricalEvent.convertToHistoricalInstanceMap(originalTargets));
                hE.successLevel = hE.LARGE;
                hE.boundary = new DistanceBasedBoundary(initiatorFragment.getEncounterBoundary());
                hE.setTimeParameters(initiatorFragment.instance.world.engine.getWorldMeanTime());
                result.setHistoricalEvent(hE);
                return result;
            }
        }
        calculateCurrentPowerLevels();
        logger.info("Powers of non-running combatants: " + powerInitiator + " vs. " + powerTarget);
        initiatorMeans = getUsedMeans(initiators, targets);
        targetMeans = getUsedMeans(targets, initiators);
        BattleResult result = doBattle(seed);
        return result;
    }

    public void calculateCurrentPowerLevels() {
        powerInitiator = getPowerLevel(initiators);
        powerTarget = getPowerLevel(targets);
    }

    private int getPowerLevel(ArrayList<EncounterUnitData> list) {
        int powerLevel = 0;
        for (EncounterUnitData d : list) {
            int power = d.getUnit().getLevel() * d.getSize();
            powerLevel += power;
        }
        return powerLevel;
    }

    public BattleResult doBattle(int seed) {
        BattleResult result = new BattleResult();
        Float initiatorNeutralizationRatio = getNeutralizationRatio(initiatorMeans);
        Float targetNeutralizationRatio = getNeutralizationRatio(targetMeans);
        int iSeed = getSeedNumber(initiators);
        int tSeed = getSeedNumber(targets);
        logger.finest("SEEDS: " + seed + " I: " + iSeed + " T: " + tSeed);
        for (WeightedSkillGroupUsage skillGroup : initiatorMeans.keySet()) {
            logger.fine("*INITIATOR MEAN: " + skillGroup.SKILL_GROUP + " " + initiatorMeans.get(skillGroup));
        }
        for (WeightedSkillGroupUsage skillGroup : targetMeans.keySet()) {
            logger.fine("#TARGET MEAN: " + skillGroup.SKILL_GROUP + " " + targetMeans.get(skillGroup));
        }
        int rollOfInitiator = HashUtil.mixPercentage(iSeed + tSeed, tSeed, seed) + 1;
        rollOfInitiator = rollOfInitiator * rollOfInitiator * powerInitiator;
        int rollOfTarget = HashUtil.mixPercentage(tSeed + iSeed, iSeed, seed) + 1;
        rollOfTarget = rollOfTarget * rollOfTarget * powerTarget;
        logger.fine("rollOfInitiator = " + rollOfInitiator + " | neutralization: " + initiatorNeutralizationRatio);
        logger.fine("rollOfTarget = " + rollOfTarget + " | neutralization: " + targetNeutralizationRatio);
        float impactRatioOnTarget = rollOfInitiator / rollOfTarget;
        float impactRatioOnInitiator = rollOfTarget / rollOfInitiator;
        applyImpact(initiators, seed + iSeed + tSeed, impactRatioOnTarget, targets, initiatorNeutralizationRatio);
        applyImpact(targets, seed + iSeed + tSeed + 1, impactRatioOnInitiator, initiators, targetNeutralizationRatio);
        HistoricalEvent hE = new HistoricalEvent();
        hE.act = org.jcrpg.world.intelligence.history.event.Militaristic.class;
        hE.setActor(HistoricalEvent.convertToHistoricalInstanceMap(initiators));
        hE.setTarget(HistoricalEvent.convertToHistoricalInstanceMap(targets));
        hE.successLevel = hE.NEUTRAL;
        hE.boundary = new DistanceBasedBoundary(initiatorFragment.getEncounterBoundary());
        hE.setTimeParameters(initiatorFragment.instance.world.engine.getWorldMeanTime());
        result.setHistoricalEvent(hE);
        return result;
    }

    /**
	 * 
	 * @param source who impacts
	 * @param seed seed
	 * @param impactRatio impact ratio (higher the worse)
	 * @param units Apply on which units (target)
	 * @param neutralizationRatio How high the source is trying to neutralize instead of destroy
	 */
    private void applyImpact(ArrayList<EncounterUnitData> source, int seed, float impactRatio, ArrayList<EncounterUnitData> units, float neutralizationRatio) {
        int sumOfRolls = 0;
        HashMap<EncounterUnitData, Float> impactAbsorbRolls = new HashMap<EncounterUnitData, Float>();
        for (EncounterUnitData unit : units) {
            float percent = HashUtil.mixPercentage(seed++, seed + 1, seed + 2);
            sumOfRolls += percent * unit.getSize() * unit.getUnit().getLevel();
            impactAbsorbRolls.put(unit, percent * unit.getSize() * unit.getUnit().getLevel());
        }
        for (EncounterUnitData unit : units) {
            Float absorb = impactAbsorbRolls.get(unit);
            absorb = absorb / sumOfRolls;
            impactAbsorbRolls.put(unit, absorb);
            logger.info("Absorbing ratio " + unit.getName() + " : " + absorb + " Impact ratio: " + impactRatio + " neutralization: " + neutralizationRatio);
            unit.applyRatioBasedFullBattleImpact(source, absorb, impactRatio, neutralizationRatio);
        }
    }

    private int getSeedNumber(ArrayList<EncounterUnitData> list) {
        int r = 0;
        for (EncounterUnitData d : list) {
            logger.finest("getSeedNumber " + d.getName() + " NID: " + d.getUnit().getNumericId() + " GID: " + d.getGroupId());
            r += d.getUnit().getNumericId() + d.getGroupId();
        }
        return r;
    }

    private Float getNeutralizationRatio(HashMap<WeightedSkillGroupUsage, Float> means) {
        Float neutralizationRatio = 0f;
        for (WeightedSkillGroupUsage skillGroup : means.keySet()) {
            boolean neutralize = skillGroup.isTryingToNeutralizeEnemyAlive();
            Float ratio = means.get(skillGroup);
            if (neutralize) {
                neutralizationRatio += ratio;
            }
        }
        return neutralizationRatio;
    }

    /**
	 * EncounterUnitData -> EncounterUnit -> EntityInstance/PMI skill list. Weight is a float between 0-1f, percentage.
	 * @return List of weighted approaches to 'do' the battle by one side (like social skills or magic or weapons). Map: weight -> skill group usage.
	 * 
	 */
    public HashMap<WeightedSkillGroupUsage, Float> getUsedMeans(ArrayList<EncounterUnitData> party, ArrayList<EncounterUnitData> oppositeParty) {
        HashMap<WeightedSkillGroupUsage, Float> map = new HashMap<WeightedSkillGroupUsage, Float>();
        int fullPowerLevel = 0;
        for (EncounterUnitData unit : party) {
            logger.finest("Unit size: " + unit.getSize());
            fullPowerLevel += unit.getUnit().getLevel() * unit.getSize();
        }
        Float sumOfAll = 0f;
        for (EncounterUnitData unit : party) {
            float pLevel = unit.getUnit().getLevel() * unit.getSize();
            logger.finest("Unit powerlevel: " + pLevel);
            float ratio = pLevel / fullPowerLevel;
            ArrayList<WeightedSkillGroupUsage> list = unit.getUsedMeansForBattle(party, oppositeParty);
            for (WeightedSkillGroupUsage wskgu : list) {
                Float value = map.get(wskgu);
                logger.finest("Ratio = " + ratio + "VALUE: " + value + " - " + wskgu.getSKILL_GROUP() + " - " + wskgu.getWeight());
                if (value == null) {
                    value = 0f;
                }
                sumOfAll += (float) (pLevel * ratio * wskgu.getWeight());
                value = value + (float) (pLevel * ratio * wskgu.getWeight());
                map.put(wskgu, value);
            }
        }
        for (WeightedSkillGroupUsage wsgu : map.keySet()) {
            Float value = map.get(wsgu);
            map.put(wsgu, value / sumOfAll);
        }
        return map;
    }

    /**
	 * 
	 * @param selfPower
	 * @param obedientPower
	 * @param self
	 * @param obedient
	 * @return
	 */
    public ArrayList<EncounterUnitData> getMotivatedUnitsForBattle(int selfPower, int obedientPower, ArrayList<EncounterUnitData> self, ArrayList<EncounterUnitData> opposed) {
        Set<EncounterUnitData> oppositUnits = new HashSet<EncounterUnitData>();
        for (EncounterUnitData eud : opposed) oppositUnits.add(eud);
        float powerRatio = selfPower * 1f / obedientPower;
        ArrayList<EncounterUnitData> returnList = new ArrayList<EncounterUnitData>();
        for (EncounterUnitData eud : self) {
            float relation = eud.getUnit().getRelationLevel(oppositUnits) / (EntityScaledRelationType.BEST_PERMANENT * 1f);
            float morale = powerRatio * 1f / (relation + 0.01f);
            logger.info("MORALE " + morale + " REL: " + relation + " / " + powerRatio);
            if (morale >= 1.9f) {
                returnList.add(eud);
            }
        }
        return returnList;
    }

    public EncounterInfo encounter = null;

    public EntityInstance initiatorInstance;

    public EntityFragment initiatorFragment;
}
