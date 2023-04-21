package com.edujinilabs.dwt.client.core.utils;

import java.util.Date;
import com.edujinilabs.dwt.client.Dwt;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.UnsafeNativeLong;

/**
 * Date Utlities
 * 
 * For terms and usage, please see the LICENSE file provided alongwith or
 * contact <a
 * href='mailto:copyright@edujini-labs.com'>copyright@edujini-labs.com</a>
 * <br/> <a href='http://www.edujini-labs.com'>http://www.edujini-labs.com</a>
 * 
 * @author Gaurav Vaish
 * @author $Author: mastergaurav $
 * @copyright (C) 2008 - 2008, Edujini Labs Pvt. Ltd. (www.edujini-labs.com)
 * @svn.url $URL: http://dwt.svn.sourceforge.net/svnroot/dwt/trunk/dwt/eclipse/src/com/edujinilabs/dwt/client/core/utils/JSDateHelper.java $
 * @version $Revision: 19 $
 */
public class JSDateHelper {

    static {
        Dwt.ensureInitialized();
    }

    private JSDateHelper() {
    }

    public static native Date toDate(JavaScriptObject obj);

    public static JavaScriptObject toJavaScriptObject(Date date) {
        return toJavaScriptObject(date.getTime());
    }

    @UnsafeNativeLong
    public static native JavaScriptObject toJavaScriptObject(long millis);
}
