package org.databene.platform.contiperf;

import org.databene.benerator.Consumer;
import org.databene.benerator.wrapper.ProductWrapper;
import org.databene.contiperf.Invoker;

/**
 * {@link Consumer} implementation that calls a ContiPerf {@link PerfTrackingConsumer}.<br/><br/>
 * Created: 22.10.2009 16:17:14
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class PerfTrackingConsumer extends PerfTrackingWrapper implements Consumer {

    private String id;

    private Consumer target;

    public PerfTrackingConsumer() {
        this(null);
    }

    public PerfTrackingConsumer(Consumer target) {
        this(target, "Unnamed");
    }

    public PerfTrackingConsumer(Consumer target, String id) {
        this.id = id;
        this.target = target;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTarget(Consumer target) {
        this.target = target;
    }

    public void startConsuming(ProductWrapper<?> wrapper) {
        try {
            getTracker().invoke(new Object[] { wrapper });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void finishConsuming(ProductWrapper<?> wrapper) {
        target.finishConsuming(wrapper);
    }

    public void flush() {
        target.flush();
    }

    @Override
    public void close() {
        super.close();
        target.close();
    }

    @Override
    protected Invoker getInvoker() {
        return new ConsumerInvoker(id, target);
    }
}
