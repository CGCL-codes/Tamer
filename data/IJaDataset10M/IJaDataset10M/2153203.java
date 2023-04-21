package com.xebia.gclogviewer.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BlockingStringBufferTest.class, RegionReaderTest.class, TailReaderTest.class, ValidXmlTest.class })
public class AllTests {
}
