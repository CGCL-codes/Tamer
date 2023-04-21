package org.openexi.fujitsu.proc.io.compression;

import java.io.IOException;
import java.io.OutputStream;
import org.openexi.fujitsu.proc.common.QName;
import org.openexi.fujitsu.proc.io.Scribble;
import org.openexi.fujitsu.proc.io.StringTable;
import org.openexi.fujitsu.proc.io.ValueScriber;
import org.openexi.fujitsu.schema.EXISchema;

final class ChannellingValueScriberProxy extends ValueScriber {

    private final ChannelKeeper m_channelKeeper;

    private final ValueScriber m_valueScriber;

    ChannellingValueScriberProxy(ChannelKeeper channelKeeper, ValueScriber valueScriber, ChannellingScriber scriber) {
        super(scriber);
        m_channelKeeper = channelKeeper;
        m_valueScriber = valueScriber;
    }

    @Override
    public void scribe(String value, Scribble scribble, String localName, String uri, int tp) throws IOException {
        final ScriberChannel channel = (ScriberChannel) m_channelKeeper.getChannel(localName, uri);
        final boolean reached = m_channelKeeper.incrementValueCount(channel);
        channel.values.add(new ScriberValueHolder(localName, uri, tp, toValue(value, scribble), this));
        if (reached) ((ChannellingScriber) m_scriber).finishBlock();
    }

    @Override
    public final QName getName() {
        return m_valueScriber.getName();
    }

    @Override
    public short getCodecID() {
        return m_valueScriber.getCodecID();
    }

    @Override
    public int getBuiltinRCS(int simpleType) {
        return m_valueScriber.getBuiltinRCS(simpleType);
    }

    @Override
    public void setEXISchema(EXISchema schema) {
        m_valueScriber.setEXISchema(schema);
    }

    @Override
    public void setStringTable(StringTable stringTable) {
        m_valueScriber.setStringTable(stringTable);
    }

    @Override
    public void setValueMaxLength(int valueMaxLength) {
        m_valueScriber.setValueMaxLength(valueMaxLength);
    }

    @Override
    public boolean process(String value, int tp, EXISchema schema, Scribble scribble) {
        return m_valueScriber.process(value, tp, schema, scribble);
    }

    @Override
    public Object toValue(String value, Scribble scribble) {
        return m_valueScriber.toValue(value, scribble);
    }

    @Override
    public void doScribe(Object value, String localName, String uri, int tp, OutputStream channelStream) throws IOException {
        m_valueScriber.doScribe(value, localName, uri, tp, channelStream);
    }

    @Override
    public void scribe(String value, Scribble scribble, String localName, String uri, int tp, OutputStream ostream) throws IOException {
        m_valueScriber.scribe(value, scribble, localName, uri, tp, ostream);
    }
}
