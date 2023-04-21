package com.kenstevens.stratdom.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kenstevens.stratdom.model.Data;
import com.kenstevens.stratdom.model.stats.History;

@Component
public class StatsWindow {

    private Table opponentsTable;

    private Table detailsTable;

    private Combo combo;

    private Table unitTable;

    private Shell dialog;

    @Autowired
    Data db;

    @Autowired
    History history;

    /**
	 * Open the window
	 */
    public void open(Shell shell) {
        dialog = new Shell(shell);
        dialog.setLayout(new FillLayout());
        createContents(shell);
        setComboListeners();
        dialog.open();
    }

    private void setComboListeners() {
        combo.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                selectOpponent();
            }
        });
        combo.addListener(SWT.DefaultSelection, new Listener() {

            public void handleEvent(Event e) {
                selectOpponent();
            }
        });
    }

    private void createContents(Shell shell) {
        dialog.setSize(526, 506);
        dialog.setText("Stats");
        final TabFolder tabFolder = new TabFolder(dialog, SWT.NONE);
        final TabItem opponentsTabItem_1 = new TabItem(tabFolder, SWT.NONE);
        opponentsTabItem_1.setText("Opponents");
        opponentsTable = new Table(tabFolder, SWT.BORDER);
        opponentsTable.setLinesVisible(true);
        opponentsTable.setHeaderVisible(true);
        opponentsTabItem_1.setControl(opponentsTable);
        final TableColumn newColumnTableColumn_10 = new TableColumn(opponentsTable, SWT.NONE);
        newColumnTableColumn_10.setWidth(100);
        newColumnTableColumn_10.setText("Opponent");
        final TableColumn newColumnTableColumn_11 = new TableColumn(opponentsTable, SWT.NONE);
        newColumnTableColumn_11.setToolTipText("Total hours to build units lost");
        newColumnTableColumn_11.setWidth(100);
        newColumnTableColumn_11.setText("I lost");
        final TableColumn newColumnTableColumn_12 = new TableColumn(opponentsTable, SWT.NONE);
        newColumnTableColumn_12.setToolTipText("Total hours to build units killed");
        newColumnTableColumn_12.setWidth(100);
        newColumnTableColumn_12.setText("I killed");
        final TableColumn newColumnTableColumn_13 = new TableColumn(opponentsTable, SWT.NONE);
        newColumnTableColumn_13.setWidth(100);
        newColumnTableColumn_13.setText("Net");
        final TableColumn newColumnTableColumn_14 = new TableColumn(opponentsTable, SWT.NONE);
        newColumnTableColumn_14.setToolTipText("net / (I lost + I killed)");
        newColumnTableColumn_14.setWidth(100);
        newColumnTableColumn_14.setText("Dominance");
        final TabItem detailsTabItem = new TabItem(tabFolder, SWT.NONE);
        detailsTabItem.setText("Details");
        final Composite opponentComposite = new Composite(tabFolder, SWT.NONE);
        opponentComposite.setLayout(new FormLayout());
        detailsTabItem.setControl(opponentComposite);
        combo = new Combo(opponentComposite, SWT.NONE);
        final FormData fd_combo = new FormData();
        fd_combo.top = new FormAttachment(0, 10);
        fd_combo.right = new FormAttachment(0, 250);
        fd_combo.left = new FormAttachment(0, 90);
        combo.setLayoutData(fd_combo);
        Label opponentLabel;
        opponentLabel = new Label(opponentComposite, SWT.NONE);
        final FormData fd_opponentLabel = new FormData();
        fd_opponentLabel.top = new FormAttachment(combo, 0, SWT.TOP);
        fd_opponentLabel.left = new FormAttachment(0, 10);
        opponentLabel.setLayoutData(fd_opponentLabel);
        opponentLabel.setText("Opponent:");
        detailsTable = new Table(opponentComposite, SWT.BORDER);
        final FormData fd_table = new FormData();
        fd_table.left = new FormAttachment(0, 5);
        fd_table.bottom = new FormAttachment(100, -5);
        fd_table.right = new FormAttachment(100, -5);
        fd_table.top = new FormAttachment(0, 55);
        detailsTable.setLayoutData(fd_table);
        detailsTable.setLinesVisible(true);
        detailsTable.setHeaderVisible(true);
        final TableColumn newColumnTableColumn_5 = new TableColumn(detailsTable, SWT.NONE);
        newColumnTableColumn_5.setWidth(138);
        newColumnTableColumn_5.setText("Unit");
        final TableColumn newColumnTableColumn_6 = new TableColumn(detailsTable, SWT.NONE);
        newColumnTableColumn_6.setWidth(82);
        newColumnTableColumn_6.setText("I Lost");
        final TableColumn newColumnTableColumn_7 = new TableColumn(detailsTable, SWT.NONE);
        newColumnTableColumn_7.setWidth(90);
        newColumnTableColumn_7.setText("I Killed");
        final TableColumn newColumnTableColumn_8 = new TableColumn(detailsTable, SWT.NONE);
        newColumnTableColumn_8.setWidth(100);
        newColumnTableColumn_8.setText("Net");
        final TableColumn newColumnTableColumn_9 = new TableColumn(detailsTable, SWT.NONE);
        newColumnTableColumn_9.setToolTipText("Total cost in hours to make the units");
        newColumnTableColumn_9.setWidth(100);
        newColumnTableColumn_9.setText("Net Cost");
        final TabItem unitsTabItem = new TabItem(tabFolder, SWT.NONE);
        unitsTabItem.setText("Units");
        final Composite unitComposite = new Composite(tabFolder, SWT.NONE);
        unitComposite.setLayout(new FillLayout());
        unitsTabItem.setControl(unitComposite);
        unitTable = new Table(unitComposite, SWT.BORDER);
        unitTable.setLinesVisible(true);
        unitTable.setHeaderVisible(true);
        final TableColumn newColumnTableColumn = new TableColumn(unitTable, SWT.NONE);
        newColumnTableColumn.setWidth(100);
        newColumnTableColumn.setText("Unit");
        final TableColumn newColumnTableColumn_1 = new TableColumn(unitTable, SWT.NONE);
        newColumnTableColumn_1.setWidth(100);
        newColumnTableColumn_1.setText("Built");
        final TableColumn newColumnTableColumn_2 = new TableColumn(unitTable, SWT.NONE);
        newColumnTableColumn_2.setWidth(100);
        newColumnTableColumn_2.setText("Lost");
        final TableColumn newColumnTableColumn_3 = new TableColumn(unitTable, SWT.NONE);
        newColumnTableColumn_3.setWidth(100);
        newColumnTableColumn_3.setText("Standing");
        final TableColumn newColumnTableColumn_4 = new TableColumn(unitTable, SWT.NONE);
        newColumnTableColumn_4.setWidth(100);
        newColumnTableColumn_4.setText("Preservation");
    }

    public void setContents() {
        for (String[] unitRow : history.getUnitRecordList().getUnitRecordStats(db.getSelectedGameId())) {
            TableItem item = new TableItem(unitTable, SWT.NONE);
            item.setText(unitRow);
        }
        for (String[] opponentRow : history.getOpponentStats(db.getSelectedGameId())) {
            TableItem item = new TableItem(opponentsTable, SWT.NONE);
            item.setText(opponentRow);
        }
        for (String opponent : history.getOpponents(db.getSelectedGameId())) {
            combo.add(opponent);
        }
    }

    private void selectOpponent() {
        String opponent = combo.getText();
        if (opponent == null) {
            return;
        }
        detailsTable.removeAll();
        for (String[] opponentRow : history.getOpponentStats(db.getSelectedGameId(), opponent)) {
            TableItem item = new TableItem(detailsTable, SWT.NONE);
            item.setText(opponentRow);
        }
    }
}
