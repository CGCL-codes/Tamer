package gurpsbeans;

import java.util.Iterator;

public class ModifierSet {

    public ModifierSet() {
    }

    public double getModifier(String id) {
        try {
            double val = 0;
            Iterator iter = character.getAdvantages().iterator();
            while (iter.hasNext()) {
                val += ((Advantage) iter.next()).getModifier(id);
            }
            iter = character.getDisadvantages().iterator();
            while (iter.hasNext()) {
                val -= ((Disadvantage) iter.next()).getModifier(id);
            }
            return val;
        } catch (Exception ex) {
            return 0;
        }
    }

    GurpsCharacter character;

    /**
		 * @return Returns the character.
		 */
    public GurpsCharacter getCharacter() {
        return character;
    }

    /**
		 * @param character
		 *						The character to set.
		 */
    public void setCharacter(GurpsCharacter character) {
        this.character = character;
    }
}
