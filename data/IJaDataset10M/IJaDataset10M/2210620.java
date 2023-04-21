package org.softmed.jops.operators;

import org.softmed.jops.Particle;
import org.softmed.jops.ParticleBehaviour;

/**
 *
 * @author eu
 */
public class SizeOperator extends ParticleOperator {

    public SizeOperator(ParticleBehaviour b) {
        super(b);
    }

    @Override
    public void update(Particle part) {
        try {
            part.size = behaviour.getSize().getValueAt(part.age);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void selectValueList(ParticleBehaviour pb) {
        valueList = pb.getSize();
    }
}
