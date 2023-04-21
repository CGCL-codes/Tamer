package PolicyAlgebra.Type;

/** 
 * This class is used to associate a policy object with a
 * policy type.
 */
public class ObjectTypeAssociation {

    private PolicyObject object;

    private Type type;

    /** 
	 * Constructor. 
	 * 
	 * @param object The policy object to associate with a type.
	 * @param type The type to associate with a policy objct.
	 */
    public ObjectTypeAssociation(PolicyObject object, Type type) {
        this.object = object;
        this.type = type;
    }

    /** 
	 * Getter method for the associated object. 
	 * 
	 * @return The object.
	 */
    public PolicyObject getObject() {
        return object;
    }

    /** 
	 * Getter method for the associated type. 
	 * 
	 * @return The type.
	 */
    public Type getType() {
        return type;
    }
}
