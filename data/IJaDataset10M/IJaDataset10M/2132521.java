package com.monad.homerun.pkg.x10;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import com.monad.homerun.core.GlobalProps;
import com.monad.homerun.objmgt.RuntimeContext;
import com.monad.homerun.objmgt.impl.SimpleObject;
import com.monad.homerun.objmgt.impl.DeviceHandlerRegistry;

/**
 * X10Device is the abstract base class for all x10 devices & pseudo-devices
 *
 */
public abstract class X10Device extends SimpleObject {

    protected X10Handler handler = null;

    protected String houseCode = null;

    protected int deviceCode = 0;

    private Map<String, String> propMap = null;

    private long loadTime = 0L;

    public X10Device() {
        super();
        propMap = new HashMap<String, String>();
    }

    public void init(Properties props, RuntimeContext context) {
        super.init(props, context);
        loadTime = System.currentTimeMillis();
        houseCode = props.getProperty("houseCode");
        deviceCode = Integer.parseInt(props.getProperty("deviceCode"));
        handler = (X10Handler) DeviceHandlerRegistry.getHandler(props.getProperty("handler"));
        if (GlobalProps.DEBUG) {
            System.out.println("Device.init exit");
        }
    }

    public void start() {
        if (handler != null) {
            if (!handler.addDevice(this)) {
                if (GlobalProps.DEBUG) {
                    System.out.println("start: couldn't add device to handler");
                }
            } else {
                if (GlobalProps.DEBUG) {
                    System.out.println("start: added device to handler");
                }
            }
        } else {
            if (GlobalProps.DEBUG) {
                System.out.println("start: handler is null");
            }
        }
    }

    public long getLoadTime() {
        return loadTime;
    }

    public String getProperty(String name) {
        return propMap.get(name);
    }

    public void setProperty(String name, String value) {
        propMap.put(name, value);
    }

    public String getHouseCode() {
        return houseCode;
    }

    public int getDeviceCode() {
        return deviceCode;
    }

    public abstract void onEvent(String address, Object event);
}
