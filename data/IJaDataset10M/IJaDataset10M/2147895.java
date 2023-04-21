package org.netbeans.cubeon.tasks.core.spi;

import org.openide.nodes.Node;

/**
 *
 * @author Anuradha G
 */
public interface TaskNodeView {

    String getId();

    String getName();

    String getDescription();

    Node getRootContext();
}
