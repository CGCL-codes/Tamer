package org.eclipse.swt.internal.ole.win32;

public class IStream extends IUnknown {

    public IStream(int address) {
        super(address);
    }

    public int Clone(int[] ppstm) {
        return COM.VtblCall(13, address, ppstm);
    }

    public int Commit(int grfCommitFlags) {
        return COM.VtblCall(8, address, grfCommitFlags);
    }

    public int Read(int pv, int cb, int[] pcbWritten) {
        return COM.VtblCall(3, address, pv, cb, pcbWritten);
    }

    public int Revert() {
        return COM.VtblCall(9, address);
    }

    public int Write(int pv, int cb, int[] pcbWritten) {
        return COM.VtblCall(4, address, pv, cb, pcbWritten);
    }
}
