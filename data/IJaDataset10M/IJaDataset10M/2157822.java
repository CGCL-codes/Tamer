package net.sf.jradius.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.LinkedHashMap;
import net.sf.jradius.exception.RadiusException;
import net.sf.jradius.freeradius.FreeRadiusFormat;

/**
 * RADIUS Packet Factor. Parses RADIUS packets and constructs
 * the appropriate Java class instance. 
 *
 * @author David Bird
 */
public class PacketFactory {

    private static LinkedHashMap codeMap = new LinkedHashMap();

    static {
        codeMap.put(new Integer(AccessRequest.CODE), AccessRequest.class);
        codeMap.put(new Integer(AccessAccept.CODE), AccessAccept.class);
        codeMap.put(new Integer(AccessReject.CODE), AccessReject.class);
        codeMap.put(new Integer(AccountingRequest.CODE), AccountingRequest.class);
        codeMap.put(new Integer(AccountingResponse.CODE), AccountingResponse.class);
        codeMap.put(new Integer(AccountingStatus.CODE), AccountingStatus.class);
        codeMap.put(new Integer(PasswordRequest.CODE), PasswordRequest.class);
        codeMap.put(new Integer(PasswordAck.CODE), PasswordAck.class);
        codeMap.put(new Integer(PasswordReject.CODE), PasswordReject.class);
        codeMap.put(new Integer(AccessChallenge.CODE), AccessChallenge.class);
        codeMap.put(new Integer(DisconnectRequest.CODE), DisconnectRequest.class);
        codeMap.put(new Integer(DisconnectACK.CODE), DisconnectACK.class);
        codeMap.put(new Integer(DisconnectNAK.CODE), DisconnectNAK.class);
        codeMap.put(new Integer(CoARequest.CODE), CoARequest.class);
        codeMap.put(new Integer(CoAACK.CODE), CoAACK.class);
        codeMap.put(new Integer(CoANAK.CODE), CoANAK.class);
    }

    /**
     * Parse a UDP RADIUS message
     * @param dp The Datagram to be parsed
     * @return Returns the RadiusPacket
     * @throws RadiusException
     */
    public static RadiusPacket parse(DatagramPacket dp) throws RadiusException {
        ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData());
        DataInputStream input = new DataInputStream(bais);
        RadiusPacket rp = null;
        try {
            int code = RadiusFormat.readUnsignedByte(input);
            int identifier = RadiusFormat.readUnsignedByte(input);
            Class c = (Class) codeMap.get(new Integer(code));
            if (c == null) {
                throw new RadiusException("bad radius code");
            }
            int length = RadiusFormat.readUnsignedShort(input);
            byte[] bAuthenticator = new byte[16];
            input.readFully(bAuthenticator);
            byte[] bAttributes = new byte[length - RadiusPacket.RADIUS_HEADER_LENGTH];
            input.readFully(bAttributes);
            try {
                rp = (RadiusPacket) c.newInstance();
                rp.setCode(code);
                rp.setIdentifier(identifier);
                rp.setAuthenticator(bAuthenticator);
                RadiusFormat.setAttributeBytes(rp, bAttributes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rp;
    }

    private static RadiusPacket parsePacket(DataInputStream input) throws RadiusException, IOException {
        RadiusPacket rp = null;
        int code = RadiusFormat.readUnsignedByte(input);
        int identifier = RadiusFormat.readUnsignedByte(input);
        Class c;
        if (code == 0) {
            c = NullPacket.class;
        } else {
            c = (Class) codeMap.get(new Integer(code));
        }
        if (c == null) {
            throw new RadiusException("bad radius packet type: " + code);
        }
        int length = input.readInt();
        byte[] bAttributes = new byte[length];
        input.readFully(bAttributes);
        try {
            rp = (RadiusPacket) c.newInstance();
            rp.setCode(code);
            rp.setIdentifier(identifier);
            FreeRadiusFormat.setAttributeBytes(rp, bAttributes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rp;
    }

    /**
     * Parse multiple RadiusPackets from a data stream
     * @param input The input data stream
     * @param packetCount Number of packets to expect
     * @return Returns an array of RadiusPackets
     * @throws RadiusException
     */
    public static RadiusPacket[] parse(DataInputStream input, int packetCount) throws RadiusException {
        RadiusPacket rp[] = new RadiusPacket[packetCount];
        try {
            for (int i = 0; i < packetCount; i++) {
                rp[i] = parsePacket(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rp;
    }
}
