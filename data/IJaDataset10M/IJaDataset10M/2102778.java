package gate.annotation;

import gate.Node;

/** Provides an implementation for the interface gate.Node.
 *
 */
public class NodeImpl implements Node, Comparable {

    /** Debug flag
   */
    private static final boolean DEBUG = false;

    /** Freeze the serialization UID. */
    static final long serialVersionUID = -8240414984367916298L;

    /** Construction from id. Creates an unrooted node.
   */
    public NodeImpl(Integer id) {
        this.id = id;
        offset = null;
    }

    /** Construction from id and offset.
   *
   * @param id the Id of the new node
   * @param offset the (temporal) offset of the Node; Should be <b>null</b>
   *     for non-anchored nodes.
   */
    public NodeImpl(Integer id, Long offset) {
        this.id = id;
        this.offset = offset;
    }

    /** Returns the Id of the Node.
   */
    public Integer getId() {
        return id;
    }

    /** Offset (will be null when the node is not anchored)
   */
    public Long getOffset() {
        return offset;
    }

    /** String representation
   */
    public String toString() {
        return "NodeImpl: id=" + id + "; offset=" + offset;
    }

    /** Ordering
   */
    public int compareTo(Object o) throws ClassCastException {
        Node other = (Node) o;
        return id.compareTo(other.getId());
    }

    /** To allow AnnotationSet to revise offsets during editing
   */
    void setOffset(Long offset) {
        this.offset = offset;
    }

    /**
   * The id of this node (used for persistency)
   *
   */
    Integer id;

    /**
   * The offset of this node
   *
   */
    Long offset;
}
