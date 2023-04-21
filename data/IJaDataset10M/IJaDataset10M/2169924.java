package com.shine.framework.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 组网工具类
 * 
 * @author viruscodecn@gmail.com
 * 
 */
public class NetworkUtils {

    private static final long ipTotal = Long.parseLong("4294967295");

    /**
	 * A address
	 */
    private static final long aAddressStart = NetworkUtils.ipToLong("10.0.0.0");

    private static final long aAddressEnd = NetworkUtils.ipToLong("10.255.255.255");

    /**
	 * B address
	 */
    private static final long bAddressStart = NetworkUtils.ipToLong("172.16.0.0");

    private static final long bAddressEnd = NetworkUtils.ipToLong("172.31.255.255");

    /**
	 * C address
	 */
    private static final long cAddressStart = NetworkUtils.ipToLong("192.168.0.0");

    private static final long cAddressEnd = NetworkUtils.ipToLong("192.168.255.255");

    /**
	 * 是否是私有地址
	 * 
	 * @param ipLong
	 * @return
	 */
    public static boolean isPrivateAddress(long ipLong) {
        if (ipLong >= aAddressStart && ipLong <= aAddressEnd) return true;
        if (ipLong >= bAddressStart && ipLong <= bAddressEnd) return true;
        if (ipLong >= cAddressStart && ipLong <= cAddressEnd) return true;
        return false;
    }

    /**
	 * convert a standard ip to a decimal ip 把标准的ip转换成long型
	 * 
	 * @param ipAddress
	 * @return
	 */
    public static long ipToLong(final String ipAddress) {
        int[] ipSegment = parseIp(ipAddress);
        if (ipSegment == null) return 0;
        long longIp = 0;
        int k = 24;
        for (int i = 0; i < ipSegment.length; i++) {
            longIp += ((long) ipSegment[i]) << k;
            k -= 8;
        }
        return longIp;
    }

    /**
	 * convert a decimal ip to a standard ip 把long的ip转换成标准ip
	 * 
	 * @param ip
	 * @return
	 */
    public static String longToIp(final long ip) {
        int b[] = new int[4];
        b[0] = (int) (ip >> 24 & 255L);
        b[1] = (int) (ip >> 16 & 255L);
        b[2] = (int) (ip >> 8 & 255L);
        b[3] = (int) (ip & 255L);
        return b[0] + "." + b[1] + "." + b[2] + "." + b[3];
    }

    /**
	 * parse an ip to four ip segments
	 */
    public static int[] parseIp(final String ipAddress) {
        if (!checkIp(ipAddress)) return null;
        int[] ipSegment = new int[4];
        StringTokenizer st = new StringTokenizer(ipAddress, ".");
        for (int i = 0; i < 4; i++) ipSegment[i] = Integer.parseInt(st.nextToken());
        return ipSegment;
    }

    /**
	 * identify an ip is valid 检查是否是无效ip
	 * 
	 * @param ipAddress
	 * @return
	 */
    public static boolean checkIp(final String ipAddress) {
        boolean isValid = true;
        try {
            StringTokenizer st = new StringTokenizer(ipAddress, ".");
            int len = st.countTokens();
            if (len != 4) return false;
            int ipSegment = 0;
            for (int i = 0; i < len; i++) {
                ipSegment = Integer.parseInt(st.nextToken());
                if (ipSegment < 0 || ipSegment > 255) {
                    isValid = false;
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return isValid;
    }

    /**
	 * identify an ip and its netmask is valid used in router table
	 */
    public static boolean isNetAddress(String ipAddress, String netMask) {
        if ("0.0.0.0".equals(ipAddress) || "255.255.255.255".equals(netMask)) return false;
        int[] ips = parseIp(ipAddress);
        int[] masks = parseIp(netMask);
        String result = null;
        for (int i = 0; i < 4; i++) {
            if (result == null) result = "" + (ips[i] & masks[i]); else result += "." + (ips[i] & masks[i]);
        }
        if (result.equals(ipAddress)) return true;
        return false;
    }

    /**
	 * get ip total according to a netmask 获取网关的网关的ip数量
	 * 
	 * @param netMask
	 * @return
	 */
    public static int getSubnetIPTotal(final String netMask) {
        int[] masks = parseIp(netMask);
        if (masks == null) return 0;
        int ipTotal = 0;
        for (int i = 0; i < 4; i++) {
            if (masks[i] != 255) {
                if (i == 2) ipTotal = (255 - masks[i]) * 256 + 256; else if (i == 3) ipTotal = 256 - masks[i];
                break;
            }
        }
        return ipTotal - 2;
    }

    /**
	 * identify an ip is in a effective range, the range is defined by the given
	 * netmask and network address
	 */
    public static boolean isValidIP(long netAddress, long netMask, String ipAddress) {
        long ipAddressLong = ipToLong(ipAddress);
        long ipTotalOfSubnet = ipTotal - netMask;
        if (ipAddressLong > netAddress && ipAddressLong < netAddress + ipTotalOfSubnet) return true;
        return false;
    }

    public static boolean isValidIP(String netAddress, String netMask, String ipAddress) {
        return isValidIP(ipToLong(netAddress), ipToLong(netMask), ipAddress);
    }

    /**
	 * 分析ip是否属于该ip范围
	 * 
	 * @param ip
	 * @param startIp
	 * @param endIp
	 * @return
	 */
    public static boolean ipInRange(String ip, String startIp, String endIp) {
        long ipLong = ipToLong(ip);
        long startIpLong = ipToLong(startIp);
        long endIpLong = ipToLong(endIp);
        return ipInRange(ipLong, startIpLong, endIpLong);
    }

    /**
	 * 分析ip是否属于该ip范围
	 * 
	 * @param ip
	 * @param startIp
	 * @param endIp
	 * @return
	 */
    public static boolean ipInRange(long ip, long startIp, long endIp) {
        if (ip >= startIp && ip <= endIp) return true;
        return false;
    }

    /**
	 * 获取本地ip
	 * 
	 * @return
	 */
    public static String getLocalIP() {
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ia = (InetAddress) ips.nextElement();
                    if (!ia.isLoopbackAddress() && checkIp(ia.getHostAddress())) return ia.getHostAddress();
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

    public static String getHexAddress(String address) {
        if (address == null) return null;
        String hexAddress = null;
        String[] adds = address.split("\\.");
        for (String sip : adds) {
            String digit = Integer.toHexString(new Integer(sip));
            if (digit.length() == 1) digit = "0" + digit;
            if (hexAddress == null) hexAddress = digit; else hexAddress += ":" + digit;
        }
        return hexAddress.toUpperCase();
    }

    public static String pingReport(String ipAddress) {
        return (String) ping(ipAddress, true);
    }

    /**
	 * use ping command from native os
	 */
    public static Object ping(String ipAddress, boolean report) {
        Process process = null;
        BufferedReader in = null;
        String pingInfo = null;
        int result = 0;
        try {
            String cmd = null;
            if (System.getProperty("os.name").indexOf("Windows") != -1) cmd = "ping -n 2 " + ipAddress; else cmd = "ping -c 2 " + ipAddress;
            process = Runtime.getRuntime().exec(cmd);
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer sb = new StringBuffer(300);
            sb.append("From ").append(getLocalIP()).append(" ");
            String line = null;
            while ((line = in.readLine()) != null) sb.append(line);
            pingInfo = sb.toString();
        } catch (IOException ioe) {
            pingInfo = null;
        } finally {
            try {
                process.destroy();
                in.close();
            } catch (Exception e) {
            }
        }
        if (pingInfo == null || pingInfo.indexOf("100% packet loss") != -1 || pingInfo.indexOf("Destination host unreachable") != -1 || pingInfo.indexOf("Unknown host") != -1 || pingInfo.indexOf("Request timed out.") != -1 || pingInfo.indexOf("TTL expired in transit.") != -1) result = 0; else result = 1;
        if (report) return pingInfo.toString(); else return result;
    }

    public static void ping(String ipAddress) {
        String cmd = null;
        if (System.getProperty("os.name").indexOf("Windows") != -1) cmd = "ping -n 2 " + ipAddress; else cmd = "ping -c 2 " + ipAddress;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
        } finally {
            try {
                process.destroy();
            } catch (Exception e) {
            }
        }
    }

    /**
	 * 获取该ip的网络地址
	 * 
	 * @param ipAddress
	 * @return
	 */
    public static String getNetAddress(String ipAddress) {
        int dotLoc = ipAddress.lastIndexOf(".");
        return ipAddress.substring(0, dotLoc) + ".0";
    }

    /**
	 * 获取该范围的所有ip
	 * 
	 * @param startIp
	 * @param endIp
	 * @return
	 */
    public static List<String> getAllIpAddress(String startIp, String endIp) {
        long startIpLong = ipToLong(startIp);
        long endIpLong = ipToLong(endIp);
        List<String> list = new ArrayList<String>();
        for (long l = startIpLong; l < endIpLong + 1; l++) {
            list.add(longToIp(l));
        }
        return list;
    }

    /**
	 * 获取改范围所有ip，long型的
	 * 
	 * @param startIpLong
	 * @param endIpLong
	 * @return
	 */
    public static List<Long> getAllIpAddress(long startIpLong, long endIpLong) {
        List<Long> list = new ArrayList<Long>();
        for (long l = startIpLong; l < endIpLong + 1; l++) {
            list.add(l);
        }
        return list;
    }

    public static void main(String args[]) {
        List<String> list = NetworkUtils.getAllIpAddress("127.0.0.1", "127.0.0.3");
        for (String s : list) {
            System.out.println(s);
        }
    }
}
