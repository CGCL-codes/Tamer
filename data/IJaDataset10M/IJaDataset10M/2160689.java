package DE.FhG.IGD.gps;

import java.io.*;
import java.util.*;
import DE.FhG.IGD.util.Listener;
import DE.FhG.IGD.semoa.event.*;

/**
 * Reads and parses NMEA183 sentences. The GPSParser runs in a thread of
 * his own and notifies registered <code>Listener</code> about parsed sentences
 * as well as state changes.
 * 
 * @author Dennis Bartussek <dennis.bartussek@igd.fhg.de>
 */
public class GPSParser implements Runnable {

    /**
     * The event reflector which is notified, every time a gps message has been
     * parsed or if the parser changes its state.
     */
    protected EventReflector eventReflector_;

    private volatile Thread parserThread_;

    private Reader reader_;

    /**
     * Constructs a new <code>GPSParser</code> to read from the specified
     * <code>reader</code>.
     *
     * @param reader the <code>Reader</code> to read from
     */
    public GPSParser(Reader reader) {
        eventReflector_ = new EventReflectorImpl();
        parserThread_ = null;
        reader_ = reader;
    }

    /**
      * Adds a <code>Listener</code> to listen to <code>GPSParserEvent</code>.
      *
      * @param listener The <code>Listener</code> to be registered.
      * @return The <code>TrustedListener</code> that wraps around
      *   the given <code>Listener</code>.
      * @exception NullPointerException if <code>listener</code>
      *   is <code>null</code>.
      */
    public TrustedListener addListener(Listener listener) {
        return eventReflector_.addListener(listener);
    }

    /**
     * Starts the parsing process by causing the internal parser thread to begin
     * execution.
     * <p>
     * The result is that two threads are running concurrently: the current
     * thread (which returns from the call to the start method) and the internal
     * parser thread (which executes its run method).
     * 
     * @return <code>true</code> if the parsing process started successfully,
     *   <code>false</code> if the parsing process has already been started
     */
    public boolean start() {
        if (parserThread_ == null) {
            parserThread_ = new Thread(this, "gps-parser");
            parserThread_.start();
            return true;
        }
        return false;
    }

    /**
     * Stops the current parsing process by causing the internal parser thread
     * to stop execution.
     *
     * @return <code>true</code> if the parsing process stopped successfully,
     *   <code>false</code> if the parsing process has not been started
     */
    public boolean stop() {
        if (parserThread_ != null) {
            parserThread_.interrupt();
            parserThread_ = null;
            return true;
        }
        return false;
    }

    /**
     * Causes this <code>GPSParser</code> to start parsing.
     * <p>
     * If called directly this method does nothing and returns. Use
     * <code>start</code> to start the parsing process.
     *
     * @see #start()
     */
    public void run() {
        StreamTokenizer tokenizer;
        Thread thisThread;
        thisThread = Thread.currentThread();
        tokenizer = new StreamTokenizer(reader_);
        tokenizer.resetSyntax();
        tokenizer.quoteChar('$');
        eventReflector_.notifyListeners(new GPSParserEvent("RUNNING"));
        try {
            while ((parserThread_ == thisThread) && (tokenizer.ttype != StreamTokenizer.TT_EOF)) {
                tokenizer.nextToken();
                if (tokenizer.ttype == '$') {
                    try {
                        parseSentence(tokenizer.sval);
                    } catch (NMEA183SentenceFormatException e) {
                        eventReflector_.notifyListeners(new GPSParserEvent("ERROR", e));
                    }
                }
            }
        } catch (IOException e) {
            eventReflector_.notifyListeners(new GPSParserEvent("ERROR", e));
        }
        eventReflector_.notifyListeners(new GPSParserEvent("TERMINATED"));
        parserThread_ = null;
    }

    /**
     * Preparses the NMEA183Sentence specified by <code>str</code>, estimates
     * the appropriate parsing method and invokes this parsing method with the
     * preparsed string.
     * 
     * @param str the string representation of the NMEA183Sentence to parse
     * @throws IOException if an input or an output exception occured
     * @throws NMEA183SentenceFormatException if <code>str</code> does not have
     *   the appropriate format
     * @see #processSentence
     * @see #processGGASentence
     * @see #processGLLSentence
     * @see #processGSASentence
     * @see #processGSVSentence
     * @see #processRMCSentence
     * @see #processVTGSentence
     */
    private void parseSentence(String str) throws IOException, NMEA183SentenceFormatException {
        NMEA183Tokenizer tokenizer = new NMEA183Tokenizer(str);
        String sentenceId;
        String talkerId;
        String buffer;
        buffer = tokenizer.nextToken();
        try {
            talkerId = buffer.substring(0, 2);
            sentenceId = buffer.substring(2, 5);
        } catch (IndexOutOfBoundsException e) {
            throw new NMEA183SentenceFormatException();
        }
        if (sentenceId.equals("GGA")) {
            processGGASentence(talkerId, tokenizer);
        } else if (sentenceId.equals("GLL")) {
            processGLLSentence(talkerId, tokenizer);
        } else if (sentenceId.equals("GSA")) {
            processGSASentence(talkerId, tokenizer);
        } else if (sentenceId.equals("GSV")) {
            processGSVSentence(talkerId, tokenizer);
        } else if (sentenceId.equals("RMC")) {
            processRMCSentence(talkerId, tokenizer);
        } else if (sentenceId.equals("VTG")) {
            processVTGSentence(talkerId, tokenizer);
        } else {
            processSentence(talkerId, sentenceId, tokenizer);
        }
    }

    /**
     * Reads and parses the preparsed generic NMEA183 sentence from the
     * specified <code>NMEA183Tokenizer</code> and notifies the registered
     * <code>EventReflector</code>.
     * 
     * @param talkerId the talker id of the preparsed NMEA183 sentence
     * @param sentenceId the sentence id of the preparsed NMEA183 sentence
     * @param tokenizer the <code>NMEA183Tokenizer</code> to read from
     */
    private void processSentence(String talkerId, String sentenceId, NMEA183Tokenizer tokenizer) {
        NMEA183Sentence sentence = new NMEA183Sentence(talkerId, sentenceId);
        sentence.parse(tokenizer);
        eventReflector_.notifyListeners(new GPSParserEvent("NMEA183", sentence));
    }

    /**
     * Reads and parses the preparsed NMEA183 GGA sentence from the
     * specified <code>NMEA183Tokenizer</code> and notifies the registered
     * <code>EventReflector</code>.
     * 
     * @param talkerId the talker id of the preparsed NMEA183 sentence
     * @param tokenizer the <code>NMEA183Tokenizer</code> to read from
     * @throws NMEA183SentenceFormatException if the specified sentence does
     *   not have the appropriate format
     */
    private void processGGASentence(String talkerId, NMEA183Tokenizer tokenizer) throws NMEA183SentenceFormatException {
        NMEA183GGASentence sentence = new NMEA183GGASentence(talkerId);
        sentence.parse(tokenizer);
        eventReflector_.notifyListeners(new GPSParserEvent("NMEA183-GGA", sentence));
    }

    /**
     * Reads and parses the preparsed NMEA183 GLL sentence from the
     * specified <code>NMEA183Tokenizer</code> and notifies the registered
     * <code>EventReflector</code>.
     * 
     * @param talkerId the talker id of the preparsed NMEA183 sentence
     * @param tokenizer the <code>NMEA183Tokenizer</code> to read from
     * @throws NMEA183SentenceFormatException if the specified sentence does
     *   not have the appropriate format
     */
    private void processGLLSentence(String talkerId, NMEA183Tokenizer tokenizer) throws NMEA183SentenceFormatException {
        NMEA183GLLSentence sentence = new NMEA183GLLSentence(talkerId);
        sentence.parse(tokenizer);
        eventReflector_.notifyListeners(new GPSParserEvent("NMEA183-GLL", sentence));
    }

    /**
     * Reads and parses the preparsed NMEA183 GSA sentence from the
     * specified <code>NMEA183Tokenizer</code> and notifies the registered
     * <code>EventReflector</code>.
     * 
     * @param talkerId the talker id of the preparsed NMEA183 sentence
     * @param tokenizer the <code>NMEA183Tokenizer</code> to read from
     * @throws NMEA183SentenceFormatException if the specified sentence does
     *   not have the appropriate format
     */
    private void processGSASentence(String talkerId, NMEA183Tokenizer tokenizer) throws NMEA183SentenceFormatException {
        NMEA183GSASentence sentence = new NMEA183GSASentence(talkerId);
        sentence.parse(tokenizer);
        eventReflector_.notifyListeners(new GPSParserEvent("NMEA183-GSA", sentence));
    }

    /**
     * Reads and parses the preparsed NMEA183 GSV sentence from the
     * specified <code>NMEA183Tokenizer</code> and notifies the registered
     * <code>EventReflector</code>.
     * 
     * @param talkerId the talker id of the preparsed NMEA183 sentence
     * @param tokenizer the <code>NMEA183Tokenizer</code> to read from
     * @throws NMEA183SentenceFormatException if the specified sentence does
     *   not have the appropriate format
     */
    private void processGSVSentence(String talkerId, NMEA183Tokenizer tokenizer) throws NMEA183SentenceFormatException {
        NMEA183GSVSentence sentence = new NMEA183GSVSentence(talkerId);
        sentence.parse(tokenizer);
        eventReflector_.notifyListeners(new GPSParserEvent("NMEA183-GSV", sentence));
    }

    /**
     * Reads and parses the preparsed NMEA183 RMC sentence from the
     * specified <code>NMEA183Tokenizer</code> and notifies the registered
     * <code>EventReflector</code>.
     * 
     * @param talkerId the talker id of the preparsed NMEA183 sentence
     * @param tokenizer the <code>NMEA183Tokenizer</code> to read from
     * @throws NMEA183SentenceFormatException if the specified sentence does
     *   not have the appropriate format
     */
    private void processRMCSentence(String talkerId, NMEA183Tokenizer tokenizer) throws NMEA183SentenceFormatException {
        NMEA183RMCSentence sentence = new NMEA183RMCSentence(talkerId);
        sentence.parse(tokenizer);
        eventReflector_.notifyListeners(new GPSParserEvent("NMEA183-RMC", sentence));
    }

    /**
     * Reads and parses the preparsed NMEA183 VTG sentence from the
     * specified <code>NMEA183Tokenizer</code> and notifies the registered
     * <code>EventReflector</code>.
     * 
     * @param talkerId the talker id of the preparsed NMEA183 sentence
     * @param tokenizer the <code>NMEA183Tokenizer</code> to read from
     * @throws NMEA183SentenceFormatException if the specified sentence does
     *   not have the appropriate format
     */
    private void processVTGSentence(String talkerId, NMEA183Tokenizer tokenizer) throws NMEA183SentenceFormatException {
        NMEA183VTGSentence sentence = new NMEA183VTGSentence(talkerId);
        sentence.parse(tokenizer);
        eventReflector_.notifyListeners(new GPSParserEvent("NMEA183-VTG", sentence));
    }
}
