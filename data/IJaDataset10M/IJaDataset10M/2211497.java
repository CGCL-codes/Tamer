package org.commsuite.sap;

import java.util.Collection;
import java.util.Map;
import org.commsuite.model.Message;
import org.commsuite.model.SAPInstanceDef;
import org.commsuite.notification.INotificationsManager;

/**
 * @since 1.0
 * @author Marcin Zduniak
 * @author Szymon Kuzniak
 */
public interface ISAPCommManager {

    public abstract Map<String, ISAPComm> getSapComms();

    public abstract ISAPComm getSAPCommByName(String name);

    public abstract boolean isSapServerWorking(String name);

    public abstract void sendMessageToDefault(Message message);

    public abstract void init();

    public abstract void destroy();

    public abstract void setSapInstances(Collection<SAPInstanceDef> sapInstances);

    public void setSapInstance(SAPInstanceDef instance);

    public abstract void startInstance(String instanceName);

    public abstract void stopInstance(String instanceName);

    public abstract void destroyInstance(String instanceName);

    public abstract void setNotificationsManager(INotificationsManager notificaionManager);

    public abstract boolean isEnabled();
}
