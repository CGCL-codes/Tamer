package com.google.gxp.compiler.base;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.gxp.compiler.alerts.SourcePosition;
import java.io.Serializable;

/**
 * Straightforward serializable implementation of {@link Node}.
 */
@SuppressWarnings("serial")
public class SerializableAbstractNode implements Node, Serializable {

    private final SourcePosition sourcePosition;

    private final String displayName;

    /**
   * @param sourcePosition the {@link SourcePosition} of this {@code Node}
   * @param displayName the display name of this {@code Node}
   */
    protected SerializableAbstractNode(SourcePosition sourcePosition, String displayName) {
        this.sourcePosition = Preconditions.checkNotNull(sourcePosition);
        this.displayName = Preconditions.checkNotNull(displayName);
    }

    /**
   * Creates an {@code SerializableAbstractNode} based on another Node.
   *
   * @param fromNode the {@code Node} that this {@code Node} is derived from
   */
    protected SerializableAbstractNode(Node fromNode) {
        this(fromNode.getSourcePosition(), fromNode.getDisplayName());
    }

    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }

    public final String getDisplayName() {
        return displayName;
    }

    protected final boolean equalsAbstractNode(SerializableAbstractNode that) {
        return Objects.equal(getSourcePosition(), that.getSourcePosition()) && Objects.equal(getDisplayName(), that.getDisplayName());
    }

    protected final int abstractNodeHashCode() {
        return Objects.hashCode(getSourcePosition(), getDisplayName());
    }
}
