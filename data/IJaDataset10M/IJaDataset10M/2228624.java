package gr.demokritos.iit.jinsect.events;

import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author ggianna
 */
public interface TokenGeneratorListener {

    public List getTokens();

    public ListIterator getIterator();
}
