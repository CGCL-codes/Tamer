package net.sf.refactorit.jdeveloper;

import net.sf.refactorit.common.util.ResourceUtil;
import net.sf.refactorit.ui.UIResources;
import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.addin.Controller;
import oracle.jdeveloper.runner.Source;
import java.net.URL;
import java.util.LinkedList;

/**
 * Implements store for the last GoToAction lines.
 * Opens this place if action is performed.
 *
 * @author Vladislav Vislogubov
 */
public class BackAction {

    public static class BackInfo {

        public URL node;

        public int line;

        /**
     * @param line
     * @param node
     */
        public BackInfo(URL node, int line) {
            this.line = line;
            this.node = node;
        }
    }

    private static LinkedList list = new LinkedList();

    public BackAction(int cmdId, Controller controller) {
        IdeAction action = IdeAction.get(cmdId, getClass().getName(), "Back", null, new Integer('B'), ResourceUtil.getIcon(UIResources.class, "back_action.gif"), Boolean.FALSE, true);
        AbstractionUtils.addController(action, controller);
        action.putValue(IdeAction.CATEGORY, RefactorItAddin.REFACTORIT_CATEGORY);
    }

    public static void performAction() {
        if (list.isEmpty()) {
            return;
        }
        BackInfo info = (BackInfo) list.removeLast();
        try {
            Source.showSourceFile(Ide.getActiveWorkspace(), Ide.getActiveProject(), info.node, info.line, false);
        } catch (Exception ignore) {
        }
    }

    public static void addRecord(URL n, int l) {
        if (n == null) {
            return;
        }
        list.addLast(new BackInfo(n, l));
    }
}
