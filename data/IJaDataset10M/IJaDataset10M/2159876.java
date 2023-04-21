package net.sourceforge.simpleworklog.client.gwt.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import net.sourceforge.simpleworklog.client.gwt.event.*;
import net.sourceforge.simpleworklog.client.gwt.service.CommandExecutorServiceAsync;
import net.sourceforge.simpleworklog.client.gwt.tabs.ProjectsTabType;
import net.sourceforge.simpleworklog.client.gwt.tabs.UsersTabType;
import net.sourceforge.simpleworklog.client.gwt.tabs.WorklogsTabType;
import net.sourceforge.simpleworklog.shared.action.AsyncCallbackAdaptor;
import net.sourceforge.simpleworklog.shared.action.LogoutAction;
import net.sourceforge.simpleworklog.shared.action.LogoutResponse;

/**
 * @author: Ignat Alexeyenko
 * @url: http://alexeyenko.net
 * Date: Nov 13, 2010
 */
public class MenuPresenter implements Presenter {

    private EventBus eventBus;

    private HasWidgets menuContainer;

    private Display display;

    private CommandExecutorServiceAsync service;

    public MenuPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void go(HasWidgets container) {
        this.menuContainer = container;
        this.menuContainer.clear();
        bindHandlers();
    }

    private void bindHandlers() {
        this.display.getLogoutButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent clickEvent) {
                service.execute(new LogoutAction(), new AsyncCallbackAdaptor<LogoutResponse>() {

                    @Override
                    public void onSuccess(LogoutResponse logoutResponse) {
                        eventBus.fireEvent(new LogoutEvent());
                    }
                });
            }
        });
        this.display.getProjectsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new TabChangeEvent(new ProjectsTabType(eventBus)));
            }
        });
        this.display.getUsersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new TabChangeEvent(new UsersTabType()));
            }
        });
        this.display.getWorklogsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new TabChangeEvent(new WorklogsTabType()));
            }
        });
        this.eventBus.addHandler(ShowMenuEvent.TYPE, new ShowMenuHandler() {

            @Override
            public void onMenuShow(ShowMenuEvent showMenuEvent) {
                menuContainer.clear();
                menuContainer.add(display.asWidget());
            }
        });
        this.eventBus.addHandler(HideMenuEvent.TYPE, new HideMenuHandler() {

            @Override
            public void onHideMenu(HideMenuEvent hideMenuEvent) {
                menuContainer.clear();
            }
        });
    }

    public void setService(CommandExecutorServiceAsync service) {
        this.service = service;
    }

    public void bindDisplay(Display menuDisplay) {
        this.display = menuDisplay;
    }

    public interface Display extends IsWidget {

        HasClickHandlers getLogoutButton();

        HasClickHandlers getProjectsButton();

        HasClickHandlers getUsersButton();

        HasClickHandlers getWorklogsButton();
    }
}
