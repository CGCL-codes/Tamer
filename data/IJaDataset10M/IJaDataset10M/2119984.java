package visualgraph.graph.attributed;

import java.util.HashMap;

/**
 * This is a simple implementation of a DelayGenerator that ignores 
 * the start and end data to determine the delay value.
 * 
 * @author Micha
 *
 */
public class DefaultDelayGenerator implements DelayGenerator {

    private long m_defaultDelay = 0;

    private HashMap m_keyValues = new HashMap();

    public DefaultDelayGenerator(long defaultDelay) {
        setDefaultDelay(defaultDelay);
    }

    public void setDefaultDelay(long delay) {
        m_defaultDelay = delay;
    }

    public void setDelay(String key, long delay) {
        m_keyValues.put(key, new Long(delay));
    }

    public long getDelay(String key, Object startData, Object endData) {
        if (m_keyValues.containsKey(key)) {
            return ((Long) m_keyValues.get(key)).longValue();
        }
        return m_defaultDelay;
    }
}
