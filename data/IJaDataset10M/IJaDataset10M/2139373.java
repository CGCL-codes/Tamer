package org.sqlexp.sql;

/**
 * Object reference.<br>
 * Objects of this class reference another <b>loaded</b> object in server tree.<br>
 * The name of a reference object is the referenced object name.
 * @param <Parent> class
 * @param <Child> class
 * @author Matthieu Réjou
 */
public abstract class SqlObjectReference<Parent extends SqlObject<?, ?>, Child extends SqlObject<?, ?>> extends SqlObject<Parent, Child> {

    private SqlObject<?, ?> referenced;

    /**
	 * Constructs an SQL reference object.
	 * @param parent to add the object to
	 * @param referenced SQL object
	 */
    public SqlObjectReference(final Parent parent, final SqlObject<?, ?> referenced) {
        super(parent, referenced.getName());
        this.referenced = referenced;
    }

    @Override
    public final boolean allowRename() {
        return false;
    }

    /**
	 * Gets the referenced SQL object.
	 * @return referenced object
	 */
    public final SqlObject<?, ?> getReferenced() {
        return referenced;
    }
}
