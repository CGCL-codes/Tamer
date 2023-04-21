package booksandfilms.client.presenter;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import booksandfilms.client.QueryServiceAsync;
import booksandfilms.client.entities.UserAccount;
import booksandfilms.client.event.UserAddEvent;
import booksandfilms.client.event.UserDeleteEvent;
import booksandfilms.client.event.UserDeleteEventHandler;
import booksandfilms.client.event.UserUpdatedEvent;
import booksandfilms.client.event.UserUpdatedEventHandler;
import booksandfilms.client.event.ShowUserPopupEvent;
import booksandfilms.client.helper.ClickPoint;
import booksandfilms.client.helper.RPCCall;

public class UserListPresenter implements Presenter {

    private List<UserAccount> users;

    private List<UserAccount> someUsers = new ArrayList<UserAccount>();

    public interface Display {

        HasClickHandlers getAddButton();

        HasClickHandlers getList();

        HasKeyPressHandlers getSearchBox();

        String getSearchValue();

        void setData(List<UserAccount> userNames);

        int getClickedRow(ClickEvent event);

        ClickPoint getClickedPoint(ClickEvent event);

        Widget asWidget();
    }

    private final QueryServiceAsync rpcService;

    private final SimpleEventBus eventBus;

    private final Display display;

    public UserListPresenter(QueryServiceAsync rpcService, SimpleEventBus eventBus, Display view) {
        this.rpcService = rpcService;
        this.eventBus = eventBus;
        this.display = view;
        bind();
    }

    public void bind() {
        display.getAddButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new UserAddEvent());
            }
        });
        display.getSearchBox().addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    searchBoxChange();
                }
            }
        });
        if (display.getList() != null) display.getList().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                int selectedPropertyButtonRow = display.getClickedRow(event);
                if (selectedPropertyButtonRow >= 0) {
                    ClickPoint point = display.getClickedPoint(event);
                    UserAccount user = users.get(selectedPropertyButtonRow);
                    eventBus.fireEvent(new ShowUserPopupEvent(user, point));
                }
            }
        });
        eventBus.addHandler(UserUpdatedEvent.TYPE, new UserUpdatedEventHandler() {

            @Override
            public void onUpdateUser(UserUpdatedEvent event) {
                fetchUserSummary();
            }
        });
        eventBus.addHandler(UserDeleteEvent.TYPE, new UserDeleteEventHandler() {

            @Override
            public void onDeleteUser(UserDeleteEvent event) {
                fetchUserSummary();
            }
        });
    }

    public void go(final HasWidgets container) {
        container.clear();
        container.add(display.asWidget());
        fetchUserSummary();
    }

    public void setUserSummary(List<UserAccount> users) {
        this.users = users;
    }

    public UserAccount getUserSummary(int index) {
        return users.get(index);
    }

    private void fetchUserSummary() {
        new RPCCall<ArrayList<UserAccount>>() {

            @Override
            protected void callService(AsyncCallback<ArrayList<UserAccount>> cb) {
                rpcService.getAllUsers(cb);
            }

            @Override
            public void onSuccess(ArrayList<UserAccount> result) {
                users = result;
                display.setData(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error fetching user summaries: " + caught.getMessage());
            }
        }.retry(3);
    }

    private void searchBoxChange() {
        String searchString = display.getSearchValue();
        someUsers.clear();
        if (searchString.isEmpty()) {
            display.setData(users);
        } else {
            for (UserAccount a : users) {
                if (a.getName().toUpperCase().startsWith(searchString.toUpperCase())) someUsers.add(a);
            }
            display.setData(someUsers);
        }
    }
}
