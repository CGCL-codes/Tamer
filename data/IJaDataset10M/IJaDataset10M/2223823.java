package edu.mit.csail.sls.wami.applet.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import org.spantus.logger.Logger;

/**
 * Controls playing of audio
 * 
 * Unless otherwise noted, methods are not thread-safe.
 * 
 */
class AudioPlayer implements Player {

    private static Logger LOG = Logger.getLogger(AudioPlayer.class);

    protected volatile SourceDataLine line = null;

    protected volatile boolean enabled = true;

    protected volatile boolean playing = false;

    protected int position = 0;

    protected volatile int startPosition = 0;

    protected byte[] data = new byte[1024];

    Converter currentConverter = null;

    protected Mixer.Info preferredMixerInfo = null;

    protected class Converter {

        AudioFormat currentAudioFormat;

        AudioFormat convertFormat;

        AudioFormat lineFormat;

        Converter(AudioFormat currentAudioFormat, AudioFormat convertFormat, AudioFormat lineFormat) {
            this.currentAudioFormat = currentAudioFormat;
            this.convertFormat = convertFormat;
            this.lineFormat = lineFormat;
        }

        boolean matches(AudioFormat desiredAudioFormat) {
            return currentAudioFormat.matches(desiredAudioFormat);
        }

        int doSamples(AudioInputStream ais) throws IOException {
            return ais.read(data);
        }
    }

    protected class Converter1 extends Converter {

        byte[] indata = new byte[data.length / 2];

        Converter1(AudioFormat currentAudioFormat, AudioFormat convertFormat, AudioFormat lineFormat) {
            super(currentAudioFormat, convertFormat, lineFormat);
        }

        @Override
        public int doSamples(AudioInputStream ais) throws IOException {
            int n = ais.read(indata);
            if (n < 0) return n;
            int j = 0;
            for (int i = 0; i < n; i++) {
                byte b = indata[i];
                data[j++] = b;
                data[j++] = b;
            }
            return n * 2;
        }
    }

    protected class Converter2 extends Converter {

        byte[] indata = new byte[data.length / 2];

        Converter2(AudioFormat currentAudioFormat, AudioFormat convertFormat, AudioFormat lineFormat) {
            super(currentAudioFormat, convertFormat, lineFormat);
        }

        @Override
        public int doSamples(AudioInputStream ais) throws IOException {
            int n = ais.read(indata);
            if (n < 0) return n;
            int j = 0;
            for (int i = 0; i < n; ) {
                byte b0 = indata[i++];
                byte b1 = indata[i++];
                data[j++] = b0;
                data[j++] = b1;
                data[j++] = b0;
                data[j++] = b1;
            }
            return n * 2;
        }
    }

    LinkedList<Listener> listeners = new LinkedList<Listener>();

    /**
	 * Add an event listener
	 * 
	 * @param l
	 *            The listener
	 * 
	 */
    public void addListener(Listener l) {
        listeners.add(l);
    }

    /**
	 * Remove an event listener
	 * 
	 * @param l
	 *            The listener
	 * 
	 */
    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    void playingHasStarted() {
        LOG.debug("[playingHasStarted]");
        for (Listener listener : listeners) {
            listener.playingHasStarted();
        }
    }

    void playingHasEnded() {
        LOG.debug("[playingHasEnded]");
        for (Listener listener : listeners) {
            listener.playingHasEnded();
        }
    }

    /**
	 * Find the best conversion for a desired audio format
	 * 
	 */
    Converter getConverter(AudioFormat desiredAudioFormat) throws LineUnavailableException {
        if (currentConverter != null && currentConverter.matches(desiredAudioFormat)) return currentConverter;
        AudioFormat minFormat = null;
        AudioFormatComparator comp = new AudioFormatComparator(desiredAudioFormat) {

            @Override
            public int conversionCompare(AudioFormat f1, AudioFormat f2) {
                boolean c1 = AudioSystem.isConversionSupported(f1, desiredAudioFormat);
                boolean c2 = AudioSystem.isConversionSupported(f2, desiredAudioFormat);
                if (c1) {
                    if (!c2) {
                        return -1;
                    }
                } else if (!c2) {
                    return 1;
                }
                return 0;
            }
        };
        ArrayList<Mixer.Info> minfoList = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo()));
        if (preferredMixerInfo != null) {
            minfoList.remove(preferredMixerInfo);
            minfoList.add(0, preferredMixerInfo);
        }
        Mixer.Info[] minfo = minfoList.toArray(new Mixer.Info[minfoList.size()]);
        for (int i = 0; i < minfo.length; i++) {
            Mixer mixer = AudioSystem.getMixer(minfo[i]);
            Line.Info[] linfo = mixer.getSourceLineInfo();
            for (int j = 0; j < linfo.length; j++) {
                if (!(linfo[j] instanceof DataLine.Info)) {
                    continue;
                }
                DataLine.Info dinfo = (DataLine.Info) linfo[j];
                AudioFormat[] formats = dinfo.getFormats();
                for (int k = 0; k < formats.length; k++) {
                    AudioFormat f = formats[k];
                    if (comp.compare(f, minFormat) == -1) {
                        minFormat = f;
                    }
                }
            }
        }
        AudioFormat lineFormat = minFormat;
        if (lineFormat.getSampleRate() == AudioSystem.NOT_SPECIFIED) {
            lineFormat = new AudioFormat(lineFormat.getEncoding(), desiredAudioFormat.getSampleRate(), lineFormat.getSampleSizeInBits(), lineFormat.getChannels(), lineFormat.getFrameSize(), desiredAudioFormat.getFrameRate(), lineFormat.isBigEndian());
        }
        AudioFormat clf = AudioFormatComparator.channelFormat(lineFormat, desiredAudioFormat.getChannels());
        AudioFormat convertFormat = AudioSystem.isConversionSupported(clf, desiredAudioFormat) ? clf : desiredAudioFormat;
        if (lineFormat.getChannels() == 2 && desiredAudioFormat.getChannels() == 1) {
            switch(convertFormat.getFrameSize()) {
                case 1:
                    return new Converter1(desiredAudioFormat, convertFormat, lineFormat);
                case 2:
                    return new Converter2(desiredAudioFormat, convertFormat, lineFormat);
                default:
                    throw new LineUnavailableException("Cannot play " + desiredAudioFormat + " on audio device");
            }
        } else {
            return new Converter(desiredAudioFormat, convertFormat, lineFormat);
        }
    }

    /**
	 * Wait for playing to complete, and then close the line.
	 * 
	 */
    public synchronized void closeLine() {
        if (line == null) return;
        double padding = .3 * line.getFormat().getFrameRate() * line.getFormat().getFrameSize();
        int nbuffers = (int) ((padding + data.length - 1) / data.length);
        for (int i = 0; i < nbuffers; i++) {
            Arrays.fill(data, (byte) 0);
            line.write(data, 0, data.length);
        }
        line.drain();
        synchronized (this) {
            line.close();
            line = null;
        }
    }

    /**
	 * Play the stream. This is the same as play(stream, true, true).
	 * 
	 * @param stream
	 *            The stream to play
	 * 
	 */
    public void play(AudioInputStream stream) throws LineUnavailableException {
        play(stream, true, true);
    }

    /**
	 * Play the stream
	 * 
	 * @param stream
	 *            The stream to play
	 * 
	 * @param setStart
	 *            If true, getFramePosition() will consider the start of this
	 *            stream as frame 0, and playingHasStarted() will be called.
	 * 
	 * @param last
	 *            If true, playingHasEnded() will be called when playing stops.
	 * 
	 */
    public void play(AudioInputStream stream, boolean setStart, boolean last) throws LineUnavailableException {
        AudioFormat desiredAudioFormat = stream.getFormat();
        LOG.debug("[play]+++");
        if (desiredAudioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            stream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, stream);
            desiredAudioFormat = stream.getFormat();
        }
        currentConverter = getConverter(desiredAudioFormat);
        AudioInputStream ais = desiredAudioFormat.matches(currentConverter.convertFormat) ? stream : AudioSystem.getAudioInputStream(currentConverter.convertFormat, stream);
        if (ais.getFormat().getSampleRate() != currentConverter.lineFormat.getSampleRate()) {
            if (2 * ais.getFormat().getSampleRate() == currentConverter.lineFormat.getSampleRate()) {
                play(new UpSample2(ais), setStart, last);
                return;
            }
            LOG.debug("[play]Here we go...");
            throw new LineUnavailableException("Audio device does not support a sampling rate of " + ais.getFormat().getSampleRate());
        }
        if (line != null) {
            if (!line.getFormat().matches(currentConverter.lineFormat)) {
                closeLine();
            }
        }
        if (line == null) {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, currentConverter.lineFormat);
            synchronized (this) {
                try {
                    LOG.debug("[play] preferred mixer is: " + preferredMixerInfo);
                    if (preferredMixerInfo != null) {
                        Mixer mixer = AudioSystem.getMixer(preferredMixerInfo);
                        try {
                            line = (SourceDataLine) mixer.getLine(info);
                        } catch (IllegalArgumentException e) {
                            line = (SourceDataLine) AudioSystem.getLine(info);
                        }
                    } else {
                        line = (SourceDataLine) AudioSystem.getLine(info);
                    }
                    position = 0;
                    line.open(currentConverter.lineFormat);
                    line.start();
                } catch (LineUnavailableException e) {
                    line = null;
                    throw e;
                }
            }
        }
        if (setStart) {
            startPosition = position;
        }
        try {
            synchronized (this) {
                playing = true;
                notifyAll();
            }
            if (setStart) {
                LOG.debug("[play]+++ setStart");
                playingHasStarted();
            }
            while (true) {
                synchronized (this) {
                    if (ais == null || line == null || !enabled) break;
                }
                int n = -1;
                try {
                    n = currentConverter.doSamples(ais);
                } catch (IOException e) {
                    break;
                }
                if (n < 0) {
                    try {
                        ais.close();
                    } catch (IOException e) {
                    }
                    synchronized (this) {
                        notifyAll();
                    }
                    break;
                } else {
                    position += n;
                    line.write(data, 0, n);
                }
            }
        } finally {
            synchronized (this) {
                playing = false;
                notifyAll();
            }
            if (last) {
                closeLine();
                playingHasEnded();
            }
        }
        LOG.debug("[play]---");
    }

    /**
	 * Returns true if in the play loop
	 * 
	 */
    public synchronized boolean isPlaying() {
        return playing;
    }

    /**
	 * Break out of playing
	 * 
	 * Can be called from any thread
	 */
    public synchronized void stopPlaying() {
        if (!playing) return;
        enabled = false;
        while (playing) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        enabled = true;
    }

    /**
	 * Return the frame position in the currently playing stream.
	 * 
	 * Can be called from any thread
	 * 
	 */
    public synchronized int getFramePosition() {
        return line == null ? 0 : line.getFramePosition() - startPosition;
    }

    /**
	 * Returns the closest supported sample rate
	 * 
	 */
    public int supportedSampleRate(int desiredSampleRate) throws LineUnavailableException {
        AudioFormat format = new AudioFormat((float) desiredSampleRate, 16, 2, true, true);
        Converter converter = getConverter(format);
        return (int) converter.lineFormat.getSampleRate();
    }

    /**
	 * Set the preferred mixer to use to playback (note, not thread safe at the
	 * moment)
	 */
    public void setPreferredMixer(Mixer.Info mInfo) {
        preferredMixerInfo = mInfo;
    }

    public void setPreferredMixer(String name) {
        for (Mixer.Info mi : AudioSystem.getMixerInfo()) {
            if (mi.getName().equals(name)) {
                setPreferredMixer(mi);
                return;
            }
        }
    }

    public Mixer.Info getPreferredMixer() {
        return preferredMixerInfo;
    }
}
