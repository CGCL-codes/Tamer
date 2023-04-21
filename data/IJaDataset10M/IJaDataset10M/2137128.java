package pcgen.core.chooser;

import java.util.List;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;

/**
 * A class to handle generating a suitable list of choices, selecting from those
 * choices and potentially applying the choices to a PC
 */
public abstract class AbstractBasicPObjectChoiceManager<T extends PObject> extends AbstractBasicChoiceManager<T> {

    public AbstractBasicPObjectChoiceManager(PObject object, String theChoices, PlayerCharacter apc) {
        super(object, theChoices, apc);
    }

    /**
	 * Add the selected Feat proficiencies
	 * 
	 * @param aPC
	 * @param selected
	 */
    @Override
    public void applyChoices(PlayerCharacter aPC, List<T> selected) {
        pobject.clearAssociated();
        for (T obj : selected) {
            String st = obj.getKeyName();
            if (isMultYes() && !isStackYes()) {
                if (!pobject.containsAssociated(st)) {
                    pobject.addAssociated(st);
                }
            } else {
                pobject.addAssociated(st);
            }
        }
        adjustPool(selected);
    }
}
