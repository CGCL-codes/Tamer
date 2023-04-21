package com.cameocontrol.cameo.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import com.cameocontrol.cameo.gui.WindowBuilder;

public class MenuFile implements ActionListener {

    private WindowBuilder _builder;

    private String save = "Save Show";

    private String saveAs = "Save Show As";

    private String load = "Load Show";

    private String quit = "Quit";

    public MenuFile(WindowBuilder wb) {
        _builder = wb;
    }

    public void getMenu(int x, MenuData menus) {
        int menuItems = 4;
        int item = 0;
        menus.names[x] = "File";
        menus.discriptions[x] = "Menu of file options";
        menus.keyEvents[x] = KeyEvent.VK_F;
        menus.itemsNames[x] = new String[menuItems];
        menus.itemsDiscriptions[x] = new String[menuItems];
        menus.itemsKeyEvents[x] = new int[menuItems];
        menus.itemsKeyMasks[x] = new int[menuItems];
        menus.itemsActionListeners[x] = new ActionListener[menuItems];
        item = 0;
        menus.itemsNames[x][item] = save;
        menus.itemsDiscriptions[x][item] = "Saves the current show to a file";
        menus.itemsKeyEvents[x][item] = KeyEvent.VK_S;
        menus.itemsKeyMasks[x][item] = ActionEvent.ALT_MASK;
        menus.itemsActionListeners[x][item] = this;
        item = 1;
        menus.itemsNames[x][item] = saveAs;
        menus.itemsDiscriptions[x][item] = "Saves the current show to a file";
        menus.itemsKeyEvents[x][item] = KeyEvent.VK_A;
        menus.itemsKeyMasks[x][item] = ActionEvent.ALT_MASK;
        menus.itemsActionListeners[x][item] = this;
        item = 2;
        menus.itemsNames[x][item] = load;
        menus.itemsDiscriptions[x][item] = "Loads a show from a file";
        menus.itemsKeyEvents[x][item] = KeyEvent.VK_O;
        menus.itemsKeyMasks[x][item] = ActionEvent.ALT_MASK;
        menus.itemsActionListeners[x][item] = this;
        item = 3;
        menus.itemsNames[x][item] = quit;
        menus.itemsDiscriptions[x][item] = "Closes the application";
        menus.itemsKeyEvents[x][item] = KeyEvent.VK_Q;
        menus.itemsKeyMasks[x][item] = ActionEvent.CTRL_MASK;
        menus.itemsActionListeners[x][item] = this;
    }

    public void actionPerformed(ActionEvent event) {
        JMenuItem source = (JMenuItem) (event.getSource());
        if (source.getText().compareTo(save) == 0) {
            _builder.buildSave();
        } else if (source.getText().compareTo(saveAs) == 0) {
            _builder.buildSaveDiag();
        } else if (source.getText().compareTo(load) == 0) {
            _builder.buildLoadDiag();
        } else if (source.getText().compareTo(quit) == 0) {
            _builder.quitApp();
        }
    }
}
