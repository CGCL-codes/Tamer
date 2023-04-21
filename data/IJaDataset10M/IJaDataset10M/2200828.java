package com.chilkatsoft;

public class CkRar {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected CkRar(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(CkRar obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            chilkatJNI.delete_CkRar(swigCPtr);
        }
        swigCPtr = 0;
    }

    public CkRar() {
        this(chilkatJNI.new_CkRar(), true);
    }

    public boolean get_Utf8() {
        return chilkatJNI.CkRar_get_Utf8(swigCPtr, this);
    }

    public void put_Utf8(boolean b) {
        chilkatJNI.CkRar_put_Utf8(swigCPtr, this, b);
    }

    public boolean Unrar(String dirPath) {
        return chilkatJNI.CkRar_Unrar(swigCPtr, this, dirPath);
    }

    public boolean FastOpen(String filename) {
        return chilkatJNI.CkRar_FastOpen(swigCPtr, this, filename);
    }

    public boolean Open(String filename) {
        return chilkatJNI.CkRar_Open(swigCPtr, this, filename);
    }

    public boolean Close() {
        return chilkatJNI.CkRar_Close(swigCPtr, this);
    }

    public int get_NumEntries() {
        return chilkatJNI.CkRar_get_NumEntries(swigCPtr, this);
    }

    public CkRarEntry GetEntryByName(String filename) {
        long cPtr = chilkatJNI.CkRar_GetEntryByName(swigCPtr, this, filename);
        return (cPtr == 0) ? null : new CkRarEntry(cPtr, true);
    }

    public CkRarEntry GetEntryByIndex(int index) {
        long cPtr = chilkatJNI.CkRar_GetEntryByIndex(swigCPtr, this, index);
        return (cPtr == 0) ? null : new CkRarEntry(cPtr, true);
    }

    public boolean SaveLastError(String filename) {
        return chilkatJNI.CkRar_SaveLastError(swigCPtr, this, filename);
    }

    public void LastErrorXml(CkString str) {
        chilkatJNI.CkRar_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void LastErrorHtml(CkString str) {
        chilkatJNI.CkRar_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void LastErrorText(CkString str) {
        chilkatJNI.CkRar_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public String lastErrorText() {
        return chilkatJNI.CkRar_lastErrorText(swigCPtr, this);
    }

    public String lastErrorXml() {
        return chilkatJNI.CkRar_lastErrorXml(swigCPtr, this);
    }

    public String lastErrorHtml() {
        return chilkatJNI.CkRar_lastErrorHtml(swigCPtr, this);
    }
}
