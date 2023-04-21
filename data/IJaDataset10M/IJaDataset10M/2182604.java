package net.sf.rem.loaders;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;

public class ZsDataNode extends DataNode {

    private static final String IMAGE_ICON_BASE = "net/sf/rem/resources/zs.png";

    public ZsDataNode(ZsDataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }
}
