package pcgen.cdom.base;

import pcgen.core.PlayerCharacter;
import pcgen.persistence.PersistenceLayerException;

/**
 * A ChooseResultActor is an object that can apply and remove choices (based on
 * the CHOOSE token) to a PlayerCharacter. This is an object that will act after
 * a selection has been made by a user through through the chooser system.
 */
public interface ChooseResultActor {

    /**
	 * Applies the given choice to the given PlayerCharacter.
	 * 
	 * @param pc
	 *            The PlayerCharacter to which the given choice should be
	 *            applied.
	 * @param obj
	 *            The CDOMObject to which the choice was applied (the CDOMObject
	 *            on which the CHOOSE token was present)
	 * @param choice
	 *            The choice being applied to the given PlayerCharacter
	 */
    void apply(PlayerCharacter pc, CDOMObject obj, String choice);

    /**
	 * Removes the given choice from the given PlayerCharacter.
	 * 
	 * @param pc
	 *            The PlayerCharacter from which the given choice should be
	 *            removed.
	 * @param obj
	 *            The CDOMObject to which the choice was applied (the CDOMObject
	 *            on which the CHOOSE token was present)
	 * @param choice
	 *            The choice being removed from the given PlayerCharacter
	 */
    void remove(PlayerCharacter pc, CDOMObject obj, String choice);

    /**
	 * Returns the source of this ChooseResultActor. Provided primarily to allow
	 * the Token/Loader system to properly identify the source of
	 * ChooseResultActors for purposes of unparsing.
	 * 
	 * @return The source of this ChooseResultActor
	 */
    String getSource();

    /**
	 * Returns the LST format for this ChooseResultActor. Provided primarily to
	 * allow the Token/Loader system to properly unparse the ChooseResultActor.
	 * 
	 * @return The LST format of this ChooseResultActor
	 */
    String getLstFormat() throws PersistenceLayerException;
}
