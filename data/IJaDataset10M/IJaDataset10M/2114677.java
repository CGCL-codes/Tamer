package huf.unibus.xmlbridge;

import huf.unibus.BusEvent;

/**
 * Deserialized bus events are instances of this class.
 *
 * <p>
 * This class purpos is to differentiate common events from those
 * which came from the bridge so it's possible to avoid sending them back to
 * where they came from.
 * </p>
 */
class BridgeBusEvent extends BusEvent {

    /**
	 * Create new UniBus event sent by XML bridge.
	 *
	 * @param origin originating bridge
	 * @param decoder XML decoder providing data from currently parsed node
	 */
    BridgeBusEvent(XmlBridge origin, XmlNodeDecoder decoder) {
        super(decoder.getName(), decoder.getNumArgs(), decoder);
        assert origin != null : "failed assert origin != null";
        this.origin = origin;
    }

    /** Originating bridge. */
    private final XmlBridge origin;

    /**
	 * Get bridge which sent event to the bus.
	 *
	 * @return bridge which sent event to the bus
	 */
    XmlBridge getOrigin() {
        return origin;
    }
}
