package de.grogra.mtg.nodes;

import de.grogra.mtg.Attributes;
import de.grogra.mtg.MTGNode;
import de.grogra.mtg.MTGNodeData;

/**
 * @author yong
 * @since  2011-11-24
 */
public class J extends MTGNode {

    public static final NType $TYPE;

    public static final NType.Field nodeData$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(J.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        public void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((J) o).setData((MTGNodeData) value);
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object object) {
            switch(id) {
                case 0:
                    return ((J) object).getData();
            }
            return super.getObject(object);
        }
    }

    static {
        $TYPE = new NType(J.class);
        $TYPE.addManagedField(nodeData$FIELD = new _Field("nodeData", _Field.PUBLIC | _Field.SCO, de.grogra.reflect.Type.OBJECT, null, 0));
        $TYPE.declareFieldAttribute(nodeData$FIELD, Attributes.MTG_NODE_DATA);
        $TYPE.addDependency(nodeData$FIELD.getAttribute(), Attributes.MTG_NODE_DATA);
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new J();
    }
}
