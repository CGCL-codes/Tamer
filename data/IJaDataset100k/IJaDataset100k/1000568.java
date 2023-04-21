package org.nms.anxova.process.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.nms.anxova.process.IProcessor;
import org.nms.anxova.process.beans.IElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chain processor.
 * <p>
 * Executes the processor chain <code>processorChain</code>.
 * </p>
 * 
 * TODO Validate types for each step.
 * 
 * @author daviz
 *
 * @param <O>
 * @param <R>
 * @param <N>
 */
public class ChainedProcessor<O extends Serializable, R extends Serializable, N extends Serializable> extends AbstractBaseProcessor<O, R, N> {

    /**
	 * Logger.
	 */
    private static final Logger log = LoggerFactory.getLogger(ChainedProcessor.class);

    /**
	 * The processor chain list.
	 */
    private List<IProcessor> processorChain;

    @Override
    public List<IElement<R>> process(IElement<O> e) {
        List<IElement<R>> result = null;
        if (this.processorChain != null) {
            int step = 1;
            List partialResult = new ArrayList<IElement>();
            partialResult.add(e);
            for (IProcessor p : processorChain) {
                partialResult = p.process(partialResult);
                log.debug("Obtained {} results in step {}", (partialResult != null) ? partialResult.size() : "null", step);
                step++;
            }
            result = partialResult;
        }
        return result;
    }

    /**
	 * @return the processorChain
	 */
    public List<IProcessor> getProcessorChain() {
        return processorChain;
    }

    /**
	 * @param processorChain the processorChain to set
	 */
    public void setProcessorChain(List<IProcessor> processorChain) {
        this.processorChain = processorChain;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Chain:").append((processorChain != null) ? processorChain.size() : "null");
        return sb.toString();
    }
}
