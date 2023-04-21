package org.gamesroom.util;

import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.gamesroom.database.Game;

public class GuiUtils {

    public static void updateGamesTable(Table table, List<Game> games) {
        table.removeAll();
        for (Game game : games) {
            TableItem ti = new TableItem(table, SWT.BORDER);
            ti.setText(new String[] { "" + game.getId(), game.getName() });
            ti.setData(game);
        }
        table.update();
    }

    public static void showMessage(Shell s, String text, String message, int style) {
        MessageBox mb = new MessageBox(s, style);
        mb.setMessage(message);
        mb.setText(text);
        mb.open();
    }

    public static int showConfirmation(Shell s, String text, String message, int style) {
        style = style | SWT.YES | SWT.NO;
        MessageBox mb = new MessageBox(s, style);
        mb.setMessage(message);
        mb.setText(text);
        return mb.open();
    }
}
