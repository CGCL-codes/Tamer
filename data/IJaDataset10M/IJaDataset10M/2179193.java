package org.otfeed.protocol.message;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import org.otfeed.command.ListSymbolEnum;
import org.otfeed.command.ListSymbolsCommand.MatchStyleEnum;
import org.otfeed.protocol.CommandEnum;
import org.otfeed.protocol.Marshalers;
import org.otfeed.protocol.MessageEnum;
import org.otfeed.protocol.ProtocolException;

public final class ListSymbolsRequestMarshaler extends AbstractSessionMarshaler<ListSymbolsRequest> {

    public ListSymbolsRequestMarshaler(String sid) {
        super(ListSymbolsRequest.class, MessageEnum.REQUEST, CommandEnum.REQUEST_LIST_SYMBOLS_EX, sid);
    }

    @Override
    public void marshalBody(Header header, String sid, ListSymbolsRequest val, ByteBuffer out) throws IllegalArgumentException {
        Marshalers.STRING_15.marshal(val.exchangeCode, out);
        Marshalers.STRING_15.marshal(val.symbolCodePattern, out);
        int mask = 0;
        for (ListSymbolEnum t : val.types) {
            mask |= t.code;
        }
        switch(val.matchStyle) {
            case PREFIX:
                break;
            case CONTAINS:
                mask |= ListSymbolEnum.CONTAINS_FLAG;
                break;
            default:
                throw new IllegalArgumentException("unexpected match type");
        }
        out.putInt(mask);
    }

    @Override
    public ListSymbolsRequest unmarshalBody(Header header, String sid, ByteBuffer in) throws ProtocolException {
        String exchangeCode = Marshalers.STRING_15.unmarshal(in);
        String symbolCodePattern = Marshalers.STRING_15.unmarshal(in);
        int mask = in.getInt();
        EnumSet<ListSymbolEnum> types = EnumSet.noneOf(ListSymbolEnum.class);
        for (ListSymbolEnum t : EnumSet.allOf(ListSymbolEnum.class)) {
            if ((mask & t.code) != 0) {
                types.add(t);
                mask &= ~t.code;
            }
        }
        MatchStyleEnum style;
        if ((mask & ListSymbolEnum.CONTAINS_FLAG) != 0) {
            style = MatchStyleEnum.CONTAINS;
            mask &= ~ListSymbolEnum.CONTAINS_FLAG;
        } else {
            style = MatchStyleEnum.PREFIX;
        }
        if (mask != 0) {
            throw new ProtocolException("Unresolved bits in mask: 0x" + Integer.toHexString(mask), in);
        }
        return new ListSymbolsRequest(header.requestId, exchangeCode, symbolCodePattern, types, style);
    }
}
