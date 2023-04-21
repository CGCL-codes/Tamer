package com.maigc.jawin;

import org.jawin.COMException;
import org.jawin.FuncPtr;
import org.jawin.ReturnFlags;
import org.jawin.io.LittleEndianOutputStream;
import org.jawin.io.NakedByteStream;

/**
 * Demo that uses the Win32 MessageBoxW API-method with the generic FuncPtr invoke.
 *
 * @version     $Revision: 1.1 $
 * @author      Morten Andersen, arosii_moa (at) users.sourceforge.net
 */
public class HelloDllGeneric {

    public static void main(String[] args) throws Exception {
        FuncPtr msgBox = null;
        try {
            msgBox = new FuncPtr("USER32.DLL", "MessageBoxW");
            NakedByteStream nbs = new NakedByteStream();
            LittleEndianOutputStream leos = new LittleEndianOutputStream(nbs);
            leos.writeInt(0);
            leos.writeStringUnicode("Generic Hello From a DLL");
            leos.writeStringUnicode("From Jawin");
            leos.writeInt(0);
            msgBox.invoke("IGGI:I:", 16, nbs, null, ReturnFlags.CHECK_FALSE);
        } catch (COMException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (msgBox != null) {
                try {
                    msgBox.close();
                } catch (COMException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }
}
