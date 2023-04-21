package freemind.modes.browsemode;

import freemind.main.FreeMindMain;
import freemind.modes.MindMapNode;
import freemind.modes.CloudAdapter;
import freemind.main.Tools;
import java.awt.Color;
import freemind.main.XMLElement;

public class BrowseCloudModel extends CloudAdapter {

    public BrowseCloudModel(MindMapNode node, FreeMindMain frame) {
        super(node, frame);
    }

    public XMLElement save() {
        return null;
    }
}
