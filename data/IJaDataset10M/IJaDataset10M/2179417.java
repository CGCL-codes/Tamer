package org.biff.crypto.openpgp;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.IllegalStateException;
import java.util.Date;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.Connector;
import net.rim.device.api.io.FileInfo;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.util.Strings;
import org.bouncycastle.bcpg.PacketTags;

/**
 * 
 */
public class PGPLiteralDataDenerator implements StreamGenerator {

    public final char Binary = PGPLiteralData.Binary;

    public final char Text = PGPLiteralData.Text;

    public final String Console = PGPLiteralData.Console;

    private BCPGOutputStream pkOut;

    private boolean oldFormat;

    public PGPLiteralDataDenerator() {
        ;
    }

    public PGPLiteralDataDenerator(boolean oldFormat) {
        this.oldFormat = oldFormat;
    }

    private void writeHeader(BCPGOutputStream outStr, char format, String name, long modificationTime) throws IOException {
        byte[] asciiName = Strings.toByteArray(name);
        outStr.write(new byte[] { (byte) format, (byte) asciiName.length });
        outStr.write(asciiName);
        long modDate = modificationTime / (long) 1000;
        outStr.write(new byte[] { (byte) (modDate >> 24), (byte) (modDate >> 16), (byte) (modDate >> 8), (byte) modDate });
    }

    public OutputStream open(OutputStream out, char format, String name, long length, Date modificationTime) throws IOException, IllegalStateException {
        if (pkOut != null) throw new IllegalStateException("gnerator alread in open state");
        pkOut = new BCPGOutputStream(out, PacketTags.LITERAL_DATA, length + 2 + name.length() + 4, this.oldFormat);
        writeHeader(pkOut, format, name, modificationTime.getTime());
        return new WrappedGeneratorStream(pkOut, this);
    }

    public OutputStream open(OutputStream out, char format, String name, Date modificationTime, byte[] buffer) throws IOException, IllegalStateException {
        if (pkOut != null) throw new IllegalStateException("generator already in open state");
        pkOut = new BCPGOutputStream(out, PacketTags.LITERAL_DATA, buffer);
        writeHeader(pkOut, format, name, modificationTime.getTime());
        return new WrappedGeneratorStream(pkOut, this);
    }

    public OutputStream open(OutputStream out, char format, FileInfo file) throws IOException, IllegalStateException, PGPException {
        if (pkOut != null) throw new IllegalStateException("generator already in open state");
        try {
            FileConnection fconn = (FileConnection) Connector.open(file.getFileName());
            if (fconn.exists()) {
                pkOut = new BCPGOutputStream(out, PacketTags.LITERAL_DATA, file.getFileSize() + 2 + file.getFileName().length() + 4, oldFormat);
                writeHeader(pkOut, format, file.getFileName(), fconn.lastModified());
                fconn.close();
                return new WrappedGeneratorStream(pkOut, this);
            } else {
                fconn.close();
                throw new IOException("file not found: " + file.getFileName());
            }
        } catch (IOException e) {
            throw new PGPException("error processing file: " + file.getFileName(), e);
        }
    }

    public void close() throws IOException {
        if (pkOut != null) {
            pkOut.finish();
            pkOut.flush();
            pkOut = null;
        }
    }
}
