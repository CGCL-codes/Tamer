package com.jmex.sound.openAL.objects.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import com.jcraft.jorbis.VorbisFile;
import com.jmex.sound.openAL.objects.util.dsp.BandpassFilter;
import com.jmex.sound.openAL.objects.util.dsp.Equalizer;

/**
 * @author Arman
 */
public class StreamPlayer {

    private static StreamPlayer instance;

    private Player[] player;

    private Equalizer equalizer;

    private BandpassFilter filter;

    static {
        instance = new StreamPlayer();
    }

    private StreamPlayer() {
    }

    public static StreamPlayer getInstance() {
        return instance;
    }

    public void setEqualizer(Equalizer e) {
        if (this.equalizer == null) {
            this.equalizer = e;
        }
    }

    /**
     * Tries to open an ogg or wav file. If the opening operation Is successful
     * a unique id is returned to identify this stream;
     * @param file the audio file To open
     * @return a unique stream id. -1 if the player fails to open the file.
     */
    public int openStream(String file) {
        JMEAudioInputStream tmp = open(file, true);
        if (tmp == null) return -1;
        int streamNumber = add(tmp);
        if (equalizer != null) {
            filter = new BandpassFilter(equalizer.getFrequencies());
            filter.init(tmp.rate());
            equalizer.addFilter(streamNumber, filter);
            tmp.addFilter(filter);
        }
        tmp.setFileName(file);
        return streamNumber;
    }

    public int openStream(URL file) {
        JMEAudioInputStream tmp = null;
        tmp = open(file, true);
        if (tmp == null) return -1;
        int streamNumber = add(tmp);
        if (equalizer != null) {
            filter = new BandpassFilter(equalizer.getFrequencies());
            filter.init(tmp.rate());
            equalizer.addFilter(streamNumber, filter);
            tmp.addFilter(filter);
        }
        return streamNumber;
    }

    private JMEAudioInputStream open(URL file, boolean calcLength) {
        JMEAudioInputStream tmp = null;
        try {
            tmp = reopenOgg(file, calcLength);
        } catch (IOException ioe) {
            if (ioe.getMessage().equals(JMEAudioInputStream.INVALID_OGG_MESSAGE)) {
                try {
                    tmp = reopenWav(file, calcLength);
                } catch (IOException exception) {
                    exception.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
        tmp.setURL(file);
        return tmp;
    }

    private JMEAudioInputStream open(String file, boolean calcLength) {
        JMEAudioInputStream tmp = null;
        try {
            tmp = reopenOgg(file, calcLength);
        } catch (IOException ioe) {
            if (ioe.getMessage().equals(JMEAudioInputStream.INVALID_OGG_MESSAGE)) {
                try {
                    tmp = reopenWav(file, calcLength);
                } catch (IOException exception) {
                    exception.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
        if (tmp != null) {
            tmp.setFileName(file);
        }
        return tmp;
    }

    private JMEAudioInputStream reopenOgg(URL file, boolean calculateLength) throws IOException {
        JMEAudioInputStream tmp = null;
        if (calculateLength) {
            float length = 0;
            InputStream in = null;
            try {
                in = file.openStream();
                VorbisFile vf = new VorbisFile(in, new byte[4096], 4096);
                length = vf.time_total(-1) * 1000;
            } catch (Exception e) {
                in.close();
                throw new IOException(JMEAudioInputStream.INVALID_OGG_MESSAGE);
            }
            tmp = new OggInputStream(file.openStream());
            tmp.setLength(length);
        } else {
            tmp = new OggInputStream(file.openStream());
        }
        return tmp;
    }

    private JMEAudioInputStream reopenOgg(String file, boolean calculateLength) throws IOException {
        JMEAudioInputStream tmp = null;
        if (calculateLength) {
            float length = 0;
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                VorbisFile vf = new VorbisFile(in, new byte[4096], 4096);
                length = vf.time_total(-1) * 1000;
            } catch (Exception e) {
                in.close();
                throw new IOException(JMEAudioInputStream.INVALID_OGG_MESSAGE);
            }
            tmp = new OggInputStream(new FileInputStream(file));
            tmp.setLength(length);
        } else {
            tmp = new OggInputStream(new FileInputStream(file));
        }
        return tmp;
    }

    private JMEAudioInputStream reopenWav(URL file, boolean calculateLength) throws IOException {
        JMEAudioInputStream tmp = null;
        if (calculateLength) {
            tmp = new WavInputStream(new BufferedInputStream(file.openStream()));
            float length = getLength(tmp) * 1000;
            tmp.close();
            tmp = new WavInputStream(new BufferedInputStream(file.openStream()));
            tmp.setLength(length);
        } else {
            tmp = new WavInputStream(new BufferedInputStream(file.openStream()));
        }
        return tmp;
    }

    private JMEAudioInputStream reopenWav(String file, boolean calculateLength) throws IOException {
        JMEAudioInputStream tmp = null;
        if (calculateLength) {
            tmp = new WavInputStream(new BufferedInputStream(new FileInputStream(file)));
            float length = getLength(tmp) * 1000;
            tmp.close();
            tmp = new WavInputStream(new BufferedInputStream(new FileInputStream(file)));
            tmp.setLength(length);
        } else {
            tmp = new WavInputStream(new BufferedInputStream(new FileInputStream(file)));
        }
        return tmp;
    }

    /**
     * @param tmp
     */
    private float getLength(JMEAudioInputStream tmpStream) throws IOException {
        byte copyBuffer[] = new byte[1024 * 4];
        boolean done = false;
        int bytesRead = 0;
        int length = 0;
        while (!done) {
            bytesRead = tmpStream.read(copyBuffer, 0, copyBuffer.length);
            if (bytesRead != -1) length += bytesRead;
            done = (bytesRead != copyBuffer.length || bytesRead < 0);
        }
        int channels = tmpStream.getChannels();
        return (length) / (float) (tmpStream.rate() * tmpStream.getAudioChannels() * 2);
    }

    /**
     * Closes the stream with the given number
     * @param streamNumber the stream to close
     */
    public void closeStream(int streamNumber) {
        if (player == null) return;
        if (streamNumber < 0 || streamNumber >= player.length) return;
        player[streamNumber].close();
    }

    /**
     * Closes the stream with the given number
     * @param streamNumber the stream to close
     */
    public void loopStream(int streamNumber, boolean loop) {
        if (player == null) return;
        if (streamNumber < 0 || streamNumber >= player.length) return;
        player[streamNumber].setLooping(loop);
    }

    public void stopStream(int streamNumber) {
        if (player == null) return;
        if (streamNumber < 0 || streamNumber >= player.length) return;
        player[streamNumber].stop();
    }

    /**
     * Pauses the stream with the given number
     * @param streamNumber the stream to close
     */
    public boolean pauseStream(int streamNumber) {
        if (player == null) return false;
        if (streamNumber < 0 || streamNumber >= player.length) return false;
        return player[streamNumber].pause();
    }

    /**
     * Pauses the stream with the given number
     * @param streamNumber the stream to close
     */
    public boolean isPlaying(int streamNumber) {
        if (player == null) return false;
        if (streamNumber < 0 || streamNumber >= player.length) return false;
        return player[streamNumber].playing();
    }

    private int add(JMEAudioInputStream tmpStream) {
        if (player == null) {
            player = new Player[1];
            player[0] = new Player(tmpStream, generateSource());
            ;
            return 0;
        }
        Player[] tmp = new Player[player.length];
        System.arraycopy(player, 0, tmp, 0, tmp.length);
        player = new Player[tmp.length + 1];
        System.arraycopy(tmp, 0, player, 0, tmp.length);
        player[tmp.length] = new Player(tmpStream, generateSource());
        return tmp.length;
    }

    public void play(int streamNumber) {
        if (player == null) return;
        if (streamNumber < 0 || streamNumber >= player.length) return;
        if (player[streamNumber].playing()) return;
        new Thread(player[streamNumber]).start();
    }

    public float length(int streamNumber) {
        if (player == null) return 0;
        if (streamNumber < 0 || streamNumber >= player.length) return 0;
        return player[streamNumber].getStreamLength();
    }

    public void setVolume(int streamNumber, float volume) {
        if (player == null) return;
        if (streamNumber < 0 || streamNumber >= player.length) return;
        player[streamNumber].setVolume(volume);
    }

    /**
     * The thread that updates the sound.
     */
    private class Player implements Runnable {

        private int source;

        private JMEAudioInputStream stream;

        private ByteBuffer dataBuffer = ByteBuffer.allocateDirect(4096 * 20);

        private IntBuffer buffers = BufferUtils.createIntBuffer(4);

        private boolean paused;

        private boolean stopped;

        private boolean finished;

        private boolean looping;

        /** Creates the PlayerThread */
        Player(JMEAudioInputStream current, int sourceNumber) {
            this.source = sourceNumber;
            stream = current;
            AL10.alGenBuffers(buffers);
        }

        public void setVolume(float volume) {
            AL10.alSourcef(source, AL10.AL_GAIN, volume);
        }

        public float getStreamLength() {
            return stream.getLength();
        }

        /**
         * Copies data from the ogg stream to openal. Must be called often.
         * @return true if sound is still playing, false if the end of file is reached.
         */
        public synchronized boolean update() throws IOException {
            if (paused) return true;
            boolean active = true;
            int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
            while (processed-- > 0) {
                IntBuffer buffer = BufferUtils.createIntBuffer(1);
                AL10.alSourceUnqueueBuffers(source, buffer);
                active = stream(buffer.get(0));
                buffer.rewind();
                AL10.alSourceQueueBuffers(source, buffer);
            }
            return active;
        }

        /**
         * reloads a buffer
         * @return true if success, false if read failed or end of file.
         */
        protected boolean stream(int buffer) {
            try {
                int bytesRead = stream.read(dataBuffer, 0, dataBuffer.capacity());
                if (bytesRead >= 0) {
                    dataBuffer.rewind();
                    int format = stream.getChannels();
                    AL10.alBufferData(buffer, format, dataBuffer, stream.rate());
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Plays the Ogg stream. update() must be called regularly so that the data
         * is copied to OpenAl
         */
        public boolean play() {
            AL10.alSource3f(source, AL10.AL_POSITION, 0, 0, 0);
            AL10.alSource3f(source, AL10.AL_VELOCITY, 0, 0, 0);
            AL10.alSource3f(source, AL10.AL_DIRECTION, 0, 0, 0);
            AL10.alSourcef(source, AL10.AL_ROLLOFF_FACTOR, 0);
            AL10.alSourcei(source, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);
            if (playing()) {
                return true;
            }
            for (int i = 0; i < buffers.capacity(); i++) {
                if (!stream(buffers.get(i))) {
                    return false;
                }
            }
            AL10.alSourceQueueBuffers(source, buffers);
            AL10.alSourcePlay(source);
            finished = false;
            return true;
        }

        public void close() {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean pause() {
            paused = !paused;
            return paused;
        }

        public void stop() {
            stopped = true;
        }

        /**
         * check if the source is playing
         */
        public boolean playing() {
            return (AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING);
        }

        /** Calls update at an interval */
        public void run() {
            if (finished) {
                float length = stream.getLength();
                String file = stream.getFileName();
                if (file != null) {
                    stream = open(file, false);
                } else {
                    stream = open(stream.getURL(), false);
                }
                if (stream == null) return;
            }
            play();
            try {
                while (update() && !stopped) {
                    Thread.sleep(2);
                }
            } catch (Exception e) {
                if (!(e instanceof InterruptedException)) e.printStackTrace();
            }
            finished = true;
            try {
                stream.close();
            } catch (IOException e1) {
            }
            if (looping && !stopped) run();
        }

        public boolean isLooping() {
            return looping;
        }

        public void setLooping(boolean looping) {
            this.looping = looping;
        }
    }

    private int generateSource() {
        IntBuffer alSources = BufferUtils.createIntBuffer(1);
        AL10.alGenSources(alSources);
        return alSources.get(0);
    }
}
