package de.grogra.pf.registry.expr;

import de.grogra.pf.registry.RegistryContext;
import de.grogra.util.StringMap;

public final class IntConst extends Expression {

    Integer value;

    public static final NType $TYPE;

    public static final NType.Field value$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(IntConst.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((IntConst) o).value = (Integer) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((IntConst) o).value;
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new IntConst());
        $TYPE.addManagedField(value$FIELD = new _Field("value", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(Integer.class), null, 0));
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new IntConst();
    }

    public IntConst() {
        super(null);
    }

    @Override
    public Object evaluate(RegistryContext ctx, StringMap args) {
        return value;
    }
}
