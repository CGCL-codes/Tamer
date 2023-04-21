package md.ui;

import md.i18n.MDI18N;
import md.ui.components.EditorPanel;
import md.ui.components.InformationPanel;
import md.ui.settings.MDSettingsManager;
import md.ui.settings.Registries;
import md.ui.wrappers.MDCoolbarManager;
import md.ui.wrappers.MDMenuManager;
import md.ui.wrappers.MDStatusLineManager;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This is our application entry point.
 * @author Gilles Meier
 */
public class MainWindow extends ApplicationWindow {

    private static InformationPanel informationPanel;

    private static EditorPanel editorPanel;

    private static MDSettingsManager settingsManager;

    private static MDStatusLineManager statusLine;

    public static final String version = "1.0";

    public static final String authors = "Gilles Meier & Bruno da Silva";

    public static final String name = "MADS Designer";

    /**
	 * Create a new main window for our application.
	 * The settings manager is created at this time.
	 */
    public MainWindow() {
        super(null);
        settingsManager = new MDSettingsManager(this);
        addStatusLine();
        addMenuBar();
        addCoolBar(SWT.NONE);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("MADS-Designer");
        shell.setImage(Registries.getIconRegistry().get("applicationlogo"));
    }

    @Override
    protected Control createContents(Composite parent) {
        SashForm sash = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);
        sash.SASH_WIDTH = 5;
        informationPanel = new InformationPanel(sash);
        editorPanel = new EditorPanel(sash);
        sash.setWeights(new int[] { 1, 3 });
        getShell().pack();
        return sash;
    }

    @Override
    protected MenuManager createMenuManager() {
        return new MDMenuManager(this);
    }

    @Override
    protected CoolBarManager createCoolBarManager(int style) {
        return new MDCoolbarManager(this, style);
    }

    @Override
    protected StatusLineManager createStatusLineManager() {
        statusLine = new MDStatusLineManager(this);
        return statusLine;
    }

    /**
	 * Return the editor panel of the application. This method must
	 * never been call before initialisation.
	 * @return The editor panel of the application
	 */
    public static EditorPanel getEditorPanel() {
        return editorPanel;
    }

    /**
	 * Return the information panel of the application. This method must
	 * never been call before initialisation.
	 * @return The information panel of the application
	 */
    public static InformationPanel getInformationPanel() {
        return informationPanel;
    }

    /**
	 * Return the settings manager of the application. This method must
	 * never been call before initialisation.
	 * @return The settings manager of the application
	 */
    public static MDSettingsManager getSettingsManager() {
        return settingsManager;
    }

    /**
	 * Return the status line manager of the application. This method must
	 * never been call before initialisation.
	 * @return The status line manager of the application
	 */
    public static MDStatusLineManager getStatusLine() {
        return statusLine;
    }

    /**
	 * Must be called when a changed property need the
	 * redrawing of all the application.
	 */
    public void propertiesChanged() {
        getEditorPanel().redrawAll();
    }

    /**
	 * Launch the application.
	 * @param args No arguments required
	 */
    public static void main(String[] args) {
        MDI18N.setLocale("en", "US");
        MainWindow window = new MainWindow();
        window.setBlockOnOpen(true);
        window.open();
        Display.getCurrent().dispose();
    }

    public boolean close() {
        if (getEditorPanel().closeAllTabs()) {
            return super.close();
        } else {
            return false;
        }
    }
}
