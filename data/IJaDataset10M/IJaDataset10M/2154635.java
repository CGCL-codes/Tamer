package pl.org.minions.stigma.game.item.type.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.game.actor.DamageType;
import pl.org.minions.stigma.game.item.type.WeaponAttack;

/**
 * Class representing weapon damage XML data.
 */
@XmlRootElement(name = "weaponAttack")
@XmlType(propOrder = {  })
public class WeaponAttackData {

    /**
     * Data converter between WeaponDamageStatistic and
     * WeaponDamageStatisticsData.
     */
    public static class DataConverter implements Converter<WeaponAttack, WeaponAttackData> {

        /** {@inheritDoc} */
        @Override
        public WeaponAttackData buildData(WeaponAttack object) {
            return new WeaponAttackData(object.getDamageType(), object.getRange(), object.getCooldown(), object.getBaseDamage(), object.getAttack(), object.getCriticalChance(), object.getStrenghtBonusStep(), object.getAgilityBonusStep(), object.getWillpowerBonusStep(), object.getFinesseBonusStep(), object.getStrenghtBonusMin(), object.getAgilityBonusMin(), object.getWillpowerBonusMin(), object.getFinesseBonusMin(), object.getStrenghtBonusMax(), object.getAgilityBonusMax(), object.getWillpowerBonusMax(), object.getFinesseBonusMax());
        }

        /** {@inheritDoc} */
        @Override
        public WeaponAttack buildObject(WeaponAttackData data) {
            return new WeaponAttack(data.getDamageType(), data.getRange() == null ? 0 : data.getRange(), data.getCooldown() == null ? 0 : data.getCooldown(), data.getBaseDamage() == null ? 0 : data.getBaseDamage(), data.getAttack() == null ? 0 : data.getAttack(), data.getCriticalChance() == null ? 0 : data.getCriticalChance(), data.getStrenghtBonusStep() == null ? 0 : data.getStrenghtBonusStep(), data.getAgilityBonusStep() == null ? 0 : data.getAgilityBonusStep(), data.getWillpowerBonusStep() == null ? 0 : data.getWillpowerBonusStep(), data.getFinesseBonusStep() == null ? 0 : data.getFinesseBonusStep(), data.getStrenghtBonusMin() == null ? 0 : data.getStrenghtBonusMin(), data.getAgilityBonusMin() == null ? 0 : data.getAgilityBonusMin(), data.getWillpowerBonusMin() == null ? 0 : data.getWillpowerBonusMin(), data.getFinesseBonusMin() == null ? 0 : data.getFinesseBonusMin(), data.getStrenghtBonusMax() == null ? 0 : data.getStrenghtBonusMax(), data.getAgilityBonusMax() == null ? 0 : data.getAgilityBonusMax(), data.getWillpowerBonusMax() == null ? 0 : data.getWillpowerBonusMax(), data.getFinesseBonusMax() == null ? 0 : data.getFinesseBonusMax());
        }
    }

    private DamageType damageType;

    private Short range;

    private Short cooldown;

    private Short baseDamage;

    private Short attack;

    private Short criticalChance;

    private Short strenghtBonusStep;

    private Short agilityBonusStep;

    private Short willpowerBonusStep;

    private Short finesseBonusStep;

    private Short strenghtBonusMin;

    private Short agilityBonusMin;

    private Short willpowerBonusMin;

    private Short finesseBonusMin;

    private Short strenghtBonusMax;

    private Short agilityBonusMax;

    private Short willpowerBonusMax;

    private Short finesseBonusMax;

    /**
     * Constructor used by JAXB.
     */
    public WeaponAttackData() {
        this.damageType = null;
        this.range = null;
        this.baseDamage = null;
        this.cooldown = null;
        this.attack = null;
        this.criticalChance = null;
        this.strenghtBonusStep = null;
        this.agilityBonusStep = null;
        this.willpowerBonusStep = null;
        this.finesseBonusStep = null;
        this.strenghtBonusMin = null;
        this.agilityBonusMin = null;
        this.willpowerBonusMin = null;
        this.finesseBonusMin = null;
        this.strenghtBonusMax = null;
        this.agilityBonusMax = null;
        this.willpowerBonusMax = null;
        this.finesseBonusMax = null;
    }

    /**
     * Default constructor.
     * @param damageType
     *            type of damage done by weapon
     * @param range
     *            range of weapon
     * @param cooldown
     *            cooldown time for weapon (indicates how
     *            often actor can attack with this weapon)
     * @param baseDamage
     *            base damage of weapon
     * @param attack
     *            weapon attack
     * @param critical
     *            chance to score critical hit
     * @param strenghtBonusStep
     *            how fast weapon damage increases with
     *            increase of strength, calculated from
     *            equation: damage +
     *            floor(actor_strength/strenghtBonusStep)
     * @param agilityBonusStep
     *            how fast weapon damage increases with
     *            increase of agility, calculated from
     *            equation: damage +
     *            floor(actor_agility/agilityBonusStep)
     * @param willpowerBonusStep
     *            how fast weapon damage increases with
     *            increase of willpower, calculated from
     *            equation: damage +
     *            floor(actor_willpower/willpowerBonusStep)
     * @param finesseBonusStep
     *            how fast weapon damage increases with
     *            increase of finesse, calculated from
     *            equation: damage +
     *            floor(actor_finesse/finesseBonusStep)
     * @param strenghtBonusMin
     *            minimal strength needed to receive bonus
     * @param agilityBonusMin
     *            minimal agility needed to receive bonus
     * @param willpowerBonusMin
     *            minimal willpower needed to receive bonus
     * @param finesseBonusMin
     *            minimal finesse needed to receive bonus
     * @param strenghtBonusMax
     *            maximal strength value from which bonus
     *            will be calculated
     * @param agilityBonusMax
     *            maximal agility value from which bonus
     *            will be calculated
     * @param willpowerBonusMax
     *            maximal willpower value from which bonus
     *            will be calculated
     * @param finesseBonusMax
     *            maximal finesse value from which bonus
     *            will be calculated
     */
    public WeaponAttackData(DamageType damageType, Short range, Short cooldown, Short baseDamage, Short attack, Short critical, Short strenghtBonusStep, Short agilityBonusStep, Short willpowerBonusStep, Short finesseBonusStep, Short strenghtBonusMin, Short agilityBonusMin, Short willpowerBonusMin, Short finesseBonusMin, Short strenghtBonusMax, Short agilityBonusMax, Short willpowerBonusMax, Short finesseBonusMax) {
        this.damageType = damageType;
        this.range = range;
        this.cooldown = cooldown;
        this.baseDamage = baseDamage;
        this.attack = attack;
        this.criticalChance = critical;
        this.strenghtBonusStep = strenghtBonusStep;
        this.agilityBonusStep = agilityBonusStep;
        this.willpowerBonusStep = willpowerBonusStep;
        this.finesseBonusStep = finesseBonusStep;
        this.strenghtBonusMin = strenghtBonusMin;
        this.agilityBonusMin = agilityBonusMin;
        this.willpowerBonusMin = willpowerBonusMin;
        this.finesseBonusMin = finesseBonusMin;
        this.strenghtBonusMax = strenghtBonusMax;
        this.agilityBonusMax = agilityBonusMax;
        this.willpowerBonusMax = willpowerBonusMax;
        this.finesseBonusMax = finesseBonusMax;
    }

    /**
     * Returns damageType.
     * @return damageType
     */
    public DamageType getDamageType() {
        return damageType;
    }

    /**
     * Returns range.
     * @return range
     */
    public Short getRange() {
        return range;
    }

    /**
     * Returns cooldown.
     * @return cooldown
     */
    public Short getCooldown() {
        return cooldown;
    }

    /**
     * Returns baseDamage.
     * @return baseDamage
     */
    public Short getBaseDamage() {
        return baseDamage;
    }

    /**
     * Returns attack.
     * @return attack
     */
    public Short getAttack() {
        return attack;
    }

    /**
     * Returns strenghtBonusStep.
     * @return strenghtBonusStep
     */
    public Short getStrenghtBonusStep() {
        return strenghtBonusStep;
    }

    /**
     * Returns agilityBonusStep.
     * @return agilityBonusStep
     */
    public Short getAgilityBonusStep() {
        return agilityBonusStep;
    }

    /**
     * Returns willpowerBonusStep.
     * @return willpowerBonusStep
     */
    public Short getWillpowerBonusStep() {
        return willpowerBonusStep;
    }

    /**
     * Returns finesseBonusStep.
     * @return finesseBonusStep
     */
    public Short getFinesseBonusStep() {
        return finesseBonusStep;
    }

    /**
     * Returns strenghtBonusMin.
     * @return strenghtBonusMin
     */
    public Short getStrenghtBonusMin() {
        return strenghtBonusMin;
    }

    /**
     * Returns agilityBonusMin.
     * @return agilityBonusMin
     */
    public Short getAgilityBonusMin() {
        return agilityBonusMin;
    }

    /**
     * Returns willpowerBonusMin.
     * @return willpowerBonusMin
     */
    public Short getWillpowerBonusMin() {
        return willpowerBonusMin;
    }

    /**
     * Returns finesseBonusMin.
     * @return finesseBonusMin
     */
    public Short getFinesseBonusMin() {
        return finesseBonusMin;
    }

    /**
     * Returns strenghtBonusMax.
     * @return strenghtBonusMax
     */
    public Short getStrenghtBonusMax() {
        return strenghtBonusMax;
    }

    /**
     * Returns agilityBonusMax.
     * @return agilityBonusMax
     */
    public Short getAgilityBonusMax() {
        return agilityBonusMax;
    }

    /**
     * Returns willpowerBonusMax.
     * @return willpowerBonusMax
     */
    public Short getWillpowerBonusMax() {
        return willpowerBonusMax;
    }

    /**
     * Returns finesseBonusMax.
     * @return finesseBonusMax
     */
    public Short getFinesseBonusMax() {
        return finesseBonusMax;
    }

    /**
     * Sets new value of damageType.
     * @param damageType
     *            the damageType to set
     */
    @XmlAttribute(name = "type", required = true)
    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }

    /**
     * Sets new value of range.
     * @param range
     *            the range to set
     */
    @XmlElement(required = true)
    public void setRange(Short range) {
        this.range = range;
    }

    /**
     * Sets new value of cooldown.
     * @param cooldown
     *            the cooldown to set
     */
    @XmlElement(required = true)
    public void setCooldown(Short cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Sets new value of baseDamage.
     * @param baseDamage
     *            the baseDamage to set
     */
    @XmlElement(name = "damage", required = true)
    public void setBaseDamage(Short baseDamage) {
        this.baseDamage = baseDamage;
    }

    /**
     * Sets new value of attack.
     * @param attack
     *            the attack to set
     */
    @XmlElement(required = true)
    public void setAttack(Short attack) {
        this.attack = attack;
    }

    /**
     * Sets new value of strenghtBonusStep.
     * @param strenghtBonusStep
     *            the strenghtBonusStep to set
     */
    @XmlElement(name = "strbonusstep", required = false)
    public void setStrenghtBonusStep(Short strenghtBonusStep) {
        this.strenghtBonusStep = strenghtBonusStep;
    }

    /**
     * Sets new value of agilityBonusStep.
     * @param agilityBonusStep
     *            the agilityBonusStep to set
     */
    @XmlElement(name = "agibonusstep", required = false)
    public void setAgilityBonusStep(Short agilityBonusStep) {
        this.agilityBonusStep = agilityBonusStep;
    }

    /**
     * Sets new value of willpowerBonusStep.
     * @param willpowerBonusStep
     *            the willpowerBonusStep to set
     */
    @XmlElement(name = "wpwbonusstep", required = false)
    public void setWillpowerBonusStep(Short willpowerBonusStep) {
        this.willpowerBonusStep = willpowerBonusStep;
    }

    /**
     * Sets new value of finesseBonusStep.
     * @param finesseBonusStep
     *            the finesseBonusStep to set
     */
    @XmlElement(name = "finbonusstep", required = false)
    public void setFinesseBonusStep(Short finesseBonusStep) {
        this.finesseBonusStep = finesseBonusStep;
    }

    /**
     * Sets new value of strenghtBonusMin.
     * @param strenghtBonusMin
     *            the strenghtBonusMin to set
     */
    @XmlElement(name = "strbonusmin", required = false)
    public void setStrenghtBonusMin(Short strenghtBonusMin) {
        this.strenghtBonusMin = strenghtBonusMin;
    }

    /**
     * Sets new value of agilityBonusMin.
     * @param agilityBonusMin
     *            the agilityBonusMin to set
     */
    @XmlElement(name = "agibonusmin", required = false)
    public void setAgilityBonusMin(Short agilityBonusMin) {
        this.agilityBonusMin = agilityBonusMin;
    }

    /**
     * Sets new value of willpowerBonusMin.
     * @param willpowerBonusMin
     *            the willpowerBonusMin to set
     */
    @XmlElement(name = "wpwbonusmin", required = false)
    public void setWillpowerBonusMin(Short willpowerBonusMin) {
        this.willpowerBonusMin = willpowerBonusMin;
    }

    /**
     * Sets new value of finesseBonusMin.
     * @param finesseBonusMin
     *            the finesseBonusMin to set
     */
    @XmlElement(name = "finbonusmin", required = false)
    public void setFinesseBonusMin(Short finesseBonusMin) {
        this.finesseBonusMin = finesseBonusMin;
    }

    /**
     * Sets new value of strenghtBonusMax.
     * @param strenghtBonusMax
     *            the strenghtBonusMax to set
     */
    @XmlElement(name = "strbonusmax", required = false)
    public void setStrenghtBonusMax(Short strenghtBonusMax) {
        this.strenghtBonusMax = strenghtBonusMax;
    }

    /**
     * Sets new value of agilityBonusMax.
     * @param agilityBonusMax
     *            the agilityBonusMax to set
     */
    @XmlElement(name = "agibonusmax", required = false)
    public void setAgilityBonusMax(Short agilityBonusMax) {
        this.agilityBonusMax = agilityBonusMax;
    }

    /**
     * Sets new value of willpowerBonusMax.
     * @param willpowerBonusMax
     *            the willpowerBonusMax to set
     */
    @XmlElement(name = "wpwbonusmax", required = false)
    public void setWillpowerBonusMax(Short willpowerBonusMax) {
        this.willpowerBonusMax = willpowerBonusMax;
    }

    /**
     * Sets new value of finesseBonusMax.
     * @param finesseBonusMax
     *            the finesseBonusMax to set
     */
    @XmlElement(name = "finbonusmax", required = false)
    public void setFinesseBonusMax(Short finesseBonusMax) {
        this.finesseBonusMax = finesseBonusMax;
    }

    /**
     * Returns criticalChance.
     * @return criticalChance
     */
    public Short getCriticalChance() {
        return criticalChance;
    }

    /**
     * Sets new value of criticalChance.
     * @param criticalChance
     *            the criticalChance to set
     */
    @XmlElement(name = "critical", required = false)
    public void setCriticalChance(Short criticalChance) {
        this.criticalChance = criticalChance;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        String newline = System.getProperty("line.separator");
        out.append("damage type: ").append(damageType).append(newline);
        out.append("base damage: ").append(baseDamage).append(newline);
        out.append("attack: ").append(attack).append(newline);
        out.append("critical: ").append(criticalChance).append(newline);
        if (agilityBonusStep != 0) {
            out.append("agilityBonusStep: ").append(agilityBonusStep).append(newline);
        }
        if (strenghtBonusStep != 0) {
            out.append("strenghtBonusStep: ").append(strenghtBonusStep).append(newline);
        }
        if (finesseBonusStep != 0) {
            out.append("finesseBonusStep: ").append(finesseBonusStep).append(newline);
        }
        if (willpowerBonusStep != 0) {
            out.append("willpowerBonusStep: ").append(willpowerBonusStep).append(newline);
        }
        if (agilityBonusMin != 0) {
            out.append("agilityBonusMin: ").append(agilityBonusMin).append(newline);
        }
        if (strenghtBonusMin != 0) {
            out.append("strenghtBonusMin: ").append(strenghtBonusMin).append(newline);
        }
        if (finesseBonusMin != 0) {
            out.append("finesseBonusMin: ").append(finesseBonusMin).append(newline);
        }
        if (willpowerBonusMin != 0) {
            out.append("willpowerBonusMin: ").append(willpowerBonusMin).append(newline);
        }
        if (agilityBonusMax != 0) {
            out.append("agilityBonusMax: ").append(agilityBonusMax).append(newline);
        }
        if (strenghtBonusMax != 0) {
            out.append("strenghtBonusMax: ").append(strenghtBonusMax).append(newline);
        }
        if (finesseBonusMax != 0) {
            out.append("finesseBonusMax: ").append(finesseBonusMax).append(newline);
        }
        if (willpowerBonusMax != 0) {
            out.append("willpowerBonusMax: ").append(willpowerBonusMax).append(newline);
        }
        return out.toString();
    }
}
