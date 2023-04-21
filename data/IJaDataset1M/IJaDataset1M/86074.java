package securus.client.system;

import java.io.File;
import securus.client.*;

/**
 *
 * @author e.dovgopoliy
 */
public class Devices {

    public enum DeviceType {

        PRIVATE, PUBLIC, SHARED
    }

    ;

    public static String getName(DeviceType index) {
        switch(index) {
            case PRIVATE:
                if (MainApp.getDevice() != null) {
                    return MainApp.getDevice().getDeviceName();
                } else {
                    return MainApp.getString("NAME_OF_DEVICE_PRIVATE");
                }
            case SHARED:
                return MainApp.getString("NAME_OF_DEVICE_SHARED");
            case PUBLIC:
                return MainApp.getString("NAME_OF_DEVICE_SYNC");
            default:
                return MainApp.getString("NOT_IMPLEMENTED_YET");
        }
    }

    public static String getToolTipText(DeviceType index) {
        switch(index) {
            case PRIVATE:
                return MainApp.getString("TOOLTIP_PRIVATE");
            case SHARED:
                return MainApp.getString("TOOLTIP_SHARED");
            case PUBLIC:
                return MainApp.getString("TOOLTIP_PUBLIC");
            default:
                return MainApp.getString("NOT_IMPLEMENTED_YET");
        }
    }

    public static File[] getDeviceRoot(DeviceType index) {
        File[] result = new File[0];
        switch(index) {
            case PRIVATE:
                result = File.listRoots();
                break;
            case SHARED:
                File sharedFolder = new File(MainApp.getSetting().getSharedPath());
                result = sharedFolder.listFiles();
                break;
            case PUBLIC:
                File publicFolder = new File(MainApp.getSetting().getPublicPath());
                result = publicFolder.listFiles();
                break;
        }
        return result;
    }
}
