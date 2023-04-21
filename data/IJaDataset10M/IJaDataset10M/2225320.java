package pokeglobal.server.battle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import pokeglobal.server.mechanics.PokemonType;
import pokeglobal.server.mechanics.moves.MoveSet;
import pokeglobal.server.mechanics.moves.MoveSetData;

/**
 * This class represents a species of pokemon.
 * @author Colin
 */
public class PokemonSpecies implements Serializable {

    private static final long serialVersionUID = -7424802824344211309L;

    /**
     * Serialised data. Note that both the species ID and the species name
     * are written. Only one of these is actually required in order to
     * load the Pokemon, but both are written in case the server operator
     * would rather load by name (ID is the default) - this allows for
     * removing pokemon from the database without breaking existing teams,
     * although it is somewhat slow.
     */
    @Element
    protected int m_species;

    @Element
    protected String m_name;

    /**
     * Gender constants.
     */
    public static final int GENDER_MALE = 1;

    public static final int GENDER_FEMALE = 2;

    public static final int GENDER_BOTH = GENDER_MALE | GENDER_FEMALE;

    public static final int GENDER_NONE = 0;

    @ElementArray
    protected transient int[] m_base;

    @ElementArray
    protected transient PokemonType[] m_type;

    @Element
    protected transient int m_genders;

    private static PokemonSpeciesData m_default = new PokemonSpeciesData();

    public PokemonSpecies() {
    }

    /**
     * Return the possible genders for this species.
     */
    public int getPossibleGenders() {
        return m_genders;
    }

    /**
     * Set the possible genders for this species.
     */
    public void setPossibleGenders(int genders) {
        m_genders = genders;
    }

    /**
     * Return the default species data.
     */
    public static PokemonSpeciesData getDefaultData() {
        return m_default;
    }

    /**
     * Set the default species data.
     */
    public static void setDefaultData(PokemonSpeciesData data) {
        m_default = data;
    }

    /**
     * Get a "balanced" level for this species using this formula:
     *     level = 113 - 0.074 * [base stat total]
     * This formula places the pokemon's level within the interval [60, 100]
     * based on base stats.
     */
    public int getBalancedLevel() {
        int total = 0;
        for (int i = 0; i < m_base.length; ++i) {
            total += m_base[i];
        }
        int level = (int) Math.round(113.0 - 0.074 * ((double) total));
        if (level < 0) {
            level = 0;
        } else if (level > 100) {
            level = 100;
        }
        return level;
    }

    /**
     * Return whether a pokemon can have a particular ability.
     */
    public boolean canUseAbility(PokemonSpeciesData data, String ability) {
        return data.canUseAbility(m_name, ability);
    }

    /**
     * Return a TreeSet of possible abilities.
     */
    public String[] getPossibleAbilities(PokemonSpeciesData data) {
        return data.getPossibleAbilities(m_name);
    }

    PokemonSpecies(int species, String name, int[] base, PokemonType[] type, int gender) {
        m_species = species;
        m_name = name;
        m_base = base;
        m_type = type;
        m_genders = gender;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * Read a PokemonSpecies from a stream, backed by an arbitrary
     * PokemonSpeciesData object.
     */
    public static synchronized Object readObject(PokemonSpeciesData data, ObjectInputStream in) throws IOException, ClassNotFoundException {
        PokemonSpeciesData old = m_default;
        m_default = data;
        Object o = in.readObject();
        m_default = old;
        return o;
    }

    /**
     * This methods prevents pokemon with arbitrary base stats from being
     * loaded. Pokemon are unserialised only by id and their stats are loaded
     * from that id.
     *
     * This method creatively throws an IOException if the species id does not
     * correspond to a valid pokemon species.
     *
     * This method works from the default species data. To use this with
     * arbitrary species data, use the <code>readFromStream</code> method.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        PokemonSpecies species;
        try {
            species = m_default.getSpecies(m_species);
        } catch (PokemonException e) {
            throw new IOException();
        }
        m_name = species.m_name;
        m_base = species.m_base;
        m_type = species.m_type;
        m_genders = species.m_genders;
    }

    /**
     * Creates a new instance of PokemonSpecies
     */
    public PokemonSpecies(PokemonSpeciesData data, int i) throws PokemonException {
        this(data.getSpecies(i));
    }

    /**
     * Allows for construction from another PokemonSpecies.
     */
    public PokemonSpecies(PokemonSpecies i) {
        m_species = i.m_species;
        m_name = i.m_name;
        m_base = i.m_base;
        m_type = i.m_type;
        m_genders = i.m_genders;
    }

    /**
     * Construct by name.
     */
    public PokemonSpecies(PokemonSpeciesData data, String str) throws PokemonException {
        this(data, data.getPokemonByName(str));
    }

    /**
     * Get this pokemon's base stats.
     */
    public int[] getBaseStats() {
        return m_base;
    }

    public PokemonType[] getTypes() {
        return m_type;
    }

    /**
     * Set the type of this pokemon.
     */
    public void setType(PokemonType[] type) {
        m_type = type;
    }

    public int getBase(int i) throws StatException {
        if ((i < 0) || (i > 5)) throw new StatException();
        return m_base[i];
    }

    /**
     * Get the name of this species.
     */
    public String getName() {
        return m_name;
    }

    /**
     * Set the name of this species.
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * Get the MoveSet associated with this species.
     */
    public MoveSet getMoveSet(MoveSetData data) {
        return data.getMoveSet(m_species);
    }

    /**
     * Return a TreeSet of moves that the pokemon can learn.
     */
    public String[] getLearnableMoves(PokemonSpeciesData data) {
        return data.getLearnableMoves(m_species);
    }

    /**
     * Return whether this species can learn a particular move.
     */
    public boolean canLearn(PokemonSpeciesData data, String move) {
        return data.canLearn(m_species, move);
    }

    public PokemonSpecies getSpecies() {
        return this;
    }
}
