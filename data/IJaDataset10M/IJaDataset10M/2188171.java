package com.aelitis.azureus.core.peermanager.messaging.bittorrent;

import org.gudy.azureus2.core3.util.*;
import com.aelitis.azureus.core.peermanager.messaging.Message;
import com.aelitis.azureus.core.peermanager.messaging.MessageException;

/**
 * BitTorrent cancel message.
 */
public class BTCancel implements BTMessage {

    private DirectByteBuffer buffer = null;

    private byte version;

    private String description = null;

    private final int piece_number;

    private final int piece_offset;

    private final int length;

    public BTCancel(int piece_number, int piece_offset, int length, byte version) {
        this.piece_number = piece_number;
        this.piece_offset = piece_offset;
        this.length = length;
        this.version = version;
    }

    public int getPieceNumber() {
        return piece_number;
    }

    public int getPieceOffset() {
        return piece_offset;
    }

    public int getLength() {
        return length;
    }

    public String getID() {
        return BTMessage.ID_BT_CANCEL;
    }

    public byte[] getIDBytes() {
        return BTMessage.ID_BT_CANCEL_BYTES;
    }

    public String getFeatureID() {
        return BTMessage.BT_FEATURE_ID;
    }

    public int getFeatureSubID() {
        return BTMessage.SUBID_BT_CANCEL;
    }

    public int getType() {
        return Message.TYPE_PROTOCOL_PAYLOAD;
    }

    public byte getVersion() {
        return version;
    }

    ;

    public String getDescription() {
        if (description == null) {
            description = BTMessage.ID_BT_CANCEL + " piece #" + piece_number + ":" + piece_offset + "->" + (piece_offset + length - 1);
        }
        return description;
    }

    public DirectByteBuffer[] getData() {
        if (buffer == null) {
            buffer = DirectByteBufferPool.getBuffer(DirectByteBuffer.AL_MSG_BT_CANCEL, 12);
            buffer.putInt(DirectByteBuffer.SS_MSG, piece_number);
            buffer.putInt(DirectByteBuffer.SS_MSG, piece_offset);
            buffer.putInt(DirectByteBuffer.SS_MSG, length);
            buffer.flip(DirectByteBuffer.SS_MSG);
        }
        return new DirectByteBuffer[] { buffer };
    }

    public Message deserialize(DirectByteBuffer data, byte version) throws MessageException {
        if (data == null) {
            throw new MessageException("[" + getID() + "] decode error: data == null");
        }
        if (data.remaining(DirectByteBuffer.SS_MSG) != 12) {
            throw new MessageException("[" + getID() + "] decode error: payload.remaining[" + data.remaining(DirectByteBuffer.SS_MSG) + "] != 12");
        }
        int num = data.getInt(DirectByteBuffer.SS_MSG);
        if (num < 0) {
            throw new MessageException("[" + getID() + "] decode error: num < 0");
        }
        int offset = data.getInt(DirectByteBuffer.SS_MSG);
        if (offset < 0) {
            throw new MessageException("[" + getID() + "] decode error: offset < 0");
        }
        int length = data.getInt(DirectByteBuffer.SS_MSG);
        if (length < 0) {
            throw new MessageException("[" + getID() + "] decode error: lngth < 0");
        }
        data.returnToPool();
        return new BTCancel(num, offset, length, version);
    }

    public void destroy() {
        if (buffer != null) buffer.returnToPool();
    }
}
