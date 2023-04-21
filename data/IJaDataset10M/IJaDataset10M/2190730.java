package storage.framework;

import java.util.Iterator;
import org.nodal.Types;
import org.nodal.model.Getter;
import org.nodal.model.Node;
import org.nodal.model.NodeContent;
import org.nodal.model.RecordContent;
import org.nodal.model.RecordNode;
import org.nodal.model.Setter;
import org.nodal.model.TxnOp;
import org.nodal.nav.Path;
import org.nodal.nav.PathOperator;
import org.nodal.nav.Paths;
import org.nodal.security.Capability;
import org.nodal.type.NodeType;
import org.nodal.type.RecordType;
import org.nodal.type.Type;
import org.nodal.util.CacheMap;
import org.nodal.util.ConstraintFailure;
import org.nodal.util.GetterUtil;
import org.nodal.util.Name;
import org.nodal.util.Names;
import org.nodal.util.PropertyConstraintFailure;
import org.nodal.util.PropertyTypeMismatch;
import org.nodal.util.SetterUtil;
import org.nodal.util.TypeConstraintFailure;

/**
 * A memory-based container for RecordType contents. Maps Names to values, given
 * the mapping defined by the RecordType.
 * 
 * @author leei
 */
final class BareRecordContent extends AbstractContent {

    protected final RecordType recordType;

    protected final RecordLayout layout;

    /**
   * The actual content. Indexed by the valuse described in {@link layout}.
   */
    protected Object[] fields;

    /**
   * A Map from Name->Getter.
   */
    protected CacheMap getters;

    static BareRecordContent ingest(RecordContent c) {
        RecordType rt = (RecordType) c.type();
        BareRecordContent rc = new BareRecordContent(rt);
        Iterator i = rt.fields();
        try {
            while (i.hasNext()) {
                Name f = (Name) i.next();
                int idx = rc.lookup(f);
                Type fieldType = rt.fieldType(f);
                rc.setFieldValue(f, idx, fieldType, c.field(f).get(), null);
            }
        } catch (ConstraintFailure f) {
            throw new RuntimeException("constraint failure in ingest: " + f);
        }
        return rc;
    }

    static BareRecordContent create(RecordType t) {
        return new BareRecordContent(t);
    }

    private BareRecordContent(RecordType t) {
        recordType = t;
        layout = RecordLayout.getLayout(t);
        fields = new Object[layout.size()];
        getters = new CacheMap();
    }

    int lookup(Name f) throws PropertyConstraintFailure {
        return layout.lookup(f);
    }

    public boolean hasField(Name nm) {
        return layout.isField(nm);
    }

    public boolean hasField(String str) {
        Name nm = Names.getName(str);
        return layout.isField(nm);
    }

    public boolean isValidProperty(Object property) {
        if (property instanceof Name) {
            return hasField((Name) property);
        } else if (property instanceof String) {
            return hasField((String) property);
        } else {
            return false;
        }
    }

    public Object getField(Name name, NodeContent c) throws PropertyConstraintFailure {
        return field(name, c).get();
    }

    public Object getField(String name, NodeContent c) throws PropertyConstraintFailure {
        return getField(Names.getName(name), c);
    }

    /**
   * Called by BareNode to create a NodeContent object.
   */
    protected NodeContent createNodeContent(AbstractNode.Backend n, Capability cap) {
        return new MyNodeContent(n, cap);
    }

    private class MyNodeContent extends AbstractRecordNode {

        private RecordNode.Editor editor;

        MyNodeContent(AbstractNode.Backend n, Capability cap) {
            super(n, BareRecordContent.this, cap);
            editor = null;
        }

        public RecordNode.Editor editRecord() {
            if (editor == null && capability().allowEdit()) {
                editor = new MyNodeEditor(this);
            }
            return editor;
        }
    }

    public NodeType type() {
        return recordType;
    }

    public int size() {
        return layout.size();
    }

    public Iterator properties() {
        return recordType.fields();
    }

    public Getter value(Object obj, NodeContent c) throws PropertyConstraintFailure {
        if (obj instanceof Name) {
            return field((Name) obj, c);
        } else if (obj instanceof String) {
            return field(Names.getName((String) obj), c);
        }
        throw new PropertyTypeMismatch(obj, Types.NAME, "invalid field name");
    }

    public Getter field(Name nm, NodeContent c) throws PropertyConstraintFailure {
        return fieldGetter(nm, c);
    }

    public Getter field(String nm, NodeContent c) throws PropertyConstraintFailure {
        return field(Names.getName(nm), c);
    }

    /**
   * Create or find a GetterUtil for the named field.
   */
    protected GetterUtil fieldGetter(Name nm, NodeContent c) throws PropertyConstraintFailure {
        GetterUtil util = (GetterUtil) getters.get(nm);
        if (util == null) {
            util = new FieldGetter(nm, lookup(nm), c);
            getters.put(nm, util);
        }
        return util;
    }

    /**
   * Implementation of the Getter associated with the named field.
   */
    private class FieldGetter extends GetterUtil {

        protected Name field;

        protected int idx;

        protected NodeContent content;

        FieldGetter(Name f, int i, NodeContent c) {
            super(recordType.fieldType(f));
            field = f;
            idx = i;
            content = c;
        }

        public Object get() {
            return fields[idx];
        }

        void notifySetValue(Name field, Object value) {
            notifySetValue(content, field, value);
        }

        public boolean valueEquals(Object v) throws PropertyConstraintFailure {
            if (v != null && getterType.isNodeType()) {
                v = ((Node) v).bareNode();
            }
            return v.equals(get());
        }

        protected Path createPath() throws Path.Failure {
            PathOperator op = Paths.createPropertyOp(field);
            return op.applyTo(content.path());
        }
    }

    /**
   * Notify all interested parties when we assign a value to a key.
   * 
   * @param field
   *          the field Name being set
   * @param val
   *          the value assigned to this key
   */
    private void notifySetValue(Name field, Object val) {
        Iterator i = nodeContents.values();
        while (i.hasNext()) {
            MyNodeContent c = (MyNodeContent) i.next();
            c.notifySetValue(field, val);
        }
        FieldGetter keyGetter = (FieldGetter) getters.get(field);
        if (keyGetter != null) {
            keyGetter.notifySetValue(field, val);
        }
    }

    /**
   * Set a field's value. No other method can directly set the value of a field.
   * 
   * @param field
   *          the Name of the field
   * @param idx
   *          the index of that field in the vector
   * @param fieldType
   *          the Type of the field
   * @param v
   *          the value to set the field to
   * @throws ConstraintFailure
   *           if v is incompatible
   */
    private void setFieldValue(Name field, int idx, Type fieldType, Object v, AbstractNodeContent.Editor c) throws ConstraintFailure {
        if (!fieldType.accepts(v)) {
            throw new TypeConstraintFailure(v, fieldType, "invalid value for ." + field);
        }
        v = fieldType.from(v);
        if (fieldType.isNodeType()) {
            v = c.assignNode(field, (Node) v);
        }
        notifySetValue(field, v);
        fields[idx] = v;
    }

    void opSetValue(Object key, Object val, AbstractNodeContent.Editor c) throws ConstraintFailure {
        if (key instanceof Name) {
            opSetField((Name) key, val, c);
        } else {
            throw new PropertyTypeMismatch(key, Types.NAME, "invalid field name");
        }
    }

    void opSetField(Name field, Object v, AbstractNodeContent.Editor c) throws ConstraintFailure {
        Type fieldType = recordType.fieldType(field);
        setFieldValue(field, lookup(field), fieldType, v, c);
    }

    AbstractOperator invertSetField(Name field, NodeContent c) throws PropertyConstraintFailure {
        Object value = getField(field, c);
        return AbstractOperator.createOpSetValue(field, value);
    }

    private final class MyNodeEditor extends AbstractNodeContent.Editor implements RecordNode.Editor {

        private MyNodeContent content;

        MyNodeEditor(MyNodeContent c) {
            super(c);
            this.content = c;
        }

        public final boolean hasField(Name nm) {
            return content.hasField(nm);
        }

        public final boolean hasField(String nm) {
            return content.hasField(nm);
        }

        public NodeContent content() {
            return content;
        }

        public RecordNode asRecordNode() {
            return content;
        }

        public Getter field(Name name) throws PropertyConstraintFailure {
            return BareRecordContent.this.field(name, content);
        }

        public Getter field(String name) throws PropertyConstraintFailure {
            return field(Names.getName(name));
        }

        public Object getField(Name name) throws PropertyConstraintFailure {
            return field(name).get();
        }

        public Object getField(String name) throws PropertyConstraintFailure {
            return getField(Names.getName(name));
        }

        public Setter setValue(Object key) throws PropertyConstraintFailure {
            if (key instanceof Name) {
                return setField((Name) key);
            } else if (key instanceof String) {
                return setField(Names.getName((String) key));
            }
            throw new PropertyTypeMismatch(key, Types.NAME, "invalid field name");
        }

        public Setter setField(Name f) throws PropertyConstraintFailure {
            return SetterUtil.create(BareRecordContent.this.fieldGetter(f, content), new FieldSetter(f, nodeMgr));
        }

        public Setter setField(String f) throws PropertyConstraintFailure {
            return setField(Names.getName(f));
        }

        private class FieldSetter implements SetterUtil.PlainSetter {

            private final Name field;

            private final Type fieldType;

            private final int idx;

            private final NodeManager nodeMgr;

            FieldSetter(Name f, NodeManager mgr) throws PropertyConstraintFailure {
                field = f;
                nodeMgr = mgr;
                fieldType = BareRecordContent.this.recordType.fieldType(field);
                idx = BareRecordContent.this.lookup(f);
            }

            public Object set(Object val) throws ConstraintFailure {
                TxnOp op = null;
                try {
                    if (nodeMgr != null) {
                        op = nodeMgr.builder().addOpSetField(content, field, val);
                    }
                    BareRecordContent.this.setFieldValue(field, idx, fieldType, val, MyNodeEditor.this);
                } catch (ConstraintFailure f) {
                    if (nodeMgr != null) {
                        nodeMgr.builder().retractOp(op);
                        throw f;
                    }
                }
                return val;
            }
        }

        public String toString() {
            return "RecordEditor{" + path() + "}";
        }

        public RecordNode.Editor editRecord() {
            return this;
        }
    }
}
