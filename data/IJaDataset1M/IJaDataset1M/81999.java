package org.xsocket.stream.io.mina;

import java.util.logging.Level;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.xsocket.QAUtil;

/**
 * 
 * @author grro@xsocket.org
 */
public final class CoreTest {

    @Test
    public void runAllTests() throws Throwable {
        System.setProperty("org.xsocket.stream.io.spi.ServerIoProviderClass", org.xsocket.stream.io.mina.MinaIoProvider.class.getName());
        Class[] classes = new Class[] { org.xsocket.stream.BlockingConnectionTest.class, org.xsocket.stream.NonBlockingConnectionClientTest.class, org.xsocket.stream.ConcurrentCallbackCallsTest.class, org.xsocket.stream.UnsynchronizedTest.class, org.xsocket.stream.AcceptorReuseAddressTest.class, org.xsocket.stream.AsyncFlushModeTest.class, org.xsocket.stream.AttachmentTest.class, org.xsocket.stream.BlockingConnectionPoolTest.class, org.xsocket.stream.ChainedRecordsCallTest.class, org.xsocket.stream.CloseTest.class, org.xsocket.stream.ConcurrentCallbackCallsTest.class, org.xsocket.stream.DataPackagesTest.class, org.xsocket.stream.DataTypesTest.class, org.xsocket.stream.DisconnectTest.class, org.xsocket.stream.FlushOnCloseTest.class, org.xsocket.stream.ReducedTransferRateTest.class, org.xsocket.stream.FlushTest.class, org.xsocket.stream.HandlerAutoflushTest.class, org.xsocket.stream.HandlerSynchronizeTest.class, org.xsocket.stream.HandlerThrowsIOExceptionTest.class, org.xsocket.stream.IndexOfTest.class, org.xsocket.stream.LargeDataTransferTest.class, org.xsocket.stream.LifeCycleTest.class, org.xsocket.stream.LocalAddressTest.class, org.xsocket.stream.MarkAndResetTest.class, org.xsocket.stream.MarkAndResetWithDelimiterTest.class, org.xsocket.stream.MaxReadSizeExceededTest.class, org.xsocket.stream.NonBlockingConnectionClientHandlerTest.class, org.xsocket.stream.NonBlockingConnectionPoolHandlerTest.class, org.xsocket.stream.NonBlockingConnectionPoolTest.class, org.xsocket.stream.NonBlockingConnectionSinglethreadedTest.class, org.xsocket.stream.NonBlockingWriteLoopTest.class, org.xsocket.stream.NoWorkerPoolTest.class, org.xsocket.stream.OnConnectTest.class, org.xsocket.stream.PendingWriteDataTest.class, org.xsocket.stream.ReadableTest.class, org.xsocket.stream.ReadAvailableByDelimiterTest.class, org.xsocket.stream.ReadByDelimiterAndMaxSizeTest.class, org.xsocket.stream.ReadByteBufferTest.class, org.xsocket.stream.ReadOnServerCloseTest.class, org.xsocket.stream.ReadSuspendAndResumeTest.class, org.xsocket.stream.ReducedTransferRateTest.class, org.xsocket.stream.RepeatedCloseTest.class, org.xsocket.stream.ServerContextTest.class, org.xsocket.stream.SimpleNonBlockingClientConnectionTest.class, org.xsocket.stream.SimultaneousReadWriteTest.class, org.xsocket.stream.ConnectionScopedTest.class, org.xsocket.stream.ChannelWriteTest.class };
        for (Class clazz : classes) {
            System.out.println("running test " + clazz.getName() + " ...");
            Result result = JUnitCore.runClasses(new Class[] { clazz });
            if (result.wasSuccessful()) {
                System.out.println(clazz.getName() + " passed");
            } else {
                System.out.println(clazz.getName() + " failed");
                for (Failure failure : result.getFailures()) {
                    System.out.println(failure.toString());
                }
                throw new Exception(clazz.getName() + " test failed");
            }
        }
        System.out.println("PASSED " + classes.length + " tests passed");
    }
}
