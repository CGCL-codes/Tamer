package de.javagimmicks.swing;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class AutoComboBoxEditorTest {

    public static void main(String[] args) {
        String[] listData = new String[] { "Adam", "Alice", "Caronline", "Charles", "Edward", "Elisabeth", "Sam", "Sandy" };
        final JComboBox comboBox = new JComboBox(listData);
        AutoComboBoxEditor.install(comboBox, true);
        comboBox.getModel().addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e) {
                if (e.getIndex0() == -1 && e.getIndex1() == -1) {
                    System.out.println("Changed to '" + comboBox.getSelectedItem() + "'");
                }
            }

            public void intervalAdded(ListDataEvent e) {
            }

            public void intervalRemoved(ListDataEvent e) {
            }
        });
        JFrame window = new JFrame(AutoComboBoxEditorTest.class.getName());
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.getContentPane().add(comboBox);
        window.pack();
        window.setVisible(true);
    }
}
