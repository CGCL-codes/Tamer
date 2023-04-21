package net.sourceforge.jds.mass.exact;

import java.util.Map;
import net.sourceforge.jds.hypothesis.IHypothesis;
import net.sourceforge.jds.hypothesis.JointHypothesis;
import net.sourceforge.jds.mass.IJointMassFunction;
import net.sourceforge.jds.mass.IMassFunction;

/**
 * Represents a mass function defined over the space AxB.
 * 
 * @author Thomas Reineking
 *
 */
public class JointMassFunction<A extends IHypothesis<A>, B extends IHypothesis<B>> extends AbstractMassFunction<JointHypothesis<A, B>, JointMassFunction<A, B>> implements IJointMassFunction<A, B, JointMassFunction<A, B>> {

    public JointMassFunction() {
        super();
    }

    public JointMassFunction(JointHypothesis<A, B> hypothesis) {
        super(hypothesis);
    }

    public JointMassFunction(A hypothesis1, B hypothesis2) {
        super(new JointHypothesis<A, B>(hypothesis1, hypothesis2));
    }

    @Override
    protected JointMassFunction<A, B> createMassFunction() {
        return new JointMassFunction<A, B>();
    }

    @Override
    public void projectLeft(IMassFunction<A, ?> dest) {
        for (Map.Entry<JointHypothesis<A, B>, Double> entry : entries.entrySet()) dest.add(entry.getKey().h1, entry.getValue());
    }

    @Override
    public void projectRight(IMassFunction<B, ?> dest) {
        for (Map.Entry<JointHypothesis<A, B>, Double> entry : entries.entrySet()) dest.add(entry.getKey().h2, entry.getValue());
    }
}
