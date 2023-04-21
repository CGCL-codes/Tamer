package org.nodal.implementation.types;

import org.nodal.model.Node;
import org.nodal.model.RecordNode;
import org.nodal.type.MapType;
import org.nodal.type.NodeType;
import org.nodal.type.RecordType;
import org.nodal.type.RestrictionType;
import org.nodal.type.SequenceType;
import org.nodal.type.SetType;
import org.nodal.type.Type;

/**
 * Canonical implementation of NodeType interface.
 * There is a singleton instance of this class, used for the type
 * meaning "any Node".
 *
 * @author Lee Iverson <leei@ece.ubc.ca>
 */
abstract class NodeTypeImpl extends TypeImpl implements NodeType {

    private static NodeTypeImpl singleton = null;

    static NodeTypeImpl create() {
        if (singleton == null) {
            singleton = new BaseNodeType();
        }
        return singleton;
    }

    protected NodeTypeImpl(TypeDoc doc, RecordNode node) {
        super(doc, node);
    }

    public final boolean isNodeType() {
        return true;
    }

    public boolean accepts(boolean b) {
        return false;
    }

    public boolean accepts(char c) {
        return false;
    }

    public boolean accepts(byte b) {
        return false;
    }

    public boolean accepts(short s) {
        return false;
    }

    public boolean accepts(int i) {
        return false;
    }

    public boolean accepts(long l) {
        return false;
    }

    public boolean accepts(float f) {
        return false;
    }

    public boolean accepts(double d) {
        return false;
    }

    public boolean acceptsObject(Object obj) {
        if (obj == null) {
            return true;
        }
        return false;
    }

    public boolean acceptsString(String s) {
        return false;
    }

    public boolean acceptsNode(Node n) {
        return acceptsType(n.nodeType());
    }

    public boolean acceptsType(Type t) {
        if (t instanceof RestrictionType) {
            t = ((RestrictionType) t).baseType();
        }
        return (t instanceof NodeType);
    }

    public Object from(boolean b) {
        return null;
    }

    public Object from(char c) {
        return null;
    }

    public Object from(byte b) {
        return null;
    }

    public Object from(short s) {
        return null;
    }

    public Object from(int b) {
        return null;
    }

    public Object from(long l) {
        return null;
    }

    public Object from(float f) {
        return null;
    }

    public Object from(double d) {
        return null;
    }

    public Object fromString(String s) {
        return null;
    }

    protected Object fromObject(Object obj) {
        return null;
    }

    public Object fromNode(Node n) {
        return acceptsNode(n) ? n.bareNode() : null;
    }

    public abstract Type propertyType();

    public abstract Type valueType(Object v);

    public abstract NodeType.Editor edit();

    public abstract String toString();

    public abstract String declString();

    public MapType asMapType() {
        return null;
    }

    public RecordType asRecordType() {
        return null;
    }

    public SequenceType asSequenceType() {
        return null;
    }

    public SetType asSetType() {
        return null;
    }

    /**
   * This class is defined so that there is an instantiable instance
   * of a generic NodeType.  It is instantiated and returned by the
   * NodeTypeImpl.create() method.
   */
    private static final class BaseNodeType extends NodeTypeImpl {

        BaseNodeType() {
            super(null, null);
        }

        public Type propertyType() {
            return null;
        }

        public Type valueType(Object v) {
            return null;
        }

        public NodeType.Editor edit() {
            return null;
        }

        public String toString() {
            return "<type named=\"ndl:NodeType\"/>";
        }

        public String declString() {
            return "<type named=\"ndl:NodeType\"/>";
        }
    }
}
