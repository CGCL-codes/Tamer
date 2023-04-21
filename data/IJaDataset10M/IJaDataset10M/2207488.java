package org.designerator.platform.ui.action;

import java.util.Arrays;
import java.util.Comparator;
import org.designerator.common.action.ActionUtil;
import org.designerator.common.interfaces.IActionAdapter;
import org.designerator.media.MediaPlugin;
import org.designerator.media.actions.ThumbViewAction;
import org.designerator.media.util.ImageHelper;
import org.designerator.platform.ui.fullscreen.FullScreenHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.handlers.ShowPerspectiveHandler;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.ShowViewMenu;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.actions.AbstractWorkingSetPulldownDelegate;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;

public class WindowAction extends AbstractWorkingSetPulldownDelegate implements IWorkbenchWindowActionDelegate, IActionAdapter {

    private IWorkbenchWindow window;

    public static final String ID = "org.designerator.media.actions.WindowAction";

    private static final String text = "Show Window";

    public WindowAction() {
    }

    private void addPerspectiveMenu(Menu menu) {
        MenuItem mi = new MenuItem(menu, SWT.NONE);
        mi.setText(IDEWorkbenchMessages.Workbench_openPerspective);
        mi.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent se) {
                try {
                    IHandlerService serv = (IHandlerService) window.getWorkbench().getService(IHandlerService.class);
                    Command c = null;
                    new ShowPerspectiveHandler().execute(serv.createExecutionEvent(c, new Event()));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        ActionUtil.addAction(menu, getShowThumbsAction(window));
    }

    private void addShowViewMenu(Menu menu) {
        String openText = IDEWorkbenchMessages.Workbench_showView;
        Menu subMenu = ActionUtil.getSubMenu(menu, openText);
        ShowViewMenu showViewMenu = new ShowViewMenu(window, MediaPlugin.PLUGIN_ID + ".ShowViewMenu", false);
        showViewMenu.fill(subMenu, -1);
    }

    private Menu createPerspectiveMenu(Menu menu) {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        Menu subMenu = new Menu(menu);
        IPerspectiveDescriptor[] perspectiveDescriptors = getPerspectiveDescriptors();
        for (IPerspectiveDescriptor perspectiveDescriptor : perspectiveDescriptors) {
            final MenuItem menuItem = new MenuItem(subMenu, SWT.PUSH);
            menuItem.setText(perspectiveDescriptor.getLabel());
            menuItem.setImage(perspectiveDescriptor.getImageDescriptor().createImage());
            menuItem.setData(perspectiveDescriptor.getId());
            menuItem.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    try {
                        IPerspectiveDescriptor perspectiveDescriptorWithId = workbench.getPerspectiveRegistry().findPerspectiveWithId((String) e.widget.getData());
                        if (perspectiveDescriptorWithId != null) {
                            workbench.showPerspective(perspectiveDescriptorWithId.getId(), window);
                        }
                    } catch (WorkbenchException we) {
                    }
                }
            });
        }
        return subMenu;
    }

    public void dispose() {
    }

    public void fillActionMenu(Menu menu) {
        ActionUtil.addAction(menu, ActionFactory.OPEN_NEW_WINDOW.create(window));
        new Separator().fill(menu, -1);
        ActionUtil.addAction(menu, new ToggleStatusAction("Toggle Status", IAction.AS_PUSH_BUTTON));
        addPerspectiveMenu(menu);
        addShowViewMenu(menu);
        new Separator().fill(menu, -1);
        ActionUtil.addAction(menu, ActionFactory.EDIT_ACTION_SETS.create(window));
        ActionUtil.addAction(menu, ActionFactory.SAVE_PERSPECTIVE.create(window));
        ActionUtil.addAction(menu, ActionFactory.RESET_PERSPECTIVE.create(window));
        ActionUtil.addAction(menu, ActionFactory.CLOSE_PERSPECTIVE.create(window));
        ActionUtil.addAction(menu, ActionFactory.CLOSE_ALL_PERSPECTIVES.create(window));
        new Separator().fill(menu, -1);
        ActionUtil.addAction(menu, ActionFactory.PREFERENCES.create(window));
        new Separator().fill(menu, -1);
        ActionUtil.addAction(menu, FullScreenHandler.getInstance().getExitAction(window));
    }

    @Override
    protected void fillMenu(Menu menu) {
        fillActionMenu(menu);
    }

    public IAction getAction(final IWorkbenchWindow w) {
        this.window = w;
        Action showWindows = new Action(text, IAction.AS_PUSH_BUTTON) {

            @Override
            public void runWithEvent(Event event) {
                ActionUtil.showActionMenu(event, text, WindowAction.this);
            }
        };
        showWindows.setImageDescriptor(WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_NEW_PAGE));
        showWindows.setToolTipText(text);
        return showWindows;
    }

    private IPerspectiveDescriptor[] getPerspectiveDescriptors() {
        IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
        IPerspectiveDescriptor[] perspectiveDescriptors = perspectiveRegistry.getPerspectives();
        Arrays.sort(perspectiveDescriptors, new Comparator<IPerspectiveDescriptor>() {

            public int compare(IPerspectiveDescriptor pd1, IPerspectiveDescriptor pd2) {
                return pd1.getLabel().compareTo(pd2.getLabel());
            }
        });
        return perspectiveDescriptors;
    }

    public Action getShowThumbsAction(final IWorkbenchWindow window2) {
        Action showThumbsAction = new Action("Open Mediaview", IAction.AS_PUSH_BUTTON) {

            @Override
            public void run() {
                ThumbViewAction.showMediaThumbView(window2.getActivePage());
            }
        };
        showThumbsAction.setImageDescriptor(ImageHelper.createImageDescriptor(null, "/icons/thumbs.gif"));
        showThumbsAction.setToolTipText("Open Mediaview");
        return showThumbsAction;
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run(IAction action) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
