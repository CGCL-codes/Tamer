package com.liferay.portlet.google.util;

import com.google.soap.search.GoogleSearch;
import com.liferay.portal.util.PropsUtil;
import java.util.List;
import java.util.Vector;

/**
 * <a href="GoogleUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class GoogleUtil {

    public static GoogleSearch getGoogleSearch() {
        GoogleSearch googleSearch = new GoogleSearch();
        googleSearch.setKey(getKey());
        return googleSearch;
    }

    public static String getKey() {
        return _instance._getKey();
    }

    public static void useNewKey() {
        _instance._useNewKey();
    }

    private GoogleUtil() {
        _licenseKeys = new Vector();
        for (int i = 0; ; i++) {
            String key = PropsUtil.get(PropsUtil.GOOGLE_LICENSE + i);
            if (key == null) {
                break;
            } else {
                _licenseKeys.add(key);
            }
        }
    }

    private String _getKey() {
        if (_licenseKeys.size() > 0) {
            _licenseCount++;
            if (_licenseCount >= 1000) {
                _useNewKey();
                return _getKey();
            } else {
                return (String) _licenseKeys.get(0);
            }
        } else {
            return null;
        }
    }

    private void _useNewKey() {
        if (_licenseKeys.size() > 0) {
            _licenseKeys.remove(0);
            _licenseCount = 0;
        }
    }

    private static GoogleUtil _instance = new GoogleUtil();

    private List _licenseKeys;

    private int _licenseCount;
}
