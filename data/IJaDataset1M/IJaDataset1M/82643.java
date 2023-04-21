package pcgen.core.chooser;

import pcgen.core.*;
import pcgen.core.prereq.PrereqHandler;
import java.util.Iterator;
import java.util.List;

/**
 * Handle the logic necessary to choose a Feat.
 *
 * @author   Andrew Wilson <nuance@sourceforge.net>
 * @version  $Revision: 466 $
 */
public class SimpleFeatChoiceManager extends AbstractSimpleChoiceManager {

    /**
	 * Creates a new SimpleFeatChoiceManager object.
	 *
	 * @param  aPObject
	 * @param  theChoices
	 * @param  aPC
	 */
    public SimpleFeatChoiceManager(PObject aPObject, String theChoices, PlayerCharacter aPC) {
        super(aPObject, theChoices, aPC);
    }

    /**
	 * Construct the choices available from this ChoiceManager in availableList.
	 * Any Feats that are eligible to be added to availableList that the PC
	 * already has will also be added to selectedList.
	 * @param  aPc
	 * @param  availableList
	 * @param  selectedList
	 */
    public void getChoices(final PlayerCharacter aPc, final List availableList, final List selectedList) {
        if (pobject.getAssociatedCount() != 0) {
            pobject.addAssociatedTo(selectedList);
        }
        Iterator it = choices.iterator();
        while (it.hasNext()) {
            String featName = (String) it.next();
            final Ability anAbility = Globals.getAbilityNamed("FEAT", featName);
            if ((anAbility != null) && PrereqHandler.passesAll(anAbility.getPreReqList(), aPc, anAbility)) {
                availableList.add(featName);
                if (aPc.hasRealFeatNamed(featName) && !selectedList.contains(featName)) {
                    selectedList.add(featName);
                }
            }
        }
    }

    /**
	 * Apply the choices made to the PObject this choiceManager is associated with
	 * 
	 * @param aPC
	 * @param selected
	 */
    public void applyChoices(PlayerCharacter aPC, List selected) {
        Iterator i = selected.iterator();
        while (i.hasNext()) {
            final String tempString = (String) i.next();
            AbilityUtilities.modFeat(aPC, null, tempString, true, false);
            pobject.addAssociated(tempString);
        }
    }

    /**
	 * what type of chooser does this handle
	 * 
	 * @return type of chooser
	 */
    public String typeHandled() {
        return chooserHandled;
    }
}
