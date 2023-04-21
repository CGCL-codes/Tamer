package com.trollworks.ttk.menu.file;

import com.trollworks.ttk.menu.Command;
import com.trollworks.ttk.print.PrintManager;
import com.trollworks.ttk.utility.LocalizedMessages;
import com.trollworks.ttk.widgets.AppWindow;
import com.trollworks.ttk.widgets.WindowUtils;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.print.Printable;
import javax.swing.JMenuItem;

/** Provides the "Print..." command. */
public class PrintCommand extends Command {

    /** The action command this command will issue. */
    public static final String CMD_PRINT = "Print";

    private static String MSG_PRINT;

    private static String MSG_NO_PRINTER_SELECTED;

    static {
        LocalizedMessages.initialize(PrintCommand.class);
    }

    /** The singleton {@link PrintCommand}. */
    public static final PrintCommand INSTANCE = new PrintCommand();

    private PrintCommand() {
        super(MSG_PRINT, CMD_PRINT, KeyEvent.VK_P);
    }

    @Override
    public void adjustForMenu(JMenuItem item) {
        Window window = getActiveWindow();
        setEnabled(window instanceof AppWindow && window instanceof Printable);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        print((AppWindow) getActiveWindow());
    }

    /** @param window The {@link AppWindow} to print. */
    public static void print(AppWindow window) {
        if (window instanceof Printable) {
            PrintManager mgr = window.getPrintManager();
            if (mgr != null) {
                mgr.print(window, window.getTitle(), (Printable) window);
            } else {
                WindowUtils.showError(window, MSG_NO_PRINTER_SELECTED);
            }
        }
    }
}
