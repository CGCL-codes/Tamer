package com.streamsicle.fluid;

import com.streamsicle.IPlayHistory;
import java.util.*;
import java.net.*;
import java.io.*;
import org.apache.log4j.Category;

/**
 * This will supposedly connect to another streamsicle server's stream and offer
 * it as it's local stream, should save bandwidth as you could setup another
 * server locally on your LAN and have lots of listeners there while the main
 * external server only sends one stream worth of bandwidth out.
 * @author Matt Hall
 */
public class RedirectorStream extends MediaInputStream {

    private Delay delay;

    private boolean done;

    private MP3FrameReader mp3input;

    private byte[][] framebuf;

    private Properties props;

    private IPlayHistory playHistory;

    private URL externalServer;

    static Category log = Category.getInstance(RedirectorStream.class);

    /**
     * Creates an RedirectorStream that is yet to be
     * connected to a stream.
     */
    public RedirectorStream(Properties properties) {
        super();
        props = properties;
    }

    /**
     * Configures the MP3 input stream.
     * <P>
     * The properties that are read from the configuration
     * are:
     * <P>
     * <UL>
     * <LI> <B> external.stream.url </B><BR>
     *      The size of the frame buffer, ie how
     *      many frames will be stuffed into a
     *      data packet before being sent
     * </UL>
     *
     * @param conf A configuration from which
     *             parameters can be read
     */
    public void configure() {
        try {
            log.debug("Configuring RedirectorStream...");
            externalServer = new URL(props.getProperty("external.stream.url"));
            log.debug("Set URL for external stream to " + externalServer);
            setInputStream((externalServer.openConnection()).getInputStream());
            log.debug("Avail: " + getInputStream().available());
            log.debug("Connection opened.");
            int bufsize = Integer.parseInt(props.getProperty("mp3inputstream.buffersize"));
            log.debug("Buffersize set to " + bufsize);
            framebuf = new byte[bufsize][];
            done = false;
            start();
        } catch (Exception e) {
            log.fatal("The RedirectorInputStream could not be configured", e);
        }
    }

    /**
     * The delay that the MediaInputStream will have to
     * wait before fetching the next packet.
     *
     * @return Between-packet delay or null if the end
     *         of the file has been reached or if there
     *         is no input stream specified
     */
    public Delay getDelay() {
        if (done || (getInputStream() == null)) return null;
        return delay;
    }

    /**
     * This method will read a number of frames
     * using the MP3FrameReader and store these
     * frames in a frame buffer.
     *
     * @return The contents of the frame buffer,
     *         ie a packet, or null when the
     *         end of the file has been reached
     */
    public byte[] read() throws IOException {
        log.debug("Read called.");
        InputStream input = getInputStream();
        if (done) return null;
        delay = new Delay(0);
        byte[] frame = null;
        for (int i = 0; i < framebuf.length; i++) {
            try {
                byte[] frameread = new byte[20000];
                int bytesread = input.read(frameread);
                frame = new byte[bytesread];
                for (int j = 0; j < frame.length; j++) frame[j] = frameread[j];
                log.debug("Read " + bytesread + " bytes.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (frame == null) {
                done = true;
                getInputStream().close();
                return Bitmask.assemble(framebuf, i);
            }
            framebuf[i] = frame;
            if (delay != null) {
                delay = delay.add(1);
            } else {
                log.info("delay was null");
            }
        }
        return Bitmask.assemble(framebuf, framebuf.length);
    }

    /**
     * To check what kind of media this stream wrapper will
     * handle.
     *
     * @param type A media type (extension)
     * @return     true for "mp3" or "mpeg3"
     */
    public boolean handlesMedia(String type) {
        return type.toLowerCase().equals("mp3") || type.toLowerCase().equals("mpeg3");
    }

    /**
    * Sets the play history object that play history info will be sent to.
    */
    public void setPlayHistory(IPlayHistory playHistory) {
        this.playHistory = playHistory;
    }
}
