package psd.parser.object;

import java.io.IOException;
import psd.parser.PsdInputStream;

/**
 * The Class PsdEnum.
 *
 * @author Dmitry Belsky
 */
public class PsdEnum extends PsdObject {

    /** The type id. */
    private final String typeId;

    /** The value. */
    private final String value;

    /**
	 * Instantiates a new psd enum.
	 *
	 * @param stream the stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public PsdEnum(PsdInputStream stream) throws IOException {
        typeId = stream.readPsdString();
        value = stream.readPsdString();
        logger.finest("PsdEnum.typeId " + typeId + " PsdEnum.value: " + value);
    }

    /**
	 * Gets the type id.
	 *
	 * @return the type id
	 */
    public String getTypeId() {
        return typeId;
    }

    /**
	 * Gets the value.
	 *
	 * @return the value
	 */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "enum:<" + typeId + ":" + value + ">";
    }
}
