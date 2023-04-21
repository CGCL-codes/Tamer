package net.sf.jretina.lib.chains;

import java.util.ArrayList;
import java.util.List;
import net.sf.jretina.lib.processors.IProcessable;
import net.sf.jretina.lib.processors.IProcessor;
import net.sf.jretina.lib.processors.ProcessListener;

/**
 * @author Andreas Beckers
 */
public class ArrayChain implements Chain {

    private List<Chainable> _chains = new ArrayList<Chainable>();

    public void add(Chainable c) {
        _chains.add(c);
    }

    @Override
    public void process(IProcessable processable, ProcessListener listener) {
        long t0 = System.currentTimeMillis();
        for (Chainable chain : _chains) {
            if (listener != null) listener.nextChainElement();
            if (chain instanceof IProcessor) {
                System.out.println("start " + chain);
                IProcessor proc = (IProcessor) chain;
                if (listener != null) listener.chainElementProcessableCount(proc.countElements(processable));
                proc.process(processable, listener);
                System.out.println("end " + chain);
            }
        }
        if (listener != null) listener.ended();
        System.out.println("total time " + (System.currentTimeMillis() - t0) + "ms");
    }

    /**
	 * @see net.sf.jretina.lib.chains.Chain#getSize()
	 */
    @Override
    public int getSize() {
        return _chains.size();
    }
}
