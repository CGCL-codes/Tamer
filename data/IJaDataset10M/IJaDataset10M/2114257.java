package tardate.jdev.ide.filter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import oracle.ide.Addin;
import oracle.ide.Context;
import oracle.ide.config.ClientSetting;
import oracle.ide.config.IdeSettings;
import oracle.ide.Ide;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;
import oracle.ide.editor.EditorManager;
import oracle.ide.log.LogManager;
import oracle.ide.model.TextNode;
import oracle.ide.model.Element;
import oracle.ide.panels.Navigable;
import oracle.ide.view.View;
import oracle.javatools.editor.BasicEditorPane;
import oracle.ide.ceditor.find.FindableEditor;
import tardate.execshell.ExecShell;

/**
 * JDeveloper filter add-in class
 * 
 * @version $Id: CodePaneAddin.java,v 1.2 2008/02/09 21:49:28 paulpg Exp $
 * @author  Paul Gallagher
 */
public final class CodePaneAddin implements Addin, Controller {

    private static final String EXTENSION_ID = "tardate.jdev.ide.filter";

    public static final int FILTER_CMD_ID = Ide.findOrCreateCmdID("Tardate.FILTER_CMD_ID");

    private static final String MENU_NAME = "Custom Text Filter";

    private static final char MENU_MNEMONIC = 'f';

    private static final String MENU_ICON = "filterIcon_16.gif";

    /**
   * Initialise the class.
   * Initialises config data (if required).
   * Creates the filter menu.
   */
    public void initialize() {
        ClientSetting clientSetting = ClientSetting.findOrCreate(EXTENSION_ID);
        if (clientSetting.getData(ConfigData.KEY) == null) {
            clientSetting.putData(ConfigData.KEY, new ConfigData());
        }
        Navigable myIdeSettingsPanel = new Navigable(ConfigPanelData.TITLE, ConfigPanelData.class);
        IdeSettings.registerUI(myIdeSettingsPanel);
        JMenuItem rclickMi1 = doCreateMenuItem(this, FILTER_CMD_ID, MENU_NAME, new Integer(MENU_MNEMONIC), MENU_ICON);
        ctxMenuListener ctxMenuLstnr1 = new ctxMenuListener(rclickMi1, FILTER_CMD_ID);
        EditorManager.getEditorManager().getContextMenu().addContextMenuListener(ctxMenuLstnr1);
    }

    static BasicEditorPane currEditorPane = null;

    /**
   * Gets a handle to the current IDE editor pane.
   * Caches value in currEditorPane class static variable
   * @param context the JDeveloper IDE Context 
   * @return BasicEditorPane - handle to the IDE editor pane
   */
    static BasicEditorPane getCurrentEditorPane(Context context) {
        if (currEditorPane != null) return currEditorPane;
        BasicEditorPane editorPane = null;
        if (context != null) {
            View view = context.getView();
            FindableEditor findableEditor = null;
            if (view instanceof FindableEditor) {
                findableEditor = (FindableEditor) view;
            }
            if (findableEditor != null) {
                editorPane = findableEditor.getFocusedEditorPane();
            }
        }
        currEditorPane = editorPane;
        return currEditorPane;
    }

    /**
   * Test if menu item should be enabled (true if some text is presently selected in the IDE)
   * @param context the JDeveloper IDE Context 
   * @return boolean whether to enable the menu item or not
   */
    private static boolean shouldMenuBeEnabled(Context context) {
        BasicEditorPane editor = getCurrentEditorPane(context);
        boolean bEnable = (editor.getSelectionStart() < editor.getSelectionEnd());
        return true;
    }

    /**
   * Main method to apply the filter
   * @param context the JDeveloper IDE Context 
   * @return boolean true if the filter succeeded
   */
    private boolean doFilter(Context context) {
        boolean ret = false;
        BasicEditorPane editor = getCurrentEditorPane(context);
        if (editor != null) {
            try {
                String selectionText = editor.getSelectedText();
                if (selectionText == null) selectionText = "";
                if (selectionText.trim().length() > 0) {
                    ConfigData opts = (ConfigData) Ide.getSettings().getData(ConfigData.KEY);
                    logMessage("Running filter: " + opts.getData());
                    String result = ExecShell.runShellCommand(opts.getData(), selectionText);
                    editor.replaceSelection(result);
                    ret = true;
                } else {
                    logMessage("No text selected");
                    ret = false;
                }
            } catch (Exception ex) {
                logMessage(ex.toString());
            }
        }
        return ret;
    }

    /**
   * This method is called when a user interaction with a View triggers the execution of a command
   * @param action action whose command is to be executed
   * @param context the JDeveloper IDE Context 
   * @return boolean true if the event was handled
   */
    public boolean handleEvent(IdeAction action, Context context) {
        boolean bHandled = false;
        int cmdId = action.getCommandId();
        String msg = null;
        if (cmdId == FILTER_CMD_ID) {
            bHandled = true;
            if (doFilter(context)) msg = "Filter applied"; else msg = "Filter failure";
            Ide.getStatusBar().setText(msg);
        }
        return bHandled;
    }

    /**
   * This method updates the enabled status of the specified action within the specified context
   * @param action action whose command is to be executed
   * @param context the JDeveloper IDE Context 
   * @return boolean true if the event was handled
   */
    public boolean update(IdeAction action, Context context) {
        int cmdId = action.getCommandId();
        if (cmdId == FILTER_CMD_ID) {
            if (isMenuAvailable(context)) {
                action.setEnabled(shouldMenuBeEnabled(context));
            }
            return true;
        }
        return false;
    }

    /**
   * Private ContextMenuListener class for add-in pop-up menu item
   * @version $Id: CodePaneAddin.java,v 1.2 2008/02/09 21:49:28 paulpg Exp $
   * @author Paul Gallagher
   */
    private static final class ctxMenuListener implements ContextMenuListener {

        private JMenuItem menuItem;

        private int nCmdID;

        ctxMenuListener(JMenuItem ctxMenuItem, int nCmdID) {
            this.menuItem = ctxMenuItem;
            this.nCmdID = nCmdID;
        }

        public void menuWillShow(ContextMenu popup) {
            Context context = (popup == null) ? null : popup.getContext();
            if (context == null) return;
            if (nCmdID != FILTER_CMD_ID) return;
            if (popup == EditorManager.getEditorManager().getContextMenu()) {
                if (isMenuAvailable(context)) {
                    popup.add(this.menuItem);
                }
            }
            return;
        }

        public void menuWillHide(ContextMenu popup) {
        }

        public boolean handleDefaultAction(Context context) {
            return false;
        }
    }

    /**
   * Creates the filter menu item
   * @param ctrlr controller object
   * @param cmdID action command ID
   * @param menuLabel menu label
   * @param mnemonic shortcut key
   * @param iconName name of icon file (if any)
   * @return JMenuItem the menu item
   */
    private static JMenuItem doCreateMenuItem(Controller ctrlr, int cmdID, String menuLabel, Integer mnemonic, String iconName) {
        Icon mi = ((iconName == null) ? null : new ImageIcon(ctrlr.getClass().getResource(iconName)));
        IdeAction actionTM = IdeAction.get(cmdID, null, menuLabel, null, mnemonic, mi, null, true);
        actionTM.addController(ctrlr);
        JMenuItem menuItem = Ide.getMenubar().createMenuItem(actionTM);
        return menuItem;
    }

    /**
   * Restricts menu availability to only when text is in the editor context
   * @param context the JDeveloper IDE Context 
   * @return boolean true if menu available
   */
    public static boolean isMenuAvailable(Context context) {
        boolean bAvail = false;
        Element el = context.getElement();
        if (el != null) {
            if (el instanceof TextNode) bAvail = true;
        }
        return bAvail;
    }

    /**
   * Helper method to log a message to the IDE messages pane
   * @param msg the message to log 
   */
    private static final void logMessage(String msg) {
        LogManager.getLogManager().showLog();
        LogManager.getLogManager().getMsgPage().log(msg + "\n");
    }
}
