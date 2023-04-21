package org.odiem.stacks.unbounid;

import java.util.Properties;
import org.odiem.OdmDriverManager;
import org.odiem.api.OdmDriver;
import org.odiem.api.OdmStackFactory;
import org.odiem.sdk.beans.OdmStackPropertyInfo;
import org.odiem.sdk.exceptions.OdmException;

public class DriverImpl implements OdmDriver {

    public static final String DRIVER_NAME = "unboundid.odm.driver";

    public enum PROPS {

        AUTO_RECONNECT("unboundid.connection.autoreconnect", "true"), POOL_SIZE("unboundid.connection.poll.size", "1");

        private String propName;

        private String defaultValue;

        private PROPS(String propName, String defaultValue) {
            this.propName = propName;
            this.defaultValue = defaultValue;
        }

        public String getPropName() {
            return propName;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    private OdmStackPropertyInfo[] propertyInfos;

    static {
        OdmDriverManager.registerDriver(new DriverImpl());
    }

    @Override
    public String getName() {
        return DRIVER_NAME;
    }

    @Override
    public OdmStackFactory getStackFactory(Properties properties) throws OdmException {
        return new StackFactoryImpl(properties);
    }

    @Override
    public OdmStackPropertyInfo[] getStackFactoryPropertyInfo() {
        if (propertyInfos == null) {
            OdmStackPropertyInfo prop1 = new OdmStackPropertyInfo(PROPS.AUTO_RECONNECT.propName, false, "Autoreconnect on connection", new String[] { "true", "false" }, PROPS.AUTO_RECONNECT.defaultValue);
            OdmStackPropertyInfo prop2 = new OdmStackPropertyInfo(PROPS.POOL_SIZE.propName, false, "use connection pool if > 1", new String[] { "1", "...", "n" }, PROPS.POOL_SIZE.defaultValue);
            propertyInfos = new OdmStackPropertyInfo[] { prop1, prop2 };
        }
        return propertyInfos;
    }
}
