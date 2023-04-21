package com.google.gxp.compiler.base;

import com.google.common.base.Preconditions;
import com.google.gxp.compiler.alerts.AlertSet;
import com.google.gxp.compiler.alerts.SourcePosition;
import java.util.Collections;

/**
 * A {@code Forest}, but with only one child: the "root".
 *
 * @param <E> type of root node
 */
public abstract class Tree<E extends Root> extends Forest<E> {

    /**
   * @param sourcePosition the {@link SourcePosition} of this {@code Tree}
   * @param alerts the {@link com.google.gxp.compiler.alerts.Alert}s
   * associated with this {@code Tree}
   * @param root the only child of this {@code Tree}
   */
    protected Tree(SourcePosition sourcePosition, AlertSet alerts, E root) {
        super(sourcePosition, alerts, Collections.singletonList(Preconditions.checkNotNull(root)));
    }

    /**
   * @return the root of this tree, or null if there is none.
   */
    public E getRoot() {
        return getChildren().get(0);
    }
}
