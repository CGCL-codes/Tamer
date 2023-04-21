package bg.plambis.dict.gui;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import bg.plambis.dict.gui.pref.PrefChangeObserver;
import bg.plambis.dict.gui.pref.PreferencesManager;
import bg.plambis.dict.local.LocalizationUtil;
import bg.plambis.dict.local.LocalizedComponents;
import bg.plambis.dict.local.LocalizedComponentsImpl;
import bg.plambis.dict.util.DictUtil;

/**
 * @author pivanov
 */
public class DictionaryGUI implements PrefChangeObserver {

    private static final String MAIN_WINDOW = "MAIN_WINDOW";

    DictionaryControl dict;

    public static final String LOGO_PATH = DictUtil.getCurrentDir() + File.separator + "images" + File.separator + "bulgaria.png";

    private LocalizedComponents locComponents = new LocalizedComponentsImpl();

    Display display;

    Shell shell;

    public static void main(String[] args) {
        new DictionaryGUI().go();
    }

    private void go() {
        display = new Display();
        shell = new Shell(display);
        shell.setText(LocalizationUtil.getI18nText("DICT_TITLE", PreferencesManager.getPreferences().getIntLang()));
        shell.setImage(new Image(display, LOGO_PATH));
        locComponents.addTextExec(shell, "DICT_TITLE");
        createMenu(display, shell);
        GridLayout layout = new GridLayout(2, false);
        shell.setLayout(layout);
        changePreferences();
        TabFolder mainTabFolder = new TabFolder(shell, SWT.FILL);
        GridData data = new GridData(GridData.FILL_BOTH);
        mainTabFolder.setLayoutData(data);
        TabItem dictTabItem = new TabItem(mainTabFolder, SWT.DEFAULT);
        dictTabItem.setText(LocalizationUtil.getI18nText("DICT_DTAB_TITLE", PreferencesManager.getPreferences().getIntLang()));
        locComponents.addTextExec(dictTabItem, "DICT_DTAB_TITLE");
        dict = new DictionaryControl(mainTabFolder, SWT.NO_SCROLL);
        dictTabItem.setControl(dict);
        TabItem translateTabItem = new TabItem(mainTabFolder, SWT.DEFAULT);
        translateTabItem.setText(LocalizationUtil.getI18nText("DICT_TTAB_TITLE", PreferencesManager.getPreferences().getIntLang()));
        locComponents.addTextExec(translateTabItem, "DICT_TTAB_TITLE");
        TranslateControl translate = new TranslateControl(mainTabFolder, SWT.NO_SCROLL);
        translateTabItem.setControl(translate);
        PreferencesManager.getInstance().addPrefObserver(MAIN_WINDOW, this);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        shell.dispose();
    }

    /** create main menu */
    private void createMenu(Display display, Shell shell) {
        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        locComponents.addTextExec(fileMenuHeader, "DICT_MENU_FILE");
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);
        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        locComponents.addTextExec(fileExitItem, "DICT_MENU_EXIT");
        MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        locComponents.addTextExec(helpMenuHeader, "DICT_MENU_HELP");
        Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
        helpMenuHeader.setMenu(helpMenu);
        MenuItem prefItem = new MenuItem(helpMenu, SWT.PUSH);
        locComponents.addTextExec(prefItem, "DICT_MENU_PREF");
        prefItem.addSelectionListener(new PrefItemListener(shell));
        MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
        locComponents.addTextExec(aboutItem, "DICT_MENU_ABOUT");
        aboutItem.addSelectionListener(new AboutItemListener(shell));
        fileExitItem.addSelectionListener(new FileExitItemListener(display, shell));
        shell.setMenuBar(menuBar);
    }

    /**
	 * Listener class that support about button
	 * @author pivanov
	 */
    class AboutItemListener implements SelectionListener {

        private Shell shell;

        public AboutItemListener(Shell shell) {
            this.shell = shell;
        }

        public void widgetSelected(SelectionEvent event) {
            widgetDefaultSelected(event);
        }

        public void widgetDefaultSelected(SelectionEvent event) {
            AboutDialog aboutDialog = new AboutDialog(shell);
            aboutDialog.open();
        }
    }

    /**
	 * Listener class that support preference menu button button
	 * @author pivanov
	 */
    class PrefItemListener implements SelectionListener {

        private Shell shell;

        public PrefItemListener(Shell shell) {
            this.shell = shell;
        }

        public void widgetSelected(SelectionEvent event) {
            widgetDefaultSelected(event);
        }

        public void widgetDefaultSelected(SelectionEvent event) {
            PreferencesDialog prefDialog = new PreferencesDialog(shell);
            prefDialog.open();
        }
    }

    /**
	 * Listener class that support exit button
	 * 
	 * @author pivanov
	 */
    class FileExitItemListener implements SelectionListener {

        private Display display;

        private Shell shell;

        public FileExitItemListener(Display display, Shell shell) {
            this.display = display;
            this.shell = shell;
        }

        public void widgetSelected(SelectionEvent event) {
            widgetDefaultSelected(event);
        }

        public void widgetDefaultSelected(SelectionEvent event) {
            shell.close();
            display.dispose();
        }
    }

    public void changePreferences() {
        locComponents.localizeAll();
        shell.redraw();
    }
}
