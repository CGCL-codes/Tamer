package playground.christoph.events.parallelEventsHandler;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import org.matsim.core.api.experimental.events.Event;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.parallelEventsHandler.LastEventOfIteration;
import org.matsim.core.events.parallelEventsHandler.LastEventOfSimStep;
import org.matsim.core.gbl.Gbl;

public class WithinDayProcessEventThread implements Runnable {

    private ArrayList<Event> preInputBuffer = null;

    private LinkedBlockingQueue<Event> eventQueue = null;

    private EventsManager events;

    private CyclicBarrier simStepBarrier = null;

    private CyclicBarrier iterationBarrier = null;

    private int preInputBufferMaxLength;

    public WithinDayProcessEventThread(EventsManager events, int preInputBufferMaxLength, CyclicBarrier simStepBarrier, CyclicBarrier iterationBarrier) {
        this.events = events;
        this.preInputBufferMaxLength = preInputBufferMaxLength;
        eventQueue = new LinkedBlockingQueue<Event>();
        preInputBuffer = new ArrayList<Event>();
        this.simStepBarrier = simStepBarrier;
        this.iterationBarrier = iterationBarrier;
    }

    public synchronized void processEvent(Event event) {
        preInputBuffer.add(event);
        if (preInputBuffer.size() > preInputBufferMaxLength) {
            eventQueue.addAll(preInputBuffer);
            preInputBuffer.clear();
        }
    }

    public void run() {
        try {
            Event nextEvent = null;
            while (true) {
                nextEvent = eventQueue.take();
                if (nextEvent != null) {
                    if (nextEvent instanceof LastEventOfIteration) {
                        break;
                    } else if (nextEvent instanceof LastEventOfSimStep) {
                        this.simStepBarrier.await();
                    }
                    events.processEvent(nextEvent);
                }
            }
            iterationBarrier.await();
            Gbl.printCurrentThreadCpuTime();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void closeIteration() {
        processEvent(new LastEventOfIteration(0.0));
        eventQueue.addAll(preInputBuffer);
        preInputBuffer.clear();
    }

    public void closeSimStep() {
        processEvent(new LastEventOfSimStep(0.0));
        eventQueue.addAll(preInputBuffer);
        preInputBuffer.clear();
    }
}
