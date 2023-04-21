package org.openthinclient.nfsd.tea;

import java.io.IOException;
import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.XdrAble;
import org.acplt.oncrpc.XdrDecodingStream;
import org.acplt.oncrpc.XdrEncodingStream;

public class filename implements XdrAble {

    public String value;

    public filename() {
    }

    public filename(String value) {
        this.value = value;
    }

    public filename(XdrDecodingStream xdr) throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr) throws OncRpcException, IOException {
        xdr.xdrEncodeString(value);
    }

    public void xdrDecode(XdrDecodingStream xdr) throws OncRpcException, IOException {
        value = xdr.xdrDecodeString();
    }
}
