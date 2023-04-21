package org.cheetah.core.action.spi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import org.cheetah.core.action.Action;
import org.cheetah.core.action.ActionContext;
import org.cheetah.core.action.ActionDefinition;
import org.cheetah.core.action.ActionPipeline;
import org.cheetah.core.action.impl.DefaultActionPipeline;
import org.cheetah.core.exchange.Exchange;

@XmlRootElement(name = "pipeline")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public class PipelineDefinition extends ActionDefinition {

    @XmlElementRef
    private List<ActionDefinition> actions;

    public PipelineDefinition() {
        actions = new ArrayList<ActionDefinition>();
    }

    public PipelineDefinition addAll(ActionDefinition... actions) {
        this.actions.addAll(Arrays.asList(actions));
        return this;
    }

    public PipelineDefinition addAll(Collection<? extends ActionDefinition> actions) {
        this.actions.addAll(actions);
        return this;
    }

    public PipelineDefinition add(ActionDefinition action) {
        actions.add(action);
        return this;
    }

    public Action createAction() throws Exception {
        Action[] actions = new Action[this.actions.size()];
        int i = 0;
        for (ActionDefinition def : this.actions) {
            actions[i] = def.createAction();
            ++i;
        }
        return new ActionImpl(new DefaultActionPipeline(actions));
    }

    private static class ActionImpl implements Action {

        private ActionPipeline pipeline;

        ActionImpl(ActionPipeline pipeline) {
            this.pipeline = pipeline;
        }

        public void process(ActionContext ctx, Exchange exchange) throws Exception {
            pipeline.process(exchange);
        }
    }
}
