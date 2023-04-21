package vavi.swing.beaninfo;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * An editor which represents a boolean value. This editor is implemented
 * as a checkbox with the text of the check box reflecting the state of the
 * checkbox.
 *
 * @author	Mark Davidson
 * @version	1.10	990923		original version <br>
 */
public class SwingBooleanEditor extends SwingEditorSupport {

    private JCheckBox checkbox;

    /** */
    public SwingBooleanEditor() {
        checkbox = new JCheckBox();
        checkbox.addItemListener(il);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(checkbox);
    }

    /** */
    private ItemListener il = new ItemListener() {

        public void itemStateChanged(ItemEvent ev) {
            if (ev.getStateChange() == ItemEvent.SELECTED) {
                setValue(Boolean.TRUE);
            } else {
                setValue(Boolean.FALSE);
            }
        }
    };

    /** */
    public void setValue(Object value) {
        super.setValue(value);
        if (value != null) {
            try {
                checkbox.setText(value.toString());
                if (checkbox.isSelected() != ((Boolean) value).booleanValue()) {
                    checkbox.setSelected(((Boolean) value).booleanValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** */
    public Object getValue() {
        return new Boolean(checkbox.isSelected());
    }
}
