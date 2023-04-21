package com.gft.larozanam.client;

import com.gft.larozanam.client.componentes.CMenu;
import com.gft.larozanam.client.componentes.CMenuBar;
import com.gft.larozanam.client.componentes.CMenuItem;
import com.gft.larozanam.client.componentes.eventos.EventBus;
import com.gft.larozanam.client.menus.SiteMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Menu extends Composite {

    private static MenuUiBinder uiBinder = GWT.create(MenuUiBinder.class);

    interface MenuUiBinder extends UiBinder<Widget, Menu> {
    }

    @UiField
    CMenuBar menubar;

    public Menu() {
        initWidget(uiBinder.createAndBindUi(this));
        for (SiteMap rootMenuItem : SiteMap.getRootMenus()) {
            CMenu subMenu = new CMenu(rootMenuItem.getTextOrHtml());
            menubar.addItem(subMenu);
            for (SiteMap mi : rootMenuItem.getChildren()) {
                if (mi.isMenu()) {
                    CMenu menu = createMenus(mi);
                    subMenu.addItem(menu);
                } else {
                    String html = createImageAndText(mi);
                    CMenuItem menuItem = new CMenuItem(html, criarCommand(mi));
                    subMenu.addItem(menuItem);
                }
            }
        }
        CMenuItem menuItem = new CMenuItem("Sair", new Command() {

            @Override
            public void execute() {
                Window.alert("saindo do sistema");
            }
        });
        menubar.addItem(menuItem);
    }

    private Command criarCommand(final SiteMap menuItem) {
        return new Command() {

            @Override
            public void execute() {
                Widget w = menuItem.getCompositeFactory().create();
                EventBus.fire(new EventoMudarCorpoAplicacao(w));
            }
        };
    }

    private String createImageAndText(SiteMap mi) {
        String html = mi.getTextOrHtml();
        return html;
    }

    private CMenu createMenus(SiteMap menuItem) {
        CMenu menu = new CMenu(createImageAndText(menuItem));
        for (SiteMap mi : menuItem.getChildren()) {
            if (mi.isMenu()) {
                menu.addItem(createMenus(mi));
            } else {
                menu.addItem(new CMenuItem(createImageAndText(mi), criarCommand(mi)));
            }
        }
        return menu;
    }
}
