package net.sourceforge.scrollrack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class ZoneDialog extends ShellAdapter implements SelectionListener {

    private static final int VIEW_CARD = 1;

    private static final int UP_ARROW = 2;

    private static final int DOWN_ARROW = 3;

    private static final int MOVEALL_BUTTON = 4;

    private static final int OK_BUTTON = 5;

    private Shell dialog;

    private Game game;

    private int pid;

    private int znum;

    private Button shuffle_box;

    public ZoneDialog(Game game, int pid, int znum, int quantity) {
        this.dialog = new Shell(game.window.getShell(), SWT.SHELL_TRIM);
        this.game = game;
        this.pid = pid;
        this.znum = znum;
        Player me = game.player[pid];
        String title = "";
        if (pid == game.local_player) {
            game.display_message("Viewing " + Game.name_of_zone(znum) + "...");
        } else {
            game.display_message("Viewing " + me.name + "'s " + Game.name_of_zone(znum) + "...");
            title = me.name + "'s ";
        }
        title = title + Game.Name_Of_Zone(znum);
        if (quantity > 0) title = title + " (" + quantity + ")";
        dialog.setText(title);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        dialog.setLayout(layout);
        GridData data;
        Zone zone = me.zone[znum];
        boolean have_all = ((quantity == 0) || (quantity >= zone.size()));
        boolean have_arrows = (pid == game.local_player);
        boolean have_shuffle = (have_arrows && have_all && zone.get_flag(Zone.SHUFFLE_CLOSE));
        boolean have_moveall = (have_arrows && !have_all);
        ZoneTable table = new ZoneTable(dialog, game, zone, quantity);
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.verticalSpan = (2 + (have_arrows ? 2 : 0) + (have_shuffle || have_moveall ? -1 : 1));
        data.heightHint = 250;
        table.getControl().setLayoutData(data);
        Button button = new Button(dialog, SWT.PUSH);
        button.setText("View");
        data = new GridData();
        data.grabExcessVerticalSpace = true;
        data.horizontalAlignment = GridData.CENTER;
        button.setLayoutData(data);
        button.setData(new Integer(VIEW_CARD));
        button.addSelectionListener(this);
        if (have_arrows) {
            button = new Button(dialog, SWT.ARROW | SWT.UP);
            data = new GridData();
            data.grabExcessVerticalSpace = true;
            data.horizontalAlignment = GridData.CENTER;
            button.setLayoutData(data);
            button.setData(new Integer(UP_ARROW));
            button.addSelectionListener(this);
            button = new Button(dialog, SWT.ARROW | SWT.DOWN);
            data = new GridData();
            data.grabExcessVerticalSpace = true;
            data.horizontalAlignment = GridData.CENTER;
            button.setLayoutData(data);
            button.setData(new Integer(DOWN_ARROW));
            button.addSelectionListener(this);
        }
        if (have_shuffle) {
            button = new Button(dialog, SWT.CHECK);
            button.setText("Shuffle When Done");
            button.setSelection(true);
            this.shuffle_box = button;
        }
        if (have_moveall) {
            button = new Button(dialog, SWT.PUSH);
            button.setText("Move All to Bottom");
            button.setData(new Integer(MOVEALL_BUTTON));
            button.addSelectionListener(this);
        }
        button = new Button(dialog, SWT.PUSH);
        button.setText("Ok");
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        button.setLayoutData(data);
        button.setData(new Integer(OK_BUTTON));
        button.addSelectionListener(this);
        table.set_dialog(this);
        dialog.addShellListener(this);
        dialog.pack();
        dialog.open();
    }

    public void setActive() {
        dialog.setActive();
    }

    public void shellClosed(ShellEvent event) {
        Zone zone = game.player[pid].zone[znum];
        ZoneTable table = zone.get_table();
        if (table != null) {
            table.set_dialog(null);
            zone.set_table(null);
        }
    }

    public void widgetDefaultSelected(SelectionEvent event) {
    }

    public void widgetSelected(SelectionEvent event) {
        Zone zone;
        int cid;
        boolean do_shuffle;
        switch(((Integer) event.widget.getData()).intValue()) {
            case VIEW_CARD:
                zone = game.player[pid].zone[znum];
                cid = zone.get_selected_card();
                if (cid > 0) game.view_card(cid);
                break;
            case UP_ARROW:
                move_card(-1);
                break;
            case DOWN_ARROW:
                move_card(+1);
                break;
            case MOVEALL_BUTTON:
                move_all_to_bottom();
                break;
            case OK_BUTTON:
                do_shuffle = ((shuffle_box != null) && shuffle_box.getSelection() && (pid == game.local_player) && (znum == Game.LIBRARY_ZONE));
                dialog.close();
                if (do_shuffle) game.local_shuffle();
                break;
        }
    }

    private void move_card(int delta) {
        Zone zone;
        ZoneTable table;
        int idx, other, size, rows, cid;
        if (pid != game.local_player) return;
        zone = game.player[pid].zone[znum];
        table = zone.get_table();
        if (table == null) return;
        idx = table.dragged_card_idx();
        if (idx < 0) return;
        if (!zone.get_flag(Zone.DISPLAY_HAND)) delta = -delta;
        other = idx + delta;
        size = zone.size();
        rows = ((Table) table.getControl()).getItemCount();
        if ((other < 0) || (other >= size) || (zone.get_flag(Zone.DISPLAY_HAND) && (other >= rows)) || (!zone.get_flag(Zone.DISPLAY_HAND) && (other < (size - rows)))) return;
        cid = zone.get(idx);
        switch(game.state) {
            case Game.GAME_SOLITAIRE:
                game.rearrange_zone(game.local_player, pid, znum, idx, other);
                break;
            case Game.GAME_PEER:
                game.rearrange_zone(game.local_player, pid, znum, idx, other);
                game.proto.send_rearrange_zone(pid, znum, cid, other);
                break;
            case Game.GAME_CLIENT:
                game.proto.send_rearrange_zone(pid, znum, cid, other);
                break;
        }
    }

    private void move_all_to_bottom() {
        Zone zone;
        ZoneTable table;
        int size, rows, idx, cid;
        if (pid != game.local_player) return;
        zone = game.player[pid].zone[znum];
        table = zone.get_table();
        if (table == null) return;
        size = zone.size();
        rows = ((Table) table.getControl()).getItemCount();
        if ((rows == 0) || (rows >= size)) return;
        while (rows > 0) {
            idx = 0;
            if (!zone.get_flag(Zone.DISPLAY_HAND)) idx = size - 1 - idx;
            cid = game.move_card(pid, znum, idx, znum, true);
            game.proto.move_card(cid, pid, znum, 0, 0, true);
            rows--;
        }
    }

    public void close() {
        dialog.close();
    }
}
