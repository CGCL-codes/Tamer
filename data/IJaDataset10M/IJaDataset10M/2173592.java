package booksandfilms.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UserListChangedEventHandler extends EventHandler {

    void onChangeUserList(UserListChangedEvent event);
}
