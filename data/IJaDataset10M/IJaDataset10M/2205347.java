package tests.org.acplt.oncrpc.jrpcgen;

import org.acplt.oncrpc.*;
import java.io.IOException;

public class ANSWER implements XdrAble, java.io.Serializable {

    public int value;

    public int wrong;

    public int the_answer;

    public int check_hash;

    private static final long serialVersionUID = 5165359675382683141L;

    public ANSWER() {
    }

    public ANSWER(XdrDecodingStream xdr) throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr) throws OncRpcException, IOException {
        xdr.xdrEncodeInt(value);
        switch(value) {
            case 40:
            case 41:
                xdr.xdrEncodeInt(wrong);
                break;
            case 42:
                xdr.xdrEncodeInt(the_answer);
                break;
            default:
                xdr.xdrEncodeInt(check_hash);
                break;
        }
    }

    public void xdrDecode(XdrDecodingStream xdr) throws OncRpcException, IOException {
        value = xdr.xdrDecodeInt();
        switch(value) {
            case 40:
            case 41:
                wrong = xdr.xdrDecodeInt();
                break;
            case 42:
                the_answer = xdr.xdrDecodeInt();
                break;
            default:
                check_hash = xdr.xdrDecodeInt();
                break;
        }
    }
}
