package net.sourceforge.parser.mkv.type;

import java.io.IOException;
import net.sourceforge.parser.util.ByteStream;
import net.sourceforge.parser.mkv.Utils;

/**
 * @author aza_sf@yahoo.com
 *
 * @version $Revision: $
 */
public class ChapterDisplay extends Master {

    private static final long serialVersionUID = -4143154125935494945L;

    public String ChapString;

    public String ChapLanguage = "eng";

    public String ChapCountry;

    public ChapterDisplay(long ebml, long size, long position) {
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
            if (t2 == Types.CHAP_LANGUAGE) {
                s = (int) Utils.getSize(stream);
                byte b[] = new byte[s];
                stream.read(b);
                ChapLanguage = new String(b);
            }
            if (t2 == Types.CHAP_COUNTRY) {
                s = (int) Utils.getSize(stream);
                byte b[] = new byte[s];
                stream.read(b);
                ChapCountry = new String(b);
            }
            if (t1 == Types.CHAP_STRING) {
                s = (int) Utils.getSize(stream);
                byte b[] = new byte[s];
                stream.read(b);
                ChapString = new String(b);
            }
        }
    }
}
