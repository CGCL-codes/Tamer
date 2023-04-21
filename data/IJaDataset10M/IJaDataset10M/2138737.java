package net.sf.jsr272.util;

import javax.microedition.broadcast.platform.PlatformProvider;
import javax.microedition.broadcast.platform.PlatformProviderSelector;

public class PlatformProviderSelectorUtil {

    public static String getAvailableProviderListDescription() {
        StringBuffer strbuff = new StringBuffer();
        PlatformProvider[] platform = PlatformProviderSelector.getAvailableProviders();
        for (int i = 0; i < platform.length; i++) {
            strbuff.append(platform[i].getID());
            if (i < platform.length - 1) strbuff.append(",");
        }
        return strbuff.toString();
    }
}
