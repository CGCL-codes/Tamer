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

/** Provides the "Page Setup..." command. */
public class PageSetupCommand extends Command {

    /** The action command this command will issue. */
    public static final String CMD_PAGE_SETUP = "PageSetup";

    private static String MSG_PAGE_SETUP;

    private static String MSG_NO_PRINTER_SELECTED;

    static {
        LocalizedMessages.initialize(PageSetupCommand.class);
    }

    /** The singleton {@link PageSetupCommand}. */
    public static final PageSetupCommand INSTANCE = new PageSetupCommand();

    private PageSetupCommand() {
        super(MSG_PAGE_SETUP, CMD_PAGE_SETUP, KeyEvent.VK_P, SHIFTED_COMMAND_MODIFIER);
    }

    @Override
    public void adjustForMenu(JMenuItem item) {
        Window window = getActiveWindow();
        setEnabled(window instanceof AppWindow && window instanceof Printable);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        AppWindow window = (AppWindow) getActiveWindow();
        PrintManager mgr = window.getPrintManager();
        if (mgr != null) {
            mgr.pageSetup(window);
        } else {
            WindowUtils.showError(window, MSG_NO_PRINTER_SELECTED);
        }
    }
}
