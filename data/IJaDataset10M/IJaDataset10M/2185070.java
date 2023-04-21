package net.sf.eBus.client;

import net.sf.eBus.messages.EMessage;
import static net.sf.eBus.messages.EMessage.MessageType;
import net.sf.eBus.messages.EMessageHeader;
import net.sf.eBus.messages.EMessageInfo;

/**
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
@EMessageInfo(messageId = 102L, messageType = MessageType.REPLY, messageFields = { "participantType", "participant", "quoteType", "bid", "bidSize", "bidTrend", "ask", "askSize", "askTrend" })
public final class LatestEquityQuoteReply extends EMessage {

    /**
     * Creates a new instance of LatestEquityQuoteReply.
     */
    public LatestEquityQuoteReply(final String subject, final boolean replyStatus, final boolean finalReply, final String replyReason, final ParticipantType pType, final String part, final PriceType qType, final double bidPx, final int bidSz, final Trend bidTd, final double askPx, final int askSz, final Trend askTd) throws IllegalArgumentException {
        this(generateHeader(subject, replyStatus, finalReply, replyReason, 102L, LatestEquityQuoteReply.class), pType, part, qType, bidPx, bidSz, bidTd, askPx, askSz, askTd);
    }

    public LatestEquityQuoteReply(final EMessageHeader header, final ParticipantType pType, final String part, final PriceType qType, final double bidPx, final int bidSz, final Trend bidTd, final double askPx, final int askSz, final Trend askTd) throws IllegalArgumentException {
        super(header);
        if (pType == null) {
            throw (new IllegalArgumentException("null pType"));
        } else if (part == null || part.isEmpty() == true) {
            throw (new IllegalArgumentException("null or empty part"));
        } else if (bidTd == null) {
            throw (new IllegalArgumentException("null bidTd"));
        } else if (askTd == null) {
            throw (new IllegalArgumentException("null askTd"));
        }
        participantType = pType;
        participant = part;
        quoteType = qType;
        bid = bidPx;
        bidSize = bidSz;
        bidTrend = bidTd;
        ask = askPx;
        askSize = askSz;
        askTrend = askTd;
    }

    public final ParticipantType participantType;

    public final String participant;

    public final PriceType quoteType;

    public final double bid;

    public final int bidSize;

    public final Trend bidTrend;

    public final double ask;

    public final int askSize;

    public final Trend askTrend;
}
