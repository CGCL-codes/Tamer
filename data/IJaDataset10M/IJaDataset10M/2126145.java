package org.mobicents.media.server.impl.rtp;

import org.mobicents.media.server.spi.format.Formats;
import org.mobicents.media.server.spi.format.AudioFormat;
import org.mobicents.media.server.component.DspFactoryImpl;
import org.mobicents.media.server.component.Dsp;
import org.mobicents.media.server.impl.rtp.sdp.AVProfile;
import org.mobicents.media.server.impl.PipeImpl;
import java.net.InetSocketAddress;
import org.mobicents.media.server.component.audio.Sine;
import org.mobicents.media.server.component.audio.SpectraAnalyzer;
import org.mobicents.media.server.scheduler.DefaultClock;
import org.mobicents.media.server.io.network.UdpManager;
import org.mobicents.media.server.scheduler.Scheduler;
import org.mobicents.media.server.scheduler.Clock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mobicents.media.server.spi.format.FormatFactory;
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class RTPChannelTest {

    private Clock clock;

    private Scheduler scheduler;

    private RTPManager rtpManager;

    private UdpManager udpManager;

    private SpectraAnalyzer analyzer1, analyzer2;

    private Sine source1, source2;

    private RTPDataChannel channel1, channel2;

    private PipeImpl txPipe1, txPipe2, rxPipe1, rxPipe2;

    private int fcount;

    private DspFactoryImpl dspFactory = new DspFactoryImpl();

    private Dsp dsp11, dsp12;

    private Dsp dsp21, dsp22;

    public RTPChannelTest() {
    }

    @Before
    public void setUp() throws Exception {
        AudioFormat pcma = FormatFactory.createAudioFormat("pcma", 8000, 8, 1);
        Formats fmts = new Formats();
        fmts.add(pcma);
        Formats dstFormats = new Formats();
        dstFormats.add(FormatFactory.createAudioFormat("LINEAR", 8000, 16, 1));
        dspFactory.addCodec("org.mobicents.media.server.impl.dsp.audio.g711.alaw.Encoder");
        dspFactory.addCodec("org.mobicents.media.server.impl.dsp.audio.g711.alaw.Decoder");
        dsp11 = dspFactory.newProcessor();
        dsp12 = dspFactory.newProcessor();
        dsp21 = dspFactory.newProcessor();
        dsp22 = dspFactory.newProcessor();
        clock = new DefaultClock();
        scheduler = new Scheduler(4);
        scheduler.setClock(clock);
        scheduler.start();
        udpManager = new UdpManager(scheduler);
        udpManager.start();
        rtpManager = new RTPManager(udpManager);
        rtpManager.setBindAddress("127.0.0.1");
        rtpManager.setScheduler(scheduler);
        source1 = new Sine(scheduler);
        source1.setFrequency(50);
        source1.setDsp(dsp11);
        source1.setFormats(fmts);
        source2 = new Sine(scheduler);
        source2.setFrequency(50);
        source2.setDsp(dsp21);
        source2.setFormats(fmts);
        analyzer1 = new SpectraAnalyzer("analyzer", scheduler);
        analyzer1.setDsp(dsp12);
        analyzer1.setFormats(dstFormats);
        analyzer2 = new SpectraAnalyzer("analyzer", scheduler);
        analyzer2.setDsp(dsp22);
        analyzer2.setFormats(dstFormats);
        channel1 = rtpManager.getChannel();
        channel2 = rtpManager.getChannel();
        channel1.bind();
        channel2.bind();
        channel1.setPeer(new InetSocketAddress("127.0.0.1", channel2.getLocalPort()));
        channel2.setPeer(new InetSocketAddress("127.0.0.1", channel1.getLocalPort()));
        channel1.setFormatMap(AVProfile.audio);
        channel2.setFormatMap(AVProfile.audio);
        txPipe1 = new PipeImpl();
        rxPipe1 = new PipeImpl();
        txPipe2 = new PipeImpl();
        rxPipe2 = new PipeImpl();
        txPipe1.connect(source1);
        txPipe1.connect(channel1.getOutput());
        rxPipe1.connect(analyzer1);
        rxPipe1.connect(channel1.getInput());
        txPipe2.connect(source2);
        txPipe2.connect(channel2.getOutput());
        rxPipe2.connect(analyzer2);
        rxPipe2.connect(channel2.getInput());
    }

    @After
    public void tearDown() {
        txPipe1.stop();
        rxPipe1.stop();
        txPipe2.stop();
        rxPipe2.stop();
        channel1.close();
        channel2.close();
        udpManager.stop();
        scheduler.stop();
    }

    @Test
    public void testTransmission() throws Exception {
        txPipe1.start();
        rxPipe1.start();
        txPipe2.start();
        rxPipe2.start();
        Thread.sleep(5000);
        txPipe1.stop();
        txPipe2.stop();
        int s1[] = analyzer1.getSpectra();
        int s2[] = analyzer2.getSpectra();
        System.out.println("rx-channel1: " + channel1.getPacketsReceived());
        System.out.println("tx-channel1: " + channel1.getPacketsTransmitted());
        System.out.println("rx-channel2: " + channel2.getPacketsReceived());
        System.out.println("tx-channel2: " + channel2.getPacketsTransmitted());
        if (s1.length != 1 || s2.length != 1) {
            System.out.println("Failure");
            fcount++;
        } else System.out.println("Passed");
    }

    @Test
    public void testHalfDuplex() throws Exception {
        txPipe1.start();
        rxPipe2.start();
        Thread.sleep(5000);
        txPipe1.stop();
        txPipe2.stop();
        int s1[] = analyzer1.getSpectra();
        int s2[] = analyzer2.getSpectra();
        System.out.println("rx-channel1: " + channel1.getPacketsReceived());
        System.out.println("tx-channel1: " + channel1.getPacketsTransmitted());
        System.out.println("rx-channel2: " + channel2.getPacketsReceived());
        System.out.println("tx-channel2: " + channel2.getPacketsTransmitted());
        if (s1.length != 0 || s2.length != 1) {
            fcount++;
        } else System.out.println("Passed");
        assertEquals(0, fcount);
        assertEquals(50, s2[0], 5);
    }

    @Test
    public void testFailureRate() throws Exception {
        for (int i = 0; i < 1; i++) {
            System.out.println("Test# " + i);
            this.testTransmission();
        }
        assertEquals(0, fcount);
    }

    private void print(int[] s) {
        for (int i = 0; i < s.length; i++) {
            System.out.print(s[i] + " ");
        }
        System.out.println();
    }
}
