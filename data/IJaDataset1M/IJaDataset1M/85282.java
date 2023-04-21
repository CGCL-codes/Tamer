package com.xuggle.xuggler.mediatool;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xuggle.xuggler.mediatool.MediaReader;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IError;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import java.awt.image.BufferedImage;
import static junit.framework.Assert.*;

public class MediaReaderTest {

    public static final int TEST_FILE_20_SECONDS_VIDEO_FRAME_COUNT = 300;

    public static final int TEST_FILE_20_SECONDS_AUDIO_FRAME_COUNT = 762;

    public static final String TEST_FILE_20_SECONDS = "fixtures/testfile_videoonly_20sec.flv";

    final boolean SHOW_VIDEO = System.getProperty(MediaReaderTest.class.getName() + ".ShowVideo") != null;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before
    public void setUp() {
        log.trace("setUp");
    }

    @Test(expected = RuntimeException.class)
    public void testMediaSourceNotExist() {
        MediaReader mr = new MediaReader("broken" + TEST_FILE_20_SECONDS, true, ConverterFactory.XUGGLER_BGR_24);
        mr.readPacket();
    }

    @Test
    public void testMediaReaderOpenReadClose() {
        final int[] counts = new int[2];
        MediaReader mr = new MediaReader(TEST_FILE_20_SECONDS, false, null);
        if (SHOW_VIDEO) mr.addListener(new MediaViewer(true));
        IMediaListener mrl = new MediaAdapter() {

            public void onVideoPicture(IMediaTool tool, IVideoPicture picture, BufferedImage image, int streamIndex) {
                assertNotNull("picture should be created", picture);
                assertNull("no buffered image should be created", image);
                ++counts[0];
            }

            public void onAudioSamples(IMediaTool tool, IAudioSamples samples, int streamIndex) {
                assertNotNull("audio samples should be created", samples);
                ++counts[1];
            }
        };
        mr.addListener(mrl);
        IError err = null;
        while ((err = mr.readPacket()) == null) ;
        assertEquals("Loop should complete with an EOF", IError.Type.ERROR_EOF, err.getType());
        assertEquals("incorrect number of video frames:", counts[0], TEST_FILE_20_SECONDS_VIDEO_FRAME_COUNT);
        assertEquals("incorrect number of audio frames:", counts[1], TEST_FILE_20_SECONDS_AUDIO_FRAME_COUNT);
    }

    @Test
    public void testCreateBufferedImages() {
        if (!IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION)) return;
        MediaReader mr = new MediaReader(TEST_FILE_20_SECONDS, true, ConverterFactory.XUGGLER_BGR_24);
        if (SHOW_VIDEO) mr.addListener(new MediaViewer(true));
        IMediaListener mrl = new MediaAdapter() {

            public void onVideoPicture(IMediaTool tool, IVideoPicture picture, BufferedImage image, int streamIndex) {
                assertNotNull("picture should be created", picture);
                assertNotNull("buffered image should be created", image);
            }

            public void onAudioSamples(IMediaTool tool, IAudioSamples samples, int streamIndex) {
                assertNotNull("audio samples should be created", samples);
            }
        };
        mr.addListener(mrl);
        while (mr.readPacket() == null) ;
    }

    @Test
    public void testOpenWithContainer() {
        if (!IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION)) return;
        final int[] counts = new int[2];
        IContainer container = IContainer.make();
        if (container.open(TEST_FILE_20_SECONDS, IContainer.Type.READ, null, true, false) < 0) throw new IllegalArgumentException("could not open: " + TEST_FILE_20_SECONDS);
        MediaReader mr = new MediaReader(container, true, ConverterFactory.XUGGLER_BGR_24);
        if (SHOW_VIDEO) mr.addListener(new MediaViewer(true));
        IMediaListener mrl = new MediaAdapter() {

            public void onVideoPicture(IMediaTool tool, IVideoPicture picture, BufferedImage image, int streamIndex) {
                assertNotNull("picture should be created", picture);
                assertNotNull("buffered image should be created", image);
                ++counts[0];
            }

            public void onAudioSamples(IMediaTool tool, IAudioSamples samples, int streamIndex) {
                assertNotNull("audio samples should be created", samples);
                ++counts[1];
            }
        };
        mr.addListener(mrl);
        IError err = null;
        while ((err = mr.readPacket()) == null) ;
        assertEquals("Loop should complete with an EOF", IError.Type.ERROR_EOF, err.getType());
        assertEquals("incorrect number of video frames:", counts[0], TEST_FILE_20_SECONDS_VIDEO_FRAME_COUNT);
        assertEquals("incorrect number of audio frames:", counts[1], TEST_FILE_20_SECONDS_AUDIO_FRAME_COUNT);
        assertTrue("container should be open", container.isOpened());
        assertEquals("container should have two streams", 2, container.getNumStreams());
        for (int i = 0; i < container.getNumStreams(); ++i) assertFalse(container.getStream(i).getStreamCoder().isOpen());
    }
}
