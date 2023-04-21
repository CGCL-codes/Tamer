package net.sourceforge.cruisecontrol.publishers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.Publisher;
import net.sourceforge.cruisecontrol.util.ValidationHelper;

/**
 * An abstract base class for any publisher which wishes to conditionally
 * execute a set of contained <coe>Publisher</code>s.
 */
public abstract class ConditionalPublisher implements Publisher {

    private List publishers = new ArrayList();

    public void publish(Element log) throws CruiseControlException {
        if (shouldPublish(log)) {
            for (Iterator iterator = publishers.iterator(); iterator.hasNext(); ) {
                Publisher publisher = (Publisher) iterator.next();
                publisher.publish(log);
            }
        }
    }

    public void validate() throws CruiseControlException {
        ValidationHelper.assertTrue(publishers.size() > 0, "conditional publishers should have at least one nested publisher");
        for (Iterator iterator = publishers.iterator(); iterator.hasNext(); ) {
            Publisher publisher = (Publisher) iterator.next();
            publisher.validate();
        }
    }

    /**
     * Adds a nested publisher
     *
     * @param publisher The publisher to add
     */
    public void add(Publisher publisher) {
        publishers.add(publisher);
    }

    /**
     * Determines if the nested publishers should be executed. This method must
     * be implemented by all derived classes.
     *
     * @param log
     *            The build log
     * @return <code>true</code> if the nested publishers should be executed,
     *         <code>false</code> otherwise
     */
    public abstract boolean shouldPublish(Element log);
}
