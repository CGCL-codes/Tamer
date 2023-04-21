package cookxml.common;

import cookxml.cookxml.CookXmlLib;
import cookxml.core.taglibrary.InheritableTagLibrary;

/**
 * This is an internal class that holds the default Common tag library instance.
 * This tag library uses CookXml tag library as its parent tag library.
 *
 * @author Heng Yuan
 * @version $Id: CommonLibSingleton.java 215 2007-06-06 03:59:41Z coconut $
 * @since CookXml 3.0
 */
class CommonLibSingleton {

    private static InheritableTagLibrary s_tagLibrary;

    static {
        try {
            s_tagLibrary = CommonLib.createTagLibrary(CookXmlLib.getSingletonTagLibrary());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static InheritableTagLibrary getTagLibrary() {
        return s_tagLibrary;
    }
}
