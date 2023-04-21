package axt.axtBridge;

import org.jboss.system.ServiceMBeanSupport;
import axt.socketBridge.SocketBridge;

public class AxtListener extends ServiceMBeanSupport implements AxtListenerMBean {

    public AxtListener() {
        SocketBridge.startSocket();
    }
}
