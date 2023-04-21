package org.eclipse.swt.internal.ole.win32;

public class IDataObject extends IUnknown {

    public IDataObject(int address) {
        super(address);
    }

    public int EnumFormatEtc(int dwDirection, int[] ppenumFormatetc) {
        return COM.VtblCall(8, address, dwDirection, ppenumFormatetc);
    }

    public int GetData(FORMATETC pFormatetc, STGMEDIUM pmedium) {
        return COM.VtblCall(3, address, pFormatetc, pmedium);
    }

    public int GetDataHere(FORMATETC pFormatetc, STGMEDIUM pmedium) {
        return COM.VtblCall(4, address, pFormatetc, pmedium);
    }

    public int QueryGetData(FORMATETC pFormatetc) {
        return COM.VtblCall(5, address, pFormatetc);
    }

    public int SetData(FORMATETC pFormatetc, STGMEDIUM pmedium, boolean fRelease) {
        return COM.VtblCall(7, address, pFormatetc, pmedium, fRelease);
    }
}
