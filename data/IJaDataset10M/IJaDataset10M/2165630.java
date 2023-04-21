package de.fzj.pkikits;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import com.novosec.pkix.asn1.cmp.PKIMessage;

public class PKIMessageInputStream extends ASN1InputStream {

    public PKIMessageInputStream(byte[] input) {
        super(input);
    }

    public PKIMessageInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public PKIMessage readMessage() throws IOException {
        return PKIMessage.getInstance((DERObject) readObject());
    }
}
