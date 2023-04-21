package org.fest.swing.test.builder;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.edt.GuiQuery;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.util.Arrays.isEmpty;

/**
 * Understands creation of <code>{@link JComboBox}</code>s.
 *
 * @author Alex Ruiz
 */
public final class JComboBoxes {

    private JComboBoxes() {
    }

    public static JComboBoxFactory comboBox() {
        return new JComboBoxFactory();
    }

    public static class JComboBoxFactory {

        boolean editable;

        Object items[];

        String name;

        int selectedIndex = -1;

        public JComboBoxFactory editable(boolean isEditable) {
            editable = isEditable;
            return this;
        }

        public JComboBoxFactory withItems(Object... newItems) {
            items = newItems;
            return this;
        }

        public JComboBoxFactory withName(String newName) {
            name = newName;
            return this;
        }

        public JComboBoxFactory withSelectedIndex(int newSelectedIndex) {
            selectedIndex = newSelectedIndex;
            return this;
        }

        @RunsInEDT
        public JComboBox createNew() {
            return execute(new GuiQuery<JComboBox>() {

                protected JComboBox executeInEDT() {
                    JComboBox comboBox = new JComboBox();
                    comboBox.setEditable(editable);
                    if (!isEmpty(items)) comboBox.setModel(new DefaultComboBoxModel(items));
                    comboBox.setName(name);
                    comboBox.setSelectedIndex(selectedIndex);
                    return comboBox;
                }
            });
        }
    }
}
