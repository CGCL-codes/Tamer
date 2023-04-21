package com.izforge.izpack.util;

import com.izforge.izpack.panels.ProcessingClient;
import com.izforge.izpack.panels.Validator;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * A validator to check whether a port is available (free) on the localhost.
 * <p/>
 * This validator can be used for rule input fields in the UserInputPanel to make sure that the port
 * the user entered is not in use.
 *
 * @author thorque
 */
public class PortValidator implements Validator {

    public boolean validate(ProcessingClient client) {
        InetAddress inet = null;
        String host = "localhost";
        boolean retValue = false;
        int numfields = client.getNumFields();
        for (int i = 0; i < numfields; i++) {
            String value = client.getFieldContents(i);
            if ((value == null) || (value.length() == 0)) {
                return false;
            }
            try {
                inet = InetAddress.getByName(host);
                ServerSocket socket = new ServerSocket(Integer.parseInt(value), 0, inet);
                retValue = socket.getLocalPort() > 0;
                if (!retValue) {
                    break;
                }
                socket.close();
            } catch (Exception ex) {
                retValue = false;
            }
        }
        return retValue;
    }
}
