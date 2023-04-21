package de.fzi.injectj.model;

import de.fzi.injectj.model.exception.ModelException;
import de.fzi.injectj.script.model.Type;

/**
 * @author Tobias Gutzmann
 * @inject export name=Property package=lang.weavepoints
 */
public abstract class PropertyWeavepoint extends MemberWeavepoint {

    protected ClassWeavepoint parent;

    /** Constructs an property weavepoint.
	 *
	 * @param parent the class weavepoint this attribute is located in
	 */
    public PropertyWeavepoint(ClassWeavepoint parent) {
        super();
        this.parent = parent;
        setModel(parent);
    }

    public final int getTypeID() {
        return Type.PROPERTY;
    }

    /**
	 * @inject export modifier=query
	 */
    public abstract String getType();

    /**
	 * 
	 * @param source
	 * @throws ModelException
	 * @inject export
	 */
    public abstract void setModifier(FragmentType source) throws ModelException;

    public boolean isConsistent() {
        if (super.isConsistent() == false) return false;
        return parent != null;
    }
}
