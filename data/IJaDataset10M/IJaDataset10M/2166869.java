package org.netbeans.module.flexbean.modules.project.ui;

import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author Arnaud
 */
public class FlexProjectSourceNode extends FilterNode {

    FlexProjectSourceNode(Node srcNode) {
        super(srcNode);
    }

    @Override
    public String getDisplayName() {
        return "Sources";
    }
}
