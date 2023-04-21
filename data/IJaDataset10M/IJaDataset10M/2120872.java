package applicationWorkbench.actions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import applicationWorkbench.Application;
import cards.CardConstants;
import cards.commands.SynchronizeProjectCommand;

public class SychronizeProjectAction extends Action implements IWorkbenchAction {

    public static final String ID = "applicationWorkbench.actions.SychronizeProjectAction";

    private IWorkbenchWindow window;

    public SychronizeProjectAction(IWorkbenchWindow window) {
        setId(ID);
        setText("&Synchronize");
        setToolTipText("Synchronize with Rally");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, CardConstants.SearchIcon));
    }

    public void dispose() {
    }

    public void run() {
    }

    SynchronizeProjectCommand syn = new SynchronizeProjectCommand();
}
