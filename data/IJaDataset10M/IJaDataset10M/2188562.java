package org.eclipse.swt.ole.win32;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.GUID;
import org.eclipse.swt.internal.ole.win32.IDispatch;
import org.eclipse.swt.internal.ole.win32.IUnknown;
import edu.mit.lcs.haystack.HaystackException;

/**
 * @author Dennis Quan
 */
public class HaystackHooks {

    public static OleAutomation createOleAutomation(String strCLSID) throws HaystackException {
        GUID clsid = new GUID();
        char[] charArray = new char[strCLSID.length() + 1];
        System.arraycopy(strCLSID.toCharArray(), 0, charArray, 0, charArray.length - 1);
        charArray[charArray.length - 1] = 0;
        if (COM.S_OK != COM.CLSIDFromString(charArray, clsid)) {
            if (COM.S_OK != COM.CLSIDFromProgID(charArray, clsid)) {
                throw new HaystackException("Invalid CLSID");
            }
        }
        int[] ppv = new int[1];
        int result = COM.CoCreateInstance(clsid, 0, COM.CLSCTX_INPROC_HANDLER | COM.CLSCTX_INPROC_SERVER | COM.CLSCTX_LOCAL_SERVER, COM.IIDIUnknown, ppv);
        if (result != COM.S_OK) {
            throw new HaystackException("CoCreateInstance failed: " + result);
        }
        IUnknown objIUnknown = new IUnknown(ppv[0]);
        ppv = new int[1];
        result = objIUnknown.QueryInterface(COM.IIDIDispatch, ppv);
        if (result != COM.S_OK) {
            objIUnknown.Release();
            throw new HaystackException("QueryInterface failed: " + result);
        }
        return new OleAutomation(new IDispatch(ppv[0]));
    }
}
