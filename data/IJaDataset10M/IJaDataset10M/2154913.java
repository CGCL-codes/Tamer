package edu.clarkson.serl.se.model;

/**
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 *
 */
public class FloatConstantAbs extends ConstantAbs {

    public static final String TYPE = "java.lang.Float";

    public static final FloatConstantAbs ZERO = new FloatConstantAbs(0.0f);

    public static final FloatConstantAbs ONE = new FloatConstantAbs(1.0f);

    public FloatConstantAbs(Float value) {
        super(TYPE, value);
    }

    @Override
    public IResult execute(String method, IObject[] arguments) {
        return new Result(IObject.UNHANDLED_OBJECT);
    }

    public float getValue() {
        return this.getValue(Float.class);
    }
}
