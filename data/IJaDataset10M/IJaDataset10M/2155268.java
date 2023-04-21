package net.cattaka.swing;

import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class StdComboBox extends JComboBox {

    private static final long serialVersionUID = 1L;

    public StdComboBox() {
        super();
        this.initialize();
    }

    public StdComboBox(ComboBoxModel model) {
        super(model);
        this.initialize();
    }

    public StdComboBox(Object[] items) {
        super(items);
        this.initialize();
    }

    public StdComboBox(Vector<?> items) {
        super(items);
        this.initialize();
    }

    private void initialize() {
        this.setEditable(true);
        this.setEditor(new StdComboBoxEditor());
    }

    public void addKeyListener(KeyListener l) {
        if (l == null) {
            return;
        }
        this.getEditor().getEditorComponent().addKeyListener(l);
    }
}
