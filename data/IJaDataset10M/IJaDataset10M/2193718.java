package org.identifylife.key.editor.gwt.client.gui.tree;

import org.identifylife.key.editor.gwt.client.EditorManager;
import org.identifylife.key.editor.gwt.shared.model.Feature;
import org.identifylife.key.editor.gwt.shared.model.State;
import com.google.gwt.core.client.GWT;

/**
 * @author dbarnier
 *
 */
public class FeatureTreeNode extends AbstractTreeNode {

    private static final String STATE_ICON = GWT.getModuleBaseURL() + "images/State_16.png";

    public FeatureTreeNode(Feature feature) {
        super(feature.getUuid(), feature);
        if (EditorManager.getInstance().getDebug()) {
            setName(feature.getLabel() + " [uuid=" + feature.getUuid() + "]");
        } else {
            setName(feature.getLabel());
        }
    }

    public FeatureTreeNode(State state) {
        super(state.getUuid(), state);
        if (EditorManager.getInstance().getDebug()) {
            setName(state.getLabel() + " [uuid=" + state.getUuid() + "]");
        } else {
            setName(state.getLabel());
        }
        setIcon(STATE_ICON);
    }

    public String toString() {
        return "FeatureTreeNode[item=" + getItem() + "]";
    }
}
