package pogvue.gui.menus;

import pogvue.gui.AlignViewport;
import pogvue.gui.Controller;
import javax.swing.*;
import pogvue.gui.*;

public final class MenuManager {

    private final AlignSplitPanel panel;

    private JMenuBar menuBar;

    private EditMenu editMenu;

    private FontMenu fontMenu;

    private ViewMenu viewMenu;

    private CalcMenu calcMenu;

    private HelpMenu helpMenu;

    private final AlignViewport av;

    private final Controller controller;

    public MenuManager(AlignSplitPanel panel, AlignViewport av, Controller controller) {
        this.panel = panel;
        this.av = av;
        this.controller = controller;
        menuInit();
    }

    private void menuInit() {
        menuBar = new JMenuBar();
        FileMenu fileMenu = new FileMenu(null, panel, av, controller);
        editMenu = new EditMenu(panel, av, controller);
        viewMenu = new ViewMenu(panel, av, controller);
        fontMenu = new FontMenu(panel, av, controller);
        calcMenu = new CalcMenu(panel, av, controller);
        helpMenu = new HelpMenu(panel, av, controller);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(fontMenu);
        menuBar.add(viewMenu);
        menuBar.add(calcMenu);
        menuBar.add(helpMenu);
        menuBar.add(helpMenu);
        panel.add("North", menuBar);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
