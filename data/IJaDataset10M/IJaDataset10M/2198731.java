package spamwatch.filter.address;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SortOrder;
import spamwatch.filter.Classification;
import spamwatch.gui.util.AbstractConfigView;
import spamwatch.gui.util.ClassificationEditor;

public class AddressFilterView extends AbstractConfigView {

    private AddressFilterModel model;

    private AddressFilter filter;

    public AddressFilterView(AddressFilter filter, AddressFilterModel model) {
        super(model);
        this.model = model;
        this.filter = filter;
        initGUI();
    }

    @Override
    public Action[] getButtonActions() {
        Action importAction = new AbstractAction("Import from Version 1.2") {

            public void actionPerformed(ActionEvent e) {
                final JFileChooser c = new JFileChooser(new File("."));
                int result = c.showOpenDialog(AddressFilterView.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    backgroundAction(new Runnable() {

                        public void run() {
                            try {
                                filter.loadFromLegacyFile(c.getSelectedFile());
                            } catch (Exception e1) {
                                JOptionPane.showMessageDialog(AddressFilterView.this, e1, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                }
            }
        };
        Action deleteAction = new AbstractAction("Delete") {

            public void actionPerformed(ActionEvent e) {
                backgroundAction(new Runnable() {

                    public void run() {
                        filter.deleteItems(model.getCheckedItems());
                    }
                });
            }
        };
        Action saveAction = new AbstractAction("Save Parameters") {

            public void actionPerformed(ActionEvent e) {
                backgroundAction(new Runnable() {

                    public void run() {
                        filter.saveData();
                    }
                });
            }
        };
        return new Action[] { importAction, deleteAction, saveAction };
    }

    @Override
    public void initTable(JTable table) {
        setDefaultSortColumn(1, SortOrder.ASCENDING);
        setCheckAllColumn(0, new Runnable() {

            public void run() {
                model.checkAll(!model.areAllRowsChecked());
            }
        });
        table.setDefaultEditor(Classification.class, new ClassificationEditor(true));
    }

    @Override
    public void freeData() {
        model.freeData();
    }

    @Override
    public void initData() {
        model.initData();
    }
}
