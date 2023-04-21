package de.tmtools.ui;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import de.tmtools.core.Activator;
import de.tmtools.ui.action.*;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private Text searchText;

    private IWorkbenchAction exitAction;

    private IWorkbenchAction aboutAction;

    private IWorkbenchAction newWindowAction;

    private IAction newFileAction;

    private IAction openFileAction;

    private IAction closeFileAction;

    private IAction saveFileAction;

    private IAction saveAsFileAction;

    private IAction findDialogAction;

    private IAction associationAction;

    private IAction colorDialogAction;

    private IAction tmCleanAction;

    private IAction backAction;

    private IAction forwardAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(final IWorkbenchWindow window) {
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        aboutAction = ActionFactory.ABOUT.create(window);
        aboutAction.setImageDescriptor(Activator.getImageDescriptor("/icons/Get Info.PNG"));
        register(aboutAction);
        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        register(newWindowAction);
        openFileAction = new OpenFileAction(window, "Open...");
        register(openFileAction);
        newFileAction = new NewFileAction(window, "New...");
        register(newFileAction);
        closeFileAction = new CloseFileAction(window, "Close");
        register(closeFileAction);
        saveFileAction = new SaveFileAction(window, "Save");
        register(saveFileAction);
        saveAsFileAction = new SaveAsFileAction(window, "Save As...");
        register(saveAsFileAction);
        findDialogAction = new FindDialogAction(window, "Find...");
        register(findDialogAction);
        associationAction = new AssociationAction(window, "Association...");
        register(associationAction);
        colorDialogAction = new ColorDialogAction(window, "Colors...");
        register(colorDialogAction);
        tmCleanAction = new TMCleanAction(window, "TopicMap Cleanup");
        register(tmCleanAction);
        backAction = new BackAction(window, "Back");
        register(backAction);
        forwardAction = new ForwardAction(window, "Forward");
        register(forwardAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager editMenu = new MenuManager("&Edit", IWorkbenchActionConstants.M_EDIT);
        MenuManager optionsMenu = new MenuManager("&Options", "Options");
        MenuManager toolsMenu = new MenuManager("&Tools", "Tools");
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(optionsMenu);
        menuBar.add(toolsMenu);
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        fileMenu.add(newFileAction);
        fileMenu.add(openFileAction);
        fileMenu.add(closeFileAction);
        fileMenu.add(new Separator());
        fileMenu.add(saveFileAction);
        fileMenu.add(saveAsFileAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        editMenu.add(findDialogAction);
        optionsMenu.add(associationAction);
        optionsMenu.add(colorDialogAction);
        toolsMenu.add(tmCleanAction);
        helpMenu.add(aboutAction);
    }

    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        toolbar.add(backAction);
        toolbar.add(forwardAction);
        toolbar.add(newFileAction);
        toolbar.add(openFileAction);
        toolbar.add(closeFileAction);
        toolbar.add(saveFileAction);
        toolbar.add(saveAsFileAction);
        toolbar.add(findDialogAction);
        toolbar.add(aboutAction);
    }

    @Override
    protected void fillStatusLine(IStatusLineManager statusLine) {
        statusLine.add(new ContributionItem("1") {

            public void fill(Composite parent) {
                searchText = new Text(parent, SWT.BORDER);
                searchText.setText("");
                searchText.addSelectionListener(new SelectionListener() {

                    public void widgetDefaultSelected(SelectionEvent e) {
                        String s = searchText.getText();
                        Activator.getDefault().findNext(s);
                    }

                    public void widgetSelected(SelectionEvent e) {
                    }
                });
                Button btnPrev = new Button(parent, SWT.PUSH | SWT.FLAT);
                btnPrev.setText("Find Previous");
                Image image = Activator.getDefault().getImage(Activator.IMG_FIND_PREV);
                btnPrev.setImage(image);
                btnPrev.addSelectionListener(new SelectionListener() {

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }

                    public void widgetSelected(SelectionEvent e) {
                        String s = searchText.getText();
                        Activator.getDefault().findPrevious(s);
                    }
                });
                Button btnNext = new Button(parent, SWT.PUSH | SWT.FLAT);
                btnNext.setText("Find Next");
                image = Activator.getDefault().getImage(Activator.IMG_FIND_NEXT);
                btnNext.setImage(image);
                btnNext.addSelectionListener(new SelectionListener() {

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }

                    public void widgetSelected(SelectionEvent e) {
                        String s = searchText.getText();
                        Activator.getDefault().findNext(s);
                    }
                });
            }
        });
        statusLine.update(true);
    }
}
