package org.breport.breport.util;

import org.breport.breport.Config;
import org.breport.breport.AppInfo;
import java.util.*;
import java.io.IOException;

/**
 * Replace the defined tokens in a string with their expanded values.
 */
public class TokenReplacer {

    /**
	 * Hashmap of token/replacement mapping
	 */
    private static HashMap<String, String> replacements = new HashMap<String, String>();

    static {
        try {
            Config config = Config.getInstance();
            replacements.put("%r", config.getReportName());
            replacements.put("%c", config.getCatalogName());
            replacements.put("%v", AppInfo.getInstance().getVersionString());
            replacements.put("%%", "%");
        } catch (IOException e) {
        }
    }

    /**
	 * Perform token replacement on a string
	 * @param text Text to replace tokens in
	 */
    public static String replaceTokens(String text) {
        for (Iterator iter = TokenReplacer.replacements.keySet().iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            text = text.replaceAll(key, TokenReplacer.replacements.get(key));
        }
        return text;
    }
}
