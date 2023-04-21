package pcgen.cdom.helper;

import pcgen.core.ArmorProf;
import pcgen.core.Equipment;

/**
 * A SimpleArmorProfProvider is an object that provides proficiency based on a
 * single ArmorProf
 */
public class SimpleArmorProfProvider extends AbstractSimpleProfProvider<ArmorProf> {

    /**
	 * Constructs a new SimpleArmorProfProvider that provides proficiency based
	 * on a the given ArmorProf
	 * 
	 * @param proficiency
	 *            The ArmorProf that this SimpleArmorProfProvider provides
	 */
    public SimpleArmorProfProvider(ArmorProf proficiency) {
        super(proficiency);
    }

    /**
	 * Returns true if this SimpleArmorProfProvider provides proficiency for the
	 * given Equipment; false otherwise.
	 * 
	 * @param eq
	 *            The Equipment to be tested to see if this
	 *            SimpleArmorProfProvider provides proficiency for the Equipment
	 * @return true if this SimpleArmorProfProvider provides proficiency for the
	 *         given Equipment; false otherwise.
	 */
    public boolean providesProficiencyFor(Equipment eq) {
        return providesProficiency(eq.getArmorProf());
    }
}
