package octlight.scene.loader.nwn;

import octlight.util.LittleEndianParser;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author $Author: creator $
 * @version $Revision: 1.3 $
 */
public class BinaryMDLFile {

    private int modelDataSize;

    private int rawDataSize;

    public BinaryMDLFile(InputStream in) throws IOException {
        LittleEndianParser parser = new LittleEndianParser(in);
        parser.check32(0);
        modelDataSize = parser.read32();
        rawDataSize = parser.read32();
    }
}
