package net.sf.escripts.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import net.sf.escripts.EscriptsElement;
import net.sf.escripts.EscriptsEngine;
import net.sf.escripts.EscriptsLogLevel;
import net.sf.escripts.automaton.Automaton;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.ui.actions.SelectionProviderAction;

/**
* The class/interface {@link ActionRunner} provides utility methods for executing Eclipse actions.
* Actions can be executed based on the following inputs:
* <ul>
*  <li> a {@link Class} object of a class that implements {@link IAction}
*  <li> an {@link IAction} object and an {@link IActionDelegate} object
*  <li> an Eclipse view ID and the name of an action handler for that view
* <ul>
* All three ways of executing an action also support an {@link Automaton} that can be programmed
* to click buttons, enter text into input fields, or perform other operations related to the UI.
* <p/>
* An {@link ActionRunner} is always related to a particular {@link EscriptsElement} and must be
* created with reference to that element. The same {@link ActionRunner} object can be reused
* multiple times, but only one thread at a time can call one of the <code>run(</code> ...
* <code>)</code> methods.
*
* @author mirko
* @version $Revision: 56 $
**/
public class ActionRunner implements Runnable {

    private static final Class[] VOID = {};

    private static final Class[] IWORKBENCHPAGE = { IWorkbenchPage.class };

    private static final Class[] IWORKBENCHWINDOW = { IWorkbenchWindow.class };

    private static final Object[] NO_OBJECT = {};

    private final EscriptsElement element;

    private final Object executeOnlyOneAtATime;

    private IActionDelegate actionDelegate;

    private IAction action;

    private Class actionClass;

    private String viewID;

    private String handlerName;

    private Automaton robot;

    private ISelection selection;

    /**
    * Creates a new {@link ActionRunner} for the given {@link EscriptsElement}.
    *
    * @param element the {@link EscriptsElement} to which this {@link ActionRunner} belongs
    *
    * @author mirko
    **/
    public ActionRunner(EscriptsElement element) {
        this.element = element;
        executeOnlyOneAtATime = new Object();
    }

    /**
    * Runs an action that is associated with a particular view.
    *
    * @param view the ID of the view (for example, "<code>org.eclipse.ui.views.ProblemView</code>")
    * @param handler the name of an action handler for the view (for example, "<code>copy</code>")
    * @param optionalSelection an optional {@link ISelection} to be passed to the action (can be
    * <code>null</code>)
    * TODO: check whether this parameter actually makes sense!
    * @param automaton an {@link Automaton} for automating any UI operations related to the action
    *
    * @author mirko
    **/
    public void run(String view, String handler, ISelection optionalSelection, Automaton automaton) {
        executeAction(set(view, handler, optionalSelection, automaton));
    }

    /**
    * Runs an action for which an {@link IActionDelegate} and an {@link IAction} object is
    * available.
    *
    * @param delegate the action's {@link IActionDelegate}
    * @param actionObject the {@link IAction} object
    * @param automaton an {@link Automaton} for automating any UI operations related to the action
    *
    * @author mirko
    **/
    public void run(IActionDelegate delegate, IAction actionObject, Automaton automaton) {
        executeAction(set(delegate, actionObject, automaton));
    }

    /**
    * Runs an action whose (publicly instantiable) {@link IAction} {@link Class} is known.
    *
    * @param classObject the {@link Class} (must implement {@link IAction})
    * @param optionalSelection an optional {@link ISelection} to be passed to the action (can be
    * <code>null</code>)
    * @param automaton an {@link Automaton} for automating any UI operations related to the action
    *
    * @author mirko
    **/
    public void run(Class classObject, ISelection optionalSelection, Automaton automaton) {
        executeAction(set(classObject, optionalSelection, automaton));
    }

    /**
    * Provides the thread body for executing an action - <b>THIS METHOD MUST NOT BE CALLED DIRECTLY
    * BY CLIENT CODE</b>!
    *
    * @see Runnable#run()
    * @see Display#syncExec(Runnable)
    *
    * @author mirko
    **/
    public void run() {
        EscriptsEngine engine = element.getEngine();
        String debugMessage = "Executing action: actionDelegate=" + actionDelegate;
        debugMessage += ", action=" + action + ", view=" + viewID;
        debugMessage += ", handler=" + handlerName;
        engine.log(debugMessage, EscriptsLogLevel.DEBUG);
        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ShellListener listener = new ActionShellListener(robot, element);
        shell.addShellListener(listener);
        if (actionDelegate != null) {
            actionDelegate.run(action);
        } else if (actionClass != null) {
            Constructor[] constructor = actionClass.getConstructors();
            for (int index = 0; index < constructor.length; index++) {
                Class[] parameters = constructor[index].getParameterTypes();
                Object[] arguments = getConstructorArgumentsFor(parameters);
                if (arguments != null) {
                    try {
                        action = (IAction) constructor[index].newInstance(arguments);
                        break;
                    } catch (InstantiationException exception) {
                        exception.printStackTrace();
                    } catch (IllegalAccessException exception) {
                        exception.printStackTrace();
                    } catch (InvocationTargetException exception) {
                        exception.printStackTrace();
                    }
                }
            }
            setSelection(engine, action);
            action.run();
        } else if (viewID != null && handlerName != null) {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            engine.log("window=" + window, EscriptsLogLevel.DEBUG);
            try {
                IViewPart part = window.getActivePage().showView(viewID);
                IActionBars actionBars = part.getViewSite().getActionBars();
                IAction viewAction = actionBars.getGlobalActionHandler(handlerName);
                engine.log("viewAction=" + viewAction, EscriptsLogLevel.DEBUG);
                setSelection(engine, action);
                viewAction.run();
            } catch (PartInitException exception) {
                exception.printStackTrace();
            }
        }
        shell.removeShellListener(listener);
        engine.log("Action executed.", EscriptsLogLevel.DEBUG);
    }

    private ActionRunner set(Class actionClass, ISelection selection, Automaton robot) {
        actionDelegate = null;
        action = null;
        viewID = null;
        handlerName = null;
        this.actionClass = actionClass;
        this.selection = selection;
        this.robot = robot;
        return this;
    }

    private ActionRunner set(String view, String handler, ISelection selection, Automaton robot) {
        actionClass = null;
        actionDelegate = null;
        action = null;
        this.viewID = view;
        this.handlerName = handler;
        this.robot = robot;
        this.selection = selection;
        return this;
    }

    private ActionRunner set(IActionDelegate actionDelegate, IAction action, Automaton robot) {
        actionClass = null;
        viewID = null;
        handlerName = null;
        selection = null;
        this.actionDelegate = actionDelegate;
        this.action = action;
        this.robot = robot;
        return this;
    }

    private void setSelection(EscriptsEngine engine, IAction action) {
        if (selection != null) {
            if (action instanceof BaseSelectionListenerAction && (selection instanceof IStructuredSelection)) {
                engine.log("Setting selection for BSLA...", EscriptsLogLevel.DEBUG);
                IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                ((BaseSelectionListenerAction) action).selectionChanged(structuredSelection);
            } else if (action instanceof SelectionProviderAction) {
                engine.log("Setting selection for SPA...", EscriptsLogLevel.DEBUG);
                ((SelectionProviderAction) action).selectionChanged(selection);
            } else if (action instanceof IWorkbenchAction) {
                engine.log("Setting selection for WA...", EscriptsLogLevel.DEBUG);
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                IWorkbenchPage page = window.getActivePage();
                if (page == null) {
                    page = window.getPages()[0];
                    window.setActivePage(page);
                }
                IViewPart resourceNavigator = page.findView(IPageLayout.ID_RES_NAV);
                IViewPart packageExplorer = page.findView(JavaUI.ID_PACKAGES);
                IViewPart selectionView = null;
                if (resourceNavigator != null && page.isPartVisible(resourceNavigator)) {
                    selectionView = resourceNavigator;
                } else if (packageExplorer != null && page.isPartVisible(packageExplorer)) {
                    selectionView = packageExplorer;
                } else {
                    selectionView = resourceNavigator != null ? resourceNavigator : packageExplorer;
                }
                if (selectionView == null) {
                    try {
                        selectionView = page.showView(IPageLayout.ID_RES_NAV);
                    } catch (PartInitException exception) {
                        exception.printStackTrace();
                    }
                }
                if (selectionView != null) {
                    page.activate(selectionView);
                    ISelectionProvider selectionProvider;
                    selectionProvider = selectionView.getSite().getSelectionProvider();
                    selectionProvider.setSelection(selection);
                }
            }
        }
    }

    private void executeAction(Runnable runnable) {
        synchronized (executeOnlyOneAtATime) {
            Display.getDefault().syncExec(runnable);
        }
    }

    private Object[] getConstructorArgumentsFor(Class[] parameters) {
        if (Arrays.equals(parameters, VOID)) {
            return NO_OBJECT;
        }
        if (Arrays.equals(parameters, IWORKBENCHWINDOW)) {
            return new Object[] { PlatformUI.getWorkbench().getActiveWorkbenchWindow() };
        }
        if (Arrays.equals(parameters, IWORKBENCHPAGE)) {
            Object[] value = { PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() };
            return value;
        }
        return null;
    }
}
