package org.apache.harmony.nio.tests.java.nio.channels;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.apache.harmony.nio.tests.java.nio.channels");
        suite.addTestSuite(FileChannelLockingTest.class);
        suite.addTestSuite(FileChannelTest.class);
        suite.addTestSuite(SinkChannelTest.class);
        suite.addTestSuite(DatagramChannelTest.class);
        suite.addTestSuite(PipeTest.class);
        suite.addTestSuite(ChannelsTest.class);
        suite.addTestSuite(ServerSocketChannelTest.class);
        suite.addTestSuite(SocketChannelTest.class);
        suite.addTestSuite(SourceChannelTest.class);
        suite.addTestSuite(SelectionKeyTest.class);
        suite.addTestSuite(SelectorTest.class);
        suite.addTestSuite(AlreadyConnectedExceptionTest.class);
        suite.addTestSuite(SelectableChannelTest.class);
        suite.addTestSuite(FileLockTest.class);
        suite.addTestSuite(AsynchronousCloseExceptionTest.class);
        suite.addTestSuite(CancelledKeyExceptionTest.class);
        suite.addTestSuite(ClosedByInterruptExceptionTest.class);
        suite.addTestSuite(ClosedChannelExceptionTest.class);
        suite.addTestSuite(ClosedSelectorExceptionTest.class);
        suite.addTestSuite(ConnectionPendingExceptionTest.class);
        suite.addTestSuite(FileLockInterruptionExceptionTest.class);
        suite.addTestSuite(IllegalBlockingModeExceptionTest.class);
        suite.addTestSuite(IllegalSelectorExceptionTest.class);
        suite.addTestSuite(NoConnectionPendingExceptionTest.class);
        suite.addTestSuite(NonReadableChannelExceptionTest.class);
        suite.addTestSuite(NonWritableChannelExceptionTest.class);
        suite.addTestSuite(NotYetBoundExceptionTest.class);
        suite.addTestSuite(NotYetConnectedExceptionTest.class);
        suite.addTestSuite(OverlappingFileLockExceptionTest.class);
        suite.addTestSuite(UnresolvedAddressExceptionTest.class);
        suite.addTestSuite(UnsupportedAddressTypeExceptionTest.class);
        suite.addTestSuite(MapModeTest.class);
        return suite;
    }
}
