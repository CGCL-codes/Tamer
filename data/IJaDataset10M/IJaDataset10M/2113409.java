package com.factorit.utils;

import java.net.InetAddress;
import java.security.SecureRandom;

public class KeyGenerator {

    public String getAphaNumKey() {
        StringBuffer sReturnBuffer = new StringBuffer();
        String strTemp = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            byte[] ipaddr = addr.getAddress();
            for (int i = 0; i < ipaddr.length; i++) {
                Byte b = new Byte(ipaddr[i]);
                strTemp = Integer.toHexString(b.intValue() & 0x000000ff);
                while (strTemp.length() < 2) {
                    strTemp = '0' + strTemp;
                }
                sReturnBuffer.append(strTemp);
            }
            sReturnBuffer.append("-");
            strTemp = Long.toHexString(System.currentTimeMillis());
            while (strTemp.length() < 12) {
                strTemp = '0' + strTemp;
            }
            sReturnBuffer.append(strTemp);
            sReturnBuffer.append('-');
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            strTemp = Integer.toHexString(prng.nextInt());
            while (strTemp.length() < 8) {
                strTemp = '0' + strTemp;
            }
            sReturnBuffer.append(strTemp.substring(4));
            sReturnBuffer.append('-');
            strTemp = Long.toHexString(System.identityHashCode((Object) this));
            while (strTemp.length() < 8) {
                strTemp = '0' + strTemp;
            }
            sReturnBuffer.append(strTemp);
        } catch (java.net.UnknownHostException ex) {
            System.err.println("Unknown Host Exception Caught: " + ex.getMessage());
        } catch (java.security.NoSuchAlgorithmException ex) {
            System.err.println("No Such Algorithm Exception Caught: " + ex.getMessage());
        }
        return sReturnBuffer.toString().toUpperCase();
    }
}
