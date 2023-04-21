package net.sourceforge.parser.mkv.type;

import java.io.IOException;
import net.sourceforge.parser.util.ByteStream;
import net.sourceforge.parser.mkv.Utils;

public class Cues extends Master {

    private static final long serialVersionUID = 928253626895270829L;

    public Cues(long ebml, long size, long position) {
        super(ebml, size, position);
    }

    @Override
    public void readData(ByteStream stream) throws IOException {
        int next = 0;
        long stream_position = stream.position();
        while (stream.position() - size != stream_position) {
            next <<= 8;
            next |= stream.read();
            int t1 = (next & 0xff);
            int s = 0;
            if (t1 == Types.CUE_POINT) {
                long pos = stream.position() - 1;
                s = (int) Utils.getSize(stream);
                CuePoint cuePoint = new CuePoint(t1, s, pos);
                cuePoint.readData(stream);
                this.add(cuePoint);
            } else if (t1 == Types.VOID) {
                long pos = stream.position() - 1;
                s = (int) Utils.getSize(stream);
                this.add(new Void(t1, s, pos));
                stream.skip(s);
            } else if (t1 == Types.CRC32) {
                long pos = stream.position() - 1;
                s = (int) Utils.getSize(stream);
                this.add(new CRC32(t1, s, pos));
                stream.skip(s);
            }
        }
    }
}
