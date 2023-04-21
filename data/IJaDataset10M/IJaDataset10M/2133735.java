package org.dbwiki.data.schema;

import java.util.Hashtable;
import org.dbwiki.data.document.DocumentGroupNode;
import org.dbwiki.data.resource.SchemaNodeIdentifier;
import org.dbwiki.data.time.TimeSequence;
import org.dbwiki.data.time.TimestampedObject;

/** A timestamped schema node. 
 * A schema node has an id, label, optional parent, and timestamp.
 * 
 * @author jcheney
 *
 */
public abstract class SchemaNode extends TimestampedObject {

    public static final String SchemaPathSeparator = "/";

    public static final int RootID = -1;

    private int _id;

    private String _label;

    private GroupSchemaNode _parent;

    @SuppressWarnings("unused")
    private TimeSequence _timestamp;

    public SchemaNode(int id, String label, GroupSchemaNode parent, TimeSequence timestamp) throws org.dbwiki.exception.WikiException {
        super(parent, timestamp);
        _id = id;
        _label = label;
        _parent = parent;
        _timestamp = timestamp;
        if (_parent != null) {
            _parent.children().add(this);
        }
    }

    public abstract boolean isAttribute();

    public SchemaNodeIdentifier identifier() {
        return new SchemaNodeIdentifier(_id);
    }

    public boolean equals(SchemaNode schema) {
        return (id() == schema.id()) && (label() == schema.label());
    }

    public int id() {
        return _id;
    }

    public boolean isGroup() {
        return !this.isAttribute();
    }

    public String label() {
        return _label;
    }

    public GroupSchemaNode parent() {
        return _parent;
    }

    public String path() {
        if (_parent != null) {
            return _parent.path() + SchemaPathSeparator + _label;
        } else {
            return SchemaPathSeparator + _label;
        }
    }

    public String toString() {
        return label();
    }

    public abstract void printToBuf(StringBuffer buf, String indentation, String extend, String cr);

    /** 
	 * 
	 * @param schema
	 * @param groupIndex
	 * @return
	 * @throws org.dbwiki.exception.WikiException
	 */
    public static DocumentGroupNode createGroupNode(GroupSchemaNode schema, Hashtable<Integer, DocumentGroupNode> groupIndex) throws org.dbwiki.exception.WikiException {
        DocumentGroupNode root = new DocumentGroupNode(schema);
        groupIndex.put(new Integer(schema.id()), root);
        for (int iChild = 0; iChild < schema.children().size(); iChild++) {
            SchemaNode child = schema.children().get(iChild);
            if (child.isGroup()) {
                root.children().add(createGroupNode((GroupSchemaNode) child, groupIndex));
            }
        }
        return root;
    }
}
