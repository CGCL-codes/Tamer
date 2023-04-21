package plugins.map;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

/**
 * @author foltin
 * @date 13.12.2011
 */
public class RemoveMapToNodeAction extends MindMapNodeHookAdapter {

    static final String NODE_CONTEXT_PLUGIN_NAME = "plugins/map/MapDialog_RemoveMapToNode.properties";

    public void invoke(MindMapNode pNode) {
        super.invoke(pNode);
        List selecteds = getMindMapController().getSelecteds();
        for (Iterator it = selecteds.iterator(); it.hasNext(); ) {
            MindMapNode node = (MindMapNode) it.next();
            MapNodePositionHolder hook = MapNodePositionHolder.getHook(node);
            if (hook != null) {
                List nodeSelected = Arrays.asList(new MindMapNode[] { node });
                getMindMapController().addHook(node, nodeSelected, MapNodePositionHolder.NODE_MAP_HOOK_NAME);
            }
        }
    }
}
