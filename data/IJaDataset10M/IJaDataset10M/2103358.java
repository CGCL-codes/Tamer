package com.ice.util;

import java.net.URL;

public class URLUtilities {

    public static int urlDepth(URL url) {
        int result = 0;
        String file = url.getFile();
        for (int i = 1; i < file.length(); ++i) {
            if (file.charAt(i) == '/') result++;
        }
        return result;
    }
}
