package ch.unizh.ini.jaer.projects.opticalflow.usbinterface;

import cl.eye.PSeye_OpticalFlowHardwareInterface;
import cl.eye.CLCamera;
import cl.eye.CLRetinaHardwareInterface;
import net.sf.jaer.hardwareinterface.*;
import de.thesycon.usbio.*;
import de.thesycon.usbio.structs.*;
import java.util.*;
import net.sf.jaer.hardwareinterface.usb.UsbIoUtilities;

/**
 * Makes OpticalFlowHardwareInterface's.
 * 
 * @author tobi
 * 
 * changes by andstein
 * <ul>
 * <li>added the new <cdoe>dsPIC33F_COM_ConfigurationPanel</code></li>
 * <li>added error message in <code>buildUsbIoList</code> that can arise when
 *     different driver version is used</li>
 * <li>added psEYE hardware interface for comparison reasons</li>
 * </ul>
 */
public class OpticalFlowHardwareInterfaceFactory implements UsbIoErrorCodes, PnPNotifyInterface, HardwareInterfaceFactoryInterface {

    private static OpticalFlowHardwareInterfaceFactory instance = new OpticalFlowHardwareInterfaceFactory();

    private String GUID = SiLabsC8051F320_OpticalFlowHardwareInterface.GUID;

    PnPNotify pnp = null;

    /** the UsbIo interface to the device. This is assigned when this particular instance is opened, after enumerating all devices */
    private UsbIo gUsbIo = null;

    private long gDevList;

    ArrayList<UsbIo> usbioList = null;

    MotionChipInterface[] psEyes;

    HardwareInterface cache[];

    /** private constructor for this singleton class.*/
    private OpticalFlowHardwareInterfaceFactory() {
        UsbIoUtilities.enablePnPNotification(this, GUID);
        buildUsbIoList();
        buildPsEyeList();
        emptyCache();
    }

    private void emptyCache() {
        cache = new HardwareInterface[getNumInterfacesAvailable()];
        for (int i = 0; i < getNumInterfacesAvailable(); i++) cache[i] = null;
    }

    /** @return singleton instance */
    public static HardwareInterfaceFactoryInterface instance() {
        return instance;
    }

    public int getNumInterfacesAvailable() {
        int ret = 1;
        if (usbioList != null) ret += usbioList.size();
        if (psEyes != null) ret += psEyes.length;
        return ret;
    }

    public HardwareInterface getFirstAvailableInterface() throws HardwareInterfaceException {
        if (getNumInterfacesAvailable() == 0) throw new HardwareInterfaceException("no interfaces available");
        return getInterface(0);
    }

    private HardwareInterface createInterface(int n) throws HardwareInterfaceException {
        if (n < usbioList.size()) return new SiLabsC8051F320_OpticalFlowHardwareInterface(n);
        n -= usbioList.size();
        if (n < psEyes.length) return psEyes[n];
        return new dsPIC33F_COM_OpticalFlowHardwareInterface();
    }

    public HardwareInterface getInterface(int n) throws HardwareInterfaceException {
        if (n >= getNumInterfacesAvailable()) throw new HardwareInterfaceException("asked for interface " + n + " but only " + getNumInterfacesAvailable() + " interfaces are available");
        if (cache[n] == null) cache[n] = createInterface(n);
        return cache[n];
    }

    public void onAdd() {
        buildUsbIoList();
        emptyCache();
    }

    public void onRemove() {
        buildUsbIoList();
        emptyCache();
    }

    void buildPsEyeList() {
        if (CLCamera.isLibraryLoaded()) {
            psEyes = new MotionChipInterface[CLCamera.cameraCount()];
            for (int i = 0; i < psEyes.length; i++) psEyes[i] = new PSeye_OpticalFlowHardwareInterface(i);
        }
    }

    void buildUsbIoList() {
        usbioList = new ArrayList<UsbIo>();
        final int MAXDEVS = 8;
        UsbIo dev;
        gDevList = UsbIo.createDeviceList(GUID);
        int numDevs = 0;
        for (int i = 0; i < MAXDEVS; i++) {
            dev = new UsbIo();
            int status = dev.open(i, gDevList, GUID);
            if (status == USBIO_ERR_NO_SUCH_DEVICE_INSTANCE) {
                numDevs = i;
                break;
            } else if (status != USBIO_ERR_SUCCESS) {
                System.err.println("OpticalFlowHardwareInterfaceFactory.openUsbIo(): open: " + UsbIo.errorText(status));
            } else {
                USB_DEVICE_DESCRIPTOR deviceDescriptor = new USB_DEVICE_DESCRIPTOR();
                status = dev.getDeviceDescriptor(deviceDescriptor);
                if (status != USBIO_ERR_SUCCESS) {
                    UsbIo.destroyDeviceList(gDevList);
                    System.err.println("OpticalFlowHardwareInterfaceFactory.openUsbIo(): getDeviceDescriptor: " + UsbIo.errorText(status));
                } else {
                    usbioList.add(dev);
                }
                dev.close();
            }
        }
        UsbIo.destroyDeviceList(gDevList);
    }

    public Collection<HardwareInterface> getInterfaceList() throws HardwareInterfaceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getGUID() {
        return GUID;
    }
}
