package de.felixbruns.jotify.protocol;

import java.nio.ByteBuffer;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.interfaces.DHPublicKey;
import de.felixbruns.jotify.crypto.DH;
import de.felixbruns.jotify.crypto.Hash;
import de.felixbruns.jotify.crypto.RSA;
import de.felixbruns.jotify.crypto.RandomBytes;
import de.felixbruns.jotify.crypto.Shannon;
import de.felixbruns.jotify.crypto.DH.*;
import de.felixbruns.jotify.crypto.RSA.RSAKeyPair;
import de.felixbruns.jotify.exceptions.AuthenticationException;
import de.felixbruns.jotify.exceptions.ConnectionException;
import de.felixbruns.jotify.exceptions.ProtocolException;

public class Session {

    private Protocol protocol;

    protected int clientId;

    protected int clientOs;

    protected int clientRevision;

    protected byte[] clientRandom;

    protected byte[] serverRandom;

    protected byte[] serverBlob;

    protected byte[] username;

    protected byte[] password;

    protected byte[] salt;

    protected byte[] authHash;

    protected String country;

    protected DHKeyPair dhClientKeyPair;

    protected DHPublicKey dhServerPublicKey;

    protected byte[] dhSharedKey;

    protected RSAKeyPair rsaClientKeyPair;

    protected byte[] keyHmac;

    protected byte[] authHmac;

    protected byte[] keyRecv;

    protected byte[] keySend;

    protected int keyRecvIv;

    protected int keySendIv;

    protected Shannon shannonSend;

    protected Shannon shannonRecv;

    protected int puzzleDenominator;

    protected int puzzleMagic;

    protected byte[] puzzleSolution;

    protected byte[] cacheHash;

    protected byte[] initialClientPacket;

    protected byte[] initialServerPacket;

    protected static final int CLIENT_OS_WINDOWS_X86 = 0x00000000;

    protected static final int CLIENT_OS_MACOSX_X86 = 0x00000100;

    protected static final int CLIENT_OS_UNKNOWN_1 = 0x00000200;

    protected static final int CLIENT_OS_UNKNOWN_2 = 0x00000300;

    protected static final int CLIENT_OS_UNKNOWN_3 = 0x00000400;

    protected static final int CLIENT_OS_MACOSX_PPC = 0x00000500;

    protected static final int CLIENT_OS_UNKNOWN_4 = 0x00000600;

    protected static final int CLIENT_ID = 0x01040101;

    protected static final int CLIENT_REVISION = 0xFFFFFFFF;

    public Session() {
        this.protocol = new Protocol(this);
        this.clientId = CLIENT_ID;
        this.clientOs = CLIENT_OS_WINDOWS_X86;
        this.clientRevision = CLIENT_REVISION;
        this.clientRandom = new byte[16];
        this.serverRandom = new byte[16];
        RandomBytes.randomBytes(this.clientRandom);
        this.serverBlob = new byte[256];
        this.username = null;
        this.password = null;
        this.salt = new byte[10];
        this.authHash = new byte[20];
        this.dhClientKeyPair = DH.generateKeyPair(768);
        this.dhSharedKey = new byte[96];
        this.rsaClientKeyPair = RSA.generateKeyPair(1024);
        this.keyHmac = new byte[20];
        this.authHmac = new byte[20];
        this.keyRecv = new byte[32];
        this.keySend = new byte[32];
        this.keyRecvIv = 0;
        this.keySendIv = 0;
        this.shannonRecv = new Shannon();
        this.shannonSend = new Shannon();
        this.puzzleDenominator = 0;
        this.puzzleMagic = 0;
        this.puzzleSolution = new byte[8];
        this.cacheHash = new byte[] { (byte) 0xf4, (byte) 0xc2, (byte) 0xaa, (byte) 0x05, (byte) 0xe8, (byte) 0x25, (byte) 0xa7, (byte) 0xb5, (byte) 0xe4, (byte) 0xe6, (byte) 0x59, (byte) 0x0f, (byte) 0x3d, (byte) 0xd0, (byte) 0xbe, (byte) 0x0a, (byte) 0xef, (byte) 0x20, (byte) 0x51, (byte) 0x95 };
        this.cacheHash[0] = (byte) new Random().nextInt();
        this.initialClientPacket = null;
        this.initialServerPacket = null;
    }

    public String getUsername() {
        return new String(this.username);
    }

    public RSAPublicKey getRSAPublicKey() {
        return this.rsaClientKeyPair.getPublicKey();
    }

    public Protocol authenticate(String username, String password) throws ConnectionException, AuthenticationException {
        int tries = 3;
        this.username = username.getBytes();
        this.password = password.getBytes();
        while (true) {
            this.protocol.connect();
            try {
                this.protocol.sendInitialPacket();
                this.protocol.receiveInitialPacket();
                break;
            } catch (ProtocolException e) {
                if (tries-- > 0) {
                    continue;
                }
                throw new AuthenticationException(e.getMessage(), e);
            }
        }
        this.generateAuthHash();
        this.dhSharedKey = DH.computeSharedKey(this.dhClientKeyPair.getPrivateKey(), this.dhServerPublicKey);
        ByteBuffer buffer = ByteBuffer.allocate(this.authHash.length + this.clientRandom.length + this.serverRandom.length + 1);
        buffer.put(this.authHash);
        buffer.put(this.clientRandom);
        buffer.put(this.serverRandom);
        buffer.put((byte) 0x00);
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        byte[] hmac = new byte[5 * 20];
        int offset = 0;
        buffer.get(bytes);
        for (int i = 1; i <= 5; i++) {
            bytes[bytes.length - 1] = (byte) i;
            Hash.hmacSha1(bytes, this.dhSharedKey, hmac, offset);
            for (int j = 0; j < 20; j++) {
                bytes[j] = hmac[offset + j];
            }
            offset += 20;
        }
        this.keySend = Arrays.copyOfRange(hmac, 20, 20 + 32);
        this.keyRecv = Arrays.copyOfRange(hmac, 52, 52 + 32);
        this.shannonSend.key(this.keySend);
        this.shannonRecv.key(this.keyRecv);
        this.keyHmac = Arrays.copyOfRange(hmac, 0, 20);
        this.solvePuzzle();
        this.generateAuthHmac();
        try {
            this.protocol.sendAuthenticationPacket();
            this.protocol.receiveAuthenticationPacket();
        } catch (ProtocolException e) {
            throw new AuthenticationException(e.getMessage(), e);
        }
        return this.protocol;
    }

    private void generateAuthHash() {
        ByteBuffer buffer = ByteBuffer.allocate(this.salt.length + 1 + this.password.length);
        buffer.put(this.salt);
        buffer.put((byte) ' ');
        buffer.put(this.password);
        this.authHash = Hash.sha1(buffer.array());
    }

    private void generateAuthHmac() {
        ByteBuffer buffer = ByteBuffer.allocate(this.initialClientPacket.length + this.initialServerPacket.length + 1 + 1 + 2 + 4 + 0 + this.puzzleSolution.length);
        buffer.put(this.initialClientPacket);
        buffer.put(this.initialServerPacket);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        buffer.putShort((short) this.puzzleSolution.length);
        buffer.putInt(0x0000000);
        buffer.put(this.puzzleSolution);
        this.authHmac = Hash.hmacSha1(buffer.array(), this.keyHmac);
    }

    private void solvePuzzle() {
        long denominator, nominatorFromHash;
        byte[] digest;
        ByteBuffer buffer = ByteBuffer.allocate(this.serverRandom.length + this.puzzleSolution.length);
        denominator = 1 << this.puzzleDenominator;
        denominator--;
        do {
            RandomBytes.randomBytes(this.puzzleSolution);
            buffer.clear();
            buffer.put(this.serverRandom);
            buffer.put(this.puzzleSolution);
            digest = Hash.sha1(buffer.array());
            nominatorFromHash = ((digest[16] & 0xFF) << 24) | ((digest[17] & 0xFF) << 16) | ((digest[18] & 0xFF) << 8) | ((digest[19] & 0xFF));
            nominatorFromHash ^= this.puzzleMagic;
        } while ((nominatorFromHash & denominator) != 0);
    }
}
