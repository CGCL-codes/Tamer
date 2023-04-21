package org.progeeks.meta.echo2.editor;

import org.progeeks.meta.echo2.AbstractPropertyEditor;
import org.progeeks.meta.PropertyMutator;
import org.progeeks.util.Inspector;
import org.progeeks.util.swing.FontChooser;
import nextapp.echo2.app.*;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.event.ActionEvent;

/**
 *  An editor implementation for java.awt.Font values.
 *
 *  @version   $Revision: 1.1 $
 *  @author    J. Dave Sheremata
 */
public class FontEditor extends AbstractPropertyEditor {

    private static Font defaultFont = new Font(Font.COURIER, 12, new Extent(Extent.PT, 12));

    private Button component;

    private FontEditor.EditorListener editListener = new FontEditor.EditorListener();

    public FontEditor() {
        component = new Button("        ");
        component.addActionListener(editListener);
    }

    /**
     *  Returns the component that allows modification of the
     *  associated property mutator.
     */
    public Component getUIComponent() {
        return (component);
    }

    /**
     *  Implemented by subclasses to release any component-related
     *  resources.
     */
    protected void releaseComponent() {
        component.removeActionListener(editListener);
    }

    /**
     *  Called to set the current value displayed in the component.
     */
    protected void setComponentValue(Object value) {
        Font f = (Font) value;
        if (f == null) f = defaultFont;
        component.setFont(f);
        String s = Inspector.encodeAsString(f);
        component.setText(s);
    }

    /**
     *  Called to set the component value to a default state.
     *  The default implementation calls setComponentValue(null).
     */
    protected void resetComponentValue() {
        component.setBackground(Color.BLACK);
    }

    private class EditorListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            PropertyMutator mutator = getPropertyMutator();
            String title = mutator.getPropertyInfo().getName();
            Font initial = component.getFont();
        }
    }
}
