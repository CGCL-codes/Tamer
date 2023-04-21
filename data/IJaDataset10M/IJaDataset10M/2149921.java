package org.mobicents.media.server.ivr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.mobicents.media.Component;
import org.mobicents.media.MediaSink;
import org.mobicents.media.MediaSource;
import org.mobicents.media.server.component.Splitter;
import org.mobicents.media.server.impl.AbstractSink;
import org.mobicents.media.server.impl.AbstractSource;
import org.mobicents.media.server.impl.PipeImpl;
import org.mobicents.media.server.impl.resource.dtmf.DetectorImpl;
import org.mobicents.media.server.scheduler.Scheduler;
import org.mobicents.media.server.spi.dtmf.DtmfDetector;
import org.mobicents.media.server.spi.format.AudioFormat;
import org.mobicents.media.server.spi.format.FormatFactory;
import org.mobicents.media.server.spi.format.Formats;
import org.mobicents.media.server.spi.memory.Frame;

/**
 *
 * @author kulikov
 */
public class AudioSink extends AbstractSink {

    private static final AudioFormat LINEAR = FormatFactory.createAudioFormat("linear", 8000, 16, 1);

    private static final Formats formats = new Formats();

    private ConcurrentLinkedQueue<Frame> buffer = new ConcurrentLinkedQueue();

    private SignalSplitter signalSplitter;

    private ArrayList<Component> components = new ArrayList();

    private DetectorImpl dtmfDetector;

    private PipeImpl dtmfPipe;

    static {
        formats.add(LINEAR);
    }

    /**
     * Creates new audio announcement source.
     * 
     * @param scheduler the scheduler instance.
     */
    public AudioSink(Scheduler scheduler, String name) {
        super("aap", scheduler);
        signalSplitter = new SignalSplitter(scheduler);
        signalSplitter.init();
        dtmfDetector = new DetectorImpl(name, scheduler);
        dtmfDetector.setVolume(-35);
        dtmfDetector.setDuration(40);
        dtmfPipe = new PipeImpl();
        MediaSource dtmfSource = signalSplitter.audioSplitter.newOutput();
        dtmfPipe.connect(dtmfSource);
        dtmfPipe.connect(dtmfDetector);
    }

    @Override
    public void start() {
        signalSplitter.start();
        dtmfPipe.start();
        super.start();
    }

    @Override
    public void stop() {
        dtmfPipe.stop();
        signalSplitter.stop();
        super.stop();
    }

    public void add(MediaSink detector) {
        signalSplitter.add(detector);
    }

    @Override
    public Formats getNativeFormats() {
        return formats;
    }

    @Override
    public void onMediaTransfer(Frame frame) throws IOException {
        buffer.offer(frame);
        signalSplitter.wakeup();
    }

    private class SignalSplitter extends AbstractSource {

        private Splitter audioSplitter;

        private PipeImpl pipe1;

        private Scheduler scheduler;

        public SignalSplitter(Scheduler scheduler) {
            super("signal-mixer", scheduler);
            this.scheduler = scheduler;
        }

        private void init() {
            pipe1 = new PipeImpl();
            audioSplitter = new Splitter(scheduler);
            pipe1.connect(audioSplitter.getInput());
            pipe1.connect(this);
        }

        @Override
        public void start() {
            audioSplitter.getInput().start();
            super.start();
        }

        @Override
        public void stop() {
            audioSplitter.getInput().stop();
            super.stop();
        }

        @Override
        public Formats getNativeFormats() {
            return formats;
        }

        public void add(MediaSink detector) {
            MediaSource source = audioSplitter.newOutput();
            PipeImpl p = new PipeImpl();
            p.connect(source);
            p.connect(detector);
            source.start();
            components.add(detector);
        }

        @Override
        public Frame evolve(long timestamp) {
            return buffer.poll();
        }
    }

    public Component getComponent(Class intf) {
        if (intf.equals(DtmfDetector.class)) {
            return dtmfDetector;
        }
        for (int i = 0; i < components.size(); i++) {
            if ((components.get(i).getClass() == intf) || (components.get(i).getInterface(intf) != null)) {
                return components.get(i);
            }
        }
        return null;
    }
}
