package net.sourceforge.parser.avi.type;

import java.io.IOException;
import net.sourceforge.parser.util.ByteStream;

/**
 * @author aza_sf@yahoo.com
 *
 * @version $Revision: 28 $
 */
public class StreamName extends Chunk {

    private static final long serialVersionUID = -3278287580468566431L;

    public String name;

    /**
	 * @param dwFourCC
	 * @param dwSize
	 */
    public StreamName(int dwFourCC, long dwSize) {
        super(dwFourCC, dwSize);
    }

    @Override
    public void readData(ByteStream stream) throws IOException {
        byte b[] = new byte[(int) dwSize];
        stream.read(b);
        name = new String(b);
    }
}
