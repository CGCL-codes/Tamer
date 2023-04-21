package org.uk2005.dialog;

import java.util.*;
import org.uk2005.data.*;

/**
 * Rule that moves the view of the flow to the next page
 *
 * This rule defaults to allow the movement to the next page.
 * However, through the adding of atoms, it can check that these
 * atoms contain valid values and that their string value does
 * not equal "".
 *
 * @author	<a href="mailto:niklas@saers.com">Niklas Saers</a>
 * @version	$Id: NextPageRule.java,v 1.10 2002/07/12 15:04:17 niklasjs Exp $
 */
public class NextPageRule extends AbstractRule {

    /**
	 * Collection of atoms to check and their errors
	 */
    protected Hashtable atoms = new Hashtable();

    protected Hashtable errors = new Hashtable();

    /**
	 * Default constructor
	 */
    public NextPageRule() {
    }

    /**
	 * Constructor.
	 *
	 * @param	name the rule name.
	 */
    public NextPageRule(String name) {
        setName(name);
    }

    /**
	 * Constructor.
	 *
	 * @param	name the rule name.
	 * @param       error the warning this rule will produce if failed.
	 */
    public NextPageRule(String name, String error) {
        setName(name);
        setError(error);
    }

    /**
	 * Makes the ruling.
	 *
	 * @return	true if the Page should be shown, false otherwise.
	 */
    public boolean rule() {
        boolean result = super.rule();
        logger.debug(getName() + " - Atom containts " + atoms.size() + " atom");
        Enumeration e = atoms.elements();
        while (e.hasMoreElements() && result) {
            Atom atom = (Atom) e.nextElement();
            String value = atom.getValue().trim();
            logger.debug("value \"" + atom.getValue() + "\" - " + atom.isValid());
            result = result && atom.isValid() && (!value.equals(""));
        }
        logger.debug("rule turned out " + result);
        return result;
    }

    /**
	 * Adds atom to ruling
	 *
	 * @param	name the name/key of the atom
	 * @param	atom the atom to be included in the checking
	 * @param      error the error failing to find the atom will produce.
	 */
    public void addAtom(String name, Atom atom, String error) {
        atoms.put(name, atom);
        errors.put(name, error);
    }

    /**
	 * Removes atom from ruling
	 */
    public void removeAtom(String name) {
        atoms.remove(name);
    }

    /**
	 * Gets the error for the ruling
	 */
    public String getError() {
        String error = super.getError();
        Enumeration e = atoms.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            Atom atom = (Atom) atoms.get(name);
            String value = atom.getValue().trim();
            if (!atom.isValid() || value.equals("")) error += (String) errors.get(name);
        }
        return error;
    }
}
