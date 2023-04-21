package to_do_o.gui.test.bot;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;
import static junit.framework.Assert.assertEquals;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotAssert;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

/**
 * 
 * "Bot" for testing To-Do-O. This class provides convenience methods for
 * testing To-Do-O and should be preferably used instead of SWTBot directly.
 * 
 * @author Ruediger Gad
 * 
 */
public class ToDoOBot {

    private SWTBot bot;

    public ToDoOBot(SWTBot bot) {
        this.bot = bot;
    }

    public void checkChildCount(int[] path, int expectedCount) {
        SWTBotTreeItem treeItem = getItemToCheck(path);
        assertEquals(expectedCount, treeItem.getItems().length);
    }

    /**
     * Check if the text of the tree node described by path equals the supplied
     * text String. Path is an integer array of indices describing the position
     * of the node to be checked inside the tree.<br/>
     * E.g.: new int[]{0,1} denotes the second child node of the first root node
     * in the tree.
     * 
     * @param path
     * @param expectedText
     */
    public void checkItemText(int[] path, String expectedText) {
        SWTBotTreeItem treeItem = getItemToCheck(path);
        SWTBotAssert.assertText(expectedText, treeItem);
    }

    /**
     * Creates a new main to-do entry and returns the created {@link TreeItem}.
     * 
     * @param text
     * @return
     */
    public void createNewMainEntry(String text) {
        bot.menu("New").menu("Main To-Do Entry").click();
        bot.waitUntil(shellIsActive("New Main To-Do Entry"));
        bot.text().setText(text);
        bot.button("OK").click();
        waitForMainShell();
    }

    /**
     * Creates a new note entry below below the entry given by path. E.g.: new
     * int[]{0,1} denotes the second child node of the first root node in the
     * tree.
     * 
     * @param text
     * @return
     */
    public void createNewSubNote(int[] path, String text) {
        selectTreeItem(path);
        bot.menu("New").menu("Sub").menu("Note").click();
        bot.waitUntil(shellIsActive("New Note"));
        bot.text().setText(text);
        bot.button("OK").click();
        waitForMainShell();
    }

    /**
     * Creates a new sub to-do entry below the entry given by path. E.g.: new
     * int[]{0,1} denotes the second child node of the first root node in the
     * tree.
     * 
     * @param text
     * @return
     */
    public void createNewSubToDo(int[] path, String text) {
        selectTreeItem(path);
        bot.menu("New").menu("Sub").menu("To-Do").click();
        bot.waitUntil(shellIsActive("New Sub To-Do"));
        bot.text().setText(text);
        bot.button("OK").click();
        waitForMainShell();
    }

    public void deleteMarkedDone(int[] path) {
        selectTreeItem(path);
        bot.menu("Tools").menu("Delete Done Items").click();
    }

    public void exitToDoO() {
        bot.menu("File").menu("Exit").click();
    }

    private SWTBotTreeItem getItemToCheck(int[] path) {
        SWTBotTree tree = bot.tree();
        SWTBotTreeItem[] treeItems = tree.getAllItems();
        for (int i = 0; i < (path.length - 1); i++) {
            SWTBotTreeItem item = treeItems[path[i]];
            item.expand();
            treeItems = item.getItems();
        }
        SWTBotTreeItem treeItem = treeItems[path[path.length - 1]];
        return treeItem;
    }

    /**
     * Mark item denoted by path done.
     * 
     * @see ToDoOBot#selectTreeItem(int[])
     * @param path
     */
    public void markItemDone(int[] path) {
        selectTreeItem(path);
        bot.menu("Item").menu("Mark Done").click();
    }

    /**
     * Execute "Move All Done To Bottom".
     * 
     * @param path
     */
    public void moveAllMarkedDoneDown(int[] path) {
        selectTreeItem(path);
        bot.menu("Tools").menu("Move All Done To Bottom").click();
    }

    /**
     * Execute "Move Done To Bottom" for the node given by path.
     * 
     * @see ToDoOBot#selectTreeItem(int[])
     * @param path
     */
    public void moveMarkedDoneDown(int[] path) {
        selectTreeItem(path);
        bot.menu("Tools").menu("Move Done To Bottom").click();
    }

    public void openSettingsDialog() {
        bot.menu("File").menu("Settings").click();
        bot.waitUntil(shellIsActive("Settings"));
    }

    /**
     * Select the node in the tree given by path. E.g.: new int[]{0,1} denotes
     * the second child node of the first root node in the tree.
     * 
     * @param path
     */
    public void selectTreeItem(int[] path) {
        SWTBotTree tree = bot.tree();
        SWTBotTreeItem[] treeItems = tree.getAllItems();
        for (int i = 0; i < (path.length - 1); i++) {
            treeItems = treeItems[path[i]].getItems();
        }
        SWTBotTreeItem treeItem = treeItems[path[path.length - 1]];
        treeItem.select();
    }

    /**
     * Opens the settings dialog and sets the placement for new main entries.
     * 
     * @param placement
     */
    public void setNewMainEntryPlacement(NewItemPlacement placement) {
        String shellText = bot.activeShell().getText();
        openSettingsDialog();
        bot.comboBox(1).setSelection(placement.toString());
        bot.button("OK").click();
        bot.waitUntil(shellIsActive(shellText));
    }

    /**
     * Opens the settings dialog and sets the placement for new sub entries.
     * 
     * @param placement
     */
    public void setNewSubEntryPlacement(NewItemPlacement placement) {
        String shellText = bot.activeShell().getText();
        openSettingsDialog();
        bot.comboBox(0).setSelection(placement.toString());
        bot.button("OK").click();
        bot.waitUntil(shellIsActive(shellText));
    }

    public void setShellSize(final int x, final int y) {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                Shell shell = bot.activeShell().widget;
                shell.setSize(x, y);
            }
        });
    }

    /**
     * Take screen shot of the currently active shell and store it to file name.
     * 
     * @param fileName
     */
    public void takeScreenShot(final String fileName) {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                Shell shell = bot.activeShell().widget;
                SWTUtils.captureScreenshot(fileName, shell);
            }
        });
    }

    /**
     * Wait until the main shell is active again.
     * 
     * @return
     */
    public void waitForMainShell() {
        bot.waitUntil(shellIsActive("To-Do-O *"));
    }
}
