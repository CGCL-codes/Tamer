package net.sourceforge.parser.avi.type;

import net.sourceforge.parser.util.ByteStream;

/**
 * @author aza_sf@yahoo.com
 *
 * @version $Revision: 28 $
 */
public class Junk extends Chunk {

    private static final long serialVersionUID = 6193846951417485217L;

    /**
	 * @param dwFourCC
	 * @param dwSize
	 */
    public Junk(int dwFourCC, long dwSize) {
        super(dwFourCC, dwSize);
    }

    @Override
    public void readData(ByteStream stream) {
    }
}
