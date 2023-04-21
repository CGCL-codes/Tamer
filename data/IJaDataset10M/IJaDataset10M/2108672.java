package net.sourceforge.javautil.bytecode.api;

import net.sourceforge.javautil.bytecode.api.type.method.BytecodeContextMethod;

/**
 * A literal value.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class LiteralValue extends BytecodeReferenceableAbstract<BytecodeContextMethod> {

    protected final boolean primitive;

    protected final Object value;

    public LiteralValue(int i) {
        this(i, true);
    }

    public LiteralValue(long i) {
        this(i, true);
    }

    public LiteralValue(short i) {
        this(i, true);
    }

    public LiteralValue(boolean i) {
        this(i, true);
    }

    public LiteralValue(byte i) {
        this(i, true);
    }

    public LiteralValue(char i) {
        this(i, true);
    }

    public LiteralValue(double i) {
        this(i, true);
    }

    public LiteralValue(float i) {
        this(i, true);
    }

    public LiteralValue(Object value) {
        this(value, false);
    }

    protected LiteralValue(Object value, boolean primitive) {
        super(primitive ? TypeDescriptor.getFor(value.getClass()).getUnboxedType() : TypeDescriptor.getFor(value.getClass()));
        this.primitive = primitive;
        this.value = value;
    }

    /**
	 * @return True if this literal value is a primitive
	 */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
	 * @return The actual value wrapped
	 */
    public Object getValue() {
        return value;
    }

    public void load(BytecodeContextMethod context) {
        context.getWriter().loadLiteral(context, this);
    }
}
