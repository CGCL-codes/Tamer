package test.editor;

import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mimosa.util.ObjectChangeEvent;
import mimosa.util.ObjectEditor;
import mimosa.util.Storage;

/**
 *
 * @author Jean-Pierre Muller
 */
@SuppressWarnings("serial")
public class DefaultMNodeEditor extends ObjectEditor {

    private DefaultMNode node;

    private JTextField nameField;

    /**
	 * The empty constructor.
	 */
    public DefaultMNodeEditor() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel namePane = new JPanel(new GridLayout(0, 2));
        namePane.add(new JLabel("Name"));
        namePane.add(nameField = new JTextField(10));
        add(namePane);
    }

    /**
	 * @see mimosa.util.ObjectEditor#setModel(mimosa.util.Storage)
	 */
    public void setModel(Storage object) {
        this.node = (DefaultMNode) object.retrieve();
        nameField.setText(node.getName());
    }

    /**
	 * @see mimosa.util.ObjectEditor#validateAndStore()
	 */
    public boolean validateAndStore() {
        String value;
        value = nameField.getText();
        if (!value.equals("")) node.setName(value); else return false;
        return true;
    }

    /**
	 * @see mimosa.util.ObjectChangeListener#objectChanged(mimosa.util.ObjectChangeEvent)
	 */
    public void objectChanged(ObjectChangeEvent evt) {
        nameField.setText((String) evt.getNewValue());
    }
}
