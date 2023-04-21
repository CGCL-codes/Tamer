package tests.com;

import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.pfyshnet.bc_codec.PfyshLevel;
import org.pfyshnet.bc_codec.PfyshNodePrivateKeys;
import org.pfyshnet.bc_codec.PfyshNodePublicKeys;
import org.pfyshnet.com.PfyshCom;
import org.pfyshnet.com.PfyshComSettings;
import org.pfyshnet.core.CoreComInterface;
import org.pfyshnet.core.DataStore;
import org.pfyshnet.core.DataTransfer;
import org.pfyshnet.core.EncodedTransferFromNode;
import org.pfyshnet.core.GroupKey;
import org.pfyshnet.core.GroupQuery;
import org.pfyshnet.core.Level;
import org.pfyshnet.core.MyNodeInfo;
import org.pfyshnet.core.NodeHello;
import org.pfyshnet.core.Push;
import org.pfyshnet.core.RouteTransfer;
import org.pfyshnet.core.SearchData;
import org.pfyshnet.core.SearchSpecification;
import org.pfyshnet.core.StoreRequest;

public class ComTester implements CoreComInterface {

    private PfyshCom Com;

    private MyNodeInfo MyData;

    private PfyshComSettings ComSet;

    private boolean Hit[];

    public ComTester() {
        Hit = new boolean[11];
        for (int cnt = 0; cnt < 11; cnt++) {
            Hit[cnt] = false;
        }
    }

    private Level[] CalculateLevels(PfyshNodePublicKeys pub) {
        RIPEMD128Digest dig = new RIPEMD128Digest();
        byte[] pbytes = ((ElGamalPublicKeyParameters) pub.getEncryptionKey()).getParameters().getP().toByteArray();
        dig.update(pbytes, 0, pbytes.length);
        byte[] hash128 = new byte[dig.getDigestSize()];
        dig.doFinal(hash128, 0);
        ByteBuffer buf = ByteBuffer.wrap(hash128);
        Level[] levels = new Level[31];
        long fullid = buf.getLong();
        for (int cnt = 0; cnt <= 30; cnt++) {
            levels[cnt] = new PfyshLevel(fullid, cnt);
        }
        return levels;
    }

    @SuppressWarnings("unchecked")
    public void Go(String testfilename) {
        File testfile = new File(testfilename);
        PfyshNodePublicKeys pub = new PfyshNodePublicKeys();
        PfyshNodePrivateKeys priv = new PfyshNodePrivateKeys();
        SecureRandom srand = new SecureRandom();
        ElGamalParametersGenerator generator = new ElGamalParametersGenerator();
        generator.init(768, 5, srand);
        ElGamalParameters ElGamalParms = generator.generateParameters();
        ElGamalKeyGenerationParameters genparms = new ElGamalKeyGenerationParameters(srand, ElGamalParms);
        ElGamalKeyPairGenerator keygen = new ElGamalKeyPairGenerator();
        keygen.init(genparms);
        AsymmetricCipherKeyPair keypair = keygen.generateKeyPair();
        pub.setEncryptionKey((ElGamalPublicKeyParameters) keypair.getPublic());
        priv.setDecryptionKey((ElGamalPrivateKeyParameters) keypair.getPrivate());
        RSAKeyGenerationParameters rsaparms = new RSAKeyGenerationParameters(BigInteger.valueOf(1001), srand, 768, 10);
        RSAKeyPairGenerator rsagen = new RSAKeyPairGenerator();
        rsagen.init(rsaparms);
        AsymmetricCipherKeyPair rsapair = rsagen.generateKeyPair();
        pub.setVerificationKey((RSAKeyParameters) rsapair.getPublic());
        priv.setSignatureKey((RSAPrivateCrtKeyParameters) rsapair.getPrivate());
        MyData = new MyNodeInfo();
        NodeHello nh = new NodeHello();
        nh.setPublicKey(pub);
        nh.setSignature(new byte[20]);
        nh.setConnectionLocation("127.0.0.1;30020");
        MyData.setPrivateKey(priv);
        MyData.setLevels(CalculateLevels(pub));
        MyData.setNode(nh);
        ComSet = new PfyshComSettings();
        ComSet.setPort(30020);
        ComSet.setRootDir("comroot");
        ComSet.setMaxIncoming(5);
        ComSet.setMaxOutgoing(5);
        Com = new PfyshCom(this);
        Com.setMyNodeInfo(MyData);
        LinkedList<DataTransfer> xfers = new LinkedList<DataTransfer>();
        DataStore datastore = new DataStore();
        datastore.setData(testfile);
        datastore.setLevel(new PfyshLevel(10L, 10));
        datastore.setTag(10);
        TestTransfer tt = new TestTransfer(nh, datastore);
        xfers.add(tt);
        Com.PushStore(xfers);
        xfers.clear();
        GroupQuery gq = new GroupQuery();
        gq.setBackTime(10L);
        gq.setLevel(new PfyshLevel(10L, 10));
        gq.setNode(nh);
        tt = new TestTransfer(nh, gq);
        xfers.add(tt);
        Com.QueryForData(xfers);
        xfers.clear();
        gq = new GroupQuery();
        gq.setBackTime(10L);
        gq.setLevel(new PfyshLevel(10L, 10));
        gq.setNode(nh);
        tt = new TestTransfer(nh, gq);
        xfers.add(tt);
        Com.QueryForKeys(xfers);
        xfers.clear();
        tt = new TestTransfer(nh, nh);
        xfers.add(tt);
        Com.QueryForNodes(xfers);
        xfers.clear();
        tt = new TestTransfer(nh, nh);
        xfers.add(tt);
        Com.QueryForSearchSpecs(xfers);
        xfers.clear();
        SearchSpecification ss = new SearchSpecification();
        LinkedList<ElGamalPublicKeyParameters> keys = new LinkedList<ElGamalPublicKeyParameters>();
        keys.add((ElGamalPublicKeyParameters) pub.getEncryptionKey());
        keys.add((ElGamalPublicKeyParameters) pub.getEncryptionKey());
        keys.add((ElGamalPublicKeyParameters) pub.getEncryptionKey());
        ss.setGroupKeys((List) keys);
        tt = new TestTransfer(nh, ss);
        xfers.add(tt);
        Com.SearchSpecification(xfers);
        xfers.clear();
        LinkedList<NodeHello> nl = new LinkedList<NodeHello>();
        nl.add(nh);
        nl.add(nh);
        tt = new TestTransfer(nh, nl);
        xfers.add(tt);
        Com.SendHellos(xfers);
        xfers.clear();
        LinkedList<GroupKey> gkl = new LinkedList<GroupKey>();
        GroupKey gk = new GroupKey();
        gk.setLevel(new PfyshLevel(10L, 10));
        gk.setPrivateKey(priv.getDecryptionKey());
        gk.setPublicKey(pub.getEncryptionKey());
        gk.setSignature(new byte[10]);
        gk.setSourceNode(nh);
        gkl.add(gk);
        gkl.add(gk);
        tt = new TestTransfer(nh, gkl);
        xfers.add(tt);
        Com.SendNewKeys(xfers);
        LinkedList<RouteTransfer> rxfers = new LinkedList<RouteTransfer>();
        TestRouteTransfer tr = new TestRouteTransfer();
        tr.setDepth(5);
        tr.setDestination(nh);
        tr.setHops(3);
        tr.setPayload(testfile);
        rxfers.add(tr);
        Com.SendRoutes(rxfers);
        xfers.clear();
        SearchData sd = new SearchData();
        sd.setData(testfile);
        sd.setDepth(5);
        sd.setFullID(25L);
        sd.setTag(13L);
        tt = new TestTransfer(nh, sd);
        xfers.add(tt);
        Com.SendSearchData(xfers);
        xfers.clear();
        LinkedList<SearchSpecification> speclist = new LinkedList<SearchSpecification>();
        speclist.add(ss);
        speclist.add(ss);
        tt = new TestTransfer(nh, speclist);
        xfers.add(tt);
        Com.SendSearchSpecQuery(xfers);
        xfers.clear();
        StoreRequest sr = new StoreRequest();
        sr.setData(testfile);
        sr.setEncodedReturn(testfile);
        KeyParameter kp = new KeyParameter(new byte[10]);
        ParametersWithIV piv = new ParametersWithIV(kp, new byte[10]);
        sr.setReturnKey(piv);
        tt = new TestTransfer(nh, sr);
        xfers.add(tt);
        Com.SendStores(xfers);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean allpass = true;
        for (int cnt = 0; cnt < 11; cnt++) {
            if (Hit[cnt]) {
                System.out.println("PASS: " + cnt);
            } else {
                System.out.println("FAIL: " + cnt);
                allpass = false;
            }
        }
        if (allpass) {
            System.out.println("ALL PASSED!");
        } else {
            System.out.println("THERE WAS A FAILURE.");
        }
    }

    public void Hello(NodeHello node) {
        System.out.println("HELLO! " + node.getConnectionLocation());
        Hit[0] = true;
    }

    public void NewData(EncodedTransferFromNode xfer) {
        System.out.println("NEW ROUTE DATA: " + xfer.getSourceNode());
        System.out.println("NEW ROUTE DATA: " + xfer.getTransfer().getHops());
        System.out.println("NEW ROUTE DATA: " + xfer.getTransfer().getLayerDepth());
        Hit[1] = true;
    }

    public void NewGroupKey(GroupKey newkey) {
        System.out.println("GROUP KEY: " + newkey.getSourceNode());
        System.out.println("GROUP KEY: " + newkey.getLevel().getDepth());
        Hit[2] = true;
    }

    public void PushData(Push p) {
        System.out.println("PUSH FROM: " + p.getSendingNode().getConnectionLocation());
        System.out.println("PUSH TAG: " + p.getDataStore().getTag());
        System.out.println("PUSH LEVEL ID: " + (Long) p.getDataStore().getLevel().getID());
        System.out.println("PUSH LEVEL DEPTH: " + p.getDataStore().getLevel().getDepth());
        System.out.println("PUSH DATA: " + p.getDataStore().getData());
        Hit[3] = true;
    }

    public void QueryForData(GroupQuery q) {
        System.out.println("DATA QUERY BACKTIME: " + q.getBackTime());
        Hit[4] = true;
    }

    public void QueryForKeys(GroupQuery key) {
        System.out.println("QUERY FOR KEYS: " + key.getBackTime());
        Hit[5] = true;
    }

    public void QueryForNodes(NodeHello node) {
        System.out.println("QUERY FOR NODES: " + node.getConnectionLocation());
        Hit[6] = true;
    }

    public void QueryForSearchSpec(NodeHello node) {
        System.out.println("QUERY FOR SEARCHSPEC: " + node.getConnectionLocation());
        Hit[7] = true;
    }

    public void SearchSpecification(org.pfyshnet.core.SearchSpecification s) {
        System.out.println("SEARCH SPEC: " + s.getGroupKeys().size());
        Hit[8] = true;
    }

    public void StoreRequest(org.pfyshnet.core.StoreRequest request) {
        System.out.println("STORE REQ: " + request.getData());
        System.out.println("STORE REQ: " + request.getEncodedReturn());
        Hit[9] = true;
    }

    public void StoreSearch(SearchData data) {
        System.out.println("SEARCH DATA: " + data.getDepth());
        System.out.println("SEARCH DATA: " + data.getTag());
        Hit[10] = true;
    }

    public void UpdateConnectionInfo(String connectinfo) {
        MyData.getNode().setConnectionLocation(connectinfo);
    }

    public Object getComSettings() {
        return ComSet;
    }

    public MyNodeInfo getMyData() {
        return MyData;
    }

    public void setComSettings(Object set) {
    }

    private class TestTransfer extends DataTransfer {

        public TestTransfer(NodeHello dest, Object payload) {
            setDestination(dest);
            setPayload(payload);
        }

        public void Failure() {
            System.out.println("TRANSFER FAILURE");
            Thread.dumpStack();
        }

        public void Success() {
            System.out.println("TRANSFER SUCCESS");
        }
    }

    private class TestRouteTransfer extends RouteTransfer {

        public void Failure() {
            System.out.println("ROUTE TRANSFER FAILURE!");
        }

        public void Success() {
            System.out.println("ROUTE TRANSFER SUCCESS!");
        }
    }

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("args: testfile");
            System.exit(0);
        }
        ComTester ct = new ComTester();
        ct.Go(args[0]);
    }
}
