package com.player.messages;

import java.io.*;
import com.player.*;

/**
Handles setting laser configuration packet communication with a Player server.

@author Jim Kramer
*/
public class PlayerMessageLaserConfigSet extends PlayerMessageRequest {

    public int payloadSize = 1 + 2 + 2 + 2 + 2 + 1;

    public double minAngle, maxAngle;

    public double angleRes;

    public int rangeRes;

    public boolean intensity;

    /** Constructor for setting the configuration data. */
    public PlayerMessageLaserConfigSet(double min, double max, double sr, int rr, boolean i) {
        super();
        psize = Player.PLAYER_MSGHDR_SIZE + payloadSize + 1;
        req_dev = header.device = Player.InterfaceCode.PLAYER.code();
        req_devi = header.device_index = 0;
        subtype = Player.PLAYER_LASER_SET_CONFIG;
        minAngle = min;
        maxAngle = max;
        angleRes = sr;
        rangeRes = rr;
        intensity = i;
    }

    /** Write the packet out. Note that this calls the
	 * {@link com.player.PlayerMessageRequest#fillHeader fillHeader} method
	 * to put the header information into a byte buffer, followed by setting
	 * the correct size, then the payload.
	 * @param dos The {@link java.io.DataOutputStream DataOutputStream}
	 * @throws IOException If there is a problem writing to <tt>dos</tt> */
    public void writeRequest(DataOutputStream dos) throws IOException {
        int off = Player.PLAYER_MSGHDR_SIZE;
        header.size = payloadSize;
        super.fillHeader();
        off = Player.putByte(subtype, buf, off);
        off = Player.putShort((int) (Math.toDegrees(minAngle * 100)), buf, off);
        off = Player.putShort((int) (Math.toDegrees(maxAngle * 100)), buf, off);
        off = Player.putShort((int) (Math.toDegrees(angleRes)), buf, off);
        off = Player.putShort(rangeRes, buf, off);
        off = Player.putBoolean(intensity, buf, off);
        dos.write(buf, 0, off);
        dos.flush();
    }

    /** Dummy method; no response expected.
	 * @param dis the {@link java.io.DataInputStream DataInputStream}
	 * @return <tt>true</tt> on success, <tt>false</tt> otherwise */
    public boolean readMessage(DataInputStream dis) {
        if (!super.readMessage(dis)) return false;
        if (header.type != Player.PLAYER_MSGTYPE_RESP_ACK || header.device != req_dev || header.device_index != req_devi) {
            return false;
        }
        return true;
    }
}
