package net.sourceforge.ikms.util.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 网络（Network） 相关操作公共类
 * 
 * @author <b>oxidy</b>, Copyright &#169; 2003
 * @since 03 September 2011
 * 
 */
public class NetworkUtils {

    /**
	 * 取得本机IP（可能有多个网卡，Linux和Windows都适用）
	 * 
	 * @return List
	 */
    public static List<?> getAllLocalIP() {
        List<String> localServers = new ArrayList<String>();
        String regExp = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
        try {
            Enumeration<?> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration<?> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = (InetAddress) address.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().matches(regExp)) {
                        localServers.add(ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return localServers;
    }
}
