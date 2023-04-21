package com.sun.corba.se.impl.encoding;

import org.omg.CORBA.CompletionStatus;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import com.sun.corba.se.impl.encoding.CDROutputStream;
import com.sun.corba.se.impl.encoding.BufferManagerWrite;
import com.sun.corba.se.impl.encoding.BufferManagerFactory;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.impl.orbutil.ORBConstants;

/**
 * Encapsulations are supposed to explicitly define their
 * code sets and GIOP version.  The original resolution to issue 2784 
 * said that the defaults were UTF-8 and UTF-16, but that was not
 * agreed upon.
 *
 * These streams currently use CDR 1.2 with ISO8859-1 for char/string and
 * UTF16 for wchar/wstring.  If no byte order marker is available,
 * the endianness of the encapsulation is used.
 *
 * When more encapsulations arise that have their own special code
 * sets defined, we can make all constructors take such parameters.
 */
public class EncapsOutputStream extends CDROutputStream {

    static final boolean usePooledByteBuffers = false;

    public EncapsOutputStream(ORB orb) {
        this(orb, GIOPVersion.V1_2);
    }

    public EncapsOutputStream(ORB orb, GIOPVersion version) {
        this(orb, version, false);
    }

    public EncapsOutputStream(ORB orb, boolean isLittleEndian) {
        this(orb, GIOPVersion.V1_2, isLittleEndian);
    }

    public EncapsOutputStream(ORB orb, GIOPVersion version, boolean isLittleEndian) {
        super(orb, version, Message.CDR_ENC_VERSION, isLittleEndian, BufferManagerFactory.newBufferManagerWrite(BufferManagerFactory.GROW, Message.CDR_ENC_VERSION, orb), ORBConstants.STREAM_FORMAT_VERSION_1, usePooledByteBuffers);
    }

    public org.omg.CORBA.portable.InputStream create_input_stream() {
        freeInternalCaches();
        return new EncapsInputStream(orb(), getByteBuffer(), getSize(), isLittleEndian(), getGIOPVersion());
    }

    protected CodeSetConversion.CTBConverter createCharCTBConverter() {
        return CodeSetConversion.impl().getCTBConverter(OSFCodeSetRegistry.ISO_8859_1);
    }

    protected CodeSetConversion.CTBConverter createWCharCTBConverter() {
        if (getGIOPVersion().equals(GIOPVersion.V1_0)) throw wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
        if (getGIOPVersion().equals(GIOPVersion.V1_1)) return CodeSetConversion.impl().getCTBConverter(OSFCodeSetRegistry.UTF_16, isLittleEndian(), false);
        boolean useBOM = ((ORB) orb()).getORBData().useByteOrderMarkersInEncapsulations();
        return CodeSetConversion.impl().getCTBConverter(OSFCodeSetRegistry.UTF_16, false, useBOM);
    }
}
