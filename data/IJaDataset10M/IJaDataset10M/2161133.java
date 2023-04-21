package net.sf.eBus.messages;

import static net.sf.eBus.messages.EMessage.MessageType;

/**
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
@EMessageInfo(messageId = 104L, messageType = MessageType.REQUEST, messageFields = { "participantType", "participant", "quoteType" })
public final class LatestEquityQuoteRequest extends EMessage {

    /**
     * Creates a new instance of LatestEquityQuoteRequest.
     */
    public LatestEquityQuoteRequest(final String symbol, final ParticipantType pType, final String part, final PriceType qType) {
        this(generateHeader(symbol, 104L, LatestEquityQuoteRequest.class), pType, part, qType);
    }

    public LatestEquityQuoteRequest(final EMessageHeader header, final ParticipantType pType, final String part, final PriceType qType) {
        super(header);
        participantType = pType;
        participant = part;
        quoteType = qType;
    }

    /**
     * The participant type is either:
     * <ol>
     *   <li>
     *     NBBO
     *   </li>
     *   <li>
     *     Market
     *   </li>
     *   <li>
     *     Market market or ECN.
     *   </li>
     * </ol>
     */
    public final ParticipantType participantType;

    /**
     * The quote is from this market participant. If this is the
     * National Best Bid and Offer quote, then participant will
     * equal "NBBO".
     */
    public final String participant;

    /**
     * This notification is either:
     * <ol>
     *   <li>
     *     Latest: the latest quote for the named symbol on the
     *     specified participant for the current session.
     *   </li>
     *   <li>
     *     Open: the symbol's first quote on the participant for
     *     the current session.
     *   </li>
     *   <li>
     *     Close: the symbol's last quote on the participant for
     *     the <i>previous</i> session.
     *   </li>
     *   <li>
     *     Low: the symbol's lowest bid and ask prices quote on
     *     the participant for the current session.
     *   </li>
     *   <li>
     *     High: the symbol's highest bid and ask prices trade on
     *     the participant for the current session.
     *   </li>
     * </ol>
     */
    public final PriceType quoteType;
}
