package com.android.im.imps;

import java.util.Map;
import android.os.RemoteException;
import com.android.im.engine.ImException;
import com.android.im.plugin.ImPluginConstants;
import com.android.im.plugin.PresenceMapping;
import dalvik.system.PathClassLoader;

public class CustomPresenceMapping implements PresenceMapping {

    private PresenceMapping mPresenceMapping;

    public CustomPresenceMapping(String pluginPath, String implClass) throws ImException {
        PathClassLoader classLoader = new PathClassLoader(pluginPath, getClass().getClassLoader());
        try {
            Class<?> cls = classLoader.loadClass(implClass);
            mPresenceMapping = (PresenceMapping) cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new ImException(e);
        } catch (IllegalAccessException e) {
            throw new ImException(e);
        } catch (InstantiationException e) {
            throw new ImException(e);
        }
    }

    public Map<String, Object> getExtra(int status) {
        return mPresenceMapping.getExtra(status);
    }

    public boolean getOnlineStatus(int status) {
        return mPresenceMapping.getOnlineStatus(status);
    }

    public int getPresenceStatus(boolean onlineStatus, String userAvailability, Map<String, Object> allValues) {
        return mPresenceMapping.getPresenceStatus(onlineStatus, userAvailability, allValues);
    }

    public int[] getSupportedPresenceStatus() {
        return mPresenceMapping.getSupportedPresenceStatus();
    }

    public String getUserAvaibility(int status) {
        return mPresenceMapping.getUserAvaibility(status);
    }

    public boolean requireAllPresenceValues() {
        return mPresenceMapping.requireAllPresenceValues();
    }
}
