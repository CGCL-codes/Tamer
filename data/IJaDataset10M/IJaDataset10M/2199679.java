package aimclient.security;

import net.kano.joscar.ByteBlock;
import net.kano.joscar.snaccmd.FullRoomInfo;
import javax.crypto.SecretKey;
import java.security.cert.X509Certificate;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public abstract class SecureSession {

    public static final SecureSession getInstance() {
        try {
            Class cl = Class.forName("net.kano.joscardemo.security.BCSecureSession");
            return (SecureSession) cl.newInstance();
        } catch (Exception e) {
        }
        System.out.println("[couldn't load security package; using null " + "security session class]");
        return new NullSecureSession();
    }

    public abstract X509Certificate getMyCertificate();

    public abstract void setCert(String sn, X509Certificate cert);

    public abstract X509Certificate getCert(String sn);

    public abstract boolean hasCert(String sn);

    public abstract void setChatKey(String roomName, SecretKey chatKey);

    public abstract SecretKey getChatKey(String chat);

    public abstract ByteBlock genChatSecurityInfo(FullRoomInfo chatInfo, String sn) throws SecureSessionException;

    public abstract ByteBlock encryptIM(String sn, String msg) throws SecureSessionException;

    public abstract String parseChatMessage(String chat, String sn, ByteBlock data) throws SecureSessionException;

    public abstract SecretKey extractChatKey(String sn, ByteBlock data) throws SecureSessionException;

    public abstract String decodeEncryptedIM(String sn, ByteBlock encData) throws SecureSessionException;

    public abstract byte[] encryptChatMsg(String chat, String msg) throws SecureSessionException;

    public abstract ServerSocket createSSLServerSocket(String sn) throws SecureSessionException;

    public abstract Socket createSecureSocket(InetAddress address, int port) throws SecureSessionException;

    public abstract void generateKey(String chat) throws SecureSessionException;
}
