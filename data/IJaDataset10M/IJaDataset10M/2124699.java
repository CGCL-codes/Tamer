package org.uk2005.dialog;

import org.uk2005.data.*;
import org.uk2005.store.*;

/**
 * Radio button select list input.
 *
 * @author	<a href="mailto:niklas@saers.com">Niklas Saers</a>
 * @version	$Id: ThingbasedInput.java,v 1.2 2002/07/04 12:25:25 niklasjs Exp $
 */
public abstract class ThingbasedInput extends AbstractInput {

    /**
	 * The thing this input is based upon
	 */
    protected Thing thing = null;

    /**
	 * The constructors from AbstractInput
	 */
    public ThingbasedInput() {
        super();
    }

    public ThingbasedInput(String name) {
        super(name);
    }

    public ThingbasedInput(String name, Atom atom) {
        super(name, atom);
    }

    public ThingbasedInput(String name, String label, Atom atom) {
        super(name, label, atom);
    }

    public ThingbasedInput(String name, String label, String value, Atom atom) {
        super(name, label, value, atom);
    }

    /**
	 * Sets the thing this input is based upon
	 */
    public void setThing(Thing thing) {
        this.thing = thing;
    }

    /**
	 * Gets the thing this input is based upon
	 */
    public Thing getThing() {
        return thing;
    }

    /**
	 * Executes the Thing search and fills the List up accordingly
	 */
    public abstract void executeThing(ThingStore store, Context context);
}
