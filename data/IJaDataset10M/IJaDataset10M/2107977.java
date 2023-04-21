package org.freem.love.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import org.freem.love.client.beans.*;
import org.freem.love.client.atomwidgets.SearchDataBean;
import java.util.List;

public interface UsersService extends RemoteService {

    public static class App {

        private static UsersServiceAsync ourInstance = null;

        public static synchronized UsersServiceAsync getInstance() {
            if (ourInstance == null) {
                ourInstance = (UsersServiceAsync) GWT.create(UsersService.class);
                ((ServiceDefTarget) ourInstance).setServiceEntryPoint(GWT.getModuleBaseURL() + "org.freem.love.Hello/UsersService");
            }
            return ourInstance;
        }
    }

    public void registerUser(UserRegistrationDataBean user);

    public UserMenuWidgetDataBean getUserMenuWidgetData();

    public MessagesWidgetDataBean getMessagesWidgetData();

    public DialogWidgetDataBean getDialogWidgetDataBean(String withUser);

    public void addMessage(String text, String toUser);

    public List findUsers(SearchDataBean searchData);

    public ProfileWidgetDataBean getProfileWidgetDataBean(String withUser);
}
