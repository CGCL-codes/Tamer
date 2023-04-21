package de.zeroboyz.ggms.model.combatant;

/**
 * This class represents a skill which could be
 * used by a {@link Combatant}.
 * @author jbierwagen
 */
public class Skill {

    /**
     * Name of Skills
     */
    private String name = "";

    /**
     * Some notes about the skill.
     */
    private String notes = "";

    /**
     * Reference to the original rule in one of the GURPS-books.
     */
    private String reference = "";

    /**
     * Some skills have to use a specialisation. E.g. "Piloting (Mecha)" than Mecha is the specialisation.
     */
    private String specialization = "";

    /**
     * The Skill-level.
     */
    private int level = 0;

    /**
     * Sets the field: level.
     *
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Returns the field: level.
     *
     * @return the level
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Sets the field: name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the field: name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the field: notes.
     *
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Returns the field: notes.
     *
     * @return the notes
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Sets the field: reference.
     *
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Returns the field: reference.
     *
     * @return the reference
     */
    public String getReference() {
        return this.reference;
    }

    /**
     * Sets the field: specialization.
     *
     * @param specialization the specialization to set
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * Returns the field: specialization.
     *
     * @return the specialization
     */
    public String getSpecialization() {
        return this.specialization;
    }
}
