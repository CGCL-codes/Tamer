package org.openthinclient.nfsd.tea;

import java.io.IOException;
import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.XdrAble;
import org.acplt.oncrpc.XdrDecodingStream;
import org.acplt.oncrpc.XdrEncodingStream;

public class nfstime implements XdrAble {

    public int seconds;

    public int useconds;

    public nfstime(long time) {
        seconds = (int) (time / 1000);
        useconds = (int) (time % 1000);
    }

    public nfstime(XdrDecodingStream xdr) throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public nfstime() {
    }

    public void xdrEncode(XdrEncodingStream xdr) throws OncRpcException, IOException {
        xdr.xdrEncodeInt(seconds);
        xdr.xdrEncodeInt(useconds);
    }

    public void xdrDecode(XdrDecodingStream xdr) throws OncRpcException, IOException {
        seconds = xdr.xdrDecodeInt();
        useconds = xdr.xdrDecodeInt();
    }
}
