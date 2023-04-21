package ti.plato.ui.views.internal.console;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import ti.plato.ui.views.console.ConsolePlugin;
import ti.plato.ui.views.console.IConsoleFactory;

/**
 * @since 3.1
 */
public class OpenConsoleAction extends Action implements IMenuCreator {

    private ConsoleFactoryExtension[] fFactoryExtensions;

    private Menu fMenu;

    public OpenConsoleAction() {
        fFactoryExtensions = ((ConsoleManager) ConsolePlugin.getDefault().getConsoleManager()).getConsoleFactoryExtensions();
        setText(ConsoleMessages.OpenConsoleAction_0);
        setToolTipText(ConsoleMessages.OpenConsoleAction_1);
        setImageDescriptor(ConsolePluginImages.getImageDescriptor(IInternalConsoleConstants.IMG_ELCL_NEW_CON));
        setMenuCreator(this);
    }

    public void dispose() {
        fFactoryExtensions = null;
    }

    public Menu getMenu(Control parent) {
        if (fMenu != null) {
            fMenu.dispose();
        }
        fMenu = new Menu(parent);
        int accel = 1;
        for (int i = 0; i < fFactoryExtensions.length; i++) {
            ConsoleFactoryExtension extension = fFactoryExtensions[i];
            if (!WorkbenchActivityHelper.filterItem(extension) && extension.isEnabled()) {
                String label = extension.getLabel();
                ImageDescriptor image = extension.getImageDescriptor();
                addActionToMenu(fMenu, new ConsoleFactoryAction(label, image, extension), accel);
                accel++;
            }
        }
        return fMenu;
    }

    private void addActionToMenu(Menu parent, Action action, int accelerator) {
        if (accelerator < 10) {
            StringBuffer label = new StringBuffer();
            label.append('&');
            label.append(accelerator);
            label.append(' ');
            label.append(action.getText());
            action.setText(label.toString());
        }
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(parent, -1);
    }

    public Menu getMenu(Menu parent) {
        return null;
    }

    private class ConsoleFactoryAction extends Action {

        private ConsoleFactoryExtension fConfig;

        private IConsoleFactory fFactory;

        public ConsoleFactoryAction(String label, ImageDescriptor image, ConsoleFactoryExtension extension) {
            setText(label);
            if (image != null) {
                setImageDescriptor(image);
            }
            fConfig = extension;
        }

        public void run() {
            try {
                if (fFactory == null) {
                    fFactory = fConfig.createFactory();
                }
                fFactory.openConsole();
            } catch (CoreException e) {
                ConsolePlugin.log(e);
            }
        }

        public void runWithEvent(Event event) {
            run();
        }
    }
}
