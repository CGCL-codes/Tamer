package bsh;

import java.lang.reflect.Field;
import java.util.Hashtable;

/**
	An LHS is a wrapper for an variable, field, or property.  It ordinarily 
	holds the "left hand side" of an assignment and may be either resolved to 
	a value or assigned a value.
	<p>
	
	There is one special case here termed METHOD_EVAL where the LHS is used
	in an intermediate evaluation of a chain of suffixes and wraps a method
	invocation.  In this case it may only be resolved to a value and cannot be 
	assigned.  (You can't assign a value to the result of a method call e.g.
	"foo() = 5;").
	<p>
*/
class LHS implements ParserConstants, java.io.Serializable {

    NameSpace nameSpace;

    /** The assignment should be to a local variable */
    boolean localVar;

    /**
		Identifiers for the various types of LHS.
	*/
    static final int VARIABLE = 0, FIELD = 1, PROPERTY = 2, INDEX = 3, METHOD_EVAL = 4;

    int type;

    String varName;

    String propName;

    Field field;

    Object object;

    int index;

    /**
		Variable LHS constructor.
*/
    LHS(NameSpace nameSpace, String varName) {
        throw new Error("namespace lhs");
    }

    /**
		@param localVar if true the variable is set directly in the This
		reference's local scope.  If false recursion to look for the variable
		definition in parent's scope is allowed. (e.g. the default case for
		undefined vars going to global).
	*/
    LHS(NameSpace nameSpace, String varName, boolean localVar) {
        type = VARIABLE;
        this.localVar = localVar;
        this.varName = varName;
        this.nameSpace = nameSpace;
    }

    /**
		Static field LHS Constructor.
		This simply calls Object field constructor with null object.
	*/
    LHS(Field field) {
        type = FIELD;
        this.object = null;
        this.field = field;
    }

    /**
		Object field LHS Constructor.
	*/
    LHS(Object object, Field field) {
        if (object == null) throw new NullPointerException("constructed empty LHS");
        type = FIELD;
        this.object = object;
        this.field = field;
    }

    /**
		Object property LHS Constructor.
	*/
    LHS(Object object, String propName) {
        if (object == null) throw new NullPointerException("constructed empty LHS");
        type = PROPERTY;
        this.object = object;
        this.propName = propName;
    }

    /**
		Array index LHS Constructor.
	*/
    LHS(Object array, int index) {
        if (array == null) throw new NullPointerException("constructed empty LHS");
        type = INDEX;
        this.object = array;
        this.index = index;
    }

    public Object getValue() throws UtilEvalError {
        if (type == VARIABLE) return nameSpace.getVariable(varName);
        if (type == FIELD) try {
            Object o = field.get(object);
            return Primitive.wrap(o, field.getType());
        } catch (IllegalAccessException e2) {
            throw new UtilEvalError("Can't read field: " + field);
        }
        if (type == PROPERTY) try {
            return Reflect.getObjectProperty(object, propName);
        } catch (ReflectError e) {
            Interpreter.debug(e.getMessage());
            throw new UtilEvalError("No such property: " + propName);
        }
        if (type == INDEX) try {
            return Reflect.getIndex(object, index);
        } catch (Exception e) {
            throw new UtilEvalError("Array access: " + e);
        }
        throw new InterpreterError("LHS type");
    }

    /**
		Assign a value to the LHS.
	*/
    public Object assign(Object val, boolean strictJava) throws UtilEvalError {
        if (type == VARIABLE) {
            if (localVar) nameSpace.setLocalVariable(varName, val, strictJava); else nameSpace.setVariable(varName, val, strictJava);
        } else if (type == FIELD) {
            try {
                Object fieldVal = val instanceof Primitive ? ((Primitive) val).getValue() : val;
                ReflectManager.RMSetAccessible(field);
                field.set(object, fieldVal);
                return val;
            } catch (NullPointerException e) {
                throw new UtilEvalError("LHS (" + field.getName() + ") not a static field.");
            } catch (IllegalAccessException e2) {
                throw new UtilEvalError("LHS (" + field.getName() + ") can't access field: " + e2);
            } catch (IllegalArgumentException e3) {
                String type = val instanceof Primitive ? ((Primitive) val).getType().getName() : val.getClass().getName();
                throw new UtilEvalError("Argument type mismatch. " + (val == null ? "null" : type) + " not assignable to field " + field.getName());
            }
        } else if (type == PROPERTY) {
            CollectionManager cm = CollectionManager.getCollectionManager();
            if (cm.isMap(object)) cm.putInMap(object, propName, val); else try {
                Reflect.setObjectProperty(object, propName, val);
            } catch (ReflectError e) {
                Interpreter.debug("Assignment: " + e.getMessage());
                throw new UtilEvalError("No such property: " + propName);
            }
        } else if (type == INDEX) try {
            Reflect.setIndex(object, index, val);
        } catch (UtilTargetError e1) {
            throw e1;
        } catch (Exception e) {
            throw new UtilEvalError("Assignment: " + e.getMessage());
        } else throw new InterpreterError("unknown lhs");
        return val;
    }

    public String toString() {
        return "LHS: " + ((field != null) ? "field = " + field.toString() : "") + (varName != null ? " varName = " + varName : "") + (nameSpace != null ? " nameSpace = " + nameSpace.toString() : "");
    }
}
