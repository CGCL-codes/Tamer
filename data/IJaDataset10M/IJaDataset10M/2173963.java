package org.netbeans.module.flexbean.platform.ui;

import org.netbeans.module.flexbean.platform.FlexPlatform;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author arnaud
 */
public class FlexPlatformNode extends AbstractNode {

    private final String name;

    public FlexPlatformNode(FlexPlatform flexPlatform) {
        super(Children.LEAF);
        this.name = flexPlatform.getFlexSpecification().getName();
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }
}
