package be.vds.jtbdive.core.view.panel;

import info.clearthought.layout.TableLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXButton;
import be.vds.jtbdive.core.model.LogBookApplicationFacade;
import be.vds.jtbdive.core.model.Palanquee;
import be.vds.jtbdive.core.model.PalanqueeEntry;
import be.vds.jtbdive.core.view.component.TranslatableJXTable;
import be.vds.jtbdive.core.view.panel.listenable.ModificationDiverSelectListenableJPanel;
import be.vds.jtbdive.core.view.renderer.DiverRoleCellRenderer;
import be.vds.jtbdive.core.view.tablemodels.PalanqueeTableModel;

public class PalanqueeTablePanel extends ModificationDiverSelectListenableJPanel {

    private static final Font font = new Font("Courier", Font.PLAIN, 10);

    private static final Dimension dim = new Dimension(40, 15);

    private PalanqueeTableModel palanqueeTableModel;

    private TranslatableJXTable palanqueeTable;

    private JXButton removeDiverButton;

    private JXButton editDiverButton;

    private LogBookApplicationFacade logBookApplicationFacade;

    private Window parentWindow;

    public PalanqueeTablePanel(Window parentWindow, LogBookApplicationFacade logBookApplicationFacade) {
        this.parentWindow = parentWindow;
        this.logBookApplicationFacade = logBookApplicationFacade;
        init();
    }

    private void init() {
        double[] cols = { TableLayout.FILL, TableLayout.PREFERRED, 2, TableLayout.PREFERRED, 2, TableLayout.PREFERRED };
        double[] rows = { TableLayout.PREFERRED, 5, TableLayout.PREFERRED };
        TableLayout tl = new TableLayout(cols, rows);
        this.setLayout(tl);
        palanqueeTableModel = new PalanqueeTableModel(PalanqueeTableModel.HEADERS_1);
        palanqueeTable = new TranslatableJXTable(palanqueeTableModel);
        palanqueeTable.setColumnControlVisible(true);
        palanqueeTable.getColumnExt(PalanqueeTableModel.ROLES).setCellRenderer(new DiverRoleCellRenderer());
        palanqueeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean enabled = palanqueeTable.getSelectedRow() > -1;
                    removeDiverButton.setEnabled(enabled);
                    enabled = palanqueeTable.getSelectedRowCount() == 1;
                    editDiverButton.setEnabled(enabled);
                    if (palanqueeTable.getSelectedRow() > -1) {
                        int row = palanqueeTable.convertRowIndexToModel(palanqueeTable.getSelectedRow());
                        notifyDiverSelectionListeners(palanqueeTableModel.getPalanqueeEntryAt(row).getDiver());
                    }
                }
            }
        });
        JScrollPane scroll = new JScrollPane(palanqueeTable);
        scroll.setPreferredSize(new Dimension(150, 100));
        JXButton addDiverButton = createAddDiverButton();
        createRemoveDiverButton();
        createEditDiverButton();
        this.add(scroll, "0, 0, 5, 0");
        this.add(addDiverButton, "5, 2");
        this.add(editDiverButton, "3, 2");
        this.add(removeDiverButton, "1, 2");
    }

    private void createEditDiverButton() {
        editDiverButton = new JXButton(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PalanqueeSelectionPanel dsp = new PalanqueeSelectionPanel(parentWindow, logBookApplicationFacade, PalanqueeSelectionPanel.MODE_EDIT);
                int realRow = palanqueeTable.convertRowIndexToModel(palanqueeTable.getSelectedRow());
                PalanqueeEntry pe = palanqueeTableModel.getPalanqueeEntryAt(realRow);
                dsp.setPalanqueeEntry(pe);
                int i = dsp.showDialog("Diver Selection");
                if (i == PalanqueeSelectionPanel.OPTION_UPDATE) {
                    palanqueeTableModel.removePalanqueeEntry(pe);
                    palanqueeTableModel.addPalanqueeEntry(dsp.getPalanqueeEntry());
                    notifyModificationListeners(true);
                }
            }
        });
        editDiverButton.setText("%");
        editDiverButton.setPreferredSize(dim);
        editDiverButton.setFont(font);
        editDiverButton.setEnabled(false);
    }

    private void createRemoveDiverButton() {
        removeDiverButton = new JXButton(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int[] diversRows = palanqueeTable.getSelectedRows();
                List<PalanqueeEntry> list = new ArrayList<PalanqueeEntry>();
                for (int i = 0; i < diversRows.length; i++) {
                    int realRow = palanqueeTable.convertRowIndexToModel(diversRows[i]);
                    list.add(palanqueeTableModel.getPalanqueeEntryAt(realRow));
                }
                palanqueeTableModel.removePalanqueeEntries(list);
                notifyModificationListeners(true);
            }
        });
        removeDiverButton.setText("-");
        removeDiverButton.setPreferredSize(dim);
        removeDiverButton.setFont(font);
        removeDiverButton.setEnabled(false);
    }

    private JXButton createAddDiverButton() {
        JXButton addDiverButton = new JXButton(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PalanqueeSelectionPanel dsp = new PalanqueeSelectionPanel(parentWindow, logBookApplicationFacade, PalanqueeSelectionPanel.MODE_NEW);
                int i = dsp.showDialog("Diver Selection");
                if (i == PalanqueeSelectionPanel.OPTION_SELECT) {
                    palanqueeTableModel.addPalanqueeEntry(dsp.getPalanqueeEntry());
                    notifyModificationListeners(true);
                }
            }
        });
        addDiverButton.setText("+");
        addDiverButton.setPreferredSize(dim);
        addDiverButton.setFont(font);
        return addDiverButton;
    }

    public void setPalanquee(Palanquee palanquee) {
        palanqueeTableModel.setPalanquee(palanquee);
    }

    public Palanquee getPalanquee() {
        return palanqueeTableModel.getPalanquee();
    }

    public void updateLocale() {
        palanqueeTable.rebuildColumnNames();
    }
}
