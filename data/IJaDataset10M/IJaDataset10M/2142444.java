package org.sf.pkb.gui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.sf.pkb.db.DBHelper;
import org.sf.pkb.gui.importpkb.ImportWizardDialog;
import org.sf.pkb.gui.importpkb.WizardImport;
import org.sf.pkb.gui.mainscreen.MainScreen;
import org.sf.pkb.kevent.PKBEvent;
import org.sf.pkb.resources.Resources;
import org.sf.pkb.upgrade.Upgrade1ChangeContentToBlob;
import org.sf.pkb.util.P;

public class PKBMain {

    public static String PRODUCT_NAME = "ALEX Personal Knowledge Base";

    public static final String PRODUCT_ALEX_PKB = "ALEX PKB";

    public static final String CONTACT_EMAIL = "alexpkb@gmail.com";

    public static final String PRODUCT_WEBSITE = "http://pkb.sourceforge.net";

    public static Shell shell;

    private static MessageBox errorMessageBox = null;

    private static MessageBox successMessageBox = null;

    public static void main(String[] args) {
        try {
            doIt();
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg != null) {
                JOptionPane.showMessageDialog(new JFrame(), msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace();
        } catch (UnsatisfiedLinkError ulex) {
            String msg = ulex.getMessage();
            if (msg != null) {
                StringBuffer buf = new StringBuffer();
                int pos32bit = msg.indexOf("32-bit");
                int pos64bit = msg.indexOf("64-bit");
                if (pos32bit >= 0 && pos64bit >= 0) {
                    if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                        if (pos32bit < pos64bit) {
                            buf.append(msg);
                            buf.append("\nPlease execute pkb-win64.exe or run_win64.bat instead.");
                        } else {
                            buf.append(msg);
                            buf.append("\nPlease execute pkb.exe or run_win32.bat instead.");
                        }
                    }
                    if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
                        if (pos32bit < pos64bit) {
                            buf.append(msg);
                            buf.append("\nPlease execute run_linux_x86_64.sh instead.");
                        } else {
                            buf.append(msg);
                            buf.append("\nPlease execute run_linux.sh instead.");
                        }
                    }
                    if (buf.length() > 0) {
                        JOptionPane.showMessageDialog(new JFrame(), buf.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            ulex.printStackTrace();
        }
        System.exit(0);
    }

    public static void doIt() throws Exception {
        if (System.getProperty("derby.system.home") == null) {
            System.setProperty("derby.system.home", System.getProperty("user.dir") + File.separator + "data");
        }
        PreferenceDialog.loadProperties();
        String title = PreferenceDialog.getProperty(PreferenceDialog.PKBProperty.rebrand_application_title);
        if (title.trim().length() > 0) {
            PRODUCT_NAME = title;
        }
        Display display = new Display();
        shell = new Shell(display);
        shell.setText(PRODUCT_NAME);
        StackLayout shellLayout = new StackLayout();
        shell.setLayout(shellLayout);
        errorMessageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
        successMessageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem itemFile = new MenuItem(menuBar, SWT.CASCADE);
        itemFile.setText("&File");
        Menu menu = new Menu(itemFile);
        MenuItem itemBackup = new MenuItem(menu, SWT.PUSH);
        itemBackup.setText("&Backup");
        itemBackup.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                FileDialog dialog = new FileDialog(shell, SWT.SAVE);
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                dialog.setFileName("backup_pkb_" + year + (month < 10 ? "0" + month : "" + month) + (day < 10 ? "0" + day : "" + day) + ".zip");
                dialog.setFilterNames(new String[] { "Zip Files", "All Files (*.*)" });
                dialog.setFilterExtensions(new String[] { "*.zip", "*.*" });
                String fileName = dialog.open();
                if (fileName != null && fileName.trim().length() > 0) {
                    File file = new File(fileName);
                    if (file.exists()) {
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                        messageBox.setMessage("File already exsists, do you want to replace?");
                        messageBox.setText("Backup");
                        int response = messageBox.open();
                        if (response != SWT.YES) {
                            return;
                        }
                    }
                    MainScreen.Widget.getKnowledgeNavPanel().getKnowledgeTreeComponent().save();
                    try {
                        ProgressBarDialog pb = new ProgressBarDialog(shell);
                        pb.registerProcessThread(new BackupThread(100, pb, fileName));
                        pb.open();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    showSuccessMessage("Database is succesfully backuped!");
                }
            }
        });
        MenuItem itemRestore = new MenuItem(menu, SWT.PUSH);
        itemRestore.setText("&Restore");
        itemRestore.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                FileDialog dialog = new FileDialog(shell, SWT.OPEN);
                dialog.setFilterNames(new String[] { "Zip Files", "All Files (*.*)" });
                dialog.setFilterExtensions(new String[] { "*.zip", "*.*" });
                String fileName = dialog.open();
                if (fileName != null && fileName.trim().length() > 0) {
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    messageBox.setMessage("Current database will be replaced by a backup, do you want to restore?");
                    messageBox.setText("Restore");
                    int response = messageBox.open();
                    if (response != SWT.YES) {
                        return;
                    }
                    try {
                        ProgressBarDialog pb = new ProgressBarDialog(shell);
                        pb.registerProcessThread(new RestoreThread(100, pb, fileName));
                        pb.open();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    showSuccessMessage("Database is succesfully restored!");
                    try {
                        if (Upgrade1ChangeContentToBlob.needToUpgrade()) {
                            Upgrade1ChangeContentToBlob.upgrade();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PreferenceDialog.loadPKBDataConfig();
                    MainScreen.Widget.getKnowledgeNavPanel().getKnowledgeTreeComponent().init();
                    MainScreen.Widget.getKnowledgeNavPanel().refreshAutoCompleteKeywords();
                    PKBEvent.fireEvent(PKBEvent.EV_SEARCH);
                    PKBEvent.fireEvent(PKBEvent.KN_TreeView, PKBEvent.EV_REFRESH, false);
                }
            }
        });
        itemFile.setMenu(menu);
        new MenuItem(menu, SWT.SEPARATOR);
        MenuItem itemImport = new MenuItem(menu, SWT.PUSH);
        P.BOT.register(itemImport, P.BOT.MENU_File_import);
        itemImport.setText("Import");
        itemImport.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                WizardImport wizard = new WizardImport();
                ImportWizardDialog dialog = new ImportWizardDialog(PKBMain.shell, wizard);
                dialog.setHelpAvailable(false);
                ImportWizardDialog.setDefaultImage(Resources.IMAGE_PKB_16);
                dialog.open();
            }
        });
        MenuItem itemExport = new MenuItem(menu, SWT.PUSH);
        itemExport.setText("Export");
        itemExport.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
                dialog.setMessage("Please choose a directory to export the HTML version of knowledge base:");
                dialog.setFilterPath(P.DIR.getPKBBasePath());
                String dirName = dialog.open();
                if (dirName != null && dirName.trim().length() > 0) {
                    try {
                        ProgressBarDialog pb = new ProgressBarDialog(shell);
                        pb.registerProcessThread(new ExportThread(100, pb, dirName));
                        pb.open();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    showSuccessMessage("Export done!");
                }
            }
        });
        MenuItem itemRebuildIndex = new MenuItem(menu, SWT.PUSH);
        itemRebuildIndex.setText("Rebuild &index");
        itemRebuildIndex.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                try {
                    ProgressBarDialog pb = new ProgressBarDialog(shell);
                    pb.registerProcessThread(new RebuildIndexThread(100, pb));
                    pb.open();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                showSuccessMessage("Index is updated!");
                MainScreen.Widget.getKnowledgeNavPanel().refreshAutoCompleteKeywords();
            }
        });
        new MenuItem(menu, SWT.SEPARATOR);
        MenuItem itemPreference = new MenuItem(menu, SWT.PUSH);
        itemPreference.setText("&Preference");
        itemPreference.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                PreferenceDialog preferenceDlg = new PreferenceDialog(shell);
                preferenceDlg.open();
            }
        });
        new MenuItem(menu, SWT.SEPARATOR);
        MenuItem itemExit = new MenuItem(menu, SWT.PUSH);
        itemExit.setText("&Exit");
        itemExit.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setMessage("Do you really want to exit?");
                messageBox.setText("Exiting Application");
                int response = messageBox.open();
                if (response == SWT.YES) {
                    DBHelper.shutdownDB();
                    MainScreen.Widget.getKnowledgeNavPanel().getKnowledgeTreeComponent().save();
                    saveUILocation();
                    System.exit(0);
                }
            }
        });
        itemFile.setMenu(menu);
        MenuItem itemHelp = new MenuItem(menuBar, SWT.CASCADE);
        itemHelp.setText("&Help");
        menu = new Menu(itemHelp);
        MenuItem itemHelpContents = new MenuItem(menu, SWT.PUSH);
        itemHelpContents.setText("&Contents");
        itemHelpContents.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                MainScreen.Widget.getKnowledgeContentPanel().showURLInBrowser(P.DIR.getPKBBasePath() + File.separator + "help" + File.separator + "index.html");
            }
        });
        MenuItem itemAbout = new MenuItem(menu, SWT.PUSH);
        itemAbout.setText("&About");
        itemAbout.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                AboutDialog aboutDlg = new AboutDialog(shell);
                aboutDlg.open();
            }
        });
        itemHelp.setMenu(menu);
        shell.setMenuBar(menuBar);
        try {
            if (!DBHelper.startDB()) {
                MessageWindow messageWindow = new MessageWindow(shell, display.getSystemImage(SWT.ICON_WORKING), "Initializing your knowledge base for the first time. \nThis may take some time.");
                messageWindow.open();
                DBHelper.initDB();
                DBHelper.startDB();
                messageWindow.close();
                showSuccessMessage("Your knowledge base is ready to use!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorMessage("Cannot start knowledge base.  Possible reasons: \n 1. PKB application is already running! \n 2. Database is not configured properly!");
            DBHelper.shutdownDB();
            MainScreen.Widget.getKnowledgeNavPanel().getKnowledgeTreeComponent().save();
            System.exit(0);
        }
        if (DBHelper.isLuceneIndexFormatChanged()) {
            MessageBox msgBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            msgBox.setMessage("The search index of your knowledge base needs to be updated, do you want to rebuild the search index now? \n" + "If not, you can do it later by choosing menu File->Rebuild index.");
            msgBox.setText("Rebuild index");
            if (msgBox.open() == SWT.YES) {
                try {
                    ProgressBarDialog pb = new ProgressBarDialog(shell);
                    pb.registerProcessThread(new RebuildIndexThread(100, pb));
                    pb.open();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                showSuccessMessage("Index is updated!");
            }
        }
        if (Upgrade1ChangeContentToBlob.needToUpgrade()) {
            Upgrade1ChangeContentToBlob.upgrade();
        }
        Resources.create(display);
        shell.setImage(Resources.IMAGE_PKB);
        MainScreen.setup(shell);
        MainScreen.show();
        PKBEvent.fireEvent(PKBEvent.EV_SEARCH);
        if (GlobalData.searchResult != null && GlobalData.searchResult.size() > 0) {
            GlobalData.currentInfo = GlobalData.searchResult.get(0);
            PKBEvent.fireEvent(PKBEvent.EV_SHOW_INFO);
        }
        try {
            final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
            tip.setMessage(PRODUCT_NAME);
            tip.setText(PRODUCT_NAME);
            final Tray tray = display.getSystemTray();
            if (tray == null) {
                System.out.println("The system tray is not available");
            } else {
                final TrayItem item = new TrayItem(tray, SWT.NONE);
                item.setVisible(false);
                item.setToolTip(tip);
                item.setToolTipText(PRODUCT_NAME);
                item.addListener(SWT.Show, new Listener() {

                    public void handleEvent(Event event) {
                    }
                });
                item.addListener(SWT.Hide, new Listener() {

                    public void handleEvent(Event event) {
                    }
                });
                item.addListener(SWT.Selection, new Listener() {

                    public void handleEvent(Event event) {
                    }
                });
                item.addListener(SWT.DefaultSelection, new Listener() {

                    public void handleEvent(Event event) {
                        Shell s = event.display.getShells()[0];
                        s.setVisible(true);
                        item.setVisible(false);
                        s.setMinimized(false);
                    }
                });
                final Menu popupMenu = new Menu(shell, SWT.POP_UP);
                MenuItem popItem = new MenuItem(popupMenu, SWT.PUSH);
                popItem.setText("Restore");
                popItem.addListener(SWT.Selection, new Listener() {

                    public void handleEvent(Event event) {
                        Shell s = event.display.getShells()[0];
                        s.setVisible(true);
                        item.setVisible(false);
                        s.setMinimized(false);
                    }
                });
                popItem = new MenuItem(popupMenu, SWT.PUSH);
                popItem.setText("Close");
                popItem.addListener(SWT.Selection, new Listener() {

                    public void handleEvent(Event event) {
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                        messageBox.setMessage("Do you really want to exit?");
                        messageBox.setText("Exiting Application");
                        int response = messageBox.open();
                        if (response == SWT.YES) {
                            DBHelper.shutdownDB();
                            MainScreen.Widget.getKnowledgeNavPanel().getKnowledgeTreeComponent().save();
                            saveUILocation();
                            item.dispose();
                            System.exit(0);
                        }
                    }
                });
                item.addListener(SWT.MenuDetect, new Listener() {

                    public void handleEvent(Event event) {
                        popupMenu.setVisible(true);
                    }
                });
                item.setImage(Resources.IMAGE_PKB_16);
                shell.addShellListener(new ShellListener() {

                    public void shellDeactivated(org.eclipse.swt.events.ShellEvent e) {
                    }

                    public void shellActivated(org.eclipse.swt.events.ShellEvent e) {
                    }

                    public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
                    }

                    public void shellDeiconified(org.eclipse.swt.events.ShellEvent e) {
                    }

                    public void shellIconified(org.eclipse.swt.events.ShellEvent e) {
                        if (PreferenceDialog.getProperty(PreferenceDialog.GlobalProperty.minnimize_to_systray).compareTo("true") == 0) {
                            ((Shell) e.getSource()).setVisible(false);
                            item.setVisible(true);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            reportError("System tray initialization error", ex);
        }
        shell.pack();
        shell.open();
        shell.addListener(SWT.Close, new Listener() {

            public void handleEvent(Event event) {
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setMessage("Do you really want to exit?");
                messageBox.setText("Exiting Application");
                event.doit = (messageBox.open() == SWT.YES);
                MainScreen.Widget.getKnowledgeNavPanel().getKnowledgeTreeComponent().save();
                saveUILocation();
            }
        });
        String isMaximized = PreferenceDialog.getProperty(PreferenceDialog.GlobalProperty.retain_window_maximized);
        if (isMaximized.compareTo("true") == 0) {
            shell.setBounds(PreferenceDialog.GlobalProperty.DEFAULT_retain_window_x, PreferenceDialog.GlobalProperty.DEFAULT_retain_window_y, PreferenceDialog.GlobalProperty.DEFAULT_retain_window_width, PreferenceDialog.GlobalProperty.DEFAULT_retain_window_height);
            shell.setMaximized(true);
        } else {
            int x = PreferenceDialog.GlobalProperty.DEFAULT_retain_window_x;
            String windowX = PreferenceDialog.getProperty(PreferenceDialog.GlobalProperty.retain_window_x);
            try {
                x = Integer.parseInt(windowX);
            } catch (Exception ex) {
            }
            int y = PreferenceDialog.GlobalProperty.DEFAULT_retain_window_y;
            String windowY = PreferenceDialog.getProperty(PreferenceDialog.GlobalProperty.retain_window_y);
            try {
                y = Integer.parseInt(windowY);
            } catch (Exception ex) {
            }
            int width = PreferenceDialog.GlobalProperty.DEFAULT_retain_window_width;
            String windowWidth = PreferenceDialog.getProperty(PreferenceDialog.GlobalProperty.retain_window_width);
            try {
                width = Integer.parseInt(windowWidth);
            } catch (Exception ex) {
            }
            int height = PreferenceDialog.GlobalProperty.DEFAULT_retain_window_height;
            String windowHeight = PreferenceDialog.getProperty(PreferenceDialog.GlobalProperty.retain_window_height);
            try {
                height = Integer.parseInt(windowHeight);
            } catch (Exception ex) {
            }
            shell.setBounds(x, y, width, height);
        }
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
        DBHelper.shutdownDB();
    }

    public static int showWarningDialog(String message) {
        if (P.BOT.underTestMode()) {
            return P.BOT.showDialog(shell, message, "Warning", SWT.ICON_WARNING | SWT.YES | SWT.NO);
        } else {
            MessageBox messageBox = new MessageBox(PKBMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
            messageBox.setText("Warning");
            messageBox.setMessage(message);
            return messageBox.open();
        }
    }

    public static int showQuestionDialog(String message) {
        if (P.BOT.underTestMode()) {
            return P.BOT.showDialog(shell, message, "Question", SWT.ICON_WARNING | SWT.YES | SWT.NO);
        } else {
            MessageBox messageBox = new MessageBox(PKBMain.shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            messageBox.setText("Question");
            messageBox.setMessage(message);
            return messageBox.open();
        }
    }

    public static void showSuccessMessage(String message) {
        if (P.BOT.underTestMode()) {
            P.BOT.showMessageBox(shell, message, "Information", SWT.ICON_INFORMATION | SWT.OK);
        } else {
            successMessageBox.setText("Information");
            successMessageBox.setMessage(message);
            successMessageBox.open();
        }
    }

    public static void showErrorMessage(String message) {
        if (P.BOT.underTestMode()) {
            P.BOT.showMessageBox(shell, message, "Information", SWT.ICON_ERROR | SWT.OK);
        } else {
            errorMessageBox.setText("Error");
            errorMessageBox.setMessage(message);
            errorMessageBox.open();
        }
    }

    public static void reportError(String context, Exception ex) {
        if (shell == null) {
            Display display = new Display();
            shell = new Shell(display);
        }
        MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.YES | SWT.NO);
        messageBox.setMessage("An error occured, would you like to send this error by email ?");
        messageBox.setText("Exiting Application");
        if (messageBox.open() == SWT.YES) {
            try {
                String LINE_BREAK = System.getProperty("line.separator");
                StringBuffer buf = new StringBuffer();
                buf.append(context).append(LINE_BREAK).append(LINE_BREAK);
                buf.append(ex.getMessage()).append(LINE_BREAK).append(LINE_BREAK);
                ByteArrayOutputStream out = new ByteArrayOutputStream(10000);
                PrintWriter writer = new PrintWriter(out);
                ex.printStackTrace(writer);
                writer.close();
                out.close();
                buf.append(out.toString());
                String body = buf.toString().replace(LINE_BREAK, "%0A");
                Program.launch("mailto:alexpkb@gmail.com?subject=[ALEX PKB error report]&body=" + body);
            } catch (Exception ex1) {
            }
            ;
        }
    }

    private static void saveUILocation() {
        int leftPanelWidth = MainScreen.getLeftPanelWidth();
        PreferenceDialog.setProperty(PreferenceDialog.GlobalProperty.retain_left_panel_width, Integer.toString(leftPanelWidth));
        int rightPanelWidth = MainScreen.getRightPanelWidth();
        PreferenceDialog.setProperty(PreferenceDialog.GlobalProperty.retain_right_panel_width, Integer.toString(rightPanelWidth));
        PreferenceDialog.setProperty(PreferenceDialog.GlobalProperty.retain_window_maximized, Boolean.toString(shell.getMaximized()));
        if (!shell.getMaximized() && !shell.getMinimized() && shell.getVisible()) {
            int windowX = shell.getBounds().x;
            int windowY = shell.getBounds().y;
            int windowWidth = shell.getBounds().width;
            int windowHeight = shell.getBounds().height;
            PreferenceDialog.setProperty(PreferenceDialog.GlobalProperty.retain_window_x, Integer.toString(windowX));
            PreferenceDialog.setProperty(PreferenceDialog.GlobalProperty.retain_window_y, Integer.toString(windowY));
            PreferenceDialog.setProperty(PreferenceDialog.GlobalProperty.retain_window_width, Integer.toString(windowWidth));
            PreferenceDialog.setProperty(PreferenceDialog.GlobalProperty.retain_window_height, Integer.toString(windowHeight));
        }
        PreferenceDialog.saveProperties();
    }
}
