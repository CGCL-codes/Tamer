package antiquity.gw.impl;

import antiquity.util.Extent;
import antiquity.util.XdrUtils;
import static antiquity.util.AntiquityUtils.createSignature;
import static antiquity.util.AntiquityUtils.GW_NULL_GUID;
import antiquity.gw.api.gw_create_args;
import antiquity.gw.api.gw_append_args;
import antiquity.gw.api.gw_snapshot_args;
import antiquity.gw.api.gw_put_args;
import antiquity.gw.api.gw_get_blocks_args;
import antiquity.gw.api.gw_get_extent_args;
import antiquity.gw.api.gw_get_certificate_args;
import antiquity.gw.api.gw_create_result;
import antiquity.gw.api.gw_append_result;
import antiquity.gw.api.gw_snapshot_result;
import antiquity.gw.api.gw_put_result;
import antiquity.gw.api.gw_get_blocks_result;
import antiquity.gw.api.gw_get_extent_result;
import antiquity.gw.api.gw_get_certificate_result;
import antiquity.gw.api.gw_api;
import antiquity.gw.api.gw_guid;
import antiquity.gw.api.gw_status;
import antiquity.gw.api.gw_public_key;
import antiquity.gw.api.gw_data_block;
import antiquity.gw.api.gw_certificate;
import antiquity.gw.api.gw_signed_certificate;
import antiquity.gw.api.gw_apiClient;
import static antiquity.gw.api.gw_api.GW_KEY_VERIFIED;
import static antiquity.gw.api.gw_api.GW_HASH_VERIFIED;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ListIterator;
import ostore.util.ByteUtils;
import ostore.util.SecureHash;
import ostore.util.SHA1Hash;

public class ClientSyncExample {

    private String[] args;

    private Signature sig_engine;

    private PrivateKey skey;

    private PublicKey pkey;

    private gw_public_key ant_pkey;

    private MessageDigest _md;

    private Random _rand;

    private long _next_cert_seq;

    private static final int MAX_TTL = gw_api.GW_MAX_TTL;

    public static void main(String[] args) throws Exception {
        try {
            ClientSyncExample client_ex = new ClientSyncExample(args);
            client_ex.test();
        } catch (Exception e) {
            BUG("Test failed with exception " + e + ".");
        }
        System.exit(0);
    }

    public ClientSyncExample(String[] args) throws Exception {
        this.args = args;
        _md = MessageDigest.getInstance("SHA");
        _rand = new Random(System.currentTimeMillis());
        _next_cert_seq = _rand.nextInt(Integer.MAX_VALUE / 2);
        return;
    }

    public void test() throws Exception {
        InetAddress addr = InetAddress.getLocalHost();
        int port = 12005;
        int program = 0x28000028;
        int version = 1;
        int protocol = org.acplt.oncrpc.OncRpcProtocols.ONCRPC_TCP;
        System.out.print("Create RPC client...");
        gw_apiClient c = new gw_apiClient(addr, program, version, port, protocol);
        System.out.println("done.");
        System.out.print("Generating key pair...");
        KeyPairGenerator key_gen = KeyPairGenerator.getInstance("RSA");
        int key_size = 1024;
        key_gen.initialize(key_size);
        KeyPair key_pair = key_gen.generateKeyPair();
        skey = key_pair.getPrivate();
        pkey = key_pair.getPublic();
        ant_pkey = new gw_public_key();
        ant_pkey.value = pkey.getEncoded();
        System.out.println("done.");
        System.out.print("Creating signature engine...");
        String sig_alg = "SHA1with" + skey.getAlgorithm();
        if (ostore.security.NativeRSAAlgorithm.available) {
            java.security.Security.addProvider(new ostore.security.NativeRSAProvider());
            sig_engine = Signature.getInstance("SHA-1/RSA/PKCS#1", "NativeRSA");
        } else {
            sig_engine = Signature.getInstance(sig_alg);
        }
        sig_engine.initSign(skey);
        System.out.println("done.");
        System.out.print("Calling null procedure...");
        c.gw_null_1();
        System.out.println("done.");
        System.out.print("Calling put procedure...");
        gw_data_block[] put_blocks = createDataBlocks(10, 1024);
        Extent<gw_public_key, gw_guid, gw_signed_certificate> put_extent = new Extent<gw_public_key, gw_guid, gw_signed_certificate>(ant_pkey, gw_signed_certificate.class, _md);
        put_extent.appendBlocks(put_blocks);
        long now_ms = System.currentTimeMillis();
        long expire_time_ms = now_ms + (long) MAX_TTL;
        gw_signed_certificate put_cert = put_extent.createCertificate(GW_HASH_VERIFIED, _rand.nextInt(Integer.MAX_VALUE / 2), now_ms, expire_time_ms, new byte[0], new byte[0]);
        put_cert.signature = createSignature(put_cert.cert, skey, sig_engine);
        gw_put_args put_args = new gw_put_args();
        put_args.cert = put_cert;
        put_args.data = put_blocks;
        gw_put_result put_result = c.gw_put_1(put_args);
        if (put_result.status != gw_status.GW_STATUS_OK) BUG("returned error.");
        System.out.println("done.");
        System.out.println("  ExtentKey = Verifier = " + XdrUtils.toString(put_result.verifier));
        System.out.print("Calling create procedure...");
        Extent<gw_public_key, gw_guid, gw_signed_certificate> create_extent = new Extent<gw_public_key, gw_guid, gw_signed_certificate>(ant_pkey, gw_signed_certificate.class, _md);
        now_ms = System.currentTimeMillis();
        expire_time_ms = now_ms + (long) MAX_TTL;
        gw_signed_certificate create_cert = create_extent.createCertificate(GW_KEY_VERIFIED, _next_cert_seq++, now_ms, expire_time_ms, new byte[0], new byte[0]);
        create_cert.signature = createSignature(create_cert.cert, skey, sig_engine);
        gw_create_args create_args = new gw_create_args();
        create_args.cert = create_cert;
        gw_create_result create_result = c.gw_create_1(create_args);
        if (create_result.status != gw_status.GW_STATUS_OK) BUG("returned error.");
        System.out.println("done.");
        System.out.print("  ExtentKey = " + XdrUtils.toString(create_result.extent_key));
        System.out.println(",  Verifier = " + XdrUtils.toString(create_result.verifier));
        System.out.print("Calling append procedure...");
        gw_data_block[] append_blocks = createDataBlocks(10, 256);
        create_extent.appendBlocks(append_blocks);
        now_ms = System.currentTimeMillis();
        expire_time_ms = create_cert.cert.timestamp;
        gw_signed_certificate append_cert = create_extent.createCertificate(GW_KEY_VERIFIED, _next_cert_seq++, now_ms, expire_time_ms, new byte[0], new byte[0]);
        append_cert.signature = createSignature(append_cert.cert, skey, sig_engine);
        gw_append_args append_args = new gw_append_args();
        append_args.cert = append_cert;
        append_args.data = append_blocks;
        gw_append_result append_result = c.gw_append_1(append_args);
        if (append_result.status != gw_status.GW_STATUS_OK) BUG("returned error.");
        System.out.println("done.");
        System.out.print("  ExtentKey = " + XdrUtils.toString(append_result.extent_key));
        System.out.println(",  Verifier = " + XdrUtils.toString(append_result.verifier));
        System.out.print("Calling snapshot procedure...");
        now_ms = System.currentTimeMillis();
        expire_time_ms = now_ms + (long) MAX_TTL;
        gw_signed_certificate snapshot_cert = create_extent.createCertificate(GW_HASH_VERIFIED, _rand.nextInt(Integer.MAX_VALUE / 2), now_ms, expire_time_ms, new byte[0], new byte[0]);
        snapshot_cert.signature = createSignature(snapshot_cert.cert, skey, sig_engine);
        gw_snapshot_args snapshot_args = new gw_snapshot_args();
        snapshot_args.cert = snapshot_cert;
        gw_snapshot_result snapshot_result = c.gw_snapshot_1(snapshot_args);
        if (snapshot_result.status != gw_status.GW_STATUS_OK) BUG("returned error.");
        System.out.println("done.");
        System.out.print("  ExtentKeyOld = " + XdrUtils.toString(snapshot_result.extent_key_old));
        System.out.println(",  Verifier = " + XdrUtils.toString(snapshot_result.verifier));
        System.out.print("Calling get_certificate procedure...");
        gw_get_certificate_args get_cert_args = new gw_get_certificate_args();
        get_cert_args.extent_key = snapshot_result.verifier;
        get_cert_args.latest = 1;
        get_cert_args.verbose = 0;
        get_cert_args.use_admin = 0;
        get_cert_args.client_id = GW_NULL_GUID;
        gw_get_certificate_result get_cert_result = c.gw_get_certificate_1(get_cert_args);
        if (get_cert_result.status != gw_status.GW_STATUS_OK) BUG("returned error.");
        System.out.println("done.");
        System.out.print("Calling get_blocks procedure...");
        gw_guid[] get_block_names = new gw_guid[2];
        get_block_names[0] = getBlockName(append_blocks[2]);
        get_block_names[1] = getBlockName(append_blocks[8]);
        gw_get_blocks_args get_blocks_args = new gw_get_blocks_args();
        get_blocks_args.extent_key = snapshot_result.verifier;
        get_blocks_args.block_names = get_block_names;
        gw_get_blocks_result get_blocks_result = c.gw_get_blocks_1(get_blocks_args);
        if (get_blocks_result.status != gw_status.GW_STATUS_OK) BUG("returned error.");
        System.out.println("done.");
        return;
    }

    private gw_data_block[] createDataBlocks(int number, int size) {
        gw_data_block[] blocks = new gw_data_block[number];
        for (int i = 0; i < number; ++i) {
            blocks[i] = createDataBlock(size, (byte) ('a' + i));
        }
        return blocks;
    }

    private gw_guid getBlockName(gw_data_block block) {
        gw_guid block_name = new gw_guid();
        SecureHash block_hash = new SHA1Hash(block.value);
        block_name.value = block_hash.bytes();
        return block_name;
    }

    private gw_data_block createDataBlock(int size, byte val) {
        byte[] data = new byte[size];
        Arrays.fill(data, val);
        gw_data_block block = new gw_data_block();
        block.value = data;
        return block;
    }

    /**
     * Like calling assert(false) in C.
     */
    private static void BUG(String msg) {
        System.err.println(msg);
        Thread.dumpStack();
        System.exit(1);
    }
}
