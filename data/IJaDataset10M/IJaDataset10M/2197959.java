package com.tredart.event.impl;

import static com.tredart.event.EEventType.CASH;
import static com.tredart.event.EEventType.TRANSACTION_CREATE;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import org.junit.Test;
import com.tredart.event.EventDistributionException;
import com.tredart.event.IDistributeEvents;
import com.tredart.event.IProcessEvent;
import com.tredart.event.ITredartEvent;

/**
 * Tests for the EventDistributor.
 * 
 * @author fdegrazia
 * @author gnicoll
 */
public class EventsDistributorTest {

    private static final double AMOUNT = 0.54d;

    private static final double CURRENT_BALANCE = 189256432.66d;

    /**
     * Tests that an event is distributed to a single processor.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDistributeEventToSingleProcessor() {
        final CashEvent cashEvent = new CashEvent("fdegrazia-million-dollar", AMOUNT, CURRENT_BALANCE);
        final IProcessEvent<CashEvent> cashProcessor = createMock(IProcessEvent.class);
        cashProcessor.process(cashEvent);
        replay(cashProcessor);
        final IDistributeEvents distributor = new EventsDistributor();
        distributor.addProcessor(CASH, cashProcessor);
        distributor.distribute(cashEvent);
        verify(cashProcessor);
    }

    /**
     * Tests that an event is distibuted to the correct processor only.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDistributeToCorrectProcessor() {
        final CashEvent cashEvent = new CashEvent("fdegrazia-million-dollar", AMOUNT, CURRENT_BALANCE);
        final IProcessEvent<CashEvent> cashProcessor = createMock(IProcessEvent.class);
        cashProcessor.process(cashEvent);
        replay(cashProcessor);
        final IProcessEvent<ITredartEvent> testProcessor = createMock(IProcessEvent.class);
        replay(testProcessor);
        final IDistributeEvents distributor = new EventsDistributor();
        distributor.addProcessor(CASH, cashProcessor);
        distributor.addProcessor(TRANSACTION_CREATE, testProcessor);
        distributor.distribute(cashEvent);
        verify(cashProcessor);
        verify(testProcessor);
    }

    /**
     * Tests that an event is distributed to the correct processors and is
     * distributed to the correct processors.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDistributeToCorrectProcessorsMultiple() {
        final CashEvent cashEvent = new CashEvent("fdegrazia-million-dollar", AMOUNT, CURRENT_BALANCE);
        final IProcessEvent<CashEvent> cashProcessor1 = createMock(IProcessEvent.class);
        cashProcessor1.process(cashEvent);
        replay(cashProcessor1);
        final IProcessEvent<ITredartEvent> testProcessor = createMock(IProcessEvent.class);
        replay(testProcessor);
        final IProcessEvent<CashEvent> cashProcessor2 = createMock(IProcessEvent.class);
        cashProcessor2.process(cashEvent);
        replay(cashProcessor2);
        final IDistributeEvents distributor = new EventsDistributor();
        distributor.addProcessor(CASH, cashProcessor1);
        distributor.addProcessor(TRANSACTION_CREATE, testProcessor);
        distributor.addProcessor(CASH, cashProcessor2);
        distributor.distribute(cashEvent);
        verify(cashProcessor1);
        verify(testProcessor);
        verify(cashProcessor2);
    }

    /**
     * Tests that if no processors are found for an event, then an
     * EventDistributionException is thrown.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = EventDistributionException.class)
    public void testProcessorNotFoundThrowsDistributionException() {
        final CashEvent cashEvent = new CashEvent("fdegrazia-million-dollar", AMOUNT, CURRENT_BALANCE);
        final IProcessEvent<ITredartEvent> testProcessor = createMock(IProcessEvent.class);
        replay(testProcessor);
        final IDistributeEvents distributor = new EventsDistributor();
        distributor.addProcessor(TRANSACTION_CREATE, testProcessor);
        distributor.distribute(cashEvent);
    }

    /**
     * Test that an exception is thrown when no event type is specified.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testMustSpecifyEventType() {
        final IDistributeEvents distributor = new EventsDistributor();
        distributor.addProcessor(null, createMock(IProcessEvent.class));
    }

    /**
     * Test that an exception is thrown when no processor is specified.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMustSpecifyProcessor() {
        final IDistributeEvents distributor = new EventsDistributor();
        distributor.addProcessor(CASH, null);
    }
}
