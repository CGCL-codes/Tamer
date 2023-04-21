package mipt.aaf.edit.swing.form;

import java.awt.Container;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import mipt.aaf.edit.form.AbstractForm;
import mipt.aaf.edit.form.Field;
import mipt.aaf.edit.form.FieldEditor;
import mipt.aaf.edit.form.FieldModel;
import mipt.gui.choice.DefaultCellEditor;

/**
 * Form initializing field editors with swing component for typical field types.
 * Swing components are sent here (in Fields) by specific editor part.
 * @author Evdokimov
 */
public class SwingForm extends AbstractForm {

    private FieldComponentCreator componentCreator;

    /**
	 * 
	 */
    public SwingForm() {
    }

    /**
	 * @param model
	 */
    public SwingForm(FieldModel model) {
        super(model);
    }

    /**
	 * @param model
	 * @param fields
	 */
    public SwingForm(FieldModel model, Field[] fields) {
        super(model, fields);
    }

    /**
	 * @param model
	 * @param fieldEditors
	 */
    public SwingForm(FieldModel model, FieldEditor[] fieldEditors) {
        super(model, fieldEditors);
    }

    /**
	 * @param model
	 * @param fieldCount
	 */
    public SwingForm(FieldModel model, int fieldCount) {
        super(model, fieldCount);
    }

    /**
	 * @see mipt.aaf.edit.form.AbstractForm#initFieldEditor(mipt.aaf.edit.Field)
	 */
    public FieldEditor initFieldEditor(Field field) {
        Object real = field.getRealEditor();
        if (real == null) real = initRealEditor(field);
        FieldEditor editor = initFieldEditor(field.getType(), real);
        if (editor != null) return editor;
        throw new IllegalArgumentException("Field type (" + field.getType() + ") and field editor component (" + real.getClass() + ")are incompatible");
    }

    /**
	 * Default implementation of map "{type,realEditor} -> fieldEditor".
	 * Can be called outside from code that does not depend on FieldModel or other Form concepts.
	 * @param fieldType - one of Field's constants
	 * @param real - non-null real editor; as usual, it's java.awt.Component, but can also be CellEditor, FileChooser, etc.
	 */
    public static final FieldEditor initFieldEditor(int fieldType, Object real) {
        switch(fieldType) {
            case Field.BOOLEAN:
                if (real instanceof JCheckBox) return new CheckFieldEditor((JCheckBox) real);
            case Field.INT:
                if (real instanceof JComboBox) return new ComboIndexFieldEditor((JComboBox) real);
                if (real instanceof JPanel) return new RadioButtonFieldEditor((Container) real);
            case Field.DOUBLE:
            case Field.UNTYPED:
            case Field.SLASH:
                if (real instanceof JTextComponent) return new TextFieldEditor((JTextComponent) real);
                if (real instanceof JComboBox) return new ComboBoxFieldEditor((JComboBox) real);
                if (real instanceof JLabel) return new LabelFieldEditorStub((JLabel) real);
            default:
                if (real instanceof DefaultCellEditor) return new CellFieldEditor((DefaultCellEditor) real);
                break;
            case Field.PATH:
                if (real instanceof JPanel) return new PathFieldEditor((JPanel) real);
                if (real instanceof JFileChooser) return new PathFieldEditor((JFileChooser) real);
                return new PathFieldEditor();
            case Field.ARRAY:
                if (real instanceof JList) return new ListFieldEditor((JList) real);
                if (real instanceof JTable) return new TableFieldEditor((JTable) real);
                break;
        }
        return null;
    }

    /**
	 * @return componentCreator
	 */
    public final FieldComponentCreator getComponentCreator() {
        if (componentCreator == null) componentCreator = initComponentCreator();
        return componentCreator;
    }

    /**
	 * Factory method
	 */
    protected FieldComponentCreator initComponentCreator() {
        return new FieldComponentCreator();
    }

    /**
	 * Call this method if default dependence of field components on field type is not valid.
	 */
    public void setComponentCreator(FieldComponentCreator componentCreator) {
        this.componentCreator = componentCreator;
    }

    /**
	 * @see FieldComponentCreator
	 */
    protected Object initRealEditor(Field field) {
        return getComponentCreator().initEditorComponent(field.getName(), field.getType());
    }
}
