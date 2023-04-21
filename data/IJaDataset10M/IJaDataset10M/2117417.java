package com.simpledata.filetools.encoders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.simpledata.filetools.SecuSelf;
import com.simpledata.filetools.SimpleException;

/**
 * A Stream that consume one Integer of the passed Stream.
 * <BR> used for compliance with the old RSA Save method
 */
public class SelfDC_IntConsumer implements SelfDConverter {

    int i;

    /** @pram i the intvalue to add (normaly not used) **/
    public SelfDC_IntConsumer(int i) {
        this.i = i;
    }

    ;

    /**
     * @see SelfD#getID()
     */
    public byte[] getID() throws IOException {
        return SelfDC_Dummy.getSimpleID(SecuSelf.C_IntConsumer);
    }

    /**
     * @see SelfD#setDestination(java.io.OutputStream)
     */
    public OutputStream setDestination(OutputStream destination) throws SimpleException, IOException {
        (new DataOutputStream(destination)).writeInt(i);
        return destination;
    }

    public static InputStream getDecoder(InputStream source) throws IOException {
        (new DataInputStream(source)).readInt();
        return source;
    }
}
