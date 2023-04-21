package org.openmim.mn2;

import org.openmim.infrastructure.Context;
import org.openmim.mn2.controller.MN2Factory;
import org.openmim.mn2.model.StatusRoomListenerExternal;
import org.openmim.mn2.controller.IMNetwork;
import org.openmim.mn2.model.IMListener;
import java.util.List;

public interface MessagingNetwork2 {

    MN2Factory getFactory();

    void setListener(MessagingNetwork2Listener listener);

    void load(Context context, IMListener imListener, StatusRoomListenerExternal statusRoomListenerExternal);

    void startReconnecting();

    public List<IMNetwork> getIMNetworks();

    boolean supportsChannels();

    String getDisplayName();
}
