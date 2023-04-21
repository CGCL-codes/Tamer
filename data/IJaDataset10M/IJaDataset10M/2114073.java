package org.nicocube.airain.app.client;

import java.util.List;
import org.nicocube.airain.app.client.domain.GameCharacter;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GCServiceAsync {

    void getList(AsyncCallback<List<GameCharacter>> callback);

    void create(GameCharacter gc, AsyncCallback<GameCharacter> callback);

    void delete(GameCharacter gc, AsyncCallback<Boolean> callback);
}
