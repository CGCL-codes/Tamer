package fr.lelouet.tools.sequentialIterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.lelouet.tools.SequentialIterator;

public class Pulsed<T> implements SequentialIterator<T> {

    private static final Logger logger = LoggerFactory.getLogger(Pulsed.class);

    public static final String PULSES_PERIOD_KEY = "PulsedIterator.periodms";

    protected Iterator<T> internalIterator = null;

    protected long pulsesPeriod_ms = 5000;

    protected long nextPulseTime = 0;

    @Override
    public boolean hasNext() {
        return internalIterator.hasNext();
    }

    @Override
    public T next() {
        return internalIterator.next();
    }

    @Override
    public void remove() {
        internalIterator.remove();
    }

    @Override
    public void configure(Properties prop) {
        if (prop.containsKey(PULSES_PERIOD_KEY)) {
            try {
                pulsesPeriod_ms = Long.parseLong(prop.getProperty(PULSES_PERIOD_KEY));
            } catch (Exception e) {
                logger.warn("{}; {}", e.getMessage(), e.getStackTrace());
            }
        }
    }

    @Override
    public void startNewLoop(Collection<T> requests) {
        long time = System.currentTimeMillis();
        long delay = nextPulseTime - time;
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                logger.warn("{}; {}", e.getMessage(), e.getStackTrace());
            }
        }
        nextPulseTime = System.currentTimeMillis() + pulsesPeriod_ms;
        internalIterator = requests.iterator();
    }
}
