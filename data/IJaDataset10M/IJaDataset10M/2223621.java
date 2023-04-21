package vavi.sound.adpcm.ccitt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import vavi.sound.adpcm.AdpcmInputStream;
import vavi.sound.adpcm.Codec;

/**
 * G723_16 InputStream.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030828 nsano initial version <br>
 */
public class G723_16InputStream extends AdpcmInputStream {

    /** */
    protected Codec getCodec() {
        return new G723_16();
    }

    /**
     * {@link vavi.io.BitInputStream} �� 2bit little endian �Œ�
     * <li>TODO BitInputStream �� endian
     * <li>TODO PCM encoding
     */
    public G723_16InputStream(InputStream in, ByteOrder byteOrder) {
        super(in, byteOrder, 2, ByteOrder.LITTLE_ENDIAN);
        ((G723_16) decoder).setEncoding(encoding);
    }

    /** ADPCM (4bit) ���Z���̒��� */
    public int available() throws IOException {
        return (in.available() * 4) + (rest ? 1 : 0);
    }
}
