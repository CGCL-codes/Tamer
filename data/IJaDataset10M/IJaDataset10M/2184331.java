package com.aelitis.azureus.core.peermanager.messaging.azureus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gudy.azureus2.core3.util.DirectByteBuffer;
import com.aelitis.azureus.core.peermanager.messaging.Message;
import com.aelitis.azureus.core.peermanager.messaging.MessageException;
import com.aelitis.azureus.core.peermanager.messaging.MessagingUtil;

public class AZHave implements AZMessage {

    private final byte version;

    private DirectByteBuffer buffer = null;

    private int[] piece_numbers;

    public AZHave(int[] _piece_numbers, byte _version) {
        piece_numbers = _piece_numbers;
        version = _version;
    }

    public String getID() {
        return (AZMessage.ID_AZ_HAVE);
    }

    public byte[] getIDBytes() {
        return (AZMessage.ID_AZ_HAVE_BYTES);
    }

    public String getFeatureID() {
        return (AZMessage.AZ_FEATURE_ID);
    }

    public int getFeatureSubID() {
        return (AZMessage.SUBID_ID_AZ_HAVE);
    }

    public int getType() {
        return (Message.TYPE_PROTOCOL_PAYLOAD);
    }

    public byte getVersion() {
        return version;
    }

    ;

    public String getDescription() {
        StringBuffer str = new StringBuffer(piece_numbers.length * 10);
        for (int i = 0; i < piece_numbers.length; i++) {
            if (i > 0) {
                str.append(",");
            }
            str.append(piece_numbers[i]);
        }
        return (getID() + " " + str);
    }

    public int[] getPieceNumbers() {
        return (piece_numbers);
    }

    public DirectByteBuffer[] getData() {
        if (buffer == null) {
            Map map = new HashMap();
            List l = new ArrayList(piece_numbers.length);
            for (int i = 0; i < piece_numbers.length; i++) {
                l.add(new Long(piece_numbers[i]));
            }
            map.put("pieces", l);
            buffer = MessagingUtil.convertPayloadToBencodedByteStream(map, DirectByteBuffer.AL_MSG);
        }
        return new DirectByteBuffer[] { buffer };
    }

    public Message deserialize(DirectByteBuffer data, byte version) throws MessageException {
        Map payload = MessagingUtil.convertBencodedByteStreamToPayload(data, 1, getID());
        List l = (List) payload.get("pieces");
        int[] pieces = new int[l.size()];
        for (int i = 0; i < pieces.length; i++) {
            pieces[i] = ((Long) l.get(i)).intValue();
        }
        AZHave message = new AZHave(pieces, version);
        return (message);
    }

    public void destroy() {
        if (buffer != null) {
            buffer.returnToPool();
        }
    }
}
