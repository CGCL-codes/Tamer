package pcgen.core;

/**
 * This tiny little class implements a way to associate prerequisites with a
 * simple string.  This is a subclass of AbilityInfo so we can reuse some of
 * its code
 *
 * @author  Andrew Wilson <nuance@sourceforge.net>
 */
public class ChoiceInfo extends AbilityInfo {

    /**
	 * Make a new object to hold info about a string and some associated
	 * prerequisites.
	 *
	 * @param  key    the Key of the Ability
	 * @param  delim  the Ability's category
	 */
    public ChoiceInfo(String key, char delim) {
        super();
        this.category = "NONE";
        if (delim == '[') {
            this.delim = '[';
        }
        this.extractPrereqs(key);
    }

    public final Ability getAbility() {
        return null;
    }

    /**
	 * Compares this AbilityInfo Object with an Object passed in.  The object
	 * passed in should be either an AbilityInfo Object or a PObject.
	 *
	 * @param   obj  the object to test against
	 *
	 * @return  the result of the compare, negative integer if this should sort
	 *          before
	 */
    public int compareTo(Object obj) {
        return this.keyName.compareTo(obj.toString());
    }
}
