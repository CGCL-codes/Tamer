package psd.base;

import java.io.IOException;
import java.util.logging.Logger;
import psd.rawObjects.PsdBoolean;
import psd.rawObjects.PsdDescriptor;
import psd.rawObjects.PsdDouble;
import psd.rawObjects.PsdEnum;
import psd.rawObjects.PsdList;
import psd.rawObjects.PsdLong;
import psd.rawObjects.PsdText;
import psd.rawObjects.PsdTextData;
import psd.rawObjects.PsdUnitFloat;

/**
 * The Class PsdObject.
 *
 * @author Dmitry Belsky
 */
public class PsdObjectBase {

    /** The Constant logger. */
    protected static final Logger logger = Logger.getLogger("psd.objects");

    /**
	 * Instantiates a new psd object.
	 */
    public PsdObjectBase() {
    }

    /**
	 * Load psd object.
	 *
	 * @param stream the stream
	 * @return the psd object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public static PsdObjectBase loadPsdObject(PsdInputStream stream) throws IOException {
        String type = stream.readString(4);
        logger.finest("loadPsdObject.type: " + type);
        if (type.equals("Objc")) {
            return new PsdDescriptor(stream);
        } else if (type.equals("VlLs")) {
            return new PsdList(stream);
        } else if (type.equals("doub")) {
            return new PsdDouble(stream);
        } else if (type.equals("long")) {
            return new PsdLong(stream);
        } else if (type.equals("bool")) {
            return new PsdBoolean(stream);
        } else if (type.equals("UntF")) {
            return new PsdUnitFloat(stream);
        } else if (type.equals("enum")) {
            return new PsdEnum(stream);
        } else if (type.equals("TEXT")) {
            return new PsdText(stream);
        } else if (type.equals("tdta")) {
            return new PsdTextData(stream);
        } else {
            throw new IOException("UNKNOWN TYPE <" + type + ">");
        }
    }
}
