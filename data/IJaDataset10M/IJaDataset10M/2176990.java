package jelb.netio;

import jelb.messaging.ISendableMessage;

public class Message implements ISendableMessage {

    private boolean consumed;

    private byte[] body;

    private Uint8 kind;

    private Uint16 length;

    public int pointer;

    private byte[] buf;

    public Message(byte b, Uint8 i) {
        this(b);
        this.body = new byte[1];
        this.body[0] = i.toByte();
        this.length = new Uint16(this.length.toInt() + 1);
    }

    public Message(byte b, Uint16 i) {
        this(b);
        this.body = new byte[2];
        i.insertIn(this.body, 0);
        this.length = new Uint16(this.length.toInt() + 2);
    }

    public Message(byte b, Uint32 i) {
        this(b);
        this.body = new byte[4];
        i.insertIn(this.body, 0);
        this.length = new Uint16(this.length.toInt() + 4);
    }

    public Message(byte b, String s) {
        this(b);
        this.add(s);
    }

    public Message(byte b, Uint32 i1, Uint32 i2) {
        this(b);
        byte[] bm = new byte[8];
        i1.insertIn(bm, 0);
        i2.insertIn(bm, 4);
        this.add(bm);
    }

    public Message(byte b, Uint8 b1, Uint8 b2, Uint32 b3) {
        this(b);
        byte[] bm = new byte[6];
        b1.insertIn(bm, 0);
        b2.insertIn(bm, 1);
        b3.insertIn(bm, 2);
        this.add(bm);
    }

    public Message(byte b, Uint8 b1, Uint32 b2) {
        this(b);
        byte[] bm = new byte[5];
        b1.insertIn(bm, 0);
        b2.insertIn(bm, 1);
        this.add(bm);
    }

    public Message(byte b) {
        consumed = false;
        this.kind = new Uint8(b);
        this.length = new Uint16(1);
        this.pointer = 0;
    }

    public Message(byte[] buf) {
        this.buf = buf;
        this.consumed = false;
        this.pointer = 0;
        this.kind = new Uint8(buf[0]);
        this.length = new Uint16(buf[1], buf[2]);
        if (this.buf.length < this.length.toInt() + 1) this.body = new byte[this.buf.length - 3]; else this.body = new byte[this.length.toInt() - 1];
        for (int i = 0; i < this.body.length; i++) {
            this.body[i] = this.buf[i + 3];
        }
    }

    public boolean isCompleted() {
        int ptr = 0;
        int msg = 0;
        while (ptr < this.buf.length) {
            if (this.buf.length > ptr + 2) {
                ptr += (new Uint16(this.buf[ptr + 1], this.buf[ptr + 2]).toInt() + 2);
            } else {
                break;
            }
            msg++;
        }
        return ptr == this.buf.length;
    }

    public int bufLng() {
        return this.buf.length;
    }

    /**
	 * Returns true if internal message pointer
	 * points to end of message
	 * @return true if message is whole parsed
	 */
    public boolean parsed() {
        if (this.pointer >= this.buf.length) return true;
        return false;
    }

    /**
	 * Moves to next sub message
	 *
	 */
    public void moveNext() {
        this.pointer += this.length.toInt() + 2;
        if (this.pointer >= this.buf.length) {
            return;
        }
        this.kind = new Uint8(buf[this.pointer]);
        this.length = new Uint16(buf[this.pointer + 1], buf[this.pointer + 2]);
        this.body = new byte[this.length.toInt() - 1];
        for (int i = 0; i < this.body.length; i++) {
            this.body[i] = this.buf[this.pointer + 3 + i];
        }
    }

    /**
	 * Returns byte of ordered offset
	 * @param offset
	 * @return 
	 */
    public byte getByte(int offset) {
        return this.body[offset];
    }

    /**
	 * Returns byte of ordered offset
	 * @param offset
	 * @return
	 */
    public Uint8 getUint8(int offset) {
        return new Uint8(this.getByte(offset));
    }

    /**
	 * Returns two bytes of ordered offset
	 * @param offset
	 * @return
	 */
    public Uint16 getUint16(int offset) {
        return new Uint16(this.getByte(offset), this.getByte(offset + 1));
    }

    /**
	 * Returns four bytes of ordered offest
	 * @param offset
	 * @return
	 */
    public Uint32 getUint32(int offset) {
        return new Uint32(this.getByte(offset + 0), this.getByte(offset + 1), this.getByte(offset + 2), this.getByte(offset + 3));
    }

    /**
	 * Returns protocol message length
	 * @return
	 */
    public Uint16 getLength() {
        return this.length;
    }

    public boolean isConsumed() {
        return this.consumed;
    }

    public void setConsumed(boolean state) {
        this.consumed = state;
    }

    public byte[] ToByteArray() {
        int bodyLength = 0;
        if (this.body != null) bodyLength = this.body.length;
        byte[] valid = new byte[bodyLength + 3];
        valid[0] = this.kind.toByte();
        length.insertIn(valid, 1);
        if (this.body != null) {
            for (int i = 0; i < this.body.length; i++) valid[i + 3] = this.body[i];
        }
        return valid;
    }

    public byte getCode() {
        return this.kind.toByte();
    }

    /**
	 * Returns message content as String from ordered
	 * start offset to orederd end offset
	 * @param offset
	 * @param offsetEnd
	 * @return
	 */
    public String getContent(int offset, int offsetEnd) {
        if (offsetEnd > this.body.length) offsetEnd = this.body.length;
        if (this.body != null) {
            byte tmp[] = new byte[offsetEnd - offset];
            for (int i = offset; i < offsetEnd; i++) tmp[i - offset] = this.body[i];
            return new String(tmp);
        }
        return "";
    }

    /**
	 * Returns message content as String of orrdered offset
	 * @param offset
	 * @return
	 */
    public String getContent(int offset) {
        return this.getContent(offset, this.body.length);
    }

    public void extend(byte[] addon) {
        if (this.body.length < this.length.toInt() - 1) {
            byte[] bodyAddon = new byte[(this.length.toInt() - 1) - this.body.length];
            for (int i = 0; i < bodyAddon.length; i++) {
                bodyAddon[i] = addon[i];
            }
            this.add(bodyAddon, false);
        }
        byte[] newBuf = new byte[this.buf.length + addon.length];
        for (int i = 0; i < newBuf.length; i++) {
            if (i < this.buf.length) newBuf[i] = this.buf[i]; else newBuf[i] = addon[i - this.buf.length];
        }
        this.buf = newBuf;
    }

    public void add(Uint8 value) {
        int bodyLength = 0;
        if (this.body != null) bodyLength = this.body.length;
        byte[] tmp = new byte[bodyLength + 1];
        for (int i = 0; i < bodyLength; i++) {
            tmp[i] = this.body[i];
        }
        value.insertIn(tmp, bodyLength);
        this.body = tmp;
        this.length = new Uint16(this.body.length + 1);
        this.pointer = 0;
    }

    public void add(Uint16 value) {
        int bodyLength = 0;
        if (this.body != null) bodyLength = this.body.length;
        byte[] tmp = new byte[bodyLength + 2];
        for (int i = 0; i < bodyLength; i++) {
            tmp[i] = this.body[i];
        }
        value.insertIn(tmp, bodyLength);
        this.body = tmp;
        this.length = new Uint16(this.body.length + 1);
        this.pointer = 0;
    }

    public void add(Uint32 value) {
        int bodyLength = 0;
        if (this.body != null) bodyLength = this.body.length;
        byte[] tmp = new byte[bodyLength + 4];
        for (int i = 0; i < bodyLength; i++) {
            tmp[i] = this.body[i];
        }
        value.insertIn(tmp, bodyLength);
        this.body = tmp;
        this.length = new Uint16(this.body.length + 1);
        this.pointer = 0;
    }

    public void add(byte[] addon) {
        this.add(addon, true);
    }

    /**
	 * Add bytes to message body
	 * @param tablica dodawanych bajtow
	 */
    public void add(byte[] addon, boolean changeLength) {
        int bodyLength = 0;
        if (this.body != null) bodyLength = this.body.length;
        byte[] tmp = new byte[bodyLength + addon.length];
        for (int i = 0; i < bodyLength; i++) {
            tmp[i] = this.body[i];
        }
        for (int i = bodyLength; i < bodyLength + addon.length; i++) {
            tmp[i] = addon[i - bodyLength];
        }
        this.body = tmp;
        if (changeLength) this.length = new Uint16(this.body.length + 1);
        this.pointer = 0;
    }

    /**
	 * Add String to message body
	 * @param str
	 */
    public void add(String str) {
        byte[] addon = str.getBytes();
        this.add(addon);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        byte ba[] = this.ToByteArray();
        sb.append("[");
        sb.append(new Uint8(ba[0]));
        sb.append("]");
        sb.append("[");
        sb.append(new Uint16(ba[2], ba[1]));
        sb.append("]");
        for (int i = 3; i < ba.length; i++) {
            sb.append(new Uint8(ba[i]));
        }
        return sb.toString();
    }

    public boolean equals(Message msg) {
        if (msg.body.length != msg.body.length) return false;
        if (msg.kind.toInt() != this.kind.toInt()) return false;
        for (int i = 0; i < this.body.length; i++) {
            if (msg.body[i] != this.body[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Message getRaw() {
        return this;
    }
}
