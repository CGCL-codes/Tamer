package artem.finance.gui.autocomplete;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

/**
 * An implementation of the AbstractComponentAdaptor that is suitable for JComboBox.
 * @author Thomas Bierhance
 */
public class ComboBoxAdaptor extends AbstractComponentAdaptor implements ActionListener {

    /** the combobox being adapted */
    private JComboBox comboBox;

    /**
     * Creates a new ComobBoxAdaptor for the given combobox.
     * @param comboBox the combobox that should be adapted
     */
    public ComboBoxAdaptor(JComboBox comboBox) {
        this.comboBox = comboBox;
        comboBox.addActionListener(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        markEntireText();
    }

    public int getItemCount() {
        return comboBox.getItemCount();
    }

    public Object getItem(int index) {
        return comboBox.getItemAt(index);
    }

    public void setSelectedItem(Object item) {
        comboBox.setSelectedItem(item);
    }

    public Object getSelectedItem() {
        return comboBox.getModel().getSelectedItem();
    }

    public JTextComponent getTextComponent() {
        return (JTextComponent) comboBox.getEditor().getEditorComponent();
    }
}
