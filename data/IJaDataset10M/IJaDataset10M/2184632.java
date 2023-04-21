package ar.com.minerva.manager;

import java.util.HashMap;

/**
 *
 * @author Pupa Rlz!
 */
public class CssManager {

    private static CssManager INSTANCE = new CssManager();

    private CssManager() {
    }

    public static CssManager getInstance() {
        return INSTANCE;
    }

    /**
     * Gets all Styles paths with keyWord
     * @return HashMap
     */
    public HashMap<String, String> getStylePaths() {
        HashMap<String, String> stylePaths = new HashMap<String, String>();
        stylePaths.put("default", "pages/css/default/default.css");
        stylePaths.put("under-the-sea", "pages/css/under-the-sea/default.css");
        stylePaths.put("make-em-proud", "pages/css/make-em-proud/default.css");
        stylePaths.put("test", "pages/css/test/default.css");
        return stylePaths;
    }
}
