package mipt.aaf.edit.swing.form;

import java.awt.Component;
import javax.swing.JLabel;
import mipt.aaf.edit.form.FieldEditorStub;
import mipt.gui.ComponentOwner;
import mipt.gui.Viewable;
import mipt.gui.choice.DefaultCellRenderer;

/**
 * Not edits but shows objects (by showing their toString() in JLabel)
 * @author Evdokimov
 */
public class LabelFieldEditorStub extends FieldEditorStub implements ComponentOwner {

    private JLabel label;

    /**
	 * 
	 */
    public LabelFieldEditorStub() {
    }

    /**
	 * @param label
	 */
    public LabelFieldEditorStub(JLabel label) {
        setLabel(label);
    }

    /**
	 * @return label
	 */
    public final JLabel getLabel() {
        return label;
    }

    /**
	 * @param label
	 */
    public void setLabel(JLabel label) {
        this.label = label;
    }

    /**
	 * @see mipt.aaf.edit.form.FieldEditor#setValue(java.lang.Object)
	 */
    public void setValue(Object value) {
        value = TextFieldEditor.correctValue(value);
        DefaultCellRenderer.showValue(getLabel(), true, value, Viewable.ICON);
    }

    /**
	 * @see mipt.gui.ComponentOwner#getComponent()
	 */
    public final Component getComponent() {
        return getLabel();
    }
}
