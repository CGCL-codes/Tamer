package org.otfeed.support;

import org.otfeed.event.IBookListener;
import org.otfeed.event.IListener;
import org.otfeed.event.IQuoteListener;
import org.otfeed.event.OTBBO;
import org.otfeed.event.OTBookCancel;
import org.otfeed.event.OTBookChange;
import org.otfeed.event.OTBookDelete;
import org.otfeed.event.OTBookExecute;
import org.otfeed.event.OTBookOrder;
import org.otfeed.event.OTBookPriceLevel;
import org.otfeed.event.OTBookPurge;
import org.otfeed.event.OTBookReplace;
import org.otfeed.event.OTError;
import org.otfeed.event.OTMMQuote;
import org.otfeed.event.OTQuote;
import org.otfeed.event.OTTrade;

/**
 * Common listener implements all lintener intefaces
 * of the org.otfeed API. Hence, it can be passed
 * as <code>listener</code> paramater to any command
 * object.
 * <p/>
 * This object delegates actual output of the data
 * to the {@link #getDataWriter() dataWriter} object, that defaults
 * to {@link SimpleDataWriter}.
 * <p/>
 * Arbitrary id string can be (optionally) associated with
 * this object, allowing to use multiple instancies of
 * this object with a single {@link #getDataWriter() dataWriter}. 
 * In this case id strings help to identify the source
 * of the record in the <code>dataWriter</code>.
 *
 * @param <T> listener type (the type of event object).
 */
public class CommonListener<T> implements IListener<T>, IQuoteListener, IBookListener {

    public CommonListener() {
    }

    public CommonListener(String id) {
        setId(id);
    }

    public CommonListener(String id, IDataWriter writer) {
        setId(id);
        setDataWriter(writer);
    }

    public CommonListener(IDataWriter writer) {
        setDataWriter(writer);
    }

    private String id = null;

    public String getId() {
        return id;
    }

    public void setId(String val) {
        id = val;
    }

    private IDataWriter writer = new SimpleDataWriter();

    public IDataWriter getDataWriter() {
        return writer;
    }

    public void setDataWriter(IDataWriter val) {
        writer = val;
    }

    public void onData(T data) {
        writer.writeData(id, data);
    }

    public void onDataEnd() {
        writer.close();
    }

    public void onError(OTError error) {
        writer.handleError(id, error);
        writer.close();
    }

    public void onBBO(OTBBO data) {
        writer.writeData(id, data);
    }

    public void onMMQuote(OTMMQuote data) {
        writer.writeData(id, data);
    }

    public void onQuote(OTQuote data) {
        writer.writeData(id, data);
    }

    public void onTrade(OTTrade data) {
        writer.writeData(id, data);
    }

    public void onBookCancel(OTBookCancel data) {
        writer.writeData(id, data);
    }

    public void onBookChange(OTBookChange data) {
        writer.writeData(id, data);
    }

    public void onBookDelete(OTBookDelete data) {
        writer.writeData(id, data);
    }

    public void onBookExecute(OTBookExecute data) {
        writer.writeData(id, data);
    }

    public void onBookOrder(OTBookOrder data) {
        writer.writeData(id, data);
    }

    public void onBookPriceLevel(OTBookPriceLevel data) {
        writer.writeData(id, data);
    }

    public void onBookPurge(OTBookPurge data) {
        writer.writeData(id, data);
    }

    public void onBookReplace(OTBookReplace data) {
        writer.writeData(id, data);
    }
}
