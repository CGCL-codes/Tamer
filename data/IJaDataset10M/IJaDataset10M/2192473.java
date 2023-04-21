package org.waveprotocol.wave.crypto;

import com.google.common.collect.ImmutableList;
import junit.framework.TestCase;
import org.apache.commons.codec.binary.Base64;
import org.waveprotocol.wave.federation.Proto.ProtocolSignature;
import org.waveprotocol.wave.federation.Proto.ProtocolSignature.SignatureAlgorithm;
import org.waveprotocol.wave.federation.Proto.ProtocolSignerInfo.HashAlgorithm;

public class WaveSignerTest extends TestCase {

    private static final String DOMAIN = "example.com";

    private WaveSigner signer;

    private SignerInfo signerInfo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        signerInfo = new SignerInfo(HashAlgorithm.SHA256, ImmutableList.of(CertConstantUtil.SERVER_PUB_CERT, CertConstantUtil.INTERMEDIATE_PUB_CERT), DOMAIN);
        signer = new WaveSigner(SignatureAlgorithm.SHA1_RSA, CertConstantUtil.SERVER_PRIV_KEY, signerInfo);
    }

    public void testSign() throws Exception {
        byte[] payload = "hello".getBytes();
        ProtocolSignature signature = signer.sign(payload);
        assertEquals(SignatureAlgorithm.SHA1_RSA, signature.getSignatureAlgorithm());
        assertEquals("zBYbw+lLkXGao+LfNWbv/faS+yAlsAmUfCNqXBxeFtI=", base64(signature.getSignerId().toByteArray()));
        assertEquals("TMX5+6tJnEfso3KnbWygPfGBKXtFjRk6K/SQHyj+O5/dMuGeh5n/Da3v/" + "Cq13LcRie18dxUWMginQUGrsgseqse5orT0C4i0P6ybSxwUZ8OfFnx3lD5K4ME" + "ceB+yAMCsnoUZA/F52ullE/aMpv9LIFmNl4QtlvKJmF3UlJCJe/M=", base64(signature.getSignatureBytes().toByteArray()));
    }

    public void testSpeed() throws Exception {
        byte[] payload = "hello".getBytes();
        long start = System.currentTimeMillis();
        long ops = 0;
        while (System.currentTimeMillis() < start + 1000L) {
            ProtocolSignature signature = signer.sign(payload);
            ++ops;
        }
        long stop = System.currentTimeMillis();
        System.out.println(String.format("%.2f ms per signature", (stop - start) / (double) ops));
    }

    private String base64(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }
}
