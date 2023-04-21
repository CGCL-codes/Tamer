package alice.cartago;

/**
 * Basic class representing operations to be executed on artifacts
 * 
 * @author aricci
 *
 */
public class Op {

    private String name;

    private Class[] types;

    private Object[] values;

    private static Object[] NO_PARMS = new Object[0];

    private static Class[] NO_PARMS_CLASS = new Class[0];

    /**
	 * Specifies an operation with no arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name) {
        this.name = name;
        values = NO_PARMS;
        types = NO_PARMS_CLASS;
    }

    /**
	 * Specifies an operation with one arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0) {
        this(name);
        values = new Object[] { obj0 };
    }

    /**
	 * Specifies an operation with 2 arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0, Object obj1) {
        this(name);
        values = new Object[] { obj0, obj1 };
    }

    /**
	 * Specifies an operation with 3 arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0, Object obj1, Object obj2) {
        this(name);
        values = new Object[] { obj0, obj1, obj2 };
    }

    /**
	 * Specifies an operation with 4 arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0, Object obj1, Object obj2, Object obj3) {
        this(name);
        values = new Object[] { obj0, obj1, obj2, obj3 };
    }

    /**
	 * Specifies an operation with 5 arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0, Object obj1, Object obj2, Object obj3, Object obj4) {
        this(name);
        values = new Object[] { obj0, obj1, obj2, obj3, obj4 };
    }

    /**
	 * Specifies an operation with 6 arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        this(name);
        values = new Object[] { obj0, obj1, obj2, obj3, obj4, obj5 };
    }

    /**
	 * Specifies an operation with 7 arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        this(name);
        values = new Object[] { obj0, obj1, obj2, obj3, obj4, obj5, obj6 };
    }

    /**
	 * Specifies an operation with 8 arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        this(name);
        values = new Object[] { obj0, obj1, obj2, obj3, obj4, obj5, obj6, obj7 };
    }

    /**
	 * Specifies an operation with 9 arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
        this(name);
        values = new Object[] { obj0, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8 };
    }

    /**
	 * Specifies an operation with 10 arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9) {
        this(name);
        values = new Object[] { obj0, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9 };
    }

    /**
	 * Specifies an operation with any number of arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object[] params) {
        this(name);
        values = params;
    }

    /**
	 * Specifies an operation with any number of arguments,
	 * specifying the type of the arguments
	 * 
	 * @param targetId id of the artifact
	 * @param name operation name
	 */
    public Op(String name, Object[] values, Class[] types) {
        this(name, values);
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public Object[] getParamValues() {
        return values;
    }

    public Class[] getParamTypes() {
        return types;
    }
}
