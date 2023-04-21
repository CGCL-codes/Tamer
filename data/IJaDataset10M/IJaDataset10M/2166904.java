package se.mushroomwars.network.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class PackageHeader {

    private static final int PACKAGELENGTH_LEN = 2;

    private static final int PLAYERNAME_LEN = 0;

    private static final int PACKAGEID_LEN = 1;

    public static final int HEADER_LENGTH = PACKAGELENGTH_LEN + PLAYERNAME_LEN + PACKAGEID_LEN;

    String playerName;

    private int packageLength;

    private int packageID;

    public PackageHeader() {
        this(0, 0);
    }

    public PackageHeader(int packageLength, int packageID) {
        this.packageLength = packageLength;
        this.packageID = packageID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPackageLength() {
        return packageLength;
    }

    public int getPackageID() {
        return packageID;
    }

    public void clear() {
        packageLength = 0;
        packageID = 0;
        playerName = "";
    }

    private short decodePackageLength(byte[] arr) {
        short res = 0;
        res |= (int) arr[0] << 7;
        res |= (int) arr[1];
        return res;
    }

    private byte[] encodePackageLength(short len) {
        byte[] res = new byte[2];
        res[0] = (byte) (len >> 7);
        res[1] = (byte) (len & 127);
        return res;
    }

    private byte encodePackageID(short ID) {
        return (byte) ID;
    }

    private byte decodePackageID(byte[] ID) {
        return ID[HEADER_LENGTH - 1];
    }

    private String decodePlayerName(byte[] arr) {
        String res = null;
        try {
            res = new String(arr, PACKAGELENGTH_LEN, PLAYERNAME_LEN, "us-ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Ivar";
        }
        return res.trim();
    }

    private byte[] encodePlayerName(String PlayerName) {
        byte[] tmp, res = new byte[PLAYERNAME_LEN];
        try {
            tmp = PlayerName.getBytes("us-ascii");
            for (int i = 0; i < tmp.length; i++) res[i] = tmp[i];
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
	 * decoding all the information given by buf and saving the result in the
	 * instancevariable buf must contaion 13 elements, controll is done in
	 * Server
	 */
    public void decodeHeader(byte[] buf) {
        packageLength = decodePackageLength(buf);
        packageID = decodePackageID(buf);
    }

    /**
	 * encoding all the information in the instancevariable
	 * 
	 * @return ByteBuffer
	 */
    public ByteBuffer encodeHeader() {
        ByteBuffer res = ByteBuffer.allocate(HEADER_LENGTH);
        res.put(encodePackageLength((short) packageLength));
        res.put(encodePackageID((short) packageID));
        res.position(0);
        return res;
    }

    public String toString() {
        return "PackageLength: " + packageLength + " PackageID: " + packageID + " PlayerName: " + playerName;
    }
}
