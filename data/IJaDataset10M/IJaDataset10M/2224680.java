package com.chilkatsoft;

public class CkGzip {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected CkGzip(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(CkGzip obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            chilkatJNI.delete_CkGzip(swigCPtr);
        }
        swigCPtr = 0;
    }

    public CkGzip() {
        this(chilkatJNI.new_CkGzip(), true);
    }

    public void get_Version(CkString str) {
        chilkatJNI.CkGzip_get_Version(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public String version() {
        return chilkatJNI.CkGzip_version(swigCPtr, this);
    }

    public boolean CompressFile2(String inFilename, String embeddedFilename, String outFilename) {
        return chilkatJNI.CkGzip_CompressFile2(swigCPtr, this, inFilename, embeddedFilename, outFilename);
    }

    public boolean CompressString(String inStr, String outCharset, CkByteData outBytes) {
        return chilkatJNI.CkGzip_CompressString(swigCPtr, this, inStr, outCharset, CkByteData.getCPtr(outBytes), outBytes);
    }

    public int CompressStringToFile(String inStr, String outCharset, String outFilename) {
        return chilkatJNI.CkGzip_CompressStringToFile(swigCPtr, this, inStr, outCharset, outFilename);
    }

    public boolean ReadFile(String filename, CkByteData outBytes) {
        return chilkatJNI.CkGzip_ReadFile(swigCPtr, this, filename, CkByteData.getCPtr(outBytes), outBytes);
    }

    public boolean UnTarGz(String gzFilename, String destDir, boolean bNoAbsolute) {
        return chilkatJNI.CkGzip_UnTarGz(swigCPtr, this, gzFilename, destDir, bNoAbsolute);
    }

    public boolean UncompressFileToString(String inFilename, String inCharset, CkString outStr) {
        return chilkatJNI.CkGzip_UncompressFileToString(swigCPtr, this, inFilename, inCharset, CkString.getCPtr(outStr), outStr);
    }

    public String uncompressFileToString(String inFilename, String inCharset) {
        return chilkatJNI.CkGzip_uncompressFileToString(swigCPtr, this, inFilename, inCharset);
    }

    public boolean UncompressString(CkByteData inData, String inCharset, CkString outStr) {
        return chilkatJNI.CkGzip_UncompressString(swigCPtr, this, CkByteData.getCPtr(inData), inData, inCharset, CkString.getCPtr(outStr), outStr);
    }

    public String uncompressString(CkByteData inData, String inCharset) {
        return chilkatJNI.CkGzip_uncompressString(swigCPtr, this, CkByteData.getCPtr(inData), inData, inCharset);
    }

    public boolean WriteFile(String filename, CkByteData binaryData) {
        return chilkatJNI.CkGzip_WriteFile(swigCPtr, this, filename, CkByteData.getCPtr(binaryData), binaryData);
    }

    public boolean ExamineFile(String inGzFilename) {
        return chilkatJNI.CkGzip_ExamineFile(swigCPtr, this, inGzFilename);
    }

    public boolean ExamineMemory(CkByteData inGzData) {
        return chilkatJNI.CkGzip_ExamineMemory(swigCPtr, this, CkByteData.getCPtr(inGzData), inGzData);
    }

    public boolean Decode(String str, String encoding, CkByteData outBytes) {
        return chilkatJNI.CkGzip_Decode(swigCPtr, this, str, encoding, CkByteData.getCPtr(outBytes), outBytes);
    }

    public boolean get_UseCurrentDate() {
        return chilkatJNI.CkGzip_get_UseCurrentDate(swigCPtr, this);
    }

    public void put_UseCurrentDate(boolean newVal) {
        chilkatJNI.CkGzip_put_UseCurrentDate(swigCPtr, this, newVal);
    }

    public boolean XfdlToXml(String xfdl, CkString outStr) {
        return chilkatJNI.CkGzip_XfdlToXml(swigCPtr, this, xfdl, CkString.getCPtr(outStr), outStr);
    }

    public String xfdlToXml(String xfdl) {
        return chilkatJNI.CkGzip_xfdlToXml(swigCPtr, this, xfdl);
    }

    public boolean Encode(CkByteData byteData, String encoding, CkString outStr) {
        return chilkatJNI.CkGzip_Encode(swigCPtr, this, CkByteData.getCPtr(byteData), byteData, encoding, CkString.getCPtr(outStr), outStr);
    }

    public String encode(CkByteData byteData, String encoding) {
        return chilkatJNI.CkGzip_encode(swigCPtr, this, CkByteData.getCPtr(byteData), byteData, encoding);
    }

    public boolean get_VerboseLogging() {
        return chilkatJNI.CkGzip_get_VerboseLogging(swigCPtr, this);
    }

    public void put_VerboseLogging(boolean newVal) {
        chilkatJNI.CkGzip_put_VerboseLogging(swigCPtr, this, newVal);
    }

    public String lastErrorText() {
        return chilkatJNI.CkGzip_lastErrorText(swigCPtr, this);
    }

    public String lastErrorXml() {
        return chilkatJNI.CkGzip_lastErrorXml(swigCPtr, this);
    }

    public String lastErrorHtml() {
        return chilkatJNI.CkGzip_lastErrorHtml(swigCPtr, this);
    }

    public String comment() {
        return chilkatJNI.CkGzip_comment(swigCPtr, this);
    }

    public String filename() {
        return chilkatJNI.CkGzip_filename(swigCPtr, this);
    }

    public void DeflateStringENC(String str, String charset, String encoding, CkString strOut) {
        chilkatJNI.CkGzip_DeflateStringENC(swigCPtr, this, str, charset, encoding, CkString.getCPtr(strOut), strOut);
    }

    public void InflateStringENC(String str, String charset, String encoding, CkString strOut) {
        chilkatJNI.CkGzip_InflateStringENC(swigCPtr, this, str, charset, encoding, CkString.getCPtr(strOut), strOut);
    }

    public String deflateStringENC(String str, String charset, String encoding) {
        return chilkatJNI.CkGzip_deflateStringENC(swigCPtr, this, str, charset, encoding);
    }

    public String inflateStringENC(String str, String charset, String encoding) {
        return chilkatJNI.CkGzip_inflateStringENC(swigCPtr, this, str, charset, encoding);
    }

    public boolean SaveLastError(String filename) {
        return chilkatJNI.CkGzip_SaveLastError(swigCPtr, this, filename);
    }

    public void LastErrorXml(CkString str) {
        chilkatJNI.CkGzip_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void LastErrorHtml(CkString str) {
        chilkatJNI.CkGzip_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void LastErrorText(CkString str) {
        chilkatJNI.CkGzip_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public boolean UnlockComponent(String unlockCode) {
        return chilkatJNI.CkGzip_UnlockComponent(swigCPtr, this, unlockCode);
    }

    public boolean IsUnlocked() {
        return chilkatJNI.CkGzip_IsUnlocked(swigCPtr, this);
    }

    public boolean CompressFile(String inFilename, String outFilename) {
        return chilkatJNI.CkGzip_CompressFile(swigCPtr, this, inFilename, outFilename);
    }

    public boolean UncompressFile(String inFilename, String outFilename) {
        return chilkatJNI.CkGzip_UncompressFile(swigCPtr, this, inFilename, outFilename);
    }

    public boolean CompressFileToMem(String inFilename, CkByteData db) {
        return chilkatJNI.CkGzip_CompressFileToMem(swigCPtr, this, inFilename, CkByteData.getCPtr(db), db);
    }

    public boolean UncompressFileToMem(String inFilename, CkByteData db) {
        return chilkatJNI.CkGzip_UncompressFileToMem(swigCPtr, this, inFilename, CkByteData.getCPtr(db), db);
    }

    public boolean CompressMemToFile(CkByteData db, String outFilename) {
        return chilkatJNI.CkGzip_CompressMemToFile(swigCPtr, this, CkByteData.getCPtr(db), db, outFilename);
    }

    public boolean UncompressMemToFile(CkByteData db, String outFilename) {
        return chilkatJNI.CkGzip_UncompressMemToFile(swigCPtr, this, CkByteData.getCPtr(db), db, outFilename);
    }

    public boolean CompressMemory(CkByteData dbIn, CkByteData dbOut) {
        return chilkatJNI.CkGzip_CompressMemory(swigCPtr, this, CkByteData.getCPtr(dbIn), dbIn, CkByteData.getCPtr(dbOut), dbOut);
    }

    public boolean UncompressMemory(CkByteData dbIn, CkByteData dbOut) {
        return chilkatJNI.CkGzip_UncompressMemory(swigCPtr, this, CkByteData.getCPtr(dbIn), dbIn, CkByteData.getCPtr(dbOut), dbOut);
    }

    public void get_Filename(CkString str) {
        chilkatJNI.CkGzip_get_Filename(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void put_Filename(String str) {
        chilkatJNI.CkGzip_put_Filename(swigCPtr, this, str);
    }

    public void get_Comment(CkString str) {
        chilkatJNI.CkGzip_get_Comment(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void put_Comment(String str) {
        chilkatJNI.CkGzip_put_Comment(swigCPtr, this, str);
    }

    public void get_ExtraData(CkByteData data) {
        chilkatJNI.CkGzip_get_ExtraData(swigCPtr, this, CkByteData.getCPtr(data), data);
    }

    public void put_ExtraData(CkByteData data) {
        chilkatJNI.CkGzip_put_ExtraData(swigCPtr, this, CkByteData.getCPtr(data), data);
    }

    public void get_LastMod(SYSTEMTIME sysTime) {
        chilkatJNI.CkGzip_get_LastMod(swigCPtr, this, SYSTEMTIME.getCPtr(sysTime), sysTime);
    }

    public void put_LastMod(SYSTEMTIME sysTime) {
        chilkatJNI.CkGzip_put_LastMod(swigCPtr, this, SYSTEMTIME.getCPtr(sysTime), sysTime);
    }
}
