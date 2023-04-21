package net.sourceforge.parser.avi.type;

import java.io.IOException;
import net.sourceforge.parser.avi.Utils;
import net.sourceforge.parser.util.ByteStream;

/**
 * @author aza_sf@yahoo.com
 *
 * @version $Revision: 28 $
 */
public class AviStreamHeader extends Chunk {

    private static final long serialVersionUID = -1941345509543519722L;

    public String fccType;

    public String fccHandler;

    public int dwFlags;

    public short wPriority;

    public short wLanguage;

    public int dwInitialFrames;

    public int dwScale;

    public int dwRate;

    public int dwStart;

    public int dwLength;

    public int dwSuggestedBufferSize;

    public int dwQuality;

    public int dwSampleSize;

    public short x;

    public short y;

    public short width;

    public short height;

    /**
	 * @param dwFourCC
	 * @param dwSize
	 */
    public AviStreamHeader(int dwFourCC, long dwSize) {
        super(dwFourCC, dwSize);
    }

    @Override
    public void readData(ByteStream stream) throws IOException {
        fccType = Utils.intToString((int) Utils.bytesToLong(stream, 4));
        fccHandler = Utils.intToString((int) Utils.bytesToLong(stream, 4));
        dwFlags = (int) Utils.bytesToLongLE(stream, 4);
        wPriority = (short) Utils.bytesToLongLE(stream, 2);
        wLanguage = (short) Utils.bytesToLongLE(stream, 2);
        dwInitialFrames = (int) Utils.bytesToLongLE(stream, 4);
        dwScale = (int) Utils.bytesToLongLE(stream, 4);
        dwRate = (int) Utils.bytesToLongLE(stream, 4);
        dwStart = (int) Utils.bytesToLongLE(stream, 4);
        dwLength = (int) Utils.bytesToLongLE(stream, 4);
        dwSuggestedBufferSize = (int) Utils.bytesToLongLE(stream, 4);
        dwQuality = (int) Utils.bytesToLongLE(stream, 4);
        dwSampleSize = (int) Utils.bytesToLongLE(stream, 4);
        x = (short) Utils.bytesToLongLE(stream, 2);
        y = (short) Utils.bytesToLongLE(stream, 2);
        width = (short) Utils.bytesToLongLE(stream, 2);
        height = (short) Utils.bytesToLongLE(stream, 2);
    }
}
