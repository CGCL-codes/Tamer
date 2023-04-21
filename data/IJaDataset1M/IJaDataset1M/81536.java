package net.sourceforge.parser.mkv.type;

import java.io.IOException;
import net.sourceforge.parser.util.ByteStream;
import net.sourceforge.parser.mkv.Utils;

public class CueTrackPositions extends Master {

    private static final long serialVersionUID = 4912344347688170792L;

    public long CueTrack = 0;

    public long CueClusterPosition;

    public long CueBlockNumber = 1;

    public long CueCodecState = 0;

    public CueTrackPositions(long ebml, long size, long position) {
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
            int t2 = (next & 0xffff);
            int s = 0;
            if (t2 == Types.CUE_BLOCK_NUMBER) {
                s = (int) Utils.getSize(stream);
                CueBlockNumber = Utils.bytesToLong(stream, s);
            } else if (t1 == Types.CUE_TRACK) {
                s = (int) Utils.getSize(stream);
                CueTrack = Utils.bytesToLong(stream, s);
            } else if (t1 == Types.CUE_CLUSTER_POSIITON) {
                s = (int) Utils.getSize(stream);
                CueClusterPosition = Utils.bytesToLong(stream, s);
            } else if (t1 == Types.CUE_CODEC_STATE) {
                s = (int) Utils.getSize(stream);
                CueCodecState = Utils.bytesToLong(stream, s);
            } else if (t1 == Types.CUE_REFERENCE) {
                long pos = stream.position() - 1;
                s = (int) Utils.getSize(stream);
                CueReference CueReference = new CueReference(t1, s, pos);
                CueReference.readData(stream);
                this.add(CueReference);
            }
        }
    }
}
