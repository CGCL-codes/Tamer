package org.dyno.visual.swing.editors;

import java.util.List;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

/**
 * 
 * ComponentTreeInput
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class ComponentTreeInput {

    private VisualDesigner root;

    public ComponentTreeInput(VisualDesigner root) {
        assert root != null;
        this.root = root;
    }

    public VisualDesigner getDesigner() {
        return root;
    }

    public List<InvisibleAdapter> getInvisibles() {
        return root.getInvisibles();
    }

    public WidgetAdapter getRootAdapter() {
        return WidgetAdapter.getWidgetAdapter(root.getRoot());
    }
}
