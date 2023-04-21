package com.google.gxp.compiler.base;

import com.google.gxp.compiler.alerts.SourcePosition;

/**
 * An artifact generated from some source code. It's probably easiest
 * to think of a {@code Node} as an AST node, though most {@code Node}s are
 * only indirectly a result of parsing.
 */
public interface Node {

    /**
   * @return the {@link SourcePosition} of this {@code Node}.
   */
    SourcePosition getSourcePosition();

    /**
   * @return the name to use for this {@code Node} in {@code Alert}s.
   */
    String getDisplayName();
}
