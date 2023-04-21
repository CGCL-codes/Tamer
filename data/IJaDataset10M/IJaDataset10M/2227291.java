package org.xito.dcf.property.editor;

import java.awt.*;
import java.beans.*;
import javax.swing.*;

public abstract class EditorSupport extends PropertyEditorSupport {

    public static final Dimension MEDIUM_DIMENSION = new Dimension(50, 30);

    public static final Dimension SMALL_DIMENSION = new Dimension(50, 30);

    /**
   * Component which holds the editor. Subclasses are responsible for
   * instantiating this panel.
   */
    protected JPanel panel;

    /**
   * Returns the panel responsible for rendering the PropertyEditor.
   */
    public Component getCustomEditor() {
        return panel;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    protected final void setAlignment(JComponent c) {
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    /**
   * For property editors that must be initialized with values from
   * the property descriptor.
   */
    public void init(PropertyDescriptor descriptor) {
    }

    /**
   * Set the Value of this Property
   * @param pValue new Value
   */
    public void setValue(Object pValue) {
        Object _oldValue = super.getValue();
        if (pValue == _oldValue) return;
        if (pValue != null && _oldValue != null && pValue.equals(_oldValue)) return;
        super.setValue(pValue);
    }
}
