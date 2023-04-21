package net.walkingtools.server.gpx;

import net.walkingtools.server.gpsTypes.GpxExtensionType;
import org.w3c.dom.Node;

/**
 * This interface specifies a parseExtension method to parse a org.w3c.dom.Node
 * object representing a GPX estension. Implementations of this interface should
 * be able to parse the node into some implementation of the abstract GpxExtensionType
 * class. The WalkingToolsGpx Api has a class, WalkingToolsExtensionParser,
 * that implements this interface and produces a GpxExtensionType as the object
 * representing WalkingToolsGpx extensions.
 * @see net.walkingtools.server.persistent.gpsTypes.GpxExtensionType
 * @see net.walkingtools.server.persistent.gpsTypes.hiperGps.HiperGpxExtensionType
 * @author Brett Stalbaum
 * @version 0.1.0
 * @since 0.0.6
 */
public interface GpxExtensionParser {

    /**
     * Parses a gpx extension node, returning a GpxExtensionType.
     * @param extension the Node to parse
     * @return the parsed GpxExtensionType
     * @throws GpxExtensionTypeException thrown if the GpxExtensionParser can not parse the extension
     */
    public GpxExtensionType parseExtension(Node extension) throws GpxExtensionTypeException;
}
