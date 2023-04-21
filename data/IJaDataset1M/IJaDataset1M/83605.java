package org.lnicholls.galleon.util;

import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class WindowsNetworkInfo extends NetInfo {

    private static final Logger log = Logger.getLogger(WindowsNetworkInfo.class.getName());

    public static final String IPCONFIG_COMMAND = "ipconfig /all";

    public WindowsNetworkInfo(String address) {
        super(address);
    }

    public void parse() {
        String ipConfigResponse = null;
        try {
            ipConfigResponse = runConsoleCommand(IPCONFIG_COMMAND);
        } catch (IOException ex) {
            Tools.logException(WindowsNetworkInfo.class, ex);
        }
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(ipConfigResponse, "\n");
        String address = null;
        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken().trim();
            if (line.startsWith("Physical Address")) {
                int pos = line.indexOf(":");
                if (pos <= 0) {
                    continue;
                }
                mPhysicalAddress = line.substring(pos + 1).trim();
            } else if (line.startsWith("Subnet Mask")) {
                int pos = line.indexOf(":");
                if (pos <= 0) {
                    continue;
                }
                mSubnetMask = line.substring(pos + 1).trim();
            } else if (line.startsWith("IP Address")) {
                int pos = line.indexOf(":");
                if (pos <= 0) {
                    continue;
                }
                address = line.substring(pos + 1).trim();
            }
            if (address != null && mPhysicalAddress != null && mSubnetMask != null) {
                if (address.equals(getAddress())) {
                    break;
                }
            }
        }
    }
}
