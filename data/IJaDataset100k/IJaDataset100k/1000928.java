package net.sf.jjmpeg.bitstream;

import net.sf.jjmpeg.bitstream.StreamInputBitstreamTest;
import net.sf.jjmpeg.bitstream.StreamOutputBitstreamTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Tests the classes in the net.sf.jjmpeg.bitstream package
 * 
 * @author Derek Whitaker & Trenton Pack
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({ StreamInputBitstreamTest.class, StreamOutputBitstreamTest.class })
public class BitStreamTests {
}
