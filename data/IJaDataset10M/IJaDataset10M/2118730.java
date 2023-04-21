package pl.radical.open.gg.packet.out;

import pl.radical.open.gg.GGException;
import pl.radical.open.gg.ILocalStatus;
import pl.radical.open.gg.packet.GGOutgoingPackage;
import pl.radical.open.gg.packet.OutgoingPacket;
import pl.radical.open.gg.packet.dicts.GGHashType;
import pl.radical.open.gg.packet.dicts.GGStatusFlags;
import pl.radical.open.gg.packet.dicts.GGStatuses;
import pl.radical.open.gg.packet.dicts.GGVersion;
import pl.radical.open.gg.utils.GGConversion;
import pl.radical.open.gg.utils.GGUtils;
import java.util.Arrays;
import org.apache.commons.collections.primitives.ArrayByteList;
import org.apache.commons.collections.primitives.ByteList;

/**
 * Login to the Gadu-Gadu network.
 * 
 * <pre>
 * struct gg_login80 {
 *        int uin;              &laquo; numer Gadu-Gadu &raquo;
 *        char language[2];     &laquo; język: &quot;pl&quot; &raquo;
 *        char hash_type;       &laquo; rodzaj funkcji skrótu hasła &raquo;
 *        char hash[64];        &laquo; skrót hasła dopełniony \0 &raquo;
 *        int status;           &laquo; początkowy status połączenia &raquo;
 *        int flags;            &laquo; początkowe flagi połączenia &raquo;
 *        int features;         &laquo; opcje protokołu (0x00000367) &raquo;
 *        int local_ip;         &laquo; lokalny adres połączeń bezpośrednich (nieużywany) &raquo;
 *        short local_port;     &laquo; lokalny port połączeń bezpośrednich (nieużywany) &raquo;
 *        int external_ip;      &laquo; zewnętrzny adres (nieużywany) &raquo;
 *        short external_port;  &laquo; zewnętrzny port (nieużywany) &raquo;
 *        char image_size;      &laquo; maksymalny rozmiar grafiki w KB &raquo;
 *        char unknown2;        &laquo; 0x64 &raquo;
 *        int version_len;      &laquo; długość ciągu z wersją (0x23) &raquo;
 *        char version[];       &laquo; &quot;Gadu-Gadu Client build 10.0.0.10450&quot; (bez \0) &raquo;
 *        int description_size; &laquo; rozmiar opisu &raquo;
 *        char description[];   &laquo; opis (nie musi wystąpić, bez \0) &raquo;
 * };
 * 
 * <pre>
 * 
 * @author <a href="mailto:lukasz.rzanek@radical.com.pl>Łukasz Rżanek</a>
 */
@OutgoingPacket(type = 0x0031, label = "GG_LOGIN80")
public class GGLogin80 implements GGOutgoingPackage {

    public static final int GG_LOGIN80 = 0x0031;

    /**
	 * Gadu-Gadu number that will be used during logging
	 */
    private int uin = -1;

    /**
	 * Language of the client, default to "pl"
	 */
    static final String LANGUAGE = "pl";

    /**
	 * Password that will be used during logging
	 */
    private char[] password = null;

    /**
	 * Login hash type that will be used to authenticate the user
	 */
    private final GGHashType hashType = GGHashType.GG_LOGIN_HASH_SHA1;

    /**
	 * Computed login hash based on seed retreived from Gadu-Gadu server
	 */
    private byte[] loginHash = null;

    /**
	 * Initial status that will be set after logging
	 */
    private int status = GGStatuses.GG_STATUS_AVAIL;

    /**
	 * Starting protocols flags
	 */
    private final int flags = GGStatusFlags.FLAG_UNKNOWN.getValue() + GGStatusFlags.FLAG_RECEIVELINKS.getValue();

    /**
	 * Protocol options - 0x00000367
	 */
    private static final int FEATURES = 0x00000007;

    /**
	 * Local IP
	 */
    private byte[] localIP = new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0 };

    /**
	 * Local port that we are listening on
	 */
    private int localPort = 0;

    /**
	 * ExternalIP
	 */
    private byte[] externalIP = new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0 };

    /**
	 * External port
	 */
    private int externalPort = 0;

    /**
	 * size of image in kilobytes
	 */
    private byte imageSize = (byte) -1;

    /**
	 * Unknown property
	 */
    private static final int UNKNOWN2 = 0x64;

    /**
	 * Version of the client
	 */
    private final int version_len = GGVersion.VERSION_60_1_build_133.getCode();

    /**
	 * Version descriptive string
	 */
    private static final String VERSION = "Gadu-Gadu Client Build 8.0.0.8731";

    /**
	 * The length of the status description
	 */
    private int descriptionSize;

    /**
	 * Description that will be set after successfuly logging
	 */
    private String description = null;

    public GGLogin80(final int uin, final char[] password, final int seed) throws GGException {
        if (uin < 0) {
            throw new IllegalArgumentException("uin cannot be less than 0");
        }
        if (password == null) {
            throw new IllegalArgumentException("password cannot be null");
        }
        this.uin = uin;
        this.password = Arrays.copyOf(password, password.length);
        loginHash = GGUtils.getLoginHash(password, seed, hashType);
    }

    public void setStatus(final ILocalStatus localStatus) {
        if (localStatus == null) {
            throw new IllegalArgumentException("localStatus cannot be null");
        }
        status = GGConversion.getProtocolStatus(localStatus, localStatus.isFriendsOnly(), false);
        if (localStatus.isDescriptionSet()) {
            description = localStatus.getDescription();
            descriptionSize = description.length();
        }
    }

    public int getUin() {
        return uin;
    }

    public char[] getPassword() {
        return password;
    }

    public void setLocalIP(final byte[] localIP) {
        if (localIP == null) {
            throw new IllegalArgumentException("localIP cannot be null");
        }
        if (localIP.length != 4) {
            throw new IllegalArgumentException("localIp table has to have 4 entries");
        }
        this.localIP = Arrays.copyOf(localIP, localIP.length);
    }

    public byte[] getLocalIP() {
        return localIP;
    }

    public void setLocalPort(final int port) {
        if (port < 0) {
            throw new IllegalArgumentException("port cannot be null");
        }
        localPort = port;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setExternalIP(final byte[] externalIP) {
        if (externalIP == null) {
            throw new IllegalArgumentException("externalIP cannot be null");
        }
        if (externalIP.length != 4) {
            throw new IllegalArgumentException("externalIP table has to have 4 entries");
        }
        this.externalIP = Arrays.copyOf(externalIP, externalIP.length);
    }

    public void setExternalPort(final int externalPort) {
        if (externalPort < 0) {
            throw new IllegalArgumentException("port cannot be null");
        }
        this.externalPort = externalPort;
    }

    public void setImageSize(final byte imageSize) {
        if (imageSize < 0) {
            throw new IllegalArgumentException("imageSize cannot be less than 0");
        }
        this.imageSize = imageSize;
    }

    /**
	 * @see pl.radical.open.gg.packet.GGOutgoingPackage#getPacketType()
	 */
    @Override
    public int getPacketType() {
        return GG_LOGIN80;
    }

    /**
	 * @see pl.radical.open.gg.packet.GGOutgoingPackage#getLength()
	 */
    @Override
    public int getLength() {
        int length = 4 + 2 + 4 + 4 + 4 + 1 + 4 + 2 + 4 + 2 + 1 + 1;
        if (description != null) {
            length += description.length() + 1;
        }
        return length;
    }

    /**
	 * @see pl.radical.open.gg.packet.GGOutgoingPackage#getContents()
	 */
    @Override
    public byte[] getContents() {
        final ByteList byteList = new ArrayByteList();
        byteList.add((byte) uin);
        byteList.add((byte) (uin >>> 8));
        byteList.add((byte) (uin >>> 16));
        byteList.add((byte) (uin >>> 24));
        byteList.add(LANGUAGE.getBytes()[0]);
        byteList.add(LANGUAGE.getBytes()[1]);
        byteList.add((byte) hashType.getValue());
        for (int i = 0; i < 64; i++) {
            if (i < loginHash.length) {
                byteList.add(loginHash[i]);
            } else {
                byteList.add(Character.UNASSIGNED);
            }
        }
        byteList.add((byte) status);
        byteList.add((byte) (status >>> 8));
        byteList.add((byte) (status >>> 16));
        byteList.add((byte) (status >>> 24));
        byteList.add((byte) flags);
        byteList.add((byte) (flags >>> 8));
        byteList.add((byte) (flags >>> 16));
        byteList.add((byte) (flags >>> 24));
        byteList.add((byte) FEATURES);
        byteList.add((byte) (FEATURES >>> 8));
        byteList.add((byte) (FEATURES >>> 16));
        byteList.add((byte) (FEATURES >>> 24));
        byteList.add(localIP[0]);
        byteList.add(localIP[1]);
        byteList.add(localIP[2]);
        byteList.add(localIP[3]);
        byteList.add((byte) (localPort & 0xFF));
        byteList.add((byte) (localPort >> 8 & 0xFF));
        byteList.add(externalIP[0]);
        byteList.add(externalIP[1]);
        byteList.add(externalIP[2]);
        byteList.add(externalIP[3]);
        byteList.add((byte) (externalPort & 0xFF));
        byteList.add((byte) (externalPort >> 8 & 0xFF));
        byteList.add(imageSize);
        byteList.add((byte) UNKNOWN2);
        byteList.add((byte) (version_len & 0xFF));
        byteList.add((byte) (version_len >> 8 & 0xFF));
        byteList.add((byte) (version_len >> 16 & 0xFF));
        byteList.add((byte) (version_len >> 24 & 0xFF));
        for (final byte b : VERSION.getBytes()) {
            byteList.add(b);
        }
        byteList.add((byte) (descriptionSize & 0xFF));
        byteList.add((byte) (descriptionSize >> 8 & 0xFF));
        byteList.add((byte) (descriptionSize >> 16 & 0xFF));
        byteList.add((byte) (descriptionSize >> 24 & 0xFF));
        if (description != null) {
            final byte[] descBytes = description.getBytes();
            for (final byte b : descBytes) {
                byteList.add(b);
            }
        }
        return byteList.toArray();
    }
}
