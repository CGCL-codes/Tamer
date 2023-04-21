package de.lema.appender.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import junit.framework.Assert;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import de.lema.appender.failure.ConnectionLostStrategy;
import de.lema.appender.net.Beacon;
import de.lema.appender.net.SenderThread;
import de.lema.appender.net.SocketReadWrite;
import atunit.AtUnit;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Unit;

@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.MOCKITO)
public class SenderThreadTest {

    @Unit
    SenderThread toTest;

    @Mock
    SocketReadWrite sender;

    @Mock
    ConnectionLostStrategy connectionLostStrategy;

    @Mock
    LoggingEvent event;

    @Test
    public void testSending() throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        toTest = create(start);
        toTest.enqueForSending(event);
        toTest.enqueForSending(event);
        final List<Serializable> actual = record();
        final List<Serializable> expected = Arrays.<Serializable>asList(event, event);
        toTest.start();
        start.countDown();
        Thread.sleep(40);
        toTest.cancel();
        toTest.join();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBeacon() throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        toTest = create(start);
        toTest.enqueForSending(Beacon.BEACON);
        final List<Serializable> actual = record();
        final List<Serializable> expected = Arrays.<Serializable>asList(Beacon.BEACON);
        toTest.start();
        start.countDown();
        Thread.sleep(40);
        toTest.cancel();
        toTest.join();
        Assert.assertEquals(expected, actual);
        Mockito.verify(sender).read();
    }

    @Test(timeout = 1000)
    public void testDrop() throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        toTest = create(start);
        toTest.enqueForSending(create());
        toTest.enqueForSending(create());
        toTest.enqueForSending(create());
        toTest.enqueForSending(event);
        toTest.enqueForSending(event);
        final List<Serializable> actual = record();
        final List<Serializable> expected = Arrays.<Serializable>asList(event, event);
        toTest.start();
        start.countDown();
        Thread.sleep(40);
        toTest.cancel();
        toTest.join();
        Assert.assertEquals(expected, actual);
        Mockito.verify(connectionLostStrategy, VerificationModeFactory.times(3)).onFailure(Mockito.<LoggingEvent>any());
    }

    private LoggingEvent create() {
        return Mockito.mock(LoggingEvent.class);
    }

    private List<Serializable> record() {
        final List<Serializable> record = new ArrayList<Serializable>();
        Mockito.doAnswer(new Answer<Boolean>() {

            public Boolean answer(InvocationOnMock invocation) throws Exception {
                record.add((Serializable) invocation.getArguments()[0]);
                return true;
            }
        }).when(sender).write(Mockito.<Serializable>any());
        return record;
    }

    private SenderThread create(CountDownLatch start) {
        return new SenderThread(sender, start, connectionLostStrategy, 2);
    }

    @Test(timeout = 1000)
    public void testDaemon() throws InterruptedException {
        toTest = create(new CountDownLatch(1));
        Assert.assertEquals(true, toTest.isDaemon());
    }
}
