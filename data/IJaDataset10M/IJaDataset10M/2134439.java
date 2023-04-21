package org.jcrpg.world.object;

import java.util.ArrayList;
import java.util.HashMap;
import org.jcrpg.world.ai.body.BodyPart;
import org.jcrpg.world.ai.profession.Profession;

public interface Equippable {

    /**
	 * @return null if no profession needed.
	 */
    public ArrayList<Profession> getProfessionRequirement();

    /**
	 * Attribute name -> minimum value
	 * @return
	 */
    public HashMap<String, Integer> getAttributeRequirement();

    /**
	 * What gender can use it?
	 * @return
	 */
    public int getGenderType();

    /**
	 * 
	 * @return the body part it can be equipped.
	 */
    public Class<? extends BodyPart> getEquippableBodyPart();
}
