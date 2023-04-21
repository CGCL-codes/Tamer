package org.otfeed.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.otfeed.command.AggregationSpan;
import org.otfeed.command.BookDeleteTypeEnum;
import org.otfeed.command.OptionTypeEnum;
import org.otfeed.command.TimeUnitEnum;
import org.otfeed.event.DividendPropertyEnum;
import org.otfeed.event.InstrumentEnum;
import org.otfeed.event.OTError;
import org.otfeed.event.TradePropertyEnum;
import org.otfeed.event.TradeSideEnum;

public final class Marshalers {

    private Marshalers() {
    }

    /**
	 * Marshaler for Boolean type.
	 */
    public static final IMarshaler<Boolean> BOOLEAN = new IMarshaler<Boolean>() {

        public void marshal(Boolean object, ByteBuffer output) throws ProtocolException {
            output.put((byte) (object ? 1 : 0));
        }

        public Boolean unmarshal(ByteBuffer buffer) throws ProtocolException {
            int c = buffer.get();
            if (c == 0 || c == 'N') {
                return false;
            } else if (c == 1 || c == 'Y') {
                return true;
            } else {
                throw new ProtocolException("boolean format is incorrect: " + ((char) c), buffer);
            }
        }
    };

    private static final Charset UTF8 = Charset.forName("utf-8");

    /**
	 * Marshaler for a variable-length string.
	 * Internal format is a two-byte little-endian "short"
	 * specifying the length, followed by utf-8 encoded bytes.
	 */
    public static final IMarshaler<String> STRING = new IMarshaler<String>() {

        public void marshal(String val, ByteBuffer out) throws ProtocolException {
            ByteBuffer encoded = UTF8.encode(val);
            int len = encoded.limit() + 1;
            out.putShort((short) len);
            out.put(encoded);
            out.put((byte) 0);
        }

        public String unmarshal(ByteBuffer in) throws ProtocolException {
            int len = in.getShort() - 1;
            ByteBuffer encoded = in.duplicate();
            in.position(in.position() + len);
            if (in.get() != 0) {
                throw new ProtocolException("Unexpected termination of variable-lenght string", in);
            }
            encoded.limit(encoded.position() + len);
            return UTF8.decode(encoded).toString().trim();
        }
    };

    /**
	 * Marshaler for a fixed-length string (padded by binary zeroes if needed).
	 */
    static class FixedLengthStringMarshaler implements IMarshaler<String> {

        private final int len;

        public FixedLengthStringMarshaler(int len) {
            this.len = len;
        }

        public void marshal(String val, ByteBuffer out) throws ProtocolException {
            ByteBuffer encoded = UTF8.encode(val);
            if (encoded.limit() >= len) {
                encoded.limit(len);
                out.put(encoded);
            } else {
                int padding = len - encoded.limit();
                out.put(encoded);
                while (padding-- > 0) {
                    out.put((byte) 0);
                }
            }
        }

        public String unmarshal(ByteBuffer in) throws ProtocolException {
            int len = this.len;
            ByteBuffer encoded = in.duplicate();
            in.position(in.position() + len);
            while (len > 0) {
                if (encoded.get(encoded.position() + len - 1) == 0) {
                    len--;
                } else {
                    break;
                }
            }
            encoded.limit(encoded.position() + len);
            return UTF8.decode(encoded).toString().trim();
        }
    }

    /**
	 * Marshaler for a string of length 2.
	 */
    public static final IMarshaler<String> STRING_2 = new FixedLengthStringMarshaler(2);

    /**
	 * Marshaler for a string of length 3.
	 */
    public static final IMarshaler<String> STRING_3 = new FixedLengthStringMarshaler(3);

    /**
	 * Marshaler for a string of length 4.
	 */
    public static final IMarshaler<String> STRING_4 = new FixedLengthStringMarshaler(4);

    /**
	 * Marshaler for a string of length 8.
	 */
    public static final IMarshaler<String> STRING_8 = new FixedLengthStringMarshaler(8);

    /**
	 * Marshaler for a string of length 9.
	 */
    public static final IMarshaler<String> STRING_9 = new FixedLengthStringMarshaler(9);

    /**
	 * Marshaler for a string of length 12.
	 */
    public static final IMarshaler<String> STRING_12 = new FixedLengthStringMarshaler(12);

    /**
	 * Marshaler for a string of length 15.
	 */
    public static final IMarshaler<String> STRING_15 = new FixedLengthStringMarshaler(15);

    /**
	 * Marshaler for a string of length 16.
	 */
    public static final IMarshaler<String> STRING_16 = new FixedLengthStringMarshaler(16);

    /**
	 * Marshaler for a string of length 21.
	 */
    public static final IMarshaler<String> STRING_21 = new FixedLengthStringMarshaler(21);

    /**
	 * Marshaler for a string of length 64.
	 */
    public static final IMarshaler<String> STRING_64 = new FixedLengthStringMarshaler(64);

    /**
	 * Marshaler for a string of length 80.
	 */
    public static final IMarshaler<String> STRING_80 = new FixedLengthStringMarshaler(80);

    /**
	 * Marshaler for a Date. Internal format is seconds since UNIX epoch.
	 */
    public static final IMarshaler<Date> DATE = new IMarshaler<Date>() {

        public void marshal(Date date, ByteBuffer out) throws ProtocolException {
            if (date == null) {
                out.putInt(0);
            } else {
                int stamp = (int) (date.getTime() / 1000L);
                out.putInt(stamp);
            }
        }

        public Date unmarshal(ByteBuffer in) throws ProtocolException {
            int stamp = in.getInt();
            if (stamp == 0) {
                return null;
            } else {
                return new Date(stamp * 1000L);
            }
        }
    };

    /**
	 * Marshaler for a TradeSideEnum.
	 */
    public static final IMarshaler<TradeSideEnum> TRADE_SIDE = new IMarshaler<TradeSideEnum>() {

        public void marshal(TradeSideEnum val, ByteBuffer out) throws ProtocolException {
            if (val == TradeSideEnum.BUYER) {
                out.put((byte) 'B');
            } else if (val == TradeSideEnum.SELLER) {
                out.put((byte) 'S');
            } else {
                throw new ProtocolException("unexpected enum  value: " + val, out);
            }
        }

        public TradeSideEnum unmarshal(ByteBuffer in) throws ProtocolException {
            char sideCode = (char) in.get();
            if (sideCode == 'B') {
                return TradeSideEnum.BUYER;
            } else if (sideCode == 'S') {
                return TradeSideEnum.SELLER;
            } else {
                throw new ProtocolException("unexpected side code: " + sideCode, in);
            }
        }
    };

    /**
	 * Marshaler for OptionTypeENum.
	 */
    public static final IMarshaler<OptionTypeEnum> OPTION_TYPE = new IMarshaler<OptionTypeEnum>() {

        public void marshal(OptionTypeEnum val, ByteBuffer out) throws ProtocolException {
            if (val == OptionTypeEnum.AMERICAN) {
                out.put((byte) 'A');
            } else if (val == OptionTypeEnum.EUROPEAN) {
                out.put((byte) 'E');
            } else if (val == OptionTypeEnum.CAPPED) {
                out.put((byte) 'C');
            } else {
                throw new ProtocolException("unexpected option type enum: " + val, out);
            }
        }

        public OptionTypeEnum unmarshal(ByteBuffer in) throws ProtocolException {
            char code = (char) in.get();
            if (code == 'A') {
                return OptionTypeEnum.AMERICAN;
            } else if (code == 'E') {
                return OptionTypeEnum.EUROPEAN;
            } else if (code == 'C') {
                return OptionTypeEnum.CAPPED;
            } else {
                throw new ProtocolException("unexpected option type code: " + code, in);
            }
        }
    };

    /**
	 * Marshaler for InstrumentEnum.
	 */
    public static final IMarshaler<InstrumentEnum> INSTRUMENT = new IMarshaler<InstrumentEnum>() {

        public void marshal(InstrumentEnum val, ByteBuffer out) throws ProtocolException {
            out.put((byte) Codecs.INSTRUMENT.encode(val));
        }

        public InstrumentEnum unmarshal(ByteBuffer in) throws ProtocolException {
            int code = in.get();
            InstrumentEnum i = Codecs.INSTRUMENT.decode(code);
            if (i == null) {
                throw new ProtocolException("unknown instrument code: " + code, in);
            }
            return i;
        }
    };

    /**
	 * Marshaler for BookDeleteTypeEnum.
	 */
    public static final IMarshaler<BookDeleteTypeEnum> BOOK_DELETE_TYPE = new IMarshaler<BookDeleteTypeEnum>() {

        public void marshal(BookDeleteTypeEnum val, ByteBuffer out) throws ProtocolException {
            out.put((byte) Codecs.BOOK_DELETE_TYPE.encode(val));
        }

        public BookDeleteTypeEnum unmarshal(ByteBuffer in) throws ProtocolException {
            int code = in.get();
            BookDeleteTypeEnum deleteType = Codecs.BOOK_DELETE_TYPE.decode(code);
            if (deleteType == null) {
                throw new ProtocolException("unknown delete type code: " + code, in);
            }
            return deleteType;
        }
    };

    /**
	 * Marshaler for StatusEnum.
	 */
    public static final IMarshaler<StatusEnum> STATUS = new IMarshaler<StatusEnum>() {

        public void marshal(StatusEnum val, ByteBuffer out) throws ProtocolException {
            out.put((byte) Codecs.STATUS.encode(val));
        }

        public StatusEnum unmarshal(ByteBuffer in) throws ProtocolException {
            int code = in.get();
            StatusEnum status = Codecs.STATUS.decode(code);
            if (status == null) {
                throw new ProtocolException("unknown status code: " + code, in);
            }
            return status;
        }
    };

    public static final IMarshaler<MessageEnum> MESSAGE = new IMarshaler<MessageEnum>() {

        public void marshal(MessageEnum val, ByteBuffer out) throws ProtocolException {
            out.put((byte) Codecs.MESSAGE.encode(val));
        }

        public MessageEnum unmarshal(ByteBuffer in) throws ProtocolException {
            int code = in.get();
            MessageEnum mess = Codecs.MESSAGE.decode(code);
            if (mess == null) {
                throw new ProtocolException("unknown message type code: " + code, in);
            }
            return mess;
        }
    };

    /**
	 * Marshaler for CommandEnum.
	 */
    public static final IMarshaler<CommandEnum> COMMAND = new IMarshaler<CommandEnum>() {

        public void marshal(CommandEnum val, ByteBuffer out) throws ProtocolException {
            out.putInt(Codecs.COMMAND.encode(val));
        }

        public CommandEnum unmarshal(ByteBuffer in) throws ProtocolException {
            int code = in.getInt();
            CommandEnum command = Codecs.COMMAND.decode(code);
            if (command == null) {
                throw new ProtocolException("unknown command code: " + code, in);
            }
            return command;
        }
    };

    private static final Set<TradePropertyEnum> TRADE_PROPERTY_ENUM_ALL = EnumSet.allOf(TradePropertyEnum.class);

    /**
	 * Marshaler for TradePropertyEnum set.
	 */
    public static final IMarshaler<Set<TradePropertyEnum>> TRADE_PROPERTY_SET = new IMarshaler<Set<TradePropertyEnum>>() {

        public void marshal(Set<TradePropertyEnum> val, ByteBuffer out) throws ProtocolException {
            int mask = 0;
            for (TradePropertyEnum tp : val) {
                int code = Codecs.TRADE_PROPERTY.encode(tp);
                mask |= 1 << code;
            }
            out.put((byte) mask);
        }

        public Set<TradePropertyEnum> unmarshal(ByteBuffer in) throws ProtocolException {
            int mask = in.get();
            if (mask < 0) mask += 256;
            Set<TradePropertyEnum> set = EnumSet.noneOf(TradePropertyEnum.class);
            for (TradePropertyEnum p : TRADE_PROPERTY_ENUM_ALL) {
                int pmask = 1 << Codecs.TRADE_PROPERTY.encode(p);
                if ((mask & pmask) != 0) {
                    set.add(p);
                    mask ^= pmask;
                }
            }
            if (mask != 0) {
                throw new ProtocolException("extra bits in the trade properties mask: " + mask, in);
            }
            return set;
        }
    };

    private static final Set<DividendPropertyEnum> DIVIDEND_PROPERTY_ENUM_ALL = EnumSet.allOf(DividendPropertyEnum.class);

    /**
	 * Marshaler for DividendPropertyEnum set.
	 */
    public static final IMarshaler<Set<DividendPropertyEnum>> DIVIDEND_PROPERTY_SET = new IMarshaler<Set<DividendPropertyEnum>>() {

        public void marshal(Set<DividendPropertyEnum> val, ByteBuffer out) throws ProtocolException {
            int mask = 0;
            for (DividendPropertyEnum dp : val) {
                mask |= 1 << Codecs.DIVIDEND_PROPERTY.encode(dp);
            }
            out.putInt(mask);
        }

        public Set<DividendPropertyEnum> unmarshal(ByteBuffer in) throws ProtocolException {
            int mask = in.getShort();
            Set<DividendPropertyEnum> set = EnumSet.noneOf(DividendPropertyEnum.class);
            for (DividendPropertyEnum p : DIVIDEND_PROPERTY_ENUM_ALL) {
                int pmask = 1 << Codecs.DIVIDEND_PROPERTY.encode(p);
                if ((mask & pmask) != 0) {
                    set.add(p);
                    mask ^= pmask;
                }
            }
            return set;
        }
    };

    /**
	 * Marshaler for AggregationSpan.
	 */
    public static final IMarshaler<AggregationSpan> SPAN = new IMarshaler<AggregationSpan>() {

        private final Map<Integer, TimeUnitEnum> decodeMap = new HashMap<Integer, TimeUnitEnum>();

        {
            for (TimeUnitEnum tu : EnumSet.allOf(TimeUnitEnum.class)) {
                decodeMap.put(tu.code, tu);
            }
        }

        public void marshal(AggregationSpan span, ByteBuffer out) throws ProtocolException {
            out.put((byte) span.units.code);
            out.put((byte) 0);
            out.putShort((short) span.length);
        }

        public AggregationSpan unmarshal(ByteBuffer in) throws ProtocolException {
            int units = in.get();
            TimeUnitEnum tu = decodeMap.get(units);
            if (tu == null) {
                throw new ProtocolException("unknown time unit code: " + units, in);
            }
            in.get();
            int length = in.getShort();
            return new AggregationSpan(tu, length);
        }
    };

    /**
	 * Marshaler for OTError.
	 */
    public static final IMarshaler<OTError> OT_ERROR = new IMarshaler<OTError>() {

        public void marshal(OTError val, ByteBuffer out) throws ProtocolException {
            out.putShort((short) val.getCode());
            STRING.marshal(val.getDescription(), out);
        }

        public OTError unmarshal(ByteBuffer in) throws ProtocolException {
            short errorCode = in.getShort();
            String reason = STRING.unmarshal(in);
            return new OTError(errorCode, reason);
        }
    };
}
