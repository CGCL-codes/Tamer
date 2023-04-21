package org.terentich.rtpa.app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.terentich.ox.managers.OXSkinManager;
import org.terentich.rtpa.ProcessModel;
import org.terentich.rtpa.dict.Dict;
import org.terentich.rtpa.graph.GraphView;

/**
 * <b>Project:</b> Real Time Processes Analyzer (RTPA) <br>
 * <p><b>Description:</b>  </p>
 * <b>Date: </b> 17.10.2008 <br>
 * @author Alexey V. Terentyev
 */
public class AppView extends JFrame {

    JMenuItem itemOpen;

    JMenuItem itemSave;

    JMenuItem itemExit;

    JMenuItem itemSettings;

    JMenuItem itemAuthor;

    public JTabbedPane tabs;

    JMenuBar bar;

    JMenu menuFile;

    JMenu menuOptions;

    JMenu menuAbout;

    public PanelStart panelStart;

    public String language = Dict.CONST.TEXT_RUSSIAN;

    public String lookAndFeel = Dict.CONST.TEXT_NIMBUS;

    private GraphView graph;

    private AppModel model;

    private final Font FONT;

    public AppView(String _settings) {
        printConsoleMessage(Dict.CONST.MSG_START);
        model = new AppModel(this, null, _settings);
        FONT = Dict.CONST.FONT_DEFAULT;
        new AppController(this, model, FONT);
        setView();
        setContent();
        updateView();
    }

    private void setView() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(Dict.CONST.TITLE_APP);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        int height = screenSize.height / 2;
        setSize(width, height);
        setLocation(width / 2, height / 2);
        setLookFeel();
    }

    private void setLookFeel() {
        OXSkinManager skinManager = OXSkinManager.self;
        skinManager.setLookAndFeel(lookAndFeel, this);
    }

    private void setContent() {
        setMenuBar();
        setGraph();
        tabs = new JTabbedPane();
        add(tabs, BorderLayout.CENTER);
        ProcessModel model1 = new ProcessModel() {

            public String getDescription() {
                return "1. Состав задач приложения является фиксированным \n" + "2. Все задачи являются строго периодическими, \n" + "3. Относительный срок выполнения каждой задачи равен ее периоду " + "(Di = Ti, для любого значения i), \n" + "4. Все задачи являются независимыми, \n" + "5. Время выполнения каждой задачи известно, \n" + "6. Задачи не могут находится в состоянии ожидания, \n" + "7. Системные издержки пренебрежимо малы.";
            }

            public String getName() {
                return "Монотонный по частоте (МЧА)";
            }
        };
        ProcessModel model2 = new ProcessModel() {

            public String getDescription() {
                return "Для каждой задачи приложения относительный срок выполнения не должен \n" + "превышать ее периода. ";
            }

            public String getName() {
                return "Монотонный по срокам (МСА)";
            }
        };
        panelStart = new PanelStart(this, new ProcessModel[] { model1, model2 });
        tabs.add(panelStart);
    }

    private void setMenuBar() {
        menuFile = new JMenu();
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        menuFile.add(itemOpen);
        menuFile.add(itemSave);
        menuFile.add(new JSeparator());
        menuFile.add(itemExit);
        menuOptions = new JMenu();
        itemSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        menuOptions.add(itemSettings);
        menuAbout = new JMenu();
        menuAbout.add(itemAuthor);
        bar = new JMenuBar();
        bar.add(menuFile);
        bar.add(menuOptions);
        bar.add(menuAbout);
        setJMenuBar(bar);
    }

    private void setGraph() {
        graph = new GraphView(this, null);
    }

    public AppModel getModel() {
        return model;
    }

    public GraphView getGraph() {
        return graph;
    }

    public void updateView() {
        tabs.setFont(FONT);
        tabs.setTitleAt(0, Dict.CONST.TAB_DEFAULT);
        TabPopup popup = new TabPopup(this);
        tabs.addMouseListener(popup);
        menuFile.setFont(FONT);
        menuFile.setText(Dict.CONST.MENU_FILE);
        itemOpen.setFont(FONT);
        itemSave.setFont(FONT);
        itemExit.setFont(FONT);
        itemOpen.setText(Dict.CONST.MENUITEM_OPEN);
        itemSave.setText(Dict.CONST.MENUITEM_SAVE);
        itemExit.setText(Dict.CONST.MENUITEM_EXIT);
        itemOpen.updateUI();
        itemSave.updateUI();
        itemExit.updateUI();
        menuOptions.setFont(FONT);
        menuOptions.setText(Dict.CONST.MENU_OPTIONS);
        itemSettings.setFont(FONT);
        itemSettings.setText(Dict.CONST.MENUITEM_SETTINGS);
        itemSettings.updateUI();
        menuAbout.setFont(FONT);
        menuAbout.setText(Dict.CONST.MENU_ABOUT);
        itemAuthor.setFont(FONT);
        itemAuthor.setText(Dict.CONST.MENUITEM_AUTHOR);
        itemAuthor.updateUI();
    }

    public void printConsoleMessage(String _message) {
        int starsLength = 60;
        String stars = "";
        for (int index = 0; index < starsLength; index++) stars += "*";
        int whitespaceLength = (stars.length() - _message.length()) / 2;
        String whitespaceLeft = "";
        for (int index = 0; index < whitespaceLength; index++) whitespaceLeft += " ";
        String title = whitespaceLeft + _message + whitespaceLeft;
        System.out.println(stars);
        System.out.println(title);
        System.out.println(stars);
    }
}
