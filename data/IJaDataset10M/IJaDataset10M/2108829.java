package net.sf.orcc.ir.util;

import net.sf.orcc.OrccRuntimeException;
import net.sf.orcc.ir.Expression;
import net.sf.orcc.ir.Type;

/**
 * This class defines a type entry.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class Entry {

    /**
	 * expression entry
	 */
    public static final int EXPR = 1;

    /**
	 * type entry
	 */
    public static final int TYPE = 2;

    /**
	 * the contents of this entry: expression or type.
	 */
    private Object content;

    /**
	 * the type of this entry
	 */
    private int type;

    /**
	 * Creates a new expression entry
	 * 
	 * @param expr
	 *            an expression
	 */
    public Entry(Expression expr) {
        this.content = expr;
        this.type = EXPR;
    }

    /**
	 * Creates a new type entry
	 * 
	 * @param type
	 *            a type
	 */
    public Entry(Type type) {
        this.content = type;
        this.type = TYPE;
    }

    /**
	 * Returns this entry's content as an expression
	 * 
	 * @return this entry's content as an expression
	 */
    public Expression getEntryAsExpr() {
        if (getType() == EXPR) {
            return (Expression) content;
        } else {
            throw new OrccRuntimeException("this entry does not contain an expression");
        }
    }

    /**
	 * Returns this entry's content as a type
	 * 
	 * @return this entry's content as a type
	 */
    public Type getEntryAsType() {
        if (getType() == TYPE) {
            return (Type) content;
        } else {
            throw new OrccRuntimeException("this entry does not contain a type");
        }
    }

    /**
	 * Returns the type of this entry.
	 * 
	 * @return the type of this entry
	 */
    public int getType() {
        return type;
    }
}
