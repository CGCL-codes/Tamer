package com.chilkatsoft;

public class CkRss {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected CkRss(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(CkRss obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            chilkatJNI.delete_CkRss(swigCPtr);
        }
        swigCPtr = 0;
    }

    public CkRss() {
        this(chilkatJNI.new_CkRss(), true);
    }

    public int get_NumChannels() {
        return chilkatJNI.CkRss_get_NumChannels(swigCPtr, this);
    }

    public int get_NumItems() {
        return chilkatJNI.CkRss_get_NumItems(swigCPtr, this);
    }

    public CkRss AddNewChannel() {
        long cPtr = chilkatJNI.CkRss_AddNewChannel(swigCPtr, this);
        return (cPtr == 0) ? null : new CkRss(cPtr, false);
    }

    public CkRss AddNewImage() {
        long cPtr = chilkatJNI.CkRss_AddNewImage(swigCPtr, this);
        return (cPtr == 0) ? null : new CkRss(cPtr, false);
    }

    public CkRss AddNewItem() {
        long cPtr = chilkatJNI.CkRss_AddNewItem(swigCPtr, this);
        return (cPtr == 0) ? null : new CkRss(cPtr, false);
    }

    public boolean DownloadRss(String url) {
        return chilkatJNI.CkRss_DownloadRss(swigCPtr, this, url);
    }

    public boolean GetAttr(String tag, String attrName, CkString outStr) {
        return chilkatJNI.CkRss_GetAttr(swigCPtr, this, tag, attrName, CkString.getCPtr(outStr), outStr);
    }

    public String getAttr(String tag, String attrName) {
        return chilkatJNI.CkRss_getAttr(swigCPtr, this, tag, attrName);
    }

    public CkRss GetChannel(int index) {
        long cPtr = chilkatJNI.CkRss_GetChannel(swigCPtr, this, index);
        return (cPtr == 0) ? null : new CkRss(cPtr, false);
    }

    public int GetCount(String tag) {
        return chilkatJNI.CkRss_GetCount(swigCPtr, this, tag);
    }

    public boolean GetDate(String tag, SYSTEMTIME sysTime) {
        return chilkatJNI.CkRss_GetDate(swigCPtr, this, tag, SYSTEMTIME.getCPtr(sysTime), sysTime);
    }

    public CkRss GetImage() {
        long cPtr = chilkatJNI.CkRss_GetImage(swigCPtr, this);
        return (cPtr == 0) ? null : new CkRss(cPtr, false);
    }

    public int GetInt(String tag) {
        return chilkatJNI.CkRss_GetInt(swigCPtr, this, tag);
    }

    public CkRss GetItem(int index) {
        long cPtr = chilkatJNI.CkRss_GetItem(swigCPtr, this, index);
        return (cPtr == 0) ? null : new CkRss(cPtr, false);
    }

    public boolean GetString(String tag, CkString outStr) {
        return chilkatJNI.CkRss_GetString(swigCPtr, this, tag, CkString.getCPtr(outStr), outStr);
    }

    public String getString(String tag) {
        return chilkatJNI.CkRss_getString(swigCPtr, this, tag);
    }

    public boolean LoadRssFile(String filename) {
        return chilkatJNI.CkRss_LoadRssFile(swigCPtr, this, filename);
    }

    public boolean LoadRssString(String rssString) {
        return chilkatJNI.CkRss_LoadRssString(swigCPtr, this, rssString);
    }

    public boolean MGetAttr(String tag, int idx, String attrName, CkString outStr) {
        return chilkatJNI.CkRss_MGetAttr(swigCPtr, this, tag, idx, attrName, CkString.getCPtr(outStr), outStr);
    }

    public String mGetAttr(String tag, int idx, String attrName) {
        return chilkatJNI.CkRss_mGetAttr(swigCPtr, this, tag, idx, attrName);
    }

    public boolean MGetString(String tag, int idx, CkString outStr) {
        return chilkatJNI.CkRss_MGetString(swigCPtr, this, tag, idx, CkString.getCPtr(outStr), outStr);
    }

    public String mGetString(String tag, int idx) {
        return chilkatJNI.CkRss_mGetString(swigCPtr, this, tag, idx);
    }

    public boolean MSetAttr(String tag, int idx, String attrName, String value) {
        return chilkatJNI.CkRss_MSetAttr(swigCPtr, this, tag, idx, attrName, value);
    }

    public boolean MSetString(String tag, int idx, String value) {
        return chilkatJNI.CkRss_MSetString(swigCPtr, this, tag, idx, value);
    }

    public void NewRss() {
        chilkatJNI.CkRss_NewRss(swigCPtr, this);
    }

    public void Remove(String tag) {
        chilkatJNI.CkRss_Remove(swigCPtr, this, tag);
    }

    public void SetAttr(String tag, String attrName, String value) {
        chilkatJNI.CkRss_SetAttr(swigCPtr, this, tag, attrName, value);
    }

    public void SetDate(String tag, SYSTEMTIME d) {
        chilkatJNI.CkRss_SetDate(swigCPtr, this, tag, SYSTEMTIME.getCPtr(d), d);
    }

    public void SetDateNow(String tag) {
        chilkatJNI.CkRss_SetDateNow(swigCPtr, this, tag);
    }

    public void SetInt(String tag, int value) {
        chilkatJNI.CkRss_SetInt(swigCPtr, this, tag, value);
    }

    public void SetString(String tag, String value) {
        chilkatJNI.CkRss_SetString(swigCPtr, this, tag, value);
    }

    public boolean ToXmlString(CkString outStr) {
        return chilkatJNI.CkRss_ToXmlString(swigCPtr, this, CkString.getCPtr(outStr), outStr);
    }

    public String toXmlString() {
        return chilkatJNI.CkRss_toXmlString(swigCPtr, this);
    }

    public boolean SaveLastError(String filename) {
        return chilkatJNI.CkRss_SaveLastError(swigCPtr, this, filename);
    }

    public void LastErrorXml(CkString str) {
        chilkatJNI.CkRss_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void LastErrorHtml(CkString str) {
        chilkatJNI.CkRss_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void LastErrorText(CkString str) {
        chilkatJNI.CkRss_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public String lastErrorText() {
        return chilkatJNI.CkRss_lastErrorText(swigCPtr, this);
    }

    public String lastErrorXml() {
        return chilkatJNI.CkRss_lastErrorXml(swigCPtr, this);
    }

    public String lastErrorHtml() {
        return chilkatJNI.CkRss_lastErrorHtml(swigCPtr, this);
    }
}
