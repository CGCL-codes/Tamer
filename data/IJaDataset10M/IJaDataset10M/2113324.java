package org.pachyderm.apollo.core;

import java.io.UnsupportedEncodingException;
import com.webobjects.appserver.WOSession;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;

public class CXPasteboard {

    private String _pboardName = null;

    private String _groupIdentifier = null;

    private int changeCount = 0;

    private NSArray<?> dataTypes = null;

    private Object dataOwner = null;

    private NSMutableDictionary<String, NSData> dataByType = null;

    public static final String GeneralPboard = "GeneralPboard";

    public static final String FindPboard = "FindPboard";

    CXPasteboard(String name, WOSession session) {
        this(name, session.sessionID());
    }

    CXPasteboard(String name, String group) {
        super();
        _pboardName = name;
        _groupIdentifier = group;
    }

    public static CXPasteboard generalPasteboard() {
        return generalPasteboard(CXAppContext.currentSession());
    }

    private static CXPasteboard generalPasteboard(WOSession session) {
        return pasteboardWithName(GeneralPboard, session);
    }

    public static CXPasteboard pasteboardWithName(String name) {
        return pasteboardWithName(name, CXAppContext.currentSession());
    }

    private static CXPasteboard pasteboardWithName(String name, WOSession session) {
        return CXPasteboardServer.defaultServer().pasteboardWithName(name, session);
    }

    public static CXPasteboard pasteboardWithUniqueName() {
        return pasteboardWithUniqueName(CXAppContext.currentSession());
    }

    private static CXPasteboard pasteboardWithUniqueName(WOSession session) {
        return CXPasteboardServer.defaultServer().pasteboardWithUniqueName(session);
    }

    public String name() {
        return _pboardName;
    }

    public String groupIdentifier() {
        return _groupIdentifier;
    }

    public int declareTypes(NSArray<?> types, Object owner) {
        int cc = CXPasteboardServer.defaultServer()._obtainChangeCountForPasteboard(this);
        if (cc == 0) {
            return cc;
        }
        int typeCount = types.count();
        dataTypes = types.immutableClone();
        dataOwner = owner;
        dataByType = new NSMutableDictionary<String, NSData>(typeCount);
        changeCount = cc;
        return changeCount;
    }

    public boolean setDataForType(NSData data, String type) {
        return _setDataForType(data, type);
    }

    public boolean setPropertyListForType(Object propertyList, String type) {
        return _setDataForType(NSPropertyListSerialization.dataFromPropertyList(propertyList, "UTF-8"), type);
    }

    private NSData getDataForType(String type) {
        Object data = dataByType.objectForKey(type);
        if (data == null && dataTypes.containsObject(type)) {
            CXPasteboardServer.defaultServer()._pasteboardNeedsDataForType(this, type);
            data = dataByType.objectForKey(type);
        }
        return (NSData) data;
    }

    private String getStringForType(String type) {
        NSData data = getDataForType(type);
        String str = null;
        if (data != null) {
            try {
                str = new String(data.bytes(), "UTF-8");
            } catch (UnsupportedEncodingException uee) {
                uee.printStackTrace();
            }
        }
        return str;
    }

    public boolean setStringForType(String string, String type) {
        NSData data = null;
        try {
            data = new NSData(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        return _setDataForType(data, type);
    }

    private boolean _setDataForType(NSData data, String type) {
        if (dataTypes.containsObject(type)) {
            dataByType.setObjectForKey(data, type);
            return true;
        }
        return false;
    }

    public Object propertyListForType(String type) {
        NSData data = getDataForType(type);
        Object plist = null;
        if (data != null) {
            plist = NSPropertyListSerialization.propertyListFromData(data, "UTF-8");
        }
        return plist;
    }

    public NSArray<?> types() {
        return dataTypes;
    }

    public int changeCount() {
        return changeCount;
    }

    int _incrementChangeCount() {
        return ++changeCount;
    }

    void _setChangeCount(int cc) {
        changeCount = cc;
    }

    Object _owner() {
        return dataOwner;
    }

    void _setOwner(Object own) {
        dataOwner = own;
    }

    NSDictionary<String, NSData> _dataDictionary() {
        return dataByType;
    }

    void _setDataDictionary(NSMutableDictionary<String, NSData> dbt) {
        dataByType = dbt;
    }
}
