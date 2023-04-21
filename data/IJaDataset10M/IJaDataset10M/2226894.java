package commonapp.datacon;

import common.AttrObj;
import common.IO;
import common.StringUtils;
import common.XMLToAttrObj;
import common.log.Log;
import commonapp.gui.RunTimeEnv;
import java.io.File;
import java.util.HashMap;

/**
   This class provides an XML namespace lookup
*/
public final class NSLookup {

    public static final NSLookup main = new NSLookup();

    /** Root element name. */
    private static final String E_NAMESPACES = "Namespaces";

    /** Child element name. */
    private static final String E_NAMESPACE = "Namespace";

    /** Key attribute name. */
    private static final String A_KEY = "key";

    /** Value attribute name. */
    private static final String A_VALUE = "value";

    private HashMap<String, String> myMap = null;

    /**
     Constructs a new NSLookup.
  */
    private NSLookup() {
        File dir = RunTimeEnv.main.get(RunTimeEnv.CONFIG_DIR);
        if (dir != null) {
            File file = IO.getFile(dir, "nslookup.xml");
            init(file);
        }
    }

    /**
     Constructs a new NSLookup using the specified file.

     @param theFile the nslookup file.
  */
    public NSLookup(File theFile) {
        if (theFile != null) {
            init(theFile);
        }
    }

    /**
     Gets the namespace string for the specified key.

     @param theKey the namespace key.

     @return the namespace string or null.
  */
    public String get(String theKey) {
        return (myMap == null) ? null : myMap.get(theKey);
    }

    private void init(File theFile) {
        if (!theFile.exists()) {
            Log.main.println(Log.ERROR, "NSLookup: file not found: " + theFile);
        } else if (!theFile.isFile()) {
            Log.main.println(Log.ERROR, "NSLookup: not a file: " + theFile);
        } else {
            XMLToAttrObj x2o = new XMLToAttrObj(theFile);
            AttrObj[] list = x2o.getAttrObj();
            if ((list == null) || (list.length != 1)) {
                Log.main.println(Log.ERROR, "NSLookup: invalid file format: " + theFile);
            } else if (!list[0].getName().equals(E_NAMESPACES)) {
                Log.main.println(Log.ERROR, "NSLookup: invalid root element: " + theFile);
            } else {
                myMap = new HashMap<String, String>();
                AttrObj root = list[0];
                list = root.getObj(E_NAMESPACE);
                if (list != null) {
                    for (AttrObj obj : list) {
                        String name = StringUtils.replaceText(obj.getAttr(A_KEY), '%', root, true);
                        String value = StringUtils.replaceText(StringUtils.replaceText(obj.getAttr(A_VALUE), '%', root, false), '%', obj, true);
                        if ((name != null) && (value != null)) {
                            myMap.put(name, value);
                        }
                    }
                }
            }
        }
    }
}
