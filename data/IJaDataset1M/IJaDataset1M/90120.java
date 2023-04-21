package joliex.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import jolie.runtime.FaultException;
import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

/**
 *
 * @author claudio
 */
public class NetworkService extends JavaService {

    public Value getNetworkInterfaceNames(Value request) throws FaultException {
        Value response = Value.create();
        ValueVector interfaces = response.getChildren("interfaceName");
        try {
            Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
            int index = 0;
            while (list.hasMoreElements()) {
                NetworkInterface n = list.nextElement();
                interfaces.get(index).setValue(n.getName());
                index++;
            }
        } catch (SocketException e) {
            throw new FaultException(e);
        }
        return response;
    }

    public Value getIPAddresses(Value request) throws FaultException {
        Value response = Value.create();
        try {
            Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
            boolean found = false;
            while (list.hasMoreElements()) {
                NetworkInterface n = list.nextElement();
                if (n.getName().equals(request.getFirstChild("interfaceName").strValue())) {
                    found = true;
                    Enumeration<InetAddress> ad = n.getInetAddresses();
                    while (ad.hasMoreElements()) {
                        InetAddress ia = ad.nextElement();
                        if (ia.getHostName().contains(".")) {
                            response.getFirstChild("ip4").setValue(ia.getHostName());
                        } else {
                            response.getFirstChild("ip6").setValue(ia.getHostName());
                        }
                    }
                }
            }
            if (!found) {
                throw new FaultException("InterfaceNotFound", new Exception());
            }
        } catch (SocketException e) {
            throw new FaultException(e);
        }
        return response;
    }
}
