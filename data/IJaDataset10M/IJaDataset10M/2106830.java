package org.redemptionhost;

import org.redemptionhost.model.Greeting;
import java.util.List;

/**
 * This interface defines type-safe methods for all queries of Greeting entities.
 */
public interface GreetingQueries {

    /**
     * Return the latest greetings, ordered by descending date.
     *
     * @param max the maximum number of greetings to return
     * @return the greetings
     */
    List<Greeting> latest(int max);
}
