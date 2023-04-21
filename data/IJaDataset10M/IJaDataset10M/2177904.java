package mimosa.table;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mimosa.util.ObjectCreationPane;
import mimosa.util.Pair;
import mimosa.util.Storage;

/**
 * This class implements a Dialog to enter a new field declaration with its name and type.
 * 
 * @author Jean-Pierre Muller
 *
 */
@SuppressWarnings("serial")
public class NewDeclarationDialog<Key, Type extends TypeDescription> extends JDialog implements ActionListener {

    private class KeyStore implements Storage {

        public Object retrieve() {
            return key;
        }

        @SuppressWarnings("unchecked")
        public void store(Object value) {
            key = (Key) value;
        }
    }

    private ObjectCreationPane keyField;

    private JTextField cardinalityField;

    private JComboBox classField;

    private JButton okButton;

    private JButton cancelButton;

    private Key key;

    private Integer cardinality;

    private Type description;

    public NewDeclarationDialog(ObjectCreationPane keyEditor, Collection<?> types, String keyTitle, String cardinalityTitle, String descriptionTitle, Integer fixedCardinality, Type fixedType) {
        super((Frame) null, true);
        Container content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(0, 2));
        keyField = keyEditor;
        keyField.setModel(new KeyStore());
        pane.add(new JLabel(keyTitle));
        pane.add(keyField);
        keyField.requestFocusInWindow();
        if (fixedCardinality == null) {
            cardinalityField = new JTextField(10);
            pane.add(new JLabel(cardinalityTitle));
            pane.add(cardinalityField);
        } else cardinality = fixedCardinality;
        if (fixedType == null) {
            if (types != null) {
                classField = new JComboBox(types.toArray());
                classField.setSelectedIndex(0);
            } else classField = new JComboBox();
            pane.add(new JLabel(descriptionTitle));
            pane.add(classField);
        } else description = fixedType;
        content.add(pane);
        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(okButton = new JButton(Messages.getString("NewDeclarationDialog.1")));
        pane.add(cancelButton = new JButton(Messages.getString("NewDeclarationDialog.2")));
        okButton.addActionListener(this);
        okButton.setDefaultCapable(true);
        getRootPane().setDefaultButton(okButton);
        cancelButton.addActionListener(this);
        content.add(pane);
    }

    /**
	 * Gets the declaration description.
	 * @return the new declaration
	 */
    @SuppressWarnings("unchecked")
    public Pair<Key, ValueDescription<Type>> getDeclaration() {
        if (key == null) return null; else return new Pair(key, new ValueDescription<Type>(cardinality.intValue(), description));
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            description = (Type) classField.getSelectedItem();
            if (keyField.validateAndStore() && description != null) {
                String card = cardinalityField.getText();
                if (card != null && !card.equals("")) {
                    if (card.equals("*")) {
                        cardinality = new Integer(-1);
                        setVisible(false);
                    } else {
                        cardinality = Integer.valueOf(card);
                        if (cardinality.intValue() < 1) JOptionPane.showMessageDialog(this, Messages.getString("NewDeclarationDialog.7")); else setVisible(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, Messages.getString("NewDeclarationDialog.8"));
                }
            } else JOptionPane.showMessageDialog(this, Messages.getString("NewDeclarationDialog.9"));
        } else if (e.getSource() == cancelButton) {
            key = null;
            cardinality = null;
            description = null;
            setVisible(false);
        }
    }

    /**
	 * This method displays a dialog to enter a new named object definition.
	 * @return the new object name or null if cancelled.
	 */
    public static Pair<?, ?> showNewDialog(NewDeclarationDialog<?, ? extends TypeDescription> newDialog) {
        newDialog.pack();
        newDialog.setVisible(true);
        newDialog.dispose();
        return newDialog.getDeclaration();
    }
}
