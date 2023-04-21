package dpsinterface;

import jroguedps.CGlobal;
import talents.CRogueTalents;

/**
 *
 * @author Mani
 */
public class CDPSTalents {

    private int m_improvedEviscerate;

    private int m_malice;

    private int m_ruthlessness;

    private int m_bloodspatter;

    private int m_puncturingWounds;

    private int m_vigor;

    private int m_lethality;

    private int m_vilePoisons;

    private int m_improvedPoisons;

    private int m_coldBlood;

    private int m_sealFate;

    private int m_murder;

    private int m_overkill;

    private int m_focusedAttacks;

    private int m_findWeakness;

    private int m_masterPoisoner;

    private int m_mutilate;

    private int m_turnTheTables;

    private int m_cutToTheChase;

    private int m_hungerForBlood;

    private int m_improvedSinisterStrike;

    private int m_dualWieldSpecialization;

    private int m_improvedSliceAndDice;

    private int m_precision;

    private int m_closeQuartersCombat;

    private int m_lightningReflexes;

    private int m_aggresion;

    private int m_maceSpecialization;

    private int m_bladeFlurry;

    private int m_swordSpecialization;

    private int m_weaponExpertise;

    private int m_bladeTwisting;

    private int m_vitality;

    private int m_adrenalineRush;

    private int m_combatPotency;

    private int m_surpriseAttacks;

    private int m_savageCombat;

    private int m_preyOnTheWeak;

    private int m_killingSpree;

    private int m_relentlessStrikes;

    private int m_opportunity;

    private int m_elusiveness;

    private int m_ghostlyStrike;

    private int m_serrateBlades;

    private int m_improvedAmbush;

    private int m_preparation;

    private int m_dirtyDeeds;

    private int m_hemorrhage;

    private int m_masterOfSubtlety;

    private int m_deadliness;

    private int m_premeditation;

    private int m_sinisterCalling;

    private int m_honorAmongThieves;

    private int m_shadowStep;

    private int m_slaughterFromTheShadows;

    private double m_improvedEviscerateDouble;

    private double m_maliceDouble;

    private double m_ruthlessnessDouble;

    private double m_bloodspatterDouble;

    private double m_puncturingWoundsDouble;

    private double m_vigorDouble;

    private double m_lethalityDouble;

    private double m_vilePoisonsDouble;

    private double m_improvedPoisonsDouble;

    private double m_coldBloodDouble;

    private double m_sealFateDouble;

    private double m_murderDouble;

    private double m_overkillDouble;

    private double m_focusedAttacksDouble;

    private double m_findWeaknessDouble;

    private double m_masterPoisonerDouble;

    private double m_mutilateDouble;

    private double m_turnTheTablesDouble;

    private double m_cutToTheChaseDouble;

    private double m_hungerForBloodDouble;

    private double m_improvedSinisterStrikeDouble;

    private double m_dualWieldSpecializationDouble;

    private double m_improvedSliceAndDiceDouble;

    private double m_precisionDouble;

    private double m_closeQuartersCombatDouble;

    private double m_lightningReflexesDouble;

    private double m_aggresionDouble;

    private double m_maceSpecializationDouble;

    private double m_bladeFlurryDouble;

    private double m_swordSpecializationDouble;

    private double m_weaponExpertiseDouble;

    private double m_bladeTwistingDouble;

    private double m_vitalityDouble;

    private double m_adrenalineRushDouble;

    private double m_combatPotencyDouble;

    private double m_surpriseAttacksDouble;

    private double m_savageCombatDouble;

    private double m_preyOnTheWeakDouble;

    private double m_killingSpreeDouble;

    private double m_relentlessStrikesDouble;

    private double m_opportunityDouble;

    private double m_elusivenessDouble;

    private double m_ghostlyStrikeDouble;

    private double m_serrateBladesDouble;

    private double m_improvedAmbushDouble;

    private double m_preparationDouble;

    private double m_dirtyDeedsDouble;

    private double m_hemorrhageDouble;

    private double m_masterOfSubtletyDouble;

    private double m_deadlinessDouble;

    private double m_premeditationDouble;

    private double m_sinisterCallingDouble;

    private double m_honorAmongThievesDouble;

    private double m_shadowStepDouble;

    private double m_slaughterFromTheShadowsDouble;

    public CDPSTalents(CRogueTalents p_tal) {
        m_improvedEviscerate = p_tal.getTalent("Improved Eviscerate").getCurrentPoints();
        m_malice = p_tal.getTalent("Malice").getCurrentPoints();
        m_ruthlessness = p_tal.getTalent("Ruthlessness").getCurrentPoints();
        m_bloodspatter = p_tal.getTalent("Blood Spatter").getCurrentPoints();
        m_puncturingWounds = p_tal.getTalent("Puncturing Wounds").getCurrentPoints();
        m_vigor = p_tal.getTalent("Vigor").getCurrentPoints();
        m_lethality = p_tal.getTalent("Lethality").getCurrentPoints();
        m_vilePoisons = p_tal.getTalent("Vile Poisons").getCurrentPoints();
        m_improvedPoisons = p_tal.getTalent("Improved Poisons").getCurrentPoints();
        m_coldBlood = p_tal.getTalent("Cold Blood").getCurrentPoints();
        m_sealFate = p_tal.getTalent("Seal Fate").getCurrentPoints();
        m_murder = p_tal.getTalent("Murder").getCurrentPoints();
        m_overkill = p_tal.getTalent("Overkill").getCurrentPoints();
        m_focusedAttacks = p_tal.getTalent("Focused Attacks").getCurrentPoints();
        m_findWeakness = p_tal.getTalent("Find Weakness").getCurrentPoints();
        m_masterPoisoner = p_tal.getTalent("Master Poisoner").getCurrentPoints();
        m_mutilate = p_tal.getTalent("Mutilate").getCurrentPoints();
        m_turnTheTables = p_tal.getTalent("Turn the Tables").getCurrentPoints();
        m_cutToTheChase = p_tal.getTalent("Cut to the Chase").getCurrentPoints();
        m_hungerForBlood = p_tal.getTalent("Hunger For Blood").getCurrentPoints();
        m_improvedSinisterStrike = p_tal.getTalent("Improved Sinister Strike").getCurrentPoints();
        m_dualWieldSpecialization = p_tal.getTalent("Dual Wield Specialization").getCurrentPoints();
        m_improvedSliceAndDice = p_tal.getTalent("Improved Slice and Dice").getCurrentPoints();
        m_precision = p_tal.getTalent("Precision").getCurrentPoints();
        m_closeQuartersCombat = p_tal.getTalent("Close Quarters Combat").getCurrentPoints();
        m_lightningReflexes = p_tal.getTalent("Lightning Reflexes").getCurrentPoints();
        m_aggresion = p_tal.getTalent("Aggression").getCurrentPoints();
        m_maceSpecialization = p_tal.getTalent("Mace Specialization").getCurrentPoints();
        m_bladeFlurry = p_tal.getTalent("Blade Flurry").getCurrentPoints();
        m_swordSpecialization = p_tal.getTalent("Sword Specialization").getCurrentPoints();
        m_weaponExpertise = p_tal.getTalent("Weapon Expertise").getCurrentPoints();
        m_bladeTwisting = p_tal.getTalent("Blade Twisting").getCurrentPoints();
        m_vitality = p_tal.getTalent("Vitality").getCurrentPoints();
        m_adrenalineRush = p_tal.getTalent("Adrenaline Rush").getCurrentPoints();
        m_combatPotency = p_tal.getTalent("Combat Potency").getCurrentPoints();
        m_surpriseAttacks = p_tal.getTalent("Surprise Attacks").getCurrentPoints();
        m_savageCombat = p_tal.getTalent("Savage Combat").getCurrentPoints();
        m_preyOnTheWeak = p_tal.getTalent("Prey on the Weak").getCurrentPoints();
        m_killingSpree = p_tal.getTalent("Murder Spree").getCurrentPoints();
        m_relentlessStrikes = p_tal.getTalent("Relentless Strikes").getCurrentPoints();
        m_opportunity = p_tal.getTalent("Opportunity").getCurrentPoints();
        m_elusiveness = p_tal.getTalent("Elusiveness").getCurrentPoints();
        m_ghostlyStrike = p_tal.getTalent("Ghostly Strike").getCurrentPoints();
        m_serrateBlades = p_tal.getTalent("Serrated Blades").getCurrentPoints();
        m_improvedAmbush = p_tal.getTalent("Improved Ambush").getCurrentPoints();
        m_preparation = p_tal.getTalent("Preparation").getCurrentPoints();
        m_dirtyDeeds = p_tal.getTalent("Dirty Deeds").getCurrentPoints();
        m_hemorrhage = p_tal.getTalent("Hemorrhage").getCurrentPoints();
        m_masterOfSubtlety = p_tal.getTalent("Master of Subtlety").getCurrentPoints();
        m_deadliness = p_tal.getTalent("Deadliness").getCurrentPoints();
        m_premeditation = p_tal.getTalent("Premeditation").getCurrentPoints();
        m_sinisterCalling = p_tal.getTalent("Sinister Calling").getCurrentPoints();
        m_honorAmongThieves = p_tal.getTalent("Honor Among Thieves").getCurrentPoints();
        m_shadowStep = p_tal.getTalent("Shadow Step").getCurrentPoints();
        m_slaughterFromTheShadows = p_tal.getTalent("Slaughter from the Shadows").getCurrentPoints();
        m_improvedEviscerateDouble = 0.01 * (double) m_improvedEviscerate;
        m_maliceDouble = 0.01 * (double) m_malice;
        m_ruthlessnessDouble = 0.01 * (double) m_ruthlessness;
        m_bloodspatterDouble = 0.01 * (double) m_bloodspatter;
        m_puncturingWoundsDouble = 0.01 * (double) m_puncturingWounds;
        m_vigorDouble = 0.01 * (double) m_vigor;
        m_lethalityDouble = 0.01 * (double) m_lethality;
        m_vilePoisonsDouble = 0.01 * (double) m_vilePoisons;
        m_improvedPoisonsDouble = 0.01 * (double) m_improvedPoisons;
        m_coldBloodDouble = 0.01 * (double) m_coldBlood;
        m_sealFateDouble = 0.01 * (double) m_sealFate;
        m_murderDouble = 0.01 * (double) m_murder;
        m_overkillDouble = 0.01 * (double) m_overkill;
        m_focusedAttacksDouble = 0.01 * (double) m_focusedAttacks;
        m_findWeaknessDouble = 0.01 * (double) m_findWeakness;
        m_masterPoisonerDouble = 0.01 * (double) m_masterPoisoner;
        m_mutilateDouble = 0.01 * (double) m_mutilate;
        m_turnTheTablesDouble = 0.01 * (double) m_turnTheTables;
        m_cutToTheChaseDouble = 0.01 * (double) m_cutToTheChase;
        m_hungerForBloodDouble = 0.01 * (double) m_hungerForBlood;
        m_improvedSinisterStrikeDouble = 0.01 * (double) m_improvedSinisterStrike;
        m_dualWieldSpecializationDouble = 0.01 * (double) m_dualWieldSpecialization;
        m_improvedSliceAndDiceDouble = 0.01 * (double) m_improvedSliceAndDice;
        m_precisionDouble = 0.01 * (double) m_precision;
        m_closeQuartersCombatDouble = 0.01 * (double) m_closeQuartersCombat;
        m_lightningReflexesDouble = 0.01 * (double) m_lightningReflexes;
        m_aggresionDouble = 0.01 * (double) m_aggresion;
        m_maceSpecializationDouble = 0.01 * (double) m_maceSpecialization;
        m_bladeFlurryDouble = 0.01 * (double) m_bladeFlurry;
        m_swordSpecializationDouble = 0.01 * (double) m_swordSpecialization;
        m_weaponExpertiseDouble = 0.01 * (double) m_weaponExpertise;
        m_bladeTwistingDouble = 0.01 * (double) m_bladeTwisting;
        m_vitalityDouble = 0.01 * (double) m_vitality;
        m_adrenalineRushDouble = 0.01 * (double) m_adrenalineRush;
        m_combatPotencyDouble = 0.01 * (double) m_combatPotency;
        m_surpriseAttacksDouble = 0.01 * (double) m_surpriseAttacks;
        m_savageCombatDouble = 0.01 * (double) m_savageCombat;
        m_preyOnTheWeakDouble = 0.01 * (double) m_preyOnTheWeak;
        m_killingSpreeDouble = 0.01 * (double) m_killingSpree;
        m_relentlessStrikesDouble = 0.01 * (double) m_relentlessStrikes;
        m_opportunityDouble = 0.01 * (double) m_opportunity;
        m_elusivenessDouble = 0.01 * (double) m_elusiveness;
        m_ghostlyStrikeDouble = 0.01 * (double) m_ghostlyStrike;
        m_serrateBladesDouble = 0.01 * (double) m_serrateBlades;
        m_improvedAmbushDouble = 0.01 * (double) m_improvedAmbush;
        m_preparationDouble = 0.01 * (double) m_preparation;
        m_dirtyDeedsDouble = 0.01 * (double) m_dirtyDeeds;
        m_hemorrhageDouble = 0.01 * (double) m_hemorrhage;
        m_masterOfSubtletyDouble = 0.01 * (double) m_masterOfSubtlety;
        m_deadlinessDouble = 0.01 * (double) m_deadliness;
        m_premeditationDouble = 0.01 * (double) m_premeditation;
        m_sinisterCallingDouble = 0.01 * (double) m_sinisterCalling;
        m_honorAmongThievesDouble = 0.01 * (double) m_honorAmongThieves;
        m_shadowStepDouble = 0.01 * (double) m_shadowStep;
        m_slaughterFromTheShadowsDouble = 0.01 * (double) m_slaughterFromTheShadows;
    }

    /**
     * @return the m_improvedEviscerate
     */
    public int getImprovedEviscerate() {
        return m_improvedEviscerate;
    }

    /**
     * @return the m_malice
     */
    public int getMalice() {
        return m_malice;
    }

    /**
     * @return the m_ruthlessness
     */
    public int getRuthlessness() {
        return m_ruthlessness;
    }

    /**
     * @return the m_bloodspatter
     */
    public int getBloodspatter() {
        return m_bloodspatter;
    }

    /**
     * @return the m_puncturingWounds
     */
    public int getPuncturingWounds() {
        return m_puncturingWounds;
    }

    /**
     * @return the m_vigor
     */
    public int getVigor() {
        return m_vigor;
    }

    /**
     * @return the m_lethality
     */
    public int getLethality() {
        return m_lethality;
    }

    /**
     * @return the m_vilePoisons
     */
    public int getVilePoisons() {
        return m_vilePoisons;
    }

    /**
     * @return the m_improvedPoisons
     */
    public int getImprovedPoisons() {
        return m_improvedPoisons;
    }

    /**
     * @return the m_coldBlood
     */
    public int getColdBlood() {
        return m_coldBlood;
    }

    /**
     * @return the m_sealFate
     */
    public int getSealFate() {
        return m_sealFate;
    }

    /**
     * @return the m_murder
     */
    public int getMurder() {
        return m_murder;
    }

    /**
     * @return the m_overkill
     */
    public int getOverkill() {
        return m_overkill;
    }

    /**
     * @return the m_focusedAttacks
     */
    public int getFocusedAttacks() {
        return m_focusedAttacks;
    }

    /**
     * @return the m_findWeakness
     */
    public int getFindWeakness() {
        return m_findWeakness;
    }

    /**
     * @return the m_masterPoisoner
     */
    public int getMasterPoisoner() {
        return m_masterPoisoner;
    }

    /**
     * @return the m_mutilate
     */
    public int getMutilate() {
        return m_mutilate;
    }

    /**
     * @return the m_turnTheTables
     */
    public int getTurnTheTables() {
        return m_turnTheTables;
    }

    /**
     * @return the m_cutToTheChase
     */
    public int getCutToTheChase() {
        return m_cutToTheChase;
    }

    /**
     * @return the m_hungerForBlood
     */
    public int getHungerForBlood() {
        return m_hungerForBlood;
    }

    /**
     * @return the m_improvedSinisterStrike
     */
    public int getImprovedSinisterStrike() {
        return m_improvedSinisterStrike;
    }

    /**
     * @return the m_dualWieldSpecialization
     */
    public int getDualWieldSpecialization() {
        return m_dualWieldSpecialization;
    }

    /**
     * @return the m_improvedSliceAndDice
     */
    public int getImprovedSliceAndDice() {
        return m_improvedSliceAndDice;
    }

    /**
     * @return the m_precision
     */
    public int getPrecision() {
        return m_precision;
    }

    /**
     * @return the m_closeQuartersCombat
     */
    public int getCloseQuartersCombat() {
        return m_closeQuartersCombat;
    }

    /**
     * @return the m_lightningReflexes
     */
    public int getLightningReflexes() {
        return m_lightningReflexes;
    }

    /**
     * @return the m_aggresion
     */
    public int getAggresion() {
        return m_aggresion;
    }

    /**
     * @return the m_maceSpecialization
     */
    public int getMaceSpecialization() {
        return m_maceSpecialization;
    }

    /**
     * @return the m_bladeFlurry
     */
    public int getBladeFlurry() {
        return m_bladeFlurry;
    }

    /**
     * @return the m_swordSpecialization
     */
    public int getSwordSpecialization() {
        return m_swordSpecialization;
    }

    /**
     * @return the m_weaponExpertise
     */
    public int getWeaponExpertise() {
        return m_weaponExpertise;
    }

    /**
     * @return the m_bladeTwisting
     */
    public int getBladeTwisting() {
        return m_bladeTwisting;
    }

    /**
     * @return the m_vitality
     */
    public int getVitality() {
        return m_vitality;
    }

    /**
     * @return the m_adrenalineRush
     */
    public int getAdrenalineRush() {
        return m_adrenalineRush;
    }

    /**
     * @return the m_combatPotency
     */
    public int getCombatPotency() {
        return m_combatPotency;
    }

    /**
     * @return the m_surpriseAttacks
     */
    public int getSurpriseAttacks() {
        return m_surpriseAttacks;
    }

    /**
     * @return the m_savageCombat
     */
    public int getSavageCombat() {
        return m_savageCombat;
    }

    /**
     * @return the m_preyOnTheWeak
     */
    public int getPreyOnTheWeak() {
        return m_preyOnTheWeak;
    }

    /**
     * @return the m_killingSpree
     */
    public int getKillingSpree() {
        return m_killingSpree;
    }

    /**
     * @return the m_relentlessStrikes
     */
    public int getRelentlessStrikes() {
        return m_relentlessStrikes;
    }

    /**
     * @return the m_opportunity
     */
    public int getOpportunity() {
        return m_opportunity;
    }

    /**
     * @return the m_elusiveness
     */
    public int getElusiveness() {
        return m_elusiveness;
    }

    /**
     * @return the m_ghostlyStrike
     */
    public int getGhostlyStrike() {
        return m_ghostlyStrike;
    }

    /**
     * @return the m_serrateBlades
     */
    public int getSerrateBlades() {
        return m_serrateBlades;
    }

    /**
     * @return the m_improvedAmbush
     */
    public int getImprovedAmbush() {
        return m_improvedAmbush;
    }

    /**
     * @return the m_preparation
     */
    public int getPreparation() {
        return m_preparation;
    }

    /**
     * @return the m_dirtyDeeds
     */
    public int getDirtyDeeds() {
        return m_dirtyDeeds;
    }

    /**
     * @return the m_hemorrhage
     */
    public int getHemorrhage() {
        return m_hemorrhage;
    }

    /**
     * @return the m_masterOfSubtlety
     */
    public int getMasterOfSubtlety() {
        return m_masterOfSubtlety;
    }

    /**
     * @return the m_deadliness
     */
    public int getDeadliness() {
        return m_deadliness;
    }

    /**
     * @return the m_premeditation
     */
    public int getPremeditation() {
        return m_premeditation;
    }

    /**
     * @return the m_sinisterCalling
     */
    public int getSinisterCalling() {
        return m_sinisterCalling;
    }

    /**
     * @return the m_honorAmongThieves
     */
    public int getHonorAmongThieves() {
        return m_honorAmongThieves;
    }

    /**
     * @return the m_shadowStep
     */
    public int getShadowStep() {
        return m_shadowStep;
    }

    /**
     * @return the m_slaughterFromTheShadows
     */
    public int getSlaughterFromTheShadows() {
        return m_slaughterFromTheShadows;
    }

    /**
     * @return the m_improvedEviscerateDouble
     */
    public double getImprovedEviscerateDouble() {
        return m_improvedEviscerateDouble;
    }

    /**
     * @return the m_maliceDouble
     */
    public double getMaliceDouble() {
        return m_maliceDouble;
    }

    /**
     * @return the m_ruthlessnessDouble
     */
    public double getRuthlessnessDouble() {
        return m_ruthlessnessDouble;
    }

    /**
     * @return the m_bloodspatterDouble
     */
    public double getBloodspatterDouble() {
        return m_bloodspatterDouble;
    }

    /**
     * @return the m_puncturingWoundsDouble
     */
    public double getPuncturingWoundsDouble() {
        return m_puncturingWoundsDouble;
    }

    /**
     * @return the m_vigorDouble
     */
    public double getVigorDouble() {
        return m_vigorDouble;
    }

    /**
     * @return the m_lethalityDouble
     */
    public double getLethalityDouble() {
        return m_lethalityDouble;
    }

    /**
     * @return the m_vilePoisonsDouble
     */
    public double getVilePoisonsDouble() {
        return m_vilePoisonsDouble;
    }

    /**
     * @return the m_improvedPoisonsDouble
     */
    public double getImprovedPoisonsDouble() {
        return m_improvedPoisonsDouble;
    }

    /**
     * @return the m_coldBloodDouble
     */
    public double getColdBloodDouble() {
        return m_coldBloodDouble;
    }

    /**
     * @return the m_sealFateDouble
     */
    public double getSealFateDouble() {
        return m_sealFateDouble;
    }

    /**
     * @return the m_murderDouble
     */
    public double getMurderDouble() {
        return m_murderDouble;
    }

    /**
     * @return the m_overkillDouble
     */
    public double getOverkillDouble() {
        return m_overkillDouble;
    }

    /**
     * @return the m_focusedAttacksDouble
     */
    public double getFocusedAttacksDouble() {
        return m_focusedAttacksDouble;
    }

    /**
     * @return the m_findWeaknessDouble
     */
    public double getFindWeaknessDouble() {
        return m_findWeaknessDouble;
    }

    /**
     * @return the m_masterPoisonerDouble
     */
    public double getMasterPoisonerDouble() {
        return m_masterPoisonerDouble;
    }

    /**
     * @return the m_mutilateDouble
     */
    public double getMutilateDouble() {
        return m_mutilateDouble;
    }

    /**
     * @return the m_turnTheTablesDouble
     */
    public double getTurnTheTablesDouble() {
        return m_turnTheTablesDouble;
    }

    /**
     * @return the m_cutToTheChaseDouble
     */
    public double getCutToTheChaseDouble() {
        return m_cutToTheChaseDouble;
    }

    /**
     * @return the m_hungerForBloodDouble
     */
    public double getHungerForBloodDouble() {
        return m_hungerForBloodDouble;
    }

    /**
     * @return the m_improvedSinisterStrikeDouble
     */
    public double getImprovedSinisterStrikeDouble() {
        return m_improvedSinisterStrikeDouble;
    }

    /**
     * @return the m_dualWieldSpecializationDouble
     */
    public double getDualWieldSpecializationDouble() {
        return m_dualWieldSpecializationDouble;
    }

    /**
     * @return the m_improvedSliceAndDiceDouble
     */
    public double getImprovedSliceAndDiceDouble() {
        return m_improvedSliceAndDiceDouble;
    }

    /**
     * @return the m_precisionDouble
     */
    public double getPrecisionDouble() {
        return m_precisionDouble;
    }

    /**
     * @return the m_closeQuartersCombatDouble
     */
    public double getCloseQuartersCombatDouble() {
        return m_closeQuartersCombatDouble;
    }

    /**
     * @return the m_lightningReflexesDouble
     */
    public double getLightningReflexesDouble() {
        return m_lightningReflexesDouble;
    }

    /**
     * @return the m_aggresionDouble
     */
    public double getAggresionDouble() {
        return m_aggresionDouble;
    }

    /**
     * @return the m_maceSpecializationDouble
     */
    public double getMaceSpecializationDouble() {
        return m_maceSpecializationDouble;
    }

    /**
     * @return the m_bladeFlurryDouble
     */
    public double getBladeFlurryDouble() {
        return m_bladeFlurryDouble;
    }

    /**
     * @return the m_swordSpecializationDouble
     */
    public double getSwordSpecializationDouble() {
        return m_swordSpecializationDouble;
    }

    /**
     * @return the m_weaponExpertiseDouble
     */
    public double getWeaponExpertiseDouble() {
        return m_weaponExpertiseDouble;
    }

    /**
     * @return the m_bladeTwistingDouble
     */
    public double getBladeTwistingDouble() {
        return m_bladeTwistingDouble;
    }

    /**
     * @return the m_vitalityDouble
     */
    public double getVitalityDouble() {
        return m_vitalityDouble;
    }

    /**
     * @return the m_adrenalineRushDouble
     */
    public double getAdrenalineRushDouble() {
        return m_adrenalineRushDouble;
    }

    /**
     * @return the m_combatPotencyDouble
     */
    public double getCombatPotencyDouble() {
        return m_combatPotencyDouble;
    }

    /**
     * @return the m_surpriseAttacksDouble
     */
    public double getSurpriseAttacksDouble() {
        return m_surpriseAttacksDouble;
    }

    /**
     * @return the m_savageCombatDouble
     */
    public double getSavageCombatDouble() {
        return m_savageCombatDouble;
    }

    /**
     * @return the m_preyOnTheWeakDouble
     */
    public double getPreyOnTheWeakDouble() {
        return m_preyOnTheWeakDouble;
    }

    /**
     * @return the m_killingSpreeDouble
     */
    public double getKillingSpreeDouble() {
        return m_killingSpreeDouble;
    }

    /**
     * @return the m_relentlessStrikesDouble
     */
    public double getRelentlessStrikesDouble() {
        return m_relentlessStrikesDouble;
    }

    /**
     * @return the m_opportunityDouble
     */
    public double getOpportunityDouble() {
        return m_opportunityDouble;
    }

    /**
     * @return the m_elusivenessDouble
     */
    public double getElusivenessDouble() {
        return m_elusivenessDouble;
    }

    /**
     * @return the m_ghostlyStrikeDouble
     */
    public double getGhostlyStrikeDouble() {
        return m_ghostlyStrikeDouble;
    }

    /**
     * @return the m_serrateBladesDouble
     */
    public double getSerrateBladesDouble() {
        return m_serrateBladesDouble;
    }

    /**
     * @return the m_improvedAmbushDouble
     */
    public double getImprovedAmbushDouble() {
        return m_improvedAmbushDouble;
    }

    /**
     * @return the m_preparationDouble
     */
    public double getPreparationDouble() {
        return m_preparationDouble;
    }

    /**
     * @return the m_dirtyDeedsDouble
     */
    public double getDirtyDeedsDouble() {
        return m_dirtyDeedsDouble;
    }

    /**
     * @return the m_hemorrhageDouble
     */
    public double getHemorrhageDouble() {
        return m_hemorrhageDouble;
    }

    /**
     * @return the m_masterOfSubtletyDouble
     */
    public double getMasterOfSubtletyDouble() {
        return m_masterOfSubtletyDouble;
    }

    /**
     * @return the m_deadlinessDouble
     */
    public double getDeadlinessDouble() {
        return m_deadlinessDouble;
    }

    /**
     * @return the m_premeditationDouble
     */
    public double getPremeditationDouble() {
        return m_premeditationDouble;
    }

    /**
     * @return the m_sinisterCallingDouble
     */
    public double getSinisterCallingDouble() {
        return m_sinisterCallingDouble;
    }

    /**
     * @return the m_honorAmongThievesDouble
     */
    public double getHonorAmongThievesDouble() {
        return m_honorAmongThievesDouble;
    }

    /**
     * @return the m_shadowStepDouble
     */
    public double getShadowStepDouble() {
        return m_shadowStepDouble;
    }

    /**
     * @return the m_slaughterFromTheShadowsDouble
     */
    public double getSlaughterFromTheShadowsDouble() {
        return m_slaughterFromTheShadowsDouble;
    }
}
