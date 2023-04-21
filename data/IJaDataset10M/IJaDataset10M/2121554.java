package vmap.modes.browsemode;

import vmap.main.VmapMain;
import vmap.modes.MindMapNode;
import vmap.modes.ArrowLinkAdapter;
import vmap.main.Tools;
import java.awt.Color;
import vmap.main.XMLElement;

public class BrowseArrowLinkModel extends ArrowLinkAdapter {

    public BrowseArrowLinkModel(MindMapNode source, MindMapNode target, VmapMain frame) {
        super(source, target, frame);
    }

    public Object clone() {
        return super.clone();
    }

    public XMLElement save() {
        return null;
    }

    public String toString() {
        return "Source=" + getSource() + ", target=" + getTarget();
    }
}
