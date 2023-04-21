package org.jiopi.ibean.module.remote.client;

import org.jiopi.blueprint.remote.RemoteJIOPi;
import org.jiopi.blueprint.remote.RemoteReference;
import org.jiopi.framework.annotation.module.Instantiation;
import org.jiopi.framework.annotation.module.InstanceType;
import org.jiopi.framework.annotation.module.RegisterModule;

/**
 * 
 * 
 * @version 0.5 2010.10.31
 * @since 0.5 2010.10.31
 *
 */
@RegisterModule
@Instantiation(type = InstanceType.CONFIGURATION_SINGLETON)
public class IBeanRemoteJIOPiClient implements RemoteJIOPi {

    public RemoteReference accessRemoteControlPanel(String moduleName, String compatibleVersion, String registerName, Object... args) {
        return null;
    }

    public RemoteReference accessRemoteControlPanelStrict(String moduleName, String compatibleVersion, String registerName, Class<?>[] parameterTypes, Object... args) {
        return null;
    }

    public Object operateJIOPiRemoteStrict(String moduleName, String compatibleVersion, String className, long objectID, String methodName, Object self, boolean transportSelf, boolean receiveSelf, Class<?>[] parameterTypes, Object... args) {
        return null;
    }

    public Object operateRemoteStrict(long objectID, String methodName, Object self, boolean transportSelf, boolean receiveSelf, Class<?>[] parameterTypes, Object... args) {
        return null;
    }
}
