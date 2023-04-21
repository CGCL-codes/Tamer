package de.shandschuh.jaolt.tools.hashmap;

import java.awt.BorderLayout;
import java.util.HashMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import de.shandschuh.jaolt.gui.core.ButtonPanelFactory;
import de.shandschuh.jaolt.gui.core.RowHighlightedJTable;
import de.shandschuh.jaolt.gui.core.SaveableJDialog;

public class EditHashMapJDialog extends SaveableJDialog {

    private static final long serialVersionUID = 1L;

    private HashMapTableModel tableModel;

    private HashMap<String, String> hashMap;

    public EditHashMapJDialog(String title, HashMap<String, String> hashMap, JDialog parent) {
        super(parent, title, true);
        this.hashMap = hashMap;
        getContentPane().setLayout(new BorderLayout());
        tableModel = new HashMapTableModel(hashMap);
        RowHighlightedJTable table = new RowHighlightedJTable(tableModel);
        table.putClientProperty("terminateEditOnFocusLost", true);
        ((DefaultCellEditor) table.getDefaultEditor(String.class)).setClickCountToStart(1);
        getContentPane().add(new JScrollPane(table));
        getContentPane().add(ButtonPanelFactory.create(this), BorderLayout.SOUTH);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public void save() throws Exception {
        hashMap.putAll(tableModel.getClonedHashMap());
        dispose();
    }
}
