package org.openthinclient.nfsd.tea;

import java.io.IOException;
import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.XdrAble;
import org.acplt.oncrpc.XdrDecodingStream;
import org.acplt.oncrpc.XdrEncodingStream;

public class readlinkres implements XdrAble {

    public int status;

    public nfspath data;

    public readlinkres() {
    }

    public readlinkres(XdrDecodingStream xdr) throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr) throws OncRpcException, IOException {
        xdr.xdrEncodeInt(status);
        switch(status) {
            case nfsstat.NFS_OK:
                data.xdrEncode(xdr);
                break;
            default:
                break;
        }
    }

    public void xdrDecode(XdrDecodingStream xdr) throws OncRpcException, IOException {
        status = xdr.xdrDecodeInt();
        switch(status) {
            case nfsstat.NFS_OK:
                data = new nfspath(xdr);
                break;
            default:
                break;
        }
    }
}
