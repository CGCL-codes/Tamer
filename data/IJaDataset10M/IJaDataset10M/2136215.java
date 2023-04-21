package org.otfeed.protocol.request;

import org.otfeed.command.MonthAndYear;
import org.otfeed.command.PriceRange;
import org.otfeed.command.VolumeStyleEnum;
import org.otfeed.event.ICompletionDelegate;
import org.otfeed.event.IDataDelegate;
import org.otfeed.event.OTBBO;
import org.otfeed.event.OTMMQuote;
import org.otfeed.event.OTQuote;
import org.otfeed.event.OTTrade;
import org.otfeed.protocol.message.Message;
import org.otfeed.protocol.message.OptionChainResponse;

/**
 * Request to receive option chain snapshot.
 * Snapshot is the most recent events.
 * <p/>
 * Sends out {@link org.otfeed.protocol.message.OptionChainSnapshotRequest} message.
 * <p/>
 * Expects to receive 
 * {@link org.otfeed.protocol.message.OptionChainResponse} messages with the payload.
 * If payload is null, this is the last message that finishes request processing.
 */
public final class OptionChainSnapshotRequest extends AbstractTickRequest {

    private final Message message;

    public OptionChainSnapshotRequest(int requestId, String exchangeCode, String symbolCode, MonthAndYear expiration, PriceRange strikeRange, VolumeStyleEnum volumeStyle, IDataDelegate<OTQuote> quoteDelegate, IDataDelegate<OTTrade> tradeDelegate, IDataDelegate<OTMMQuote> mmQuoteDelegate, IDataDelegate<OTBBO> bboDelegate, ICompletionDelegate completionDelegate) {
        super(quoteDelegate, tradeDelegate, mmQuoteDelegate, bboDelegate, completionDelegate);
        Check.notNull(exchangeCode, "exchangeCode");
        Check.notNull(symbolCode, "symbolCode");
        Check.notNull(volumeStyle, "volumeStyle");
        this.message = new org.otfeed.protocol.message.OptionChainSnapshotRequest(requestId, exchangeCode, symbolCode, expiration, strikeRange, volumeStyle, getTypeSet());
    }

    @Override
    public Message getCancelMessage(int requestId) {
        return null;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public JobStatus handleMessage(Message message) {
        OptionChainResponse response = (OptionChainResponse) message;
        if (response.tickEvent == null) {
            return JobStatus.FINISHED;
        }
        handleTickEvent(response.tickEvent);
        return JobStatus.ACTIVE;
    }
}
