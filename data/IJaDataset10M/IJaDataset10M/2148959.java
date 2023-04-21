package thoto.jamyda.gui;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;
import thoto.jamyda.data.AbstractData;
import thoto.jamyda.data.ItemData;

public class JamydaComponent {

    private AbstractData data;

    private JComponent component;

    private JComponent[] dataComponents;

    /**
	 * Creates a new instance of JamydaComponent
	 *
	 * @param data
	 * @param component
	 */
    public JamydaComponent(AbstractData data, JComponent component) {
        this.data = data;
        this.component = component;
    }

    /**
	 * Returns the current data
	 *
	 * @return the data
	 */
    public AbstractData getData() {
        return this.data;
    }

    /**
	 * Returns 
	 *
	 * @return the component
	 */
    public JComponent getComponent() {
        return component;
    }

    /**
	 * Saves the components data into the internal {@link AbstractData} object
	 * 
	 */
    public void saveData() {
        if (getDataComponents() != null && this.data instanceof ItemData) {
            ItemData item = (ItemData) this.data;
            String value = "";
            for (int a = 0, z = getDataComponents().length; a < z; a++) {
                JComponent c = getDataComponents()[a];
                if (c instanceof JTextComponent) {
                    value += ((JTextComponent) c).getText();
                } else if (c instanceof JSpinner) {
                    value += ((JSpinner) c).getValue().toString();
                } else if (c instanceof JCheckBox) {
                    value += Boolean.toString(((JCheckBox) c).isSelected());
                } else if (c instanceof JComboBox) {
                    Object selItem = ((JComboBox) c).getSelectedItem();
                    if (selItem != null) value += selItem.toString();
                }
                if (a + 1 < z && value.trim().length() > 0) value += ",";
            }
            item.setValue(value);
        }
    }

    /**
	 * Returns the components that holds the data
	 *
	 * @return the dataComponents
	 */
    public JComponent[] getDataComponents() {
        return dataComponents;
    }

    /**
	 * Sets the component that holds the data
	 *
	 * @param dataComponents the dataComponents to set
	 */
    public void setDataComponent(JComponent dataComponent) {
        this.dataComponents = new JComponent[] { dataComponent };
    }

    /**
	 * Sets the components that holds the data
	 *
	 * @param dataComponents the dataComponents to set
	 */
    public void setDataComponents(JComponent[] dataComponents) {
        this.dataComponents = dataComponents;
    }
}
