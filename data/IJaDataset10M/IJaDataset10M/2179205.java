package acide.gui.menuBar.configurationMenu;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import acide.configuration.menu.AcideMenuConfiguration;
import acide.gui.menuBar.configurationMenu.consoleMenu.AcideConsoleMenu;
import acide.gui.menuBar.configurationMenu.fileEditor.AcideFileEditorMenu;
import acide.gui.menuBar.configurationMenu.grammarMenu.AcideGrammarMenu;
import acide.gui.menuBar.configurationMenu.languageMenu.AcideLanguageMenu;
import acide.gui.menuBar.configurationMenu.lexiconMenu.AcideLexiconMenu;
import acide.gui.menuBar.configurationMenu.listeners.AcideCompilerMenuItemListener;
import acide.gui.menuBar.configurationMenu.menuMenu.AcideMenuMenu;
import acide.gui.menuBar.configurationMenu.toolBarMenu.AcideToolBarMenu;
import acide.language.AcideLanguageManager;

/**
 * ACIDE - A Configurable IDE configuration menu.
 * 
 * @version 0.8
 * @see ActionListener
 */
public class AcideConfigurationMenu extends JMenu {

    /**
	 * ACIDE - A Configurable IDE configuration menu class serial version UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * ACIDE - A Configurable IDE configuration menu lexicon menu item name.
	 */
    public static final String LEXICON_NAME = "Lexicon";

    /**
	 * ACIDE - A Configurable IDE configuration menu grammar menu item name.
	 */
    public static final String GRAMMAR_NAME = "Grammar";

    /**
	 * ACIDE - A Configurable IDE configuration menu language menu item name.
	 */
    public static final String LANGUAGE_NAME = "Language";

    /**
	 * ACIDE - A Configurable IDE configuration menu file editor menu item name.
	 */
    public static final String FILE_EDITOR_NAME = "File Editor";

    /**
	 * ACIDE - A Configurable IDE configuration menu menu menu item name.
	 */
    public static final String MENU_NAME = "Menu";

    /**
	 * ACIDE - A Configurable IDE configuration menu console menu item name.
	 */
    public static final String CONSOLE_NAME = "Console";

    /**
	 * ACIDE - A Configurable IDE configuration menu toolbar menu item name.
	 */
    public static final String TOOLBAR_NAME = "Toolbar";

    /**
	 * ACIDE - A Configurable IDE configuration menu compiler menu item name.
	 */
    public static final String COMPILER_NAME = "Compiler";

    /**
	 * ACIDE - A Configurable IDE configuration menu compiler menu item image
	 * icon.
	 */
    public static final ImageIcon COMPILER_IMAGE = new ImageIcon("./resources/icons/menu/configuration/compiler.png");

    /**
	 * ACIDE - A Configurable IDE configuration menu menu menu item.
	 */
    private AcideMenuMenu _menuMenu;

    /**
	 * ACIDE - A Configurable IDE configuration menu language menu item.
	 */
    private AcideLanguageMenu _languageMenu;

    /**
	 * ACIDE - A Configurable IDE configuration menu file editor menu item.
	 */
    private AcideFileEditorMenu _fileEditorMenu;

    /**
	 * ACIDE - A Configurable IDE configuration menu console menu item.
	 */
    private AcideConsoleMenu _consoleMenu;

    /**
	 * ACIDE - A Configurable IDE configuration menu tool bar menu item.
	 */
    private AcideToolBarMenu _toolBarMenu;

    /**
	 * ACIDE - A Configurable IDE configuration menu lexicon menu item.
	 */
    private AcideLexiconMenu _lexiconMenu;

    /**
	 * ACIDE - A Configurable IDE configuration menu grammar menu item.
	 */
    private AcideGrammarMenu _grammarMenu;

    /**
	 * ACIDE - A Configurable IDE configuration menu compiler menu item.
	 */
    private JMenuItem _compilerMenuItem;

    /**
	 * ACIDE - A Configurable IDE configuration menu compiler file editor
	 * separator.
	 */
    private JSeparator _compilerFileEditorSeparator;

    /**
	 * ACIDE - A Configurable IDE configuration menu console language separator.
	 */
    private JSeparator _consoleLanguageSeparator;

    /**
	 * Creates a new ACIDE - A Configurable IDE configuration menu.
	 */
    public AcideConfigurationMenu() {
        buildComponents();
        addComponents();
        setTextOfMenuComponents();
    }

    /**
	 * Adds the components to the ACIDE - A Configurable IDE configuration menu.
	 */
    private void addComponents() {
        add(_lexiconMenu);
        add(_grammarMenu);
        add(_compilerMenuItem);
        add(_compilerFileEditorSeparator);
        add(_fileEditorMenu);
        add(_consoleMenu);
        add(_consoleLanguageSeparator);
        add(_languageMenu);
        add(_menuMenu);
        add(_toolBarMenu);
    }

    /**
	 * Builds the ACIDE - A Configurable IDE configuration menu components.
	 */
    private void buildComponents() {
        _consoleMenu = new AcideConsoleMenu();
        _consoleMenu.setName(CONSOLE_NAME);
        _fileEditorMenu = new AcideFileEditorMenu();
        _fileEditorMenu.setName(FILE_EDITOR_NAME);
        _languageMenu = new AcideLanguageMenu();
        _languageMenu.setName(LANGUAGE_NAME);
        _menuMenu = new AcideMenuMenu();
        _menuMenu.setName(MENU_NAME);
        _toolBarMenu = new AcideToolBarMenu();
        _toolBarMenu.setName(TOOLBAR_NAME);
        _lexiconMenu = new AcideLexiconMenu();
        _lexiconMenu.setName(LEXICON_NAME);
        _grammarMenu = new AcideGrammarMenu();
        _grammarMenu.setName(GRAMMAR_NAME);
        _compilerMenuItem = new JMenuItem(COMPILER_IMAGE);
        _compilerMenuItem.setName(COMPILER_NAME);
        _compilerFileEditorSeparator = new JSeparator();
        _consoleLanguageSeparator = new JSeparator();
    }

    /**
	 * Sets the text of the ACIDE - A Configurable IDE configuration menu
	 * components with the labels in the selected language to display.
	 */
    public void setTextOfMenuComponents() {
        _languageMenu.setText(AcideLanguageManager.getInstance().getLabels().getString("s6"));
        _languageMenu.setTextOfMenuComponents();
        _consoleMenu.setText(AcideLanguageManager.getInstance().getLabels().getString("s332"));
        _consoleMenu.setTextOfMenuComponents();
        _fileEditorMenu.setText(AcideLanguageManager.getInstance().getLabels().getString("s1045"));
        _fileEditorMenu.setTextOfMenuComponets();
        _menuMenu.setText(AcideLanguageManager.getInstance().getLabels().getString("s34"));
        _menuMenu.setTextOfMenuComponents();
        _toolBarMenu.setText(AcideLanguageManager.getInstance().getLabels().getString("s169"));
        _toolBarMenu.setTextOfMenuComponents();
        _lexiconMenu.setText(AcideLanguageManager.getInstance().getLabels().getString("s224"));
        _lexiconMenu.setTextOfMenuComponents();
        _grammarMenu.setText(AcideLanguageManager.getInstance().getLabels().getString("s225"));
        _grammarMenu.setTextOfMenuComponents();
        _compilerMenuItem.setText(AcideLanguageManager.getInstance().getLabels().getString("s240"));
    }

    /**
	 * Updates the ACIDE - A Configurable IDE configuration menu components
	 * visibility with the menu configuration.
	 */
    public void updateComponentsVisibility() {
        _lexiconMenu.build();
        _lexiconMenu.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.DOCUMENT_LEXICON_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.MODIFY_LEXICON_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.NEW_LEXICON_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.DEFAULT_LEXICONS_NAME));
        _grammarMenu.updateComponentsVisibility();
        _grammarMenu.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.NEW_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.LOAD_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.MODIFY_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.SAVE_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.SAVE_GRAMMAR_AS_NAME));
        _compilerMenuItem.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(COMPILER_NAME));
        _compilerFileEditorSeparator.setVisible((AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.DOCUMENT_LEXICON_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.MODIFY_LEXICON_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.NEW_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.LOAD_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.MODIFY_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(COMPILER_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.NEW_LEXICON_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.SAVE_GRAMMAR_NAME)) && (AcideMenuConfiguration.getInstance().getIsDisplayed(AcideConsoleMenu.CONFIGURE_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideConsoleMenu.EXTERNAL_COMMAND_NAME)));
        _fileEditorMenu.updateComponentsVisibility();
        _fileEditorMenu.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(AcideFileEditorMenu.FILE_EDITOR_DISPLAY_OPTIONS_NAME));
        _consoleMenu.updateComponentsVisibility();
        _consoleMenu.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(AcideConsoleMenu.CONFIGURE_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideConsoleMenu.EXTERNAL_COMMAND_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideConsoleMenu.CONSOLE_DISPLAY_OPTIONS_NAME));
        _consoleLanguageSeparator.setVisible((AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.DOCUMENT_LEXICON_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.MODIFY_LEXICON_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.NEW_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.LOAD_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.MODIFY_GRAMMAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(COMPILER_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideConsoleMenu.CONFIGURE_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideConsoleMenu.EXTERNAL_COMMAND_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideConsoleMenu.CONSOLE_DISPLAY_OPTIONS_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLexiconMenu.NEW_LEXICON_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideGrammarMenu.SAVE_GRAMMAR_NAME)) && (AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLanguageMenu.SPANISH_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLanguageMenu.ENGLISH_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(MENU_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(TOOLBAR_NAME)));
        _languageMenu.updateComponentsVisibility();
        _languageMenu.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLanguageMenu.SPANISH_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideLanguageMenu.ENGLISH_NAME));
        _menuMenu.updateComponentsVisibility();
        _menuMenu.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(AcideMenuMenu.NEW_MENU_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideMenuMenu.LOAD_MENU_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideMenuMenu.MODIFY_MENU_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideMenuMenu.SAVE_MENU_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideMenuMenu.SAVE_MENU_AS_NAME));
        _toolBarMenu.updateComponentsVisibility();
        _toolBarMenu.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(AcideToolBarMenu.NEW_TOOLBAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideToolBarMenu.LOAD_TOOLBAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideToolBarMenu.MODIFY_TOOLBAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideToolBarMenu.SAVE_TOOLBAR_NAME) || AcideMenuConfiguration.getInstance().getIsDisplayed(AcideToolBarMenu.SAVE_TOOLBAR_AS_NAME));
    }

    /**
	 * Sets the ACIDE - A Configurable IDE configuration menu menu item
	 * listeners.
	 */
    public void setListeners() {
        _lexiconMenu.setListeners();
        _grammarMenu.setListeners();
        _fileEditorMenu.setListeners();
        _consoleMenu.setListeners();
        _menuMenu.setListeners();
        _languageMenu.setListeners();
        _toolBarMenu.setListeners();
        _compilerMenuItem.addActionListener(new AcideCompilerMenuItemListener());
    }

    /**
	 * Returns the ACIDE - A Configurable IDE configuration menu grammar menu.
	 * 
	 * @return the ACIDE - A Configurable IDE configuration menu grammar menu.
	 */
    public AcideGrammarMenu getGrammarMenu() {
        return _grammarMenu;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE configuration menu menu menu.
	 * 
	 * @return the ACIDE - A Configurable IDE configuration menu menu menu.
	 */
    public AcideMenuMenu getMenuMenu() {
        return _menuMenu;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE configuration menu lexicon menu.
	 * 
	 * @return the ACIDE - A Configurable IDE configuration menu lexicon menu.
	 */
    public AcideLexiconMenu getLexiconMenu() {
        return _lexiconMenu;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE configuration menu console menu.
	 * 
	 * @return the ACIDE - A Configurable IDE configuration menu console menu.
	 */
    public AcideConsoleMenu getConsoleMenu() {
        return _consoleMenu;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE configuration menu file editor
	 * menu.
	 * 
	 * @return the ACIDE - A Configurable IDE configuration menu file editor
	 *         menu.
	 */
    public AcideFileEditorMenu getFileEditorMenu() {
        return _fileEditorMenu;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE configuration menu tool bar menu.
	 * 
	 * @return the ACIDE - A Configurable IDE configuration menu tool bar menu.
	 */
    public AcideToolBarMenu getToolBarMenu() {
        return _toolBarMenu;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE configuration menu language menu.
	 * 
	 * @return the ACIDE - A Configurable IDE configuration menu language menu.
	 */
    public AcideLanguageMenu getLanguageMenu() {
        return _languageMenu;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE configuration menu compiler menu
	 * item.
	 * 
	 * @return the ACIDE - A Configurable IDE configuration menu compiler menu
	 *         item.
	 */
    public JMenuItem getCompilerMenuItem() {
        return _compilerMenuItem;
    }
}
