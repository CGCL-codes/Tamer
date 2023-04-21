package particle;

import math.ConstantFunction;
import math.DoubleFunction;
import math.ParametricFunction;
import math.ZeroParametricFunction;

public class ParticleFactory {

    private ParametricFunction positionFunction = new ZeroParametricFunction();

    private ParametricFunction velocityFunction = new ZeroParametricFunction();

    private DoubleFunction massFunction = new ConstantFunction(9.10938188E-31);

    private DoubleFunction chargeFunction = new ConstantFunction(1.60217646E-19);

    public ParametricFunction getPositionFunction() {
        return positionFunction;
    }

    public void setPositionFunction(ParametricFunction positionFunction) {
        this.positionFunction = positionFunction;
    }

    public ParametricFunction getVelocityFunction() {
        return velocityFunction;
    }

    public void setVelocityFunction(ParametricFunction velocityFunction) {
        this.velocityFunction = velocityFunction;
    }

    public DoubleFunction getMassFunction() {
        return massFunction;
    }

    public void setMassFunction(DoubleFunction massFunction) {
        this.massFunction = massFunction;
    }

    public DoubleFunction getChargeFunction() {
        return chargeFunction;
    }

    public void setChargeFunction(DoubleFunction chargeFunction) {
        this.chargeFunction = chargeFunction;
    }

    /**
	 * Create some particles
	 * @return Particle2D[]
	 */
    public Particle2D[] createParticles(int particleCount) {
        Particle2D[] pa = new Particle2D[particleCount];
        double dt = 1.0 / particleCount;
        int i = 0;
        for (double t = 0; t <= 1 && i < particleCount; t += dt) {
            pa[i] = new Particle2D(positionFunction.getPoint(t));
            pa[i].setMass(massFunction.getValue(t));
            pa[i].setCharge(chargeFunction.getValue(t));
            pa[i].setVelocity(velocityFunction.getPoint(t));
            i++;
        }
        return pa;
    }
}
