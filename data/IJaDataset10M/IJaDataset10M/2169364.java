package net.sourceforge.parser.mp4.box;

import java.io.IOException;
import net.sourceforge.parser.util.ByteStream;

/**
 * @author aza_sf@yahoo.com
 *
 * @version $Revision: 25 $
 */
public class DataEntryUrnBox extends DataEntryBox {

    public String name;

    public String location;

    private static final long serialVersionUID = -1793936619660781805L;

    /**
	 * @param boxtype
	 * @param size
	 * @param stream
	 * @throws IOException 
	 */
    public DataEntryUrnBox(int boxtype, long size, ByteStream stream) throws IOException {
        super(boxtype, size, stream);
    }

    @Override
    protected void readData(ByteStream stream) throws IOException {
        int size = (int) getSize() - (int) stream.getCounter();
        if ((flags & 1) == 1) {
        } else if (size > 0) {
            byte bytes[] = new byte[size];
            stream.read(bytes);
            int i = 0;
            StringBuffer buff = new StringBuffer();
            while (i < bytes.length && bytes[i] != 0) {
                buff.append((char) bytes[i++]);
            }
            name = buff.toString();
            buff = new StringBuffer();
            while (++i < bytes.length && bytes[i] != 0) {
                buff.append((char) bytes[i]);
            }
            location = buff.toString();
        }
    }
}
