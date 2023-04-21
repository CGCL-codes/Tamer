package com.ibm.tuningfork.infra.textwriter;

import com.ibm.tuningfork.infra.Logging;
import com.ibm.tuningfork.infra.data.EventInfo;
import com.ibm.tuningfork.infra.data.TimeInterval;
import com.ibm.tuningfork.infra.data.TimedData;
import com.ibm.tuningfork.infra.event.EventType;
import com.ibm.tuningfork.infra.event.TypedEvent;
import com.ibm.tuningfork.infra.feed.Feed;
import com.ibm.tuningfork.infra.stream.MixedStreamAndFeedIterator;
import com.ibm.tuningfork.infra.stream.core.EventStream;
import com.ibm.tuningfork.infra.stream.core.SampleStream;
import com.ibm.tuningfork.infra.stream.core.Stream;
import com.ibm.tuningfork.infra.stream.core.TimeIntervalStream;
import com.ibm.tuningfork.infra.streambundle.StreamBundle;
import com.ibm.tuningfork.infra.streambundle.StreamNotInBundleException;

/**
 * A TextWriter is used to output raw events and/or stream data to or other output sink, including a text view.
 * <p>
 * Different TextWriter's can support different formats (XML, CSV, etc). They are responsible for providing a
 * datumToString() method which converts a TimedData object into a string in the appropriate output file format, and
 * also for supplying header and footer data for the file as needed.
 */
public abstract class TextWriter {

    protected static final boolean DEBUG = false;

    protected static final boolean REFRESH_DEBUG = true;

    protected final String name;

    private final ITextOutput writer;

    protected final StreamBundle streams;

    protected MixedStreamAndFeedIterator iterator;

    private boolean written;

    protected TextWriter(String name, ITextOutput writer, StreamBundle streams) {
        this.name = name;
        this.writer = writer;
        this.streams = streams;
        if (streams != null) {
            streams.startAllStreams();
        }
        this.iterator = new MixedStreamAndFeedIterator(streams, TimeInterval.ALL_TIME.getStart(), TimeInterval.ALL_TIME.getEnd());
    }

    protected SampleStream samples(String name) {
        return streams == null ? null : (SampleStream) streams.getStreamByName(name);
    }

    protected TimeIntervalStream intervals(String name) {
        return streams == null ? null : (TimeIntervalStream) streams.getStreamByName(name);
    }

    protected EventStream events(String name) {
        return streams == null ? null : (EventStream) streams.getStreamByName(name);
    }

    /**
     * Write the complete information for the inputs to the output. All TimedData records from beginning to end are
     * processed.
     */
    public void write() {
        write(TimeInterval.ALL_TIME);
    }

    /**
     * Write the complete information for the inputs to the output. It outputs the header, then waits for all inputs to
     * complete, and then writes data for the individual TimedData elements, and finally writes a footer.
     *
     * This method may block; when that is not acceptable, use writeIncremental().
     *
     * @param interval The time interval over which to select TimedData records for output
     */
    public void write(TimeInterval interval) {
        try {
            writeHeader();
            if (streams != null && !streams.waitForAllStreamsToComplete()) {
                output("ERROR: failed to complete streams!");
            }
            EventInfo eventInfo;
            while ((eventInfo = iterator.getNext()) != null) {
                if (eventInfo.event.getTime() > interval.getEnd()) {
                    System.out.println("runover");
                }
                write(eventInfo.event, eventInfo.stream);
            }
            writeFooter();
        } catch (Exception e) {
            Logging.msgln("Error closing table figure as text output file: " + e);
        }
    }

    private boolean started = false;

    private boolean done = false;

    /**
     * Write data to the output until there is no more currently available, and then return. This is used for display in
     * interactive figures where it is not acceptable to block in the drawing loop. It could also be used to parallelize
     * output computation if there is more than one in progress.
     */
    public boolean writeIncremental() {
        if (REFRESH_DEBUG) {
            System.out.println("writeIncremental");
        }
        if (done) {
            if (REFRESH_DEBUG) {
                System.out.println("   (done)");
            }
            return false;
        }
        if (!started) {
            writeHeader();
            started = true;
            if (REFRESH_DEBUG) {
                System.out.println("   (header)");
            }
        }
        EventInfo eventInfo;
        while ((eventInfo = iterator.getNext()) != null) {
            write(eventInfo.event, eventInfo.stream);
        }
        if (iterator.stillOpen() == 0) {
            writeFooter();
            done = true;
            if (REFRESH_DEBUG) {
                System.out.println("   (footer)");
            }
        }
        boolean hasWritten = written;
        written = false;
        return hasWritten;
    }

    /**
     * Write the header information to the output file, including format metadata, property information, and so on.
     */
    public void writeHeader() {
        output(getHeader());
    }

    /**
     * Write whatever data should terminate the output file, such as closing tags or brackets.
     */
    public void writeFooter() {
        output(getFooter());
        writer.close();
    }

    /**
     * Write a datum to the output file. The source is provided so that additional information can be included about the
     * datum in the output.
     *
     * @param datum Event or stream datum to output
     * @param source Stream or feed from which it comes
     */
    public void write(TimedData datum, Stream source) {
        output(datumToString(datum, source));
    }

    /**
     * Write a datum to the output file.
     *
     * @param datum Event or stream datum to output
     */
    public void write(TimedData datum) {
        output(datumToString(datum));
    }

    /**
     * Get the string to put at the beginning of the output file. This should include format metadata as well as things
     * like feed properties and stream definitions.
     *
     * @return String for beginning of output file
     */
    public String getHeader() {
        return "";
    }

    /**
     * Get the string with which to end the output file
     *
     * @return Ending string
     */
    public String getFooter() {
        return "";
    }

    /**
     * Convert a datum from a stream or feed into a string for the output file.
     *
     * @param datum The datum to convert
     * @param name The name of the datum, or null if not specified
     * @param feedlet The name of the source feedlet of the datum, or null if not specified
     * @return Datum converted into a string of the appropriate format
     */
    public String datumToString(TimedData datum, EventType type, String feedlet) {
        return "";
    }

    /**
     * Convert an event into a string for the output file
     *
     * @param event The event to convert
     * @return The resulting string
     */
    public String datumToString(TimedData datum) {
        return datumToString(datum, null);
    }

    /**
     * Convert a datum from a stream, feed, or feedlet into a string for the output file.
     *
     * In order to output complete information including the name, the source is needed for stream data since for
     * efficiency reasons stream data does not contain back-pointers into the originating stream or to a type descriptor
     * for the datum.
     *
     * @param datum The event or stream datum to convert
     * @param source The source of the datum (feed, feedlet, or stream)
     * @return A textual representation of the datum
     */
    public String datumToString(TimedData datum, Stream source) {
        if (datum instanceof TypedEvent) {
            TypedEvent event = (TypedEvent) datum;
            return datumToString(event, event.getType(), event.getFeedlet().getName());
        } else if (source instanceof Stream) {
            return datumToString(datum, source.getEventType(), null);
        } else {
            return datumToString(datum, null, null);
        }
    }

    /**
     * Output a string to the {@link ITextOutput} writer. All output funneled through this method so we can turn on
     * debug output and avoid unneccessary calls to the underlying widget/device.
     *
     * @param s String to output
     */
    protected void output(String s) {
        if (s.length() > 0) {
            writer.print(s);
            written = true;
            if (DEBUG) {
                Logging.msgln(s);
            }
        }
    }

    /**
     * Output a string with a newline appended.
     *
     * @param s String to output
     */
    protected void outputln(String s) {
        output(s + "\n");
    }

    protected void outputln() {
        output("\n");
    }

    /**
     * Inquire about the number of streams for which there is still data to process
     *
     * @return number of streams still open
     */
    public int incompleteInputCount() {
        return iterator.stillOpen();
    }

    public static String rightJustify(String s, int columnSize) {
        String out = "";
        for (int i = s.length(); i < columnSize; i++) {
            out += " ";
        }
        out += s + " ";
        return out;
    }

    public static boolean hasRequiredStreams(StreamBundle bundle, String[] names) {
        for (String s : names) {
            if (bundle.getStreamByName(s) == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasOnlyRequiredStreams(StreamBundle bundle, String[] names) {
        return bundle.size() == names.length && hasRequiredStreams(bundle, names);
    }

    public static StreamBundle getStreams(Feed feed, String[] names) {
        try {
            return new StreamBundle(feed, names);
        } catch (StreamNotInBundleException e) {
            return null;
        }
    }
}
