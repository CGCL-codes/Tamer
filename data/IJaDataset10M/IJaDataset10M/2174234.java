package ast;

import java.util.Map;

public class ConditionAtom extends Atom implements Condition {

    public static enum Type implements AtomType {

        STRONGER, WEAKER, MUCH_STRONGER, MUCH_WEAKER, EVEN, NEAR, FAR, ALWAYS
    }

    public ConditionAtom(String type) {
        super(type);
    }

    @Override
    public boolean evaluate(Map<AtomType, Boolean> state) {
        try {
            return state.get(type);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public Type getType() {
        return (Type) type;
    }

    @Override
    public void setTypeFromTypeName() {
        type = Type.valueOf(typeName.toUpperCase());
    }
}
