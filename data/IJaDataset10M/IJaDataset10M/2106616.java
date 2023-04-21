package com.jw.mos.system;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.jw.mos.system.exception.MoSGuiException;

/**
 * @Title Util.
 * @Description 
 * @Author Jacek Wisniewski
 * @Date 2008-06-11 01:07:56
 */
public class Util {

    public static String getHostIpAddress() throws MoSGuiException {
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            throw new MoSGuiException("Unalbe to determine loacal machine IP address.");
        }
        return ip;
    }
}
