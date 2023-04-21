package com.google.code.p.keytooliui.shared.lang.string;

import com.google.code.p.keytooliui.shared.lang.*;
import java.util.*;

public class StringFilter {

    public static String s_getExtension(String strFileName) {
        if (strFileName == null) return null;
        int i = strFileName.lastIndexOf('.');
        if (i > 0 && i < strFileName.length() - 1) {
            return strFileName.substring(i + 1);
        }
        return null;
    }

    public boolean init() {
        return true;
    }

    public void destroy() {
    }

    public StringFilter(String[] strsExtension) {
        String f_strMethod = "StringFilter(strsExtension)";
        if (strsExtension == null) {
            MySystem.s_printOutExit(this, f_strMethod, "nil strsExtension");
        }
        this._hstFilters = new Hashtable<String, StringFilter>(strsExtension.length);
        for (int i = 0; i < strsExtension.length; i++) {
            if (!_addExtension(strsExtension[i])) {
                MySystem.s_printOutExit(this, f_strMethod, "failed");
            }
        }
    }

    /**
        if any error, exits
    **/
    public boolean accept(String strName) {
        String f_strMethod = "accept(strName)";
        if (strName == null) {
            MySystem.s_printOutExit(this, f_strMethod, "nil strName");
        }
        String extension = _getExtension(strName);
        if (extension == null) return false;
        if (this._hstFilters.get(extension) != null) return true;
        return false;
    }

    /** #######
        PRIVATE
        ####### **/
    private Hashtable<String, StringFilter> _hstFilters = null;

    private String _getExtension(String strFileName) {
        String str = StringFilter.s_getExtension(strFileName);
        if (str == null) return null;
        return str.toLowerCase();
    }

    private boolean _addExtension(String strExtension) {
        String f_strMethod = "_addExtension(strExtension)";
        if (strExtension == null) {
            MySystem.s_printOutError(this, f_strMethod, "nil strExtension");
            return false;
        }
        if (this._hstFilters == null) {
            MySystem.s_printOutError(this, f_strMethod, "nil this._hstFilters");
            return false;
        }
        this._hstFilters.put(strExtension.toLowerCase(), this);
        return true;
    }
}
