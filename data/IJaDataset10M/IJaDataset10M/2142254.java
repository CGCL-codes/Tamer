package ch.ntb.usb;

public class Utils {

    public static void logBus(Usb_Bus bus) {
        Usb_Bus usb_Bus = bus;
        while (usb_Bus != null) {
            System.out.println(usb_Bus.toString());
            Usb_Device dev = usb_Bus.getDevices();
            while (dev != null) {
                System.out.println("\t" + dev.toString());
                Usb_Device_Descriptor defDesc = dev.getDescriptor();
                System.out.println("\t\t" + defDesc.toString());
                Usb_Config_Descriptor[] confDesc = dev.getConfig();
                for (int i = 0; i < confDesc.length; i++) {
                    System.out.println("\t\t" + confDesc[i].toString());
                    Usb_Interface[] int_ = confDesc[i].getInterface();
                    if (int_ != null) {
                        for (int j = 0; j < int_.length; j++) {
                            System.out.println("\t\t\t" + int_[j].toString());
                            Usb_Interface_Descriptor[] intDesc = int_[j].getAltsetting();
                            if (intDesc != null) {
                                for (int k = 0; k < intDesc.length; k++) {
                                    System.out.println("\t\t\t\t" + intDesc[k].toString());
                                    Usb_Endpoint_Descriptor[] epDesc = intDesc[k].getEndpoint();
                                    if (epDesc != null) {
                                        for (int e = 0; e < epDesc.length; e++) {
                                            System.out.println("\t\t\t\t\t" + epDesc[e].toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                dev = dev.getNext();
            }
            usb_Bus = usb_Bus.getNext();
        }
    }
}
