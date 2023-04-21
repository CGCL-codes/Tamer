package org.tranche.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.tranche.commons.DebugUtil;
import org.tranche.security.Signature;
import org.tranche.flatfile.FlatFileTrancheServer;
import org.tranche.hash.BigHash;
import org.tranche.hash.span.HashSpan;
import org.tranche.remote.RemoteUtil;
import org.tranche.remote.Token;
import org.tranche.util.DevUtil;
import org.tranche.util.IOUtil;
import org.tranche.security.SecurityUtil;
import org.tranche.util.TestNetwork;
import org.tranche.util.TestServerConfiguration;
import org.tranche.util.TestUtil;
import org.tranche.util.TrancheTestCase;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class DeleteMetaDataItemTest extends TrancheTestCase {

    @Override()
    protected void setUp() throws Exception {
        super.setUp();
        DebugUtil.setDebug(FlatFileTrancheServer.class, true);
        DebugUtil.setDebug(Server.class, true);
    }

    @Override()
    protected void tearDown() throws Exception {
        super.tearDown();
        DebugUtil.setDebug(FlatFileTrancheServer.class, false);
        DebugUtil.setDebug(Server.class, false);
    }

    public void testDoAction() throws Exception {
        TestUtil.printTitle("DeleteMetaDataItemTest:testDoAction()");
        String HOST1 = "server1.com";
        TestNetwork testNetwork = new TestNetwork();
        testNetwork.addTestServerConfiguration(TestServerConfiguration.generateForDataServer(443, HOST1, 1500, "127.0.0.1", true, true, false, HashSpan.FULL_SET, DevUtil.DEV_USER_SET));
        try {
            testNetwork.start();
            Server s = testNetwork.getServer(HOST1);
            FlatFileTrancheServer ffts = testNetwork.getFlatFileTrancheServer(HOST1);
            byte[] bytes = DevUtil.createRandomMetaDataChunk();
            BigHash hash = new BigHash(bytes);
            PropagationReturnWrapper pew = IOUtil.setMetaData(ffts, DevUtil.getDevAuthority(), DevUtil.getDevPrivateKey(), false, hash, bytes);
            assertFalse(pew.isAnyErrors());
            DeleteMetaDataItem item = new DeleteMetaDataItem(s);
            byte[][] nonces = new byte[1][];
            byte[] nonce = IOUtil.getNonce(ffts);
            nonces[0] = nonce;
            byte[] hashBytes = hash.toByteArray();
            byte[] sigBytes = new byte[nonce.length + hash.toByteArray().length];
            System.arraycopy(nonce, 0, sigBytes, 0, nonce.length);
            System.arraycopy(hashBytes, 0, sigBytes, nonce.length, hashBytes.length);
            String algorithm = SecurityUtil.getSignatureAlgorithm(DevUtil.getDevPrivateKey());
            byte[] sig = SecurityUtil.sign(new ByteArrayInputStream(sigBytes), DevUtil.getDevPrivateKey(), algorithm);
            Signature[] signatures = new Signature[1];
            Signature signature = new Signature(sig, algorithm, DevUtil.getDevAuthority());
            signatures[0] = signature;
            ByteArrayOutputStream request = new ByteArrayOutputStream();
            DeleteMetaDataItem.writeRequest(false, hash, null, null, null, new String[] { HOST1 }, signatures, nonces, request);
            ByteArrayOutputStream response = new ByteArrayOutputStream();
            item.doAction(new ByteArrayInputStream(request.toByteArray()), response, "localhost");
            ByteArrayInputStream in = new ByteArrayInputStream(response.toByteArray());
            assertEquals(Token.OK_STRING, RemoteUtil.readLine(in));
            PropagationReturnWrapper wrapper = PropagationReturnWrapper.createFromBytes(RemoteUtil.readDataBytes(in));
            assertEquals(0, wrapper.getErrors().size());
            assertFalse(IOUtil.hasMetaData(ffts, hash));
        } finally {
            testNetwork.stop();
        }
    }
}
