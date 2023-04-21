package simtools.shapes.undo;

import javax.swing.undo.AbstractUndoableEdit;
import simtools.shapes.AbstractShape;

/**
 * Instances of this class should be cast as events when a shape's property is modified
 * @author Jean-Baptiste Li&egrave;vremont
 *
 */
public class PropertyChangeEdit extends AbstractUndoableEdit {

    private AbstractShape shape;

    private String name;

    private Object oldValue;

    private Object newValue;

    /**
	 * 
	 * @param _shape The modified shape
	 * @param _name The name of the modified property
	 * @param _oldValue The old value of the property
	 * @param _newValue The new value of the property
	 */
    public PropertyChangeEdit(AbstractShape _shape, String _name, Object _oldValue, Object _newValue) {
        shape = _shape;
        name = _name;
        oldValue = _oldValue;
        newValue = _newValue;
    }

    /**
	 * @see AbstractUndoableEdit#undo()
	 */
    public void undo() {
        super.undo();
        shape.setPropertyValue(name, oldValue);
    }

    /**
	 * @see AbstractUndoableEdit#redo()
	 */
    public void redo() {
        super.redo();
        shape.setPropertyValue(name, newValue);
    }

    public String getPresentationName() {
        return "property: " + name;
    }
}
