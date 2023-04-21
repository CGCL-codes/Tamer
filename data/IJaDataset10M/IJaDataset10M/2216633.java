package org.cheetah.core.action.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.cheetah.core.action.Action;
import org.cheetah.core.action.ActionContext;
import org.cheetah.core.action.ActionDefinition;
import org.cheetah.core.exchange.Exchange;
import org.cheetah.core.exchange.Message;

@XmlRootElement(name = "fork")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public class ForkDefinition extends ActionDefinition {

    @XmlTransient
    public static ExecutorService DEFAULT_EXECUTOR = null;

    @XmlElementRef
    private List<PipelineDefinition> pipelines;

    @XmlTransient
    private ExecutorService executor;

    public ForkDefinition() {
        pipelines = new ArrayList<PipelineDefinition>();
        executor = null;
    }

    public ForkDefinition add(PipelineDefinition pipeline) {
        pipelines.add(pipeline);
        return this;
    }

    public ForkDefinition setExecutor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public Action createAction() throws Exception {
        Action[] actions = new Action[pipelines.size()];
        int i = 0;
        for (PipelineDefinition def : pipelines) {
            actions[i] = def.createAction();
            ++i;
        }
        if (executor == null) {
            executor = DEFAULT_EXECUTOR;
        }
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
        }
        return new ActionImpl(actions, executor);
    }

    private static class ActionImpl implements Action {

        private Action[] actions;

        private int count;

        private ExecutorService executor;

        ActionImpl(Action[] actions, ExecutorService executor) {
            this.actions = actions;
            this.count = actions.length;
            this.executor = executor;
        }

        public void process(ActionContext ctx, Exchange exchange) throws Exception {
            final CountDownLatch latch = new CountDownLatch(count);
            final List<Throwable> faults = new ArrayList<Throwable>();
            for (int i = 0; i < count; ++i) {
                try {
                    final Action action = actions[i];
                    final Exchange cloned = new Exchange((Message) exchange.getIn().clone());
                    executor.submit(new Runnable() {

                        public void run() {
                            try {
                                action.process(ActionContext.NOTHING, cloned);
                            } catch (Throwable t) {
                                faults.add(t);
                            }
                            latch.countDown();
                        }
                    }, cloned);
                } catch (Throwable t) {
                    faults.add(t);
                    latch.countDown();
                }
            }
            latch.await();
            if (!faults.isEmpty()) {
                Throwable t = faults.get(0);
                if (t instanceof Exception) {
                    throw (Exception) t;
                } else if (t instanceof Error) {
                    throw (Error) t;
                } else {
                    throw new Exception(t);
                }
            }
            ctx.process(exchange);
        }
    }
}
