package jsynoptic.plugins.java3d.panels;

import javax.media.j3d.SceneGraphObject;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jsynoptic.plugins.java3d.edit.PropertyEdit;

/**
 *
 */
public class CheckBox<T extends SceneGraphObject> extends JCheckBox implements PropertyEdit.UndoRedoListener, PropertiesPanel.Editor<T, Boolean> {

    PropertyEdit<T, Boolean> _editor;

    public CheckBox() {
        addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                _editor.setNewValue(isSelected());
            }
        });
    }

    @Override
    public void edit(PropertyEdit<T, Boolean> editor) {
        if (_editor != null) {
            _editor.removeListener(this);
        }
        _editor = editor;
        if (_editor != null) {
            _editor.addListener(this);
            setSelected(_editor.getPropertyValue());
        }
    }

    @Override
    public void undoRedoPerfomed(boolean isUndo) {
        setSelected(_editor.getPropertyValue());
    }
}
