package com.hp.hpl.jena.query.darq.engine.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.hp.hpl.jena.query.core.Var;
import com.hp.hpl.jena.query.darq.core.MultipleServiceGroup;
import com.hp.hpl.jena.query.darq.core.RemoteService;
import com.hp.hpl.jena.query.darq.engine.compiler.iterators.QueryIterUnionParallel;
import com.hp.hpl.jena.query.darq.util.FedPlanVisitor;
import com.hp.hpl.jena.query.darq.util.OutputUtils;
import com.hp.hpl.jena.query.engine.QueryIterator;
import com.hp.hpl.jena.query.engine1.ExecutionContext;
import com.hp.hpl.jena.query.engine1.PlanElement;
import com.hp.hpl.jena.query.engine1.PlanVisitor;
import com.hp.hpl.jena.query.engine1.iterator.QueryIterDistinct;
import com.hp.hpl.jena.query.engine1.plan.PlanElement1;
import com.hp.hpl.jena.query.engine1.plan.Transform;
import com.hp.hpl.jena.query.util.Context;
import de.hu_berlin.informatik.wbi.darq.cache.Caching;

/**
 * 
 * @author Bastian Quilitz
 * @version $ID$
 *
 */
public class FedPlanMultipleService extends PlanElement1 {

    private MultipleServiceGroup serviceGroup;

    private Caching cache;

    private Boolean cacheEnabled;

    public MultipleServiceGroup getServiceGroup() {
        return serviceGroup;
    }

    public static PlanElement make(Context c, MultipleServiceGroup sg, PlanElement subElt, Caching cache, Boolean cacheEnabled) {
        return new FedPlanMultipleService(c, sg, subElt, cache, cacheEnabled);
    }

    private FedPlanMultipleService(Context c, MultipleServiceGroup sg, PlanElement cElt, Caching cache, Boolean cacheEnabled) {
        super(c, cElt);
        serviceGroup = sg;
        this.cache = cache;
        this.cacheEnabled = cacheEnabled;
    }

    public QueryIterator build(QueryIterator input, ExecutionContext execCxt) {
        List<PlanElement> list = new ArrayList<PlanElement>();
        for (RemoteService s : serviceGroup.getServices()) {
            list.add(FedPlanService.make(this.getContext(), serviceGroup.getServiceGroup(s), this.getSubElement(), cache, cacheEnabled));
        }
        Set<String> usedVariables = serviceGroup.getUsedVariables();
        List<Var> vars = new ArrayList<Var>();
        for (String s : usedVariables) vars.add(Var.alloc(s));
        for (String s : (List<String>) execCxt.getQuery().getResultVars()) vars.add(Var.alloc(s));
        QueryIterator qIter = BindingImmutableDistinctUnion.create(vars, (QueryIterator) new QueryIterUnionParallel(input, list, execCxt), execCxt);
        return new QueryIterDistinct(qIter, execCxt);
    }

    public void visit(PlanVisitor visitor) {
        if (visitor instanceof FedPlanVisitor) {
            ((FedPlanVisitor) visitor).visit(this);
        } else {
        }
    }

    @Override
    public String toString() {
        return OutputUtils.serviceGroupToString(this.serviceGroup);
    }

    @Override
    public PlanElement apply(Transform transform, PlanElement x) {
        return x;
    }

    @Override
    public PlanElement copy(PlanElement newSubElement) {
        return make(getContext(), serviceGroup, newSubElement, cache, cacheEnabled);
    }
}
