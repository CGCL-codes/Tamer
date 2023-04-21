package cookxml.cookswing;

import cookxml.common.CommonLib;
import cookxml.core.taglibrary.InheritableTagLibrary;

/**
 * This is an internal class that holds the default Swing tag library instance.
 *
 * @author Heng Yuan
 * @version $Id: CookSwingLibSingleton.java 215 2007-06-06 03:59:41Z coconut $
 * @since CookSwing 1.5
 */
class CookSwingLibSingleton {

    private static InheritableTagLibrary s_swingTagLib;

    static {
        try {
            s_swingTagLib = CookSwingLib.createTagLibrary(CommonLib.getSingletonTagLibrary());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static InheritableTagLibrary getTagLibrary() {
        return s_swingTagLib;
    }
}
