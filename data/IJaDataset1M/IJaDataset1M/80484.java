package net.sf.jjmpeg;

import net.sf.jjmpeg.bitstream.BitStreamTests;
import net.sf.jjmpeg.codec.CodecTests;
import net.sf.jjmpeg.container.ContainerTests;
import net.sf.jjmpeg.media.MediaTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Tests the classes in the net.sf.jjmpeg package
 * 
 * @author Derek Whitaker & Trenton Pack
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({ BitStreamTests.class, MediaTests.class, ContainerTests.class, CodecTests.class })
public class AllTests {
}
