package wm;

import java.util.List;
import java.util.Map;
import org.red5.server.api.IAttributeStore;
import org.red5.server.api.so.ISharedObjectBase;
import org.red5.server.api.so.ISharedObjectListener;

public class ChatSharedObjectListener implements ISharedObjectListener {

    @Override
    public void onSharedObjectClear(ISharedObjectBase so) {
    }

    @Override
    public void onSharedObjectConnect(ISharedObjectBase so) {
    }

    @Override
    public void onSharedObjectDelete(ISharedObjectBase so, String key) {
    }

    @Override
    public void onSharedObjectDisconnect(ISharedObjectBase so) {
    }

    @Override
    public void onSharedObjectSend(ISharedObjectBase so, String method, List<?> params) {
    }

    @Override
    public void onSharedObjectUpdate(ISharedObjectBase so, IAttributeStore values) {
    }

    @Override
    public void onSharedObjectUpdate(ISharedObjectBase so, Map<String, Object> values) {
    }

    @Override
    public void onSharedObjectUpdate(ISharedObjectBase so, String key, Object value) {
    }
}
