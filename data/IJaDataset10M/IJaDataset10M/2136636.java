package org.saintandreas.serket.impl.av;

import org.saintandreas.serket.Serket;
import org.saintandreas.serket.device.BaseDevice;
import org.saintandreas.serket.device.DeviceType;
import org.saintandreas.serket.device.Manufacturer;
import org.saintandreas.serket.device.Model;
import org.saintandreas.util.NetUtil;

public class SerketMediaServer extends BaseDevice {

    private static class SerketModel implements Model {

        @Override
        public String getDescription() {
            return "Serket Media Server reference implementation";
        }

        @Override
        public String getName() {
            return "Serket AV Server";
        }

        @Override
        public String getNumber() {
            return Serket.VERSION;
        }

        @Override
        public String getUPC() {
            return null;
        }

        @Override
        public String getURL() {
            return "http://code.google.com/p/serket/";
        }
    }

    private static class SerketManufacturer implements Manufacturer {

        @Override
        public String getName() {
            return "Saint Andreas";
        }

        @Override
        public String getURL() {
            return Serket.PROJECT_URL;
        }
    }

    public SerketMediaServer(String UDN, String presentationURL) {
        super("Serket AV " + NetUtil.getHostname(), UDN, "", presentationURL, new SerketManufacturer(), new SerketModel());
    }

    @Override
    public DeviceType getDeviceTypeEnum() {
        return DeviceType.MediaServer;
    }
}
