package com.limegroup.gnutella.messages;

import java.io.IOException;
import java.net.SocketAddress;
import org.limewire.inspection.InspectablePrimitive;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.limegroup.gnutella.messages.Message.Network;
import com.limegroup.gnutella.messages.MessageFactory.MessageParser;
import com.limegroup.gnutella.messages.vendor.VendorMessageFactory;
import com.limegroup.gnutella.routing.RouteTableMessage;

@Singleton
public class MessageParserBinderImpl implements MessageParserBinder {

    private final PingReplyFactory pingReplyFactory;

    private final QueryReplyFactory queryReplyFactory;

    private final QueryRequestFactory queryRequestFactory;

    private final VendorMessageFactory vendorMessageFactory;

    private final PingRequestFactory pingRequestFactory;

    @InspectablePrimitive("bad hops messages")
    private static volatile long badHops;

    @InspectablePrimitive("bad ttl messages")
    private static volatile long badTTL;

    @InspectablePrimitive("high hops messages")
    private static volatile long highHops;

    @InspectablePrimitive("adjusted ttl messages")
    private static volatile long adjustings;

    @InspectablePrimitive("parsed messages")
    private static volatile long parsings;

    @Inject
    public MessageParserBinderImpl(PingReplyFactory pingReplyFactory, QueryRequestFactory queryRequestFactory, QueryReplyFactory queryReplyFactory, VendorMessageFactory vendorMessageFactory, PingRequestFactory pingRequestFactory) {
        this.pingReplyFactory = pingReplyFactory;
        this.queryRequestFactory = queryRequestFactory;
        this.queryReplyFactory = queryReplyFactory;
        this.vendorMessageFactory = vendorMessageFactory;
        this.pingRequestFactory = pingRequestFactory;
    }

    public void bind(MessageFactory messageFactory) {
        messageFactory.setParser(Message.F_PING, new PingRequestParser());
        messageFactory.setParser(Message.F_PING_REPLY, new PingReplyParser());
        messageFactory.setParser(Message.F_QUERY, new QueryRequestParser());
        messageFactory.setParser(Message.F_QUERY_REPLY, new QueryReplyParser());
        messageFactory.setParser(Message.F_PUSH, new PushRequestParser());
        messageFactory.setParser(Message.F_ROUTE_TABLE_UPDATE, new RouteTableUpdateParser());
        messageFactory.setParser(Message.F_VENDOR_MESSAGE, new VendorMessageParser());
        messageFactory.setParser(Message.F_VENDOR_MESSAGE_STABLE, new VendorMessageStableParser());
    }

    /**
     * An abstract class for Gnutella Message parsers
     */
    public abstract static class GnutellaMessageParser implements MessageParser {

        public Message parse(byte[] header, byte[] payload, Network network, byte max, SocketAddress address) throws BadPacketException, IOException {
            byte func = header[16];
            byte ttl = header[17];
            byte hops = header[18];
            if (hops < 0) {
                badHops++;
                throw new BadPacketException("Negative (or very large) hops");
            } else if (ttl < 0) {
                badTTL++;
                throw new BadPacketException("Negative (or very large) TTL");
            } else if ((hops > max) && (func != Message.F_PING_REPLY)) {
                highHops++;
                throw new BadPacketException("func: " + func + ", ttl: " + ttl + ", hops: " + hops);
            } else if ((ttl + hops > max) && (func != Message.F_PING_REPLY)) {
                adjustings++;
                ttl = (byte) (max - hops);
                assert (ttl >= 0);
            } else parsings++;
            byte[] guid = new byte[16];
            System.arraycopy(header, 0, guid, 0, guid.length);
            return parse(guid, ttl, hops, payload, network);
        }

        protected abstract Message parse(byte[] guid, byte ttl, byte hops, byte[] payload, Network network) throws BadPacketException;
    }

    private class PingRequestParser extends GnutellaMessageParser {

        protected Message parse(byte[] guid, byte ttl, byte hops, byte[] payload, Network network) throws BadPacketException {
            return pingRequestFactory.createFromNetwork(guid, ttl, hops, payload, network);
        }
    }

    private class PingReplyParser extends GnutellaMessageParser {

        protected Message parse(byte[] guid, byte ttl, byte hops, byte[] payload, Network network) throws BadPacketException {
            return pingReplyFactory.createFromNetwork(guid, ttl, hops, payload, network);
        }
    }

    private class QueryRequestParser extends GnutellaMessageParser {

        protected Message parse(byte[] guid, byte ttl, byte hops, byte[] payload, Network network) throws BadPacketException {
            if (payload.length < 3) {
                throw new BadPacketException("Query request too short: " + payload.length);
            }
            return queryRequestFactory.createNetworkQuery(guid, ttl, hops, payload, network);
        }
    }

    private class QueryReplyParser extends GnutellaMessageParser {

        protected Message parse(byte[] guid, byte ttl, byte hops, byte[] payload, Network network) throws BadPacketException {
            if (payload.length < 26) {
                throw new BadPacketException("Query reply too short: " + payload.length);
            }
            return queryReplyFactory.createFromNetwork(guid, ttl, hops, payload, network);
        }
    }

    private static class PushRequestParser extends GnutellaMessageParser {

        protected Message parse(byte[] guid, byte ttl, byte hops, byte[] payload, Network network) throws BadPacketException {
            return new PushRequestImpl(guid, ttl, hops, payload, network);
        }
    }

    private static class RouteTableUpdateParser extends GnutellaMessageParser {

        protected Message parse(byte[] guid, byte ttl, byte hops, byte[] payload, Network network) throws BadPacketException {
            return RouteTableMessage.read(guid, ttl, hops, payload, network);
        }
    }

    private class VendorMessageParser extends GnutellaMessageParser {

        protected Message parse(byte[] guid, byte ttl, byte hops, byte[] payload, Network network) throws BadPacketException {
            return vendorMessageFactory.deriveVendorMessage(guid, ttl, hops, payload, network);
        }
    }

    private class VendorMessageStableParser extends GnutellaMessageParser {

        protected Message parse(byte[] guid, byte ttl, byte hops, byte[] payload, Network network) throws BadPacketException {
            return vendorMessageFactory.deriveVendorMessage(guid, ttl, hops, payload, network);
        }
    }
}
