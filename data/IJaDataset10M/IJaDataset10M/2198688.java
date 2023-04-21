package org.tzi.wr;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Psi
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DeviceFactory {

    public static BtDevice make(ByteArrayInputStream aIn) {
        BtDevice device = null;
        DataInputStream inData = new DataInputStream(aIn);
        try {
            String classname = inData.readUTF();
            try {
                Class c = Class.forName(classname);
                try {
                    device = (BtDevice) c.newInstance();
                    device.setData(aIn);
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                }
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return device;
    }
}
