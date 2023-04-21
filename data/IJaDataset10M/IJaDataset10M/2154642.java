package ganglia.xdr.v31x;

import org.acplt.oncrpc.*;
import java.io.IOException;

public class Ganglia_gmetric_ushort implements XdrAble {

    public Ganglia_metric_id metric_id;

    public String fmt;

    public short us;

    public Ganglia_gmetric_ushort() {
    }

    public Ganglia_gmetric_ushort(XdrDecodingStream xdr) throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr) throws OncRpcException, IOException {
        metric_id.xdrEncode(xdr);
        xdr.xdrEncodeString(fmt);
        xdr.xdrEncodeShort(us);
    }

    public void xdrDecode(XdrDecodingStream xdr) throws OncRpcException, IOException {
        metric_id = new Ganglia_metric_id(xdr);
        fmt = xdr.xdrDecodeString();
        us = xdr.xdrDecodeShort();
    }
}
