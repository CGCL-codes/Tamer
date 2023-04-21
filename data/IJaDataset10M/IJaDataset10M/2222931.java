package de.haumacher.timecollect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import de.haumacher.timecollect.Ticket.LCState;
import de.haumacher.timecollect.common.util.Util;
import de.haumacher.timecollect.util.TableUtil;

public class ManageTicketsDialog extends DialogBase {

    private Table table;

    private Composite buttonBar;

    private Button cancelButton;

    private Button okButton;

    private Button addButton;

    private Button removeButton;

    protected Edit currentEdit;

    public Collection<Ticket> newTickets = new HashSet<Ticket>();

    public Collection<Ticket> changedTickets = new HashSet<Ticket>();

    public Collection<Ticket> removedTickets = new HashSet<Ticket>();

    private final int tracidIdx;

    private final int titleIdx;

    private final int typeIdx;

    private final int componentIdx;

    private final int mnemonicIdx;

    private final int stateIdx;

    public class Sorter extends SelectionAdapter {

        @Override
        public void widgetSelected(SelectionEvent e) {
            TableColumn sortColumn = (TableColumn) e.widget;
            Table table = sortColumn.getParent();
            boolean descending;
            if (table.getSortColumn() == sortColumn) {
                descending = table.getSortDirection() == SWT.UP;
            } else {
                descending = false;
            }
            Comparator comparator;
            if (descending) {
                comparator = TableUtil.getDescendingComparator(sortColumn);
            } else {
                comparator = TableUtil.getComparator(sortColumn);
            }
            if (comparator == null) {
                return;
            }
            saveCurrentEdit();
            int itemCount = table.getItemCount();
            ArrayList objects = new ArrayList(itemCount);
            for (int n = 0; n < itemCount; n++) {
                objects.add(table.getItem(n).getData());
            }
            Collections.sort(objects, comparator);
            table.removeAll();
            TableUtil.addRows(table, objects);
            table.setSortColumn(sortColumn);
            table.setSortDirection(descending ? SWT.DOWN : SWT.UP);
        }
    }

    class Edit {

        private final Ticket ticket;

        private TableEditor titleEdit;

        private TableEditor typeEdit;

        private TableEditor componentEdit;

        private TableEditor mnemonicEdit;

        public Edit(TableItem item, Ticket ticket) {
            this.ticket = ticket;
            if (isLocal()) {
                titleEdit = new TableEditor(table);
                Text text = new Text(table, SWT.NONE);
                text.setText(ticket.getSummary());
                titleEdit.grabHorizontal = true;
                titleEdit.grabVertical = true;
                titleEdit.setEditor(text, item, titleIdx);
                typeEdit = new TableEditor(table);
                Text type = new Text(table, SWT.NONE);
                type.setText(ticket.getType());
                typeEdit.grabHorizontal = true;
                typeEdit.grabVertical = true;
                typeEdit.setEditor(type, item, typeIdx);
                componentEdit = new TableEditor(table);
                Text component = new Text(table, SWT.NONE);
                component.setText(ticket.getComponent());
                componentEdit.grabHorizontal = true;
                componentEdit.grabVertical = true;
                componentEdit.setEditor(component, item, componentIdx);
            }
            mnemonicEdit = new TableEditor(table);
            Text mnemonic = new Text(table, SWT.NONE);
            mnemonic.setText(ticket.getMnemonic());
            mnemonicEdit.grabHorizontal = true;
            mnemonicEdit.grabVertical = true;
            mnemonicEdit.setEditor(mnemonic, item, mnemonicIdx);
        }

        private boolean isLocal() {
            return ticket.getRemoteId() == 0;
        }

        public void dispose() {
            if (isLocal()) {
                dispose(titleEdit);
                dispose(typeEdit);
                dispose(componentEdit);
            }
            dispose(mnemonicEdit);
        }

        private void dispose(TableEditor editor) {
            editor.getEditor().dispose();
            editor.dispose();
        }

        public void save() {
            boolean changed = false;
            if (isLocal()) {
                String newTitle = ((Text) titleEdit.getEditor()).getText();
                titleEdit.getItem().setText(titleIdx, newTitle);
                if (!Util.equals(ticket.getSummary(), newTitle)) {
                    ticket.setSummary(newTitle);
                    changed = true;
                }
                String newType = ((Text) typeEdit.getEditor()).getText();
                typeEdit.getItem().setText(typeIdx, newType);
                if (!Util.equals(ticket.getType(), newType)) {
                    ticket.setType(newType);
                    changed = true;
                }
                String newComponent = ((Text) componentEdit.getEditor()).getText();
                componentEdit.getItem().setText(componentIdx, newComponent);
                if (!Util.equals(ticket.getComponent(), newComponent)) {
                    ticket.setComponent(newComponent);
                    changed = true;
                }
            }
            String newMnemonic = ((Text) mnemonicEdit.getEditor()).getText();
            mnemonicEdit.getItem().setText(mnemonicIdx, newMnemonic);
            if (!Util.equals(ticket.getMnemonic(), newMnemonic)) {
                ticket.setMnemonic(newMnemonic);
                changed = true;
            }
            if (changed && !newTickets.contains(ticket)) {
                changedTickets.add(ticket);
            }
        }
    }

    public ManageTicketsDialog(Display display, DialogConfig dialogConfig) {
        super(display, dialogConfig);
        this.dialog.setText("Manage tickets");
        this.dialog.setLayout(new GridLayout(1, true));
        this.table = new Table(dialog, SWT.BORDER | SWT.FULL_SELECTION);
        this.table.setLinesVisible(true);
        this.table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.table.setHeaderVisible(true);
        int idx = 0;
        TableColumn tracidColumn = new TableColumn(table, SWT.RIGHT);
        tracidColumn.setText("Trac ID");
        tracidColumn.setData(TableUtil.GETTER_PROPERTY, TicketAccess.GET_REMOTE_ID);
        tracidColumn.setData(TableUtil.LABEL_PROVIDER_PROPERTY, TicketAccess.ID_LABEL);
        tracidIdx = idx++;
        TableColumn typeColumn = new TableColumn(table, SWT.LEFT);
        typeColumn.setText("Type");
        typeColumn.setData(TableUtil.GETTER_PROPERTY, TicketAccess.GET_TYPE);
        typeIdx = idx++;
        TableColumn componentColumn = new TableColumn(table, SWT.LEFT);
        componentColumn.setText("Component");
        componentColumn.setData(TableUtil.GETTER_PROPERTY, TicketAccess.GET_COMPONENT);
        componentIdx = idx++;
        TableColumn stateColumn = new TableColumn(table, SWT.LEFT);
        stateColumn.setText("State");
        stateColumn.setData(TableUtil.GETTER_PROPERTY, TicketAccess.GET_STATE);
        stateIdx = idx++;
        TableColumn mnemonicColumn = new TableColumn(table, SWT.LEFT);
        mnemonicColumn.setText("Mnemonic");
        mnemonicColumn.setData(TableUtil.GETTER_PROPERTY, TicketAccess.GET_MNEMONIC);
        mnemonicIdx = idx++;
        TableColumn titleColumn = new TableColumn(table, SWT.LEFT);
        titleColumn.setText("Title");
        titleColumn.setData(TableUtil.GETTER_PROPERTY, TicketAccess.GET_SUMMARY);
        titleIdx = idx++;
        addSorter(table);
        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                saveCurrentEdit();
                TableItem item = (TableItem) e.item;
                Ticket ticket = (Ticket) item.getData();
                currentEdit = new Edit(item, ticket);
            }
        });
        buttonBar = new Composite(dialog, SWT.NONE);
        buttonBar.setLayout(new FillLayout());
        buttonBar.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
        addButton = new Button(buttonBar, SWT.PUSH);
        addButton.setText("Add");
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Ticket ticket = new Ticket(0, 0, "", "New Ticket", "", "", "", LCState.SELECTED);
                TableUtil.addRows(table, Collections.singletonList(ticket));
                newTickets.add(ticket);
            }
        });
        removeButton = new Button(buttonBar, SWT.PUSH);
        removeButton.setText("Remove");
        removeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentEdit != null) {
                    currentEdit.dispose();
                    currentEdit = null;
                }
                int selectionIndex = table.getSelectionIndex();
                if (selectionIndex < 0) {
                    return;
                }
                TableItem item = table.getItem(selectionIndex);
                Ticket ticket = (Ticket) item.getData();
                if (!newTickets.remove(ticket)) {
                    removedTickets.add(ticket);
                }
                table.remove(selectionIndex);
                item.dispose();
            }
        });
        cancelButton = new Button(buttonBar, SWT.PUSH);
        cancelButton.setText("Cancel");
        cancelButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                dialog.close();
            }
        });
        okButton = new Button(buttonBar, SWT.PUSH);
        okButton.setText("OK");
        okButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                saveCurrentEdit();
                handleSave(newTickets, removedTickets, changedTickets);
            }
        });
    }

    private void addSorter(Table table) {
        SelectionListener columnSortListener = new Sorter();
        for (TableColumn column : table.getColumns()) {
            column.addSelectionListener(columnSortListener);
        }
    }

    protected void handleSave(Collection<Ticket> newTickets, Collection<Ticket> removedTickets, Collection<Ticket> changedTickets) {
        dialog.close();
    }

    public ManageTicketsDialog load(Collection<Ticket> tickets) {
        ArrayList<Ticket> ticketList = new ArrayList<Ticket>(tickets);
        Collections.sort(ticketList, new TicketComparator());
        table.removeAll();
        TableUtil.addRows(table, ticketList);
        for (TableColumn column : table.getColumns()) {
            column.pack();
        }
        return this;
    }

    private void saveCurrentEdit() {
        if (currentEdit != null) {
            currentEdit.save();
            currentEdit.dispose();
            currentEdit = null;
        }
    }
}
