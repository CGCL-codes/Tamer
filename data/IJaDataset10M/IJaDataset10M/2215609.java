package uk.ac.ncl.cs.instantsoap.commandlineprocessor.xmlParser;

import org.w3c.dom.Element;
import uk.ac.ncl.cs.instantsoap.commandlineprocessor.commandBuildingModule.CommandFormat;

/**
 * Writer that builds DOM elements. Intended for static import.
 *
 * @author Matthew Pocock
 */
public final class DomFormatWriter {

    private DomFormatWriter() {
    }

    /**
     * Add elements to parent capturing all the information from the commandFormat
     *
     * @param commandFormat the CommandFormat to write
     * @return an Element encoding commandFormat
     */
    public static Element write(CommandFormat commandFormat) {
    }
}
