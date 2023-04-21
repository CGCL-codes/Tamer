package net.sf.jmedcat.core.plugin.insert;

import net.sf.jmedcat.core.insert.QueueElement;
import net.sf.jmedcat.core.util.JMCException;

/**
 * @author Peter Butkovic
 * 
 */
public abstract class Processor {

    private Processor next;

    /**
	 * Inserts file to DB.
	 * 
	 * @param qElement
	 * @return
	 * @throws JMCException 
	 */
    public QueueElement process(QueueElement qElement) throws JMCException {
        qElement.getFile().process();
        qElement = exec(qElement);
        if (!qElement.getFile().isProcessingDone() && (null != next)) {
            next.process(qElement);
        }
        return qElement;
    }

    protected abstract QueueElement exec(QueueElement qElement);

    /**
	 * @param next
	 *            the next to set
	 */
    public Processor setNext(Processor next) {
        this.next = next;
        return next;
    }

    /**
	 * @return true if is able to be plugged in. Otherwise returns false.
	 */
    public abstract boolean isPlugged();
}
