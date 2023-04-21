package consciouscode.bonsai.junit.tags;

import static consciouscode.seedling.Nodes.requiredPath;
import consciouscode.bonsai.nodes.WindowManager;
import consciouscode.seedling.NodeProvisioningException;
import consciouscode.seedling.jelly.tags.SeedlingTagSupport;
import consciouscode.swing.SwingUtils;
import java.awt.Component;
import java.awt.Container;
import org.apache.commons.jelly.tags.junit.JellyAssertionFailedError;

/**

*/
public abstract class BonsaiTestTag extends SeedlingTagSupport {

    /**
       Cause test failure with a given message.  This method always throws an
       error.

       @throws JellyAssertionFailedError at every call.
    */
    public static void fail(String message) throws JellyAssertionFailedError {
        throw new JellyAssertionFailedError(message);
    }

    /**
       Cause test failure with a given message and exception.
       This method always throws an error.

       @throws JellyAssertionFailedError at every call.
    */
    public static JellyAssertionFailedError fail(String message, Throwable exception) throws JellyAssertionFailedError {
        throw new JellyAssertionFailedError(message, exception);
    }

    public Object getNode(String fullPath) {
        try {
            return requiredPath(getLocalRoot(), fullPath);
        } catch (NodeProvisioningException e) {
            throw fail("Unable to get node " + fullPath, e);
        }
    }

    public WindowManager getWindowManager() {
        WindowManager manager = (WindowManager) getNode(WindowManager.DEFAULT_PATH);
        if (manager == null) {
            fail("No window manager found");
        }
        return manager;
    }

    public Component getFocus() {
        AbstractFocusTag focusTag = (AbstractFocusTag) findAncestorWithClass(AbstractFocusTag.class);
        return (focusTag == null ? null : focusTag.getFocus());
    }

    public Component getWidget(String name) {
        Component focus = getFocus();
        Component widget = SwingUtils.getChildComponentByName((Container) focus, name);
        if (widget == null) {
            fail("Widget not found: " + name);
        }
        return widget;
    }
}
