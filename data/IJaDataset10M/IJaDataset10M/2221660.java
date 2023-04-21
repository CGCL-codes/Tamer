package net.sf.jml.message;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import net.sf.cindy.util.ByteBufferUtils;
import net.sf.jml.MsnObject;
import net.sf.jml.message.p2p.DisplayPictureDuelManager;
import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;

public class MsnEmoticonMessage extends MsnMimeMessage {

    public static final String EMOTICON_SEPARATOR = "\t";

    private Map<String, MsnObject> emoticons;

    public MsnEmoticonMessage() {
        setContentType(MessageConstants.CT_EMOTICON);
        emoticons = new HashMap<String, MsnObject>(5);
    }

    public void putEmoticon(String shortcut, MsnObject emoticon, DisplayPictureDuelManager duelManager) {
        emoticons.put(shortcut, emoticon);
        if (emoticon != null) {
            duelManager.putPicture(emoticon.toString(), emoticon);
        }
    }

    @Override
    protected void parseMessage(byte[] message) {
        ByteBuffer split = Charset.encode(JmlConstants.LINE_SEPARATOR + JmlConstants.LINE_SEPARATOR);
        int pos = ByteBufferUtils.indexOf(ByteBuffer.wrap(message), split);
        String header = pos == -1 ? Charset.decode(message) : Charset.decode(message, 0, pos);
        headers.parseString(header);
        pos += split.remaining();
        ByteBuffer body = ByteBuffer.allocate(message.length - pos);
        body.put(message, pos, message.length - pos);
        body.flip();
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        for (Map.Entry<String, MsnObject> entry : emoticons.entrySet()) {
            ret.append(entry.getKey());
            ret.append(EMOTICON_SEPARATOR);
            ret.append(entry.getValue());
            ret.append(EMOTICON_SEPARATOR);
        }
        return super.toString() + ret.toString();
    }
}
