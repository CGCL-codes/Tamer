package fr.lig.sigma.astral.core.operators.relational;

import fr.lig.sigma.astral.AstralEngine;
import fr.lig.sigma.astral.common.AttributeSet;
import fr.lig.sigma.astral.common.AxiomNotVerifiedException;
import fr.lig.sigma.astral.common.Batch;
import fr.lig.sigma.astral.common.Tuple;
import fr.lig.sigma.astral.common.structure.Stream;
import fr.lig.sigma.astral.core.common.AbstractPojoFactory;
import fr.lig.sigma.astral.operators.Operator;
import fr.lig.sigma.astral.operators.StreamOperator;
import fr.lig.sigma.astral.operators.relational.UnaryRelationalOperation;
import fr.lig.sigma.astral.query.AstralCore;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.log4j.Logger;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Provides
public class UnaryRelationalStreamOperator extends StreamOperator implements Operator, Stream {

    private LinkedList<UnaryRelationalOperation> unaryOperations = new LinkedList<UnaryRelationalOperation>();

    @Property(mandatory = true)
    private List<String> attributes;

    @Property(mandatory = true)
    private List<Map<String, Object>> operations;

    @Requires(id = "in")
    private Stream in;

    @Requires(id = "core")
    private AstralCore core;

    private static final Logger log = Logger.getLogger(UnaryRelationalStreamOperator.class);

    @Override
    public int getMaxInputs() {
        return 1;
    }

    public void prepare() throws Exception {
        for (Map<String, Object> op : operations) unaryOperations.add(core.getOf().getUnaryFactory().instanciateUnaryOperator(op));
        String name = in.getName();
        Set<String> attributes = new AttributeSet(this.attributes);
        for (UnaryRelationalOperation op : unaryOperations) {
            name = op.getOperationName() + "/" + name;
        }
        setOutput(createNewFrom(in, attributes, name));
        addInput(in, true);
    }

    @Invalidate
    private void destroy() {
        for (UnaryRelationalOperation op : unaryOperations) AbstractPojoFactory.dispose(op);
    }

    @Override
    public void processEvent(Batch b) throws AxiomNotVerifiedException {
        while (in.hasNext(b)) {
            Tuple res = in.pop();
            for (UnaryRelationalOperation op : unaryOperations) {
                try {
                    res = op.compute(res);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (res == null) break;
            }
            if (res != null) put(res, b.getId());
        }
    }

    @Override
    public String toString() {
        String res = "";
        for (UnaryRelationalOperation op : unaryOperations) res += op.getOperationName() + "\n";
        return res.substring(0, res.length() - 1);
    }
}
