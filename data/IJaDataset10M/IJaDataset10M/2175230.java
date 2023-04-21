package drcl.comp.lib.bytestream;

import drcl.comp.Contract;
import drcl.comp.Message;
import drcl.comp.Port;

/**
The byte stream sending contract.
In this contract, the initiator sends byte stream data to the reactor.
The reactor has a receiving buffer to hold the data and the data is consumed
by the reactor at the pace depending on the implementation of the reactor.
<dl>
<dt><code>ByteStreamSending</code>
<dd>The initiator sends a message which consists of:
  <ol>
  <li> an integer of 0 (the "send" command),
  <li> the byte array (<code>byte[]</code>),
  <li> the offset of the first byte in the byte array to start sending, and
  <li> the length of sending (<code>int</code>).
  </ol>
  The reactor moves the bytes of the specified length (&gt 0) from the
  byte array to its buffer if the buffer in the reactor allows, and 
  may return an
  <code>Integer</code> indicating the available receiving buffer in bytes
  in the reactor.  A negative value indicates the amount of buffer overflow
  if the data is too large to fit in the receiving buffer.
<dt><code>BufferReport</code>
<dd>The reactor sends the available receiving buffer size in bytes in a
  message whenever data is consumed and removed from the buffer.
    The message consists of:
  <ol>
  <li> an integer of 1 (the "report" command) and
  <li> the available buffer size in bytes (int).
  </ol>
<dt><code>BufferQuery</code>
<dd>The initiator sends a message which consists of an integer of 2
    (the "query" command).
  In response, the reactor returns the available buffer in bytes
  in <code>Integer</code>.
<dt><code>Stop</code>
<dd>The reactor sends out the "stop" message to stop the initiator from
    trying to send out more bytes.  The message consists of an integer of 3.
</dl>

@author Hung-ying Tyan
@version 1.0, 06/2001
 */
public class ByteStreamContract extends Contract implements ByteStreamConstants {

    public ByteStreamContract() {
        super();
    }

    public ByteStreamContract(int role_) {
        super(role_);
    }

    public String getName() {
        return "Byte Stream Contract";
    }

    public Object getContractContent() {
        return null;
    }

    public static int query(Port port_) {
        return ((Integer) port_.sendReceive(new Message(QUERY))).intValue();
    }

    public static class Message extends drcl.comp.Message {

        int type;

        byte[] buffer;

        int length, offset;

        public Message() {
        }

        /** Query */
        public Message(int type_) {
            type = type_;
        }

        /** Report */
        public Message(int type_, int size_) {
            type = type_;
            length = size_;
        }

        /** Send */
        public Message(int type_, byte[] buf_, int len_) {
            type = type_;
            buffer = buf_;
            offset = 0;
            length = len_;
        }

        /** Send */
        public Message(int type_, byte[] buf_, int offset_, int len_) {
            type = type_;
            buffer = buf_;
            offset = offset_;
            length = len_;
        }

        public byte[] getByteArray() {
            return buffer;
        }

        public int getOffset() {
            return offset;
        }

        public int getLength() {
            return length;
        }

        public boolean isSend() {
            return type == SEND;
        }

        public boolean isQuery() {
            return type == QUERY;
        }

        public boolean isReport() {
            return type == REPORT;
        }

        public boolean isStop() {
            return type == STOP;
        }

        public int getType() {
            return type;
        }

        public Object clone() {
            return new Message(type, buffer, offset, length);
        }

        public Contract getContract() {
            return new ByteStreamContract();
        }

        public String toString(String separator_) {
            switch(type) {
                case SEND:
                    return "BYTE_STREAM_SEND" + separator_ + "offset:" + offset + separator_ + "length:" + length + separator_ + "data:" + drcl.util.StringUtil.toString(buffer);
                case QUERY:
                    return "BYTE_STREAM_QUERY";
                case REPORT:
                    return "BYTE_STREAM_REPORT:" + length;
                case STOP:
                    return "BYTE_STREAM_STOP";
                default:
                    return "unknown,offset=" + offset + ",length=" + length + ",data=" + drcl.util.StringUtil.toString(buffer);
            }
        }
    }
}
