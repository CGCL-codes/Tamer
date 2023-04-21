package com.g2d.studio.scene.editor;

import java.awt.Component;
import java.util.Vector;
import javax.swing.JComboBox;
import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.Studio;
import com.g2d.studio.scene.entity.SceneNode;

public class SceneListCellEdit extends JComboBox implements PropertyCellEdit<String> {

    private static final long serialVersionUID = 1L;

    ObjectPropertyEdit panel;

    public SceneListCellEdit(Object scene_id) {
        super(new Vector<SceneNode>(Studio.getInstance().getSceneManager().getAllScenes()));
        try {
            if (scene_id != null) {
                SceneNode node = Studio.getInstance().getSceneManager().getSceneNode(scene_id + "");
                if (node != null) {
                    setSelectedItem(node);
                }
            }
        } catch (Exception err) {
        }
    }

    public Component getComponent(ObjectPropertyEdit panel) {
        this.panel = panel;
        return this;
    }

    public String getValue() {
        Object item = getSelectedItem();
        if (item instanceof SceneNode) {
            return ((SceneNode) item).getID();
        }
        return null;
    }
}
