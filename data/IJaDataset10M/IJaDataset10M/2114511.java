package net.sourceforge.filebot.ui.sfv;

import static java.lang.Math.*;
import static net.sourceforge.filebot.ui.sfv.ChecksumTableModel.*;
import static net.sourceforge.filebot.ui.transfer.BackgroundFileTransferablePolicy.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import net.sourceforge.filebot.ResourceManager;
import net.sourceforge.filebot.hash.HashType;
import net.sourceforge.filebot.ui.SelectDialog;
import net.sourceforge.filebot.ui.transfer.DefaultTransferHandler;
import net.sourceforge.filebot.ui.transfer.LoadAction;
import net.sourceforge.filebot.ui.transfer.SaveAction;
import net.sourceforge.tuned.FileUtilities;
import net.sourceforge.tuned.ui.TunedUtilities;

public class SfvPanel extends JComponent {

    private final ChecksumComputationService computationService = new ChecksumComputationService();

    private final ChecksumTable table = new ChecksumTable();

    private final ChecksumTableTransferablePolicy transferablePolicy = new ChecksumTableTransferablePolicy(table.getModel(), computationService);

    private final ChecksumTableExportHandler exportHandler = new ChecksumTableExportHandler(table.getModel());

    public SfvPanel() {
        table.setTransferHandler(new DefaultTransferHandler(transferablePolicy, exportHandler));
        JPanel contentPane = new JPanel(new MigLayout("insets 0, nogrid, fill", "", "[fill]10px[bottom, pref!]4px"));
        contentPane.setBorder(new TitledBorder("SFV"));
        setLayout(new MigLayout("insets dialog, fill"));
        add(contentPane, "grow");
        contentPane.add(new JScrollPane(table), "grow, wrap");
        contentPane.add(new JButton(loadAction), "gap left 15px");
        contentPane.add(new JButton(saveAction));
        contentPane.add(new JButton(clearAction), "gap right 40px");
        ButtonGroup group = new ButtonGroup();
        for (HashType hash : HashType.values()) {
            JToggleButton button = new ChecksumButton(new ChangeHashTypeAction(hash));
            group.add(button);
            contentPane.add(button);
        }
        contentPane.add(new TotalProgressPanel(computationService), "gap left 35px:push, gap right 7px, hidemode 1");
        table.getModel().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (HASH_TYPE_PROPERTY.equals(evt.getPropertyName())) {
                    restartComputation((HashType) evt.getNewValue());
                }
            }
        });
        putClientProperty("transferablePolicy", transferablePolicy);
        TunedUtilities.installAction(this, KeyStroke.getKeyStroke("DELETE"), removeAction);
    }

    protected void restartComputation(HashType hash) {
        computationService.reset();
        ChecksumTableModel model = table.getModel();
        Map<File, ExecutorService> executors = new HashMap<File, ExecutorService>(4);
        for (ChecksumRow row : model.rows()) {
            for (ChecksumCell cell : row.values()) {
                if (cell.getChecksum(hash) == null && cell.getRoot().isDirectory()) {
                    cell.putTask(new ChecksumComputationTask(new File(cell.getRoot(), cell.getName()), hash));
                    ExecutorService executor = executors.get(cell.getRoot());
                    if (executor == null) {
                        executor = computationService.newExecutor();
                        executors.put(cell.getRoot(), executor);
                    }
                    executor.execute(cell.getTask());
                }
            }
        }
        for (ExecutorService executor : executors.values()) {
            executor.shutdown();
        }
    }

    private final SaveAction saveAction = new ChecksumTableSaveAction();

    private final LoadAction loadAction = new LoadAction(transferablePolicy);

    private final AbstractAction clearAction = new AbstractAction("Clear", ResourceManager.getIcon("action.clear")) {

        public void actionPerformed(ActionEvent e) {
            transferablePolicy.reset();
            computationService.reset();
            table.getModel().clear();
        }
    };

    private final AbstractAction removeAction = new AbstractAction("Remove") {

        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRowCount() < 1) return;
            int[] rows = table.getSelectedRows();
            if (rows.length <= 0) {
                return;
            }
            int selectedRow = table.getSelectedRow();
            for (int i = 0; i < rows.length; i++) {
                rows[i] = table.getRowSorter().convertRowIndexToModel(rows[i]);
            }
            table.getModel().remove(rows);
            computationService.purge();
            selectedRow = min(selectedRow, table.getRowCount() - 1);
            table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
        }
    };

    protected class ChangeHashTypeAction extends AbstractAction implements PropertyChangeListener {

        private ChangeHashTypeAction(HashType hash) {
            super(hash.toString());
            putValue(HASH_TYPE_PROPERTY, hash);
            propertyChange(new PropertyChangeEvent(this, HASH_TYPE_PROPERTY, null, table.getModel().getHashType()));
            transferablePolicy.addPropertyChangeListener(this);
            table.getModel().addPropertyChangeListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            table.getModel().setHashType((HashType) getValue(HASH_TYPE_PROPERTY));
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (LOADING_PROPERTY.equals(evt.getPropertyName())) {
                setEnabled(!(Boolean) evt.getNewValue());
            } else if (HASH_TYPE_PROPERTY.equals(evt.getPropertyName())) {
                putValue(SELECTED_KEY, evt.getNewValue() == getValue(HASH_TYPE_PROPERTY));
            }
        }
    }

    protected class ChecksumTableSaveAction extends SaveAction {

        private File selectedColumn = null;

        public ChecksumTableSaveAction() {
            super(exportHandler);
        }

        @Override
        public ChecksumTableExportHandler getExportHandler() {
            return (ChecksumTableExportHandler) super.getExportHandler();
        }

        @Override
        protected boolean canExport() {
            return selectedColumn != null && super.canExport();
        }

        @Override
        protected void export(File file) throws IOException {
            getExportHandler().export(file, selectedColumn);
        }

        @Override
        protected String getDefaultFileName() {
            return getExportHandler().getDefaultFileName(selectedColumn);
        }

        @Override
        protected File getDefaultFolder() {
            return selectedColumn;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<File> options = new ArrayList<File>();
            for (File file : table.getModel().getChecksumColumns()) {
                if (file.isDirectory()) options.add(file);
            }
            if (options.isEmpty()) {
                return;
            }
            try {
                if (options.size() == 1) {
                    this.selectedColumn = options.get(0);
                } else if (options.size() > 1) {
                    SelectDialog<File> selectDialog = new SelectDialog<File>(SwingUtilities.getWindowAncestor(SfvPanel.this), options) {

                        @Override
                        protected String convertValueToString(Object value) {
                            return FileUtilities.getFolderName((File) value);
                        }
                    };
                    selectDialog.getHeaderLabel().setText("Select checksum column:");
                    selectDialog.setLocationRelativeTo(SfvPanel.this);
                    selectDialog.setVisible(true);
                    this.selectedColumn = selectDialog.getSelectedValue();
                }
                if (this.selectedColumn != null) {
                    super.actionPerformed(e);
                }
            } finally {
                this.selectedColumn = null;
            }
        }
    }
}
