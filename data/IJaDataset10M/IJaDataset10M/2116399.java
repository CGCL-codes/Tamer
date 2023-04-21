package jezuch.utils.starmapper3.model;

/**
 * This is a hopefully common base class for custom implementations of
 * {@link NodeInfo} interface. It provides basic facilities like storing and
 * returning properties for the {@link #getNode() node},
 * {@link #getOwner() owner}, {@link #getReporter() reporter} and
 * {@link #getReportTime() report time}. Other methods of the interface should
 * be implemented separately.
 * <p>
 * This class implements methods {@link #equals(Object)} and {@link #hashCode()}
 * based on the properties declared by {@link NodeInfo}; subclasses should
 * override them if they add other proprties, but may call the
 * superimplementations to exploit the base behaviour.
 * <p>
 * This class also provides several utilities, like various variants of
 * {@link #equals(NodeInfo, NodeInfo) equals} and
 * {@link #shouldMergeWith(NodeInfo, NodeInfo)} method, which can be helpful for
 * implementation of {@link NodeInfo#mergeWith(NodeInfo)}.
 * 
 * @author ksobolewski
 */
public abstract class AbstractNodeInfo<N extends Node, R extends Region, I extends NodeInfo<N, R, I>> implements NodeInfo<N, R, I> {

    public enum MergeResolution {

        OLDER, NEWER, CONFLICT
    }

    /**
	 * @param o1
	 *            the first object to compare
	 * @param o2
	 *            the second object to compare
	 * @return {@literal true} if the two given objects are
	 *         {@link Object#equals(Object) equal} or are both {@literal null}
	 */
    protected static boolean equalsNullable(Object o1, Object o2) {
        return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
    }

    /**
	 * Compares one {@link NodeInfo} to an arbitrary object; the result can be
	 * {@literal true} only if the second object is also a {@link NodeInfo}.
	 * 
	 * @param o1
	 *            the {@link NodeInfo}
	 * @param o2
	 *            the second object to compare
	 * @return {@literal true} if the second object is a {@link NodeInfo} equal
	 *         to the firs object
	 * @see #equals(NodeInfo, NodeInfo)
	 */
    protected static boolean equals(NodeInfo<?, ?, ?> o1, Object o2) {
        return o2 instanceof NodeInfo<?, ?, ?> && equals(o1, (NodeInfo<?, ?, ?>) o2);
    }

    /**
	 * Compares two {@link NodeInfo}s. The node infos are considered equal if
	 * all of their properties, as declared by {@link NodeInfo} interface, are
	 * equal (or both {@literal null}).
	 * 
	 * @param e1
	 *            the first {@link NodeInfo} to compare
	 * @param e2
	 *            the second {@link NodeInfo} to compare
	 * @return {@literal true} if the two {@link NodeInfo}s have equal
	 *         properties
	 */
    protected static boolean equals(NodeInfo<?, ?, ?> e1, NodeInfo<?, ?, ?> e2) {
        return e1.getNode().equals(e2.getNode()) && equalsNullable(e1.getOwner(), e2.getOwner()) && e1.getReportTime() == e2.getReportTime() && e1.getReporter().equals(e2.getReporter());
    }

    /**
	 * A helper method that gives a hint for implementors of
	 * {@link NodeInfo#mergeWith(NodeInfo)} method. The return value tells if
	 * the given (other) node info is newer, older or "conflicts" with this node
	 * info. The comparison is based on time of the reports, but for reports
	 * from the same time the report that comes from the owner of the node is
	 * considered "newer". The reports "conflict" if are from the same time and
	 * the owner of the node is not "better" than the reporter; these reports
	 * should be merged "manually".
	 * 
	 * @param other
	 *            the other {@link NodeInfo} to compare
	 * @return whether the other nodeinfo is newer, older or "conflicts" with
	 *         this one
	 */
    public static MergeResolution shouldMergeWith(NodeInfo<?, ?, ?> that, NodeInfo<?, ?, ?> other) {
        if (other.getReportTime() > that.getReportTime() || (other.getReportTime() == that.getReportTime() && other.getReporter().equals(other.getOwner()))) {
            return MergeResolution.NEWER;
        } else if (that.getReportTime() > other.getReportTime() || (that.getReportTime() == other.getReportTime() && that.getReporter().equals(that.getOwner()))) {
            return MergeResolution.OLDER;
        } else {
            return MergeResolution.CONFLICT;
        }
    }

    private final N node;

    private final R owner;

    private final int reportTime;

    private final R reporter;

    /**
	 * Initialises this object with the given node, reporter, report time and
	 * owner. Node and reporter which are {@literal null} are nor allowed.
	 * 
	 * @param node
	 *            the {@link Node node}
	 * @param reporter
	 *            the {@link Region reporter}
	 * @param reportTime
	 *            the report time
	 * @param owner
	 *            the {@link Region owner} of the node
	 */
    public AbstractNodeInfo(N node, R reporter, int reportTime, R owner) {
        if (node == null) throw new IllegalArgumentException("node");
        if (reporter == null) throw new IllegalArgumentException("reporter");
        this.node = node;
        this.owner = owner;
        this.reportTime = reportTime;
        this.reporter = reporter;
    }

    public N getNode() {
        return node;
    }

    public R getOwner() {
        return owner;
    }

    public int getReportTime() {
        return reportTime;
    }

    /**
	 * Always returns {@literal false}.
	 * 
	 * @see NodeInfo#isObsolete(int)
	 */
    public boolean isObsolete(int time) {
        return false;
    }

    public R getReporter() {
        return reporter;
    }

    /**
	 * @param o
	 *            the other object to compare
	 * @return {@literal true} if the other object is also a {@link NodeInfo}
	 *         and has properties declared by this interface equal to this
	 *         object's
	 * @see #equals(NodeInfo, Object)
	 */
    @Override
    public boolean equals(Object o) {
        return o != this && equals(this, o);
    }

    /**
	 * @return a hash code of this object based on has codes or values of this
	 *         object's properties: node, owner, report time and reporter
	 */
    @Override
    public int hashCode() {
        return ((node.hashCode() * 31 + (owner == null ? 0 : owner.hashCode())) * 31 + reportTime) * 31 + (reporter == null ? 0 : reporter.hashCode());
    }

    @Override
    public String toString() {
        return String.format("[%s; node=%s, owner=%s, reporter=%s, time=%d]", getClass().getSimpleName(), node, owner, reporter, reportTime);
    }
}
