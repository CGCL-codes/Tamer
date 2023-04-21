package jaco.swing;

import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 * {@link JComboBoxWithCustomEditorComponent}
 * 
 * @version 1.0.0, October 25, 2011
 * @author Cristian Sulea ( http://cristiansulea.entrust.ro )
 */
public class JComboBoxWithCustomEditorComponent extends JComboBox {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public JComboBoxWithCustomEditorComponent(JTextField editorComponent) {
        super();
        init(editorComponent);
    }

    public JComboBoxWithCustomEditorComponent(JTextField editorComponent, Object[] items) {
        super(items);
        init(editorComponent);
    }

    public JComboBoxWithCustomEditorComponent(JTextField editorComponent, Vector<?> items) {
        super(items);
        init(editorComponent);
    }

    public JComboBoxWithCustomEditorComponent(JTextField editorComponent, ComboBoxModel aModel) {
        super(aModel);
        init(editorComponent);
    }

    private void init(final JTextField editorComponent) {
        setEditor(new BasicComboBoxEditor() {

            protected JTextField createEditorComponent() {
                return editorComponent;
            }
        });
    }
}
