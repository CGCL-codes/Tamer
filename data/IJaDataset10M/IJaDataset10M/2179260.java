package com.newbee.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetMac {

    public static String getMacAddressIP() {
        String macAddress = "";
        macAddress = _getMacAddressIP("ipconfig /all", "-").replaceAll("-", "");
        if ("".equals(macAddress)) {
            macAddress = _getMacAddressIP("ifconfig", ":").replaceAll(":", "");
        }
        return macAddress;
    }

    private static String _getMacAddressIP(String strCommand, String fg) {
        String str = "";
        String macAddress = "";
        try {
            Process pp = Runtime.getRuntime().exec(strCommand);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            Pattern pattern = Pattern.compile("([0-9A-Fa-f]{2})(" + fg + "[0-9A-Fa-f]{2}){5}");
            while ((str = input.readLine()) != null) {
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()) {
                    if (macAddress.equals("")) {
                        macAddress = matcher.group();
                    } else {
                        macAddress += "," + matcher.group();
                    }
                }
            }
        } catch (IOException e) {
            Logger.getAnonymousLogger().info("\"" + strCommand + "\" is a invalid command !");
        }
        return macAddress;
    }

    public static void main(String[] args) {
        System.out.println(Long.parseLong(GetMac.getMacAddressIP().split(",")[0], 16));
        System.out.println(GetMac.getMacAddressIP());
    }
}
