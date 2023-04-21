package net.sourceforge.myvd.protocol.ldap.mina.asn1.der;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ASN1OutputStream extends FilterOutputStream {

    public ASN1OutputStream(OutputStream os) {
        super(os);
    }

    public ASN1OutputStream(ByteBuffer out) {
        super(newOutputStream(out));
    }

    public static OutputStream newOutputStream(final ByteBuffer buf) {
        return new OutputStream() {

            public synchronized void write(int integer) throws IOException {
                buf.put((byte) integer);
            }

            public synchronized void write(byte[] bytes, int off, int len) throws IOException {
                buf.put(bytes, off, len);
            }
        };
    }

    private void writeLength(int length) throws IOException {
        if (length > 127) {
            int size = 1;
            int val = length;
            while ((val >>>= 8) != 0) {
                size++;
            }
            write((byte) (size | 0x80));
            for (int i = (size - 1) * 8; i >= 0; i -= 8) {
                write((byte) (length >> i));
            }
        } else {
            write((byte) length);
        }
    }

    void writeEncoded(int tag, byte[] bytes) throws IOException {
        write(tag);
        writeLength(bytes.length);
        write(bytes);
    }

    public void writeObject(Object obj) throws IOException {
        if (obj == null) {
            writeNull();
        } else if (obj instanceof DEREncodable) {
            ((DEREncodable) obj).encode(this);
        } else {
            throw new IOException("Object not DEREncodable.");
        }
    }

    protected void writeNull() throws IOException {
        write(DERObject.NULL);
        write(DERObject.TERMINATOR);
    }
}
