package pl.org.minions.stigma.game.actor;

import java.util.EnumMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * Base for both {@link Actor} and {@link Archetype}.
 * Describes basic static information about actor. Can be
 * "aggregated" to create more complicated archetypes.
 */
public abstract class ArchetypeBase {

    private String name;

    private String shortDescription;

    private String description;

    private Gender gender;

    private byte strength;

    private byte agility;

    private byte willpower;

    private byte finesse;

    private EnumMap<DamageType, Resistance> resistances = new EnumMap<DamageType, Resistance>(DamageType.class);

    {
        for (DamageType type : DamageType.getValuesArray()) resistances.put(type, new Resistance());
    }

    private Set<Short> proficiencies = new TreeSet<Short>();

    /**
     * Full constructor.
     * @param name
     *            name of archetype
     */
    protected ArchetypeBase(String name) {
        this.name = name;
    }

    /**
     * Gets agility of archetype.
     * @return the agility
     */
    public final byte getAgility() {
        return agility;
    }

    /**
     * Gets finesse of archetype.
     * @return the finesse
     */
    public final byte getFinesse() {
        return finesse;
    }

    /**
     * Gets name of archetype.
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns resistance per specified damage type.
     * @param type
     *            damage type for which resistance will be
     *            returned
     * @return resistance value
     */
    public final Resistance getResistance(DamageType type) {
        return resistances.get(type);
    }

    /**
     * Gets map of resistances. Used while marshaling and
     * unmarshaling archetype.
     * @return map (resistance per damage type) of
     *         resistances
     */
    public final EnumMap<DamageType, Resistance> getResistanceMap() {
        return resistances;
    }

    /**
     * Gets strength of archetype.
     * @return the strength
     */
    public final byte getStrength() {
        return strength;
    }

    /**
     * Gets willpower of archetype.
     * @return the willpower
     */
    public final byte getWillpower() {
        return willpower;
    }

    /** {@inheritDoc} */
    @Override
    public abstract int hashCode();

    /**
     * Sets agility of archetype.
     * @param agility
     *            the agility to set
     */
    public final void setAgility(byte agility) {
        this.agility = agility;
    }

    /**
     * Sets finesse of archetype.
     * @param finesse
     *            the finesse to set
     */
    public final void setFinesse(byte finesse) {
        this.finesse = finesse;
    }

    /**
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * Sets map of resistances. Used while marshaling and
     * unmarshaling archetype.
     * @param resistances
     *            map (resistance per damage type) of
     *            resistances
     */
    public final void setResistanceMap(EnumMap<DamageType, Resistance> resistances) {
        this.resistances = resistances;
    }

    /**
     * Sets strength of archetype.
     * @param strength
     *            the strength to set
     */
    public final void setStrength(byte strength) {
        this.strength = strength;
    }

    /**
     * Sets willpower of archetype.
     * @param willpower
     *            the willpower to set
     */
    public final void setWillpower(byte willpower) {
        this.willpower = willpower;
    }

    /**
     * Returns proficiencies.
     * @return proficiencies
     */
    public final Set<Short> getProficiencies() {
        return proficiencies;
    }

    /**
     * Sets new value of gender.
     * @param gender
     *            the gender to set
     */
    public final void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Returns gender.
     * @return gender
     */
    public final Gender getGender() {
        return gender;
    }

    /**
     * Sets new value of shortDescription.
     * @param shortDescription
     *            the shortDescription to set
     */
    public final void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Returns shortDescription.
     * @return shortDescription
     */
    public final String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    public final void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns description.
     * @return description
     */
    public final String getDescription() {
        return description;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Name = " + name + "\n");
        out.append("Strength = " + strength + "\n");
        out.append("Willpower = " + willpower + "\n");
        out.append("Agility = " + agility + "\n");
        out.append("Finesse = " + finesse + "\n");
        for (DamageType type : DamageType.getValuesArray()) {
            if (resistances.containsKey(type)) out.append(type.name() + " - (" + resistances.get(type).getRelative() + "/" + resistances.get(type).getThreshold() + ")\n");
        }
        return out.toString();
    }
}
