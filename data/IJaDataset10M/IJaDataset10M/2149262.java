package com.esri.gpt.control.georss;

import com.esri.gpt.catalog.search.OpenSearchProperties;
import com.esri.gpt.catalog.search.SearchResultRecord;
import com.esri.gpt.catalog.search.SearchResultRecords;
import com.esri.gpt.framework.jsf.MessageBroker;
import com.esri.gpt.framework.util.Val;
import java.io.PrintWriter;

class HtmlFragmentFeedWriter implements FeedWriter {

    /** Message broker. */
    protected MessageBroker _messageBroker;

    /** print writer */
    protected PrintWriter _writer;

    /** links target */
    protected RecordSnippetWriter.Target _target = RecordSnippetWriter.Target.blank;

    /**
 * Creates instance of the writer.
 * @param messageBroker message broker
 * @param writer underlying print writer
 */
    public HtmlFragmentFeedWriter(MessageBroker messageBroker, PrintWriter writer) {
        _messageBroker = messageBroker;
        if (_messageBroker == null) {
            throw new IllegalArgumentException("A MessageBroker is required.");
        }
        _writer = writer;
    }

    /**
 * Gets links target.
 * @return links targets
 */
    public RecordSnippetWriter.Target getTarget() {
        return _target;
    }

    /**
 * Sets links target.
 * @param target links target
 */
    public void setTarget(RecordSnippetWriter.Target target) {
        _target = target;
    }

    /**
 * Writers records.
 * @param records records to write
 */
    public void write(SearchResultRecords records) {
        OpenSearchProperties osProps = records.getOpenSearchProperties();
        if (osProps != null) {
            _writer.println("<input type=\"hidden\" id=\"startIndex\" value=\"" + osProps.getStartRecord() + "\"/>");
            _writer.println("<input type=\"hidden\" id=\"itemsPerPage\" value=\"" + osProps.getRecordsPerPage() + "\"/>");
            _writer.println("<input type=\"hidden\" id=\"totalResults\" value=\"" + osProps.getNumberOfHits() + "\"/>");
        }
        RecordSnippetWriter snippetWriter = new RecordSnippetWriter(_messageBroker, _writer);
        snippetWriter.setShowTitle(true);
        snippetWriter.setShowIcon(true);
        snippetWriter.setClipText(true);
        snippetWriter.setTarget(_target);
        for (SearchResultRecord record : records) {
            writeRecord(snippetWriter, record);
        }
    }

    /**
 * Writes a single record.
 * @param snippetWriter description HTML snippet writer
 * @param record records to writeTag
 */
    protected void writeRecord(RecordSnippetWriter snippetWriter, SearchResultRecord record) {
        snippetWriter.write(record);
        _writer.flush();
    }
}
