package net.sourceforge.scrollrack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class InputDialog implements SelectionListener {

    public static final int SET_NAME = 1;

    public static final int VIEW_LIBTOP = 2;

    public static final int SHOW_LIBTOP = 3;

    public static final int SET_LIFE = 4;

    public static final int SET_COUNTERS = 5;

    public static final int ROLL_DIE = 6;

    public static final int JOIN_GAME = 7;

    private static final int ENTRY = 1;

    private static final int OK_BUTTON = 2;

    private static final int CANCEL_BUTTON = 3;

    private Shell dialog;

    private Game game;

    private int id;

    private Text entry;

    private int cid;

    public InputDialog(Game game, String title, String prompt, String def, int id) {
        this.dialog = new Shell(game.window.getShell(), SWT.SHELL_TRIM);
        this.game = game;
        this.id = id;
        dialog.setText(title);
        GridLayout layout = new GridLayout(2, true);
        layout.horizontalSpacing = 15;
        dialog.setLayout(layout);
        GridData data;
        Label label = new Label(dialog, 0);
        label.setText(prompt);
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.CENTER;
        data.horizontalSpan = 2;
        label.setLayoutData(data);
        entry = new Text(dialog, SWT.SINGLE | SWT.BORDER);
        if (def != null) entry.setText(def);
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        data.horizontalSpan = 2;
        data.widthHint = 180;
        data.grabExcessHorizontalSpace = true;
        entry.setLayoutData(data);
        entry.setData(new Integer(ENTRY));
        entry.addSelectionListener(this);
        label = new Label(dialog, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        data.horizontalSpan = 2;
        label.setLayoutData(data);
        Button button = new Button(dialog, SWT.PUSH);
        button.setText("OK");
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.CENTER;
        data.widthHint = 60;
        button.setLayoutData(data);
        button.setData(new Integer(OK_BUTTON));
        button.addSelectionListener(this);
        button = new Button(dialog, SWT.PUSH);
        button.setText("Cancel");
        data.grabExcessHorizontalSpace = true;
        data = new GridData();
        data.horizontalAlignment = SWT.CENTER;
        data.widthHint = 60;
        button.setLayoutData(data);
        button.setData(new Integer(CANCEL_BUTTON));
        button.addSelectionListener(this);
        dialog.pack();
        dialog.open();
    }

    public void set_cid(int cid) {
        this.cid = cid;
    }

    public void widgetDefaultSelected(SelectionEvent event) {
        if (((Integer) event.widget.getData()).intValue() == ENTRY) process_ok_button();
    }

    public void widgetSelected(SelectionEvent event) {
        switch(((Integer) event.widget.getData()).intValue()) {
            case OK_BUTTON:
                process_ok_button();
                break;
            case CANCEL_BUTTON:
                dialog.close();
                break;
        }
    }

    private void process_ok_button() {
        String text = entry.getText();
        switch(id) {
            case SET_NAME:
                if ((text != null) && !text.equals("")) {
                    game.set_player_name(game.local_player, text);
                    game.proto.send_player_name();
                    game.prefs.setProperty(Game.player_name_key, text);
                }
                break;
            case VIEW_LIBTOP:
                int size = atoi(text);
                if (size > 0) game.local_view_library(size);
                break;
            case SHOW_LIBTOP:
                size = atoi(text);
                if (size > 0) game.local_show_zone(Game.LIBRARY_ZONE, size);
                break;
            case SET_LIFE:
                boolean add = false;
                int old_life = game.player[game.local_player].life;
                if (text.startsWith("+")) {
                    add = true;
                    text = text.substring(1);
                } else if (text.startsWith("-")) {
                    add = true;
                }
                int new_life = atoi(text);
                if (new_life == 0) break;
                if (add) new_life += old_life;
                if (old_life != new_life) {
                    game.set_player_life(game.local_player, new_life);
                    game.proto.send_player_life();
                }
                break;
            case SET_COUNTERS:
                TableCard tcard = game.playarea.get_table_card(cid);
                if (tcard != null) {
                    int counters = atoi(text);
                    game.set_card_counters(tcard, counters);
                    game.proto.send_card_counters(tcard.cid, tcard.counters);
                }
                break;
            case ROLL_DIE:
                size = atoi(text);
                int result;
                switch(game.state) {
                    case Game.GAME_SOLITAIRE:
                        result = 1 + DevRandom.random_number(size);
                        game.roll_die(game.local_player, result, size);
                        break;
                    case Game.GAME_PEER:
                    case Game.GAME_CLIENT:
                        if (!game.is_watcher()) game.proto.send_roll_die(size);
                        break;
                }
                break;
        }
        dialog.close();
    }

    private static int atoi(String text) {
        try {
            return (Integer.parseInt(text));
        } catch (Exception exception) {
            return (0);
        }
    }
}
