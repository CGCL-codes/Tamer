package com.googlecode.legendtv.ui.console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.xml.bind.JAXBException;
import com.googlecode.legendtv.data.menu.ActionEmitter;
import com.googlecode.legendtv.data.menu.ActionType;
import com.googlecode.legendtv.data.menu.Control;
import com.googlecode.legendtv.data.menu.MenuImpl;
import com.googlecode.legendtv.data.menu.MenuListImpl;

public class ConsoleInterface implements ActionListener {

    private static final String MENU_FILE = "src/com/googlecode/legendtv/ui/menus.xml";

    private MenuListImpl menuInfo;

    private Map<String, MenuImpl> menuMap;

    private Menu curMenuUI;

    public ConsoleInterface() throws JAXBException {
        this.menuInfo = MenuListImpl.load(MENU_FILE);
        this.menuInfo.resolveMenuLinks();
        this.menuMap = this.menuInfo.getMenuMap();
        setupListener();
    }

    private void setupListener() {
        for (MenuImpl curMenu : this.menuMap.values()) {
            for (Control curControl : curMenu.getControlList()) {
                if (curControl instanceof ActionEmitter) {
                    ((ActionEmitter) curControl).addActionListener(this);
                }
            }
        }
    }

    public void start() {
        goTo(this.menuInfo.getRootMenu());
        do {
            assert (this.curMenuUI != null);
            this.curMenuUI.paint();
        } while (!processInput());
    }

    private void goTo(String name) {
        com.googlecode.legendtv.data.menu.Menu newMenu;
        newMenu = this.menuMap.get(name);
        this.curMenuUI = new com.googlecode.legendtv.ui.console.Menu((MenuImpl) newMenu);
    }

    private boolean processInput() {
        return true;
    }

    public static void main(String[] args) throws JAXBException {
        ConsoleInterface intf = new ConsoleInterface();
        intf.start();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getID() == ActionType.MENU.ordinal()) {
            this.goTo(event.getActionCommand());
        }
    }
}
