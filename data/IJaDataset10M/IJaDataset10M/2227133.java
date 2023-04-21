package fr.lig.sigma.astral.core.operators.streamer;

import fr.lig.sigma.astral.common.AttributeSet;
import fr.lig.sigma.astral.common.AxiomNotVerifiedException;
import fr.lig.sigma.astral.common.Batch;
import fr.lig.sigma.astral.common.Tuple;
import fr.lig.sigma.astral.common.structure.DynamicRelation;
import fr.lig.sigma.astral.common.structure.Stream;
import fr.lig.sigma.astral.common.structure.TupleSet;
import fr.lig.sigma.astral.operators.Operator;
import fr.lig.sigma.astral.operators.StreamOperator;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import java.util.List;

/**
 * @author Loic Petit
 */
@Component
@Provides
public class DynamicIs extends StreamOperator implements Operator {

    @Requires(id = "in")
    private DynamicRelation in;

    private int id = 0;

    @Property
    private List<String> attributes;

    @Override
    public int getMaxInputs() {
        return 1;
    }

    @Override
    public void prepare() throws Exception {
        setOutput((Stream) entityFactory.instanciateEntity("StreamQueueImpl", "IS(" + in.getName() + ")", new AttributeSet(attributes)));
        addInput(in, true);
    }

    @Override
    public void processEvent(Batch b) throws AxiomNotVerifiedException {
        TupleSet insertedTuples = in.getInsertedTuples(b);
        TupleSet toCommit = entityFactory.instanciateTupleSet(getAttributes());
        for (Tuple t : insertedTuples) {
            Tuple t1 = new Tuple(t, id++);
            t1.put(Tuple.TIMESTAMP_ATTRIBUTE, b.getTimestamp());
            toCommit.add(t1);
        }
        putAll(toCommit, b.getId());
    }
}
