package cz.langteacher.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.springframework.beans.factory.annotation.Autowired;
import cz.langteacher.I18n;
import cz.langteacher.LTSettingKeys;
import cz.langteacher.gui.components.LButtonLink;
import cz.langteacher.gui.components.LIconProviderIface;
import cz.langteacher.gui.components.LOptionPane;
import cz.langteacher.gui.exam.ExamConfigWindowIface;
import cz.langteacher.gui.importexport.ExportWindowIface;
import cz.langteacher.gui.importexport.ImportWindowIface;
import cz.langteacher.gui.mainwindow.MainWindowIface;
import cz.langteacher.gui.search.SearchWindowIface;
import cz.langteacher.gui.util.GUIConstants;
import cz.langteacher.gui.util.LTIcon;
import cz.langteacher.manager.LangTeacherSettingsIface;
import cz.langteacher.plugin.ILTPLugin;
import cz.langteacher.plugin.impl.IPluginManager;
import cz.langteacher.util.ApplContextProviderIface;

public class MenuBar implements MenuBarIface {

    private static final long serialVersionUID = 313357105062773436L;

    @Autowired
    private MainWindowIface mainWindow;

    @Autowired
    private HelperHandlerIface helpHandler;

    @Autowired
    private ImportWindowIface importWindow;

    @Autowired
    private ExportWindowIface exportWindow;

    @Autowired
    private LangTeacherSettingsIface settings;

    @Autowired
    private PreferencesWindow preferencesWindow;

    @Autowired
    private ApplContextProviderIface applContexProvider;

    @Autowired
    private ExamConfigWindowIface examConfigWindow;

    @Autowired
    private LIconProviderIface iconProvider;

    private JMenuBar menuBar = null;

    @Autowired
    private LOptionPane lOptionPane;

    @Autowired
    private IPluginManager pluginManager;

    private void init() {
        menuBar = new JMenuBar();
        JMenu file = new JMenu(I18n.translate("File"));
        JMenu exam = new JMenu(I18n.translate("Exam"));
        JMenu tools = new JMenu(I18n.translate("Tools"));
        JMenu help = new JMenu(I18n.translate("Help"));
        JMenu plugins = buildMenuForPlugins();
        JMenuItem quit = new JMenuItem(I18n.translate("Quit"));
        quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.quitApplication();
            }
        });
        JMenuItem imporT = new JMenuItem(I18n.translate("Import..."));
        imporT.setIcon(iconProvider.getIcon(LTIcon.IMPORT));
        imporT.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                importWindow.showWindow();
            }
        });
        JMenuItem export = new JMenuItem(I18n.translate("Export..."));
        export.setIcon(iconProvider.getIcon(LTIcon.EXPORT));
        export.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exportWindow.showWindow();
            }
        });
        file.add(imporT);
        file.add(export);
        file.add(quit);
        JMenuItem search = new JMenuItem(I18n.translate("Search..."));
        search.setIcon(iconProvider.getIcon(LTIcon.SEARCH));
        search.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((SearchWindowIface) applContexProvider.getBean("searchWindow")).showWindow();
            }
        });
        tools.add(search);
        JMenuItem preferences = new JMenuItem(I18n.translate("Preferences..."));
        preferences.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                preferencesWindow.showWindow();
            }
        });
        tools.add(preferences);
        JMenuItem helpItem = new JMenuItem(I18n.translate("Help"));
        helpItem.setIcon(iconProvider.getIcon(LTIcon.HELP));
        helpItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                helpHandler.showHelp(e);
            }
        });
        help.add(helpItem);
        final JComponent aboutPanel = getAboutPanel();
        quit = new JMenuItem(I18n.translate("About"));
        quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                lOptionPane.showOptionPane(mainWindow.getFrame(), aboutPanel, "About", "Ok");
            }
        });
        help.add(quit);
        JMenuItem examItem = new JMenuItem(I18n.translate("Take Exam..."));
        examItem.setIcon(iconProvider.getIcon(LTIcon.PLAY));
        examItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, InputEvent.CTRL_MASK | InputEvent.ALT_DOWN_MASK));
        examItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                examConfigWindow.showWindow();
            }
        });
        exam.add(examItem);
        menuBar.add(file);
        menuBar.add(exam);
        if (plugins != null) {
            menuBar.add(plugins);
        }
        menuBar.add(tools);
        menuBar.add(help);
    }

    private JMenu buildMenuForPlugins() {
        if (pluginManager.getPlugins().isEmpty()) {
            return null;
        }
        JMenu pluginsMenu = new JMenu(I18n.translate("Plugins"));
        List<ILTPLugin> plugins = pluginManager.getPlugins();
        Collections.sort(plugins, new PluginsNameComparator());
        for (ILTPLugin plugin : plugins) {
            if (plugin.getPluginMenu() != null) {
                pluginsMenu.add(plugin.getPluginMenu());
            }
        }
        return pluginsMenu;
    }

    public JMenuBar getMenuBar() {
        if (menuBar == null) {
            init();
        }
        return menuBar;
    }

    private JComponent getAboutPanel() {
        Box box = Box.createVerticalBox();
        Box visitBox = Box.createHorizontalBox();
        visitBox.setAlignmentX(0.0f);
        String version = settings.getSetting(LTSettingKeys.APPL_VERSION);
        JLabel versionLabel = new JLabel("LangTeacher version " + version);
        visitBox.add(new JLabel(I18n.translate("Please visit")));
        visitBox.add(Box.createHorizontalStrut(GUIConstants.LABEL_HGAP));
        visitBox.add(new LButtonLink("www.langteacher.cz"));
        box.add(versionLabel);
        box.add(Box.createHorizontalStrut(GUIConstants.LABEL_HGAP));
        box.add(visitBox);
        return box;
    }

    private class PluginsNameComparator implements Comparator<ILTPLugin> {

        @Override
        public int compare(ILTPLugin o1, ILTPLugin o2) {
            if (o1.getPluginMenu() != null) {
                if (o2.getPluginMenu() != null) {
                    if (o1.getPluginMenu() instanceof JMenu) {
                        if (o2.getPluginMenu() instanceof JMenu) {
                            return o1.getPluginMenu().getText().compareTo(o2.getPluginMenu().getText());
                        } else {
                            return -1;
                        }
                    } else if (o2.getPluginMenu() instanceof JMenu) {
                        return 1;
                    }
                    return o1.getPluginMenu().getText().compareTo(o2.getPluginMenu().getText());
                }
                return -1;
            } else if (o2.getPluginMenu() != null) {
                return 1;
            }
            return o1.getName().compareTo(o2.getName());
        }
    }
}
