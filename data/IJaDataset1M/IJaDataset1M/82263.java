package gov.nasa.runjpf;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

public class JPFConfigDataNode extends DataNode {

    private static final String IMAGE_ICON_BASE = "gov/nasa/runjpf/runjpf.gif";

    public JPFConfigDataNode(JPFConfigDataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    JPFConfigDataNode(JPFConfigDataObject obj, Lookup lookup) {
        super(obj, Children.LEAF, lookup);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }
}
