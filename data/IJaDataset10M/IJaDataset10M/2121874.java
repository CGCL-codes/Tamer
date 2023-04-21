package net.gombology.synOscP5;

import net.gombology.synOscP5.SYN;
import junit.framework.TestCase;

public class VolumeTest extends TestCase {

    /**
     * test volume of synth
     * 
     * @since 0.0.1
     */
    public void testVol() {
        SynMessage msg = new SYN().synth(1).volume(100);
        assertEquals("Fi", msg.getTypetag());
        assertEquals((SynMessage.MAX_32BIT), msg.getArguments()[1]);
        assertEquals("/SYN/ID1/VOL", msg.getAddress());
    }

    /**
     * test volume of synth for PureData
     * 
     * @since 0.0.3
     */
    public void testVolPureData() {
        SynMessage msg = new SYN(SYN.PUREDATA).synth(1).volume(100);
        assertEquals("Fi", msg.getTypetag());
        assertEquals((SynMessage.MAX_24BIT), msg.getArguments()[1]);
        assertEquals("/SYN/ID1/VOL", msg.getAddress());
    }

    /**
     * test volume  of synth relative
     * 
     * @since 0.0.1
     */
    public void testVolRel() {
        SynMessage msg = new SYN().synth(1).volume(100).relative();
        assertEquals("Ti", msg.getTypetag());
        assertEquals((SynMessage.MAX_32BIT), msg.getArguments()[1]);
        assertEquals("/SYN/ID1/VOL", msg.getAddress());
    }

    /**
     * Voice volume
     * 
     * @since 0.0.1
     */
    public void testVoiceVol() {
        SynMessage msg = new SYN().synth(1).voice(1).volume(100);
        assertEquals("Fi", msg.getTypetag());
        assertEquals((SynMessage.MAX_32BIT), msg.getArguments()[1]);
        assertEquals("/SYN/ID1/V1/VOL", msg.getAddress());
    }

    /**
     * Voice volume relative
     * 
     * @since 0.0.1
     */
    public void testRelVoiceVol() {
        SynMessage msg = new SYN().synth(1).voice(1).volume(-50).relative();
        assertEquals("Ti", msg.getTypetag());
        assertEquals((-SynMessage.MAX_32BIT / 2), msg.getArguments()[1]);
        assertEquals("/SYN/ID1/V1/VOL", msg.getAddress());
    }
}
