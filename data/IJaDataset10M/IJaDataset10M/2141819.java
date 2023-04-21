package de.lema.appender.failure;

import junit.framework.Assert;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import atunit.AtUnit;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Unit;
import de.lema.appender.net.SenderController;

@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.MOCKITO)
public class DropAndSendInfoStrategyTest {

    @Unit
    DropAndSendInfoStrategy toTest;

    @Mock
    LoggingEvent event;

    @Mock
    SenderController eventQueue;

    @Test
    public void testCount() {
        toTest = new DropAndSendInfoStrategy(eventQueue);
        toTest.onFailure(event);
        toTest.onFailure(event);
        Assert.assertEquals(2, toTest.getDroppedEvents());
    }

    @Test
    public void testResend() {
        DropAndSendInfoStrategy toTest = new DropAndSendInfoStrategy(eventQueue);
        toTest.onFailure(event);
        toTest.onFailure(event);
        toTest.afterReconnect();
        Assert.assertEquals(0, toTest.getDroppedEvents());
        Mockito.verify(eventQueue, VerificationModeFactory.times(1)).enqueForSending(Mockito.any(LoggingEvent.class));
    }
}
