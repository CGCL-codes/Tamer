package org.softmed.jops.space;

import org.openmali.vecmath2.Vector3f;
import org.softmed.jops.InfoObject;
import org.softmed.jops.Particle;
import org.softmed.jops.random.RandomGenerator;

/**
 * 
 * @author eu
 */
public abstract class GeneratorSpace extends InfoObject {

    protected Vector3f cposition;

    protected RandomGenerator generator = new RandomGenerator();

    public void rebuild() {
        generator = new RandomGenerator();
    }

    public abstract void generate(Particle part);

    public abstract void update(float life);

    public abstract void setResolution(int resolution);

    public abstract void recompile();

    public abstract void reset();
}
