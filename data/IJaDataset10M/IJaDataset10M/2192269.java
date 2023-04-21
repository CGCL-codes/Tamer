package org.mobicents.media.server.mgcp.pkg.au;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.mobicents.media.server.mgcp.controller.signal.Event;
import org.mobicents.media.server.mgcp.controller.signal.NotifyImmediately;
import org.mobicents.media.server.mgcp.controller.signal.Signal;
import org.mobicents.media.server.spi.Connection;
import org.mobicents.media.server.spi.Endpoint;
import org.mobicents.media.server.spi.MediaType;
import org.mobicents.media.server.spi.dtmf.DtmfDetector;
import org.mobicents.media.server.spi.listener.TooManyListenersException;
import org.mobicents.media.server.spi.player.Player;
import org.mobicents.media.server.spi.player.PlayerEvent;
import org.mobicents.media.server.spi.player.PlayerListener;
import org.mobicents.media.server.spi.recorder.Recorder;
import org.mobicents.media.server.spi.recorder.RecorderEvent;
import org.mobicents.media.server.spi.recorder.RecorderListener;
import org.mobicents.media.server.utils.Text;

/**
 * Implements play announcement signal.
 *
 * Plays a prompt and collects DTMF digits entered by a user.  If no
 * digits are entered or an invalid digit pattern is entered, the
 * user may be reprompted and given another chance to enter a correct
 * pattern of digits.  The following digits are supported:  0-9, *,
 * #, A, B, C, D.  By default PlayCollect does not play an initial
 * prompt, makes only one attempt to collect digits, and therefore
 * functions as a simple Collect operation.  Various special purpose
 * keys, key sequences, and key sets can be defined for use during
 * the PlayCollect operation.
 *
 *
 * @author kulikov
 * @author Laurent Schweizer
 */
public class PlayRecord extends Signal {

    private Event oc = new Event(new Text("oc"));

    private Event of = new Event(new Text("of"));

    private volatile boolean isActive;

    private Player player;

    private Recorder recorder;

    private DtmfDetector dtmfDetector;

    private Options options;

    private EventBuffer buffer = new EventBuffer();

    private RecordingHandler recordingHandler;

    private DtmfHandler dtmfHandler;

    private PromptHandler promptHandler;

    private volatile boolean isPromptActive;

    private Iterator<Text> prompt;

    private int segCount;

    private static final Logger logger = Logger.getLogger(PlayRecord.class);

    public PlayRecord(String name) {
        super(name);
        oc.add(new NotifyImmediately("N"));
        of.add(new NotifyImmediately("N"));
        recordingHandler = new RecordingHandler(this);
        dtmfHandler = new DtmfHandler(this);
        promptHandler = new PromptHandler(this);
    }

    @Override
    public void execute() {
        segCount = 0;
        this.isActive = true;
        options = new Options(getTrigger().getParams());
        logger.info(String.format("(%s) Prepare digit collect phase", getEndpoint().getLocalName()));
        prepareCollectPhase(options);
        if (options.hasPrompt()) {
            logger.info(String.format("(%s) Start prompt phase", getEndpoint().getLocalName()));
            this.isPromptActive = true;
            startPromptPhase(options);
            return;
        }
        logger.info(String.format("(%s) Start collect phase", getEndpoint().getLocalName()));
        flushBuffer();
        startCollectPhase();
        logger.info(String.format("(%s) Start record phase", getEndpoint().getLocalName()));
        startRecordPhase(options);
    }

    @Override
    public boolean doAccept(Text event) {
        if (!oc.isActive() && oc.matches(event)) {
            return true;
        }
        if (!of.isActive() && of.matches(event)) {
            return true;
        }
        return false;
    }

    @Override
    public void cancel() {
        this.isActive = false;
        this.terminate();
    }

    /**
     * Starts the prompt phase.
     *
     * @param options requested options.
     */
    private void startPromptPhase(Options options) {
        player = this.getPlayer();
        try {
            player.addListener(promptHandler);
            prompt = options.getPrompt().iterator();
            player.setURL(prompt.next().toString());
            player.start();
        } catch (TooManyListenersException e) {
            of.fire(this, new Text("Too many listeners"));
        } catch (Exception e) {
            of.fire(this, new Text(e.getMessage()));
        }
    }

    /**
     * Terminates prompt phase if it was started or do nothing otherwise.
     */
    private void terminatePrompt() {
        if (prompt != null) {
            while (prompt.hasNext()) {
                prompt.next();
            }
        }
        if (player != null) {
            player.stop();
            player.removeListener(promptHandler);
        }
    }

    /**
     * Starts record phase.
     *
     * @param options requested options
     */
    private void startRecordPhase(Options options) {
        recorder = getRecorder();
        recorder.setMaxRecordTime(options.getRecordDuration());
        recorder.setPostSpeechTimer(options.getPostSpeechTimer());
        try {
            recorder.addListener(recordingHandler);
            recorder.setRecordFile(options.getRecordID().toString(), !options.isOverride());
            recorder.start();
        } catch (Exception e) {
            of.fire(this, new Text(e.getMessage()));
        }
    }

    /**
     * Terminates prompt phase if it was started or do nothing otherwise.
     */
    private void terminateRecordPhase() {
        if (recorder != null) {
            recorder.stop();
            recorder.removeListener(recordingHandler);
        }
    }

    /**
     * Prepares resources for DTMF collection phase.
     *
     * @param options
     */
    private void prepareCollectPhase(Options options) {
        dtmfDetector = this.getDetector();
        if (options.isClearDigits()) {
            dtmfDetector.clearDigits();
        }
        buffer.clear();
        buffer.setListener(dtmfHandler);
        buffer.setPatterns(options.getDigitPattern());
        buffer.setCount(options.getDigitsNumber());
    }

    /**
     * Terminates digit collect phase.
     */
    private void terminateCollectPhase() {
        if (dtmfDetector != null) {
            dtmfDetector.removeListener(buffer);
            buffer.passivate();
            buffer.clear();
        }
    }

    /**
     * Flushes DTMF buffer content to local buffer
     */
    private void flushBuffer() {
        try {
            dtmfDetector.addListener(buffer);
            dtmfDetector.flushBuffer();
        } catch (TooManyListenersException e) {
            of.fire(this, new Text("Too many listeners for DTMF detector"));
        }
    }

    private void startCollectPhase() {
        buffer.activate();
        buffer.flush();
    }

    /**
     * Terminates any activity.
     */
    private void terminate() {
        this.terminatePrompt();
        this.terminateRecordPhase();
        this.terminateCollectPhase();
    }

    private Player getPlayer() {
        if (getTrigger().getConnectionID() == null) {
            Endpoint endpoint = getEndpoint();
            return (Player) getEndpoint().getResource(MediaType.AUDIO, Player.class);
        }
        String connectionID = getTrigger().getConnectionID().toString();
        Connection connection = getConnection(connectionID);
        if (connection == null) {
            return null;
        }
        return null;
    }

    private Recorder getRecorder() {
        if (getTrigger().getConnectionID() == null) {
            Endpoint endpoint = getEndpoint();
            return (Recorder) getEndpoint().getResource(MediaType.AUDIO, Recorder.class);
        }
        String connectionID = getTrigger().getConnectionID().toString();
        Connection connection = getConnection(connectionID);
        if (connection == null) {
            return null;
        }
        return null;
    }

    private DtmfDetector getDetector() {
        if (getTrigger().getConnectionID() == null) {
            Endpoint endpoint = getEndpoint();
            return (DtmfDetector) getEndpoint().getResource(MediaType.AUDIO, DtmfDetector.class);
        }
        String connectionID = getTrigger().getConnectionID().toString();
        Connection connection = getConnection(connectionID);
        if (connection == null) {
            return null;
        }
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        this.terminate();
        oc.reset();
        of.reset();
    }

    private void next(long delay) {
        try {
            segCount++;
            player.setURL(prompt.next().toString());
            player.setInitialDelay(delay * 1000000L);
            player.start();
        } catch (Exception e) {
            of.fire(this, new Text(e.getMessage()));
        }
    }

    /**
     * Handler for prompt phase.
     */
    private class PromptHandler implements PlayerListener {

        private PlayRecord signal;

        /**
         * Creates new handler instance.
         *
         * @param signal the play record signal instance
         */
        protected PromptHandler(PlayRecord signal) {
            this.signal = signal;
        }

        public void process(PlayerEvent event) {
            switch(event.getID()) {
                case PlayerEvent.START:
                    if (segCount == 0) {
                        flushBuffer();
                    }
                    break;
                case PlayerEvent.STOP:
                    if (prompt.hasNext()) {
                        next(options.getInterval());
                        return;
                    }
                    if (isPromptActive) {
                        isPromptActive = false;
                        logger.info(String.format("(%s) Prompt phase terminated, start collect/record phase", getEndpoint().getLocalName()));
                        startCollectPhase();
                        logger.info(String.format("(%s) Start record phase", getEndpoint().getLocalName()));
                        startRecordPhase(options);
                    }
                    break;
                case PlayerEvent.FAILED:
                    of.fire(signal, null);
                    complete();
                    break;
            }
        }
    }

    /**
     * Handler for recorder events
     */
    private class RecordingHandler implements RecorderListener {

        private PlayRecord signal;

        protected RecordingHandler(PlayRecord signal) {
            this.signal = signal;
        }

        public void process(RecorderEvent event) {
            switch(event.getID()) {
                case RecorderEvent.STOP:
                    switch(event.getQualifier()) {
                        case RecorderEvent.MAX_DURATION_EXCEEDED:
                            oc.fire(signal, new Text("rc=328"));
                            break;
                        case RecorderEvent.NO_SPEECH:
                            oc.fire(signal, new Text("rc=327"));
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * Handler for digit collect phase.
     *
     */
    private class DtmfHandler implements BufferListener {

        private PlayRecord signal;

        /**
         * Constructor for this handler.
         * @param signal
         */
        public DtmfHandler(PlayRecord signal) {
            this.signal = signal;
        }

        /**
         * (Non Java-doc.)
         *
         * @see BufferListener#patternMatches(int, java.lang.String)
         */
        public void patternMatches(int index, String s) {
            oc.fire(signal, new Text("rc=100 dc=" + s + " pi=" + index));
            reset();
            complete();
        }

        /**
         * (Non Java-doc.)
         *
         * @see BufferListener#countMatches(java.lang.String)
         */
        public void countMatches(String s) {
            oc.fire(signal, new Text("rc=100 dc=" + s));
            reset();
            complete();
        }

        /**
         * (Non Java-doc.)
         *
         * @see BufferListener#tone(java.lang.String)
         */
        public void tone(String s) {
            logger.info(String.format("(%s) Tone '%s' has been detected", getEndpoint().getLocalName(), s));
            if (!options.isNonInterruptable()) {
                if (isPromptActive) {
                    logger.info(String.format("(%s) Tone '%s' has been detected: prompt phase interrupted", getEndpoint().getLocalName(), s));
                    terminatePrompt();
                } else {
                    logger.info(String.format("(%s) Tone '%s' has been detected: collected", getEndpoint().getLocalName(), s));
                }
            } else {
                if (isPromptActive) {
                    logger.info(String.format("(%s) Tone '%s' has been detected, waiting for prompt phase termination", getEndpoint().getLocalName(), s));
                } else {
                    logger.info(String.format("(%s) Tone '%s' has been detected: collected", getEndpoint().getLocalName(), s));
                }
            }
        }
    }
}
