package fr.lig.sigma.astral.core.operators.join;

import fr.lig.sigma.astral.AstralEngine;
import fr.lig.sigma.astral.common.AttributeSet;
import fr.lig.sigma.astral.common.AxiomNotVerifiedException;
import fr.lig.sigma.astral.common.Batch;
import fr.lig.sigma.astral.common.Couple;
import fr.lig.sigma.astral.common.Tuple;
import fr.lig.sigma.astral.common.WrongAttributeException;
import fr.lig.sigma.astral.common.structure.DynamicRelation;
import fr.lig.sigma.astral.common.structure.TupleSet;
import fr.lig.sigma.astral.operators.DynamicRelationOperator;
import fr.lig.sigma.astral.operators.Operator;
import fr.lig.sigma.astral.operators.relational.sigma.Condition;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.log4j.Logger;
import java.util.Collection;
import java.util.List;

/**
 * @author Loic Petit
 */
@Component
@Provides
public class SymetricHashJoin extends DynamicRelationOperator implements Operator {

    private static final Logger log = Logger.getLogger(SymetricHashJoin.class);

    private Condition optional;

    @Property
    private String condition;

    @Property
    private Comparable defaultOuter;

    @Property
    private List<String> attributes;

    @Property
    private List<String> conditionAttributes;

    @Requires(id = "in", policy = "dynamic-priority")
    private DynamicRelation[] in;

    @Requires(id = "engine")
    private AstralEngine engine;

    private DynamicRelation left;

    private DynamicRelation right;

    private AttributeSet joinAttributes;

    private AttributeSet otherAttributes;

    private AttributeSet outerAttributes;

    private int comparisons;

    private int outer = 0;

    @Override
    public int getMaxInputs() {
        return 2;
    }

    public void prepare() throws Exception {
        left = in[0];
        right = in[1];
        joinAttributes = new AttributeSet(left.getAttributes());
        joinAttributes.retainAll(right.getAttributes());
        joinAttributes.remove(Tuple.PHYSICAL_ID);
        otherAttributes = new AttributeSet(attributes);
        otherAttributes.removeAll(joinAttributes);
        otherAttributes.remove(Tuple.PHYSICAL_ID);
        if (optional != null) otherAttributes.removeAll(conditionAttributes);
        if (condition != null && !condition.isEmpty()) this.optional = engine.getGlobalEA().buildCondition(condition);
        if (optional != null && !attributes.containsAll(conditionAttributes)) throw new WrongAttributeException("Condition " + optional + " has attributes that does not match " + attributes);
        if (defaultOuter != null) {
            outerAttributes = new AttributeSet(right.getAttributes());
            outerAttributes.removeAll(joinAttributes);
            try {
                if (defaultOuter instanceof String) defaultOuter = Long.parseLong((String) defaultOuter);
            } catch (Exception ignored) {
            }
            try {
                if (defaultOuter instanceof String) defaultOuter = Double.parseDouble((String) defaultOuter);
            } catch (Exception ignored) {
            }
        }
        setOutput(createNewFrom(left, new AttributeSet(attributes), left.getName() + "\\Join" + (optional != null ? "_{" + optional + "}" : " ") + right.getName()));
        addInput(left, true);
        addInput(right, true);
    }

    @Override
    public void processEvent(Batch b) throws AxiomNotVerifiedException {
        TupleSet leftContent = left.getContent(b);
        leftContent.addIndex(joinAttributes);
        TupleSet rightContent = right.getContent(b);
        rightContent.addIndex(joinAttributes);
        TupleSet pastLeftContent = left.getContent(b.getClose());
        leftContent.addIndex(joinAttributes);
        TupleSet pastRightContent = right.getContent(b.getClose());
        rightContent.addIndex(joinAttributes);
        comparisons = 0;
        TupleSet is = entityFactory.instanciateTupleSet(getAttributes());
        TupleSet ds = entityFactory.instanciateTupleSet(getAttributes());
        TupleSet dsLeft = left.getDeletedTuples(b);
        TupleSet isLeft = left.getInsertedTuples(b);
        TupleSet dsRight = right.getDeletedTuples(b);
        dsRight.addIndex(joinAttributes);
        TupleSet isRight = right.getInsertedTuples(b);
        isRight.addIndex(joinAttributes);
        computeJoin(isRight, leftContent, is, false);
        computeJoin(isLeft, rightContent, is, true);
        computeJoin(dsRight, pastLeftContent, ds, false);
        computeJoin(dsLeft, pastRightContent, ds, true);
        update(b, is, ds);
        if (log.isTraceEnabled()) log.trace(b + ": Join (on " + joinAttributes + ") computed with a size of " + getSizeAsString(is, 0, ds) + " (comparisons: " + comparisons + ", " + (defaultOuter != null ? "outer: " + outer + ", " : "") + "left: " + getSizeAsString(isLeft, leftContent.size(), dsLeft) + ", right: " + getSizeAsString(isRight, rightContent.size(), dsRight) + ")");
    }

    private String getSizeAsString(TupleSet is, int size, TupleSet ds) {
        return "(" + is.size() + "," + size + "," + ds.size() + ")";
    }

    private void computeJoin(TupleSet left, TupleSet rightContent, TupleSet target, boolean leftToRight) {
        for (Tuple l : left) {
            Collection<Tuple> rightTuples = rightContent.fetchTupleFromValue(joinAttributes, l);
            if (rightTuples == null) {
                continue;
            }
            for (Tuple r : rightTuples) {
                comparisons++;
                Tuple t1 = leftToRight ? l : r;
                Tuple t2 = leftToRight ? r : l;
                Tuple t = new Tuple(t1, new Couple(t1.getId(), t2.getId()));
                for (String s : right.getAttributes()) {
                    if (!Tuple.PHYSICAL_ID.equals(s)) t.put(s, t2.get(s));
                }
                if (optional != null && !optional.evaluate(t)) {
                    continue;
                }
                target.add(t);
            }
        }
    }
}
