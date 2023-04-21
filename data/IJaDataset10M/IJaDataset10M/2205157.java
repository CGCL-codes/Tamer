package net.sourceforge.scrollrack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;

public class DeckTable implements MouseListener {

    private CardBase cardbase;

    private Gallery gallery;

    private Table table;

    private boolean packable;

    public boolean changed;

    public DeckTable(Composite parent, CardBase cardbase, Gallery gallery, int width) {
        TableColumn column;
        this.cardbase = cardbase;
        this.gallery = gallery;
        table = new Table(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
        column = new TableColumn(table, SWT.LEFT);
        column.setWidth(18);
        column.setResizable(false);
        column = new TableColumn(table, SWT.LEFT);
        packable = (width <= 0);
        if (!packable) column.setWidth(190);
        column.setResizable(true);
        table.setHeaderVisible(false);
        table.setLinesVisible(true);
        table.addMouseListener(this);
    }

    public void set_cardlist(IntList cardlist) {
        int size, iii, globalid, count;
        TableItem item;
        CardInfo info;
        String name;
        int[] data;
        table.setVisible(false);
        table.removeAll();
        size = (cardlist == null ? 0 : cardlist.size());
        iii = 0;
        while (iii < size) {
            globalid = cardlist.get(iii);
            count = 1;
            while ((iii + count < size) && (cardlist.get(iii + count) == globalid)) count++;
            item = new TableItem(table, 0);
            item.setText(0, "" + count);
            info = cardbase.get(globalid);
            name = ((info == null || info.name == null) ? "" : info.name);
            item.setText(1, name);
            data = new int[] { count, globalid };
            item.setData(data);
            iii += count;
        }
        if (packable) table.getColumn(1).pack();
        table.setVisible(true);
        this.changed = false;
    }

    public Table getControl() {
        return table;
    }

    public int find_globalid(int globalid) {
        int size, idx;
        TableItem item;
        int[] data;
        size = table.getItemCount();
        for (idx = 0; idx < size; idx++) {
            item = table.getItem(idx);
            data = (int[]) item.getData();
            if (data[1] == globalid) return (idx);
        }
        return (-1);
    }

    public void add(int globalid, boolean select) {
        int idx;
        TableItem item;
        int[] data;
        CardInfo info;
        String name;
        this.changed = true;
        idx = find_globalid(globalid);
        if (idx >= 0) {
            item = table.getItem(idx);
            data = (int[]) item.getData();
            data[0]++;
            item.setText(0, "" + data[0]);
            if (select) table.select(idx);
            return;
        }
        item = new TableItem(table, 0);
        data = new int[] { 1, globalid };
        item.setData(data);
        item.setText(0, "" + data[0]);
        info = cardbase.get(globalid);
        name = ((info == null || info.name == null) ? "" : info.name);
        item.setText(1, name);
        if (packable) table.getColumn(1).pack();
        if (select) table.select(table.indexOf(item));
    }

    public int remove_card_idx(int idx) {
        TableItem item;
        int[] data;
        this.changed = true;
        item = table.getItem(idx);
        data = (int[]) item.getData();
        data[0]--;
        if (data[0] == 0) table.remove(idx); else item.setText(0, "" + data[0]);
        return data[1];
    }

    public void swap(int row1, int row2) {
        this.changed = true;
        TableItem item1 = table.getItem(row1);
        TableItem item2 = table.getItem(row2);
        int count = table.getColumnCount();
        for (int iii = 0; iii < count; iii++) {
            String text = item1.getText(iii);
            item1.setText(iii, item2.getText(iii));
            item2.setText(iii, text);
        }
        Object data = item1.getData();
        item1.setData(item2.getData());
        item2.setData(data);
        table.select(row2);
        table.showSelection();
    }

    public void add_this_to_deck(Deck deck, boolean is_sb) {
        int size, idx;
        TableItem item;
        int[] data;
        size = table.getItemCount();
        for (idx = 0; idx < size; idx++) {
            item = table.getItem(idx);
            data = (int[]) item.getData();
            deck.add(data[0], data[1], is_sb);
        }
    }

    public void mouseDown(MouseEvent event) {
    }

    public void mouseUp(MouseEvent event) {
        if ((event.widget instanceof Table) && (event.button == 3)) {
            Table table = (Table) event.widget;
            TableItem item = table.getItem(new Point(event.x, event.y));
            int idx;
            if ((item != null) && ((idx = table.indexOf(item)) >= 0) && table.isSelected(idx)) {
                int[] data = (int[]) item.getData();
                CardInfo info = cardbase.get(data[1]);
                Shell shell = table.getShell();
                Composite parent = shell.getParent();
                if (parent != null) shell = parent.getShell();
                new ViewCardDialog(shell, info, gallery);
            }
        }
    }

    public void mouseDoubleClick(MouseEvent event) {
    }

    public int getSelectionIndex() {
        return table.getSelectionIndex();
    }

    public int getItemCount() {
        return table.getItemCount();
    }

    public int get_globalid(int idx) {
        TableItem item = table.getItem(idx);
        return ((int[]) item.getData())[1];
    }

    public int get_num_cards() {
        int num_cards, num_rows, idx;
        TableItem item;
        int[] data;
        num_cards = 0;
        num_rows = table.getItemCount();
        for (idx = 0; idx < num_rows; idx++) {
            item = table.getItem(idx);
            data = (int[]) item.getData();
            num_cards += data[0];
        }
        return num_cards;
    }
}
