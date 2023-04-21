package com.chilkatsoft;

public class CkCharset {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected CkCharset(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(CkCharset obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            chilkatJNI.delete_CkCharset(swigCPtr);
        }
        swigCPtr = 0;
    }

    public CkCharset() {
        this(chilkatJNI.new_CkCharset(), true);
    }

    public String lastErrorText() {
        return chilkatJNI.CkCharset_lastErrorText(swigCPtr, this);
    }

    public String lastErrorXml() {
        return chilkatJNI.CkCharset_lastErrorXml(swigCPtr, this);
    }

    public String lastErrorHtml() {
        return chilkatJNI.CkCharset_lastErrorHtml(swigCPtr, this);
    }

    public boolean UrlDecodeStr(String inStr, CkString outStr) {
        return chilkatJNI.CkCharset_UrlDecodeStr(swigCPtr, this, inStr, CkString.getCPtr(outStr), outStr);
    }

    public String urlDecodeStr(String inStr) {
        return chilkatJNI.CkCharset_urlDecodeStr(swigCPtr, this, inStr);
    }

    public String getHtmlFileCharset(String htmlFilename) {
        return chilkatJNI.CkCharset_getHtmlFileCharset(swigCPtr, this, htmlFilename);
    }

    public String getHtmlCharset(CkByteData htmlData) {
        return chilkatJNI.CkCharset_getHtmlCharset(swigCPtr, this, CkByteData.getCPtr(htmlData), htmlData);
    }

    public String lastOutputAsQP() {
        return chilkatJNI.CkCharset_lastOutputAsQP(swigCPtr, this);
    }

    public String lastInputAsQP() {
        return chilkatJNI.CkCharset_lastInputAsQP(swigCPtr, this);
    }

    public String lastOutputAsHex() {
        return chilkatJNI.CkCharset_lastOutputAsHex(swigCPtr, this);
    }

    public String lastInputAsHex() {
        return chilkatJNI.CkCharset_lastInputAsHex(swigCPtr, this);
    }

    public String htmlDecodeToStr(String str) {
        return chilkatJNI.CkCharset_htmlDecodeToStr(swigCPtr, this, str);
    }

    public String toCharset() {
        return chilkatJNI.CkCharset_toCharset(swigCPtr, this);
    }

    public String fromCharset() {
        return chilkatJNI.CkCharset_fromCharset(swigCPtr, this);
    }

    public String version() {
        return chilkatJNI.CkCharset_version(swigCPtr, this);
    }

    public String codePageToCharset(int codePage) {
        return chilkatJNI.CkCharset_codePageToCharset(swigCPtr, this, codePage);
    }

    public String altToCharset() {
        return chilkatJNI.CkCharset_altToCharset(swigCPtr, this);
    }

    public String upperCase(String inStr) {
        return chilkatJNI.CkCharset_upperCase(swigCPtr, this, inStr);
    }

    public String lowerCase(String inStr) {
        return chilkatJNI.CkCharset_lowerCase(swigCPtr, this, inStr);
    }

    public void UpperCase(String inStr, CkString outStr) {
        chilkatJNI.CkCharset_UpperCase(swigCPtr, this, inStr, CkString.getCPtr(outStr), outStr);
    }

    public void LowerCase(String inStr, CkString outStr) {
        chilkatJNI.CkCharset_LowerCase(swigCPtr, this, inStr, CkString.getCPtr(outStr), outStr);
    }

    public boolean GetHtmlFileCharset(String htmlFilename, CkString strCharset) {
        return chilkatJNI.CkCharset_GetHtmlFileCharset(swigCPtr, this, htmlFilename, CkString.getCPtr(strCharset), strCharset);
    }

    public boolean GetHtmlCharset(CkByteData htmlData, CkString strCharset) {
        return chilkatJNI.CkCharset_GetHtmlCharset(swigCPtr, this, CkByteData.getCPtr(htmlData), htmlData, CkString.getCPtr(strCharset), strCharset);
    }

    public boolean ConvertHtmlFile(String inFilename, String outFilename) {
        return chilkatJNI.CkCharset_ConvertHtmlFile(swigCPtr, this, inFilename, outFilename);
    }

    public boolean ConvertHtml(CkByteData htmlIn, CkByteData htmlOut) {
        return chilkatJNI.CkCharset_ConvertHtml(swigCPtr, this, CkByteData.getCPtr(htmlIn), htmlIn, CkByteData.getCPtr(htmlOut), htmlOut);
    }

    public void get_LastOutputAsQP(CkString str) {
        chilkatJNI.CkCharset_get_LastOutputAsQP(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void get_LastInputAsQP(CkString str) {
        chilkatJNI.CkCharset_get_LastInputAsQP(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void get_LastOutputAsHex(CkString str) {
        chilkatJNI.CkCharset_get_LastOutputAsHex(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void get_LastInputAsHex(CkString str) {
        chilkatJNI.CkCharset_get_LastInputAsHex(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void put_SaveLast(boolean value) {
        chilkatJNI.CkCharset_put_SaveLast(swigCPtr, this, value);
    }

    public boolean get_SaveLast() {
        return chilkatJNI.CkCharset_get_SaveLast(swigCPtr, this);
    }

    public void EntityEncodeHex(String inStr, CkString outStr) {
        chilkatJNI.CkCharset_EntityEncodeHex(swigCPtr, this, inStr, CkString.getCPtr(outStr), outStr);
    }

    public void EntityEncodeDec(String inStr, CkString outStr) {
        chilkatJNI.CkCharset_EntityEncodeDec(swigCPtr, this, inStr, CkString.getCPtr(outStr), outStr);
    }

    public String entityEncodeHex(String inStr) {
        return chilkatJNI.CkCharset_entityEncodeHex(swigCPtr, this, inStr);
    }

    public String entityEncodeDec(String inStr) {
        return chilkatJNI.CkCharset_entityEncodeDec(swigCPtr, this, inStr);
    }

    public boolean WriteFile(String filename, CkByteData dataBuf) {
        return chilkatJNI.CkCharset_WriteFile(swigCPtr, this, filename, CkByteData.getCPtr(dataBuf), dataBuf);
    }

    public boolean ReadFile(String filename, CkByteData dataBuf) {
        return chilkatJNI.CkCharset_ReadFile(swigCPtr, this, filename, CkByteData.getCPtr(dataBuf), dataBuf);
    }

    public boolean ConvertFromUnicode(CkByteData uniData, CkByteData mbData) {
        return chilkatJNI.CkCharset_ConvertFromUnicode(swigCPtr, this, CkByteData.getCPtr(uniData), uniData, CkByteData.getCPtr(mbData), mbData);
    }

    public boolean ConvertToUnicode(CkByteData mbData, CkByteData uniData) {
        return chilkatJNI.CkCharset_ConvertToUnicode(swigCPtr, this, CkByteData.getCPtr(mbData), mbData, CkByteData.getCPtr(uniData), uniData);
    }

    public boolean VerifyFile(String charset, String filename) {
        return chilkatJNI.CkCharset_VerifyFile(swigCPtr, this, charset, filename);
    }

    public boolean VerifyData(String charset, CkByteData charData) {
        return chilkatJNI.CkCharset_VerifyData(swigCPtr, this, charset, CkByteData.getCPtr(charData), charData);
    }

    public boolean HtmlEntityDecode(CkByteData inData, CkByteData outData) {
        return chilkatJNI.CkCharset_HtmlEntityDecode(swigCPtr, this, CkByteData.getCPtr(inData), inData, CkByteData.getCPtr(outData), outData);
    }

    public boolean HtmlDecodeToStr(String str, CkString strOut) {
        return chilkatJNI.CkCharset_HtmlDecodeToStr(swigCPtr, this, str, CkString.getCPtr(strOut), strOut);
    }

    public boolean HtmlEntityDecodeFile(String inFilename, String outFilename) {
        return chilkatJNI.CkCharset_HtmlEntityDecodeFile(swigCPtr, this, inFilename, outFilename);
    }

    public boolean ConvertFile(String inFilename, String outFilename) {
        return chilkatJNI.CkCharset_ConvertFile(swigCPtr, this, inFilename, outFilename);
    }

    public boolean ConvertData(CkByteData inData, CkByteData outData) {
        return chilkatJNI.CkCharset_ConvertData(swigCPtr, this, CkByteData.getCPtr(inData), inData, CkByteData.getCPtr(outData), outData);
    }

    public void get_ToCharset(CkString str) {
        chilkatJNI.CkCharset_get_ToCharset(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void put_ToCharset(String charset) {
        chilkatJNI.CkCharset_put_ToCharset(swigCPtr, this, charset);
    }

    public void get_FromCharset(CkString str) {
        chilkatJNI.CkCharset_get_FromCharset(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void put_FromCharset(String charset) {
        chilkatJNI.CkCharset_put_FromCharset(swigCPtr, this, charset);
    }

    public void get_Version(CkString str) {
        chilkatJNI.CkCharset_get_Version(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public boolean UnlockComponent(String unlockCode) {
        return chilkatJNI.CkCharset_UnlockComponent(swigCPtr, this, unlockCode);
    }

    public boolean IsUnlocked() {
        return chilkatJNI.CkCharset_IsUnlocked(swigCPtr, this);
    }

    public boolean SaveLastError(String filename) {
        return chilkatJNI.CkCharset_SaveLastError(swigCPtr, this, filename);
    }

    public void LastErrorXml(CkString str) {
        chilkatJNI.CkCharset_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void LastErrorHtml(CkString str) {
        chilkatJNI.CkCharset_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void LastErrorText(CkString str) {
        chilkatJNI.CkCharset_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public int CharsetToCodePage(String charsetName) {
        return chilkatJNI.CkCharset_CharsetToCodePage(swigCPtr, this, charsetName);
    }

    public boolean CodePageToCharset(int codePage, CkString sCharset) {
        return chilkatJNI.CkCharset_CodePageToCharset(swigCPtr, this, codePage, CkString.getCPtr(sCharset), sCharset);
    }

    public int get_ErrorAction() {
        return chilkatJNI.CkCharset_get_ErrorAction(swigCPtr, this);
    }

    public void put_ErrorAction(int val) {
        chilkatJNI.CkCharset_put_ErrorAction(swigCPtr, this, val);
    }

    public void get_AltToCharset(CkString str) {
        chilkatJNI.CkCharset_get_AltToCharset(swigCPtr, this, CkString.getCPtr(str), str);
    }

    public void put_AltToCharset(String charsetName) {
        chilkatJNI.CkCharset_put_AltToCharset(swigCPtr, this, charsetName);
    }

    public void SetErrorString(String str) {
        chilkatJNI.CkCharset_SetErrorString(swigCPtr, this, str);
    }
}
