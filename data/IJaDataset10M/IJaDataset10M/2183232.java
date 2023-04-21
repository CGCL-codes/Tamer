package jogamp.graph.font.typecast.ot.table;

import java.io.DataInput;
import java.io.IOException;

/**
 * @version $Id: CmapFormat.java,v 1.3 2004-12-21 16:56:35 davidsch Exp $
 * @author <a href="mailto:davidsch@dev.java.net">David Schweinsberg</a>
 */
public abstract class CmapFormat {

    public class Range {

        private int _startCode;

        private int _endCode;

        protected Range(int startCode, int endCode) {
            _startCode = startCode;
            _endCode = endCode;
        }

        public int getStartCode() {
            return _startCode;
        }

        public int getEndCode() {
            return _endCode;
        }
    }

    protected int _format;

    protected int _length;

    protected int _language;

    protected CmapFormat(DataInput di) throws IOException {
        _length = di.readUnsignedShort();
        _language = di.readUnsignedShort();
    }

    protected static CmapFormat create(int format, DataInput di) throws IOException {
        switch(format) {
            case 0:
                return new CmapFormat0(di);
            case 2:
                return new CmapFormat2(di);
            case 4:
                return new CmapFormat4(di);
            case 6:
                return new CmapFormat6(di);
            default:
                return new CmapFormatUnknown(format, di);
        }
    }

    public int getFormat() {
        return _format;
    }

    public int getLength() {
        return _length;
    }

    public int getLanguage() {
        return _language;
    }

    public abstract int getRangeCount();

    public abstract Range getRange(int index) throws ArrayIndexOutOfBoundsException;

    public abstract int mapCharCode(int charCode);

    public String toString() {
        return new StringBuffer().append("format: ").append(_format).append(", length: ").append(_length).append(", language: ").append(_language).toString();
    }
}
