package org.databene.stat;

import static org.junit.Assert.*;
import org.databene.stat.CounterRepository;
import org.databene.stat.LatencyCounter;
import org.junit.After;
import org.junit.Test;

/**
 * Tests the {@link CounterRepository}.<br/><br/>
 * Created: 14.01.2011 11:34:25
 * @since 1.08
 * @author Volker Bergmann
 */
public class CounterRepositoryTest {

    private static final String NAME = "CounterRepositoryTest";

    CounterRepository repository = CounterRepository.getInstance();

    @After
    public void tearDown() {
        repository.clear();
    }

    @Test
    public void testLifeCyle() {
        assertNull("Counter should not be defined yet", repository.getCounter(NAME));
        repository.addSample(NAME, 100);
        LatencyCounter counter = repository.getCounter(NAME);
        assertNotNull("Counter should have been defined after calling addSample()", counter);
        assertTrue("repository is expected to return the same counter instance on subsequent calls to getCounter()", counter == repository.getCounter(NAME));
        repository.clear();
        assertNull("After calling clear(), the repository should have no counters", repository.getCounter(NAME));
    }
}
